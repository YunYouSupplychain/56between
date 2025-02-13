package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 物流轨迹查询Request
 */
public class StoTraceQueryRequest implements Serializable {
    private static final long serialVersionUID = -5342963745526165592L;
    // 排序方式
    private String order;
    // 运单号集合
    private List<String> waybillNoList;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getWaybillNoList() {
        return waybillNoList;
    }

    public void setWaybillNoList(List<String> waybillNoList) {
        this.waybillNoList = waybillNoList;
    }
}
