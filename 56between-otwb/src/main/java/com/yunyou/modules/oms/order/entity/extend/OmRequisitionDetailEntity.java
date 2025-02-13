package com.yunyou.modules.oms.order.entity.extend;

import com.yunyou.modules.oms.order.entity.OmRequisitionDetail;

import java.math.BigDecimal;

public class OmRequisitionDetailEntity extends OmRequisitionDetail {
    private static final long serialVersionUID = -3104381206794259217L;
    private String ownerName;
    private String skuName;
    private BigDecimal invQty = BigDecimal.ZERO;
    private BigDecimal planTaskQty = BigDecimal.ZERO;

    public OmRequisitionDetailEntity() {
    }

    public OmRequisitionDetailEntity(String id) {
        super(id);
    }

    public OmRequisitionDetailEntity(String reqNo, String orgId) {
        super(reqNo, orgId);
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

    public BigDecimal getInvQty() {
        return invQty;
    }

    public void setInvQty(BigDecimal invQty) {
        this.invQty = invQty;
    }

    public BigDecimal getPlanTaskQty() {
        return planTaskQty;
    }

    public void setPlanTaskQty(BigDecimal planTaskQty) {
        this.planTaskQty = planTaskQty;
    }
}
