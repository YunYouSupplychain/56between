package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 出库日报表
 * @author WMJ
 * @version 2019/07/03
 */
public class BanQinWmRepOutDailyQueryEntity extends DataEntity<BanQinWmSoHeader> {
    private String soNo;
    private String lineNo;
    private String status;
    private String soType;
    private String logisticNo;
    private String logisticLineNo;
    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private String packCode;
    private String uom;
    private Double qtySoEa;
    private Double qtySoUom;
    private Double qtyShipEa;
    private Double qtyShipUom;
    private String consigneeCode;
    private String consigneeName;
    private Date fmEtd;
    private Date toEtd;
    private String orgId;
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private Date createTimeF;
    private Date createTimeT;
    private Double totalSoEaQty;
    private Double totalShipEaQty;
    private String orgName;
    private Date orderTime;
    private Date beginOrderTime;		// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public String getLogisticLineNo() {
        return logisticLineNo;
    }

    public void setLogisticLineNo(String logisticLineNo) {
        this.logisticLineNo = logisticLineNo;
    }

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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQtySoEa() {
        return qtySoEa;
    }

    public void setQtySoEa(Double qtySoEa) {
        this.qtySoEa = qtySoEa;
    }

    public Double getQtyShipEa() {
        return qtyShipEa;
    }

    public void setQtyShipEa(Double qtyShipEa) {
        this.qtyShipEa = qtyShipEa;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEtd() {
        return fmEtd;
    }

    public void setFmEtd(Date fmEtd) {
        this.fmEtd = fmEtd;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEtd() {
        return toEtd;
    }

    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public Date getCreateTimeF() {
        return createTimeF;
    }

    public void setCreateTimeF(Date createTimeF) {
        this.createTimeF = createTimeF;
    }

    public Date getCreateTimeT() {
        return createTimeT;
    }

    public void setCreateTimeT(Date createTimeT) {
        this.createTimeT = createTimeT;
    }

    public Double getTotalSoEaQty() {
        return totalSoEaQty;
    }

    public void setTotalSoEaQty(Double totalSoEaQty) {
        this.totalSoEaQty = totalSoEaQty;
    }

    public Double getTotalShipEaQty() {
        return totalShipEaQty;
    }

    public void setTotalShipEaQty(Double totalShipEaQty) {
        this.totalShipEaQty = totalShipEaQty;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getQtySoUom() {
        return qtySoUom;
    }

    public void setQtySoUom(Double qtySoUom) {
        this.qtySoUom = qtySoUom;
    }

    public Double getQtyShipUom() {
        return qtyShipUom;
    }

    public void setQtyShipUom(Double qtyShipUom) {
        this.qtyShipUom = qtyShipUom;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }
}