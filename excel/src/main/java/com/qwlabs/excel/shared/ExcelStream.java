package com.qwlabs.excel.shared;

import com.qwlabs.excel.exceptions.ExcelReadException;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class ExcelStream {
    private final InputStream input;
    private final String fileName;

    private ExcelStream(InputStream input, String fileName) {
        this.input = input;
        this.fileName = fileName;
    }

    public static ExcelStream of(InputStream input, String fileName) {
        return new ExcelStream(input, fileName);
    }

    public static ExcelStream of(File file) {
        try (InputStream inputStream = FileUtils.openInputStream(file)) {
            return of(inputStream, file.getName());
        } catch (IOException e) {
            throw new ExcelReadException("read excel file error.", e);
        }
    }
}
