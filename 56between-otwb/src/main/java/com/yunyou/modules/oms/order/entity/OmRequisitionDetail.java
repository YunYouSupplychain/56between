package com.yunyou.modules.oms.order.entity;

import com.yunyou.core.persistence.DataEntity;

import java.math.BigDecimal;

public class OmRequisitionDetail extends DataEntity<OmRequisitionDetail> {
    private static final long serialVersionUID = 8353189326021206814L;

    private String reqNo;
    private String lineNo;
    private String ownerCode;
    private String skuCode;
    private String skuSpec;
    private String uom;
    private BigDecimal qty = BigDecimal.ZERO;
    private BigDecimal taskQty = BigDecimal.ZERO;
    private String auxiliaryUom;
    private BigDecimal auxiliaryQty = BigDecimal.ZERO;
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private String orgId;

    public OmRequisitionDetail() {
    }

    public OmRequisitionDetail(String id) {
        super(id);
    }

    public OmRequisitionDetail(String reqNo, String orgId) {
        this.reqNo = reqNo;
        this.orgId = orgId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getTaskQty() {
        return taskQty;
    }

    public void setTaskQty(BigDecimal taskQty) {
        this.taskQty = taskQty;
    }

    public String getAuxiliaryUom() {
        return auxiliaryUom;
    }

    public void setAuxiliaryUom(String auxiliaryUom) {
        this.auxiliaryUom = auxiliaryUom;
    }

    public BigDecimal getAuxiliaryQty() {
        return auxiliaryQty;
    }

    public void setAuxiliaryQty(BigDecimal auxiliaryQty) {
        this.auxiliaryQty = auxiliaryQty;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
