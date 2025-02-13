package com.yunyou.modules.tms.common.map.exception;

import com.yunyou.common.exception.GlobalException;

/**
 * 路线规划异常
 */
public class RoutePlanningException extends GlobalException {
    private static final long serialVersionUID = -8362765480955351380L;

    public RoutePlanningException() {
    }

    public RoutePlanningException(String message) {
        super(message);
    }

    public RoutePlanningException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoutePlanningException(Throwable cause) {
        super(cause);
    }

    public RoutePlanningException(String code, String message) {
        super(code, message);
    }

    public RoutePlanningException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
