package com.qwlabs.excel.read;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.util.Map;
import java.util.Optional;

public class ExcelColumnDataReader implements ExcelDataReader {
    private final ReadSheet sheet;

    public ExcelColumnDataReader(ReadSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Map<Integer, ReadCellData<?>> by(int index) {
        return sheet.loadColumnByRow(index);
    }

    @Override
    public ReadCellData<?> by(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.loadRowByColumn(rowIndex))
            .map(rowCells -> rowCells.get(columnIndex))
            .orElse(null);
    }

    @Override
    public boolean isTail(int dataIndex) {
        return sheet.maxColumnIndex() < dataIndex;
    }
}
