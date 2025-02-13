package com.yunyou.modules.interfaces.kdBest.service;

import com.google.common.base.Splitter;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.kdBest.entity.KdWaybillApplyNotifyRequestEntity;
import com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.request.KdInspectionSubmitReq;
import com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.response.KdInspectionSubmitRsp;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.request.KdTraceQueryReq;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.request.MailNos;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.KdTraceQueryRsp;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request.Auth;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request.EDIPrintDetailList;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request.KdWaybillApplyNotifyReq;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response.KdWaybillApplyNotifyRsp;
import com.yunyou.modules.interfaces.kdBest.utils.Client;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 百世快递接口service
 *
 * @author zyf
 * @version 2019-10-09
 */
@Service
@Transactional(readOnly = true)
public class KdBestInterfaceService extends BaseService {
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 运单信息查询接口
     *
     * @param mailNoList
     */
    @Transactional
    public KdTraceQueryRsp kdTraceQuery(List<String> mailNoList, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String[] urlArray = url.split(";", -1);
        String partnerKey = split.get("partnerKey"), partnerID = split.get("partnerID"), userName = split.get("username"), passWord = split.get("password");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(partnerKey) || StringUtils.isBlank(partnerID) || StringUtils.isBlank(userName) || StringUtils.isBlank(passWord) || urlArray.length != 2) {
            throw new WarehouseException("接口参数未维护！");
        }
        String format = InterfaceConstant.FORMAT_TYPE_XML;//如果是JSON的数据格式，填JSON
        Client client = new Client(urlArray[1], partnerKey, partnerID, format);

        // 创建请求对象
        KdTraceQueryReq kdTraceQueryReq = new KdTraceQueryReq();
        MailNos mailNos = new MailNos();
        mailNos.setMailNo(mailNoList);
        kdTraceQueryReq.setMailNos(mailNos);

        String requestData = Parser.coverObject2Xml(kdTraceQueryReq);
        // 请求百世接口前，保存日志
        saveLog(InterfaceConstant.KD_TRACE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, null, String.join(",", mailNoList), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_PRE);
        KdTraceQueryRsp kdTraceQueryRsp = client.executed(kdTraceQueryReq);
        if (null != kdTraceQueryRsp) {
            String responseData = Parser.coverObject2Xml(kdTraceQueryRsp);
            if (kdTraceQueryRsp.getResult()) {
                saveLog(InterfaceConstant.KD_TRACE_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, responseData, String.join(",", mailNoList), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            } else {
                saveLog(InterfaceConstant.KD_TRACE_QUERY, InterfaceConstant.STATUS_N, kdTraceQueryRsp.getErrorDescription(), requestData, responseData, String.join(",", mailNoList), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            }
        } else {
            saveLog(InterfaceConstant.KD_TRACE_QUERY, InterfaceConstant.STATUS_N, "接口异常", requestData, null, String.join(",", mailNoList), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }

        return kdTraceQueryRsp;
    }

    public void saveLog(String type, String isSuccess, String message, String requestData, String responseData, String searchNo, User user, String handleDir) {
        new Thread(() -> interfaceLogService.saveLogNew(type, isSuccess, message, requestData, responseData, searchNo, user, handleDir)).start();
    }

    /**
     * 电子面单获取接口
     */
    @Transactional
    public KdWaybillApplyNotifyRsp kdWaybillApplyNotify(KdWaybillApplyNotifyRequestEntity requestEntity, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String[] urlArray = url.split(";", -1);
        String partnerKey = split.get("partnerKey"), partnerID = split.get("partnerID"), userName = split.get("username"), passWord = split.get("password");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(partnerKey) || StringUtils.isBlank(partnerID) || StringUtils.isBlank(userName) || StringUtils.isBlank(passWord) || urlArray.length != 2) {
            throw new WarehouseException("接口参数未维护！");
        }

        Client client = new Client(urlArray[0], partnerKey, partnerID, InterfaceConstant.FORMAT_TYPE_XML);

        KdWaybillApplyNotifyReq billPrintRequestReq = new KdWaybillApplyNotifyReq();
        billPrintRequestReq.setDeliveryConfirm(false);
        billPrintRequestReq.setMsgId(requestEntity.getMsgId());
        billPrintRequestReq.setAuth(new Auth(userName, passWord));
        EDIPrintDetailList ediPrintDetailList = new EDIPrintDetailList();
        BeanUtils.copyProperties(requestEntity, ediPrintDetailList);
        billPrintRequestReq.setEDIPrintDetailList(new ArrayList<EDIPrintDetailList>(){{add(ediPrintDetailList);}});

        String requestData = Parser.coverObject2Xml(billPrintRequestReq);
        // 请求百世接口前，保存日志
        saveLog(InterfaceConstant.KD_WAYBILL_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, null, requestEntity.getMsgId(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_PRE);
        KdWaybillApplyNotifyRsp billPrintRequestRsp = client.executed(billPrintRequestReq);
        if (billPrintRequestRsp == null) {
            saveLog(InterfaceConstant.KD_WAYBILL_QUERY, InterfaceConstant.STATUS_N, "接口异常,反馈信息为空", requestData, null, requestEntity.getMsgId(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
        String responseData = Parser.coverObject2Xml(billPrintRequestRsp);
        if (billPrintRequestRsp.getResult()) {
            saveLog(InterfaceConstant.KD_WAYBILL_QUERY, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, responseData, requestEntity.getMsgId(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            saveLog(InterfaceConstant.KD_WAYBILL_QUERY, InterfaceConstant.STATUS_N, billPrintRequestRsp.getErrorDescription(), requestData, responseData, requestEntity.getMsgId(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return billPrintRequestRsp;
    }

    /**
     * 抽检数据提交接口
     *
     * @param request
     * @return
     */
    @Transactional
    public KdInspectionSubmitRsp kdInspectionSubmit(KdInspectionSubmitReq request, String url, String params) {
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        String[] urlArray = url.split(";", -1);
        String partnerKey = split.get("partnerKey"), partnerID = split.get("partnerID");

        if (StringUtils.isBlank(url) || StringUtils.isBlank(partnerKey) || StringUtils.isBlank(partnerID) || urlArray.length != 2) {
            throw new WarehouseException("接口参数未维护！");
        }
        Client client = new Client(urlArray[1], partnerKey, partnerID, InterfaceConstant.FORMAT_TYPE_XML);

        String requestData = Parser.coverObject2Xml(request);
        // 请求百世接口前，保存日志
        interfaceLogService.saveLog(InterfaceConstant.KD_INSPECTION_SUBMIT, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, null, request.getMailNo(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_PRE);
        // 请求接口
        KdInspectionSubmitRsp response = client.executed(request);
        if (response == null) {
            interfaceLogService.saveLog(InterfaceConstant.KD_INSPECTION_SUBMIT, InterfaceConstant.STATUS_N, "接口异常,反馈信息为空", requestData, null, request.getMailNo(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
            return null;
        }
        String responseData = Parser.coverObject2Xml(response);
        if (response.getResult()) {
            interfaceLogService.saveLog(InterfaceConstant.KD_INSPECTION_SUBMIT, InterfaceConstant.STATUS_Y, InterfaceConstant.SUCCESS_LOG, requestData, responseData, request.getMailNo(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } else {
            interfaceLogService.saveLog(InterfaceConstant.KD_INSPECTION_SUBMIT, InterfaceConstant.STATUS_N, response.getRemark(), requestData, responseData, request.getMailNo(), InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        }
        return response;
    }
}