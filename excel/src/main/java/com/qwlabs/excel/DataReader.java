package com.qwlabs.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Maps;
import com.qwlabs.lang.InputStreams;
import org.apache.commons.compress.utils.Lists;

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
                                          int headRowNumber) {
        InputStreams.reset(inputStream);
        var listener = new Listener(headMapping);
        options.config(EasyExcel.read(inputStream, listener))
            .headRowNumber(headRowNumber)
            .doRead();
        return listener.getData();
    }

    private static final class Listener extends AnalysisEventListener<Map<Integer, String>> {
        private final Map<String, String> headMapping;
        private final Map<Integer, String> indexFields;
        private final List<Map<String, String>> data;

        public Listener(Map<String, String> headMapping) {
            this.headMapping = headMapping;
            this.indexFields = Maps.newHashMap();
            this.data = Lists.newArrayList();
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            this.indexFields.putAll(ExcelHeaders.lookupHeaders(headMap, headMapping));
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            this.data.add(buildRowData(data));
        }

        private Map<String, String> buildRowData(Map<Integer, String> data) {
            Map<String, String> rowData = Maps.newLinkedHashMapWithExpectedSize(data.size());
            data.forEach((rowIndex, value) -> {
                var field = this.indexFields.get(rowIndex);
                if (Objects.nonNull(field)) {
                    rowData.putIfAbsent(field, value);
                }
            });
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
