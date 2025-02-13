package com.yunyou.common.exception;

public class GlobalException extends RuntimeException {
    private String code;

    public GlobalException() {
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    public GlobalException(String code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
