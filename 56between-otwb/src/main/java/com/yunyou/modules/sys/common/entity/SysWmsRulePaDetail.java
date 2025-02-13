package com.yunyou.modules.sys.common.entity;

import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 上架规则明细Entity
 */
public class SysWmsRulePaDetail extends DataEntity<SysWmsRulePaDetail> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "规则编码不能为空")
    private String ruleCode;// 规则编码
    @NotNull(message = "行号不能为空")
    private String lineNo;// 行号
    @NotNull(message = "主规则编码不能为空")
    private String mainCode;// 主规则编码
    private String isEnable;// 是否启用（Y、N）
    private String fmLoc;// 源库位
    private String toZone;// 目标库区
    private String toLoc;// 目标库位
    private String isPackageRestrict;// 是否包装限制（总开关）
    private String isLessCs;// 不满箱（Qty < 1CS）
    private String isMoreCsLessPl;// 满箱不满托（1CS=<Qty < 1PL）
    private String isMorePl;// 满托（Qty >= 1PL）
    private String isAsnTypeRestrict;// 是否订单类型限制（总开关）
    private String includeAsnType;// 包含订单类型
    private String excludeAsnType;// 不包含订单类型
    private String isLotAttRestrict;// 是否批次属性限制（总开关）
    private String lotAtt04Equal;// 批次属性04（品质）等于
    private String lotAtt04Unequal;// 批次属性04（品质）不等于
    private String lotAtt05Equal;// 批次属性05等于
    private String lotAtt05Unequal;// 批次属性05不等于
    private String isUseTypeRestrict;// 是否库位使用类型限制（总开关）
    private String includeUseType;// 包含库位使用类型
    private String excludeUseType;// 不包含库位使用类型
    private String isSpaceRestrict;// 是否库位空间限制（总开关）
    private String isPlRestrict;// 是否托盘数限制
    private String isCubicRestrict;// 是否体积限制
    private String isWeightRestrict;// 是否重量限制
    private String isCategoryRestrict;// 是否库位种类限制（总开关）
    private String includeCategory;// 包含库位种类
    private String excludeCategory;// 不包含库位种类
    private String isAbcRestrict;// 是否库位ABC限制（总开关）
    private String includeAbc;// 包含库位ABC分类
    private String excludeAbc;// 不包含库位ABC分类
    private String headerId;// 头表Id
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套

    public SysWmsRulePaDetail() {
        super();
    }

    public SysWmsRulePaDetail(String id) {
        super(id);
    }

    public SysWmsRulePaDetail(String headerId, String dataSet) {
        this.headerId = headerId;
        this.dataSet = dataSet;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getFmLoc() {
        return fmLoc;
    }

    public void setFmLoc(String fmLoc) {
        this.fmLoc = fmLoc;
    }

    public String getToZone() {
        return toZone;
    }

    public void setToZone(String toZone) {
        this.toZone = toZone;
    }

    public String getToLoc() {
        return toLoc;
    }

    public void setToLoc(String toLoc) {
        this.toLoc = toLoc;
    }

    public String getIsPackageRestrict() {
        return isPackageRestrict;
    }

    public void setIsPackageRestrict(String isPackageRestrict) {
        this.isPackageRestrict = isPackageRestrict;
    }

    public String getIsLessCs() {
        return isLessCs;
    }

    public void setIsLessCs(String isLessCs) {
        this.isLessCs = isLessCs;
    }

    public String getIsMoreCsLessPl() {
        return isMoreCsLessPl;
    }

    public void setIsMoreCsLessPl(String isMoreCsLessPl) {
        this.isMoreCsLessPl = isMoreCsLessPl;
    }

    public String getIsMorePl() {
        return isMorePl;
    }

    public void setIsMorePl(String isMorePl) {
        this.isMorePl = isMorePl;
    }

    public String getIsAsnTypeRestrict() {
        return isAsnTypeRestrict;
    }

    public void setIsAsnTypeRestrict(String isAsnTypeRestrict) {
        this.isAsnTypeRestrict = isAsnTypeRestrict;
    }

    public String getIncludeAsnType() {
        return includeAsnType;
    }

    public void setIncludeAsnType(String includeAsnType) {
        this.includeAsnType = includeAsnType;
    }

    public String getExcludeAsnType() {
        return excludeAsnType;
    }

    public void setExcludeAsnType(String excludeAsnType) {
        this.excludeAsnType = excludeAsnType;
    }

    public String getIsLotAttRestrict() {
        return isLotAttRestrict;
    }

    public void setIsLotAttRestrict(String isLotAttRestrict) {
        this.isLotAttRestrict = isLotAttRestrict;
    }

    public String getLotAtt04Equal() {
        return lotAtt04Equal;
    }

    public void setLotAtt04Equal(String lotAtt04Equal) {
        this.lotAtt04Equal = lotAtt04Equal;
    }

    public String getLotAtt04Unequal() {
        return lotAtt04Unequal;
    }

    public void setLotAtt04Unequal(String lotAtt04Unequal) {
        this.lotAtt04Unequal = lotAtt04Unequal;
    }

    public String getLotAtt05Equal() {
        return lotAtt05Equal;
    }

    public void setLotAtt05Equal(String lotAtt05Equal) {
        this.lotAtt05Equal = lotAtt05Equal;
    }

    public String getLotAtt05Unequal() {
        return lotAtt05Unequal;
    }

    public void setLotAtt05Unequal(String lotAtt05Unequal) {
        this.lotAtt05Unequal = lotAtt05Unequal;
    }

    public String getIsUseTypeRestrict() {
        return isUseTypeRestrict;
    }

    public void setIsUseTypeRestrict(String isUseTypeRestrict) {
        this.isUseTypeRestrict = isUseTypeRestrict;
    }

    public String getIncludeUseType() {
        return includeUseType;
    }

    public void setIncludeUseType(String includeUseType) {
        this.includeUseType = includeUseType;
    }

    public String getExcludeUseType() {
        return excludeUseType;
    }

    public void setExcludeUseType(String excludeUseType) {
        this.excludeUseType = excludeUseType;
    }

    public String getIsSpaceRestrict() {
        return isSpaceRestrict;
    }

    public void setIsSpaceRestrict(String isSpaceRestrict) {
        this.isSpaceRestrict = isSpaceRestrict;
    }

    public String getIsPlRestrict() {
        return isPlRestrict;
    }

    public void setIsPlRestrict(String isPlRestrict) {
        this.isPlRestrict = isPlRestrict;
    }

    public String getIsCubicRestrict() {
        return isCubicRestrict;
    }

    public void setIsCubicRestrict(String isCubicRestrict) {
        this.isCubicRestrict = isCubicRestrict;
    }

    public String getIsWeightRestrict() {
        return isWeightRestrict;
    }

    public void setIsWeightRestrict(String isWeightRestrict) {
        this.isWeightRestrict = isWeightRestrict;
    }

    public String getIsCategoryRestrict() {
        return isCategoryRestrict;
    }

    public void setIsCategoryRestrict(String isCategoryRestrict) {
        this.isCategoryRestrict = isCategoryRestrict;
    }

    public String getIncludeCategory() {
        return includeCategory;
    }

    public void setIncludeCategory(String includeCategory) {
        this.includeCategory = includeCategory;
    }

    public String getExcludeCategory() {
        return excludeCategory;
    }

    public void setExcludeCategory(String excludeCategory) {
        this.excludeCategory = excludeCategory;
    }

    public String getIsAbcRestrict() {
        return isAbcRestrict;
    }

    public void setIsAbcRestrict(String isAbcRestrict) {
        this.isAbcRestrict = isAbcRestrict;
    }

    public String getIncludeAbc() {
        return includeAbc;
    }

    public void setIncludeAbc(String includeAbc) {
        this.includeAbc = includeAbc;
    }

    public String getExcludeAbc() {
        return excludeAbc;
    }

    public void setExcludeAbc(String excludeAbc) {
        this.excludeAbc = excludeAbc;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }
}