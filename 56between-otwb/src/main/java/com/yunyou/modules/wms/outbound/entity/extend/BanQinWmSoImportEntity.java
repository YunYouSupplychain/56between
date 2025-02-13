package com.yunyou.modules.wms.outbound.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库单导入Entity
 * @author WMJ
 * @version 2020-02-18
 */
public class BanQinWmSoImportEntity extends BanQinWmSoHeader {
    private String orderNo;
    private String skuCode;
    private String uom;
    private BigDecimal qty;
    private Date outboundTime;
    private String lotAtt01;
    private String lotAtt02;
    private String lotAtt03;
    private String lotAtt04;
    private String lotAtt05;
    private String lotAtt06;
    private String lotAtt07;
    private String lotAtt08;
    private String lotAtt09;
    private String lotAtt10;
    private String lotAtt11;
    private String lotAtt12;
    private String rotationRule;    // 库存周转规则

    @ExcelField(title = "订单号**必填 根据该字段合并生成订单", align = 2, sort = 1)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @ExcelField(title="出库单类型**必填 填写英文\nPO:普通出库 AO:调拨出库\nRO:退厂出库 EO:换货出库\nLO:领用出库 ADD:调整出库 CO:盘点出库", dictType="", align=2, sort=2)
    @Override
    public String getSoType() {
        return super.getSoType();
    }

    @Override
    public void setSoType(String asnType) {
        super.setSoType(asnType);
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ExcelField(title="订单时间**必填 日期格式: yyyy-MM-dd hh:mm:ss", align=2, sort=3)
    @Override
    public Date getOrderTime() {
        return super.getOrderTime();
    }

    @Override
    public void setOrderTime(Date orderTime) {
        super.setOrderTime(orderTime);
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="预计发货时间从", align=2, sort=4)
    @Override
    public Date getFmEtd() {
        return super.getFmEtd();
    }

    @Override
    public void setFmEtd(Date fmEtd) {
        super.setFmEtd(fmEtd);
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="预计发货时间到", align=2, sort=5)
    @Override
    public Date getToEtd() {
        return super.getToEtd();
    }

    @Override
    public void setToEtd(Date toEtd) {
        super.setToEtd(toEtd);
    }

    @ExcelField(title="货主编码**必填", align=2, sort=6)
    @Override
    public String getOwnerCode() {
        return super.getOwnerCode();
    }

    @Override
    public void setOwnerCode(String ownerCode) {
        super.setOwnerCode(ownerCode);
    }

    @ExcelField(title="承运商编码", align=2, sort=8)
    @Override
    public String getCarrierCode() {
        return super.getCarrierCode();
    }

    @Override
    public void setCarrierCode(String carrierCode) {
        super.setCarrierCode(carrierCode);
    }

    @ExcelField(title="联系人名称", align=2, sort=9)
    @Override
    public String getContactName() {
        return super.getContactName();
    }

    @Override
    public void setContactName(String contactName) {
        super.setContactName(contactName);
    }

    @ExcelField(title="联系人电话", align=2, sort=10)
    @Override
    public String getContactTel() {
        return super.getContactTel();
    }

    @Override
    public void setContactTel(String contactTel) {
        super.setContactTel(contactTel);
    }

    @ExcelField(title="联系人地址", align=2, sort=11)
    @Override
    public String getContactAddr() {
        return super.getContactAddr();
    }

    @Override
    public void setContactAddr(String contactAddr) {
        super.setContactAddr(contactAddr);
    }

    @ExcelField(title = "商品编码**必填", align = 2, sort=14)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "包装单位**必填 填写英文\nEA:件 IP:内包装\nCS:箱 PL:托盘\nOT:大包装", align = 2, sort = 15)
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @ExcelField(title = "数量**必填 必须是数字类型", align = 2, sort = 15)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @ExcelField(title = "生产日期**日期格式: yyyy-MM-dd", align = 2, sort = 16)
    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @ExcelField(title = "失效日期**日期格式: yyyy-MM-dd", align = 2, sort = 17)
    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @ExcelField(title = "入库日期**日期格式: yyyy-MM-dd", align = 2, sort = 18)
    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    @ExcelField(title = "品质** 填写英文\nY:良品 N:不良品", align = 2, sort = 19)
    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    @ExcelField(title = "批次属性5", align = 2, sort = 20)
    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    @ExcelField(title = "批次属性6", align = 2, sort = 21)
    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    @ExcelField(title = "批次属性7", align = 2, sort = 22)
    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    @ExcelField(title = "批次属性8", align = 2, sort = 23)
    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    @ExcelField(title = "批次属性9", align = 2, sort = 24)
    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    @ExcelField(title = "批次属性10", align = 2, sort = 25)
    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    @ExcelField(title = "批次属性11", align = 2, sort = 26)
    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    @ExcelField(title = "批次属性12", align = 2, sort = 27)
    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    @ExcelField(title="收货人编码", align=2, sort=12)
    @Override
    public String getConsigneeCode() {
        return super.getConsigneeCode();
    }

    @Override
    public void setConsigneeCode(String consigneeCode) {
        super.setConsigneeCode(consigneeCode);
    }

    @ExcelField(title="收货人名称", align=2, sort=13)
    @Override
    public String getConsigneeName() {
        return super.getConsigneeName();
    }

    @Override
    public void setConsigneeName(String consigneeName) {
        super.setConsigneeName(consigneeName);
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    @ExcelField(title="出库时间**必填 日期格式: yyyy-MM-dd hh:mm:ss", align = 2, sort = 26)
    public Date getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(Date outboundTime) {
        this.outboundTime = outboundTime;
    }

    @ExcelField(title = "库存周转规则", align = 2, sort = 28)
    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }
}
