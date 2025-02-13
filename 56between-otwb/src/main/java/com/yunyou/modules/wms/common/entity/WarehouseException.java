package com.yunyou.modules.wms.common.entity;

import com.yunyou.common.exception.GlobalException;

public class WarehouseException extends GlobalException {
    protected static final long serialVersionUID = 1L;
    protected Object[] errorArgs = null;

    public WarehouseException(String errorCode) {
        super(errorCode);
    }

    public WarehouseException(String errorCode, Object... errorArgs) {
        super(errorCode);
        this.errorArgs = errorArgs;
    }

    public WarehouseException(String errorCode, Object[] errorArgs, Throwable cause) {
        super(errorCode);
        this.errorArgs = errorArgs;
    }

    public Object[] getErrorArgs() {
        return this.errorArgs;
    }

}
