package com.yunyou.modules.bms.common;

import com.yunyou.common.exception.GlobalException;

/**
 * 描述：BMS异常类
 *
 * @author Jianhua
 * @version 2019/5/28
 */
public class BmsException extends GlobalException {
    private static final long serialVersionUID = 4898074934716184828L;

    public BmsException() {
    }

    public BmsException(String message) {
        super(message);
    }

    public BmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BmsException(Throwable cause) {
        super(cause);
    }

    public BmsException(String code, String message) {
        super(code, message);
    }

    public BmsException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
