package com.yunyou.modules.wms.inbound.entity;

import com.yunyou.modules.wms.common.entity.ActionCode;

import java.util.List;

/**
 * 描述：收货明细扩展Entity
 *
 * @auther: Jianhua on 2019/1/25
 */
public class BanQinWmAsnDetailReceiveEntity extends BanQinWmAsnDetailReceive {

    private static final long serialVersionUID = 1L;
    // 货主名称
    private String ownerName;
    // 商品名称
    private String skuName;
    // 单位描述
    private String uomDesc;
    // 包装描述
    private String packDesc;
    // 上架规则
    private String paRuleName;
    // 当前收货数
    private Double currentQtyRcvEa;
    // 当前收货单位数
    private Double currentQtyRcvUom;
    // 包装数量
    private Double uomQty;
    // 预收单位数
    private Double qtyPlanUom;
    // 收货单位数
    private Double qtyRcvUom;
    // 订单类型
    private String orderType;
    // 保质期
    private Double shelfLife;
    private String lifeType;
    private String isValidity;
    private Double inLifeDays;
    private Double outLifeDays;
    private String isOverRcv;
    private Double overRcvPct;
    // 体积
    private Double cubic;
    // 毛重
    private Double grossWeight;
    // 净重
    private Double netWeight;
    private String barCode;
    private String supBarCode;
    private String isSerial;
    // 快速录入码
    private String quickCode;
    private String asnType;
    // 业务操作代码：收货（RECEIVING）、取消收货（CANCEL_RECEIVING）
    private ActionCode actionCode;
    // 序列
    private List<BanQinWmAsnSerialEntity> serialList;
    // 事务交易ID
    private String transactionId;
    private Long cdprHI;
    private Long cdprTI;

    private String skuSpec;     // 商品规格
    private String qtyUnit;         // 数量单位

    private String skuCodeAndName;

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

    public String getPaRuleName() {
        return paRuleName;
    }

    public void setPaRuleName(String paRuleName) {
        this.paRuleName = paRuleName;
    }

    public Double getCurrentQtyRcvEa() {
        return currentQtyRcvEa;
    }

    public void setCurrentQtyRcvEa(Double currentQtyRcvEa) {
        this.currentQtyRcvEa = currentQtyRcvEa;
    }

    public Double getCurrentQtyRcvUom() {
        return currentQtyRcvUom;
    }

    public void setCurrentQtyRcvUom(Double currentQtyRcvUom) {
        this.currentQtyRcvUom = currentQtyRcvUom;
    }

    public Double getUomQty() {
        return uomQty;
    }

    public void setUomQty(Double uomQty) {
        this.uomQty = uomQty;
    }

    public Double getQtyPlanUom() {
        return qtyPlanUom;
    }

    public void setQtyPlanUom(Double qtyPlanUom) {
        this.qtyPlanUom = qtyPlanUom;
    }

    public Double getQtyRcvUom() {
        return qtyRcvUom;
    }

    public void setQtyRcvUom(Double qtyRcvUom) {
        this.qtyRcvUom = qtyRcvUom;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getLifeType() {
        return lifeType;
    }

    public void setLifeType(String lifeType) {
        this.lifeType = lifeType;
    }

    public String getIsValidity() {
        return isValidity;
    }

    public void setIsValidity(String isValidity) {
        this.isValidity = isValidity;
    }

    public Double getInLifeDays() {
        return inLifeDays;
    }

    public void setInLifeDays(Double inLifeDays) {
        this.inLifeDays = inLifeDays;
    }

    public Double getOutLifeDays() {
        return outLifeDays;
    }

    public void setOutLifeDays(Double outLifeDays) {
        this.outLifeDays = outLifeDays;
    }

    public String getIsOverRcv() {
        return isOverRcv;
    }

    public void setIsOverRcv(String isOverRcv) {
        this.isOverRcv = isOverRcv;
    }

    public Double getOverRcvPct() {
        return overRcvPct;
    }

    public void setOverRcvPct(Double overRcvPct) {
        this.overRcvPct = overRcvPct;
    }

    public Double getCubic() {
        return cubic;
    }

    public void setCubic(Double cubic) {
        this.cubic = cubic;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSupBarCode() {
        return supBarCode;
    }

    public void setSupBarCode(String supBarCode) {
        this.supBarCode = supBarCode;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public String getQuickCode() {
        return quickCode;
    }

    public void setQuickCode(String quickCode) {
        this.quickCode = quickCode;
    }

    public String getAsnType() {
        return asnType;
    }

    public void setAsnType(String asnType) {
        this.asnType = asnType;
    }

    public ActionCode getActionCode() {
        return actionCode;
    }

    public void setActionCode(ActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public List<BanQinWmAsnSerialEntity> getSerialList() {
        return serialList;
    }

    public void setSerialList(List<BanQinWmAsnSerialEntity> serialList) {
        this.serialList = serialList;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getCdprHI() {
        return cdprHI;
    }

    public void setCdprHI(Long cdprHI) {
        this.cdprHI = cdprHI;
    }

    public Long getCdprTI() {
        return cdprTI;
    }

    public void setCdprTI(Long cdprTI) {
        this.cdprTI = cdprTI;
    }

    public String getSkuCodeAndName() {
        return skuCodeAndName;
    }

    public void setSkuCodeAndName(String skuCodeAndName) {
        this.skuCodeAndName = skuCodeAndName;
    }

    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }
}
