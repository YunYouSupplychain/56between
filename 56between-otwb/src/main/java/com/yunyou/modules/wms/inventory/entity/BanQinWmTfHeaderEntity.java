package com.yunyou.modules.wms.inventory.entity;

/**
 * 转移单单头entity
 * @author WMJ
 * @version 2019/03/06
 */
public class BanQinWmTfHeaderEntity extends BanQinWmTfHeader {
    private String ownerName;
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
