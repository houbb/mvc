package com.github.houbb.mvc.core.exception;

/**
 * @author binbin.hou
 * @since 0.0.1
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
