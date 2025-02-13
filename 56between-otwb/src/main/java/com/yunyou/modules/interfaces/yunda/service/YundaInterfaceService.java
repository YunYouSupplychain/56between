package com.yunyou.modules.interfaces.yunda.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.request.YundaCreateOrderRequest;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponse;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponseData;
import com.yunyou.modules.interfaces.yunda.entity.routeQuery.response.YundaRouteQueryResponse;
import com.yunyou.modules.interfaces.yunda.utils.YundaUtils;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class YundaInterfaceService {

    private HttpClientUtil clientUtil = new HttpClientUtil();
    @Autowired
    private InterfaceLogService interfaceLogService;

    @Transactional
    public YundaCreateOrderResponse createOrder(List<YundaCreateOrderRequest> requestList, String url, String params) {
        String requestType = "data";
        String[] urlArray = url.split(";", -1);
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String partnerid = split.get("partnerid1");
        String password = split.get("password1");
        if (StringUtils.isBlank(url) || StringUtils.isBlank(partnerid) || StringUtils.isBlank(password)) {
            throw new WarehouseException("接口参数未维护！");
        }
        // 转xml
        String xmlString = YundaUtils.requestCover2Xml(requestList);
        String requestIds = String.join(",", requestList.stream().map(t -> t.getOrder_serial_no()).collect(Collectors.toList()));
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.YUNDA_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, xmlString, null, requestIds, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 发送http请求
        String responseString = YundaUtils.execute(urlArray[0], partnerid, password, requestType, xmlString);
        YundaCreateOrderResponse response;
        if (StringUtils.isBlank(responseString)) {
            saveLog(InterfaceConstant.YUNDA_CREATE_ORDER, InterfaceConstant.STATUS_N, "反馈信息为空", xmlString, responseString, requestIds, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        } else {
            response = XmlObjectUtils.coverXml2Object(responseString, YundaCreateOrderResponse.class);
            for (YundaCreateOrderResponseData responseData : response.getResponse()) {
                if ("1".equals(responseData.getStatus())) {
                    saveLog(InterfaceConstant.YUNDA_CREATE_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, xmlString, responseString, responseData.getOrder_serial_no(), InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                } else {
                    saveLog(InterfaceConstant.YUNDA_CREATE_ORDER, InterfaceConstant.STATUS_N, responseData.getMsg(), xmlString, responseString, responseData.getOrder_serial_no(), InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                }
            }
            return response;
        }
    }

    @Transactional
    public YundaRouteQueryResponse routeQuery(String mailNo, String url, String params) {
        String[] urlArray = url.split(";", -1);
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String partnerid = split.get("partnerid2");
        if (StringUtils.isBlank(url) || StringUtils.isBlank(partnerid) || urlArray.length < 2) {
            throw new WarehouseException("接口参数未维护！");
        }
        // 拼接http请求
        String httpUrl = urlArray[1] + "?partnerid=" + partnerid + "&mailno=" + mailNo + "&type=json";
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.YUNDA_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, httpUrl, null, mailNo, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 发送http请求
        String responseString = clientUtil.sendHttpGet(httpUrl);
        YundaRouteQueryResponse response;
        if (StringUtils.isBlank(responseString)) {
            saveLog(InterfaceConstant.YUNDA_ROUTE_QUERY, InterfaceConstant.STATUS_N, "反馈信息为空", httpUrl, responseString, mailNo, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        } else {
            response = JSONObject.parseObject(responseString, YundaRouteQueryResponse.class);
            if (response.getResult()) {
                saveLog(InterfaceConstant.YUNDA_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, httpUrl, responseString, mailNo, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            } else {
                response.setRemark(response.getRemark() + YundaUtils.ERROR_DESCRIPTION.get(response.getRemark()));
                saveLog(InterfaceConstant.YUNDA_ROUTE_QUERY, InterfaceConstant.STATUS_N, response.getRemark(), httpUrl, responseString, mailNo, InterfaceConstant.YUNDA_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            }
            return response;
        }
    }

    public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }
}
