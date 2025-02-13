package com.yunyou.modules.interfaces.yto.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.yto.entity.createOrder.request.YTOCreateOrderRequest;
import com.yunyou.modules.interfaces.yto.entity.createOrder.response.YTOCreateOrderResponseData;
import com.yunyou.modules.interfaces.yto.entity.routeQuery.request.YTORouteQueryRequest;
import com.yunyou.modules.interfaces.yto.entity.routeQuery.response.YTORouteQueryResponse;
import com.yunyou.modules.interfaces.yto.utils.YTOUtils;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class YTOInterfaceService {

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Transactional
    public YTOCreateOrderResponseData createOrder(YTOCreateOrderRequest request, String url, String params) {
        String[] urlArray = url.split(";", -1);
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String clientID = split.get("clientID");    // 客户编码
        String partnerID = split.get("partnerID");  // 验证码
        if (StringUtils.isBlank(url) || StringUtils.isBlank(clientID) || StringUtils.isBlank(partnerID)) {
            throw new WarehouseException("接口参数未维护！");
        }
        // 转xml
        request.setClientID(clientID);
        request.setCustomerId(clientID);
        request.setTradeNo(clientID);
        String xmlString = YTOUtils.requestCover2Xml(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.YTO_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, xmlString, null, request.getTxLogisticID(), InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 发送http请求
        String responseString = YTOUtils.execute(urlArray[0], clientID, partnerID, xmlString);
        YTOCreateOrderResponseData response;
        if (StringUtils.isBlank(responseString)) {
            saveLog(InterfaceConstant.YTO_CREATE_ORDER, InterfaceConstant.STATUS_N, "反馈信息为空", xmlString, responseString, request.getTxLogisticID(), InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        } else {
            response = XmlObjectUtils.coverXml2Object(responseString, YTOCreateOrderResponseData.class);
            if ("true".equals(response.getSuccess()) && "200".equals(response.getCode())) {
                saveLog(InterfaceConstant.YTO_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, xmlString, responseString, response.getTxLogisticID(), InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            } else {
                saveLog(InterfaceConstant.YTO_CREATE_ORDER, InterfaceConstant.STATUS_N, response.getReason(), xmlString, responseString, response.getTxLogisticID(), InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            }
            return response;
        }
    }

    @Transactional
    public  List<YTORouteQueryResponse> routeQuery(String mailNo, String url, String params) {
        String method = "yto.Marketing.WaybillTrace";
        String v = "1.01";
        String[] urlArray = url.split(";", -1);
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String appKey = split.get("appKey");
        String userId = split.get("userId");
        String secretKey = split.get("secretKey");
        if (StringUtils.isBlank(url) || StringUtils.isBlank(appKey) || StringUtils.isBlank(userId) || StringUtils.isBlank(secretKey) || urlArray.length < 2) {
            throw new WarehouseException("接口参数未维护！");
        }
        YTORouteQueryRequest request = new YTORouteQueryRequest();
        request.setNumber(mailNo);
        String requestString = JSONObject.toJSONString(request);
        saveLog(InterfaceConstant.YTO_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, mailNo, InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_PRE);
        String s = YTOUtils.traceQuery(requestString, urlArray[1], method, v, appKey, userId, secretKey);
        if (s == null) {
            saveLog(InterfaceConstant.YTO_ROUTE_QUERY, InterfaceConstant.STATUS_N, "反馈报文为空", requestString, null, mailNo, InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
        try {
            List<YTORouteQueryResponse> response = JSON.parseArray(s, YTORouteQueryResponse.class);
            saveLog(InterfaceConstant.YTO_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, s, mailNo, InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return response;
        } catch (RuntimeException e) {
            saveLog(InterfaceConstant.YTO_ROUTE_QUERY, InterfaceConstant.STATUS_N, "反馈报文解析失败", requestString, s, mailNo, InterfaceConstant.YTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }

    public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }


}
