package com.yunyou.modules.wms.inbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 入库日报表entity
 * @author WMJ
 * @version 2019/07/03
 */
public class BanQinWmRepInDailyQueryEntity extends DataEntity {
    private String asnNo;
    private String asnLineNo;
    private String status;
    private String asnType;
    private String logisticNo;
    private String logisticLineNo;
    private String ownerCode;
    private String ownerName;
    private String skuCode;
    private String skuName;
    private String packCode;
    private String uom;
    private Double qtyAsnEa;
    private Double qtyAsnUom;
    private String toLoc;
    private String toId;
    private Double qtyRcvEa;
    private Double qtyRcvUom;
    private Date fmEta;
    private Date toEta;
    private Date rcvTime;
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
    private String orgId;
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;
    private Date rcvTimeFrom;
    private Date rcvTimeTo;
    private Double totalQtyAsnEa;
    private Double totalQtyRcvEa;
    private String orgName;
    private Date orderTime;
    private Date beginOrderTime;		// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getAsnLineNo() {
        return asnLineNo;
    }

    public void setAsnLineNo(String asnLineNo) {
        this.asnLineNo = asnLineNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAsnType() {
        return asnType;
    }

    public void setAsnType(String asnType) {
        this.asnType = asnType;
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

    public Double getQtyAsnEa() {
        return qtyAsnEa;
    }

    public void setQtyAsnEa(Double qtyAsnEa) {
        this.qtyAsnEa = qtyAsnEa;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Double getQtyRcvEa() {
        return qtyRcvEa;
    }

    public void setQtyRcvEa(Double qtyRcvEa) {
        this.qtyRcvEa = qtyRcvEa;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFmEta() {
        return fmEta;
    }

    public void setFmEta(Date fmEta) {
        this.fmEta = fmEta;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getToEta() {
        return toEta;
    }

    public void setToEta(Date toEta) {
        this.toEta = toEta;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
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

    public Date getRcvTimeFrom() {
        return rcvTimeFrom;
    }

    public void setRcvTimeFrom(Date rcvTimeFrom) {
        this.rcvTimeFrom = rcvTimeFrom;
    }

    public Date getRcvTimeTo() {
        return rcvTimeTo;
    }

    public void setRcvTimeTo(Date rcvTimeTo) {
        this.rcvTimeTo = rcvTimeTo;
    }

    public Double getTotalQtyAsnEa() {
        return totalQtyAsnEa;
    }

    public void setTotalQtyAsnEa(Double totalQtyAsnEa) {
        this.totalQtyAsnEa = totalQtyAsnEa;
    }

    public Double getTotalQtyRcvEa() {
        return totalQtyRcvEa;
    }

    public void setTotalQtyRcvEa(Double totalQtyRcvEa) {
        this.totalQtyRcvEa = totalQtyRcvEa;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getQtyAsnUom() {
        return qtyAsnUom;
    }

    public void setQtyAsnUom(Double qtyAsnUom) {
        this.qtyAsnUom = qtyAsnUom;
    }

    public Double getQtyRcvUom() {
        return qtyRcvUom;
    }

    public void setQtyRcvUom(Double qtyRcvUom) {
        this.qtyRcvUom = qtyRcvUom;
    }

    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
