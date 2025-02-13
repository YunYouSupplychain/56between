package com.yunyou.modules.tms.report.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.e6.E6Client;
import com.yunyou.modules.interfaces.gps.e6.constant.Constants;
import com.yunyou.modules.interfaces.gps.e6.entity.EquipmentHistoryInfoByGpsNoResponse;
import com.yunyou.modules.interfaces.gps.e6.util.SignUtil;
import com.yunyou.modules.interfaces.gps.g7.G7Client;
import com.yunyou.modules.interfaces.gps.g7.constant.ApiPathList;
import com.yunyou.modules.interfaces.gps.GpsRunTrackInfo;
import com.yunyou.modules.tms.common.GpsManufacturer;
import com.yunyou.modules.tms.common.map.MapUtil;
import com.yunyou.modules.tms.common.map.geo.Point;
import com.yunyou.modules.tms.order.entity.TmDeviceAcquireData;
import com.yunyou.modules.tms.order.service.TmDeviceAcquireDataService;
import com.yunyou.modules.tms.report.entity.TmRepDispatchOrder;
import com.yunyou.modules.tms.report.entity.TmRepDispatchOrderDrivingSpeed;
import com.yunyou.modules.tms.report.mapper.TmRepDispatchOrderMapper;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yunyou.modules.interfaces.gps.e6.constant.ApiPathList.GET_EQUIP_INFO_HISTORY;

@Service
public class TmRepDispatchOrderService extends BaseService {
    @Autowired
    private TmRepDispatchOrderMapper mapper;
    @Autowired
    private TmDeviceAcquireDataService tmDeviceAcquireDataService;

    public Page<TmRepDispatchOrder> findPage(Page<TmRepDispatchOrder> page, TmRepDispatchOrder qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(this.findList(qEntity));
        return page;
    }

    private List<TmRepDispatchOrder> findList(TmRepDispatchOrder qEntity) {
        List<TmRepDispatchOrder> list = mapper.findList(qEntity);
        for (TmRepDispatchOrder o : list) {
            if (StringUtils.isBlank(o.getVehicleNo()) || o.getDepartureTime() == null || o.getArrivalTime() == null || StringUtils.isBlank(o.getOrgId())) {
                continue;
            }
            o.setAvgTemp(tmDeviceAcquireDataService.getAvgTempByVehicleAndTime(o.getVehicleNo(), o.getDepartureTime(), o.getArrivalTime(), o.getOrgId()));
        }
        return list;
    }

    public Map<String, Object> findTempList(TmRepDispatchOrder entity) {
        Map<String, Object> rsMap = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(entity.getVehicleNo()) || StringUtils.isBlank(entity.getOrgId()) || entity.getDepartureTime() == null) {
            return rsMap;
        }
        String vehicleNo = entity.getVehicleNo();
        Date departureTime = entity.getDepartureTime();
        Date arrivalTime = entity.getArrivalTime() == null ? DateUtil.addDays(departureTime, 4) : entity.getArrivalTime();
        String orgId = entity.getOrgId();

        List<TmDeviceAcquireData> list = tmDeviceAcquireDataService.getTempByVehicleAndTime(vehicleNo, departureTime, arrivalTime, orgId);
        if (CollectionUtil.isEmpty(list)) {
            return rsMap;
        }
        list.sort(Comparator.comparing(TmDeviceAcquireData::getAcquireTime));
        for (TmDeviceAcquireData o : list) {
            String key = DateUtils.formatDate(o.getAcquireTime(), "yyyy-MM-dd HH:mm");
            double temperature1 = o.getTemperature1() == null ? 0 : o.getTemperature1();
            double temperature2 = o.getTemperature2() == null ? 0 : o.getTemperature2();
            rsMap.put(key, Lists.newArrayList(temperature1, temperature2));
        }
        double maxTemperature1 = list.stream().mapToDouble(TmDeviceAcquireData::getTemperature1).max().orElse(0D);
        double minTemperature1 = list.stream().mapToDouble(TmDeviceAcquireData::getTemperature1).min().orElse(0D);
        double avgTemperature1 = BigDecimalUtil.div(list.stream().mapToDouble(TmDeviceAcquireData::getTemperature1).reduce(0D, BigDecimalUtil::add), list.size(), 2, BigDecimal.ROUND_HALF_UP);
        double maxTemperature2 = list.stream().mapToDouble(TmDeviceAcquireData::getTemperature2).max().orElse(0D);
        double minTemperature2 = list.stream().mapToDouble(TmDeviceAcquireData::getTemperature2).min().orElse(0D);
        double avgTemperature2 = BigDecimalUtil.div(list.stream().mapToDouble(TmDeviceAcquireData::getTemperature2).reduce(0D, BigDecimalUtil::add), list.size(), 2, BigDecimal.ROUND_HALF_UP);
        rsMap.put("temperature1", "最高（" + maxTemperature1 + "℃）最低（" + minTemperature1 + "℃）平均（" + avgTemperature1 + "℃）");
        rsMap.put("temperature2", "最高（" + maxTemperature2 + "℃）最低（" + minTemperature2 + "℃）平均（" + avgTemperature2 + "℃）");
        return rsMap;
    }

    public List<TmRepDispatchOrderDrivingSpeed> findSpeedList(TmRepDispatchOrder entity) {
        List<TmRepDispatchOrderDrivingSpeed> rsList = Lists.newArrayList();
        if (StringUtils.isBlank(entity.getVehicleNo()) || StringUtils.isBlank(entity.getGpsNo()) || StringUtils.isBlank(entity.getGpsManufacturer()) || entity.getDepartureTime() == null) {
            return rsList;
        }
        String vehicleNo = entity.getVehicleNo();
        String gpsNo = entity.getGpsNo();
        String gpsManufacturer = entity.getGpsManufacturer();
        Date departureTime = entity.getDepartureTime();
        Date arrivalTime = entity.getArrivalTime() == null ? DateUtil.addDays(departureTime, 4) : entity.getArrivalTime();

        switch (GpsManufacturer.valueOf(gpsManufacturer)) {
            case G7:
                rsList = getSpeedFromG7(vehicleNo, gpsNo, departureTime, arrivalTime);
                break;
            case E6:
                rsList = getSpeedFromE6(vehicleNo, gpsNo, departureTime, arrivalTime);
                break;
            default:
                break;
        }
        return rsList;
    }

    private List<TmRepDispatchOrderDrivingSpeed> getSpeedFromG7(String vehicleNo, String gpsNo, Date startTime, Date endTime) {
        List<TmRepDispatchOrderDrivingSpeed> rsList = Lists.newArrayList();
        Map<String, String> params = Maps.newHashMap();
        params.put("gpsno", gpsNo);
        params.put("from", DateUtils.formatDate(startTime, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("to", DateUtils.formatDate(endTime, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("timeInterval", "120");// 秒
        List<GpsRunTrackInfo> list;
        try {
            Response response = G7Client.get(ApiPathList.GET_TRACKS_BY_GPS_NO, params);
            if (response == null || StringUtils.isBlank(response.getBody())) {
                return rsList;
            }
            JSONObject res = JSONObject.parseObject(response.getBody());
            if (res.getInteger("code") != 0 && res.getInteger("sub_code") != 0) {
                return rsList;
            }
            list = res.getJSONArray("data").toJavaList(GpsRunTrackInfo.class);
        } catch (Exception e) {
            logger.info("根据GPS获取车辆历史轨迹（G7）异常", e);
            return rsList;
        }
        for (int i = 0; i < list.size(); i += 20) {
            try {
                List<GpsRunTrackInfo> subList = list.subList(i, Math.min(i + 20, list.size()));
                List<String> addressList = MapUtil.getAddress(subList.stream().map(o -> new Point(o.getLon(), o.getLat())).collect(Collectors.toList()), "gcj02ll");
                int index = 0;
                for (GpsRunTrackInfo o : subList) {
                    TmRepDispatchOrderDrivingSpeed speed = new TmRepDispatchOrderDrivingSpeed();
                    speed.setVehicleNo(vehicleNo);
                    speed.setGpsTime(new Date(o.getTime()));
                    speed.setAddress(addressList.get(index++));
                    speed.setSpeed(o.getSpeed());
                    rsList.add(speed);
                }
            } catch (Exception ignored) {
            }
        }
        return rsList.stream().sorted(Comparator.comparing(TmRepDispatchOrderDrivingSpeed::getGpsTime)).collect(Collectors.toList());
    }

    private List<TmRepDispatchOrderDrivingSpeed> getSpeedFromE6(String vehicleNo, String gpsNo, Date startTime, Date endTime) {
        List<TmRepDispatchOrderDrivingSpeed> rsList = Lists.newArrayList();
        Map<String, String> params = Maps.newHashMap();
        params.put("method", "getEquipInfoHistory");
        params.put("appkey", Constants.ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("equipcode", gpsNo);
        params.put("isoffsetlonlat", "1");
        params.put("btime", DateUtils.formatDate(startTime, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("etime", DateUtils.formatDate(endTime, DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("second", "120");// 秒
        params.put("sign", SignUtil.sign(params));
        List<EquipmentHistoryInfoByGpsNoResponse> result;
        try {
            Response response = E6Client.get(GET_EQUIP_INFO_HISTORY, params);
            if (response == null || StringUtils.isBlank(response.getBody())) {
                return rsList;
            }
            JSONObject res = JSONObject.parseObject(response.getBody());
            if (res.getInteger("code") != 1) {
                return rsList;
            }
            result = res.getJSONArray("result").toJavaList(EquipmentHistoryInfoByGpsNoResponse.class);
        } catch (Exception e) {
            logger.info("根据GPS获取指定时间设备数据（JY）异常", e);
            return rsList;
        }
        for (EquipmentHistoryInfoByGpsNoResponse o : result) {
            TmRepDispatchOrderDrivingSpeed speed = new TmRepDispatchOrderDrivingSpeed();
            speed.setVehicleNo(vehicleNo);
            speed.setGpsTime(o.getGpsTime());
            speed.setAddress(o.getProvice() + o.getCity() + o.getDistrict() + o.getRoadName());
            speed.setSpeed(o.getSpeed());
            rsList.add(speed);
        }
        return rsList;
    }
}
