package com.qwlabs.excel.read;

import com.qwlabs.excel.Excel;
import com.qwlabs.excel.annotations.SheetNo;
import com.qwlabs.excel.shared.ExcelStream;
import org.apache.commons.codec.Resources;
import org.junit.jupiter.api.Test;

public class RowExcelReaderTest {

    @Test
    void should_read_row() {
        var stream = ExcelStream.of(Resources.getInputStream("read/row.xlsx"), "row.xlsx");
        var reader = Excel.reader(stream);
        var result1 = reader.read(
            ReadOptions.<Record>builder()
                .dataStartIndex(1)
                .type(Record.class)
                .build()
        );

        var result2 = reader.read(
            ReadOptions.<Record>builder()
                .dataStartIndex(1)
                .type(Record.class)
                .build()
        );

    }


    @SheetNo
    public static class Record {
        private String idValue;
        private String stringValue;
        private String integerValue;
        private String bigDecimalValue;
        private String doubleValue;
        private String longValue;
        private String booleanValue;
        private String floatValue;
    }

}
