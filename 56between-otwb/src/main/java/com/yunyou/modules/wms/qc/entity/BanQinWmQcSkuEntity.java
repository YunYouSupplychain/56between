package com.yunyou.modules.wms.qc.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 描述：质检单商品扩展Entity
 *
 * @auther: Jianhua on 2019/1/29
 */
public class BanQinWmQcSkuEntity extends BanQinWmQcSku {
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 质检规则
    private String qcRuleName;
    // 上架规则
    private String paRuleName;
    // 质检项组
    private String itemGroupName;
    // 可质检包装数
    private Double qtyAvailQcUom;
    // 计划质检包装数
    private Double qtyPlanQcUom;
    // 包装数量
    private Double uomQty;
    // 快速录入码
    private String quickCode;
    // 质检明细
    private List<BanQinWmQcDetailEntity> wmqcDetailList = Lists.newArrayList();

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public String getQcRuleName() {
        return qcRuleName;
    }

    public void setQcRuleName(String qcRuleName) {
        this.qcRuleName = qcRuleName;
    }

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public Double getQtyAvailQcUom() {
        return qtyAvailQcUom;
    }

    public void setQtyAvailQcUom(Double qtyAvailQcUom) {
        this.qtyAvailQcUom = qtyAvailQcUom;
    }

    public Double getQtyPlanQcUom() {
        return qtyPlanQcUom;
    }

    public void setQtyPlanQcUom(Double qtyPlanQcUom) {
        this.qtyPlanQcUom = qtyPlanQcUom;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }

    public List<BanQinWmQcDetailEntity> getWmqcDetailList() {
        return wmqcDetailList;
    }

    public void setWmqcDetailList(List<BanQinWmQcDetailEntity> wmqcDetailList) {
        this.wmqcDetailList = wmqcDetailList;
    }
}
