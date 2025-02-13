package com.yunyou.modules.tms.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.e6.E6Client;
import com.yunyou.modules.interfaces.gps.e6.entity.EquipmentRealInfoByGpsNoResponse;
import com.yunyou.modules.interfaces.gps.e6.util.SignUtil;
import com.yunyou.modules.interfaces.gps.g7.G7Client;
import com.yunyou.modules.interfaces.gps.g7.constant.ApiPathList;
import com.yunyou.modules.interfaces.gps.g7.entity.VehicleCurrentTempInfo;
import com.yunyou.modules.tms.common.GpsManufacturer;
import com.yunyou.modules.tms.report.entity.TmRepVehicleMonitoring;
import com.yunyou.modules.tms.report.mapper.TmRepVehicleMonitoringMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.yunyou.modules.interfaces.gps.e6.constant.ApiPathList.GET_EQUIP_INFO_REAL;

@Service
public class TmRepVehicleMonitoringService extends BaseService {
    @Autowired
    private TmRepVehicleMonitoringMapper mapper;

    public Page<TmRepVehicleMonitoring> findPage(Page<TmRepVehicleMonitoring> page, TmRepVehicleMonitoring entity) {
        entity.setPage(page);
        page.setList(this.findList(entity));
        return page;
    }

    private List<TmRepVehicleMonitoring> findList(TmRepVehicleMonitoring entity) {
        List<TmRepVehicleMonitoring> list = mapper.findList(entity);
        Map<String, List<TmRepVehicleMonitoring>> map = list.stream()
                .filter(o -> GpsManufacturer.G7.name().equals(o.getGpsManufacturer()) || GpsManufacturer.E6.name().equals(o.getGpsManufacturer()))
                .collect(Collectors.groupingBy(TmRepVehicleMonitoring::getGpsManufacturer));
        for (Map.Entry<String, List<TmRepVehicleMonitoring>> entry : map.entrySet()) {
            switch (GpsManufacturer.valueOf(entry.getKey())) {
                case G7:
                    Map<String, TmRepVehicleMonitoring> vehicleCurrentInfoFormG7 = getVehicleCurrentInfoFormG7(entry.getValue().stream().map(TmRepVehicleMonitoring::getVehicleNo).collect(Collectors.toSet()));
                    for (TmRepVehicleMonitoring o : list) {
                        if (!vehicleCurrentInfoFormG7.containsKey(o.getVehicleNo())) {
                            o.setGpsStatus(2);
                            continue;
                        }
                        TmRepVehicleMonitoring tmRepVehicleMonitoring = vehicleCurrentInfoFormG7.get(o.getVehicleNo());
                        o.setGpsStatus(tmRepVehicleMonitoring.getGpsStatus());
                        o.setTime(tmRepVehicleMonitoring.getTime());
                        o.setGpsTime(tmRepVehicleMonitoring.getGpsTime());
                        o.setLng(tmRepVehicleMonitoring.getLng());
                        o.setLat(tmRepVehicleMonitoring.getLat());
                        o.setAddress(tmRepVehicleMonitoring.getAddress());
                        o.setCourse(tmRepVehicleMonitoring.getCourse());
                        o.setAcquireTime(tmRepVehicleMonitoring.getAcquireTime());
                        o.setTemperature1(tmRepVehicleMonitoring.getTemperature1());
                        o.setTemperature2(tmRepVehicleMonitoring.getTemperature2());
                    }
                    break;
                case E6:
                    Map<String, TmRepVehicleMonitoring> vehicleCurrentInfoFormE6 = getVehicleCurrentInfoFormE6(entry.getValue().stream().map(TmRepVehicleMonitoring::getGpsNo).collect(Collectors.toSet()));
                    for (TmRepVehicleMonitoring o : list) {
                        if (!vehicleCurrentInfoFormE6.containsKey(o.getVehicleNo())) {
                            o.setGpsStatus(2);
                            continue;
                        }
                        TmRepVehicleMonitoring tmRepVehicleMonitoring = vehicleCurrentInfoFormE6.get(o.getGpsNo());
                        o.setGpsStatus(tmRepVehicleMonitoring.getGpsStatus());
                        o.setTime(tmRepVehicleMonitoring.getTime());
                        o.setGpsTime(tmRepVehicleMonitoring.getGpsTime());
                        o.setLng(tmRepVehicleMonitoring.getLng());
                        o.setLat(tmRepVehicleMonitoring.getLat());
                        o.setAddress(tmRepVehicleMonitoring.getAddress());
                        o.setCourse(tmRepVehicleMonitoring.getCourse());
                        o.setAcquireTime(tmRepVehicleMonitoring.getAcquireTime());
                        o.setTemperature1(tmRepVehicleMonitoring.getTemperature1());
                        o.setTemperature2(tmRepVehicleMonitoring.getTemperature2());
                    }
                    break;
                default:
                    break;
            }
        }
        return list.stream().sorted(Comparator.comparing(TmRepVehicleMonitoring::hasGps, Comparator.reverseOrder())
                        .thenComparing(TmRepVehicleMonitoring::getVehicleNo))
                .collect(Collectors.toList());
    }

    private Map<String, TmRepVehicleMonitoring> getVehicleCurrentInfoFormG7(Set<String> vehicleNos) {
        Map<String, TmRepVehicleMonitoring> rsMap = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("plate_nums", vehicleNos);
        params.put("fields", Lists.newArrayList("loc", "status", "cold"));
        params.put("addr_required", true);
        JSONObject data;
        try {
            Response response = G7Client.post(ApiPathList.GET_TRUCK_INFO, params);
            if (response == null || StringUtils.isBlank(response.getBody())) {
                return rsMap;
            }
            JSONObject res = JSON.parseObject(response.getBody());
            if (res.getInteger("code") != 0 && res.getInteger("sub_code") != 0) {
                return rsMap;
            }
            data = res.getJSONObject("data");
        } catch (Exception ignore) {
            return rsMap;
        }
        for (String vehicleNo : vehicleNos) {
            if (!data.containsKey(vehicleNo)) {
                continue;
            }
            try {
                JSONObject o = data.getJSONObject(vehicleNo).getJSONObject("data");
                JSONObject loc = o.getJSONObject("loc");
                JSONObject cold = o.getJSONObject("cold");
                List<VehicleCurrentTempInfo> temps = JSON.parseObject(cold.getString("temp")).getJSONArray("temps").toJavaList(VehicleCurrentTempInfo.class);

                TmRepVehicleMonitoring tmRepVehicleMonitoring = new TmRepVehicleMonitoring();
                tmRepVehicleMonitoring.setGpsStatus(o.getInteger("gpsStatus"));
                tmRepVehicleMonitoring.setTime(o.getDate("time"));

                tmRepVehicleMonitoring.setGpsTime(loc.getString("gps_time"));
                tmRepVehicleMonitoring.setLng(loc.getDouble("lng"));
                tmRepVehicleMonitoring.setLat(loc.getDouble("lat"));
                tmRepVehicleMonitoring.setAddress(loc.getString("address"));
                tmRepVehicleMonitoring.setSpeed(loc.getInteger("speed"));
                tmRepVehicleMonitoring.setCourse(loc.getInteger("course"));
                tmRepVehicleMonitoring.setLocationType(loc.getInteger("location_type"));

                tmRepVehicleMonitoring.setAcquireTime(cold.getString("time"));
                tmRepVehicleMonitoring.setEnvTemp(JSON.parseObject(cold.getString("temp")).getDouble("envTemp"));
                for (VehicleCurrentTempInfo temp : temps) {
                    if (temp.getNum() == 1) {
                        tmRepVehicleMonitoring.setTemperature1(temp.getTemperature());
                    } else if (temp.getNum() == 2) {
                        tmRepVehicleMonitoring.setTemperature2(temp.getTemperature());
                    } else if (temp.getNum() == 3) {
                        tmRepVehicleMonitoring.setTemperature3(temp.getTemperature());
                    } else if (temp.getNum() == 4) {
                        tmRepVehicleMonitoring.setTemperature4(temp.getTemperature());
                    }
                }
                rsMap.put(vehicleNo, tmRepVehicleMonitoring);
            } catch (Exception ignore) {
            }
        }
        return rsMap;
    }

    private Map<String, TmRepVehicleMonitoring> getVehicleCurrentInfoFormE6(Set<String> gpsNos) {
        Map<String, TmRepVehicleMonitoring> rsMap = Maps.newHashMap();
        Map<String, String> params = Maps.newHashMap();
        params.put("method", "getEquipInfoReal");
        params.put("appkey", com.yunyou.modules.interfaces.gps.e6.constant.Constants.ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("equipcode", String.join(",", gpsNos));
        params.put("isoffsetlonlat", "1");
        params.put("sessionid", "");
        params.put("sign", SignUtil.sign(params));
        List<EquipmentRealInfoByGpsNoResponse> result;
        try {
            Response response = E6Client.get(GET_EQUIP_INFO_REAL, params);
            if (response == null || StringUtils.isBlank(response.getBody())) {
                return rsMap;
            }
            JSONObject res = JSON.parseObject(response.getBody());
            if (res.getInteger("code") != 1) {
                return rsMap;
            }
            result = res.getJSONArray("result").toJavaList(EquipmentRealInfoByGpsNoResponse.class);
        } catch (Exception e) {
            return rsMap;
        }
        for (EquipmentRealInfoByGpsNoResponse data : result) {
            TmRepVehicleMonitoring o = new TmRepVehicleMonitoring();
            o.setGpsTime(data.getGpsTime());
            o.setLng(data.getLon());
            o.setLat(data.getLat());
            o.setAddress(data.getProvice() + data.getCity() + data.getDistrict() + data.getRoadName());
            o.setSpeed(data.getSpeed());
            o.setCourse(data.getDirection());
            o.setAcquireTime(data.getGpsTime());
            o.setTemperature1(data.getT1());
            o.setTemperature2(data.getT2());
            rsMap.put(data.getEquipCode(), o);
        }
        return rsMap;
    }

}
