package com.yunyou.modules.wms.outbound.entity;

import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleAllocDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;

import java.util.List;

/**
 * 按分配规则明细循环 分配入参Entity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinAllocForAllocRuleEntity {
	// 出库单明细行
	private BanQinWmSoDetail wmSoDetailModel;
	// 出库单明细行
	private BanQinWmSoDetailEntity wmSoDetailItem;
	// 分配规则明细
	private List<BanQinCdRuleAllocDetail> ruleAllocDetailList;
	// 包装单位换算
	private List<BanQinCdWhPackageRelation> packageRelationList;
	// 库存周转规则明细
	private List<BanQinCdRuleRotationDetail> ruleRotationDetailList;
	// 商品
	private BanQinCdWhSku cdWhSkuModel;
	// 预配明细
	private BanQinWmSoPreallocEntity wmSoPreallocEntity;
	// 预配明细单位换算数量
	private Double qtyPackUom;
	// 收货人- 从出库单获取
	private String consigneeCode;
	// 快递单号- 从出库单获取
	private String trackingNo;

    public BanQinWmSoDetail getWmSoDetailModel() {
        return wmSoDetailModel;
    }

    public void setWmSoDetailModel(BanQinWmSoDetail wmSoDetailModel) {
        this.wmSoDetailModel = wmSoDetailModel;
    }

    public BanQinWmSoDetailEntity getWmSoDetailItem() {
        return wmSoDetailItem;
    }

    public void setWmSoDetailItem(BanQinWmSoDetailEntity wmSoDetailItem) {
        this.wmSoDetailItem = wmSoDetailItem;
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

    public BanQinWmSoPreallocEntity getWmSoPreallocEntity() {
        return wmSoPreallocEntity;
    }

    public void setWmSoPreallocEntity(BanQinWmSoPreallocEntity wmSoPreallocEntity) {
        this.wmSoPreallocEntity = wmSoPreallocEntity;
    }

    public Double getQtyPackUom() {
        return qtyPackUom;
    }

    public void setQtyPackUom(Double qtyPackUom) {
        this.qtyPackUom = qtyPackUom;
    }

    public String getConsigneeCode() {
        return consigneeCode;
    }

    public void setConsigneeCode(String consigneeCode) {
        this.consigneeCode = consigneeCode;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }
}