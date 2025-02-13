package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmDemandPlanDetail;

public class TmDemandPlanDetailEntity extends TmDemandPlanDetail {
    private static final long serialVersionUID = 1L;
    // 客户名称
    private String ownerName;
    // 商品名称
    private String skuName;

    public TmDemandPlanDetailEntity() {
        super();
    }

    public TmDemandPlanDetailEntity(String id) {
        super(id);
    }


    public TmDemandPlanDetailEntity(String planOrderNo, String orgId) {
        super(planOrderNo, orgId);
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

}
