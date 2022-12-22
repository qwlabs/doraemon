package com.qwlabs.exceptions;

public class CodeException extends ServiceException {
    public CodeException() {
    }

    public CodeException(String message) {
        super(message);
    }

    public CodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeException(Throwable cause) {
        super(cause);
    }

    public CodeException(String message,
                         Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
