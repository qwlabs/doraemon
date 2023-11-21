package com.qwlabs.excel.exceptions;

public class ExcelReadException extends ExcelException {
    public ExcelReadException() {
    }

    public ExcelReadException(String message) {
        super(message);
    }

    public ExcelReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReadException(Throwable cause) {
        super(cause);
    }

    public ExcelReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
