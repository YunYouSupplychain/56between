package com.yunyou.modules.oms.order.entity;

/**
 * 描述：
 *
 * @auther: zyf on 2019/5/28
 */
public class OmChainDetailQueryEntity extends OmChainDetail {

    private String outWarehouse;    // 下发仓库
    private String chainNo;
    private String owner;           // 货主
    private String sourceOrderType; // 来源订单类型
    private String orderStatus;     // 订单状态
    private String lessStatus;      // 小于订单状态
    private String moreStatus;      // 大于订单状态

    public OmChainDetailQueryEntity(){
        super();
    }

    public String getOutWarehouse() {
        return outWarehouse;
    }

    public void setOutWarehouse(String outWarehouse) {
        this.outWarehouse = outWarehouse;
    }

    public String getChainNo() {
        return chainNo;
    }

    public void setChainNo(String chainNo) {
        this.chainNo = chainNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getLessStatus() {
        return lessStatus;
    }

    public void setLessStatus(String lessStatus) {
        this.lessStatus = lessStatus;
    }

    public String getMoreStatus() {
        return moreStatus;
    }

    public void setMoreStatus(String moreStatus) {
        this.moreStatus = moreStatus;
    }
}
