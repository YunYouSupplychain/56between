package com.yunyou.modules.wms.task.entity;

import java.util.Date;

/**
 * 描述：上架任务扩展Entity
 *
 * @auther: Jianhua on 2019/1/28
 */
public class BanQinWmTaskPaEntity extends BanQinWmTaskPa {
    // 当前上架数
    private Double currentPaQtyEa;
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 数量
    private Double qtyPa;
    // 毛重
    private Double grossWeight;
    // 净重
    private Double netWeight;
    // 体积
    private Double cubic;
    // 批次属性7
    private String realCaseId;
    private String asnType;
    // 计划上架库位
    private String planPaLoc;
    // 指定上架规则
    private String newPaRule;
    // 上架库位指定规则
    private String newReserveCode;
    // 收货明细的上架任务ID
    private String paIdRcv;
    // 事务交易ID
    private String transactionId;
    // 收货库位
    private String fromLoc;
    // 收货traceID
    private String fromId;
    // 上架EA数量
    private Double qtyEa;
    // 上架规则名称
    private String paRuleName;
    //
    private String orgName;
    private Double uomQty;

    private Date beginOrderTime;		// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间

    public Double getQtyPa() {
        return qtyPa;
    }

    public void setQtyPa(Double qtyPa) {
        this.qtyPa = qtyPa;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public String getRealCaseId() {
        return realCaseId;
    }

    public void setRealCaseId(String realCaseId) {
        this.realCaseId = realCaseId;
    }

    public String getAsnType() {
        return asnType;
    }

    public void setAsnType(String asnType) {
        this.asnType = asnType;
    }

    public String getPlanPaLoc() {
        return planPaLoc;
    }

    public void setPlanPaLoc(String planPaLoc) {
        this.planPaLoc = planPaLoc;
    }

    public String getNewPaRule() {
        return newPaRule;
    }

    public void setNewPaRule(String newPaRule) {
        this.newPaRule = newPaRule;
    }

    public String getNewReserveCode() {
        return newReserveCode;
    }

    public void setNewReserveCode(String newReserveCode) {
        this.newReserveCode = newReserveCode;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public String getPaIdRcv() {
        return paIdRcv;
    }

    public void setPaIdRcv(String paIdRcv) {
        this.paIdRcv = paIdRcv;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getCurrentPaQtyEa() {
        return currentPaQtyEa;
    }

    public void setCurrentPaQtyEa(Double currentPaQtyEa) {
        this.currentPaQtyEa = currentPaQtyEa;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

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

    public String getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(String fromLoc) {
        this.fromLoc = fromLoc;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public Double getQtyEa() {
        return qtyEa;
    }

    public void setQtyEa(Double qtyEa) {
        this.qtyEa = qtyEa;
    }

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }
}
