package com.yunyou.modules.wms.inventory.entity;

/**
 * 库存盘点Entity
 * @Author WMJ
 * @version  2019-06-10
 */
public class BanQinWmCountHeaderEntity extends BanQinWmCountHeader {
    private String skuName;
    private String ownerName;
    private String zoneName;
    private String areaName;
    private String isCreateAdCheck;
    private String orgName;

    private String[] ownerCodes;
    private String[] skuCodes;
    private String[] areaCodes;
    private String[] zoneCodes;

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIsCreateAdCheck() {
        return isCreateAdCheck;
    }

    public void setIsCreateAdCheck(String isCreateAdCheck) {
        this.isCreateAdCheck = isCreateAdCheck;
    }

    public String[] getOwnerCodes() {
        return ownerCodes;
    }

    public void setOwnerCodes(String[] ownerCodes) {
        this.ownerCodes = ownerCodes;
    }

    public String[] getSkuCodes() {
        return skuCodes;
    }

    public void setSkuCodes(String[] skuCodes) {
        this.skuCodes = skuCodes;
    }

    public String[] getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String[] areaCodes) {
        this.areaCodes = areaCodes;
    }

    public String[] getZoneCodes() {
        return zoneCodes;
    }

    public void setZoneCodes(String[] zoneCodes) {
        this.zoneCodes = zoneCodes;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
