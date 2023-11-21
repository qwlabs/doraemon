package com.qwlabs.excel.read.converters;

import com.alibaba.excel.metadata.data.ReadCellData;

import java.lang.reflect.Field;

public class StringCellConverter implements CellConverter<String> {
    @Override
    public String convert(Field field, ReadCellData<?> cellData) {
        return null;
    }
}
