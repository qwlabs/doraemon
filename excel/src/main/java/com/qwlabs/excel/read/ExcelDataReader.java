package com.qwlabs.excel.read;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.util.Map;

public interface ExcelDataReader {
    Map<Integer, ReadCellData<?>> by(int index);
    ReadCellData<?> by(int rowIndex, int columnIndex);
    boolean isTail(int dataIndex);
}
