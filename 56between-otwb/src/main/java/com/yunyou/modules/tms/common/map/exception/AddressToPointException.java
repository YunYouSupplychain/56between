package com.yunyou.modules.tms.common.map.exception;

import com.yunyou.common.exception.GlobalException;

/**
 * 地址转换坐标异常
 */
public class AddressToPointException extends GlobalException {
    private static final long serialVersionUID = 2389181785272574835L;

    public AddressToPointException() {
    }

    public AddressToPointException(String message) {
        super(message);
    }

    public AddressToPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressToPointException(Throwable cause) {
        super(cause);
    }

    public AddressToPointException(String code, String message) {
        super(code, message);
    }

    public AddressToPointException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
