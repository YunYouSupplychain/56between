package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：序列号库存扩展Entity
 *
 * @auther: Jianhua on 2019/1/28
 */
public class BanQinWmInvSerialEntity extends BanQinWmInvSerial {
    // 交易类型
    private String serialTranType;
    // 单据类型
    private String orderType;
    // 单据号
    private String orderNo;
    // 行号
    private String lineNo;
    // 交易ID
    private String tranId;
    private Date lotAtt01;
    private Date lotAtt02;
    private Date lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private String skuName;
    private String ownerName;
    private String orgName;

    public String getSerialTranType() {
        return serialTranType;
    }

    public void setSerialTranType(String serialTranType) {
        this.serialTranType = serialTranType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(Date lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
