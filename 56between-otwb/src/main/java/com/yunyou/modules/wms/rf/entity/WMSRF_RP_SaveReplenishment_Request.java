package com.yunyou.modules.wms.rf.entity;

import java.io.Serializable;

public class WMSRF_RP_SaveReplenishment_Request implements Serializable {
    // 用户Id
    private String userId;
    // 主键
    private String id;
    // 包装单位
    private String uom;
    // 补货包装数量
    private Double qtyRpUom;
    // 补货EA数量
    private Double qtyRpEa;
    // 目标库位编码
    private String toLoc;

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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtyRpUom() {
        return qtyRpUom;
    }

    public void setQtyRpUom(Double qtyRpUom) {
        this.qtyRpUom = qtyRpUom;
    }

    public Double getQtyRpEa() {
        return qtyRpEa;
    }

    public void setQtyRpEa(Double qtyRpEa) {
        this.qtyRpEa = qtyRpEa;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }
}
