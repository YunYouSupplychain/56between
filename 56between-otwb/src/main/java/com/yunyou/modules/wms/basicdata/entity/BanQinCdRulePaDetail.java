package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 上架规则明细Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRulePaDetail extends DataEntity<BanQinCdRulePaDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String mainCode;		// 主规则编码
	private String isEnable;		// 是否启用（Y、N）
	private String fmLoc;		// 源库位
	private String toZone;		// 目标库区
	private String toLoc;		// 目标库位
	private String isPackageRestrict;		// 是否包装限制（总开关）
	private String isLessCs;		// 不满箱（Qty < 1CS）
	private String isMoreCsLessPl;		// 满箱不满托（1CS=<Qty < 1PL）
	private String isMorePl;		// 满托（Qty >= 1PL）
	private String isAsnTypeRestrict;		// 是否订单类型限制（总开关）
	private String includeAsnType;		// 包含订单类型
	private String excludeAsnType;		// 不包含订单类型
	private String isLotAttRestrict;		// 是否批次属性限制（总开关）
	private String lotAtt04Equal;		// 批次属性04（品质）等于
	private String lotAtt04Unequal;		// 批次属性04（品质）不等于
	private String lotAtt05Equal;		// 批次属性05等于
	private String lotAtt05Unequal;		// 批次属性05不等于
	private String isUseTypeRestrict;		// 是否库位使用类型限制（总开关）
	private String includeUseType;		// 包含库位使用类型
	private String excludeUseType;		// 不包含库位使用类型
	private String isSpaceRestrict;		// 是否库位空间限制（总开关）
	private String isPlRestrict;		// 是否托盘数限制
	private String isCubicRestrict;		// 是否体积限制
	private String isWeightRestrict;		// 是否重量限制
	private String isCategoryRestrict;		// 是否库位种类限制（总开关）
	private String includeCategory;		// 包含库位种类
	private String excludeCategory;		// 不包含库位种类
	private String isAbcRestrict;		// 是否库位ABC限制（总开关）
	private String includeAbc;		// 包含库位ABC分类
	private String excludeAbc;		// 不包含库位ABC分类
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRulePaDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRulePaDetail(String id){
		super(id);
	}

	@NotNull(message="规则编码不能为空")
	@ExcelField(title="规则编码", align=2, sort=2)
	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	@NotNull(message="行号不能为空")
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	@NotNull(message="主规则编码不能为空")
	@ExcelField(title="主规则编码", align=2, sort=4)
	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}
	
	@ExcelField(title="是否启用", align=2, sort=5)
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	@ExcelField(title="源库位", align=2, sort=6)
	public String getFmLoc() {
		return fmLoc;
	}

	public void setFmLoc(String fmLoc) {
		this.fmLoc = fmLoc;
	}
	
	@ExcelField(title="目标库区", align=2, sort=7)
	public String getToZone() {
		return toZone;
	}

	public void setToZone(String toZone) {
		this.toZone = toZone;
	}
	
	@ExcelField(title="目标库位", align=2, sort=8)
	public String getToLoc() {
		return toLoc;
	}

	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	
	@ExcelField(title="是否包装限制（总开关）", align=2, sort=9)
	public String getIsPackageRestrict() {
		return isPackageRestrict;
	}

	public void setIsPackageRestrict(String isPackageRestrict) {
		this.isPackageRestrict = isPackageRestrict;
	}
	
	@ExcelField(title="不满箱（Qty < 1CS）", align=2, sort=10)
	public String getIsLessCs() {
		return isLessCs;
	}

	public void setIsLessCs(String isLessCs) {
		this.isLessCs = isLessCs;
	}
	
	@ExcelField(title="满箱不满托（1CS=<Qty < 1PL）", align=2, sort=11)
	public String getIsMoreCsLessPl() {
		return isMoreCsLessPl;
	}

	public void setIsMoreCsLessPl(String isMoreCsLessPl) {
		this.isMoreCsLessPl = isMoreCsLessPl;
	}
	
	@ExcelField(title="满托（Qty >= 1PL）", align=2, sort=12)
	public String getIsMorePl() {
		return isMorePl;
	}

	public void setIsMorePl(String isMorePl) {
		this.isMorePl = isMorePl;
	}
	
	@ExcelField(title="是否订单类型限制（总开关）", align=2, sort=13)
	public String getIsAsnTypeRestrict() {
		return isAsnTypeRestrict;
	}

	public void setIsAsnTypeRestrict(String isAsnTypeRestrict) {
		this.isAsnTypeRestrict = isAsnTypeRestrict;
	}
	
	@ExcelField(title="包含订单类型", align=2, sort=14)
	public String getIncludeAsnType() {
		return includeAsnType;
	}

	public void setIncludeAsnType(String includeAsnType) {
		this.includeAsnType = includeAsnType;
	}
	
	@ExcelField(title="不包含订单类型", align=2, sort=15)
	public String getExcludeAsnType() {
		return excludeAsnType;
	}

	public void setExcludeAsnType(String excludeAsnType) {
		this.excludeAsnType = excludeAsnType;
	}
	
	@ExcelField(title="是否批次属性限制（总开关）", align=2, sort=16)
	public String getIsLotAttRestrict() {
		return isLotAttRestrict;
	}

	public void setIsLotAttRestrict(String isLotAttRestrict) {
		this.isLotAttRestrict = isLotAttRestrict;
	}
	
	@ExcelField(title="批次属性04（品质）等于", align=2, sort=17)
	public String getLotAtt04Equal() {
		return lotAtt04Equal;
	}

	public void setLotAtt04Equal(String lotAtt04Equal) {
		this.lotAtt04Equal = lotAtt04Equal;
	}
	
	@ExcelField(title="批次属性04（品质）不等于", align=2, sort=18)
	public String getLotAtt04Unequal() {
		return lotAtt04Unequal;
	}

	public void setLotAtt04Unequal(String lotAtt04Unequal) {
		this.lotAtt04Unequal = lotAtt04Unequal;
	}
	
	@ExcelField(title="批次属性05等于", align=2, sort=19)
	public String getLotAtt05Equal() {
		return lotAtt05Equal;
	}

	public void setLotAtt05Equal(String lotAtt05Equal) {
		this.lotAtt05Equal = lotAtt05Equal;
	}
	
	@ExcelField(title="批次属性05不等于", align=2, sort=20)
	public String getLotAtt05Unequal() {
		return lotAtt05Unequal;
	}

	public void setLotAtt05Unequal(String lotAtt05Unequal) {
		this.lotAtt05Unequal = lotAtt05Unequal;
	}
	
	@ExcelField(title="是否库位使用类型限制（总开关）", align=2, sort=21)
	public String getIsUseTypeRestrict() {
		return isUseTypeRestrict;
	}

	public void setIsUseTypeRestrict(String isUseTypeRestrict) {
		this.isUseTypeRestrict = isUseTypeRestrict;
	}
	
	@ExcelField(title="包含库位使用类型", align=2, sort=22)
	public String getIncludeUseType() {
		return includeUseType;
	}

	public void setIncludeUseType(String includeUseType) {
		this.includeUseType = includeUseType;
	}
	
	@ExcelField(title="不包含库位使用类型", align=2, sort=23)
	public String getExcludeUseType() {
		return excludeUseType;
	}

	public void setExcludeUseType(String excludeUseType) {
		this.excludeUseType = excludeUseType;
	}
	
	@ExcelField(title="是否库位空间限制（总开关）", align=2, sort=24)
	public String getIsSpaceRestrict() {
		return isSpaceRestrict;
	}

	public void setIsSpaceRestrict(String isSpaceRestrict) {
		this.isSpaceRestrict = isSpaceRestrict;
	}
	
	@ExcelField(title="是否托盘数限制", align=2, sort=25)
	public String getIsPlRestrict() {
		return isPlRestrict;
	}

	public void setIsPlRestrict(String isPlRestrict) {
		this.isPlRestrict = isPlRestrict;
	}
	
	@ExcelField(title="是否体积限制", align=2, sort=26)
	public String getIsCubicRestrict() {
		return isCubicRestrict;
	}

	public void setIsCubicRestrict(String isCubicRestrict) {
		this.isCubicRestrict = isCubicRestrict;
	}
	
	@ExcelField(title="是否重量限制", align=2, sort=27)
	public String getIsWeightRestrict() {
		return isWeightRestrict;
	}

	public void setIsWeightRestrict(String isWeightRestrict) {
		this.isWeightRestrict = isWeightRestrict;
	}
	
	@ExcelField(title="是否库位种类限制（总开关）", align=2, sort=28)
	public String getIsCategoryRestrict() {
		return isCategoryRestrict;
	}

	public void setIsCategoryRestrict(String isCategoryRestrict) {
		this.isCategoryRestrict = isCategoryRestrict;
	}
	
	@ExcelField(title="包含库位种类", align=2, sort=29)
	public String getIncludeCategory() {
		return includeCategory;
	}

	public void setIncludeCategory(String includeCategory) {
		this.includeCategory = includeCategory;
	}
	
	@ExcelField(title="不包含库位种类", align=2, sort=30)
	public String getExcludeCategory() {
		return excludeCategory;
	}

	public void setExcludeCategory(String excludeCategory) {
		this.excludeCategory = excludeCategory;
	}
	
	@ExcelField(title="是否库位ABC限制（总开关）", align=2, sort=31)
	public String getIsAbcRestrict() {
		return isAbcRestrict;
	}

	public void setIsAbcRestrict(String isAbcRestrict) {
		this.isAbcRestrict = isAbcRestrict;
	}
	
	@ExcelField(title="包含库位ABC分类", align=2, sort=32)
	public String getIncludeAbc() {
		return includeAbc;
	}

	public void setIncludeAbc(String includeAbc) {
		this.includeAbc = includeAbc;
	}
	
	@ExcelField(title="不包含库位ABC分类", align=2, sort=33)
	public String getExcludeAbc() {
		return excludeAbc;
	}

	public void setExcludeAbc(String excludeAbc) {
		this.excludeAbc = excludeAbc;
	}

	@ExcelField(title="分公司", align=2, sort=41)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=42)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}