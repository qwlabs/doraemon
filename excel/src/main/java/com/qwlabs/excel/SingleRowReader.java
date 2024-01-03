package com.qwlabs.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.qwlabs.lang.InputStreams;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public class SingleRowReader {
    private final InputStream inputStream;

    protected SingleRowReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<Integer, String> read(SheetReadOptions options, int rowIndex) {
        InputStreams.reset(inputStream);
        var listener = new Listener();
        options.config(EasyExcel.read(inputStream, listener))
            .headRowNumber(rowIndex + 1)
            .doRead();
        return listener.getData();
    }

    private static final class Listener extends AnalysisEventListener<Map<Integer, String>> {
        private Map<Integer, String> data;
        @Override
        public boolean hasNext(AnalysisContext context) {
            return false;
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            this.data = headMap;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {

        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            this.data = Optional.ofNullable(this.data).orElseGet(Map::of);
        }

        public Map<Integer, String> getData() {
            return data;
        }
    }

}
