package com.yunyou.modules.wms.kit.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;

import java.util.List;

/**
 * 描述：Bom Entity
 * <p>
 * create by Jianhua on 2019/8/19
 */
public class BanQinCdWhBomEntity extends BanQinCdWhBomHeader {
	private static final long serialVersionUID = -5907827771107839144L;
	// 货主名称
	private String ownerName;
	// 组合件名称
	private String parentSkuName;
	// 组合件明细
	private List<BanQinCdWhBomDetailEntity> bomDetailEntitys = Lists.newArrayList();
	// 组合件加工工序
	private List<BanQinCdWhBomStep> bomStepModels = Lists.newArrayList();

	// 包装代码
	private String packCode;
	// 包装规格
	private String packDesc;
	// 包装单位
	private String uom;
	// 包装单位描述
	private String uomDesc;
	// 单位数量
	private Integer uomQty;
	// 上架规则
	private String paRule;
	// 上架规则名称
	private String paRuleName;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getParentSkuName() {
		return parentSkuName;
	}

	public void setParentSkuName(String parentSkuName) {
		this.parentSkuName = parentSkuName;
	}

	public List<BanQinCdWhBomDetailEntity> getBomDetailEntitys() {
		return bomDetailEntitys;
	}

	public void setBomDetailEntitys(List<BanQinCdWhBomDetailEntity> bomDetailEntitys) {
		this.bomDetailEntitys = bomDetailEntitys;
	}

	public List<BanQinCdWhBomStep> getBomStepModels() {
		return bomStepModels;
	}

	public void setBomStepModels(List<BanQinCdWhBomStep> bomStepModels) {
		this.bomStepModels = bomStepModels;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomDesc() {
		return uomDesc;
	}

	public void setUomDesc(String uomDesc) {
		this.uomDesc = uomDesc;
	}

	public Integer getUomQty() {
		return uomQty;
	}

	public void setUomQty(Integer uomQty) {
		this.uomQty = uomQty;
	}

	public String getPaRule() {
		return paRule;
	}

	public void setPaRule(String paRule) {
		this.paRule = paRule;
	}

	public String getPaRuleName() {
		return paRuleName;
	}

	public void setPaRuleName(String paRuleName) {
		this.paRuleName = paRuleName;
	}
}
