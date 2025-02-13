package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 结算商品Entity
 */
public class SysBmsSku extends DataEntity<SysBmsSku> {

    private static final long serialVersionUID = 1L;
    // 货主编码
    private String ownerCode;
    // 商品编码
    private String skuCode;
    // 商品名称
    private String skuName;
    // 规格
    private String skuSpec;
    // 长
    private Double length;
    // 宽
    private Double width;
    // 高
    private Double height;
    // 毛重
    private Double grossWeight;
    // 净重
    private Double netWeight;
    // 体积
    private Double volume;
    // 商品类型
    private String priceType;
    // 件-换算比例
    private Double eaQuantity;
    // 小包装-换算比例
    private Double ipQuantity;
    // 箱-换算比例
    private Double csQuantity;
    // 托-换算比例
    private Double plQuantity;
    // 大包装-换算比例
    private Double otQuantity;
    // 数据套
    private String dataSet;
    // 商品简称
    private String skuShortName;
    // 客户商品编码
    private String skuCustomerCode;
    // 商品型号
    private String skuModel;
    // 商品类别
    private String skuType;
    // 销售单位
    private String salesUnit;
    // 门店订购倍数
    private Integer storeOrderTimes;
    // 拆零标识
    private Integer detachableFlag;
    // 扣重比例
    private Double deductWeightRatio;
    // 商品称重类型
    private String skuWeighType;
    // 商品温层
    private String skuTempLayer;
    // 物品种类（sku/veh）
    private String skuClass;
    // 称重方式
    private String weighWay;
    // 皮重
    private Double tareWeight;
    // 是否进口检疫
    private String isIqc = "N";
    // 是否冰品
    private String isIces = "N";
    // 箱体积
    private Double boxVolume;
    // 检验商品类别
    private String skuCheckType;
    // 统一码
    private String unno;
    // 统一码名称
    private String unname;
    // 商品供应商列表
    private List<SysBmsSkuSupplier> skuSuppliers = Lists.newArrayList();;

    public SysBmsSku() {
        super();
    }

    public SysBmsSku(String id) {
        super(id);
    }

    public SysBmsSku(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "货主编码", align = 2, sort = 1)
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @ExcelField(title = "商品编码", align = 2, sort = 3)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "商品名称", align = 2, sort = 4)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @ExcelField(title = "规格", align = 2, sort = 5)
    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    @ExcelField(title = "长", align = 2, sort = 6)
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @ExcelField(title = "宽", align = 2, sort = 7)
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @ExcelField(title = "高", align = 2, sort = 8)
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @ExcelField(title = "毛重", align = 2, sort = 9)
    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    @ExcelField(title = "净重", align = 2, sort = 10)
    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    @ExcelField(title = "体积", align = 2, sort = 11)
    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @ExcelField(title = "商品类型", dictType = "SYS_MATERIAL_TYPE", align = 2, sort = 12)
    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    @ExcelField(title = "件-换算比例", align = 2, sort = 13)
    public Double getEaQuantity() {
        return eaQuantity;
    }

    public void setEaQuantity(Double eaQuantity) {
        this.eaQuantity = eaQuantity;
    }

    @ExcelField(title = "小包装-换算比例", align = 2, sort = 14)
    public Double getIpQuantity() {
        return ipQuantity;
    }

    public void setIpQuantity(Double ipQuantity) {
        this.ipQuantity = ipQuantity;
    }

    @ExcelField(title = "箱-换算比例", align = 2, sort = 15)
    public Double getCsQuantity() {
        return csQuantity;
    }

    public void setCsQuantity(Double csQuantity) {
        this.csQuantity = csQuantity;
    }

    @ExcelField(title = "托-换算比例", align = 2, sort = 16)
    public Double getPlQuantity() {
        return plQuantity;
    }

    public void setPlQuantity(Double plQuantity) {
        this.plQuantity = plQuantity;
    }

    @ExcelField(title = "大包装-换算比例", align = 2, sort = 17)
    public Double getOtQuantity() {
        return otQuantity;
    }

    public void setOtQuantity(Double otQuantity) {
        this.otQuantity = otQuantity;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getSkuShortName() {
        return skuShortName;
    }

    public void setSkuShortName(String skuShortName) {
        this.skuShortName = skuShortName;
    }

    public String getSkuCustomerCode() {
        return skuCustomerCode;
    }

    public void setSkuCustomerCode(String skuCustomerCode) {
        this.skuCustomerCode = skuCustomerCode;
    }

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public Integer getStoreOrderTimes() {
        return storeOrderTimes;
    }

    public void setStoreOrderTimes(Integer storeOrderTimes) {
        this.storeOrderTimes = storeOrderTimes;
    }

    public Integer getDetachableFlag() {
        return detachableFlag;
    }

    public void setDetachableFlag(Integer detachableFlag) {
        this.detachableFlag = detachableFlag;
    }

    public Double getDeductWeightRatio() {
        return deductWeightRatio;
    }

    public void setDeductWeightRatio(Double deductWeightRatio) {
        this.deductWeightRatio = deductWeightRatio;
    }

    public String getSkuWeighType() {
        return skuWeighType;
    }

    public void setSkuWeighType(String skuWeighType) {
        this.skuWeighType = skuWeighType;
    }

    public String getSkuTempLayer() {
        return skuTempLayer;
    }

    public void setSkuTempLayer(String skuTempLayer) {
        this.skuTempLayer = skuTempLayer;
    }

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public String getWeighWay() {
        return weighWay;
    }

    public void setWeighWay(String weighWay) {
        this.weighWay = weighWay;
    }

    public Double getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(Double tareWeight) {
        this.tareWeight = tareWeight;
    }

    public String getIsIqc() {
        return isIqc;
    }

    public void setIsIqc(String isIqc) {
        this.isIqc = isIqc;
    }

    public String getIsIces() {
        return isIces;
    }

    public void setIsIces(String isIces) {
        this.isIces = isIces;
    }

    public Double getBoxVolume() {
        return boxVolume;
    }

    public void setBoxVolume(Double boxVolume) {
        this.boxVolume = boxVolume;
    }

    public String getSkuCheckType() {
        return skuCheckType;
    }

    public void setSkuCheckType(String skuCheckType) {
        this.skuCheckType = skuCheckType;
    }

    public String getUnno() {
        return unno;
    }

    public void setUnno(String unno) {
        this.unno = unno;
    }

    public String getUnname() {
        return unname;
    }

    public void setUnname(String unname) {
        this.unname = unname;
    }

    public List<SysBmsSkuSupplier> getSkuSuppliers() {
        return skuSuppliers;
    }

    public void setSkuSuppliers(List<SysBmsSkuSupplier> skuSuppliers) {
        this.skuSuppliers = skuSuppliers;
    }
}