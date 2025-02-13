package com.yunyou.modules.interfaces.sto.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.sto.entity.*;
import com.sto.link.request.LinkRequest;
import com.sto.link.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StoInterfaceService {
    @Autowired
    private InterfaceLogService interfaceLogService;

    private void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, InterfaceConstant.STO_USER, handleDir)).start();
    }

    private String call(String url, String params, String content, String apiName, String toAppKey) {
        Map<String, String> map = Splitter.on("&").withKeyValueSeparator("=").split(params);
        LinkRequest request = new LinkRequest();
        request.setApiName(apiName);
        request.setFromAppkey(map.get("appKey"));
        request.setFromCode(map.get("fromCode"));
        request.setToAppkey(toAppKey);
        request.setToCode(toAppKey);
        request.setContent(content);
        return LinkUtils.request(request, url, map.get("secretKey"));
    }

    public StoOrderCreateResponse orderCreate(StoOrderCreateRequest request, String url, String params) {
        String apiName = "OMS_EXPRESS_ORDER_CREATE";
        String toAppKey = "sto_oms";
        if (request == null) {
            throw new GlobalException("报文内容为空");
        }
        if (StringUtils.isBlank(url) || StringUtils.isBlank(params)) {
            throw new GlobalException("接口参数未维护！");
        }
        String content = JSON.toJSONString(request);
        saveLog(InterfaceConstant.STO_ORDER_CREATE, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, content, null, request.getOrderNo(), InterfaceConstant.HANDLE_DIR_PRE);
        String s = call(url, params, content, apiName, toAppKey);
        if (s == null) {
            saveLog(InterfaceConstant.STO_ORDER_CREATE, InterfaceConstant.STATUS_N, "反馈报文为空", content, null, request.getOrderNo(), InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
        try {
            StoResponse<StoOrderCreateResponse> response = JSON.parseObject(s, new TypeReference<StoResponse<StoOrderCreateResponse>>() {
            });
            saveLog(InterfaceConstant.STO_ORDER_CREATE, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, content, s, request.getOrderNo(), InterfaceConstant.HANDLE_DIR_RECEIVE);
            return response.isSuccess() ? response.getData() : null;
        } catch (RuntimeException e) {
            saveLog(InterfaceConstant.STO_ORDER_CREATE, InterfaceConstant.STATUS_N, "反馈报文解析失败", content, s, request.getOrderNo(), InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }

    public List<StoTraceQueryResponse> traceQuery(StoTraceQueryRequest request, String url, String params) {
        String apiName = "STO_TRACE_QUERY_COMMON";
        String toAppKey = "sto_trace_query";
        if (request == null) {
            throw new GlobalException("报文内容为空");
        }
        if (StringUtils.isBlank(url) || StringUtils.isBlank(params)) {
            throw new GlobalException("接口参数未维护！");
        }
        String content = JSON.toJSONString(request);
        String searchNo = String.join(",", request.getWaybillNoList());
        saveLog(InterfaceConstant.STO_TRACE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, content, null, searchNo, InterfaceConstant.HANDLE_DIR_PRE);
        String s = call(url, params, content, apiName, toAppKey);
        if (s == null) {
            saveLog(InterfaceConstant.STO_TRACE_QUERY, InterfaceConstant.STATUS_N, "反馈报文为空", content, null, searchNo, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
        try {
            List<StoTraceQueryResponse> responseList = Lists.newArrayList();
            // 此处解析反馈报文，可能是xml，可能是json，待确认
            StoResponse<Map<String, List<CommonResultVO>>> response = JSON.parseObject(s, new TypeReference<StoResponse<Map<String, List<CommonResultVO>>>>() {
            });
            saveLog(InterfaceConstant.STO_TRACE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, content, s, searchNo, InterfaceConstant.HANDLE_DIR_RECEIVE);
            if (response.getData() != null && CollectionUtil.isNotEmpty(request.getWaybillNoList())) {
                for (String wayBillNo : request.getWaybillNoList()) {
                    StoTraceQueryResponse req = new StoTraceQueryResponse();
                    req.setWaybillNo(wayBillNo);
                    req.setTrackList(response.getData().get(wayBillNo));
                    responseList.add(req);
                }
            }
            return responseList;
        } catch (RuntimeException e) {
            saveLog(InterfaceConstant.STO_TRACE_QUERY, InterfaceConstant.STATUS_N, "反馈报文解析失败", content, s, searchNo, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
    }
}
