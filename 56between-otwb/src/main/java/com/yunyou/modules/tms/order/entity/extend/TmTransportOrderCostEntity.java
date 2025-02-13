package com.yunyou.modules.tms.order.entity.extend;

import com.yunyou.modules.tms.order.entity.TmTransportOrderCost;

public class TmTransportOrderCostEntity extends TmTransportOrderCost {
    private static final long serialVersionUID = 160676980980383474L;

    private String settlementName;

    public TmTransportOrderCostEntity() {
        super();
    }

    public TmTransportOrderCostEntity(String id) {
        super(id);
    }

    public TmTransportOrderCostEntity(String transportNo, String orgId) {
        super(transportNo, orgId);
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }
}
