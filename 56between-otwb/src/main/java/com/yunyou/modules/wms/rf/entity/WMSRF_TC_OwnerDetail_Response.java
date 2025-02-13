package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_TC_OwnerDetail_Response implements Serializable {
    // 货主编码
    private String ownerCode;
    // 货主中文名
    private String ownerName;

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
