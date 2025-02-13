package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmPreTransportOrderCost;

public class TmPreTransportOrderCostEntity extends TmPreTransportOrderCost {
    private static final long serialVersionUID = 160676980980383474L;

    private String settlementName;

    public TmPreTransportOrderCostEntity() {
        super();
    }

    public TmPreTransportOrderCostEntity(String id) {
        super(id);
    }

    public TmPreTransportOrderCostEntity(String transportNo, String orgId) {
        super(transportNo, orgId);
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }
}
