package com.qwlabs.excel;

import com.qwlabs.lang.InputStreams;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SingleRowReaderTest {
    @Test
    void should_read_once() {
        var inputStream = read("single-row-reader.xlsx");
        var data = Excels.singleRowReader(inputStream).read(
            SheetReadOptions.builder().sheetNo(0).build(),
            0
        );
        valid(data);
    }

    @Test
    void should_read_twice() {
        var inputStream = InputStreams.toRepeatableRead(read("single-row-reader.xlsx"));

        var data1 = Excels.singleRowReader(inputStream).read(
            SheetReadOptions.builder().sheetNo(0).build(),
            0
        );
        var data2 = Excels.singleRowReader(inputStream).read(
            SheetReadOptions.builder().sheetNo(0).build(),
            0
        );

        valid(data1);
        valid(data2);
    }

    private void valid(Map<Integer, String> data) {
        assertThat(data.size(), is(13));
        assertThat(data.get(0), is("所属航司"));
        assertThat(data.get(1), is("机队名称"));
        assertThat(data.get(2), is("飞机号"));
        assertThat(data.get(3), is("飞机型号"));
        assertThat(data.get(4), is("发动机型号"));
        assertThat(data.get(5), is("发动机序列号"));
        assertThat(data.get(6), is("航站"));
        assertThat(data.get(8), is("ATA章节"));
        assertThat(data.get(9), is("故障描述"));
        assertThat(data.get(10), is("处置措施"));
        assertThat(data.get(11), is("故障发生时间"));
        assertThat(data.get(12), is("故障记录时间"));
        assertThat(data.get(13), is("故障关闭时间"));
    }

    private InputStream read(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
