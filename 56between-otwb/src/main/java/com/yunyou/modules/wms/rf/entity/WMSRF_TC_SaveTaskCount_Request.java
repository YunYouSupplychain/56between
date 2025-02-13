package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_TC_SaveTaskCount_Request implements Serializable {
    // 用户Id
    private String userId;
    // 主键
    private String id;
    // 盘点任务ID
    private String countId;
    // 盘点单号
    private String countNo;
    // 盘点库存EA
    private Double qtyCountEa;
    // 版本号
    private Integer recVer;
    // 盘点差异数
    private Double qtyDiff;

    private String isSerial;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public Double getQtyCountEa() {
        return qtyCountEa;
    }

    public void setQtyCountEa(Double qtyCountEa) {
        this.qtyCountEa = qtyCountEa;
    }

    public Integer getRecVer() {
        return recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }

    public Double getQtyDiff() {
        return qtyDiff;
    }

    public void setQtyDiff(Double qtyDiff) {
        this.qtyDiff = qtyDiff;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }
}
