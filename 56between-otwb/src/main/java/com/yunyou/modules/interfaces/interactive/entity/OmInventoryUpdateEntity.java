package com.yunyou.modules.interfaces.interactive.entity;

import com.yunyou.common.utils.StringUtils;

public class OmInventoryUpdateEntity {

    private String taskNo;
    private String ownerCode;
    private String skuCode;
    private String warehouse;
    private Double shipQty;

    public boolean isNotEmpty() {
        if (StringUtils.isNotBlank(taskNo) && StringUtils.isNotBlank(skuCode) && StringUtils.isNotBlank(ownerCode) && StringUtils.isNotBlank(warehouse) && shipQty != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Double getShipQty() {
        return shipQty;
    }

    public void setShipQty(Double shipQty) {
        this.shipQty = shipQty;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
}
