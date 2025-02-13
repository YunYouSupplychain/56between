package com.yunyou.modules.tms.common.map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.map.exception.AddressToPointException;
import com.yunyou.modules.tms.common.map.exception.PointToAddressException;
import com.yunyou.modules.tms.common.map.exception.RoutePlanningException;
import com.yunyou.modules.tms.common.map.geo.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地图工具类
 */
public class MapUtil {
    private static Logger logger = LoggerFactory.getLogger(MapUtil.class);

    /**
     * 地址转坐标
     *
     * @param address 详细的结构化地址
     */
    public static Point getPoint(String address) throws IOException {
        if (MapConstants.USE_MAP.equals(MapConstants.B_MAP)) {
            return getBMapPoint(address);
        } else {
            return getGMapSearch(address);
        }
    }

    /**
     * 获取路线规划最大距离
     *
     * @param origin      起点
     * @param destination 终点
     */
    public static Double getMaxDirectionDistance(Point origin, Point destination) throws IOException {
        if (MapConstants.USE_MAP.equals(MapConstants.B_MAP)) {
            return getBMapMaxDirectionDistance(origin, destination);
        } else {
            return getGMapMaxDirectionDistance(origin, destination);
        }
    }

    /**
     * 坐标转地址
     *
     * @param point 坐标
     * @param type  坐标类型
     */
    public static String getAddress(Point point, String type) throws IOException {
        if (MapConstants.USE_MAP.equals(MapConstants.B_MAP)) {
            return getBMapAddress(point, type);
        } else {
            return getGMapAddress(point);
        }
    }

    /**
     * 坐标转地址
     *
     * @param points 坐标
     * @param type   坐标类型
     */
    public static List<String> getAddress(List<Point> points, String type) throws IOException {
        if (MapConstants.USE_MAP.equals(MapConstants.B_MAP)) {
            return points.stream().map(o -> {
                try {
                    return getAddress(o, type);
                } catch (IOException e) {
                    return null;
                }
            }).collect(Collectors.toList());
        } else {
            return getGMapAddress(points);
        }
    }


    /**
     * 地址转坐标 - 百度地图
     *
     * @param address 详细的结构化地址
     */
    private static Point getBMapPoint(String address) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://api.map.baidu.com/geocoding/v3?ak=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&output=json&address=" + URLEncoder.encode(address));
        if (StringUtils.isBlank(res)) {
            throw new IOException("(地址解析)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (json.getInteger("status") != 0) {
            logger.error("百度地图地址转坐标失败！返回内容：" + res);
            throw new AddressToPointException("地址解析失败");
        }
        JSONObject location = json.getJSONObject("result").getJSONObject("location");
        return new Point(location.getDouble("lng"), location.getDouble("lat"));
    }

    /**
     * 获取路线规划 - 百度地图
     *
     * @param origin      起点
     * @param destination 终点
     */
    private static String getBMapDirection(Point origin, Point destination) {
        return HttpClientUtil.getInstance().sendHttpGet("https://api.map.baidu.com/direction/v2/driving?ak=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&origin=" + URLEncoder.encode(origin.getLatLng()) + "&destination=" + URLEncoder.encode(destination.getLatLng()) + "&alternatives=1");
    }

    /**
     * 获取路线规划最大距离 - 百度地图
     *
     * @param origin      起点
     * @param destination 终点
     */
    private static Double getBMapMaxDirectionDistance(Point origin, Point destination) throws IOException {
        if (origin == null || destination == null) {
            return 0D;
        }
        String res = getBMapDirection(origin, destination);
        if (StringUtils.isBlank(res)) {
            throw new IOException("(路线规划)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (json.getInteger("status") != 0) {
            logger.error("起点坐标：[" + origin.getLngLat() + "], 终点坐标：[" + destination.getLngLat() + "], 返回内容：" + res);
            throw new RoutePlanningException(json.getString("message"));
        }
        double distance = 0D;
        JSONArray array = json.getJSONObject("result").getJSONArray("routes");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            double v = next.getDouble("distance") / 1000;
            if (v > distance) {
                distance = v;
            }
        }
        return distance;
    }

    /**
     * 坐标转地址 - 百度地图
     *
     * @param point 坐标
     * @param type  坐标类型
     */
    private static String getBMapAddress(Point point, String type) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://api.map.baidu.com/reverse_geocoding/v3?ak=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&output=json&location=" + URLEncoder.encode(point.getLatLng()) + "&coordtype=" + URLEncoder.encode(type));
        if (StringUtils.isBlank(res)) {
            throw new IOException("(坐标解析)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (json.getInteger("status") != 0) {
            logger.error("百度地图坐标转地址失败！返回内容：" + res);
            throw new PointToAddressException("坐标解析失败");
        }
        return json.getString("formatted_address");
    }


    /**
     * 地址转坐标 - 高德地图
     *
     * @param address 详细的结构化地址
     */
    private static Point getGMapPoint(String address) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://restapi.amap.com/v3/geocode/geo?key=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&address=" + URLEncoder.encode(address));
        if (StringUtils.isBlank(res)) {
            throw new IOException("(地址解析)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (!"1".equals(json.getString("status"))) {
            logger.error("高德地图地址转坐标失败！返回内容：" + res);
            throw new AddressToPointException("地址解析失败，" + json.getString("info"));
        }
        if (json.getInteger("count") == 0) {
            throw new AddressToPointException("地址解析失败，未找到该地址");
        }
        String[] location = json.getJSONArray("geocodes").getJSONObject(0).getString("location").split(",");
        return new Point(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
    }

    /**
     * 获取路线规划 - 高德地图
     *
     * @param origin      起点
     * @param destination 终点
     */
    private static String getGMapDirection(Point origin, Point destination) {
        return HttpClientUtil.getInstance().sendHttpGet("https://restapi.amap.com/v3/direction/driving?key=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&origin=" + URLEncoder.encode(origin.getLngLat()) + "&destination=" + URLEncoder.encode(destination.getLngLat()) + "&strategy=0&nosteps=1");
    }

    /**
     * 获取路线规划最大距离 - 高德地图
     *
     * @param origin      起点
     * @param destination 终点
     */
    private static Double getGMapMaxDirectionDistance(Point origin, Point destination) throws IOException {
        if (origin == null || destination == null) {
            return 0D;
        }
        String res = getGMapDirection(origin, destination);
        if (StringUtils.isBlank(res)) {
            throw new IOException("(路线规划)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (!"1".equals(json.getString("status"))) {
            logger.error("起点坐标：[" + origin.getLngLat() + "], 终点坐标：[" + destination.getLngLat() + "], 返回内容：" + res);
            throw new RoutePlanningException(json.getString("info"));
        }
        if (json.getInteger("count") == 0) {
            throw new RoutePlanningException("路线规划失败，未找到路径规划方案");
        }
        double distance = 0D;
        JSONArray array = json.getJSONObject("route").getJSONArray("paths");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            double v = next.getDouble("distance") / 1000;
            if (v > distance) {
                distance = v;
            }
        }
        return distance;
    }

    /**
     * 坐标转地址 - 高德地图
     *
     * @param point 坐标
     */
    private static String getGMapAddress(Point point) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://restapi.amap.com/v3/geocode/regeo?key=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&output=json&location=" + URLEncoder.encode(point.getLngLat()));
        if (StringUtils.isBlank(res)) {
            throw new IOException("(坐标解析)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (!"1".equals(json.getString("status"))) {
            logger.error("高德地图坐标转地址失败！返回内容：" + res);
            throw new PointToAddressException("坐标解析失败");
        }
        return json.getJSONObject("regeocode").getString("formatted_address");
    }

    /**
     * 坐标转地址 - 高德地图
     *
     * @param points 坐标
     */
    private static List<String> getGMapAddress(List<Point> points) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://restapi.amap.com/v3/geocode/regeo?key=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&output=json&batch=true&location=" + URLEncoder.encode(points.stream().map(Point::getLngLat).collect(Collectors.joining("|"))));
        if (StringUtils.isBlank(res)) {
            throw new IOException("(坐标解析)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (!"1".equals(json.getString("status"))) {
            logger.error("高德地图坐标转地址失败！返回内容：" + res);
            throw new PointToAddressException("坐标解析失败");
        }
        return json.getJSONArray("regeocodes").stream().map(o -> ((JSONObject) o).getString("formatted_address")).collect(Collectors.toList());
    }

    /**
     * 地址搜索 - 高德地图
     *
     * @param address 详细的结构化地址
     */
    private static Point getGMapSearch(String address) throws IOException {
        String res = HttpClientUtil.getInstance().sendHttpGet("https://restapi.amap.com/v3/place/text?key=" + SysControlParamsUtils.getValue(SysParamConstants.MAP_WEB_SERVER_AK) + "&keywords=" + URLEncoder.encode(address) + "&offset=1");
        if (StringUtils.isBlank(res)) {
            throw new IOException("(地址搜索)请求地图连接失败");
        }
        JSONObject json = JSONObject.parseObject(res);
        if (!"1".equals(json.getString("status"))) {
            logger.error("高德地图地址搜索失败！返回内容：" + res);
            throw new AddressToPointException("地址搜索失败，" + json.getString("info"));
        }
        if (json.getInteger("count") == 0) {
            throw new AddressToPointException("地址搜索失败，未找到该地址");
        }
        String[] location = json.getJSONArray("pois").getJSONObject(0).getString("location").split(",");
        return new Point(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
    }
}