package com.yunyou.modules.tms.common.map.geo;

/**
 * 折线
 */
public class Polyline {
    // 折线的点数组
    public Point[] path;

    public Polyline(Point[] path) {
        this.path = path;
    }
}
