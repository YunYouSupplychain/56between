package com.yunyou.modules.wms.basicdata.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 商品Entity
 *
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhSku extends DataEntity<BanQinCdWhSku> {
    private static final long serialVersionUID = 1L;
    private String ownerCode;        // 货主编码
    private String skuCode;        // 商品编码
    private String skuName;        // 商品名称-本地语言
    private String foreignName;        // 商品名称-外语
    private String shortName;        // 商品简称
    private String isEnable;        // 是否启用
    private String barCode;        // 商品条码
    private String supBarCode;        // 供应商商品条码
    private String packCode;        // 包装编码
    private String lotCode;        // 批次属性编码
    private String abc;        // 商品ABC
    private String groupCode;        // 商品组编码
    private String materialCode;        // 物料编码
    private Double cubic;        // 体积
    private Double grossWeight;        // 毛重
    private Double netWeight;        // 净重
    private Double price;        // 单价
    private Double length;        // 长
    private Double width;        // 宽
    private Double height;        // 高
    private String rcvUom;        // 缺省收货单位
    private String shipUom;        // 缺省发货单位
    private String printUom;        // 缺省打印单位
    private Double maxLimit;        // 库存上限
    private Double minLimit;        // 库存下限
    private String isValidity;        // 是否做效期控制
    private Double shelfLife;        // 保质期
    private String lifeType;        // 周期类型（生产日期、失效日期）
    private Double inLifeDays;        // 入库效期(天数)
    private Double outLifeDays;        // 出库效期(天数)
    private String isOverRcv;        // 是否允许超收（Y,N）
    private Double overRcvPct;        // 超收百分比
    private String paZone;        // 上架库区
    private String paLoc;        // 上架库位
    private String reserveCode;        // 上架库位指定规则
    private String paRule;        // 上架规则
    private String rotationRule;        // 库存周转规则
    private String preallocRule;        // 预配规则
    private String allocRule;        // 分配规则
    private String cycleCode;        // 循环级别
    private String cdClass;        // 越库级别
    private Date lastCountTime;        // 上次循环盘点时间
    private Date firstInTime;        // 首次入库时间
    private String style;        // 款号
    private String color;        // 颜色
    private String skuSize;        // 尺码
    private String isDg;        // 是否危险品
    private String dgClass;        // 危险品等级
    private String unno;        // 危险品编号
    private String isCold;        // 是否温控
    private Double minTemp;        // 最低温度
    private Double maxTemp;        // 最高温度
    private String hsCode;        // 海关商品编码(HSCODE)
    private String isSerial;        // 是否序列号管理
    private String isParent;        // 是否为父件
    private String isQc;        // 是否质检管理
    private String qcPhase;        // 质检阶段
    private String qcRule;        // 质检规则
    private String itemGroupCode;        // 质检项组编码
    private String rateGroup;        // 费率组编码
    private String def1;        // 自定义1
    private String def2;        // 自定义2
    private String def3;        // 自定义3
    private String def4;        // 自定义4
    private String def5;        // 自定义5
    private String def6;        // 自定义6
    private String def7;        // 自定义7
    private String def8;        // 自定义8
    private String def9;        // 自定义9
    private String def10;        // 自定义10
    private String def11;        // 自定义11
    private String def12;        // 自定义12
    private String def13;        // 自定义13
    private String def14;        // 自定义14
    private String def15;        // 自定义15
    private Double periodOfValidity;        // 商品效期
    private String validityUnit;        // 效期单位
    private String typeCode;        // 商品类型（物料类型:1普货,2危险品,3剧毒品）
    private String stockCurId;        // 采购币别
    private String quickCode;        // 快速录入码
    private String formCode;        // 商品形态（固态、液态）
    private String emergencyTel;        // 应急电话
    private Date effectiveDate;        // 生效日期
    private Date expirationDate;        // 失效日期
    private Double flashPoint;        // 闪点
    private Double burningPoint;        // 燃点
    private String provideCustCode;        // 制造商代码
    private String auditStatus;        // 审核状态
    private String tempLevel;        // 商品温层
    private String materialLevel;        // 层级
    private String materialAlias1;        // 商品别名1
    private String materialAlias2;        // 商品别名2
    private String materialAlias3;        // 商品别名3
    private String materialAlias4;        // 商品别名4
    private String materialAlias5;        // 商品别名5
    private String materialGroup1;        // MATERIAL组1
    private String materialGroup2;        // MATERIAL组2
    private String materialGroup3;        // MATERIAL组3
    private String materialGroup4;        // MATERIAL组4
    private String materialGroup5;        // MATERIAL组5
    private String materialGroup6;        // MATERIAL组6
    private String materialGroup7;        // MATERIAL组7
    private String materialGroup8;        // MATERIAL组8
    private String materialGroup9;        // MATERIAL组9
    private String orgId;        // 分公司
	private String isUnSerial;			// 是否非库存序列号管理
    private String skuCodeAndName; // 模糊查询字段
    private String qtyUnit;        // 数量单位
    private String spec;// 规格
    private String skuCustomerCode;// 客户商品编码

    public BanQinCdWhSku() {
        super();
        this.recVer = 0;
    }

    public BanQinCdWhSku(String id) {
        super(id);
    }

    @NotNull(message = "货主编码不能为空")
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    @NotNull(message = "商品编码不能为空")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @NotNull(message = "商品名称不能为空")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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

    @NotNull(message = "包装编码不能为空")
    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    @NotNull(message = "批次属性编码不能为空")
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

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @NotNull(message = "缺省收货单位不能为空")
    public String getRcvUom() {
        return rcvUom;
    }

    public void setRcvUom(String rcvUom) {
        this.rcvUom = rcvUom;
    }

    @NotNull(message = "缺省发货单位不能为空")
    public String getShipUom() {
        return shipUom;
    }

    public void setShipUom(String shipUom) {
        this.shipUom = shipUom;
    }

    @NotNull(message = "缺省打印单位不能为空")
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

    @NotNull(message = "上架库位指定规则不能为空")
    public String getReserveCode() {
        return reserveCode;
    }

    public void setReserveCode(String reserveCode) {
        this.reserveCode = reserveCode;
    }

    @NotNull(message = "上架规则不能为空")
    public String getPaRule() {
        return paRule;
    }

    public void setPaRule(String paRule) {
        this.paRule = paRule;
    }

    @NotNull(message = "库存周转规则规则不能为空")
    public String getRotationRule() {
        return rotationRule;
    }

    public void setRotationRule(String rotationRule) {
        this.rotationRule = rotationRule;
    }

    public String getPreallocRule() {
        return preallocRule;
    }

    public void setPreallocRule(String preallocRule) {
        this.preallocRule = preallocRule;
    }

    @NotNull(message = "分配规则规则不能为空")
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastCountTime() {
        return lastCountTime;
    }

    public void setLastCountTime(Date lastCountTime) {
        this.lastCountTime = lastCountTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFirstInTime() {
        return firstInTime;
    }

    public void setFirstInTime(Date firstInTime) {
        this.firstInTime = firstInTime;
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

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef10() {
        return def10;
    }

    public void setDef10(String def10) {
        this.def10 = def10;
    }

    public String getDef11() {
        return def11;
    }

    public void setDef11(String def11) {
        this.def11 = def11;
    }

    public String getDef12() {
        return def12;
    }

    public void setDef12(String def12) {
        this.def12 = def12;
    }

    public String getDef13() {
        return def13;
    }

    public void setDef13(String def13) {
        this.def13 = def13;
    }

    public String getDef14() {
        return def14;
    }

    public void setDef14(String def14) {
        this.def14 = def14;
    }

    public String getDef15() {
        return def15;
    }

    public void setDef15(String def15) {
        this.def15 = def15;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
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

    public String getMaterialAlias1() {
        return materialAlias1;
    }

    public void setMaterialAlias1(String materialAlias1) {
        this.materialAlias1 = materialAlias1;
    }

    public String getMaterialAlias2() {
        return materialAlias2;
    }

    public void setMaterialAlias2(String materialAlias2) {
        this.materialAlias2 = materialAlias2;
    }

    public String getMaterialAlias3() {
        return materialAlias3;
    }

    public void setMaterialAlias3(String materialAlias3) {
        this.materialAlias3 = materialAlias3;
    }

    public String getMaterialAlias4() {
        return materialAlias4;
    }

    public void setMaterialAlias4(String materialAlias4) {
        this.materialAlias4 = materialAlias4;
    }

    public String getMaterialAlias5() {
        return materialAlias5;
    }

    public void setMaterialAlias5(String materialAlias5) {
        this.materialAlias5 = materialAlias5;
    }

    public String getMaterialGroup1() {
        return materialGroup1;
    }

    public void setMaterialGroup1(String materialGroup1) {
        this.materialGroup1 = materialGroup1;
    }

    public String getMaterialGroup2() {
        return materialGroup2;
    }

    public void setMaterialGroup2(String materialGroup2) {
        this.materialGroup2 = materialGroup2;
    }

    public String getMaterialGroup3() {
        return materialGroup3;
    }

    public void setMaterialGroup3(String materialGroup3) {
        this.materialGroup3 = materialGroup3;
    }

    public String getMaterialGroup4() {
        return materialGroup4;
    }

    public void setMaterialGroup4(String materialGroup4) {
        this.materialGroup4 = materialGroup4;
    }

    public String getMaterialGroup5() {
        return materialGroup5;
    }

    public void setMaterialGroup5(String materialGroup5) {
        this.materialGroup5 = materialGroup5;
    }

    public String getMaterialGroup6() {
        return materialGroup6;
    }

    public void setMaterialGroup6(String materialGroup6) {
        this.materialGroup6 = materialGroup6;
    }

    public String getMaterialGroup7() {
        return materialGroup7;
    }

    public void setMaterialGroup7(String materialGroup7) {
        this.materialGroup7 = materialGroup7;
    }

    public String getMaterialGroup8() {
        return materialGroup8;
    }

    public void setMaterialGroup8(String materialGroup8) {
        this.materialGroup8 = materialGroup8;
    }

    public String getMaterialGroup9() {
        return materialGroup9;
    }

    public void setMaterialGroup9(String materialGroup9) {
        this.materialGroup9 = materialGroup9;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSkuCodeAndName() {
        return skuCodeAndName;
    }

    public void setSkuCodeAndName(String skuCodeAndName) {
        this.skuCodeAndName = skuCodeAndName;
    }
	
	public String getIsUnSerial() {
		return isUnSerial;
	}

	public void setIsUnSerial(String isUnSerial) {
		this.isUnSerial = isUnSerial;
	}

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSkuCustomerCode() {
        return skuCustomerCode;
    }

    public void setSkuCustomerCode(String skuCustomerCode) {
        this.skuCustomerCode = skuCustomerCode;
    }
}