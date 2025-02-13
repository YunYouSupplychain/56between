package com.yunyou.modules.tms.common;

import com.yunyou.common.exception.GlobalException;

public class TmsException extends GlobalException {
    protected static final long serialVersionUID = 1L;

    public TmsException() {
    }

    public TmsException(String message) {
        super(message);
    }

    public TmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TmsException(Throwable cause) {
        super(cause);
    }

    public TmsException(String code, String message) {
        super(code, message);
    }

    public TmsException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
