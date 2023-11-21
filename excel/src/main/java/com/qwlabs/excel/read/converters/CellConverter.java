package com.qwlabs.excel.read.converters;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.lang.reflect.Field;

public interface CellConverter<T> {
    T convert(Field field, ReadCellData<?> cellData);
}
