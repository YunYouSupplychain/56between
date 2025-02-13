package com.yunyou.modules.tms.common.map.geo;

/**
 * GeoUtils类提供若干几何算法，用来帮助判断点与矩形、圆形、多边形线、多边形面的关系(百度地图)
 */
public class GeoUtils {
    /**
     * 地球半径
     */
    public static final double EARTHRADIUS = 6370996.81;

    /**
     * 点是否在矩阵内
     *
     * @param point  测试点
     * @param bounds 矩阵边界点
     */
    public static boolean isPointInRect(Point point, Point[] bounds) {
        Point sw = getSouthWest(bounds); //西南脚点
        Point ne = getNorthEast(bounds); //东北脚点
        return point.lng >= sw.lng
                && point.lng <= ne.lng
                && point.lat >= sw.lat
                && point.lat <= ne.lat;
    }

    /**
     * 点是否在圆内
     *
     * @param point  测试点
     * @param circle 圆
     */
    public static boolean isPointInCircle(Point point, Circle circle) {
        double distance = getDistance(point, circle.center);
        return distance <= circle.radius;
    }

    /**
     * 点是否在折线上
     *
     * @param point    测试点
     * @param polyline 折线
     */
    public static boolean isPointOnPolyline(Point point, Polyline polyline) {
        //首先判断点是否在线的外包矩形内，如果在，则进一步判断，否则返回false
        if (!isPointInRect(point, polyline.path)) {
            return false;
        }
        //判断点是否在线段上，设点为Q，线段为P1P2，判断点Q在该线段上的依据是：( Q - P1 ) × ( P2 - P1 ) = 0，且 Q 在以 P1，P2为对角顶点的矩形内
        for (int i = 0; i < polyline.path.length - 1; i++) {
            Point curPt = polyline.path[i];
            Point nextPt = polyline.path[i + 1];
            //首先判断point是否在curPt和nextPt之间，即：此判断该点是否在该线段的外包矩形内
            if (point.lng >= Math.min(curPt.lng, nextPt.lng)
                    && point.lng <= Math.max(curPt.lng, nextPt.lng)
                    && point.lat >= Math.min(curPt.lat, nextPt.lat)
                    && point.lat <= Math.max(curPt.lat, nextPt.lat)) {
                //判断点是否在直线上公式
                double precision = (curPt.lng - point.lng) * (nextPt.lat - point.lat)
                        - (nextPt.lng - point.lng) * (curPt.lat - point.lat);
                if (precision < 2e-9 && precision > -2e-9) {
                    //实质判断是否接近0
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 点是否在多边形内
     *
     * @param p       测试点
     * @param polygon 多边形
     */
    public static boolean isPointInPolygon(Point p, Polygon polygon) {
        //首先判断点是否在多边形的外包矩形内，如果在，则进一步判断，否则返回false
        if (!isPointInRect(p, polygon.path)) {
            return false;
        }
        //基本思想是利用射线法，计算射线与多边形各边的交点，如果是偶数，则点在多边形外，否则在多边形内。还会考虑一些特殊情况，如点在多边形顶点上，点在多边形边上等特殊情况。
        int N = polygon.path.length;
        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0; //cross points count of x
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point p1, p2; //neighbour bound vertices

        p1 = polygon.path[0]; //left vertex
        for (int i = 1; i <= N; ++i) {
            //check all rays
            if (p.equals(p1)) {
                return boundOrVertex; //p is an vertex
            }
            p2 = polygon.path[i % N]; //right vertex
            if (p.lat < Math.min(p1.lat, p2.lat) || p.lat > Math.max(p1.lat, p2.lat)) {
                //ray is outside of our interests
                p1 = p2;
                continue; //next ray left point
            }
            if (p.lat > Math.min(p1.lat, p2.lat) && p.lat < Math.max(p1.lat, p2.lat)) {
                //ray is crossing over by the algorithm (common part of)
                if (p.lng <= Math.max(p1.lng, p2.lng)) {
                    //x is before of ray
                    if (p1.lat.equals(p2.lat) && p.lng >= Math.min(p1.lng, p2.lng)) {
                        //overlies on a horizontal ray
                        return boundOrVertex;
                    }
                    if (p1.lng.equals(p2.lng)) {
                        //ray is vertical
                        if (p1.lng.equals(p.lng)) {
                            //overlies on a vertical ray
                            return boundOrVertex;
                        } else {
                            //before ray
                            ++intersectCount;
                        }
                    } else {
                        //cross point on the left side
                        double xinters = ((p.lat - p1.lat) * (p2.lng - p1.lng)) / (p2.lat - p1.lat) + p1.lng; //cross point of lng
                        if (Math.abs(p.lng - xinters) < precision) {
                            //overlies on a ray
                            return boundOrVertex;
                        }
                        if (p.lng < xinters) {
                            //before ray
                            ++intersectCount;
                        }
                    }
                }
            } else {
                //special case when ray is crossing through the vertex
                if (p.lat.equals(p2.lat) && p.lng <= p2.lng) {
                    //p crossing over p2
                    Point p3 = polygon.path[(i + 1) % N];//next vertex
                    if (p.lat >= Math.min(p1.lat, p3.lat) && p.lat <= Math.max(p1.lat, p3.lat)) {
                        //p.lat lies between p1.lat & p3.lat
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2; //next ray left point
        }
        //偶数在多边形外，奇数在多边形内
        return intersectCount % 2 != 0;
    }

    /**
     * 计算两点之间的距离,两点坐标必须为经纬度
     *
     * @param point1 点对象
     * @param point2 点对象
     * @return 两点之间距离，单位为米
     */
    public static double getDistance(Point point1, Point point2) {
        point1.lng = _getLoop(point1.lng, -180, 180);
        point1.lat = _getRange(point1.lat, -90, 90);
        point2.lng = _getLoop(point2.lng, -180, 180);
        point2.lat = _getRange(point2.lat, -90, 90);

        double x1 = GeoUtils.degreeToRad(point1.lng);
        double y1 = GeoUtils.degreeToRad(point1.lat);
        double x2 = GeoUtils.degreeToRad(point2.lng);
        double y2 = GeoUtils.degreeToRad(point2.lat);
        return EARTHRADIUS * Math.acos(Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1));
    }

    /**
     * 将v值限定在a,b之间，经度使用
     */
    private static double _getLoop(double v, double a, double b) {
        while (v > b) {
            v -= b - a;
        }
        while (v < a) {
            v += b - a;
        }
        return v;
    }

    /**
     * 将v值限定在a,b之间，纬度使用
     */
    private static double _getRange(double v, double a, double b) {
        v = Math.max(v, a);
        v = Math.min(v, b);
        return v;
    }

    /**
     * 将度转化为弧度
     *
     * @param degree 度
     */
    private static double degreeToRad(double degree) {
        return (Math.PI * degree) / 180;
    }

    /**
     * 将弧度转化为度
     *
     * @param radian 弧度
     */
    private static double radToDegree(double radian) {
        return (180 * radian) / Math.PI;
    }

    /**
     * 获取该组坐标点外包矩阵西南角的坐标点
     *
     * @param bounds 坐标点
     * @return 外包矩阵西南角的坐标点
     */
    private static Point getSouthWest(Point[] bounds) {
        double minLng = bounds[0].lng, minLat = bounds[0].lat;
        for (Point point : bounds) {
            double lng = point.lng;
            double lat = point.lat;
            if (lng < minLng) {
                minLng = lng;
            }
            if (lat < minLat) {
                minLat = lat;
            }
        }
        return new Point(minLng, minLat);
    }

    /**
     * 获取该组坐标点外包矩阵东北角的坐标点
     *
     * @param bounds 坐标点
     * @return 外包矩阵东北角的坐标点
     */
    private static Point getNorthEast(Point[] bounds) {
        double minLng = bounds[0].lng, minLat = bounds[0].lat;
        for (Point point : bounds) {
            double lng = point.lng;
            double lat = point.lat;
            if (lng > minLng) {
                minLng = lng;
            }
            if (lat > minLat) {
                minLat = lat;
            }
        }
        return new Point(minLng, minLat);
    }
}
