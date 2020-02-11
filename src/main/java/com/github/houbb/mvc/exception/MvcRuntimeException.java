package com.github.houbb.mvc.exception;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class MvcRuntimeException extends RuntimeException {

    public MvcRuntimeException() {
    }

    public MvcRuntimeException(String message) {
        super(message);
    }

    public MvcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MvcRuntimeException(Throwable cause) {
        super(cause);
    }

    public MvcRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
