package com.yunyou.modules.interfaces.edi.service;

import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.edi.entity.*;
import com.yunyou.modules.interfaces.edi.mapper.EdiMapper;
import com.yunyou.modules.interfaces.edi.utils.EdiConstant;
import com.yunyou.modules.interfaces.edi.utils.EdiMethod;
import com.yunyou.modules.sys.OfficeType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PushToEdiService extends BaseService {
    @Autowired
    private EdiMapper mapper;
    @Autowired
    private EdiLogService logService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private EdiSendOrderInfoService ediSendOrderInfoService;

    public void pushToEdi(String ediType) {
        switch (ediType) {
            case EdiConstant.EDI_TYPE_ASN:
                pushAsnInfoToEdi();
                break;
            case EdiConstant.EDI_TYPE_SO:
                pushSoInfoToEdi();
                break;
            case EdiConstant.EDI_TYPE_INV:
                pushInvInfoToEdi();
                break;
            case EdiConstant.EDI_TYPE_PLAN_INV:
                pushPlanInvInfoToEdi();
                break;
            case EdiConstant.EDI_TYPE_DO:
                pushDoInfoToEdi();
                break;
        }
    }

    private void pushAsnInfoToEdi() {
        String requestString = "";
        String responseString = "";
        try {
            List<EdiSendOrderInfo> unSendData = ediSendOrderInfoService.findUnSendDataByType(EdiConstant.EDI_TYPE_ASN);
            if (CollectionUtil.isEmpty(unSendData)) {
                return;
            }
            List<EdiOrderInfo> orderInfos = unSendData.stream().map(o -> {
                EdiOrderInfo ediOrderInfo = new EdiOrderInfo();
                BeanUtils.copyProperties(o, ediOrderInfo);
                return ediOrderInfo;
            }).collect(Collectors.toList());

            requestString = JSONObject.toJSONString(new EdiOrderRequest(EdiConstant.EDI_TYPE_ASN, orderInfos));
            responseString = HttpClientUtil.getInstance().sendHttpPost(EdiConstant.EDI_URL + EdiMethod.ORDER.getURL(), requestString, true);

            if (StringUtils.isNotBlank(responseString)) {
                logService.saveLog(EdiLog.success(EdiConstant.EDI_TYPE_ASN, requestString, responseString));
                EdiResponse response = JSONObject.parseObject(responseString, EdiResponse.class);
                if (response != null && response.isSuccess()) {
                    ediSendOrderInfoService.deleteByIds(unSendData.stream().map(EdiSendOrderInfo::getId).collect(Collectors.toList()));
                }
            } else {
                logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_ASN, requestString, "反馈信息为空", new IOException()));
            }
        } catch (Exception e) {
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_ASN, requestString, responseString, e));
        }
    }

    private void pushSoInfoToEdi() {
        String requestString = "";
        String responseString = "";
        try {
            List<EdiSendOrderInfo> unSendData = ediSendOrderInfoService.findUnSendDataByType(EdiConstant.EDI_TYPE_SO);
            if (CollectionUtil.isEmpty(unSendData)) {
                return;
            }
            List<EdiOrderInfo> orderInfos = unSendData.stream().map(o -> {
                EdiOrderInfo ediOrderInfo = new EdiOrderInfo();
                BeanUtils.copyProperties(o, ediOrderInfo);
                return ediOrderInfo;
            }).collect(Collectors.toList());

            requestString = JSONObject.toJSONString(new EdiOrderRequest(EdiConstant.EDI_TYPE_SO, orderInfos));
            responseString = HttpClientUtil.getInstance().sendHttpPost(EdiConstant.EDI_URL + EdiMethod.ORDER.getURL(), requestString, true);

            if (StringUtils.isNotBlank(responseString)) {
                logService.saveLog(EdiLog.success(EdiConstant.EDI_TYPE_SO, requestString, responseString));
                EdiResponse response = JSONObject.parseObject(responseString, EdiResponse.class);
                if (response != null && response.isSuccess()) {
                    ediSendOrderInfoService.deleteByIds(unSendData.stream().map(EdiSendOrderInfo::getId).collect(Collectors.toList()));
                }
            } else {
                logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_SO, requestString, "反馈信息为空", new IOException()));
            }
        } catch (Exception e) {
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_SO, requestString, responseString, e));
        }
    }

    private void pushInvInfoToEdi() {
        Office offCon = new Office();
        offCon.setType("5");
        List<Office> warehouseList = officeService.findCompanyData(offCon);
        if (CollectionUtil.isEmpty(warehouseList)) {
            return;
        }
        String requestString = "";
        String responseString = "";
        for (Office warehouse : warehouseList) {
            try {
                List<EdiInvInfo> ediInvInfos = mapper.findInvEdi(warehouse.getId());
                if (CollectionUtil.isEmpty(ediInvInfos)) {
                    continue;
                }

                requestString = JSONObject.toJSONString(new EdiInvRequest(ediInvInfos));
                responseString = HttpClientUtil.getInstance().sendHttpPost(EdiConstant.EDI_URL + EdiMethod.INV.getURL(), requestString, true);

                if (StringUtils.isNotBlank(responseString)) {
                    logService.saveLog(EdiLog.success(EdiConstant.EDI_TYPE_INV, requestString, responseString));
                } else {
                    logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_INV, requestString, "反馈信息为空", new IOException()));
                }
            } catch (Exception e) {
                logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_INV, requestString, responseString, e));
            }
        }
    }

    private void pushPlanInvInfoToEdi() {
        Office offCon = new Office();
        offCon.setType("5");
        List<Office> warehouseList = officeService.findCompanyData(offCon);
        if (CollectionUtil.isEmpty(warehouseList)) {
            return;
        }
        String requestString = "";
        String responseString = "";
        for (Office warehouse : warehouseList) {
            try {
                List<EdiPlanInvInfo> ediInvInfos = mapper.findPlanInvEdi(warehouse.getId());
                if (CollectionUtil.isEmpty(ediInvInfos)) {
                    continue;
                }

                requestString = JSONObject.toJSONString(new EdiRequest(EdiConstant.EDI_TYPE_PLAN_INV, ediInvInfos));
                responseString = HttpClientUtil.getInstance().sendHttpPost(EdiConstant.EDI_URL + EdiMethod.SAVE.getURL(), requestString, true);

                if (StringUtils.isNotBlank(responseString)) {
                    logService.saveLog(EdiLog.success(EdiConstant.EDI_TYPE_PLAN_INV, requestString, responseString));
                } else {
                    logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_PLAN_INV, requestString, "反馈信息为空", new IOException()));
                }
            } catch (Exception e) {
                logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_PLAN_INV, requestString, responseString, e));
            }
        }
    }

    private void pushDoInfoToEdi() {
        String requestString = "";
        String responseString = "";

        Office offCon = new Office();
        offCon.setType(OfficeType.ORG_CENTER.getValue());
        List<Office> offices = officeService.findCompanyData(offCon);
        if (CollectionUtil.isEmpty(offices)) {
            return;
        }
        for (Office office : offices) {
            try {
                List<EdiDispatchOrderInfo> orderInfos = ediSendOrderInfoService.findUnSendDataByDo(office.getId());
                if (CollectionUtil.isEmpty(orderInfos)) {
                    continue;
                }

                Collection<List<EdiDispatchOrderInfo>> values = orderInfos.stream().collect(Collectors.groupingBy(o -> o.getDispatchNo() + o.getOrgId())).values();
                for (List<EdiDispatchOrderInfo> list : values) {

                    requestString = JSONObject.toJSONString(new EdiRequest(EdiConstant.EDI_TYPE_DO, list));
                    responseString = HttpClientUtil.getInstance().sendHttpPost(EdiConstant.EDI_URL + EdiMethod.SAVE.getURL(), requestString, true);

                    if (StringUtils.isNotBlank(responseString)) {
                        logService.saveLog(EdiLog.success(EdiConstant.EDI_TYPE_DO, requestString, responseString));
                    } else {
                        logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_DO, requestString, "反馈信息为空", new IOException()));
                    }
                    list.clear();
                }
            } catch (Exception e) {
                logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_DO, requestString, responseString, e));
            }
        }
    }

}
