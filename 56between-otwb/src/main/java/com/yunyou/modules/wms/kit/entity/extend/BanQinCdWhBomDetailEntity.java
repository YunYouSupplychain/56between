package com.yunyou.modules.wms.kit.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;

import java.util.List;

/**
 * 描述：Bom明细Entity
 * <p>
 * create by Jianhua on 2019/8/19
 */
public class BanQinCdWhBomDetailEntity extends BanQinCdWhBomDetail {
	// 货主名称
	private String ownerName;
	// 子件商品名称
	private String subSkuName;
	// 包装规格
	private String packDesc;
	// 单位描述
	private String uomDesc;
	// 单位换算数
	private Integer uomQty;
	// 组合件的所有父件
	private List<String> parentSkuCodeList = Lists.newArrayList();

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSubSkuName() {
		return subSkuName;
	}

	public void setSubSkuName(String subSkuName) {
		this.subSkuName = subSkuName;
	}

	public String getPackDesc() {
		return packDesc;
	}

	public void setPackDesc(String packDesc) {
		this.packDesc = packDesc;
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

	public List<String> getParentSkuCodeList() {
		return parentSkuCodeList;
	}

	public void setParentSkuCodeList(List<String> parentSkuCodeList) {
		this.parentSkuCodeList = parentSkuCodeList;
	}
}
