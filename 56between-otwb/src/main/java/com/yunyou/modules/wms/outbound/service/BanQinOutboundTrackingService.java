package com.yunyou.modules.wms.outbound.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import com.yunyou.modules.interfaces.kd100.Kd100InterfaceService;
import com.yunyou.modules.interfaces.kd100.entity.CloudPrintOldParam;
import com.yunyou.modules.interfaces.kd100.entity.PrintBaseResp;
import com.yunyou.modules.interfaces.kd100.entity.PrintImgData;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response.KdWaybillApplyNotifyRsp;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.SfCreateOrderResponse;
import com.yunyou.modules.interfaces.sto.entity.StoOrderCreateResponse;
import com.yunyou.modules.interfaces.yto.entity.createOrder.response.YTOCreateOrderResponseData;
import com.yunyou.modules.interfaces.yto.utils.YTOUtils;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponse;
import com.yunyou.modules.interfaces.yunda.utils.YundaUtils;
import com.yunyou.common.utils.number.BigDecimalUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelationEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinWmCarrierTypeRelationService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmGetWayBillLog;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 快递面单操作类
 * @author WMJ
 * @version 2019/04/10
 */
@Service
public class BanQinOutboundTrackingService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmWvHeaderService wmWvHeaderService;
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private BanQinWmCarrierTypeRelationService banQinWmCarrierTypeRelationService;
    @Autowired
    private BanQinOutboundPackService outboundPackService;
    @Autowired
    private BanQinWmGetWayBillLogService wmGetWayBillLogService;
    @Autowired
    private Kd100InterfaceService kd100InterfaceService;

    /**
     * 获取面单BY波次
     *
     * @param waveNoList 波次单号
     * @param orgId      机构ID
     */
    public ResultMessage getWaybillByWave(List<String> waveNoList, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String waveNo : waveNoList) {
            List<BanQinWmSoEntity> list = wmSoHeaderService.findEntityByWaveNo(waveNo, orgId);
            if (CollectionUtil.isEmpty(list)) {
                continue;
            }
            // 过滤不是分配状态和已经打包的SO单
            List<BanQinWmSoEntity> finalList = filterNotCanGetWayBill(list);
            if (list.size() != finalList.size()) {
                msg.addMessage("波次号[" + waveNo + "]存在不是分配状态或不是拣货状态或不是未打包状态的订单");
                continue;
            }
            for (BanQinWmSoEntity entity : finalList) {
                entity.setOrderSeq(entity.getOrderSeq() + "/" + list.size());
                try {
                    requestExpress(entity);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage() + "<br>");
                }
            }
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
        } else {
            msg.setSuccess(true);
            msg.setMessage("操作成功");
        }
        return msg;
    }

    /**
     * 获取面单BY出库单
     *
     * @param soNoList 出库单号
     * @param orgId    机构ID
     */
    public ResultMessage getWaybillBySo(List<String> soNoList, String orgId) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmSoEntity> list = ListUtil.newArrayList();
        for (String soNo : soNoList) {
            list.add(wmSoHeaderService.findEntityBySoNo(soNo, orgId));
        }
        if (CollectionUtil.isNotEmpty(list)) {
            // 过滤不是分配状态和已经打包的SO单
            List<BanQinWmSoEntity> finalList = filterNotCanGetWayBill(list);
            for (BanQinWmSoEntity entity : finalList) {
                entity.setOrderSeq(entity.getOrderSeq() + "/" + list.size());
                try {
                    requestExpress(entity);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage() + "<br>");
                }
            }
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
        } else {
            msg.setSuccess(true);
            msg.setMessage("操作成功");
        }
        return msg;
    }

    /**
     * 打印面单BY波次
     *
     * @param waveNoList 波次单号
     * @param orgId      机构ID
     */
    public ResultMessage printWaybillByWave(List<String> waveNoList, String orgId) {
        ResultMessage msg = new ResultMessage();
        List<String> result = Lists.newArrayList();
        for (String waveNo : waveNoList) {
            List<BanQinWmSoAllocEntity> list = wmSoAllocService.getEntityByWaveNo(waveNo, orgId);
            List<BanQinWmSoEntity> soList = wmSoHeaderService.findEntityByWaveNo(waveNo, orgId);
            result.addAll(getPrintImages(list, soList));
            // 更新波次打印面单标记
            wmWvHeaderService.updatePrintWayBillFlag(waveNo, orgId);
        }
        msg.setData(result);
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 打印面单BY出库单
     *
     * @param soNoList 出库单号
     * @param orgId    机构ID
     */
    public ResultMessage printWaybillBySo(List<String> soNoList, String orgId) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmSoAllocEntity> allocList = ListUtil.newArrayList();
        List<BanQinWmSoEntity> soList = ListUtil.newArrayList();
        for (String soNo : soNoList) {
            allocList.addAll(wmSoAllocService.getEntityBySoNo(soNo, orgId));
            soList.add(wmSoHeaderService.findEntityBySoNo(soNo, orgId));
        }
        msg.setData(getPrintImages(allocList, soList));
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 打印面单BY分配明细
     *
     * @param allocIds 分配ID
     * @param orgId    机构ID
     */
    public ResultMessage printWaybillByAlloc(List<String> allocIds, String orgId) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmSoAllocEntity> allocList = ListUtil.newArrayList();
        for (String allocId : allocIds) {
            allocList.add(wmSoAllocService.getEntityByAllocId(allocId, orgId));
        }
        List<BanQinWmSoEntity> soList = ListUtil.newArrayList();
        List<String> soNoList = allocList.stream().map(BanQinWmSoAllocEntity::getSoNo).distinct().collect(Collectors.toList());
        for (String soNo : soNoList) {
            soList.add(wmSoHeaderService.findEntityBySoNo(soNo, orgId));
        }
        msg.setData(getPrintImages(allocList, soList));
        msg.setMessage("操作成功");
        return msg;
    }

    private List<String> getPrintImages(List<BanQinWmSoAllocEntity> list, List<BanQinWmSoEntity> soList) {
        List<String> result = Lists.newArrayList();
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }
        Map<String, List<BanQinWmSoAllocEntity>> collect = list.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getSoNo));
        soList.sort(Comparator.comparing(BanQinWmSoEntity::getOrderSeq));
        Map<String, BanQinWmSoEntity> soMap = soList.stream().collect(Collectors.toMap(BanQinWmSoEntity::getSoNo, Function.identity(), (key1, key2) -> key2, LinkedHashMap::new));
        for (Map.Entry<String, BanQinWmSoEntity> entry : soMap.entrySet()) {
            List<String> printList = ListUtil.newArrayList();
            BanQinWmSoEntity wmSoEntity = entry.getValue();
            String reportName = "";
            wmSoEntity.setOrderSeq(StringUtils.isNotBlank(wmSoEntity.getOrderSeq()) ? (wmSoEntity.getOrderSeq() + "/" + soList.size()) : "1/1");
            BanQinWmCarrierTypeRelationEntity carrierType = banQinWmCarrierTypeRelationService.getByCarrierCode(wmSoEntity.getCarrierCode(), wmSoEntity.getOrgId());
            if (null != carrierType) {
                reportName = outboundPackService.getReportName(carrierType.getMailType());
            }

            List<BanQinWmSoAllocEntity> allocList = collect.get(entry.getKey());
            double sumQty = allocList.stream().mapToDouble(BanQinWmSoAlloc::getQtyEa).sum();
            allocList = allocList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BanQinWmSoAlloc::getTrackingNo))), ArrayList::new));
            for (BanQinWmSoAllocEntity entity : allocList) {
                List<BanQinWmGetWayBillLog> logList = wmGetWayBillLogService.findByUniKey(entity.getSoNo(), entity.getTrackingNo(), entity.getCaseNo(), entity.getOrgId());
                if (CollectionUtil.isEmpty(logList)) {
                    continue;
                }
                BanQinWmGetWayBillLog log = logList.get(0);
                switch (log.getType()) {
                    case WmsConstants.EXPRESS_TYPE_HTKY:
                        KdWaybillApplyNotifyRsp response = Parser.coverXml2Object(log.getResponse(), KdWaybillApplyNotifyRsp.class);
                        printList = outboundPackService.getPrintListBest(response, wmSoEntity, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_SFEXPRESS:
                        SfCreateOrderResponse sfResponse = JSONObject.parseObject(log.getResponse(), SfCreateOrderResponse.class);
                        printList = outboundPackService.getPrintListSf(sfResponse, wmSoEntity, sumQty, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_ZT:
                        printList = outboundPackService.getPrintListSelf(sumQty, wmSoEntity, log.getTrackingNo(), reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_ZTO:
                        Map map = JSONObject.parseObject(log.getResponse(), Map.class);
                        printList = outboundPackService.getPrintListZto(map, wmSoEntity, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_YUNDA:
                        YundaCreateOrderResponse yundaResponse = XmlObjectUtils.coverXml2Object(log.getResponse(), YundaCreateOrderResponse.class);
                        printList = outboundPackService.getPrintListYunda(yundaResponse, wmSoEntity, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_STO:
                        StoOrderCreateResponse stoResponse = JSONObject.parseObject(log.getResponse(), StoOrderCreateResponse.class);
                        printList = outboundPackService.getPrintListSto(stoResponse, wmSoEntity, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_YTO:
                        YTOCreateOrderResponseData ytoResponse = XmlObjectUtils.coverXml2Object(log.getResponse(), YTOCreateOrderResponseData.class);
                        printList = outboundPackService.getPrintListYto(ytoResponse, wmSoEntity, reportName);
                        break;
                    case WmsConstants.EXPRESS_TYPE_KD100:
                        if (carrierType == null) {
                            return printList;
                        }
                        PrintBaseResp<PrintImgData> kd100Response = kd100InterfaceService.replay(new CloudPrintOldParam(log.getResponse()), carrierType.getUrl(), carrierType.getParams());
                        printList = outboundPackService.getPrintListKd100(kd100Response);
                        break;
                    default:
                        break;
                }
                result.addAll(printList);
            }
        }
        return result;
    }

    private List<BanQinWmSoEntity> filterNotCanGetWayBill(List<BanQinWmSoEntity> list) {
        return list.stream().filter(s -> WmsCodeMaster.SO_PART_ALLOC.getCode().equals(s.getStatus())
                || WmsCodeMaster.SO_FULL_ALLOC.getCode().equals(s.getStatus())
                || WmsCodeMaster.SO_PART_PICKING.getCode().equals(s.getStatus())
                || WmsCodeMaster.SO_FULL_PICKING.getCode().equals(s.getStatus()))
                .filter(s -> WmsCodeMaster.PACK_NEW.getCode().equals(s.getPackStatus()))
                .collect(Collectors.toList());
    }

    private List<BanQinWmSoAlloc> getAllocInfo(BanQinWmSoEntity entity) {
        return wmSoAllocService.getBySoNo(entity.getSoNo(), entity.getOrgId());
    }

    @Transactional
    public Map<String, String> getMailInfo(BanQinWmSoEntity wmSoEntity) {
        Map<String, String> map = MapUtil.newHashMap();
        if (StringUtils.isBlank(wmSoEntity.getCarrierCode())) {
            throw new WarehouseException("[" + wmSoEntity.getSoNo() + "]承运商未维护！");
        }
        BanQinWmCarrierTypeRelationEntity carrierType = banQinWmCarrierTypeRelationService.getByCarrierCode(wmSoEntity.getCarrierCode(), wmSoEntity.getOrgId());
        if (carrierType == null || StringUtils.isBlank(carrierType.getType())) {
            throw new WarehouseException("[" + wmSoEntity.getSoNo() + "]承运商类型关系未维护！");
        }
        // 调用接口
        ResultMessage mailInfo = outboundPackService.getMailInfo(wmSoEntity, wmSoEntity.getSoNo(), carrierType);
        if (!mailInfo.isSuccess()) {
            throw new WarehouseException(mailInfo.getMessage());
        }
        String mailNo;
        String consigneeAddress;
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        String response = null;
        switch (carrierType.getType()) {
            case WmsConstants.EXPRESS_TYPE_HTKY:
                KdWaybillApplyNotifyRsp kdResponse = (KdWaybillApplyNotifyRsp) mailInfo.getData();
                mailNo = outboundPackService.getMailNo(kdResponse);
                consigneeAddress = kdResponse.getEDIPrintDetailList().get(0).getReceiveManAddress();
                response = Parser.coverObject2Xml(kdResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_SFEXPRESS:
                SfCreateOrderResponse sfResponse = (SfCreateOrderResponse) mailInfo.getData();
                mailNo = outboundPackService.getMailNoSf(sfResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                response = JSONObject.toJSONString(sfResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_ZT:
                mailNo = wmSoEntity.getSoNo();
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                break;
            case WmsConstants.EXPRESS_TYPE_ZTO:
                Map ztoMap = (Map) mailInfo.getData();
                mailNo = ztoMap.get("mailNo").toString();
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                response = JSONObject.toJSONString(ztoMap);
                break;
            case WmsConstants.EXPRESS_TYPE_YUNDA:
                YundaCreateOrderResponse yundaResponse = (YundaCreateOrderResponse) mailInfo.getData();
                mailNo = outboundPackService.getMailNoYunda(yundaResponse);
                consigneeAddress = outboundPackService.getConsigneeAddressYunda(yundaResponse);
                response = YundaUtils.responseCover2Xml(yundaResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_STO:
                StoOrderCreateResponse stoResponse = (StoOrderCreateResponse) mailInfo.getData();
                mailNo = outboundPackService.getMailNoSto(stoResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                response = JSONObject.toJSONString(stoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_YTO:
                YTOCreateOrderResponseData ytoResponse = (YTOCreateOrderResponseData) mailInfo.getData();
                mailNo = outboundPackService.getMailNoYto(ytoResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                response = YTOUtils.responseCover2Xml(ytoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_KD100:
                PrintBaseResp<PrintImgData> kd100Response = (PrintBaseResp<PrintImgData>) mailInfo.getData();
                mailNo = outboundPackService.getMailNoKd100(kd100Response);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                response = kd100Response.getData().getTaskId();// 快递100的任务ID，用于复打面单
                break;
            default:
                throw new WarehouseException("[" + wmSoEntity.getSoNo() + "]承运商类型关系未维护！");
        }
        map.put("mailNo", mailNo);
        map.put("consigneeAddress", consigneeAddress);
        map.put("response", response);
        map.put("type", carrierType.getType());
        return map;
    }

    @Transactional
    public void requestExpress(BanQinWmSoEntity entity) {
        entity.setQtyTotal(wmSoAllocService.getBySoNo(entity.getSoNo(), entity.getOrgId()).stream()
                .filter(o -> WmsCodeMaster.SO_FULL_ALLOC.getCode().equals(o.getStatus()) || WmsCodeMaster.SO_FULL_PICKING.getCode().equals(o.getStatus()))
                .mapToDouble(BanQinWmSoAlloc::getQtyEa).reduce(0, BigDecimalUtil::add));
        // 呼面单接口
        Map<String, String> mailInfo = getMailInfo(entity);
        // 插入面单信息
        wmGetWayBillLogService.saveInfo(entity.getWaveNo(), entity.getSoNo(), mailInfo.get("mailNo"), entity.getSoNo(), mailInfo.get("type"), mailInfo.get("response"), entity.getOrgId(), WmsConstants.ZERO, entity.getOrderSeq());
        // 更新分配明细的面单信息和地址
        List<BanQinWmSoAlloc> allocList = getAllocInfo(entity);
        for (BanQinWmSoAlloc alloc : allocList) {
            wmSoAllocService.updatePackInfo(alloc.getId(), mailInfo.get("mailNo"), mailInfo.get("consigneeAddress"), entity.getSoNo());
        }
        // 更新波次获取面单标记
        if (StringUtils.isNotBlank(entity.getWaveNo())) {
            wmWvHeaderService.updateGetWayBillFlag(entity.getWaveNo(), entity.getOrgId());
        }
    }

}