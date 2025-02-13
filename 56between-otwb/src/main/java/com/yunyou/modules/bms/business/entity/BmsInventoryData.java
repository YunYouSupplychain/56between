package com.yunyou.modules.bms.business.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 描述：库存数据
 */
public class BmsInventoryData extends DataEntity<BmsInventoryData> {

    private static final long serialVersionUID = 1L;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "库存日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date invDate;
    @ExcelField(title = "货主编码")
    private String ownerCode;
    @ExcelField(title = "货主名称")
    private String ownerName;
    @ExcelField(title = "供应商编码")
    private String supplierCode;
    @ExcelField(title = "供应商名称")
    private String supplierName;
    @ExcelField(title = "商品编码")
    private String skuCode;
    @ExcelField(title = "商品名称")
    private String skuName;
    @ExcelField(title = "品类")
    private String skuClass;
    @ExcelField(title = "批次号")
    private String lotNo;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "生产日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt01;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "失效日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt02;
    @JsonFormat(pattern = DateFormatUtil.PATTERN_ISO_ON_DATE)
    @ExcelField(title = "入库日期", format = DateFormatUtil.PATTERN_ISO_ON_DATE)
    private Date lotAtt03;
    @ExcelField(title = "批次属性04")
    private String lotAtt04;
    @ExcelField(title = "批次属性05")
    private String lotAtt05;
    @ExcelField(title = "批次属性06")
    private String lotAtt06;
    @ExcelField(title = "批次属性07")
    private String lotAtt07;
    @ExcelField(title = "批次属性08")
    private String lotAtt08;
    @ExcelField(title = "批次属性09")
    private String lotAtt09;
    @ExcelField(title = "批次属性10")
    private String lotAtt10;
    @ExcelField(title = "批次属性11")
    private String lotAtt11;
    @ExcelField(title = "批次属性12")
    private String lotAtt12;
    @ExcelField(title = "库位编码")
    private String locCode;
    @ExcelField(title = "托盘跟踪号")
    private String traceId;
    // 入库数量
    private Double inQty;
    // 入库箱数
    private Double inQtyCs;
    // 入库托数
    private Double inQtyPl;
    // 出库数量
    private Double outQty;
    // 出库箱数
    private Double outQtyCs;
    // 出库托数
    private Double outQtyPl;
    // 期初数量
    private Double beginQty;
    // 期初箱数
    private Double beginQtyCs;
    // 期初托数
    private Double beginQtyPl;
    @ExcelField(title = "数量")
    private Double endQty;
    @ExcelField(title = "箱数")
    private Double endQtyCs;
    @ExcelField(title = "托数")
    private Double endQtyPl;
    @ExcelField(title = "重量")
    private Double weight;
    @ExcelField(title = "体积")
    private Double volume;
    // 机构ID
    private String orgId;
    // 数据来源
    private String dataSources;
    // 是否参与计费
    private String isFee;

    // 库存模式
    private String businessModel;
    // 项目编码
    private String projectCode;
    // 项目名称
    private String projectName;

    public BmsInventoryData() {
        super();
    }

    public BmsInventoryData(String id) {
        super(id);
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Date getLotAtt01() {
        return lotAtt01;
    }

    public void setLotAtt01(Date lotAtt01) {
        this.lotAtt01 = lotAtt01;
    }

    public Date getLotAtt02() {
        return lotAtt02;
    }

    public void setLotAtt02(Date lotAtt02) {
        this.lotAtt02 = lotAtt02;
    }

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

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Double getInQty() {
        return inQty;
    }

    public void setInQty(Double inQty) {
        this.inQty = inQty;
    }

    public Double getInQtyCs() {
        return inQtyCs;
    }

    public void setInQtyCs(Double inQtyCs) {
        this.inQtyCs = inQtyCs;
    }

    public Double getInQtyPl() {
        return inQtyPl;
    }

    public void setInQtyPl(Double inQtyPl) {
        this.inQtyPl = inQtyPl;
    }

    public Double getOutQty() {
        return outQty;
    }

    public void setOutQty(Double outQty) {
        this.outQty = outQty;
    }

    public Double getOutQtyCs() {
        return outQtyCs;
    }

    public void setOutQtyCs(Double outQtyCs) {
        this.outQtyCs = outQtyCs;
    }

    public Double getOutQtyPl() {
        return outQtyPl;
    }

    public void setOutQtyPl(Double outQtyPl) {
        this.outQtyPl = outQtyPl;
    }

    public Double getBeginQty() {
        return beginQty;
    }

    public void setBeginQty(Double beginQty) {
        this.beginQty = beginQty;
    }

    public Double getBeginQtyCs() {
        return beginQtyCs;
    }

    public void setBeginQtyCs(Double beginQtyCs) {
        this.beginQtyCs = beginQtyCs;
    }

    public Double getBeginQtyPl() {
        return beginQtyPl;
    }

    public void setBeginQtyPl(Double beginQtyPl) {
        this.beginQtyPl = beginQtyPl;
    }

    public Double getEndQty() {
        return endQty;
    }

    public void setEndQty(Double endQty) {
        this.endQty = endQty;
    }

    public Double getEndQtyCs() {
        return endQtyCs;
    }

    public void setEndQtyCs(Double endQtyCs) {
        this.endQtyCs = endQtyCs;
    }

    public Double getEndQtyPl() {
        return endQtyPl;
    }

    public void setEndQtyPl(Double endQtyPl) {
        this.endQtyPl = endQtyPl;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDataSources() {
        return dataSources;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIsFee() {
        return isFee;
    }

    public void setIsFee(String isFee) {
        this.isFee = isFee;
    }
}