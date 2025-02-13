package com.yunyou.modules.interfaces.zto.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.zto.entity.*;
import com.yunyou.modules.interfaces.zto.utils.ZtoUtils;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ZtoInterfaceService {
    @Autowired
    private InterfaceLogService interfaceLogService;

    @Transactional
    public ZtoGetOrderNoResponse getMailInfo(ZtoOrderNoRequest request, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String companyId = split.get("companyId");
        String key = split.get("key");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(companyId) || StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数未维护！");
        }

        url = url + "/partnerInsertSubmitagent";
        request.setPartner(companyId);
        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.ZTO_GET_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, request.getId(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 调用顺丰接口
        String responseString = ZtoUtils.execute(companyId, key, "submitAgent", requestString, url);
        ZtoGetOrderNoResponse response;
        if (StringUtils.isNotBlank(responseString)) {
            response = JSONObject.parseObject(responseString, ZtoGetOrderNoResponse.class);
        } else {
            response = new ZtoGetOrderNoResponse();
            response.setResult(false);
            response.setMessage("接口异常");
        }
        if (response.isResult()) {
            saveLog(InterfaceConstant.ZTO_GET_ORDER, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, responseString, request.getId(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.ZTO_GET_ORDER, InterfaceConstant.STATUS_N, response.getMessage(), requestString, responseString, request.getId(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }

    @Transactional
    public ZtoGetMarkResponse getMark(ZtoGetMarkRequest request, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String companyId = split.get("companyId");
        String key = split.get("key");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(companyId) || StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数未维护！");
        }

        url = url + "/bagAddrMarkGetmark";
        String requestString = JSONObject.toJSONString(request);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.ZTO_GET_DTB, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, request.getUnionCode(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 调用中通接口
        String responseString = ZtoUtils.execute(companyId, key, "GETMARK", requestString, url);
        ZtoGetMarkResponse response;
        if (StringUtils.isNotBlank(responseString)) {
            response = JSONObject.parseObject(responseString, ZtoGetMarkResponse.class);
        } else {
            response = new ZtoGetMarkResponse();
            response.setStatus(false);
            response.setMessage("接口异常");
        }
        if (response.isStatus()) {
            saveLog(InterfaceConstant.ZTO_GET_DTB, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, responseString, request.getUnionCode(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.ZTO_GET_DTB, InterfaceConstant.STATUS_N, response.getMessage(), requestString, responseString, request.getUnionCode(), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }

    @Transactional
    public ZtoGetTraceResponse getTraceInfo(List<String> mailNos, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String companyId = split.get("companyId");
        String key = split.get("key");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(companyId) || StringUtils.isBlank(key)) {
            throw new WarehouseException("接口参数未维护！");
        }

        url = url + "/traceInterfaceNewTraces";
        String requestString = JSONObject.toJSONString(mailNos);
        // 调用接口前，保存日志
        saveLog(InterfaceConstant.ZTO_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, null, String.join(",", mailNos), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 调用中通接口
        String responseString = ZtoUtils.execute(companyId, key, "NEW_TRACES", requestString, url);
        ZtoGetTraceResponse response;
        if (StringUtils.isNotBlank(responseString)) {
            response = JSONObject.parseObject(responseString, ZtoGetTraceResponse.class);
        } else {
            response = new ZtoGetTraceResponse();
            response.setStatus(false);
            response.setMsg("接口异常");
        }
        if (response.isStatus()) {
            saveLog(InterfaceConstant.ZTO_ROUTE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestString, responseString, String.join(",", mailNos), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.ZTO_ROUTE_QUERY, InterfaceConstant.STATUS_N, response.getMsg(), requestString, responseString, String.join(",", mailNos), InterfaceConstant.ZTO_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }

    public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }

}
