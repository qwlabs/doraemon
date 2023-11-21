package com.qwlabs.excel.read;

import com.google.common.collect.Maps;
import com.qwlabs.excel.read.fields.ReadFieldLoader;
import com.qwlabs.excel.validation.Violations;

import java.lang.reflect.Field;
import java.util.Map;

public class ReadFieldLoaders<T> {
    private final ReadOptions<T> options;
    private final ExcelDataReader dataReader;
    private final Map<Field, ReadFieldLoader> fieldLoaders;

    protected ReadFieldLoaders(ReadOptions<T> options,
                               ExcelDataReader dataReader) {
        this.options = options;
        this.dataReader = dataReader;
        this.fieldLoaders = Maps.newHashMap();
    }

    public void init(Violations violations) {
        for (Field field : options.getType().getFields()) {

        }


        options.getType();
    }



//        this.headers = Maps.newHashMap();
//        loadByIndex(options.getHeadIndex()).forEach((index, cellData) -> {
//            this.headers.putIfAbsent(cellData.getStringValue(), index);
//        });
}
