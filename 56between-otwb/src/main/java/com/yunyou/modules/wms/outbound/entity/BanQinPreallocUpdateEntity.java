package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;

import java.util.List;

/**
 * 预配-更新Entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinPreallocUpdateEntity {

	private Double qtyPrealloc;
	// 库存
	private BanQinWmInvLot invLotModel;
	// 包装单位
	private List<BanQinCdWhPackageRelation> packageRelationList;
	// 预配明细
	private List<BanQinCdRulePreallocDetail> rulePreallocDetailList;
	// 出库单明细行
	private BanQinWmSoDetailEntity soDetail;

	public Double getQtyPrealloc() {
		return qtyPrealloc;
	}

	public void setQtyPrealloc(Double qtyPrealloc) {
		this.qtyPrealloc = qtyPrealloc;
	}

    public BanQinWmInvLot getInvLotModel() {
        return invLotModel;
    }

    public void setInvLotModel(BanQinWmInvLot invLotModel) {
        this.invLotModel = invLotModel;
    }

    public List<BanQinCdWhPackageRelation> getPackageRelationList() {
        return packageRelationList;
    }

    public void setPackageRelationList(List<BanQinCdWhPackageRelation> packageRelationList) {
        this.packageRelationList = packageRelationList;
    }

    public List<BanQinCdRulePreallocDetail> getRulePreallocDetailList() {
        return rulePreallocDetailList;
    }

    public void setRulePreallocDetailList(List<BanQinCdRulePreallocDetail> rulePreallocDetailList) {
        this.rulePreallocDetailList = rulePreallocDetailList;
    }

    public BanQinWmSoDetailEntity getSoDetail() {
        return soDetail;
    }

    public void setSoDetail(BanQinWmSoDetailEntity soDetail) {
        this.soDetail = soDetail;
    }
}