package com.qwlabs.excel.read;

import com.qwlabs.excel.messages.ExcelMessages;

public class ExcelReadUtils {
    private ExcelReadUtils() {
    }

    protected static ExcelDataReader createDataReader(ReadSheet sheet, ReadMode readMode) {
        if (readMode == ReadMode.ROW) {
            return new ExcelRowDataReader(sheet);
        } else if (readMode == ReadMode.COLUMN) {
            return new ExcelColumnDataReader(sheet);
        }
        throw ExcelMessages.INSTANCE.dataReaderNotFound(readMode);
    }
}
