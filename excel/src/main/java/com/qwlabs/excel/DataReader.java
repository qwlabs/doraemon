package com.qwlabs.excel;

import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import com.google.common.collect.Maps;
import com.qwlabs.lang.InputStreams;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.Strings;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataReader {
    private final InputStream inputStream;

    protected DataReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<Map<String, String>> read(SheetReadOptions options,
                                          Map<String, String> headMapping,
                                          int headRowIndex) {
        return read(options, headMapping, headRowIndex, headRowIndex + 1, null);
    }

    public List<Map<String, String>> read(SheetReadOptions options,
                                          Map<String, String> headMapping,
                                          int headRowIndex,
                                          String rowField) {
        return read(options, headMapping, headRowIndex, headRowIndex + 1, rowField);
    }

    public List<Map<String, String>> read(SheetReadOptions options,
                                          Map<String, String> headMapping,
                                          int headRowStartIndex,
                                          int dataRowStartIndex,
                                          String rowField) {
        if (headRowStartIndex >= dataRowStartIndex) {
            throw Messages.INSTANCE.conflictHeadAndDataRowIndex(headRowStartIndex, dataRowStartIndex);
        }
        InputStreams.reset(inputStream);
        var listener = new Listener(headMapping, headRowStartIndex, dataRowStartIndex, rowField);
        options.config(FastExcel.read(inputStream, listener))
            .headRowNumber(0)
            .doRead();
        return listener.getData();
    }

    private static final class Listener extends AnalysisEventListener<Map<Integer, String>> {
        private final Map<String, String> headMapping;
        private final Map<Integer, String> indexFields;
        private final List<Map<String, String>> data;
        private final int headRowStartIndex;
        private final int dataRowStartIndex;
        private final String rowField;

        public Listener(Map<String, String> headMapping,
                        int headRowStartIndex,
                        int dataRowStartIndex,
                        String rowField) {
            this.headMapping = headMapping;
            this.headRowStartIndex = headRowStartIndex;
            this.dataRowStartIndex = dataRowStartIndex;
            this.rowField = rowField;
            this.indexFields = Maps.newHashMap();
            this.data = Lists.newArrayList();

        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            var currentRowIndex = context.readRowHolder().getRowIndex();
            if (currentRowIndex == headRowStartIndex) {
                this.indexFields.putAll(ExcelHelper.lookupHeaders(data, headMapping));
            } else if (currentRowIndex >= dataRowStartIndex) {
                this.data.add(buildRowData(data, context));
            }
        }

        private Map<String, String> buildRowData(Map<Integer, String> data, AnalysisContext context) {
            var hasRowField = Strings.isNotEmpty(rowField);
            Map<String, String> rowData = Maps.newLinkedHashMapWithExpectedSize(data.size() + (hasRowField ? 1 : 0));
            data.forEach((columnIndex, value) -> {
                var field = this.indexFields.get(columnIndex);
                if (Objects.nonNull(field)) {
                    rowData.putIfAbsent(field, value);
                }
            });
            if (hasRowField) {
                rowData.put(rowField, String.valueOf(ExcelHelper.toNaturalSequence(context.readRowHolder().getRowIndex())));
            }
            return rowData;
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {

        }

        public List<Map<String, String>> getData() {
            return data;
        }
    }
}
