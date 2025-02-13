package com.yunyou.modules.oms.common;

import com.yunyou.common.exception.GlobalException;

/**
 * 描述：
 *
 * @auther: Jianhua on 2019/5/5
 */
public class OmsException extends GlobalException {

    public OmsException() {
    }

    public OmsException(String message) {
        super(message);
    }

    public OmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmsException(Throwable cause) {
        super(cause);
    }

    public OmsException(String code, String message) {
        super(code, message);
    }

    public OmsException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
