package com.yunyou.modules.wms.inbound.entity.extend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单导入Entity
 * @author WMJ
 * @version 2020-02-18
 */
public class BanQinWmAsnImportEntity extends BanQinWmAsnHeader {
    private String orderNo;
    private String skuCode;
    private String skuName;
    private String uom;
    private BigDecimal qty;
    private String reserveCode;		// 上架库位指定规则
    private String paRule;		    // 上架规则
    private String rotationRule;	// 库存周转规则
    private String allocRule;		// 分配规则
    private String traceId;		    // 跟踪号
    private String planRcvLoc;
    private BigDecimal csQty;
    private BigDecimal plQty;
    private BigDecimal cdprTi;
    private BigDecimal cdprHi;
    private String tempLevel;
    private String skuDef1;
    private String skuDef2;
    private String skuDef3;
    private String skuDef4;
    private String skuDef5;
    private String skuDef6;
    private String skuDef7;
    private String skuDef8;
    private String skuDef9;
    private String skuDef10;
    private String skuDef11;
    private String skuDef12;
    private String skuDef13;
    private String skuDef14;
    private String skuDef15;
    private Date inboundTime;
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

    @ExcelField(title = "订单号**必填 根据该字段合并生成订单", align = 2, sort = 1)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @ExcelField(title="入库单类型**必填 填写英文\nPI:普通入库 AI:调拨入库\nRI:退货入库 EI:换货入库\nADI:调整入库 CI:盘点入库", dictType="", align=2, sort=2)
    @Override
    public String getAsnType() {
        return super.getAsnType();
    }

    @Override
    public void setAsnType(String asnType) {
        super.setAsnType(asnType);
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
    @ExcelField(title="预计到货时间从", align=2, sort=4)
    @Override
    public Date getFmEta() {
        return super.getFmEta();
    }

    @Override
    public void setFmEta(Date fmEta) {
        super.setFmEta(fmEta);
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="预计到货时间到", align=2, sort=5)
    @Override
    public Date getToEta() {
        return super.getToEta();
    }

    @Override
    public void setToEta(Date toEta) {
        super.setToEta(toEta);
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

    @ExcelField(title="供应商编码", align=2, sort=7)
    @Override
    public String getSupplierCode() {
        return super.getSupplierCode();
    }

    @Override
    public void setSupplierCode(String supplierCode) {
        super.setSupplierCode(supplierCode);
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

    @ExcelField(title = "商品编码**必填", align = 2, sort = 9)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "商品名称", align = 2, sort = 10)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @ExcelField(title = "数量**必填 必须是数字类型", align = 2, sort = 12)
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @ExcelField(title = "计划跟踪号", align = 2, sort = 22)
    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @ExcelField(title = "计划收货库位**必填", align = 2, sort = 23)
    public String getPlanRcvLoc() {
        return planRcvLoc;
    }

    public void setPlanRcvLoc(String planRcvLoc) {
        this.planRcvLoc = planRcvLoc;
    }

    @ExcelField(title = "生产日期**日期格式: yyyy-MM-dd", align = 2, sort = 24)
    public String getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(String lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    @ExcelField(title = "失效日期**日期格式: yyyy-MM-dd", align = 2, sort = 25)
    public String getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(String lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

    @ExcelField(title = "入库日期**日期格式: yyyy-MM-dd", align = 2, sort = 26)
    public String getLotAtt03() {
        return lotAtt03;
    }

    public void setLotAtt03(String lotAtt03) {
        this.lotAtt03 = lotAtt03;
    }

    @ExcelField(title = "品质** 填写英文\nY:良品 N:不良品", align = 2, sort = 27)
    public String getLotAtt04() {
        return lotAtt04;
    }

    public void setLotAtt04(String lotAtt04) {
        this.lotAtt04 = lotAtt04;
    }

    @ExcelField(title = "批次属性5", align = 2, sort = 28)
    public String getLotAtt05() {
        return lotAtt05;
    }

    public void setLotAtt05(String lotAtt05) {
        this.lotAtt05 = lotAtt05;
    }

    @ExcelField(title = "批次属性6", align = 2, sort = 29)
    public String getLotAtt06() {
        return lotAtt06;
    }

    public void setLotAtt06(String lotAtt06) {
        this.lotAtt06 = lotAtt06;
    }

    @ExcelField(title = "批次属性7", align = 2, sort = 30)
    public String getLotAtt07() {
        return lotAtt07;
    }

    public void setLotAtt07(String lotAtt07) {
        this.lotAtt07 = lotAtt07;
    }

    @ExcelField(title = "批次属性8", align = 2, sort = 31)
    public String getLotAtt08() {
        return lotAtt08;
    }

    public void setLotAtt08(String lotAtt08) {
        this.lotAtt08 = lotAtt08;
    }

    @ExcelField(title = "批次属性9", align = 2, sort = 32)
    public String getLotAtt09() {
        return lotAtt09;
    }

    public void setLotAtt09(String lotAtt09) {
        this.lotAtt09 = lotAtt09;
    }

    @ExcelField(title = "批次属性10", align = 2, sort = 33)
    public String getLotAtt10() {
        return lotAtt10;
    }

    public void setLotAtt10(String lotAtt10) {
        this.lotAtt10 = lotAtt10;
    }

    @ExcelField(title = "批次属性11", align = 2, sort = 34)
    public String getLotAtt11() {
        return lotAtt11;
    }

    public void setLotAtt11(String lotAtt11) {
        this.lotAtt11 = lotAtt11;
    }

    @ExcelField(title = "批次属性12", align = 2, sort = 35)
    public String getLotAtt12() {
        return lotAtt12;
    }

    public void setLotAtt12(String lotAtt12) {
        this.lotAtt12 = lotAtt12;
    }

    @ExcelField(title = "包装单位**必填 填写英文\nEA:件 IP:内包装\nCS:箱 PL:托盘\nOT:大包装", align = 2, sort = 11)
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @ExcelField(title = "上架库位指定规则**必填 填写英文\nMAN:人工指定库位 PA:上架时计算库位\nRCV:收货时计算库位 B_RCV_ONE:收货前计算库位-一步收货\nB_RCV_TWO:收货前计算库位-两步收货", align = 2, sort = 18)
    public String getReserveCode() {
        return reserveCode;
    }

    public void setReserveCode(String reserveCode) {
        this.reserveCode = reserveCode;
    }

    @ExcelField(title = "上架规则", align = 2, sort = 19)
    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    @ExcelField(title = "库存周转规则", align = 2, sort = 20)
    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }

    @ExcelField(title = "分配规则", align = 2, sort = 21)
    public String getAllocRule() {
        return allocRule;
    }

    public void setAllocRule(String allocRule) {
        this.allocRule = allocRule;
    }

    @ExcelField(title = "商品自定义1", align = 2, sort = 36)
    public String getSkuDef1() {
        return skuDef1;
    }

    public void setSkuDef1(String skuDef1) {
        this.skuDef1 = skuDef1;
    }

    @ExcelField(title = "商品自定义2", align = 2, sort = 37)
    public String getSkuDef2() {
        return skuDef2;
    }

    public void setSkuDef2(String skuDef2) {
        this.skuDef2 = skuDef2;
    }

    @ExcelField(title = "商品自定义3", align = 2, sort = 38)
    public String getSkuDef3() {
        return skuDef3;
    }

    public void setSkuDef3(String skuDef3) {
        this.skuDef3 = skuDef3;
    }

    @ExcelField(title = "商品自定义4", align = 2, sort = 39)
    public String getSkuDef4() {
        return skuDef4;
    }

    public void setSkuDef4(String skuDef4) {
        this.skuDef4 = skuDef4;
    }

    @ExcelField(title = "商品自定义5", align = 2, sort = 40)
    public String getSkuDef5() {
        return skuDef5;
    }

    public void setSkuDef5(String skuDef5) {
        this.skuDef5 = skuDef5;
    }

    @ExcelField(title = "商品自定义6", align = 2, sort = 41)
    public String getSkuDef6() {
        return skuDef6;
    }

    public void setSkuDef6(String skuDef6) {
        this.skuDef6 = skuDef6;
    }

    @ExcelField(title = "商品自定义7", align = 2, sort = 42)
    public String getSkuDef7() {
        return skuDef7;
    }

    public void setSkuDef7(String skuDef7) {
        this.skuDef7 = skuDef7;
    }

    @ExcelField(title = "商品自定义8", align = 2, sort = 43)
    public String getSkuDef8() {
        return skuDef8;
    }

    public void setSkuDef8(String skuDef8) {
        this.skuDef8 = skuDef8;
    }

    @ExcelField(title = "商品自定义9", align = 2, sort = 44)
    public String getSkuDef9() {
        return skuDef9;
    }

    public void setSkuDef9(String skuDef9) {
        this.skuDef9 = skuDef9;
    }

    @ExcelField(title = "商品自定义10", align = 2, sort = 45)
    public String getSkuDef10() {
        return skuDef10;
    }

    public void setSkuDef10(String skuDef10) {
        this.skuDef10 = skuDef10;
    }

    @ExcelField(title = "商品自定义11", align = 2, sort = 46)
    public String getSkuDef11() {
        return skuDef11;
    }

    public void setSkuDef11(String skuDef11) {
        this.skuDef11 = skuDef11;
    }

    @ExcelField(title = "商品自定义12", align = 2, sort = 47)
    public String getSkuDef12() {
        return skuDef12;
    }

    public void setSkuDef12(String skuDef12) {
        this.skuDef12 = skuDef12;
    }

    @ExcelField(title = "商品自定义13", align = 2, sort = 48)
    public String getSkuDef13() {
        return skuDef13;
    }

    public void setSkuDef13(String skuDef13) {
        this.skuDef13 = skuDef13;
    }

    @ExcelField(title = "商品自定义14", align = 2, sort = 49)
    public String getSkuDef14() {
        return skuDef14;
    }

    public void setSkuDef14(String skuDef14) {
        this.skuDef14 = skuDef14;
    }

    @ExcelField(title = "商品自定义15", align = 2, sort = 50)
    public String getSkuDef15() {
        return skuDef15;
    }

    public void setSkuDef15(String skuDef15) {
        this.skuDef15 = skuDef15;
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getInboundTime() {
        return inboundTime;
    }

    public void setInboundTime(Date inboundTime) {
        this.inboundTime = inboundTime;
    }

    @ExcelField(title = "箱含量**必须是数字类型", align = 2, sort = 16)
    public BigDecimal getCsQty() {
        return csQty;
    }

    public void setCsQty(BigDecimal csQty) {
        this.csQty = csQty;
    }

    @ExcelField(title = "托盘含量**必须是数字类型", align = 2, sort = 17)
    public BigDecimal getPlQty() {
        return plQty;
    }

    public void setPlQty(BigDecimal plQty) {
        this.plQty = plQty;
    }

    @ExcelField(title = "TI**必须是数字类型", align = 2, sort = 13)
    public BigDecimal getCdprTi() {
        return cdprTi;
    }

    public void setCdprTi(BigDecimal cdprTi) {
        this.cdprTi = cdprTi;
    }

    @ExcelField(title = "HI**必须是数字类型", align = 2, sort = 14)
    public BigDecimal getCdprHi() {
        return cdprHi;
    }

    public void setCdprHi(BigDecimal cdprHi) {
        this.cdprHi = cdprHi;
    }

    @ExcelField(title = "商品温层**填写英文\nNT:常温 TT:恒温\nRT:冷藏 FT:冷冻", align = 2, sort = 15)
    public String getTempLevel() {
        return tempLevel;
    }

    public void setTempLevel(String tempLevel) {
        this.tempLevel = tempLevel;
    }
}
