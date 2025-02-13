package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysTmsTransportObjScope;

public class SysTmsTransportObjScopeEntity extends SysTmsTransportObjScope {
    private static final long serialVersionUID = 5897129117496772131L;

    private String transportObjName;// 业务对象名称
    private String transportScopeName;// 业务服务范围名称

    public String getTransportObjName() {
        return transportObjName;
    }

    public void setTransportObjName(String transportObjName) {
        this.transportObjName = transportObjName;
    }

    public String getTransportScopeName() {
        return transportScopeName;
    }

    public void setTransportScopeName(String transportScopeName) {
        this.transportScopeName = transportScopeName;
    }
}
