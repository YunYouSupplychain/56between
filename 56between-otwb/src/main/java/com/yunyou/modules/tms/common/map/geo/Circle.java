package com.yunyou.modules.tms.common.map.geo;

/**
 * 圆形
 */
public class Circle {
    // 中心点
    public Point center;
    // 半径
    public Double radius;

    public Circle(Point center, Double radius) {
        this.center = center;
        this.radius = radius;
    }
}
