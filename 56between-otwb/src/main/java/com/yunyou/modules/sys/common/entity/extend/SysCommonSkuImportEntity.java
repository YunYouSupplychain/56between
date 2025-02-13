package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.common.utils.excel.annotation.ExcelField;

import java.math.BigDecimal;

/**
 * 商品导入实体
 */
public class SysCommonSkuImportEntity {
    @ExcelField(title = "数据套编码**必填", type = 2)
    private String dataSet;
    @ExcelField(title = "货主编码**必填", type = 2)
    private String ownerCode;
    @ExcelField(title = "商品编码**必填", type = 2)
    private String skuCode;
    @ExcelField(title = "商品名称**必填", type = 2)
    private String skuName;
    @ExcelField(title = "客户商品编码", type = 2)
    private String skuCustomerCode;
    @ExcelField(title = "收发货包装单位**必填 填写英文\nEA:件\nIP:内包装\nCS:箱\nPL:托盘\nOT:大包装", type = 2)
    private String uom;
    @ExcelField(title = "规格", type = 2)
    private String skuSpec;
    @ExcelField(title = "箱含量**必须是数字类型", type = 2)
    private BigDecimal csQty;
    @ExcelField(title = "托盘含量**必须是数字类型", type = 2)
    private BigDecimal plQty;
    @ExcelField(title = "TI**必须是数字类型", type = 2)
    private BigDecimal ti;
    @ExcelField(title = "HI**必须是数字类型", type = 2)
    private BigDecimal hi;
    @ExcelField(title = "保质期**只能填写数字", type = 2)
    private Double shelfLife;
    @ExcelField(title = "商品温层**填写英文\nNT:常温 TT:恒温\nRT:冷藏 FT:冷冻", type = 2)
    private String tempLevel;
    @ExcelField(title = "课别", type = 2)
    private String skuType;
    @ExcelField(title = "称重类型", type = 2)
    private String skuWeighType;
    @ExcelField(title = "称重方式**填写\n10:标件称重 20:非标称重", type = 2)
    private String weighWay;
    @ExcelField(title = "销售单位", type = 2)
    private String salesUnit;
    @ExcelField(title = "扣重比例**只能填写数字", type = 2)
    private Double deductWeightRatio;
    @ExcelField(title = "上架库位指定规则**必填 填写编码\n MAN:人工指定库位\n PA:上架时计算库位\n RCV:收货时计算库位\n B_RCV_ONE:收货前计算库位-一步收货\n B_RCV_TWO:收货前计算库位-两步收货\n", type = 2)
    private String reserveCode;
    @ExcelField(title = "上架规则", type = 2)
    private String paRule;
    @ExcelField(title = "库存周转规则", type = 2)
    private String rotationRule;
    @ExcelField(title = "分配规则", type = 2)
    private String allocRule;
    @ExcelField(title = "上架库区", type = 2)
    private String paZone;
    @ExcelField(title = "上架库位", type = 2)
    private String paLoc;
    @ExcelField(title = "批次属性", type = 2)
    private String lotCode;
    @ExcelField(title = "默认供应商编码", type = 2)
    private String supplierCode;
    @ExcelField(title = "默认条码", type = 2)
    private String barCode;
    @ExcelField(title = "单价**只能填写数字", type = 2)
    private BigDecimal price;
    @ExcelField(title = "毛重**只能填写数字", type = 2)
    private Double grossWeight;
    @ExcelField(title = "净重**只能填写数字", type = 2)
    private Double netWeight;
    @ExcelField(title = "体积**只能填写数字", type = 2)
    private Double volume;
    @ExcelField(title = "长**只能填写数字", type = 2)
    private Double length;
    @ExcelField(title = "宽**只能填写数字", type = 2)
    private Double width;
    @ExcelField(title = "高**只能填写数字", type = 2)
    private Double height;

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuCustomerCode() {
        return skuCustomerCode;
    }

    public void setSkuCustomerCode(String skuCustomerCode) {
        this.skuCustomerCode = skuCustomerCode;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public BigDecimal getCsQty() {
        return csQty;
    }

    public void setCsQty(BigDecimal csQty) {
        this.csQty = csQty;
    }

    public BigDecimal getPlQty() {
        return plQty;
    }

    public void setPlQty(BigDecimal plQty) {
        this.plQty = plQty;
    }

    public BigDecimal getTi() {
        return ti;
    }

    public void setTi(BigDecimal ti) {
        this.ti = ti;
    }

    public BigDecimal getHi() {
        return hi;
    }

    public void setHi(BigDecimal hi) {
        this.hi = hi;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getTempLevel() {
        return tempLevel;
    }

    public void setTempLevel(String tempLevel) {
        this.tempLevel = tempLevel;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSkuWeighType() {
        return skuWeighType;
    }

    public void setSkuWeighType(String skuWeighType) {
        this.skuWeighType = skuWeighType;
    }

    public String getWeighWay() {
        return weighWay;
    }

    public void setWeighWay(String weighWay) {
        this.weighWay = weighWay;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public Double getDeductWeightRatio() {
        return deductWeightRatio;
    }

    public void setDeductWeightRatio(Double deductWeightRatio) {
        this.deductWeightRatio = deductWeightRatio;
    }

    public String getReserveCode() {
        return reserveCode;
    }

    public void setReserveCode(String reserveCode) {
        this.reserveCode = reserveCode;
    }

    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }

    public String getAllocRule() {
        return allocRule;
    }

    public void setAllocRule(String allocRule) {
        this.allocRule = allocRule;
    }

    public String getPaZone() {
        return paZone;
    }

    public void setPaZone(String paZone) {
        this.paZone = paZone;
    }

    public String getPaLoc() {
        return paLoc;
    }

    public void setPaLoc(String paLoc) {
        this.paLoc = paLoc;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
