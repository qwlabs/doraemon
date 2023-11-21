package com.qwlabs.excel.shared;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.qwlabs.excel.annotations.SheetNo;
import com.qwlabs.excel.annotations.SheetName;
import com.qwlabs.lang.Annotations;

public class SheetSelectors {
    public static <T> ReadSheet read(Class<T> type) {
        SheetNo sheetNo = Annotations.getAnnotation(type, SheetNo.class);
        SheetName sheetName = Annotations.getAnnotation(type, SheetName.class);
        return EasyExcel.readSheet(sheetNo.value(), sheetName.value()).build();
    }
}
