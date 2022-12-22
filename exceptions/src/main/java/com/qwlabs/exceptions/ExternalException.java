package com.qwlabs.exceptions;

public class ExternalException extends ServiceException {

    public ExternalException() {
    }

    public ExternalException(String message) {
        super(message);
    }

    public ExternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalException(Throwable cause) {
        super(cause);
    }

    public ExternalException(String message,
                             Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
