package com.qwlabs.excel.read.converters;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.lang.reflect.Field;

public class IntegerCellConverter implements CellConverter<Integer> {
    @Override
    public Integer convert(Field field, ReadCellData<?> cellData) {
        return null;
    }
}
