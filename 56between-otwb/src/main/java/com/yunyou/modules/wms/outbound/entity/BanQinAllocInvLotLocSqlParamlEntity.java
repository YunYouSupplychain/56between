package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.common.entity.BanQinCdRuleRotationSqlParamEntity;

/**
 * 获取批次库位库存 入参entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinAllocInvLotLocSqlParamlEntity {
	// 货主
	private String ownerCode;
	// 商品
	private String skuCode;
	// 批次号
	private String lotNum;
	// 区域
	private String areaCode;
	// 拣货区
	private String zoneCode;
	// 拣货位
	private String locCode;
	// 跟踪号
	private String traceId;
	// 商品固定拣货位对象
	private String skuLocCode;
	// 库位使用类型
	private String locUseType;
	// 是否清仓优先
	private String isClearFirst;
	// 库存周转规则查询条件
	private BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity;
	// 商品(出库效期校验)
	private BanQinCdWhSku cdWhSkuModel;
	// 单位换算数量
	private Integer qtyPackUom;

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

	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSkuLocCode() {
		return skuLocCode;
	}

	public void setSkuLocCode(String skuLocCode) {
		this.skuLocCode = skuLocCode;
	}

	public String getLocUseType() {
		return locUseType;
	}

	public void setLocUseType(String locUseType) {
		this.locUseType = locUseType;
	}

	public String getIsClearFirst() {
		return isClearFirst;
	}

	public void setIsClearFirst(String isClearFirst) {
		this.isClearFirst = isClearFirst;
	}

	public Integer getQtyPackUom() {
		return qtyPackUom;
	}

	public void setQtyPackUom(Integer qtyPackUom) {
		this.qtyPackUom = qtyPackUom;
	}

    public BanQinCdRuleRotationSqlParamEntity getRuleRotationSqlParamEntity() {
        return ruleRotationSqlParamEntity;
    }

    public void setRuleRotationSqlParamEntity(BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity) {
        this.ruleRotationSqlParamEntity = ruleRotationSqlParamEntity;
    }

    public BanQinCdWhSku getCdWhSkuModel() {
        return cdWhSkuModel;
    }

    public void setCdWhSkuModel(BanQinCdWhSku cdWhSkuModel) {
        this.cdWhSkuModel = cdWhSkuModel;
    }
}