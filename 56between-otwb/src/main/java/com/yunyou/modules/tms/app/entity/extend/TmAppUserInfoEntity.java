package com.yunyou.modules.tms.app.entity.extend;

import com.yunyou.modules.tms.app.entity.TmAppUserInfo;

public class TmAppUserInfoEntity extends TmAppUserInfo {
    private static final long serialVersionUID = 773928936668397429L;

    private String transportObjName;
    private String driverName;
    private String orgName;

    public String getTransportObjName() {
        return transportObjName;
    }

    public void setTransportObjName(String transportObjName) {
        this.transportObjName = transportObjName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
