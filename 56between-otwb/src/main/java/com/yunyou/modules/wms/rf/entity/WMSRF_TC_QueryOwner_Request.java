package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_TC_QueryOwner_Request implements Serializable {
    // 用户Id
    private String useId;
    //
    private String ownerCode;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }
}
