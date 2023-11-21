package com.qwlabs.excel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.google.common.collect.Maps;
import com.qwlabs.excel.annotations.SheetName;
import com.qwlabs.excel.annotations.SheetNo;
import com.qwlabs.excel.messages.ExcelMessages;
import com.qwlabs.lang.Annotations;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.alibaba.excel.enums.CellExtraTypeEnum.MERGE;

@Slf4j
public class ReadWorkbook {
    private final InputStream inputStream;
    private final Map<Integer, ReadSheet> sheetByNo;
    private final Map<String, ReadSheet> sheetByName;
    private final Map<String, Integer> sheetNoByName;
    private final Map<Integer, String> sheetNameByNo;
    private boolean read;

    public ReadWorkbook(InputStream inputStream) {
        this.inputStream = inputStream;
        this.read = false;
        this.sheetByNo = Maps.newHashMap();
        this.sheetByName = Maps.newHashMap();
        this.sheetNoByName = Maps.newHashMap();
        this.sheetNameByNo = Maps.newHashMap();
    }

    public void put(AnalysisContext context, ReadSheet sheet) {
        var sheetNo = context.readSheetHolder().getSheetNo();
        var sheetName = context.readSheetHolder().getSheetName();
        this.sheetNoByName.put(sheetName, sheetNo);
        this.sheetNameByNo.put(sheetNo, sheetName);
        this.sheetByNo.put(sheetNo, sheet);
        this.sheetByName.put(sheetName, sheet);
    }

    public synchronized void read() {
        if (read) {
            return;
        }
        EasyExcel.read(inputStream, new RawReadListener(this))
            .autoTrim(true)
            .headRowNumber(0)
            .autoCloseStream(true)
            .charset(StandardCharsets.UTF_8)
            .mandatoryUseInputStream(false)
            .useDefaultListener(false)
            .ignoreEmptyRow(true)
            .extraRead(MERGE)
            .doReadAll();
        this.read = true;
    }

    public Optional<ReadSheet> sheet(int sheetNo) {
        return Optional.ofNullable(sheetByNo.get(sheetNo));
    }

    public Optional<ReadSheet> sheet(String sheetName) {
        return Optional.ofNullable(sheetByName.get(sheetName));
    }


    public <T> ReadSheet sheet(Class<T> type) {
        var sheetNo = Annotations.getAnnotation(type, SheetNo.class);
        if (Objects.nonNull(sheetNo)) {
            return sheet(sheetNo.value())
                .orElseThrow(() -> ExcelMessages.INSTANCE.sheetNoNotFound(sheetNo.value()));
        }
        var sheetName = Annotations.getAnnotation(type, SheetName.class);
        if (Objects.nonNull(sheetName)) {
            return sheet(sheetName.value())
                .orElseThrow(() -> ExcelMessages.INSTANCE.sheetNameNotFound(sheetName.value()));
        }
        return sheet(0)
            .orElseThrow(() -> ExcelMessages.INSTANCE.sheetNoNotFound(0));
    }

    private static class RawReadListener extends AnalysisEventListener<Map<Integer, ReadCellData<?>>> {
        private final ReadWorkbook holder;
        private ReadSheet readSheet;

        public RawReadListener(ReadWorkbook holder) {
            this.holder = holder;
        }

        @Override
        public void invoke(Map<Integer, ReadCellData<?>> data, AnalysisContext context) {
            if (readSheet == null) {
                readSheet = ReadSheet.of(context.readSheetHolder().getApproximateTotalRowNumber());
            }
            int rowIndex = context.readRowHolder().getRowIndex();
            readSheet.put(rowIndex, data);
        }

        @Override
        public void extra(CellExtra extra, AnalysisContext context) {
            readSheet.put(extra);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            holder.put(context, readSheet);
            readSheet = null;
        }
    }
}
