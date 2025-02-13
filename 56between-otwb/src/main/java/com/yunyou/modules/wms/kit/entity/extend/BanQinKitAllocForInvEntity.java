package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLoc;
import com.yunyou.modules.wms.common.entity.BanQinCdRuleRotationSqlParamEntity;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinAllocInvLotLocEntity;

import java.util.List;

/**
 * 加工子件分配循环更新参数entity
 */
public class BanQinKitAllocForInvEntity {
	// 可分配数量
	private Double qtyAllocEa;
	// 分配规则
	private BanQinCdRuleAllocDetail ruleAllocDetailModel;
	// 库存周转规则条件
	private BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity;
	// 批次库位库存
	private List<BanQinAllocInvLotLocEntity> invLotLocList;
	// 包装单位换算
	private BanQinCdWhPackageRelationEntity packageEntity;
	// 子件明细行
	private BanQinWmKitSubDetailEntity kitSubDetailEntity;
	// 商品固定拣货位
	private BanQinCdWhSkuLoc cdWhSkuLocModel;
	// 商品信息
	private BanQinCdWhSku cdWhSkuModel;

	public Double getQtyAllocEa() {
		return qtyAllocEa;
	}

	public void setQtyAllocEa(Double qtyAllocEa) {
		this.qtyAllocEa = qtyAllocEa;
	}

	public BanQinCdRuleAllocDetail getRuleAllocDetailModel() {
		return ruleAllocDetailModel;
	}

	public void setRuleAllocDetailModel(BanQinCdRuleAllocDetail ruleAllocDetailModel) {
		this.ruleAllocDetailModel = ruleAllocDetailModel;
	}

	public BanQinCdRuleRotationSqlParamEntity getRuleRotationSqlParamEntity() {
		return ruleRotationSqlParamEntity;
	}

	public void setRuleRotationSqlParamEntity(BanQinCdRuleRotationSqlParamEntity ruleRotationSqlParamEntity) {
		this.ruleRotationSqlParamEntity = ruleRotationSqlParamEntity;
	}

	public List<BanQinAllocInvLotLocEntity> getInvLotLocList() {
		return invLotLocList;
	}

	public void setInvLotLocList(List<BanQinAllocInvLotLocEntity> invLotLocList) {
		this.invLotLocList = invLotLocList;
	}

	public BanQinCdWhPackageRelationEntity getPackageEntity() {
		return packageEntity;
	}

	public void setPackageEntity(BanQinCdWhPackageRelationEntity packageEntity) {
		this.packageEntity = packageEntity;
	}

	public BanQinWmKitSubDetailEntity getKitSubDetailEntity() {
		return kitSubDetailEntity;
	}

	public void setKitSubDetailEntity(BanQinWmKitSubDetailEntity kitSubDetailEntity) {
		this.kitSubDetailEntity = kitSubDetailEntity;
	}

	public BanQinCdWhSkuLoc getCdWhSkuLocModel() {
		return cdWhSkuLocModel;
	}

	public void setCdWhSkuLocModel(BanQinCdWhSkuLoc cdWhSkuLocModel) {
		this.cdWhSkuLocModel = cdWhSkuLocModel;
	}

	public BanQinCdWhSku getCdWhSkuModel() {
		return cdWhSkuModel;
	}

	public void setCdWhSkuModel(BanQinCdWhSku cdWhSkuModel) {
		this.cdWhSkuModel = cdWhSkuModel;
	}
}
