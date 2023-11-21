package com.qwlabs.excel;

import com.qwlabs.excel.read.ExcelReader;
import com.qwlabs.excel.shared.ExcelStream;

public class Excel {

    public static ExcelReader reader(ExcelStream excelStream) {
        return ExcelReader.of(excelStream);
    }
}
