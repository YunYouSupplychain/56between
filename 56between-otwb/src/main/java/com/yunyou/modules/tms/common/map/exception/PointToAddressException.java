package com.yunyou.modules.tms.common.map.exception;

import com.yunyou.common.exception.GlobalException;

public class PointToAddressException extends GlobalException {
    private static final long serialVersionUID = -3404734002529026326L;

    public PointToAddressException() {
    }

    public PointToAddressException(String message) {
        super(message);
    }

    public PointToAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointToAddressException(Throwable cause) {
        super(cause);
    }

    public PointToAddressException(String code, String message) {
        super(code, message);
    }

    public PointToAddressException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
