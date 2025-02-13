package com.yunyou.modules.sys.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品信息Entity
 */
public class SysCommonSku extends DataEntity<SysCommonSku> {

    private static final long serialVersionUID = 1L;
    private String ownerCode;// 货主编码
    private String skuCode;// 商品编码
    private String skuName;// 商品名称
    private String skuShortName;// 商品简称
    private String skuForeignName;// 商品外语名称
    private String skuSpec;// 规格
    private String packCode;// 包装代码
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date filingTime;// 建档时间
    private BigDecimal price;// 单价
    private Double grossWeight;// 毛重
    private Double netWeight;// 净重
    private Double volume;// 体积
    private Double length;// 长
    private Double width;// 宽
    private Double height;// 高
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    private String tempLevel;// 商品温层
    private String skuClass;// 品类
    private String skuCustomerCode;// 客户商品编码
    private String unit;// 单位

    // SMS
    private String skuModel;// 商品型号
    private String skuType;// 商品课别
    private String salesUnit;// 销售单位
    private String storageType;// 存储类别
    private Integer storeOrderTimes;// 门店订购倍数
    private Integer detachableFlag;// 拆零标识
    private Double deductWeightRatio;// 扣重比例
    private String skuWeighType;// 商品称重类型
    private String lotAttr;// 批次属性
    private String weighWay;// 称重方式
    private Double tareWeight;//皮重
    private String isIqc;//是否进口检疫
    private String isIces;//是否冰品
    private Double boxVolume;//箱体积
    private String skuCheckType;//检疫商品类别
    private String purchaseType;//采购类型
    private Double om;// 理论mop
    private Double poOm;// 采购mop
    private String poClass;// 采购分类

    // WMS
    private String barCode;// 商品条码
    private String supBarCode;// 供应商商品条码
    private String lotCode;// 批次属性编码
    private String abc;// 商品ABC
    private String groupCode;// 商品组编码
    private String materialCode;// 物料编码
    private String rcvUom;// 缺省收货单位
    private String shipUom;// 缺省发货单位
    private String printUom;// 缺省打印单位
    private Double maxLimit;// 库存上限
    private Double minLimit;// 库存下限
    private String isValidity;// 是否做效期控制
    private Double shelfLife;// 保质期
    private String lifeType;// 周期类型（生产日期、失效日期）
    private Double inLifeDays;// 入库效期(天数)
    private Double outLifeDays;// 出库效期(天数)
    private String isOverRcv;// 是否允许超收（Y,N）
    private Double overRcvPct;// 超收百分比
    private String paZone;// 上架库区
    private String paLoc;// 上架库位
    private String reserveCode;// 上架库位指定规则
    private String paRule;// 上架规则
    private String rotationRule;// 库存周转规则
    private String allocRule;// 分配规则
    private String cycleCode;// 循环级别
    private String cdClass;// 越库级别
    private String style;// 款号
    private String color;// 颜色
    private String skuSize;// 尺码
    private String isDg;// 是否危险品
    private String dgClass;// 危险品等级
    private String unno;// 危险品编号
    private String isCold;// 是否温控
    private Double minTemp;// 最低温度
    private Double maxTemp;// 最高温度
    private String hsCode;// 海关商品编码(HSCODE)
    private String isSerial;// 是否序列号管理
    private String isParent;// 是否为父件
    private String isQc;// 是否质检管理
    private String qcPhase;// 质检阶段
    private String qcRule;// 质检规则
    private String itemGroupCode;// 质检项组编码
    private String rateGroup;// 费率组编码
    private Double periodOfValidity;// 商品效期
    private String validityUnit;// 效期单位
    private String stockCurId;// 采购币别
    private String quickCode;// 快速录入码
    private String formCode;// 商品形态（固态、液态）
    private String emergencyTel;// 应急电话
    private Date effectiveDate;// 生效日期
    private Date expirationDate;// 失效日期
    private Double flashPoint;// 闪点
    private Double burningPoint;// 燃点
    private String provideCustCode;// 制造商代码
    private String materialLevel;// 层级
    @Deprecated
    private String typeCode;// 类型
    private List<SysCommonSkuSupplier> skuSupplierList = Lists.newArrayList();
    private List<SysCommonSkuBarcode> skuBarcodeList = Lists.newArrayList();
    private List<SysCommonSkuLoc> skuLocList = Lists.newArrayList();

    // 扩展字段
    private String dataSetName;// 数据套名称
    private String ownerName;// 货主名称
    private String packFormat;// 包装规格
    private String lotName;// 批次属性名称
    private String rcvUomName;
    private String shipUomName;
    private String printUomName;
    private String paRuleName;
    private String rotationRuleName;
    private String allocRuleName;
    private String qcRuleName;
    private String paZoneName;
    private String cycleName;
    private String itemGroupName;
    private String skuClassName;
    private List<String> ids;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    public SysCommonSku() {
        super();
    }

    public SysCommonSku(String dataSet) {
        super();
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

    public String getSkuShortName() {
        return skuShortName;
    }

    public void setSkuShortName(String skuShortName) {
        this.skuShortName = skuShortName;
    }

    public String getSkuForeignName() {
        return skuForeignName;
    }

    public void setSkuForeignName(String skuForeignName) {
        this.skuForeignName = skuForeignName;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public Date getFilingTime() {
        return filingTime;
    }

    public void setFilingTime(Date filingTime) {
        this.filingTime = filingTime;
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

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPackFormat() {
        return packFormat;
    }

    public void setPackFormat(String packFormat) {
        this.packFormat = packFormat;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSupBarCode() {
        return supBarCode;
    }

    public void setSupBarCode(String supBarCode) {
        this.supBarCode = supBarCode;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getRcvUom() {
        return rcvUom;
    }

    public void setRcvUom(String rcvUom) {
        this.rcvUom = rcvUom;
    }

    public String getShipUom() {
        return shipUom;
    }

    public void setShipUom(String shipUom) {
        this.shipUom = shipUom;
    }

    public String getPrintUom() {
        return printUom;
    }

    public void setPrintUom(String printUom) {
        this.printUom = printUom;
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public String getIsValidity() {
        return isValidity;
    }

    public void setIsValidity(String isValidity) {
        this.isValidity = isValidity;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getLifeType() {
        return lifeType;
    }

    public void setLifeType(String lifeType) {
        this.lifeType = lifeType;
    }

    public Double getInLifeDays() {
        return inLifeDays;
    }

    public void setInLifeDays(Double inLifeDays) {
        this.inLifeDays = inLifeDays;
    }

    public Double getOutLifeDays() {
        return outLifeDays;
    }

    public void setOutLifeDays(Double outLifeDays) {
        this.outLifeDays = outLifeDays;
    }

    public String getIsOverRcv() {
        return isOverRcv;
    }

    public void setIsOverRcv(String isOverRcv) {
        this.isOverRcv = isOverRcv;
    }

    public Double getOverRcvPct() {
        return overRcvPct;
    }

    public void setOverRcvPct(Double overRcvPct) {
        this.overRcvPct = overRcvPct;
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

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public String getCdClass() {
        return cdClass;
    }

    public void setCdClass(String cdClass) {
        this.cdClass = cdClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public String getIsDg() {
        return isDg;
    }

    public void setIsDg(String isDg) {
        this.isDg = isDg;
    }

    public String getDgClass() {
        return dgClass;
    }

    public void setDgClass(String dgClass) {
        this.dgClass = dgClass;
    }

    public String getUnno() {
        return unno;
    }

    public void setUnno(String unno) {
        this.unno = unno;
    }

    public String getIsCold() {
        return isCold;
    }

    public void setIsCold(String isCold) {
        this.isCold = isCold;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }

    public String getQcPhase() {
        return qcPhase;
    }

    public void setQcPhase(String qcPhase) {
        this.qcPhase = qcPhase;
    }

    public String getQcRule() {
        return qcRule;
    }

    public void setQcRule(String qcRule) {
        this.qcRule = qcRule;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getRateGroup() {
        return rateGroup;
    }

    public void setRateGroup(String rateGroup) {
        this.rateGroup = rateGroup;
    }

    public Double getPeriodOfValidity() {
        return periodOfValidity;
    }

    public void setPeriodOfValidity(Double periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    public String getValidityUnit() {
        return validityUnit;
    }

    public void setValidityUnit(String validityUnit) {
        this.validityUnit = validityUnit;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStockCurId() {
        return stockCurId;
    }

    public void setStockCurId(String stockCurId) {
        this.stockCurId = stockCurId;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }

    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(Double flashPoint) {
        this.flashPoint = flashPoint;
    }

    public Double getBurningPoint() {
        return burningPoint;
    }

    public void setBurningPoint(Double burningPoint) {
        this.burningPoint = burningPoint;
    }

    public String getProvideCustCode() {
        return provideCustCode;
    }

    public void setProvideCustCode(String provideCustCode) {
        this.provideCustCode = provideCustCode;
    }

    public String getTempLevel() {
        return tempLevel;
    }

    public void setTempLevel(String tempLevel) {
        this.tempLevel = tempLevel;
    }

    public String getMaterialLevel() {
        return materialLevel;
    }

    public void setMaterialLevel(String materialLevel) {
        this.materialLevel = materialLevel;
    }

    public String getSkuCustomerCode() {
        return skuCustomerCode;
    }

    public void setSkuCustomerCode(String skuCustomerCode) {
        this.skuCustomerCode = skuCustomerCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
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

    public String getSkuClass() {
        return skuClass;
    }

    public void setSkuClass(String skuClass) {
        this.skuClass = skuClass;
    }

    public String getLotAttr() {
        return lotAttr;
    }

    public void setLotAttr(String lotAttr) {
        this.lotAttr = lotAttr;
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

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public Double getOm() {
        return om;
    }

    public void setOm(Double om) {
        this.om = om;
    }

    public Double getPoOm() {
        return poOm;
    }

    public void setPoOm(Double poOm) {
        this.poOm = poOm;
    }

    public String getPoClass() {
        return poClass;
    }

    public void setPoClass(String poClass) {
        this.poClass = poClass;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getRcvUomName() {
        return rcvUomName;
    }

    public void setRcvUomName(String rcvUomName) {
        this.rcvUomName = rcvUomName;
    }

    public String getShipUomName() {
        return shipUomName;
    }

    public void setShipUomName(String shipUomName) {
        this.shipUomName = shipUomName;
    }

    public String getPrintUomName() {
        return printUomName;
    }

    public void setPrintUomName(String printUomName) {
        this.printUomName = printUomName;
    }

    public String getPaZoneName() {
        return paZoneName;
    }

    public void setPaZoneName(String paZoneName) {
        this.paZoneName = paZoneName;
    }

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public String getRotationRuleName() {
        return rotationRuleName;
    }

    public void setRotationRuleName(String rotationRuleName) {
        this.rotationRuleName = rotationRuleName;
    }

    public String getAllocRuleName() {
        return allocRuleName;
    }

    public void setAllocRuleName(String allocRuleName) {
        this.allocRuleName = allocRuleName;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getQcRuleName() {
        return qcRuleName;
    }

    public void setQcRuleName(String qcRuleName) {
        this.qcRuleName = qcRuleName;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public String getSkuClassName() {
        return skuClassName;
    }

    public void setSkuClassName(String skuClassName) {
        this.skuClassName = skuClassName;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<SysCommonSkuSupplier> getSkuSupplierList() {
        return skuSupplierList;
    }

    public void setSkuSupplierList(List<SysCommonSkuSupplier> skuSupplierList) {
        this.skuSupplierList = skuSupplierList;
    }

    public List<SysCommonSkuBarcode> getSkuBarcodeList() {
        return skuBarcodeList;
    }

    public void setSkuBarcodeList(List<SysCommonSkuBarcode> skuBarcodeList) {
        this.skuBarcodeList = skuBarcodeList;
    }

    public List<SysCommonSkuLoc> getSkuLocList() {
        return skuLocList;
    }

    public void setSkuLocList(List<SysCommonSkuLoc> skuLocList) {
        this.skuLocList = skuLocList;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}