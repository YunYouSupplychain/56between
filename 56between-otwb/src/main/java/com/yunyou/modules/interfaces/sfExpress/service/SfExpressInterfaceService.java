package com.yunyou.modules.interfaces.sfExpress.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.request.SfCreateOrderRequest;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.SfCreateOrderResponse;
import com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.request.SfRouteQueryRequest;
import com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.response.SfRouteQueryResponse;
import com.yunyou.modules.interfaces.sfExpress.utils.Sign;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SfExpressInterfaceService extends BaseService {
    private HttpClientUtil clientUtil = new HttpClientUtil();

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Transactional
    public SfCreateOrderResponse sfCreateOrder(SfCreateOrderRequest request, String url, String params) {
        String serviceType = "/api/open/createOrder";
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String appId = split.get("appId");
        String appSecret = split.get("appSecret");
        if (StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
            throw new WarehouseException("接口参数未维护！");
        }

        request.setAppId(appId);
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setSign(Sign.sign(appId, request.getRequestId(), request.getTimestamp(), appSecret));

        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.SF_EXPRESS_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, request.getRequestId(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 调用顺丰接口
        String responseString = clientUtil.sendHttpPost(url + serviceType, requestString, true);
        SfCreateOrderResponse response;
        if (StringUtils.isNotBlank(responseString)) {
            response = JSONObject.parseObject(responseString, SfCreateOrderResponse.class);
        } else {
            response = new SfCreateOrderResponse();
            response.setHttpStatus(404);
            response.setMessage("反馈信息为空");
        }
        if (response.getHttpStatus() == 200) {
            saveLog(InterfaceConstant.SF_EXPRESS_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, responseString, request.getRequestId(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.SF_EXPRESS_CREATE_ORDER, InterfaceConstant.STATUS_N, response.getErrorCode() + ":" + response.getMessage(), requestString, responseString, request.getRequestId(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }

    public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }

    @Transactional
    public SfRouteQueryResponse sfRouteQuery(SfRouteQueryRequest request, String url, String params) {
        String serviceType = "/api/order/routeQuery";
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String appId = split.get("appId");
        String appSecret = split.get("appSecret");
        if (StringUtils.isBlank(url) || StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
            throw new WarehouseException("接口参数未维护！");
        }

        request.setAppId(appId);
        request.setTimestamp(String.valueOf(System.currentTimeMillis()));
        request.setSign(Sign.sign(appId, request.getRequestId(), request.getTimestamp(), appSecret));

        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.SF_EXPRESS_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, request.getMailNumber(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 调用顺丰接口
        String responseString = clientUtil.sendHttpPost(url + serviceType, requestString, true);

        SfRouteQueryResponse response;
        if (StringUtils.isNotBlank(responseString)) {
            response = JSONObject.parseObject(responseString, SfRouteQueryResponse.class);
        } else {
            response = new SfRouteQueryResponse();
            response.setHttpStatus(404);
            response.setMessage("反馈信息为空");
        }
        if (response.getHttpStatus() == 200) {
            saveLog(InterfaceConstant.SF_EXPRESS_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, responseString, request.getMailNumber(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.SF_EXPRESS_ROUTE_QUERY, InterfaceConstant.STATUS_N, response.getErrorCode() + ":" + response.getMessage(), requestString, responseString, request.getMailNumber(), InterfaceConstant.SF_EXPRESS_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }
}
