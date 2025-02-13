package com.yunyou.modules.tms.authority;

public enum TmAuthorityTable {
    /*运输订单*/
    TM_TRANSPORT_ORDER_HEADER("tm_transport_order_header"),
    /*运输订单标签*/
    TM_TRANSPORT_ORDER_LABEL("tm_transport_order_label"),
    /*运输标签路由*/
    TM_TRANSPORT_ORDER_ROUTE("tm_transport_order_route"),
    /*收货标签记录*/
    TM_RECEIVE_LABEL("tm_receive_label"),
    /*派车单*/
    TM_DISPATCH_ORDER_HEADER("tm_dispatch_order_header"),
    /*派车单配送点*/
    TM_DISPATCH_ORDER_SITE("tm_dispatch_order_site"),
    /*派车单标签*/
    TM_DISPATCH_ORDER_LABEL("tm_dispatch_order_label"),
    /*发货标签记录*/
    TM_DELIVER_LABEL("tm_deliver_label"),
    /*交接单*/
    TM_HANDOVER_ORDER_HEADER("tm_handover_order_header"),
    /*维修工单*/
    TM_REPAIR_ORDER_HEADER("tm_repair_order_header"),
    /*需求计划*/
    TM_DEMAND_PLAN_HEADER("tm_demand_plan_header"),
    /*调度计划*/
    TM_DISPATCH_PLAN_HEADER("tm_dispatch_plan_header"),
    /*预运输订单*/
    TM_PRE_TRANSPORT_ORDER_HEADER("tm_pre_transport_order_header");

    private String value;

    TmAuthorityTable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
