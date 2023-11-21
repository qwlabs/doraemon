package com.qwlabs.excel.read;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.util.Map;

public class ExcelRowDataReader implements ExcelDataReader {
    private final ReadSheet sheet;

    public ExcelRowDataReader(ReadSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Map<Integer, ReadCellData<?>> by(int index) {
        return null;
    }

    @Override
    public ReadCellData<?> by(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public boolean isTail(int dataIndex) {
        return sheet.maxRowIndex() < dataIndex;
    }
}
