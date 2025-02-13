package com.yunyou.modules.monitor.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.modules.interfaces.ALiYunUtil;
import com.yunyou.modules.interfaces.gps.Response;
import com.yunyou.modules.interfaces.gps.e6.E6Client;
import com.yunyou.modules.interfaces.gps.e6.entity.EquipmentHistoryInfoByGpsNoResponse;
import com.yunyou.modules.interfaces.gps.e6.util.SignUtil;
import com.yunyou.modules.interfaces.gps.g7.G7Client;
import com.yunyou.modules.interfaces.gps.g7.entity.Temperature;
import com.yunyou.modules.interfaces.gps.g7.entity.TemperatureByGpsNoResponse;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.GpsManufacturer;
import com.yunyou.modules.tms.order.entity.TmDeviceAcquireData;
import com.yunyou.modules.tms.order.entity.extend.TmNoReturnVehicleInfo;
import com.yunyou.modules.tms.order.manager.TmDispatchOrderManager;
import com.yunyou.modules.tms.order.service.TmDeviceAcquireDataService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yunyou.modules.interfaces.gps.e6.constant.ApiPathList.GET_EQUIP_INFO_HISTORY;
import static com.yunyou.modules.interfaces.gps.g7.constant.ApiPathList.GET_TEMPERATURE_BY_GPS_NO;

/**
 * 获取GPS温度数据定时任务
 *
 * @author liujianhua
 */
@DisallowConcurrentExecution
public class TmsGpsTempTask extends Task {
    private final Logger logger = LoggerFactory.getLogger(TmsGpsTempTask.class);
    private TmDispatchOrderManager tmDispatchOrderManager = SpringContextHolder.getBean(TmDispatchOrderManager.class);
    private TmDeviceAcquireDataService tmDeviceAcquireDataService = SpringContextHolder.getBean(TmDeviceAcquireDataService.class);

    @Override
    public void run() {
        List<TmNoReturnVehicleInfo> list = tmDispatchOrderManager.findNoReturnVehicle();
        for (TmNoReturnVehicleInfo o : list) {
            if (StringUtils.isBlank(o.getGpsManufacturer())) {
                continue;
            }
            if (o.getStartTime() != null) {
                o.setStartTime(DateUtil.addSeconds(o.getStartTime(), 1));
            }
            try {
                List<TmDeviceAcquireData> tmDeviceAcquireDataList;
                switch (GpsManufacturer.valueOf(o.getGpsManufacturer())) {
                    case G7:
                        tmDeviceAcquireDataList = getTemperatureFormG7(o);
                        break;
                    case E6:
                        tmDeviceAcquireDataList = getTemperatureFormE6(o);
                        break;
                    default:
                        continue;
                }
                if (CollectionUtil.isNotEmpty(tmDeviceAcquireDataList)) {
                    tmDeviceAcquireDataService.removeByVehicleNoAndTime(o.getVehicleNo(), o.getGpsNo(), o.getStartTime(), o.getEndTime(), o.getOrgId());
                    tmDeviceAcquireDataService.batchInsert(tmDeviceAcquireDataList);
                }
                // TMS参数：温度预警呼叫通知
                final String IS_TEMPERATURE_WARNING_SMART_CALL = SysControlParamsUtils.getValue(SysParamConstants.IS_TEMPERATURE_WARNING_SMART_CALL);
                if (Global.Y.equals(IS_TEMPERATURE_WARNING_SMART_CALL) && CollectionUtil.isNotEmpty(tmDeviceAcquireDataList)) {
                    List<Double> temperatureList = Lists.newArrayList();
                    tmDeviceAcquireDataList.forEach(e -> {
                        if (e.getTemperature1() != null) {
                            temperatureList.add(e.getTemperature1());
                        }
                        if (e.getTemperature2() != null) {
                            temperatureList.add(e.getTemperature2());
                        }
                        if (e.getTemperature3() != null) {
                            temperatureList.add(e.getTemperature3());
                        }
                        if (e.getTemperature4() != null) {
                            temperatureList.add(e.getTemperature4());
                        }
                        if (e.getTemperature5() != null) {
                            temperatureList.add(e.getTemperature5());
                        }
                    });
                    intelligentVoiceCall(o.getVehicleNo(), temperatureList, o.getWarningTempFm(), o.getWarningTempTo(), o.getDriverName(), o.getDriverTel());
                }
            } catch (Exception e) {
                logger.error(JSON.toJSONString(o), e);
            }
        }
    }

    /**
     * 获取G7温度数据
     *
     * @return 温度
     */
    private List<TmDeviceAcquireData> getTemperatureFormG7(TmNoReturnVehicleInfo o) throws Exception {
        Response response = getG7Response(o, 1);
        if (response == null || StringUtils.isBlank(response.getBody())) {
            return null;
        }
        JSONObject res = JSONObject.parseObject(response.getBody());
        if (res.getInteger("code") != 0) {
            return null;
        }
        JSONObject result = res.getJSONObject("data");
        Integer count = result.getInteger("totalCount");
        if (count == null || count <= 0) {
            return null;
        }
        List<TmDeviceAcquireData> tmDeviceAcquireDataList = parseG7Response(o, response);
        if (CollectionUtil.isEmpty(tmDeviceAcquireDataList)) {
            return null;
        }

        Integer pageSize = result.getInteger("pageSize");
        int num = count % pageSize == 0 ? (count / pageSize) : (count / pageSize + 1);
        for (int i = 2; i <= num; i++) {
            List<TmDeviceAcquireData> list = parseG7Response(o, getG7Response(o, i));
            if (CollectionUtil.isEmpty(list)) {
                continue;
            }
            tmDeviceAcquireDataList.addAll(list);
        }
        return tmDeviceAcquireDataList;
    }

    private Response getG7Response(TmNoReturnVehicleInfo o, int pageNo) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("gpsno", o.getGpsNo());
        params.put("from", DateUtils.formatDate(o.getStartTime(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("to", DateUtils.formatDate(o.getEndTime(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("space", "1");// 是否按一分钟间隔查询数据，1：是，else：否
        params.put("page_no", String.valueOf(pageNo));
        params.put("orderby", "time asc");
        return G7Client.get(GET_TEMPERATURE_BY_GPS_NO, params);
    }

    private List<TmDeviceAcquireData> parseG7Response(TmNoReturnVehicleInfo o, Response response) {
        if (response == null || StringUtils.isBlank(response.getBody())) {
            return null;
        }
        JSONObject res = JSONObject.parseObject(response.getBody());
        if (res.getInteger("code") != 0) {
            return null;
        }
        List<TemperatureByGpsNoResponse> list = res.getJSONObject("data").getJSONArray("result").toJavaList(TemperatureByGpsNoResponse.class);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<TmDeviceAcquireData> rsList = Lists.newArrayList();
        for (TemperatureByGpsNoResponse data : list) {
            Temperature probeInfo = data.getProbeInfo();

            TmDeviceAcquireData tmDeviceAcquireData = new TmDeviceAcquireData();
            tmDeviceAcquireData.setId(IdGen.uuid());
            tmDeviceAcquireData.setVehicleNo(o.getVehicleNo());
            tmDeviceAcquireData.setGpsNo(o.getGpsNo());
            tmDeviceAcquireData.setAcquireTime(data.getTime());
            tmDeviceAcquireData.setCoordinate(data.getLng() + "," + data.getLat());
            tmDeviceAcquireData.setTemperature1(probeInfo.getT1Temperature());
            tmDeviceAcquireData.setTemperature2(probeInfo.getT2Temperature());
            tmDeviceAcquireData.setBaseOrgId(o.getBaseOrgId());
            tmDeviceAcquireData.setOrgId(o.getOrgId());
            rsList.add(tmDeviceAcquireData);
        }
        return rsList;
    }

    /**
     * 获取易流温度数据
     *
     * @return 温度
     */
    private List<TmDeviceAcquireData> getTemperatureFormE6(TmNoReturnVehicleInfo o) throws Exception {
        return parseE6Response(o, getE6Response(o));
    }

    private Response getE6Response(TmNoReturnVehicleInfo o) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("method", "getEquipInfoHistory");
        params.put("appkey", com.yunyou.modules.interfaces.gps.e6.constant.Constants.ACCESS_ID);
        params.put("timestamp", DateUtils.formatDate(new Date(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("format", "json");
        params.put("equipcode", o.getGpsNo());
        params.put("btime", DateUtils.formatDate(o.getStartTime(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("etime", DateUtils.formatDate(o.getEndTime(), DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
        params.put("isoffsetlonlat", "1");
        params.put("second", "0");// 秒
        params.put("sign", SignUtil.sign(params));
        return E6Client.get(GET_EQUIP_INFO_HISTORY, params);
    }

    private List<TmDeviceAcquireData> parseE6Response(TmNoReturnVehicleInfo o, Response response) {
        if (response == null || StringUtils.isBlank(response.getBody())) {
            return null;
        }
        JSONObject res = JSONObject.parseObject(response.getBody());
        if (res.getInteger("code") != 0) {
            return null;
        }
        List<EquipmentHistoryInfoByGpsNoResponse> list = res.getJSONArray("result").toJavaList(EquipmentHistoryInfoByGpsNoResponse.class);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<TmDeviceAcquireData> tmDeviceAcquireDataList = Lists.newArrayList();
        for (EquipmentHistoryInfoByGpsNoResponse data : list) {
            TmDeviceAcquireData tmDeviceAcquireData = new TmDeviceAcquireData();
            tmDeviceAcquireData.setVehicleNo(o.getVehicleNo());
            tmDeviceAcquireData.setGpsNo(o.getGpsNo());
            tmDeviceAcquireData.setAcquireTime(data.getGpsTime());
            tmDeviceAcquireData.setCoordinate(data.getLon() + data.getLat() + "");
            tmDeviceAcquireData.setTemperature1(data.getT1());
            tmDeviceAcquireData.setTemperature2(data.getT2());
            tmDeviceAcquireData.setTemperature3(data.getT3());
            tmDeviceAcquireData.setTemperature4(data.getT4());
            tmDeviceAcquireData.setBaseOrgId(o.getBaseOrgId());
            tmDeviceAcquireData.setOrgId(o.getOrgId());
            tmDeviceAcquireDataList.add(tmDeviceAcquireData);
        }
        return tmDeviceAcquireDataList;
    }


    /**
     * 智能语音呼叫司机
     *
     * @param vehicleNo       车牌号
     * @param temperatureList 温度
     * @param warningTempFm   预警最低温
     * @param warningTempTo   预警最高温
     * @param driverName      司机
     * @param driverTel       电话
     */
    private void intelligentVoiceCall(String vehicleNo, List<Double> temperatureList, Double warningTempFm, Double warningTempTo, String driverName, String driverTel) {
        if (CollectionUtil.isEmpty(temperatureList) || StringUtils.isBlank(driverTel)) {
            return;
        }
        for (Double temperature : temperatureList) {
            if (temperature < warningTempFm || temperature > warningTempTo) {
                Map<String, Object> params = new HashMap<>();
                params.put("vehicleNo", vehicleNo);
                params.put("currentTemp", temperature);
                String ttsParam = JSON.toJSONString(params);
                final String accessKeyId = SysControlParamsUtils.getValue(SysParamConstants.ALIYUN_ACCESS_KEY_ID);
                final String accessKeySecret = SysControlParamsUtils.getValue(SysParamConstants.ALIYUN_ACCESS_KEY_SECRET);
                final String ttsCode = SysControlParamsUtils.getValue(SysParamConstants.ALIYUN_ANNOUNCEMENT_TEMPLATE);

                SingleCallByTtsRequest request = new SingleCallByTtsRequest();
                request.setCalledNumber(driverTel);
                request.setTtsCode(ttsCode);
                request.setTtsParam(ttsParam);
                request.setPlayTimes(1);
                request.setSpeed(-200);
                try {
                    SingleCallByTtsResponse response = ALiYunUtil.singleCallByTts(accessKeyId, accessKeySecret, request);
                    logger.info("阿里云语音通知:" + JSON.toJSONString(response));
                } catch (Throwable e) {
                    logger.error("阿里云语音通知:" + JSON.toJSONString(request), e);
                }
                return;
            }
        }
    }

}
