package com.qwlabs.excel;

import cn.idev.excel.enums.CellExtraTypeEnum;
import cn.idev.excel.enums.ReadDefaultReturnEnum;
import cn.idev.excel.read.builder.ExcelReaderBuilder;
import cn.idev.excel.read.builder.ExcelReaderSheetBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Getter
@Builder
@AllArgsConstructor
public class SheetReadOptions {
    private final Integer sheetNo;
    private final String sheetName;
    @Builder.Default
    private final Locale locale = Locale.getDefault();
    @Builder.Default
    private final Charset charset = StandardCharsets.UTF_8;
    @Builder.Default
    private final ReadDefaultReturnEnum readDefaultReturn = ReadDefaultReturnEnum.STRING;
    @Builder.Default
    private final boolean autoCloseStream = false;
    @Builder.Default
    private final boolean autoTrim = true;
    @Builder.Default
    private final boolean mandatoryUseInputStream = false;
    @Builder.Default
    private final boolean ignoreEmptyRow = true;

    protected ExcelReaderSheetBuilder config(ExcelReaderBuilder builder) {
        return builder
            .charset(charset)
            .locale(locale)
            .readDefaultReturn(readDefaultReturn)
            .autoCloseStream(autoCloseStream)
            .mandatoryUseInputStream(mandatoryUseInputStream)
            .ignoreEmptyRow(ignoreEmptyRow)
            .extraRead(CellExtraTypeEnum.MERGE)
            .sheet(sheetNo, sheetName)
            .autoTrim(autoTrim);
    }
}
