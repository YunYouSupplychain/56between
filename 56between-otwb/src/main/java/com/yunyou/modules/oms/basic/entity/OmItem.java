package com.yunyou.modules.oms.basic.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import java.util.List;

/**
 * 商品信息Entity
 *
 * @author WMJ
 * @version 2019-04-15
 */
public class OmItem extends DataEntity<OmItem> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;// 货主编码
    private String ownerName;// 货主名称
    private String skuCode;// 商品编码
    private String skuName;// 商品名称
    private String shortName;// 商品简称
    private String spec;// 规格
    private String packCode;// 包装规格
    private String customerSkuCode;// 客户商品编码
    private String skuTempLayer;// 商品温层
    private String skuClass;// 品类
    private String skuModel;// 商品型号
    private String skuType;// 商品课别
    private String brand;// 品牌

    private Double grossWeight;// 毛重
    private Double netWeight;// 净重
    private Double volume;// 体积
    private Double length;// 长
    private Double width;// 宽
    private Double height;// 高

    private String bigSort;// 大分类
    private String midSort;// 中分类
    private String smallSort;// 小分类
    private String unit;// 单位
    private String auxiliaryUnit;// 辅助单位
    private String unicode;// 统一码
    private String orgId;// 平台编码
    private List<OmItemBarcode> omItemBarcodeList = Lists.newArrayList();// 子表列表
    @Deprecated
    private Double tare;// 皮重

    public OmItem() {
        super();
    }

    public OmItem(String id) {
        super(id);
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getCustomerSkuCode() {
        return customerSkuCode;
    }

    public void setCustomerSkuCode(String customerSkuCode) {
        this.customerSkuCode = customerSkuCode;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Double getTare() {
        return tare;
    }

    public void setTare(Double tare) {
        this.tare = tare;
    }

    public String getBigSort() {
        return bigSort;
    }

    public void setBigSort(String bigSort) {
        this.bigSort = bigSort;
    }

    public String getMidSort() {
        return midSort;
    }

    public void setMidSort(String midSort) {
        this.midSort = midSort;
    }

    public String getSmallSort() {
        return smallSort;
    }

    public void setSmallSort(String smallSort) {
        this.smallSort = smallSort;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAuxiliaryUnit() {
        return auxiliaryUnit;
    }

    public void setAuxiliaryUnit(String auxiliaryUnit) {
        this.auxiliaryUnit = auxiliaryUnit;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<OmItemBarcode> getOmItemBarcodeList() {
        return omItemBarcodeList;
    }

    public void setOmItemBarcodeList(List<OmItemBarcode> omItemBarcodeList) {
        this.omItemBarcodeList = omItemBarcodeList;
    }
}