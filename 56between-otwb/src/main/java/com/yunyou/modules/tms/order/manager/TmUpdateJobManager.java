package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.interactive.service.TmInteractiveService;
import com.yunyou.modules.interfaces.kd100.Kd100InterfaceService;
import com.yunyou.modules.interfaces.kd100.entity.QueryTrackData;
import com.yunyou.modules.interfaces.kd100.entity.QueryTrackResp;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.KdTraceQueryRsp;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.Trace;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.TraceLogs;
import com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response.Traces;
import com.yunyou.modules.interfaces.kdBest.service.KdBestInterfaceService;
import com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.request.SfRouteQueryRequest;
import com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.response.SfRouteQueryResponse;
import com.yunyou.modules.interfaces.sfExpress.entity.sfRouteQuery.response.SfRouteQueryResponseData;
import com.yunyou.modules.interfaces.sfExpress.service.SfExpressInterfaceService;
import com.yunyou.modules.interfaces.sto.entity.CommonResultVO;
import com.yunyou.modules.interfaces.sto.entity.StoTraceQueryRequest;
import com.yunyou.modules.interfaces.sto.entity.StoTraceQueryResponse;
import com.yunyou.modules.interfaces.sto.service.StoInterfaceService;
import com.yunyou.modules.interfaces.yto.entity.routeQuery.response.YTORouteQueryResponse;
import com.yunyou.modules.interfaces.yto.service.YTOInterfaceService;
import com.yunyou.modules.interfaces.yunda.entity.routeQuery.response.YundaRouteQueryResponse;
import com.yunyou.modules.interfaces.yunda.entity.routeQuery.response.YundaRouteQueryStepsResponse;
import com.yunyou.modules.interfaces.yunda.service.YundaInterfaceService;
import com.yunyou.modules.interfaces.zto.entity.ZtoGetTraceInfoResponse;
import com.yunyou.modules.interfaces.zto.entity.ZtoGetTraceResponse;
import com.yunyou.modules.interfaces.zto.service.ZtoInterfaceService;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmTransportOrderTrack;
import com.yunyou.modules.tms.order.service.TmTransportOrderDeliveryService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import com.yunyou.modules.tms.report.service.TmsRoutingNodeExceptionService;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelationEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinWmCarrierTypeRelationService;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TmUpdateJobManager extends BaseService {
    @Autowired
    private TmInteractiveService tmInteractiveService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private BanQinWmCarrierTypeRelationService wmCarrierTypeRelationService;
    @Autowired
    private TmsRoutingNodeExceptionService tmsRoutingNodeExceptionService;
    @Autowired
    private KdBestInterfaceService kdBestInterfaceService;
    @Autowired
    private SfExpressInterfaceService sfExpressInterfaceService;
    @Autowired
    private ZtoInterfaceService ztoInterfaceService;
    @Autowired
    private YundaInterfaceService yundaInterfaceService;
    @Autowired
    private StoInterfaceService stoInterfaceService;
    @Autowired
    private YTOInterfaceService ytoInterfaceService;
    @Autowired
    private Kd100InterfaceService kd100InterfaceService;

    @Transactional
    public boolean updateTmsWayBillTrace(TmTransportOrderHeader tmTransportOrderHeader) {
        TmTransportOrderDelivery tmTransportOrderDelivery = tmTransportOrderDeliveryService.getByNo(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
        String mailNo = tmTransportOrderHeader.getTrackingNo();
        String carrierCode = tmTransportOrderDelivery.getCarrierCode();
        if (StringUtils.isBlank(mailNo) || StringUtils.isBlank(carrierCode)) {
            return false;
        }
        BanQinWmCarrierTypeRelationEntity carrierType = wmCarrierTypeRelationService.getByCarrierCode(carrierCode, tmTransportOrderHeader.getOrgId());
        if (carrierType == null || StringUtils.isBlank(carrierType.getType())) {
            throw new TmsException("承运商类型关系未维护！");
        }
        boolean signFlag = false;
        boolean isSign;
        Date signTime;
        List<TmTransportOrderTrack> signList;
        List<String> mailList = Arrays.stream(mailNo.split(",")).distinct().collect(Collectors.toList());
        switch (carrierType.getType()) {
            case WmsConstants.EXPRESS_TYPE_HTKY:// 调用百世接口，保存运单信息
                for (String no : mailList) {
                    KdTraceQueryRsp kdTraceQueryRsp = kdBestInterfaceService.kdTraceQuery(Lists.newArrayList(no), carrierType.getUrl(), carrierType.getParams());
                    if (null == kdTraceQueryRsp || !kdTraceQueryRsp.getResult()) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_HTKY, (null == kdTraceQueryRsp ? null : (kdTraceQueryRsp.getErrorCode() + ":" + kdTraceQueryRsp.getErrorDescription())));
                        continue;
                    }
                    // 存入tms订单跟踪信息表
                    updateTmTransportOrderTrack(kdTraceQueryRsp.getTraceLogs(), tmTransportOrderHeader, carrierType.getUrl(), carrierType.getParams());
                }
                // 判断更新签收时间
                signList = tmTransportOrderTrackService.getSignList(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                if (CollectionUtil.isNotEmpty(signList) && signList.size() >= mailList.size()) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignBy(signList.get(0).getOpPerson());
                    tmTransportOrderDelivery.setSignTime(signList.get(0).getOpTime());
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.BEST_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_SFEXPRESS:
                // 调用顺丰接口
                for (String no : mailList) {
                    SfRouteQueryRequest request = new SfRouteQueryRequest();
                    request.setRequestId(no);
                    request.setMailNumber(no);
                    SfRouteQueryResponse response = sfExpressInterfaceService.sfRouteQuery(request, carrierType.getUrl(), carrierType.getParams());
                    if (null == response || response.getHttpStatus() != 200) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_SFEXPRESS, (null == response ? null : response.getErrorCode() + ":" + response.getMessage()));
                        continue;
                    }
                    // 存入tms订单跟踪信息表
                    updateTmTransportOrderTrackSF(response.getData(), tmTransportOrderHeader, no);
                }
                // 判断更新签收时间
                signList = tmTransportOrderTrackService.getSignList(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                if (CollectionUtil.isNotEmpty(signList) && signList.size() >= mailList.size()) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignBy(signList.get(0).getOpPerson());
                    tmTransportOrderDelivery.setSignTime(signList.get(0).getOpTime());
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.SF_EXPRESS_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_ZTO:
                // 调用中通接口
                for (String no : mailList) {
                    ZtoGetTraceResponse response = ztoInterfaceService.getTraceInfo(new ArrayList<String>() {{
                        add(no);
                    }}, carrierType.getUrl(), carrierType.getParams());
                    if (null == response || !response.isStatus()) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_SFEXPRESS, (null == response ? null : response.getMsg()));
                        continue;
                    }
                    // 存入tms订单跟踪信息表
                    updateTmTransportOrderTrackZTO(response.getData(), tmTransportOrderHeader, no);
                }
                // 判断更新签收时间
                signList = tmTransportOrderTrackService.getSignList(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                if (CollectionUtil.isNotEmpty(signList) && signList.size() >= mailList.size()) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignTime(signList.get(0).getOpTime());
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.ZTO_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_YUNDA:
                // 调用韵达接口
                isSign = false;
                signTime = new Date();
                String signer = "";
                for (String no : mailList) {
                    YundaRouteQueryResponse response = yundaInterfaceService.routeQuery(no, carrierType.getUrl(), carrierType.getParams());
                    if (null == response || !response.getResult()) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_YUNDA, (null == response ? null : response.getRemark()));
                        continue;
                    }
                    if ("SIGNED".equals(response.getStatus()) || "SIGNFAIL".equals(response.getStatus())) {
                        isSign = true;
                        List<YundaRouteQueryStepsResponse> signedList = response.getSteps().stream().filter(t -> "SIGNED".equals(t.getAction()) || "SIGNFAIL".equals(t.getAction())).collect(Collectors.toList());
                        signTime = CollectionUtil.isNotEmpty(signedList) ? signedList.get(0).getTime() : new Date();
                        signer = CollectionUtil.isNotEmpty(signedList) ? signedList.get(0).getSigner() : "";
                    }
                    updateTmTransportOrderTrackYunda(response.getSteps(), tmTransportOrderHeader, no);
                }
                if (isSign) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignBy(signer);
                    tmTransportOrderDelivery.setSignTime(signTime);
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.YUNDA_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_STO:
                // 调用申通接口
                for (String no : mailList) {
                    StoTraceQueryRequest stoTraceQueryRequest = new StoTraceQueryRequest();
                    stoTraceQueryRequest.setOrder("asc");
                    stoTraceQueryRequest.setWaybillNoList(Lists.newArrayList(no));
                    List<StoTraceQueryResponse> response = stoInterfaceService.traceQuery(stoTraceQueryRequest, carrierType.getUrl(), carrierType.getParams());
                    if (CollectionUtil.isEmpty(response)) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_STO, null);
                        continue;
                    }
                    updateTmTransportOrderTrackSto(response.get(0).getTrackList(), tmTransportOrderHeader, no);
                }
                // 判断更新签收时间
                signList = tmTransportOrderTrackService.getSignList(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                if (CollectionUtil.isNotEmpty(signList) && signList.size() >= mailList.size()) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignTime(signList.get(0).getOpTime());
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.STO_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_YTO:
                isSign = false;
                signTime = new Date();
                for (String no : mailList) {
                    List<YTORouteQueryResponse> response = ytoInterfaceService.routeQuery(no, carrierType.getUrl(), carrierType.getParams());
                    if (CollectionUtil.isEmpty(response)) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_YTO, null);
                        continue;
                    }
                    updateTmTransportOrderTrackYto(response, tmTransportOrderHeader, no);

                    List<YTORouteQueryResponse> signInfo = response.stream().filter(t -> t.getProcessInfo().contains("签收")).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(signInfo)) {
                        isSign = true;
                        signTime = DateUtils.parseDate(signInfo.get(0).getUpload_Time());
                    }
                }
                if (isSign) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignTime(signTime);
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.YUNDA_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            case WmsConstants.EXPRESS_TYPE_KD100:
                for (String no : mailList) {
                    QueryTrackResp response = kd100InterfaceService.routeQuery(carrierCode, no, carrierType.getUrl(), carrierType.getParams());
                    if (response == null) {
                        tmsRoutingNodeExceptionService.saveInfo(tmTransportOrderHeader.getTransportNo(), no, WmsConstants.EXPRESS_TYPE_KD100, null);
                        continue;
                    }
                    updateTmTransportOrderTrackKd100(response, tmTransportOrderHeader, no);
                }
                // 判断更新签收时间
                signList = tmTransportOrderTrackService.getSignList(tmTransportOrderHeader.getTransportNo(), tmTransportOrderHeader.getOrgId());
                if (CollectionUtil.isNotEmpty(signList) && signList.size() >= mailList.size()) {
                    tmTransportOrderDelivery.setSignStatus(Global.Y);
                    tmTransportOrderDelivery.setSignTime(signList.get(0).getOpTime());
                    tmTransportOrderDelivery.setUpdateDate(new Date());
                    tmTransportOrderDelivery.setUpdateBy(InterfaceConstant.KD100_USER);
                    tmTransportOrderDeliveryService.save(tmTransportOrderDelivery);
                    signFlag = true;
                }
                break;
            default:
                throw new TmsException("承运商类型关系未维护！");
        }
        return signFlag;
    }

    /**
     * 更新tms订单跟踪信息
     */
    @Transactional
    public void updateTmTransportOrderTrack(List<TraceLogs> wayBillList, TmTransportOrderHeader tmTransportOrderHeader, String url, String params) {
        if (CollectionUtil.isEmpty(wayBillList)) {
            return;
        }
        for (TraceLogs wayBill : wayBillList) {
            if (wayBill.getCheck()) {
                tmInteractiveService.kdBestInspectionSubmit(wayBill.getMailNo(), tmTransportOrderHeader.getOrgId(), url, params);
            }
            String mailNo = wayBill.getMailNo();// 运单号
            Traces traces = wayBill.getTraces();
            if (traces == null || CollectionUtil.isEmpty(traces.getTrace())) {
                continue;
            }
            // 删除该订单的运单物流节点
            tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
            // 重新生成物流节点
            for (Trace trace : traces.getTrace()) {
                TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
                tmTransportOrderTrack.setCreateBy(InterfaceConstant.BEST_USER);
                tmTransportOrderTrack.setCreateDate(new Date());
                tmTransportOrderTrack.setUpdateBy(InterfaceConstant.BEST_USER);
                tmTransportOrderTrack.setUpdateDate(new Date());
                tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
                tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
                tmTransportOrderTrack.setLabelNo(mailNo);
                tmTransportOrderTrack.setReceiveOutletName(trace.getAcceptAddress());
                tmTransportOrderTrack.setOpNode(trace.getScanType());
                tmTransportOrderTrack.setOperation(trace.getRemark());
                if (trace.getAcceptTime() != null) {
                    tmTransportOrderTrack.setOpTime(DateUtils.parseDate(trace.getAcceptTime()));
                }
                tmTransportOrderTrackService.save(tmTransportOrderTrack);
            }
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（顺丰）
     */
    @Transactional
    public void updateTmTransportOrderTrackSF(List<SfRouteQueryResponseData> dataList, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (SfRouteQueryResponseData data : dataList) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.SF_EXPRESS_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.SF_EXPRESS_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setReceiveOutletName(data.getAcceptAddress());
            tmTransportOrderTrack.setOpNode(data.getOpCode());
            tmTransportOrderTrack.setOperation(data.getRemark());
            if (data.getAcceptTime() != null) {
                tmTransportOrderTrack.setOpTime(data.getAcceptTime());
            }
            tmTransportOrderTrackService.save(tmTransportOrderTrack);
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（中通）
     */
    @Transactional
    public void updateTmTransportOrderTrackZTO(List<ZtoGetTraceResponse.Data> dataList, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (ZtoGetTraceInfoResponse response : dataList.get(0).getTraces()) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.ZTO_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.ZTO_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setReceiveOutletName(response.getScanCity());
            tmTransportOrderTrack.setOpNode(response.getScanType());
            tmTransportOrderTrack.setOperation(response.getDesc());
            if (StringUtils.isNotBlank(response.getScanDate())) {
                tmTransportOrderTrack.setOpTime(DateUtils.parseDate(response.getScanDate()));
            }
            tmTransportOrderTrackService.save(tmTransportOrderTrack);
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（韵达）
     */
    @Transactional
    public void updateTmTransportOrderTrackYunda(List<YundaRouteQueryStepsResponse> dataList, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (YundaRouteQueryStepsResponse data : dataList) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.YUNDA_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.YUNDA_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setReceiveOutletName(data.getStation_name());
            tmTransportOrderTrack.setOpNode(data.getAction());
            tmTransportOrderTrack.setOperation(data.getDescription());
            if (data.getTime() != null) {
                tmTransportOrderTrack.setOpTime(data.getTime());
            }
            tmTransportOrderTrackService.save(tmTransportOrderTrack);
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（申通）
     */
    @Transactional
    public void updateTmTransportOrderTrackSto(List<CommonResultVO> dataList, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (CommonResultVO response : dataList) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.ZTO_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.ZTO_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setReceiveOutletName(response.getOpOrgProvinceName() + response.getOpOrgCityName() + response.getOpOrgName());
            tmTransportOrderTrack.setOpNode(response.getScanType());
            tmTransportOrderTrack.setOperation(response.getMemo());
            if (StringUtils.isNotBlank(response.getOpTime())) {
                tmTransportOrderTrack.setOpTime(DateUtils.parseDate(response.getOpTime()));
            }
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（圆通）
     */
    @Transactional
    public void updateTmTransportOrderTrackYto(List<YTORouteQueryResponse> dataList, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(dataList)) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (YTORouteQueryResponse response : dataList) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.YTO_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.YTO_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setOperation(response.getProcessInfo());
            if (StringUtils.isNotBlank(response.getUpload_Time())) {
                tmTransportOrderTrack.setOpTime(DateUtils.parseDate(response.getUpload_Time()));
            }
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }

    /**
     * 更新tms订单跟踪信息（快递100）
     */
    @Transactional
    public void updateTmTransportOrderTrackKd100(QueryTrackResp data, TmTransportOrderHeader tmTransportOrderHeader, String mailNo) {
        if (CollectionUtil.isEmpty(data.getData())) {
            return;
        }
        // 删除该订单的运单物流节点
        tmTransportOrderTrackService.removeByTransportNoAndLabelNo(tmTransportOrderHeader.getTransportNo(), mailNo, tmTransportOrderHeader.getOrgId());
        // 重新生成物流节点
        for (QueryTrackData response : data.getData()) {
            TmTransportOrderTrack tmTransportOrderTrack = new TmTransportOrderTrack();
            tmTransportOrderTrack.setCreateBy(InterfaceConstant.KD100_USER);
            tmTransportOrderTrack.setCreateDate(new Date());
            tmTransportOrderTrack.setUpdateBy(InterfaceConstant.KD100_USER);
            tmTransportOrderTrack.setUpdateDate(new Date());
            tmTransportOrderTrack.setTransportNo(tmTransportOrderHeader.getTransportNo());
            tmTransportOrderTrack.setCustomerNo(tmTransportOrderHeader.getCustomerNo());
            tmTransportOrderTrack.setLabelNo(mailNo);
            tmTransportOrderTrack.setOpNode(response.getStatus());
            tmTransportOrderTrack.setOperation(response.getContext());
            tmTransportOrderTrack.setOpTime(DateUtils.parseDate(response.getFtime()));
            if (data.getRouteInfo() != null && data.getRouteInfo().getCur() != null) {
                tmTransportOrderTrack.setReceiveOutletName(data.getRouteInfo().getCur().getName());
            }
            tmTransportOrderTrackService.save(tmTransportOrderTrack);
        }
        // 更新运输订单更新时间
        tmTransportOrderHeaderService.save(tmTransportOrderHeaderService.get(tmTransportOrderHeader.getId()));
    }
}
