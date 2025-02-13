package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * 商品信息Entity
 */
public class SysOmsItem extends DataEntity<SysOmsItem> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;// 货主编码
    private String ownerName;// 货主名称
    private String skuCode;     // 商品编码
    private String skuName;        // 商品名称
    private String shortName;        // 商品简称
    private String customerSkuCode;        // 客户商品编码
    private String skuModel;        // 商品型号
    private String skuType;        // 商品课别
    private String packCode;        // 包装规格
    private String spec;        // 规格
    private Double grossWeight;        // 毛重
    private Double netWeight;        // 净重
    private Double volume;        // 体积
    private Double length;        // 长
    private Double width;        // 宽
    private Double height;        // 高
    private Double tare;        // 皮重
    private String unicode;        // 统一码
    private String dataSet;        // 数据套
    private String brand;        // 品牌
    private String bigSort;        // 大分类
    private String midSort;        // 中分类
    private String smallSort;    // 小分类
    private String unit;        // 单位
    private String auxiliaryUnit;    // 辅助单位

    private String skuTempLayer;// 商品温层
    private String skuClass;    // 品类

    private List<SysOmsItemBarcode> sysOmsItemBarcodeList = Lists.newArrayList();        // 子表列表

    // 查询条件
    private String codeAndName;

    public SysOmsItem() {
        super();
    }

    public SysOmsItem(String id) {
        super(id);
    }

    public SysOmsItem(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    @ExcelField(title = "货主编码", align = 2, sort = 7)
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @ExcelField(title = "货主名称", align = 2, sort = 6)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @ExcelField(title = "商品编码", align = 2, sort = 8)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ExcelField(title = "商品名称", align = 2, sort = 9)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @ExcelField(title = "商品简称", align = 2, sort = 10)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @ExcelField(title = "客户商品编码", align = 2, sort = 11)
    public String getCustomerSkuCode() {
        return customerSkuCode;
    }

    public void setCustomerSkuCode(String customerSkuCode) {
        this.customerSkuCode = customerSkuCode;
    }

    @ExcelField(title = "商品型号", align = 2, sort = 12)
    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    @ExcelField(title = "商品类别", align = 2, sort = 13)
    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    @ExcelField(title = "包装规格", align = 2, sort = 14)
    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    @ExcelField(title = "规格", align = 2, sort = 17)
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = StringEscapeUtils.unescapeHtml4(spec);// 解决HTML字符被转义问题
    }

    @ExcelField(title = "毛重", align = 2, sort = 18)
    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    @ExcelField(title = "净重", align = 2, sort = 19)
    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    @ExcelField(title = "体积", align = 2, sort = 20)
    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @ExcelField(title = "长", align = 2, sort = 21)
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @ExcelField(title = "宽", align = 2, sort = 22)
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @ExcelField(title = "高", align = 2, sort = 23)
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @ExcelField(title = "皮重", align = 2, sort = 24)
    public Double getTare() {
        return tare;
    }

    public void setTare(Double tare) {
        this.tare = tare;
    }

    @ExcelField(title = "统一码", align = 2, sort = 25)
    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<SysOmsItemBarcode> getOmItemBarcodeList() {
        return sysOmsItemBarcodeList;
    }

    public void setOmItemBarcodeList(List<SysOmsItemBarcode> sysOmsItemBarcodeList) {
        this.sysOmsItemBarcodeList = sysOmsItemBarcodeList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
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
}