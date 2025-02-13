package com.yunyou.modules.wms.inventory.entity;

/**
 * 调整单单头Entity
 * @author WMJ
 * @version 2019/03/05
 */
public class BanQinWmAdHeaderEntity extends BanQinWmAdHeader {
    // 货主名称
    private String ownerName;
    //
    private String orgName;

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
}
