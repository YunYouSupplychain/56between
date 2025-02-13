package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmRouteScopeObj;

public class TmRouteScopeObjEntity extends TmRouteScopeObj {
    private static final long serialVersionUID = -2605434191657958961L;
    private String transportObjName;
    private String transportObjAddress;

    public TmRouteScopeObjEntity() {
    }

    public TmRouteScopeObjEntity(String id) {
        super(id);
    }

    public TmRouteScopeObjEntity(String routeScopeCode, String orgId) {
        super(routeScopeCode, orgId);
    }

    public TmRouteScopeObjEntity(String routeScopeCode, String scopeType, String orgId) {
        super(routeScopeCode, scopeType, orgId);
    }

    public String getTransportObjName() {
        return transportObjName;
    }

    public void setTransportObjName(String transportObjName) {
        this.transportObjName = transportObjName;
    }

    public String getTransportObjAddress() {
        return transportObjAddress;
    }

    public void setTransportObjAddress(String transportObjAddress) {
        this.transportObjAddress = transportObjAddress;
    }
}
