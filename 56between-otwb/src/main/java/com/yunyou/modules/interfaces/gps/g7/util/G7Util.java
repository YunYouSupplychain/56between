package com.yunyou.modules.interfaces.gps.g7.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.modules.interfaces.gps.GpsNewestLocationInfo;
import com.yunyou.modules.interfaces.gps.GpsRunTrackInfo;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.g7.G7Client;
import com.yunyou.modules.interfaces.gps.g7.constant.ApiPathList;
import com.yunyou.modules.interfaces.gps.g7.entity.VehicleCurrentTempInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * G7接口工具类
 *
 * @author yunyou
 * @since 2021/6/25 16:21
 */
public class G7Util {

    /**
     * 获取用户车辆的最新位置信息
     *
     * @param vehicleNos 车牌号
     */
    public static List<GpsNewestLocationInfo> getNewestLocationInfoByVehicleNo(List<String> vehicleNos) throws Exception {
        List<GpsNewestLocationInfo> records = Lists.newArrayList();

        Map<String, Object> params = Maps.newHashMap();
        params.put("plate_nums", vehicleNos);
        params.put("fields", Lists.newArrayList("loc", "cold"));
        params.put("addr_required", true);
        Response response = G7Client.post(ApiPathList.GET_TRUCK_INFO, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (res.getInteger("code") != 0 && res.getInteger("sub_code") != 0) {
            return records;
        }
        JSONObject data = res.getJSONObject("data");
        for (String vehicleNo : vehicleNos) {
            JSONObject o = data.getJSONObject(vehicleNo).getJSONObject("data");
            JSONObject loc = o.getJSONObject("loc");
            JSONObject cold = o.getJSONObject("cold");
            List<VehicleCurrentTempInfo> temps = JSON.parseObject(cold.getString("temp"))
                    .getJSONArray("temps").toJavaList(VehicleCurrentTempInfo.class);

            GpsNewestLocationInfo result = new GpsNewestLocationInfo();
            result.setTime(loc.getString("gps_time"));
            result.setLon(loc.getDouble("lng"));
            result.setLat(loc.getDouble("lat"));
            result.setAddress(loc.getString("address"));
            result.setSpeed(loc.getInteger("speed"));
            result.setCourse(loc.getInteger("course"));
            GpsNewestLocationInfo.GpsTemperatureInfo temperatureInfo = new GpsNewestLocationInfo.GpsTemperatureInfo();
            for (VehicleCurrentTempInfo temp : temps) {
                if (temp.getNum() == 1) {
                    temperatureInfo.setT1(temp.getTemperature());
                } else if (temp.getNum() == 2) {
                    temperatureInfo.setT2(temp.getTemperature());
                } else if (temp.getNum() == 3) {
                    temperatureInfo.setT3(temp.getTemperature());
                } else if (temp.getNum() == 4) {
                    temperatureInfo.setT4(temp.getTemperature());
                }
            }
            result.setTemperature(temperatureInfo);
            records.add(result);
        }
        return records;
    }

    /**
     * 获取指定时间行驶轨迹
     *
     * @param vehicleNo 车牌号
     * @param from      时间从
     * @param to        时间到
     */
    public static List<GpsRunTrackInfo> getRunTrackInfoByVehicleNo(String vehicleNo, Date from, Date to) throws Exception {
        List<GpsRunTrackInfo> records = Lists.newArrayList();

        Map<String, String> queries = Maps.newHashMap();
        queries.put("plate_num", vehicleNo);
        queries.put("from", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, from));
        queries.put("to", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, to));
        queries.put("map", "baidu");
        queries.put("timeInterval", "30");
        Response response = G7Client.get(ApiPathList.GET_TRACKS_BY_VEHICLE_NO, queries);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSONObject.parseObject(response.getBody());
        if (res.getInteger("code") == 0 && res.getInteger("sub_code") == 0) {
            List<GpsRunTrackInfo> tracks = Lists.newArrayList();
            JSONArray array = res.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject data = array.getJSONObject(i);
                // GPS时间：yyyy-MM-dd HH:mm:ss
                String gpsTime = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, data.getLong("time"));
                // 坐标
                Double lon = data.getDouble("lng");
                Double lat = data.getDouble("lat");
                // 速度
                Integer speed = data.getInteger("speed");
                // 偏向
                Integer course = data.getInteger("course");

                GpsRunTrackInfo info = new GpsRunTrackInfo();
                info.setNo(vehicleNo);
                info.setTime(gpsTime);
                info.setLon(lon);
                info.setLat(lat);
                info.setSpeed(speed);
                info.setCourse(course);
                records.add(info);
            }
            records.addAll(tracks);

            long maxTime = tracks.stream().map(GpsRunTrackInfo::getLongTime).max(Long::compareTo).get();
            long preMaxTime = maxTime;
            int size = tracks.size();
            // 一次最多返回1000条数据，变更参数开始时间滚动查询
            while (size == 1000) {
                // 以最大gps时间作为参数开始时间新值
                queries.put("from", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date(maxTime)));
                Response againResponse = G7Client.get(ApiPathList.GET_TRACKS_BY_VEHICLE_NO, queries);
                JSONObject againRes = JSONObject.parseObject(againResponse.getBody());
                if (againRes.getInteger("code") == 0 && againRes.getInteger("sub_code") == 0) {
                    List<GpsRunTrackInfo> trackList = Lists.newArrayList();
                    JSONArray againArray = againRes.getJSONArray("data");
                    for (int i = 0; i < againArray.size(); i++) {
                        JSONObject data = againArray.getJSONObject(i);
                        // GPS时间：yyyy-MM-dd HH:mm:ss
                        String gpsTime = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, data.getLong("time"));
                        // 坐标
                        Double lon = data.getDouble("lng");
                        Double lat = data.getDouble("lat");
                        // 速度
                        Integer speed = data.getInteger("speed");
                        // 偏向
                        Integer course = data.getInteger("course");

                        GpsRunTrackInfo info = new GpsRunTrackInfo();
                        info.setNo(vehicleNo);
                        info.setTime(gpsTime);
                        info.setLon(lon);
                        info.setLat(lat);
                        info.setSpeed(speed);
                        info.setCourse(course);
                        trackList.add(info);
                    }
                    if (CollectionUtil.isNotEmpty(trackList)) {
                        records.addAll(trackList);

                        maxTime = trackList.stream().map(GpsRunTrackInfo::getLongTime).max(Long::compareTo).get();
                        size = trackList.size();
                        if (maxTime > preMaxTime) {
                            preMaxTime = maxTime;
                            continue;
                        }
                    }
                }
                break;
            }
        }
        return records;
    }

    /**
     * 获取用户车辆的最新位置信息
     *
     * @param gpsNos 设备号
     */
    public static List<GpsNewestLocationInfo> getNewestLocationInfoByGpsNo(List<String> gpsNos) throws Exception {
        List<GpsNewestLocationInfo> records = Lists.newArrayList();

        Map<String, Object> params = Maps.newHashMap();
        params.put("gpsnos", String.join(",", gpsNos));
        Response response = G7Client.post(ApiPathList.GET_EQUIP_INFO, params);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSON.parseObject(response.getBody());
        if (res.getInteger("code") != 0 && res.getInteger("sub_code") != 0) {
            return records;
        }
        JSONArray array = res.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            String gpsNo = data.getString("gpsno");
            // GPS时间：yyyy-MM-dd HH:mm:ss
            String gpsTime = data.getString("gps_time");
            Double lon = data.getDouble("lng");
            Double lat = data.getDouble("lat");
            // 速度
            Integer speed = data.getInteger("speed");
            // 偏向
            Integer course = data.getInteger("course");

            GpsNewestLocationInfo info = new GpsNewestLocationInfo();
            info.setNo(gpsNo);
            info.setTime(gpsTime);
            info.setLon(lon);
            info.setLat(lat);
            info.setSpeed(speed);
            info.setCourse(course);
            records.add(info);
        }
        return records;
    }

    /**
     * 获取指定时间行驶轨迹
     *
     * @param gpsNo 设备号
     * @param from  时间从
     * @param to    时间到
     */
    public static List<GpsRunTrackInfo> getRunTrackInfoByGpsNo(String gpsNo, Date from, Date to) throws Exception {
        List<GpsRunTrackInfo> records = Lists.newArrayList();

        Map<String, String> queries = Maps.newHashMap();
        queries.put("gpsno", gpsNo);
        queries.put("from", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, from));
        queries.put("to", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, to));
        queries.put("map", "baidu");
        queries.put("timeInterval", "30");
        Response response = G7Client.get(ApiPathList.GET_TRACKS_BY_GPS_NO, queries);
        if (response == null || response.getStatusCode() != 200 || StringUtils.isBlank(response.getBody())) {
            return records;
        }
        JSONObject res = JSONObject.parseObject(response.getBody());
        if (res.getInteger("code") == 0 && res.getInteger("sub_code") == 0) {
            List<GpsRunTrackInfo> tracks = Lists.newArrayList();
            JSONArray array = res.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject data = array.getJSONObject(i);
                // GPS时间：yyyy-MM-dd HH:mm:ss
                String gpsTime = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, data.getLong("time"));
                // 坐标
                Double lon = data.getDouble("lng");
                Double lat = data.getDouble("lat");
                // 速度
                Integer speed = data.getInteger("speed");
                // 偏向
                Integer course = data.getInteger("course");

                GpsRunTrackInfo info = new GpsRunTrackInfo();
                info.setNo(gpsNo);
                info.setTime(gpsTime);
                info.setLon(lon);
                info.setLat(lat);
                info.setSpeed(speed);
                info.setCourse(course);
                records.add(info);
            }
            records.addAll(tracks);

            long maxTime = tracks.stream().map(GpsRunTrackInfo::getLongTime).max(Long::compareTo).get();
            long preMaxTime = maxTime;
            int size = tracks.size();
            // 一次最多返回1000条数据，变更参数开始时间滚动查询
            while (size == 1000) {
                // 以最大gps时间作为参数开始时间新值
                queries.put("from", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date(maxTime)));
                Response againResponse = G7Client.get(ApiPathList.GET_TRACKS_BY_GPS_NO, queries);
                JSONObject againRes = JSONObject.parseObject(againResponse.getBody());
                if (againRes.getInteger("code") == 0 && againRes.getInteger("sub_code") == 0) {
                    List<GpsRunTrackInfo> trackList = Lists.newArrayList();
                    JSONArray againArray = againRes.getJSONArray("data");
                    for (int i = 0; i < againArray.size(); i++) {
                        JSONObject data = againArray.getJSONObject(i);
                        // GPS时间：yyyy-MM-dd HH:mm:ss
                        String gpsTime = DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, data.getLong("time"));
                        // 坐标
                        Double lon = data.getDouble("lng");
                        Double lat = data.getDouble("lat");
                        // 速度
                        Integer speed = data.getInteger("speed");
                        // 偏向
                        Integer course = data.getInteger("course");

                        GpsRunTrackInfo info = new GpsRunTrackInfo();
                        info.setNo(gpsNo);
                        info.setTime(gpsTime);
                        info.setLon(lon);
                        info.setLat(lat);
                        info.setSpeed(speed);
                        info.setCourse(course);
                        trackList.add(info);
                    }
                    if (CollectionUtil.isNotEmpty(trackList)) {
                        records.addAll(trackList);

                        maxTime = trackList.stream().map(GpsRunTrackInfo::getLongTime).max(Long::compareTo).get();
                        size = trackList.size();
                        if (maxTime > preMaxTime) {
                            preMaxTime = maxTime;
                            continue;
                        }
                    }
                }
                break;
            }
        }
        return records;
    }

}
