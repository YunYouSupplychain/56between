package com.yunyou.modules.interfaces.kd100.entity;

public class QueryTrackRouteInfo {
    /**
     * 出发位置
     */
    private QueryTrackPosition from;
    /**
     * 当前位置
     */
    private QueryTrackPosition cur;
    /**
     * 收货地
     */
    private QueryTrackPosition to;

    public QueryTrackPosition getFrom() {
        return from;
    }

    public void setFrom(QueryTrackPosition from) {
        this.from = from;
    }

    public QueryTrackPosition getCur() {
        return cur;
    }

    public void setCur(QueryTrackPosition cur) {
        this.cur = cur;
    }

    public QueryTrackPosition getTo() {
        return to;
    }

    public void setTo(QueryTrackPosition to) {
        this.to = to;
    }
}
