package com.yunyou.modules.interfaces.gps.e6.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.modules.interfaces.gps.GpsNewestLocationInfo;
import com.yunyou.modules.interfaces.gps.GpsRunTrackInfo;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.e6.E6Client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yunyou.modules.interfaces.gps.e6.constant.ApiPathList.*;
import static com.yunyou.modules.interfaces.gps.e6.constant.Constants.ACCESS_ID;

/**
 * E6接口工具类
 *
 * @author yunyou
 * @since 2021/6/25 18:53
 */
public class E6Util {

    /**
     * 获取用户车辆的最新位置信息
     *
     * @param vehicleNos 车牌号
     */
    public static List<GpsNewestLocationInfo> getNewestLocationInfoByVehicleNo(List<String> vehicleNos) throws Exception {
        List<GpsNewestLocationInfo> records = Lists.newArrayList();

        Map<String, String> params = Maps.newHashMap();
        params.put("method", "GetVehcileInfo");
        params.put("appkey", ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("vehicle", String.join(",", vehicleNos));
        params.put("isoffsetlonlat", "1");// 是否返回其他经纬度 2.百度(BD-09) 1.偏移经纬度(GCJ-02) 0.不需要
        params.put("sessionid", "");
        params.put("sign", SignUtil.sign(params));
        Response response = E6Client.get(GET_VEHICLE_INFO, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (!res.containsKey("result")) {
            return records;
        }
        JSONObject result = res.getJSONObject("result");
        if (result.getInteger("code") != 1) {
            return records;
        }
        JSONArray array = result.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            String vehicleNo = data.getString("Vehicle");
            // GPS时间：yyyy-MM-dd HH:mm:ss
            String gpsTime = data.getString("GPSTime");
            // WGS-84坐标
            Double lon = data.getDouble("Lon");
            Double lat = data.getDouble("Lat");
            // GCJ-02坐标
            Double lon02 = data.getDouble("Lon02");
            Double lat02 = data.getDouble("Lat02");
            // 速度
            Integer speed = data.getInteger("Speed");
            // 偏向
            Integer direction = data.getInteger("Direction");
            // 位置
            String placeName = data.getString("PlaceName");
            // 路名
            String roadName = data.getString("RoadName");
            // 里程表
            Double odometer = data.getDouble("Odometer");
            // 温度
            Double t1 = data.getDouble("T1");
            Double t2 = data.getDouble("T2");
            Double t3 = data.getDouble("T3");
            Double t4 = data.getDouble("T4");
            String time1 = data.getString("Time1");
            String time2 = data.getString("Time2");
            String time3 = data.getString("Time3");
            String time4 = data.getString("Time4");
            GpsNewestLocationInfo.GpsTemperatureInfo temperatureInfo = new GpsNewestLocationInfo.GpsTemperatureInfo();
            temperatureInfo.setT1(t1);
            temperatureInfo.setT2(t2);
            temperatureInfo.setT3(t3);
            temperatureInfo.setT4(t4);
            temperatureInfo.setTime1(time1);
            temperatureInfo.setTime2(time2);
            temperatureInfo.setTime3(time3);
            temperatureInfo.setTime4(time4);
            // 湿度
            Double h1 = data.getDouble("H1");
            Double h2 = data.getDouble("H2");
            Double h3 = data.getDouble("H3");
            Double h4 = data.getDouble("H4");
            String ht1 = data.getString("HT1");
            String ht2 = data.getString("HT2");
            String ht3 = data.getString("HT3");
            String ht4 = data.getString("HT4");
            GpsNewestLocationInfo.GpsHumidityInfo humidityInfo = new GpsNewestLocationInfo.GpsHumidityInfo();
            humidityInfo.setH1(h1);
            humidityInfo.setH2(h2);
            humidityInfo.setH3(h3);
            humidityInfo.setH4(h4);
            humidityInfo.setTime1(ht1);
            humidityInfo.setTime2(ht2);
            humidityInfo.setTime3(ht3);
            humidityInfo.setTime4(ht4);

            GpsNewestLocationInfo info = new GpsNewestLocationInfo();
            info.setNo(vehicleNo);
            info.setTime(gpsTime);
            info.setLon(lon02);
            info.setLat(lat02);
            info.setSpeed(speed);
            info.setCourse(direction);
            info.setAddress(StringUtils.trimToNull(placeName) + StringUtils.trimToNull(roadName));
            info.setOdometer(odometer);
            info.setTemperature(temperatureInfo);
            info.setHumidity(humidityInfo);
            records.add(info);
        }
        return records;
    }

    /**
     * 根据车牌号获取行驶轨迹
     *
     * @param vehicleNo 车牌号
     * @param from      时间从
     * @param to        时间到
     */
    public static List<GpsRunTrackInfo> getRunTrackInfoByVehicleNo(String vehicleNo, Date from, Date to) throws Exception {
        List<GpsRunTrackInfo> records = Lists.newArrayList();

        Map<String, String> params = Maps.newHashMap();
        params.put("method", "GetTrackDetail");
        params.put("appkey", ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("vehicle", vehicleNo);
        params.put("btime", DateUtils.formatDate(from, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("etime", DateUtils.formatDate(to, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("isoffsetlonlat", "1");// 是否返回其他经纬度 2.百度(BD-09) 1.偏移经纬度(GCJ-02) 0.不需要
        params.put("isplacename", "1");// 是否返回车辆位置以及路名信息 1.需要 0.不需要
        params.put("sign", SignUtil.sign(params));
        Response response = E6Client.get(GET_TRACK_DETAIL, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (!res.containsKey("result")) {
            return records;
        }
        JSONObject result = res.getJSONObject("result");
        if (result.getInteger("code") != 1) {
            return records;
        }
        JSONArray array = result.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            // GPS时间：yyyy-MM-dd HH:mm:ss
            String gpsTime = data.getString("GPSTime");
            // WGS-84坐标
            Double lon = data.getDouble("Lon");
            Double lat = data.getDouble("Lat");
            // GCJ-02坐标
            Double lon02 = data.getDouble("Lon02");
            Double lat02 = data.getDouble("Lat02");
            // 速度
            Integer speed = data.getInteger("Speed");
            // 偏向
            Integer direction = data.getInteger("Direction");
            // 位置
            String placeName = data.getString("PlaceName");
            // 路名
            String roadName = data.getString("RoadName");
            // 里程表
            Double odometer = data.getDouble("Odometer");

            GpsRunTrackInfo info = new GpsRunTrackInfo();
            info.setNo(vehicleNo);
            info.setTime(gpsTime);
            info.setLon(lon02);
            info.setLat(lat02);
            info.setSpeed(speed);
            info.setCourse(direction);
            info.setAddress(StringUtils.trimToNull(placeName) + StringUtils.trimToNull(roadName));
            info.setOdometer(odometer);
            records.add(info);
        }
        return records;
    }

    /**
     * 获取设备实时数据
     *
     * @param gpsNos 设备号
     */
    public static List<GpsNewestLocationInfo> getNewestLocationInfoByGpsNo(List<String> gpsNos) throws Exception {
        List<GpsNewestLocationInfo> records = Lists.newArrayList();

        Map<String, String> params = Maps.newHashMap();
        params.put("method", "getEquipInfoReal");
        params.put("appkey", ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("equipcode", String.join(",", gpsNos));
        params.put("isoffsetlonlat", "1");// 是否返回其他经纬度 2.百度(BD-09) 1.偏移经纬度(GCJ-02) 0.标准经纬度（WGS84） -1.不需要
        params.put("sessionid", "");
        params.put("sign", SignUtil.sign(params));
        Response response = E6Client.get(GET_EQUIP_INFO_REAL, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (res.getInteger("code") != 1) {
            return records;
        }
        JSONArray array = res.getJSONArray("result");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            String gpsNo = data.getString("equipCode");
            // GPS时间：yyyy-MM-dd HH:mm:ss
            String gpsTime = data.getString("gpsTime");
            // 坐标
            Double lon = data.getDouble("lon");
            Double lat = data.getDouble("lat");
            // 速度
            Integer speed = data.getInteger("speed");
            // 偏向
            Integer direction = data.getInteger("direction");
            // 省
            String province = data.getString("provice");
            // 市
            String city = data.getString("city");
            // 区
            String district = data.getString("district");
            // 路名
            String roadName = data.getString("roadName");
            // 里程表
            Double odometer = data.getDouble("odometer");
            // 温度
            Double t1 = data.getDouble("t1");
            Double t2 = data.getDouble("t2");
            Double t3 = data.getDouble("t3");
            Double t4 = data.getDouble("t4");
            String time1 = data.getString("tempTime1");
            String time2 = data.getString("tempTime2");
            String time3 = data.getString("tempTime3");
            String time4 = data.getString("tempTime4");
            GpsNewestLocationInfo.GpsTemperatureInfo temperatureInfo = new GpsNewestLocationInfo.GpsTemperatureInfo();
            temperatureInfo.setT1(t1);
            temperatureInfo.setT2(t2);
            temperatureInfo.setT3(t3);
            temperatureInfo.setT4(t4);
            temperatureInfo.setTime1(time1);
            temperatureInfo.setTime2(time2);
            temperatureInfo.setTime3(time3);
            temperatureInfo.setTime4(time4);
            // 湿度
            Double h1 = data.getDouble("h1");
            Double h2 = data.getDouble("h2");
            Double h3 = data.getDouble("h3");
            Double h4 = data.getDouble("h4");
            String ht1 = data.getString("humiTime1");
            String ht2 = data.getString("humiTime2");
            String ht3 = data.getString("humiTime3");
            String ht4 = data.getString("humiTime4");
            GpsNewestLocationInfo.GpsHumidityInfo humidityInfo = new GpsNewestLocationInfo.GpsHumidityInfo();
            humidityInfo.setH1(h1);
            humidityInfo.setH2(h2);
            humidityInfo.setH3(h3);
            humidityInfo.setH4(h4);
            humidityInfo.setTime1(ht1);
            humidityInfo.setTime2(ht2);
            humidityInfo.setTime3(ht3);
            humidityInfo.setTime4(ht4);

            GpsNewestLocationInfo info = new GpsNewestLocationInfo();
            info.setNo(gpsNo);
            info.setTime(gpsTime);
            info.setLon(lon);
            info.setLat(lat);
            info.setSpeed(speed);
            info.setCourse(direction);
            info.setProvince(StringUtils.trimToNull(province));
            info.setCity(StringUtils.trimToNull(city));
            info.setDistrict(StringUtils.trimToNull(district));
            info.setAddress(StringUtils.trimToNull(province) + StringUtils.trimToNull(city) + StringUtils.trimToNull(district) + StringUtils.trimToNull(roadName));
            info.setOdometer(odometer);
            info.setTemperature(temperatureInfo);
            info.setHumidity(humidityInfo);
            records.add(info);
        }
        return records;
    }

    /**
     * 获取指定时间设备数据
     *
     * @param gpsNo 设备号
     * @param from  时间从
     * @param to    时间到
     */
    public static List<GpsRunTrackInfo> getRunTrackInfoByGpsNo(String gpsNo, Date from, Date to) throws Exception {
        List<GpsRunTrackInfo> records = Lists.newArrayList();

        Map<String, String> params = Maps.newHashMap();
        params.put("method", "getEquipInfoHistory");
        params.put("appkey", ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("equipcode", gpsNo);
        params.put("btime", DateUtils.formatDate(from, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("etime", DateUtils.formatDate(to, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("isoffsetlonlat", "1");// 是否返回其他经纬度 2.百度(BD-09) 1.偏移经纬度(GCJ-02) 0.标准经纬度(WGS84) -1.不需要要
        params.put("second", "30");
        params.put("sign", SignUtil.sign(params));
        Response response = E6Client.get(GET_EQUIP_INFO_HISTORY, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (res.getInteger("code") != 1) {
            return records;
        }
        JSONArray array = res.getJSONArray("result");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            // GPS时间：yyyy-MM-dd HH:mm:ss
            String gpsTime = data.getString("gpsTime");
            // 坐标
            Double lon = data.getDouble("lon");
            Double lat = data.getDouble("lat");
            // 速度
            Integer speed = data.getInteger("speed");
            // 偏向
            Integer direction = data.getInteger("direction");
            // 省
            String province = data.getString("provice");
            // 市
            String city = data.getString("city");
            // 区
            String district = data.getString("district");
            // 路名
            String roadName = data.getString("roadName");
            // 里程表
            Double odometer = data.getDouble("odometer");

            GpsRunTrackInfo info = new GpsRunTrackInfo();
            info.setNo(gpsNo);
            info.setTime(gpsTime);
            info.setLon(lon);
            info.setLat(lat);
            info.setSpeed(speed);
            info.setCourse(direction);
            info.setAddress(StringUtils.trimToNull(province) + StringUtils.trimToNull(city) + StringUtils.trimToNull(district) + StringUtils.trimToNull(roadName));
            info.setOdometer(odometer);
            records.add(info);
        }
        return records;
    }
}
