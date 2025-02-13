package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 分配规则Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdRuleAllocDetail extends DataEntity<BanQinCdRuleAllocDetail> {
	
	private static final long serialVersionUID = 1L;
	private String ruleCode;		// 规则编码
	private String lineNo;		// 行号
	private String uom;		// 包装单位（PL、CS、IP、EA、OT）
	private String locUseType;		// 库位使用类型（RS、CS、EA、PC、QC、ST、SS、KT）
	private String skuLocType;		// 商品拣货位类型（CS、EA、PC）
	private String isClearFirst;		// 是否清仓优先
	private String orgId;		// 分公司
	private String headerId;		// 头表Id
	
	public BanQinCdRuleAllocDetail() {
		super();
		this.recVer = 0;
	}

	public BanQinCdRuleAllocDetail(String id){
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

	@NotNull(message="包装单位不能为空")
	@ExcelField(title="包装单位", align=2, sort=4)
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	@ExcelField(title="库位使用类型", align=2, sort=5)
	public String getLocUseType() {
		return locUseType;
	}

	public void setLocUseType(String locUseType) {
		this.locUseType = locUseType;
	}
	
	@ExcelField(title="商品拣货位类型", align=2, sort=6)
	public String getSkuLocType() {
		return skuLocType;
	}

	public void setSkuLocType(String skuLocType) {
		this.skuLocType = skuLocType;
	}
	
	@ExcelField(title="是否清仓优先", align=2, sort=7)
	public String getIsClearFirst() {
		return isClearFirst;
	}

	public void setIsClearFirst(String isClearFirst) {
		this.isClearFirst = isClearFirst;
	}

	@ExcelField(title="分公司", align=2, sort=15)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="头表Id", align=2, sort=16)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}