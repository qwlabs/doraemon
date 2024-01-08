package com.qwlabs.excel;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DataReaderTest {

    @Test
    void should_read_with_out_row() {
        var inputStream = read("data-reader.xlsx");
        var headMapping = Map.of(
            "所属航司", "airline",
            "机队名称", "feet",
            "飞机号", "tailNo",
            "飞机型号", "airplaneModel"
        );
        var data = Excels.dataReader(inputStream).read(
            SheetReadOptions.builder().sheetNo(0).build(),
            headMapping,
            0
        );

        assertThat(data.size(), is(1));
        var row = data.get(0);
        assertThat(row.size(), is(16));
        assertThat(row.get("airline"), is("我是航司"));
        assertThat(row.get("feet"), is("我是机队名称"));
        assertThat(row.get("tailNo"), is("我是飞机号"));
        assertThat(row.get("airplaneModel"), is("我是飞机型号"));
        assertThat(row.get("新增字段1"), is("我是新增字段1"));
        assertThat(row.get("新增字段2"), is("我是新增字段2"));
        assertThat(row.get("新增字段3"), is("我是新增字段3"));
        assertThat(row.get("发动机序列号"), is("我是发动机序列号"));
    }

    @Test
    void should_read_with_row() {
        var inputStream = read("data-reader.xlsx");
        var headMapping = Map.of(
            "所属航司", "airline",
            "机队名称", "feet",
            "飞机号", "tailNo",
            "飞机型号", "airplaneModel"
        );
        var data = Excels.dataReader(inputStream).read(
            SheetReadOptions.builder().sheetNo(0).build(),
            headMapping,
            0,
            "_row"
        );

        assertThat(data.size(), is(1));
        var row = data.get(0);
        assertThat(row.size(), is(17));
        assertThat(row.get("_row"), is("2"));
        assertThat(row.get("airline"), is("我是航司"));
        assertThat(row.get("feet"), is("我是机队名称"));
        assertThat(row.get("tailNo"), is("我是飞机号"));
        assertThat(row.get("airplaneModel"), is("我是飞机型号"));
        assertThat(row.get("新增字段1"), is("我是新增字段1"));
        assertThat(row.get("新增字段2"), is("我是新增字段2"));
        assertThat(row.get("新增字段3"), is("我是新增字段3"));
        assertThat(row.get("发动机序列号"), is("我是发动机序列号"));
    }

    private InputStream read(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
