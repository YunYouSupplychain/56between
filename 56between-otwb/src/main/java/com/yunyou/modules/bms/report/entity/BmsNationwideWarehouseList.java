package com.yunyou.modules.bms.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 全国仓网清单
 */
public class BmsNationwideWarehouseList extends DataEntity<BmsNationwideWarehouseList> {

    private static final long serialVersionUID = 7867989793836194468L;
    // 区域编码
    private String regionCode;
    @ExcelField(title = "区域", type = 1)
    private String regionName;
    // 城市ID
    private String areaId;
    @ExcelField(title = "城市", type = 1)
    private String area;
    // 结算对象编码
    private String settleObjectCode;
    @ExcelField(title = "结算对象", type = 1)
    private String settleObjectName;
    @ExcelField(title = "仓别", type = 1)
    private String orgCode;
    @ExcelField(title = "合同类型", dictType = "BMS_CONTRACT_TYPE", type = 1)
    private String contractType;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "合同起租日", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateFm;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "合同终止日", format = DateFormatUtil.PATTERN_ISO_ON_DATE, type = 1)
    private Date effectiveDateTo;
    // 费用科目编码
    private String billSubjectCode;
    @ExcelField(title = "费用科目", type = 1)
    private String billSubjectName;
    // 品类
    private String skuClass;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    @ExcelField(title = "阶梯范围从", type = 1)
    private Double fm;
    @ExcelField(title = "阶梯范围到", type = 1)
    private Double to;
    @ExcelField(title = "单价", type = 1)
    private Double price;
    @ExcelField(title = "单位", type = 1)
    private String unit;
    // 机构ID
    private String orgId;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSettleObjectCode() {
        return settleObjectCode;
    }

    public void setSettleObjectCode(String settleObjectCode) {
        this.settleObjectCode = settleObjectCode;
    }

    public String getSettleObjectName() {
        return settleObjectName;
    }

    public void setSettleObjectName(String settleObjectName) {
        this.settleObjectName = settleObjectName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getEffectiveDateFm() {
        return effectiveDateFm;
    }

    public void setEffectiveDateFm(Date effectiveDateFm) {
        this.effectiveDateFm = effectiveDateFm;
    }

    public Date getEffectiveDateTo() {
        return effectiveDateTo;
    }

    public void setEffectiveDateTo(Date effectiveDateTo) {
        this.effectiveDateTo = effectiveDateTo;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
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

    public Double getFm() {
        return fm;
    }

    public void setFm(Double fm) {
        this.fm = fm;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
