package com.qwlabs.excel;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import com.qwlabs.lang.InputStreams;

import java.io.InputStream;
import java.util.Map;

public class SingleRowReader {
    private final InputStream inputStream;

    protected SingleRowReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<Integer, String> read(SheetReadOptions options, int rowIndex) {
        InputStreams.reset(inputStream);
        var listener = new Listener(rowIndex);
        options.config(EasyExcel.read(inputStream, listener))
            .headRowNumber(0)
            .doRead();
        return ExcelHelper.cleanup(listener.getData());
    }

    private static final class Listener extends AnalysisEventListener<Map<Integer, String>> {
        private Map<Integer, String> data;
        private int rowIndex;

        private Listener(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        public boolean hasNext(AnalysisContext context) {
            return context.readRowHolder().getRowIndex() < rowIndex;
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            this.data = headMap;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            if (context.readRowHolder().getRowIndex() == rowIndex) {
                this.data = data;
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public Map<Integer, String> getData() {
            return data;
        }
    }

}
