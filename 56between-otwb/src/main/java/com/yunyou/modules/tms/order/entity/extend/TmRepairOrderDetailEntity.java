package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmRepairOrderDetail;

public class TmRepairOrderDetailEntity extends TmRepairOrderDetail {
    private static final long serialVersionUID = 8602151515924103426L;
    private Double repairPrice; // 维修工时单价
    private String repairName;  // 维修单位名称
    private String skuName;     // 配件名称

    public TmRepairOrderDetailEntity() {
    }

    public TmRepairOrderDetailEntity(String id) {
        super(id);
    }

    public TmRepairOrderDetailEntity(String repairNo, String orgId) {
        super(repairNo, orgId);
    }

    public String getRepairName() {
        return repairName;
    }

    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Double getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Double repairPrice) {
        this.repairPrice = repairPrice;
    }
}
