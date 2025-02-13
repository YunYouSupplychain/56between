package com.yunyou.modules.tms.common.map.geo;

/**
 * 多边形
 */
public class Polygon {
    // 折线的点数组
    public Point[] path;

    public Polygon(Point[] path) {
        this.path = path;
    }
}
