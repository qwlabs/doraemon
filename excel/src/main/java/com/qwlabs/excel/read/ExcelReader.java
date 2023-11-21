package com.qwlabs.excel.read;


import com.qwlabs.excel.messages.ExcelMessages;
import com.qwlabs.excel.shared.ExcelStream;

public class ExcelReader {
    private final ExcelStream excelStream;
    private final ReadWorkbook workbook;


    public ExcelReader(ExcelStream excelStream) {
        this.excelStream = excelStream;
        this.workbook = new ReadWorkbook(excelStream.getInput());
    }

    public <T> ReadResult<T> read(ReadOptions<T> options) {
        this.workbook.read();
        ModelLoader<T> modelReader = new ModelLoader<>(options,
            ExcelReadUtils.createDataReader(
                this.workbook.sheet(options.getType()),
                options.getReadMode()));
        modelReader.load();
        return ReadResult.<T>builder()
            .data(modelReader.getData())
            .build();
    }

    public static ExcelReader of(ExcelStream excelStream) {
        return new ExcelReader(excelStream);
    }
}
