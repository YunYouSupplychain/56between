package com.yunyou.modules.wms.inventory.entity.extend;

import com.yunyou.modules.wms.inventory.entity.BanQinWmMvHeader;

import java.util.List;

public class BanQinWmMvEntity extends BanQinWmMvHeader {

    private String ownerName;
    private String orgName;
    private List<BanQinWmMvDetailEntity> wmMvDetailList;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<BanQinWmMvDetailEntity> getWmMvDetailList() {
        return wmMvDetailList;
    }

    public void setWmMvDetailList(List<BanQinWmMvDetailEntity> wmMvDetailList) {
        this.wmMvDetailList = wmMvDetailList;
    }
}
