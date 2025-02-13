package com.yunyou.modules.wms.kit.entity.extend;

import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;

import java.util.List;

/**
 * Description: 加工子件分配参数entity
 * 
 * @author: Morice Create Date: 2014-12-25
 * 
 * <pre>
 * 修改记录:
 * 修改后版本			修改人		修改日期			修改内容 
 * 2014-12-25.1		Morice		2014-12-25		create					
 * </pre>
 */
public class BanQinKitAllocForRuleEntity {
	// 子件明细行
	private BanQinWmKitSubDetailEntity subDetailEntity;
	// 分配规则明细
	private List<BanQinCdRuleAllocDetail> ruleAllocDetailList;
	// 包装单位换算
	private List<BanQinCdWhPackageRelation> packageRelationList;
	// 库存周转规则明细
	private List<BanQinCdRuleRotationDetail> ruleRotationDetailList;
	// 商品
	private BanQinCdWhSku cdWhSkuModel;

	public BanQinWmKitSubDetailEntity getSubDetailEntity() {
		return subDetailEntity;
	}

	public void setSubDetailEntity(BanQinWmKitSubDetailEntity subDetailEntity) {
		this.subDetailEntity = subDetailEntity;
	}

	public List<BanQinCdRuleAllocDetail> getRuleAllocDetailList() {
		return ruleAllocDetailList;
	}

	public void setRuleAllocDetailList(List<BanQinCdRuleAllocDetail> ruleAllocDetailList) {
		this.ruleAllocDetailList = ruleAllocDetailList;
	}

	public List<BanQinCdWhPackageRelation> getPackageRelationList() {
		return packageRelationList;
	}

	public void setPackageRelationList(List<BanQinCdWhPackageRelation> packageRelationList) {
		this.packageRelationList = packageRelationList;
	}

	public List<BanQinCdRuleRotationDetail> getRuleRotationDetailList() {
		return ruleRotationDetailList;
	}

	public void setRuleRotationDetailList(List<BanQinCdRuleRotationDetail> ruleRotationDetailList) {
		this.ruleRotationDetailList = ruleRotationDetailList;
	}

	public BanQinCdWhSku getCdWhSkuModel() {
		return cdWhSkuModel;
	}

	public void setCdWhSkuModel(BanQinCdWhSku cdWhSkuModel) {
		this.cdWhSkuModel = cdWhSkuModel;
	}
}
