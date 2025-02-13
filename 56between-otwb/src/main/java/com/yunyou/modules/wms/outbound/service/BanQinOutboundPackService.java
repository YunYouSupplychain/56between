package com.yunyou.modules.wms.outbound.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.number.PhoneUtil;
import com.yunyou.common.utils.unicode.UnicodeUtils;
import com.yunyou.modules.interfaces.kd100.Kd100InterfaceService;
import com.yunyou.modules.interfaces.kd100.entity.PrintBaseResp;
import com.yunyou.modules.interfaces.kd100.entity.PrintImgData;
import com.yunyou.modules.interfaces.kd100.entity.PrintImgParam;
import com.yunyou.modules.interfaces.kdBest.entity.KdWaybillApplyNotifyRequestEntity;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response.EDIPrintDetailList;
import com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.response.KdWaybillApplyNotifyRsp;
import com.yunyou.modules.interfaces.kdBest.service.KdBestInterfaceService;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.request.Cargo;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.request.SfCreateOrderRequest;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.RlsDetail;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.RlsInfo;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.SfCreateOrderResponse;
import com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response.SfCreateOrderResponseData;
import com.yunyou.modules.interfaces.sfExpress.service.SfExpressInterfaceService;
import com.yunyou.modules.interfaces.sto.entity.*;
import com.yunyou.modules.interfaces.sto.service.StoInterfaceService;
import com.yunyou.modules.interfaces.yto.entity.createOrder.request.YTOCreateOrderReceiverRequest;
import com.yunyou.modules.interfaces.yto.entity.createOrder.request.YTOCreateOrderRequest;
import com.yunyou.modules.interfaces.yto.entity.createOrder.request.YTOCreateOrderSenderRequest;
import com.yunyou.modules.interfaces.yto.entity.createOrder.response.YTOCreateOrderResponseData;
import com.yunyou.modules.interfaces.yto.service.YTOInterfaceService;
import com.yunyou.modules.interfaces.yto.utils.YTOUtils;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.request.YundaCreateOrderCustomerRequest;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.request.YundaCreateOrderRequest;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponse;
import com.yunyou.modules.interfaces.yunda.entity.createOrder.response.YundaCreateOrderResponseData;
import com.yunyou.modules.interfaces.yunda.service.YundaInterfaceService;
import com.yunyou.modules.interfaces.yunda.utils.YundaUtils;
import com.yunyou.modules.interfaces.zto.entity.*;
import com.yunyou.modules.interfaces.zto.service.ZtoInterfaceService;
import com.yunyou.modules.print.entity.*;
import com.yunyou.modules.print.service.BackstagePrintService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.common.utils.number.BigDecimalUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelationEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinWmCarrierTypeRelationService;
import com.yunyou.modules.wms.common.entity.BanQinCdWhPackageRelationEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 打包功能
 *
 * @author WMJ
 * @version 2019/02/22
 */
@Service
public class BanQinOutboundPackService {
    @Autowired
    private BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private BanQinWmSoSerialService wmSoSerialService;
    @Autowired
    private BanQinWmsCommonService wmCommon;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmSoPackService wmSoPackService;
    @Autowired
    private KdBestInterfaceService kdBestInterfaceService;
    @Autowired
    private BackstagePrintService backstagePrintService;
    @Autowired
    private BanQinWmCarrierTypeRelationService banQinWmCarrierTypeRelationService;
    @Autowired
    private SfExpressInterfaceService sfExpressInterfaceService;
    @Autowired
    private BanQinOutboundCommonService outboundCommonService;
    @Autowired
    private BanQinWmGetWayBillLogService wmGetWayBillLogService;
    @Autowired
    private ZtoInterfaceService ztoInterfaceService;
    @Autowired
    private BanQinWmPackSerialService wmPackSerialService;
    @Autowired
    private YundaInterfaceService yundaInterfaceService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;
    @Autowired
    private StoInterfaceService stoInterfaceService;
    @Autowired
    private YTOInterfaceService ytoInterfaceService;
    @Autowired
    private Kd100InterfaceService kd100InterfaceService;

    /**
     * 打包
     *
     * @param allocItems   分配明细
     * @param toId         目标跟踪号
     * @param soSerialList 出库序列号
     * @param trackingNo   快递单号
     * @param isCheck      是否复核
     */
    @Transactional
    public List<BanQinWmSoAlloc> updateTraceId(List<BanQinWmSoAllocEntity> allocItems, String toId, List<BanQinWmSoSerialEntity> soSerialList, String trackingNo, String isCheck, BanQinWmSoEntity wmSoEntity) {
        List<BanQinWmSoAlloc> result = Lists.newArrayList();
        String userName = UserUtils.getUser().getName();
        List<BanQinWmSoSerial> serialModels = Lists.newArrayList();
        List<BanQinWmSoSerialEntity> unSerialModels = Lists.newArrayList();
        for (BanQinWmSoAllocEntity allocItem : allocItems) {
            // 源记录信息
            BanQinWmSoAlloc oldAllocModel = this.wmSoAllocService.getByAllocId(allocItem.getAllocId(), allocItem.getOrgId());
            Double qtyUom = oldAllocModel.getQtyUom();// 源单位数
            Double qtyEa = oldAllocModel.getQtyEa();// 源单位数EA
            String uom = oldAllocModel.getUom();// 源单位
            // 界面传入，目标包装单位
            Double newQtyEa = allocItem.getQtyPackEa();// 打包数量
            String packCode = allocItem.getPackCode();// 目标包装代码
            String newUom = allocItem.getUom();// 目标包装单位
            Double packWeight = allocItem.getPackWeight();// 包裹重量
            String scanCaseNo = allocItem.getCaseNo();// 扫描箱号
            // 目标单位包装换算数量
            Long uomQty;
            // 如果目标单位不为空,那么获取换算数量
            if (StringUtils.isNotEmpty(packCode) && StringUtils.isNotEmpty(newUom)) {
                BanQinCdWhPackageRelationEntity packageEntity = wmCommon.getPackageRelationAndQtyUom(packCode, newUom, allocItem.getOrgId());
                uomQty = packageEntity.getCdprQuantity().longValue();// 目标单位换算数量
            } else {
                throw new WarehouseException("包装代码不存在或配置错误", packCode);
            }
            // 如果源单位!=目标单位，那么计算源数据单位数
            if (!newUom.equals(uom)) {
                // 源单位ea数量/目标单位换算数量 ，将源单位数量换算成与目标单位数量的单位数量同级
                qtyUom = qtyEa / uomQty;
                oldAllocModel.setUom(newUom);
                oldAllocModel.setQtyUom(qtyUom);
            }
            // 库存交易任务ID = 分配ID
            // 如果源拣货数EA=目标打包数量EA
            if (qtyEa.equals(newQtyEa)) {
                // oldAllocModel.setToId(toId);// 目标跟踪号修改
                oldAllocModel.setCaseNo(toId);
                oldAllocModel.setPackOp(userName);// 打包人
                oldAllocModel.setPackTime(new Date());// 打包时间
                oldAllocModel.setRemarks(allocItem.getRemarks());
                // 源单位数量*目标单位换算数量(源单位数量已经换算成与目标单位数量同级单位数)
                oldAllocModel.setQtyEa(qtyUom * uomQty);
                // 如果需要复核，那么写复核状态
                if (WmsConstants.YES.equals(isCheck)) {
                    oldAllocModel.setCheckTime(new Date());
                    oldAllocModel.setCheckOp(userName);
                    oldAllocModel.setCheckStatus(WmsCodeMaster.CHECK_CLOSE.getCode());
                }
                // 如果需要换快递单号，则写快递单号
                if (StringUtils.isNotEmpty(trackingNo)) {
                    oldAllocModel.setTrackingNo(trackingNo);
                }
                oldAllocModel.setPackWeight(packWeight);// 包裹重量
                if (soSerialList.size() > 0) {
                    // 出库序列号(库存序列号管理)
                    List<BanQinWmSoSerialEntity> isSerialList = soSerialList.stream().filter(s -> !WmsConstants.YES.equals(s.getIsUnSerial())).collect(Collectors.toList());
                    serialModels.addAll(updateWmSoSerial(oldAllocModel.getAllocId(), oldAllocModel.getAllocId(), isSerialList, oldAllocModel.getQtyEa()));
                    // 非库存序列号管理
                    unSerialModels = soSerialList.stream().filter(s -> WmsConstants.YES.equals(s.getIsUnSerial())).collect(Collectors.toList());
                }
                // 分配拣货记录
                wmSoAllocService.save(oldAllocModel);
                // 更新打包记录
                updateWmPack(oldAllocModel, wmSoEntity, allocItem.getSkuName(), scanCaseNo);
                // 更新打包扫描次数
                if (allocItem.getPackScanCount() != null) {
                    wmSoAllocService.updatePackScanCount(oldAllocModel.getId(), allocItem.getPackScanCount());
                }
                result.add(oldAllocModel);
            }
            // 如果源拣货数EA>目标打包数量EA，需要拆分记录
            else if (qtyEa > newQtyEa) {
                // 打包拆分的新分配记录(完成打包)
                BanQinWmSoAlloc newAllocModel = new BanQinWmSoAlloc();
                BeanUtils.copyProperties(oldAllocModel, newAllocModel);// 复制
                newAllocModel.setPackOp(userName);
                newAllocModel.setPackTime(new Date());
                newAllocModel.setRemarks(allocItem.getRemarks());
                // 新分配ID
                String newAllocId = noService.getDocumentNo(GenNoType.WM_ALLOC_ID.name());
                newAllocModel.setAllocId(newAllocId);
                // 新单位数量*目标单位换算数量
                newAllocModel.setQtyEa(newQtyEa);// 打包数量
                newAllocModel.setQtyUom(newAllocModel.getQtyEa() / uomQty);
                newAllocModel.setId(IdGen.uuid());
                newAllocModel.setIsNewRecord(true);
                // 目标跟踪号
                // newAllocModel.setToId(toId);
                newAllocModel.setCaseNo(toId);
                // 打包分配明细写复核状态
                if (WmsConstants.YES.equals(isCheck)) {
                    newAllocModel.setCheckTime(new Date());
                    newAllocModel.setCheckOp(userName);
                    newAllocModel.setCheckStatus(WmsCodeMaster.CHECK_CLOSE.getCode());
                }
                // 如果需要换快递单号，则写快递单号
                if (StringUtils.isNotEmpty(trackingNo)) {
                    newAllocModel.setTrackingNo(trackingNo);
                }
                newAllocModel.setPackWeight(packWeight);// 包裹重量
                // 目标分配拣货记录
                wmSoAllocService.save(newAllocModel);
                // 原单位数量-打包单位数量
                oldAllocModel.setQtyEa(qtyEa - newQtyEa);
                oldAllocModel.setQtyUom(oldAllocModel.getQtyEa() / uomQty);
                if (soSerialList.size() > 0) {
                    // 出库序列号
                    serialModels.addAll(updateWmSoSerial(oldAllocModel.getAllocId(), newAllocId, soSerialList, newAllocModel.getQtyEa()));
                }
                wmSoAllocService.save(oldAllocModel);
                // 更新打包记录
                updateWmPack(newAllocModel, wmSoEntity, allocItem.getSkuName(), scanCaseNo);
                // 更新打包扫描次数
                if (allocItem.getPackScanCount() != null) {
                    wmSoAllocService.updatePackScanCount(newAllocModel.getId(), allocItem.getPackScanCount());
                }
                result.add(newAllocModel);
            }
            /*// set源转移信息
            BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
            fminvm.setLotNum(oldAllocModel.getLotNum());
            fminvm.setLocCode(oldAllocModel.getToLoc());
            fminvm.setTraceId(oldToId);
            fminvm.setSkuCode(oldAllocModel.getSkuCode());
            fminvm.setOwnerCode(oldAllocModel.getOwnerCode());
            fminvm.setAction(ActionCode.PACKING);
            fminvm.setPackCode(oldAllocModel.getPackCode());
            fminvm.setQtyUom(allocItem.getQtyPackUom());
            fminvm.setUom(allocItem.getUom());
            fminvm.setQtyEaOp(allocItem.getQtyPackEa());
            fminvm.setOrderNo(oldAllocModel.getSoNo());
            fminvm.setLineNo(oldAllocModel.getLineNo());
            fminvm.setTaskId(oldAllocModel.getAllocId());
            fminvm.setOrgId(oldAllocModel.getOrgId());
            // set目标转移信息
            BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
            toinvm.setLotNum(oldAllocModel.getLotNum());
            toinvm.setLocCode(oldAllocModel.getToLoc());
            toinvm.setTraceId(toId);
            toinvm.setSkuCode(oldAllocModel.getSkuCode());
            toinvm.setOwnerCode(oldAllocModel.getOwnerCode());
            toinvm.setAction(ActionCode.PACKING);
            toinvm.setPackCode(oldAllocModel.getPackCode());
            toinvm.setQtyUom(allocItem.getQtyPackUom());
            toinvm.setUom(allocItem.getUom());
            toinvm.setQtyEaOp(allocItem.getQtyPackEa());
            toinvm.setOrderNo(oldAllocModel.getSoNo());
            toinvm.setLineNo(oldAllocModel.getLineNo());
            toinvm.setTaskId(newTaskId);
            toinvm.setOrgId(oldAllocModel.getOrgId());
            // 更新库存方法
            inventoryService.updateInventory(fminvm, toinvm);*/
        }
        // 写保存出库序列号
        if (serialModels.size() > 0) {
            for (BanQinWmSoSerial wmSoSerial : serialModels) {
                wmSoSerialService.save(wmSoSerial);
            }
        }
        // 插入非库存序列号管理
        if (CollectionUtil.isNotEmpty(unSerialModels)) {
            for (BanQinWmSoSerialEntity soSerialEntity : unSerialModels) {
                BanQinWmPackSerial packSerial = new BanQinWmPackSerial();
                BeanUtils.copyProperties(soSerialEntity, packSerial);
                packSerial.setId(IdGen.uuid());
                packSerial.setIsNewRecord(true);
                wmPackSerialService.save(packSerial);
            }
        }
        // 更新SO单头的打包状态
        outboundCommonService.updatePackStatus(wmSoEntity);
        return result;
    }

    /**
     * 将出库序列号的分配ID，修改为目标分配ID
     *
     * @param fmAllocId    源分配ID
     * @param toAllocId    目标分配ID
     * @param soSerialList 出库序列号
     * @param allocEa      分配数量
     */
    @Transactional
    public List<BanQinWmSoSerial> updateWmSoSerial(String fmAllocId, String toAllocId, List<BanQinWmSoSerialEntity> soSerialList, Double allocEa) {
        // 获取出库序列号
        List<BanQinWmSoSerial> models = new ArrayList<>();
        String orgId = null;
        double qtySerial = 0D;// 未保存的出库序列号数量
        for (BanQinWmSoSerialEntity serialItem : soSerialList) {
            // 如果分配ID相同，并且源分配ID != 目标分配ID，将最新的分配ID写入出库序列号中 或者出库序列号不存在
            if (StringUtils.isEmpty(serialItem.getId()) && serialItem.getAllocId().equals(fmAllocId)) {
                qtySerial++;// 统计源分配ID的新序列号
            }
            // 如果源分配ID相同，并且源分配ID != 目标分配ID 或者 未保存
            if (serialItem.getAllocId().equals(fmAllocId)) {
                if (!fmAllocId.equals(toAllocId) || StringUtils.isEmpty(serialItem.getId())) {
                    BanQinWmSoSerial model = new BanQinWmSoSerial();
                    BeanUtils.copyProperties(serialItem, model);
                    model.setId(IdGen.uuid());
                    model.setIsNewRecord(true);
                    model.setAllocId(toAllocId);
                    models.add(model);
                }
            }
            orgId = serialItem.getOrgId();
        }
        // 新序列号数大于0时，校验序列号与打包数是否一致。原因:序列号总数大于打包数 否则，部分打包
        if (qtySerial > 0) {
            List<BanQinWmSoSerialEntity> countItem = wmSoSerialService.findByAllocIds(Lists.newArrayList(fmAllocId), orgId);
            double count = countItem.size();// 存在于数据库的出库序列号
            if (!allocEa.equals(qtySerial + count)) {
                throw new WarehouseException("出库序列号总数与打包数不一致，不能操作");
            }
        }
        return models;
    }

    @Transactional
    public void updateWmPack(BanQinWmSoAlloc soAlloc, BanQinWmSoEntity wmSoEntity, String skuName, String scanCaseNo) {
        if (null != wmSoEntity) {
            BanQinWmSoPack wmSoPack = new BanQinWmSoPack();
            wmSoPack.setSoNo(wmSoEntity.getSoNo());
            wmSoPack.setSoType(wmSoEntity.getSoType());
            wmSoPack.setOrderDate(wmSoEntity.getOrderTime());
            wmSoPack.setOwnerCode(wmSoEntity.getOwnerCode());
            wmSoPack.setOwnerName(wmSoEntity.getOwnerName());
            wmSoPack.setDeliveryName(wmSoEntity.getOwnerShortName());
            wmSoPack.setDeliveryTel(wmSoEntity.getOwnerTel());
            wmSoPack.setDeliveryZip(wmSoEntity.getOwnerPostCode());
            wmSoPack.setDeliveryArea(wmSoEntity.getOwnerArea());
            wmSoPack.setDeliveryAddress(wmSoEntity.getOwnerAddress());
            wmSoPack.setConsignee(wmSoEntity.getContactName());
            wmSoPack.setConsigneeTel(wmSoEntity.getContactTel());
            wmSoPack.setConsigneeAddress(wmSoEntity.getContactAddr());
            wmSoPack.setConsigneeZip(wmSoEntity.getContactZip());
            wmSoPack.setConsigneeArea(wmSoEntity.getDef17());
            wmSoPack.setBusinessNo(wmSoEntity.getDef1());
            wmSoPack.setChainNo(wmSoEntity.getDef2());
            wmSoPack.setTaskNo(wmSoEntity.getDef3());
            wmSoPack.setCustomerOrderNo(wmSoEntity.getDef5());
            wmSoPack.setExternalNo(wmSoEntity.getDef16());
            wmSoPack.setAllocId(soAlloc.getAllocId());
            wmSoPack.setWaveNo(soAlloc.getWaveNo());
            wmSoPack.setSkuCode(soAlloc.getSkuCode());
            wmSoPack.setSkuName(skuName);
            wmSoPack.setLocCode(soAlloc.getLocCode());
            wmSoPack.setTraceId(soAlloc.getTraceId());
            wmSoPack.setQty(soAlloc.getQtyEa());
            wmSoPack.setToLoc(soAlloc.getToLoc());
            wmSoPack.setToId(soAlloc.getToId());
            wmSoPack.setPickOp(soAlloc.getPickOp());
            wmSoPack.setPickTime(soAlloc.getPickTime());
            wmSoPack.setCheckOp(soAlloc.getCheckOp());
            wmSoPack.setCheckTime(soAlloc.getCheckTime());
            wmSoPack.setPackOp(soAlloc.getPackOp());
            wmSoPack.setPackTime(soAlloc.getPackTime());
            wmSoPack.setTrackingNo(soAlloc.getTrackingNo());
            wmSoPack.setOrgId(soAlloc.getOrgId());
            wmSoPack.setCaseNo(soAlloc.getCaseNo());
            wmSoPack.setCarrierCode(wmSoEntity.getCarrierCode());
            wmSoPack.setCarrierName(wmSoEntity.getCarrierName());
            wmSoPack.setScanCaseNo(scanCaseNo);
            wmSoPackService.save(wmSoPack);
        }
    }

    @Transactional
    public ResultMessage packAndPrint(List<BanQinWmSoAllocEntity> allocItems, String toId, List<BanQinWmSoSerialEntity> soSerialList, String isCheck, BanQinWmSoEntity wmSoEntity) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.isBlank(wmSoEntity.getCarrierCode())) {
            throw new WarehouseException("承运商未维护！");
        }
        BanQinWmCarrierTypeRelationEntity carrierType = banQinWmCarrierTypeRelationService.getByCarrierCode(wmSoEntity.getCarrierCode(), wmSoEntity.getOrgId());
        if (carrierType == null) {
            throw new WarehouseException("承运商类型关系未维护！");
        }
        // 统计数量用于快递100接口
        wmSoEntity.setQtyTotal(allocItems.stream().map(BanQinWmSoAlloc::getQtyEa).reduce(0d, BigDecimalUtil::add));
        // 打包确认
        List<BanQinWmSoAlloc> soAllocList = this.updateTraceId(allocItems, toId, soSerialList, null, isCheck, wmSoEntity);
        // 调用接口
        ResultMessage mailInfo = getMailInfo(wmSoEntity, toId, carrierType);
        if (!mailInfo.isSuccess()) {
            throw new WarehouseException(mailInfo.getMessage());
        }

        String mailNo;
        String consigneeAddress;
        // 构建打印模板
        List<String> printList;
        wmSoEntity.setOrderSeq("1/1");
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        String responseString = null;
        String reportName = getReportName(carrierType.getMailType());
        switch (carrierType.getType()) {
            case WmsConstants.EXPRESS_TYPE_HTKY:
                KdWaybillApplyNotifyRsp response = (KdWaybillApplyNotifyRsp) mailInfo.getData();
                mailNo = getMailNo(response);
                consigneeAddress = getConsigneeAddress(response);
                printList = getPrintListBest(response, wmSoEntity, reportName);
                responseString = Parser.coverObject2Xml(response);
                break;
            case WmsConstants.EXPRESS_TYPE_SFEXPRESS:
                SfCreateOrderResponse sfResponse = (SfCreateOrderResponse) mailInfo.getData();
                mailNo = getMailNoSf(sfResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                printList = getPrintListSf(sfResponse, wmSoEntity, allocItems.stream().mapToDouble(BanQinWmSoAllocEntity::getQtyPackEa).sum(), reportName);
                responseString = JSONObject.toJSONString(sfResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_ZT:
                double sumQty = soAllocList.stream().mapToDouble(BanQinWmSoAlloc::getQtyEa).sum();
                mailNo = toId;
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                printList = getPrintListSelf(sumQty, wmSoEntity, mailNo, reportName);
                break;
            case WmsConstants.EXPRESS_TYPE_ZTO:
                Map<String, String> map = (Map<String, String>) mailInfo.getData();
                mailNo = map.get("mailNo");
                consigneeAddress = wmSoEntity.getDef17().replaceAll(":", "") + wmSoEntity.getContactAddr();
                printList = getPrintListZto(map, wmSoEntity, reportName);
                responseString = JSONObject.toJSONString(map);
                break;
            case WmsConstants.EXPRESS_TYPE_YUNDA:
                YundaCreateOrderResponse yundaResponse = (YundaCreateOrderResponse) mailInfo.getData();
                mailNo = getMailNoYunda(yundaResponse);
                consigneeAddress = getConsigneeAddressYunda(yundaResponse);
                printList = getPrintListYunda(yundaResponse, wmSoEntity, reportName);
                responseString = YundaUtils.responseCover2Xml(yundaResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_STO:
                StoOrderCreateResponse stoResponse = (StoOrderCreateResponse) mailInfo.getData();
                mailNo = getMailNoSto(stoResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                printList = getPrintListSto(stoResponse, wmSoEntity, reportName);
                responseString = JSON.toJSONString(stoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_YTO:
                YTOCreateOrderResponseData ytoResponse = (YTOCreateOrderResponseData) mailInfo.getData();
                mailNo = getMailNoYto(ytoResponse);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                printList = getPrintListYto(ytoResponse, wmSoEntity, reportName);
                responseString = YTOUtils.responseCover2Xml(ytoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_KD100:
                PrintBaseResp<PrintImgData> kd100Response = (PrintBaseResp<PrintImgData>) mailInfo.getData();
                mailNo = getMailNoKd100(kd100Response);
                consigneeAddress = receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr();
                printList = getPrintListKd100(kd100Response);
                responseString = kd100Response.getData().getTaskId();// 快递100的任务ID，用于复打面单
                break;
            default:
                throw new WarehouseException("承运商类型关系未维护！");
        }

        for (BanQinWmSoAlloc alloc : soAllocList) {
            // 更新面单号和收货地址到分配明细
            wmSoAllocService.updatePackInfo(alloc.getId(), mailNo, consigneeAddress, alloc.getCaseNo());
            // 更新面单号到打包记录
            wmSoPackService.updateTrackingNo(alloc.getAllocId(), mailNo, alloc.getOrgId());
            // 更新面单记录
            wmGetWayBillLogService.saveInfo(wmSoEntity.getWaveNo(), wmSoEntity.getSoNo(), mailNo, toId, carrierType.getType(), responseString, wmSoEntity.getOrgId(), WmsConstants.ONE, wmSoEntity.getOrderSeq());
        }
        msg.setData(printList);
        return msg;
    }

    public ResultMessage getMailInfo(BanQinWmSoEntity wmSoEntity, String toId, BanQinWmCarrierTypeRelationEntity carrierType) {
        ResultMessage msg = new ResultMessage();
        ResultMessage resultMessage = getBillNoCheck(wmSoEntity);
        if (!resultMessage.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(resultMessage.getMessage());
            return msg;
        }
        switch (carrierType.getType()) {
            case WmsConstants.EXPRESS_TYPE_HTKY:
                KdWaybillApplyNotifyRsp response = getWayBillInfo(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == response) {
                    msg.setSuccess(false);
                    msg.addMessage("百世获取面单号接口连接异常，请联系百世客服解决！");
                    return msg;
                }
                if (!response.getResult()) {
                    msg.setSuccess(false);
                    msg.addMessage("百世系统异常：" + response.getErrorDescription());
                    return msg;
                }
                msg.setData(response);
                break;
            case WmsConstants.EXPRESS_TYPE_SFEXPRESS:
                SfCreateOrderResponse sfCreateOrderResponse = getWayBillInfoSF(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == sfCreateOrderResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("顺丰下单接口连接异常！");
                    return msg;
                }
                if (sfCreateOrderResponse.getHttpStatus() != 200) {
                    msg.setSuccess(false);
                    msg.addMessage("顺丰系统异常：" + sfCreateOrderResponse.getMessage());
                    return msg;
                }
                msg.setData(sfCreateOrderResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_ZTO:
                ZtoGetOrderNoResponse ztoGetOrderNoResponse = getWayBillInfoZto(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == ztoGetOrderNoResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("中通获取面单接口连接异常！");
                    return msg;
                }
                if (!ztoGetOrderNoResponse.isResult()) {
                    msg.setSuccess(false);
                    msg.addMessage("中通接口异常:" + ztoGetOrderNoResponse.getMessage() + (null != ztoGetOrderNoResponse.getData() ? ztoGetOrderNoResponse.getData().getMessage() : ""));
                    return msg;
                }
                // 调用获取集装地大头笔接口
                String mailNo = ztoGetOrderNoResponse.getData().getBillCode();
                ZtoGetMarkResponse ztoGetMarkResponse = getWayBillInfoZtoMark(wmSoEntity, mailNo, carrierType.getUrl(), carrierType.getParams());
                if (null == ztoGetMarkResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("中通获取大头笔接口连接异常！");
                    return msg;
                }
                if (!ztoGetMarkResponse.isStatus()) {
                    msg.setSuccess(false);
                    msg.addMessage("中通接口异常:" + ztoGetMarkResponse.getMessage());
                    return msg;
                }
                Map<String, String> map = new HashMap<>();
                map.put("mailNo", mailNo);
                map.put("bagAddr", ztoGetMarkResponse.getResult().getBagAddr());
                map.put("mark", ztoGetMarkResponse.getResult().getMark());
                msg.setData(map);
                break;
            case WmsConstants.EXPRESS_TYPE_YUNDA:
                YundaCreateOrderResponse yundaCreateOrderResponse = getWayBillInfoYunda(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == yundaCreateOrderResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("韵达下单接口连接异常！");
                    return msg;
                }
                if ("0".equals(yundaCreateOrderResponse.getResponse().get(0).getStatus())) {
                    msg.setSuccess(false);
                    msg.addMessage("韵达系统异常：" + yundaCreateOrderResponse.getResponse().get(0).getMsg());
                    return msg;
                }
                msg.setData(yundaCreateOrderResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_STO:
                StoOrderCreateResponse stoResponse = getWayBillInfoSto(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == stoResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("申通下单接口连接异常！");
                    return msg;
                }
                msg.setData(stoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_YTO:
                YTOCreateOrderResponseData ytoResponse = getWayBillInfoYto(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == ytoResponse) {
                    msg.setSuccess(false);
                    msg.addMessage("圆通下单接口连接异常！");
                    return msg;
                }
                if (!"200".equals(ytoResponse.getCode())) {
                    msg.setSuccess(false);
                    msg.addMessage("圆通系统异常：" + ytoResponse.getReason());
                    return msg;
                }
                msg.setData(ytoResponse);
                break;
            case WmsConstants.EXPRESS_TYPE_KD100:
                PrintBaseResp<PrintImgData> kd100Response = getWayBillInfoKd100(wmSoEntity, toId, carrierType.getUrl(), carrierType.getParams());
                if (null == kd100Response) {
                    msg.setSuccess(false);
                    msg.addMessage("快递100下单接口连接异常！");
                    return msg;
                }
                if (!"200".equals(kd100Response.getReturnCode())) {
                    msg.setSuccess(false);
                    msg.addMessage("快递100系统异常：" + kd100Response.getMessage());
                    return msg;
                }
                msg.setData(kd100Response);
                break;
        }
        return msg;
    }

    private ResultMessage getBillNoCheck(BanQinWmSoEntity entity) {
        ResultMessage msg = new ResultMessage();
        if (null == entity) {
            msg.setSuccess(false);
            msg.addMessage("so单数据已过期！");
            return msg;
        }
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(entity.getOwnerShortName())) {
            msg.setSuccess(false);
            builder.append("发货人不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getOwnerTel())) {
            msg.setSuccess(false);
            builder.append("发货人电话不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getOwnerAddress())) {
            msg.setSuccess(false);
            builder.append("发货人地址不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getOwnerPostCode())) {
            msg.setSuccess(false);
            builder.append("发货人邮编不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getOwnerArea())) {
            msg.setSuccess(false);
            builder.append("发货人区域不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getContactName())) {
            msg.setSuccess(false);
            builder.append("收货人不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getContactTel())) {
            msg.setSuccess(false);
            builder.append("收货人电话不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getContactAddr())) {
            msg.setSuccess(false);
            builder.append("收货人地址不能为空！<br>");
        }
        if (StringUtils.isBlank(entity.getDef17())) {
            msg.setSuccess(false);
            builder.append("收货人区域不能为空！<br>");
        }
        if (StringUtils.isNotBlank(entity.getOwnerArea())) {
            String[] ownerArea = entity.getOwnerArea().split(":", -1);
            if (ownerArea.length < 3) {
                msg.setSuccess(false);
                builder.append("发货人区域格式维护错误！<br>");
            }
        }
        if (StringUtils.isNotBlank(entity.getDef17())) {
            String[] consigneeArea = entity.getDef17().split(":", -1);
            if (consigneeArea.length < 3) {
                msg.setSuccess(false);
                builder.append("收货人区域格式维护错误！<br>");
            }
        }

        msg.setMessage(builder.toString());
        return msg;
    }

    private KdWaybillApplyNotifyRsp getWayBillInfo(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        KdWaybillApplyNotifyRequestEntity entity = new KdWaybillApplyNotifyRequestEntity();
        entity.setSendMan(wmSoEntity.getOwnerShortName());
        entity.setSendManPhone(wmSoEntity.getOwnerTel());
        entity.setSendManAddress(wmSoEntity.getOwnerAddress());
        entity.setSendPostcode(wmSoEntity.getOwnerPostCode());
        String[] sendArea = wmSoEntity.getOwnerArea().split(":", -1);
        entity.setSendProvince(sendArea[0]);
        entity.setSendCity(sendArea[1]);
        entity.setSendCounty(sendArea[2]);
        entity.setReceiveMan(wmSoEntity.getContactName());
        entity.setReceiveManPhone(wmSoEntity.getContactTel());
        entity.setReceivePostcode(wmSoEntity.getContactZip());
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        entity.setReceiveProvince(receiveArea[0]);
        entity.setReceiveCity(receiveArea[1]);
        entity.setReceiveCounty(receiveArea[2]);
        entity.setReceiveManAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());
        entity.setMsgId(toId);
        return kdBestInterfaceService.kdWaybillApplyNotify(entity, url, params);
    }

    public List<String> getPrintListBest(KdWaybillApplyNotifyRsp response, BanQinWmSoEntity wmSoEntity, String reportName) {
        List<KdWaybillApplyNotifyPrintEntity> printList = Lists.newArrayList();
        if (response != null) {
            List<EDIPrintDetailList> ediPrintDetailList = response.getEDIPrintDetailList();
            if (CollectionUtil.isNotEmpty(ediPrintDetailList)) {
                for (EDIPrintDetailList printDetail : ediPrintDetailList) {
                    KdWaybillApplyNotifyPrintEntity printEntity = new KdWaybillApplyNotifyPrintEntity();
                    BeanUtils.copyProperties(printDetail, printEntity);
                    printEntity.setRemarks(printDetail.getRemark());
                    printEntity.setLabel(wmSoEntity.getOrderSeq());
                    printEntity.setRemark("SO单号：" + wmSoEntity.getSoNo());
                    printEntity.setRemark1("订单号：" + wmSoEntity.getDef16());
                    printEntity.setRemark2((StringUtils.isEmpty(wmSoEntity.getOwnerRemarks()) ? "" : wmSoEntity.getOwnerRemarks()) + (StringUtils.isEmpty(wmSoEntity.getRemarks()) ? "" : wmSoEntity.getRemarks()));
                    printList.add(printEntity);
                }
            }
        }

        return CollectionUtil.isNotEmpty(printList) ? backstagePrintService.getImageList(reportName, new JRBeanCollectionDataSource(printList)) : null;
    }

    public List<String> getPrintListSelf(Double qty, BanQinWmSoEntity wmSoEntity, String mailNo, String reportName) {
        List<SelfExpressBillPrintEntity> printList = Lists.newArrayList();
        SelfExpressBillPrintEntity printEntity = new SelfExpressBillPrintEntity();
        printEntity.setMailNo(mailNo);
        printEntity.setReceiveMan(wmSoEntity.getContactName());
        printEntity.setReceiveManPhone(wmSoEntity.getContactTel());
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        printEntity.setReceiveManAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());
        printEntity.setSendMan(wmSoEntity.getOwnerShortName());
        printEntity.setSendManPhone(wmSoEntity.getOwnerTel());
        String[] sendArea = wmSoEntity.getOwnerArea().split(":", -1);
        printEntity.setSendManAddress(sendArea[0] + sendArea[1] + sendArea[2]);
        printEntity.setTotalQty(qty);
        printEntity.setOrderNo1(wmSoEntity.getSoNo());
        printEntity.setOrderNo2(wmSoEntity.getDef16());
        printEntity.setCustomerOrderNo(wmSoEntity.getDef5());
        printList.add(printEntity);
        if (CollectionUtil.isNotEmpty(printList)) {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(printList);
            return backstagePrintService.getImageList(reportName, jrDataSource);
        } else {
            return null;
        }
    }

    public List<String> getPrintListZto(Map<String, String> map, BanQinWmSoEntity wmSoEntity, String reportName) {
        List<ZtoPrintEntity> printList = Lists.newArrayList();
        ZtoPrintEntity printEntity = new ZtoPrintEntity();
        printEntity.setBagAddr(map.get("bagAddr"));
        printEntity.setMark(map.get("mark"));
        printEntity.setMailNo(map.get("mailNo"));
        printEntity.setConsignee(wmSoEntity.getContactName());
        printEntity.setConsigneeTel(wmSoEntity.getContactTel());
        printEntity.setConsigneeAddr(wmSoEntity.getDef17().replaceAll(":", "") + wmSoEntity.getContactAddr());
        printEntity.setSender(wmSoEntity.getOwnerShortName());
        printEntity.setSenderTel(wmSoEntity.getOwnerTel());
        printEntity.setSenderAddr(wmSoEntity.getOwnerArea().replaceAll(":", "") + wmSoEntity.getOwnerAddress());
        printEntity.setSoNo(wmSoEntity.getSoNo());
        printEntity.setCustomerNo(wmSoEntity.getDef5());
        printEntity.setLabel(wmSoEntity.getOrderSeq());
        printList.add(printEntity);

        return backstagePrintService.getImageList(reportName, new JRBeanCollectionDataSource(printList));
    }

    public String getMailNo(KdWaybillApplyNotifyRsp response) {
        return null != response ? response.getEDIPrintDetailList().get(0).getMailNo() : null;
    }

    public String getMailNoSf(SfCreateOrderResponse response) {
        return null != response ? response.getData().getRlsInfo().get(0).getRlsDetail().get(0).getWaybillNumber() : null;
    }

    public String getMailNoYunda(YundaCreateOrderResponse response) {
        return null != response ? response.getResponse().get(0).getMail_no() : null;
    }

    public String getMailNoKd100(PrintBaseResp<PrintImgData> response) {
        return null != response ? response.getData().getKuaidinum() : null;
    }

    public String getMailNoSto(StoOrderCreateResponse response) {
        return response != null ? response.getWaybillNo() : null;
    }

    public String getMailNoYto(YTOCreateOrderResponseData response) {
        return response != null ? response.getMailNo() : null;
    }

    private String getConsigneeAddress(KdWaybillApplyNotifyRsp response) {
        return null != response ? response.getEDIPrintDetailList().get(0).getReceiveManAddress() : null;
    }

    public String getConsigneeAddressYunda(YundaCreateOrderResponse response) {
        if (null != response) {
            String pdfInfo = UnicodeUtils.unicodeToString(response.getResponse().get(0).getPdf_info());
            pdfInfo = pdfInfo.substring(pdfInfo.indexOf("{"), pdfInfo.indexOf("}") + 1);
            YundaPrintEntity printEntity = JSONObject.parseObject(pdfInfo, YundaPrintEntity.class);
            return printEntity.getReceiver_address();
        } else {
            return null;
        }
    }

    private SfCreateOrderResponse getWayBillInfoSF(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        SfCreateOrderRequest entity = new SfCreateOrderRequest();

        entity.setSenderName(wmSoEntity.getOwnerShortName());
        entity.setSenderCompany(wmSoEntity.getOwnerShortName());
        entity.setSenderTel(wmSoEntity.getOwnerTel());
        entity.setSenderAddress(wmSoEntity.getOwnerAddress());
        entity.setReceiverName(wmSoEntity.getContactName());
        entity.setReceiverTel(wmSoEntity.getContactTel());
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        entity.setReceiverAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());
        entity.setRequestId(toId);

        entity.setPayMethod("1");
        entity.setExpressType("2");     // 顺丰产品类别，由顺丰确认（标快，特惠，重货等）
        entity.setCargo(new ArrayList<Cargo>() {{
            add(new Cargo("普货"));
        }});
        entity.setAddedService(Lists.newArrayList());
        entity.setExtra(Lists.newArrayList());

        return sfExpressInterfaceService.sfCreateOrder(entity, url, params);
    }

    public List<String> getPrintListSf(SfCreateOrderResponse response, BanQinWmSoEntity wmSoEntity, double sumPackEa, String reportName) {
        List<SfExpressBillPrintEntity> printList = Lists.newArrayList();
        if (response != null) {
            SfCreateOrderResponseData data = response.getData();
            if (data != null) {
                List<RlsInfo> rlsInfos = data.getRlsInfo();
                if (CollectionUtil.isNotEmpty(rlsInfos)) {
                    // 暂时不考虑字母单业务
                    RlsInfo rlsInfo = rlsInfos.get(0);
                    if ("OK".equals(rlsInfo.getInvokeResult())) {
                        List<RlsDetail> rlsDetails = rlsInfo.getRlsDetail();
                        if (CollectionUtil.isNotEmpty(rlsDetails)) {
                            RlsDetail rlsDetail = rlsDetails.get(0);
                            SfExpressBillPrintEntity printEntity = new SfExpressBillPrintEntity();
                            printEntity.setPrintNum("1");
                            printEntity.setProCode(rlsDetail.getLimitTypeCode());
                            printEntity.setMailNo(rlsDetail.getWaybillNumber());
                            printEntity.setDestRouteLabel(rlsDetail.getDestRouteLabel() == null ? rlsDetail.getDestCityCode() : rlsDetail.getDestRouteLabel());
                            printEntity.setReceiveMan(wmSoEntity.getContactName());
                            if (PhoneUtil.isCellPhone(wmSoEntity.getContactTel())) {
                                String pre = wmSoEntity.getContactTel().substring(0, 3);
                                String end = wmSoEntity.getContactTel().substring(wmSoEntity.getContactTel().length() - 4);
                                printEntity.setReceiveManPhone(pre + "****" + end);
                            } else {
                                if (wmSoEntity.getContactTel().length() >= 4) {
                                    String end = wmSoEntity.getContactTel().substring(wmSoEntity.getContactTel().length() - 4);
                                    printEntity.setReceiveManPhone("****" + end);
                                } else {
                                    printEntity.setReceiveManPhone("****" + wmSoEntity.getContactTel());
                                }

                            }
                            String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
                            printEntity.setReceiveManAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());
                            printEntity.setDestTeamCode(rlsDetail.getDestTeamCode());
                            printEntity.setCodFlag("0");
                            printEntity.setPayWay("寄付月结");
                            printEntity.setCodingMapping(rlsDetail.getCodingMapping());
                            if (StringUtils.isBlank(rlsDetail.getTwoDimensionCode())) {
                                printEntity.setTwoDimensionCode(getTwoDimensionCode(rlsDetail.getDestCityCode(), rlsDetail.getLimitTypeCode(), rlsDetail.getWaybillNumber(), StringUtils.isBlank(rlsDetail.getAbFlag()) ? "" : rlsDetail.getAbFlag()));
                            } else {
                                printEntity.setTwoDimensionCode(rlsDetail.getTwoDimensionCode());
                            }
                            printEntity.setAbFlag(rlsDetail.getAbFlag());
                            printEntity.setCodingMappingOut(rlsDetail.getCodingMappingOut());
                            printEntity.setSendMan(wmSoEntity.getOwnerShortName());
                            if (PhoneUtil.isCellPhone(wmSoEntity.getOwnerTel())) {
                                String pre = wmSoEntity.getOwnerTel().substring(0, 3);
                                String end = wmSoEntity.getOwnerTel().substring(wmSoEntity.getOwnerTel().length() - 4);
                                printEntity.setSendManPhone(pre + "****" + end);
                            } else {
                                if (wmSoEntity.getOwnerTel().length() >= 4) {
                                    String end = wmSoEntity.getOwnerTel().substring(wmSoEntity.getOwnerTel().length() - 4);
                                    printEntity.setSendManPhone("****" + end);
                                } else {
                                    printEntity.setSendManPhone("****" + wmSoEntity.getOwnerTel());
                                }

                            }
                            printEntity.setSendManCompany(wmSoEntity.getOwnerShortName());
                            printEntity.setSendManAddress(wmSoEntity.getOwnerAddress());
                            printEntity.setCargo("普货");
                            printEntity.setCargoQty(sumPackEa + "");
                            printEntity.setCustomerOrderNo(wmSoEntity.getDef16());      // 外部单号
                            printEntity.setLabel(wmSoEntity.getOrderSeq());

                            printList.add(printEntity);
                        }
                    }
                }
            }
        }
        if (CollectionUtil.isNotEmpty(printList)) {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(printList);
            return backstagePrintService.getImageList("sfExpress/" + reportName, jrDataSource);
        } else {
            return null;
        }
    }

    private String getTwoDimensionCode(String destCityCode, String limitTypeCode, String waybillNumber, String abFlag) {
        String twoDimensionCode = "M={'k1':'','k2':'" + destCityCode + "','k3':'','k4':'" + limitTypeCode + "','k5':'" + waybillNumber + "','k6':'" + abFlag + "'}";
        twoDimensionCode = StringUtils.leftPad(twoDimensionCode, 128, "M");
        return twoDimensionCode;
    }

    private ZtoGetOrderNoResponse getWayBillInfoZto(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        ZtoOrderNoRequest request = new ZtoOrderNoRequest();
        request.setId(toId);
        request.setTypeid("1");
        // 发件人
        ZtoOrderNoSenderRequest senderRequest = new ZtoOrderNoSenderRequest();
        senderRequest.setName(wmSoEntity.getOwnerShortName());
        senderRequest.setMobile(wmSoEntity.getOwnerTel());
        senderRequest.setAddress(wmSoEntity.getOwnerAddress());
        senderRequest.setCity(wmSoEntity.getOwnerArea().replaceAll(":", ","));
        request.setSender(senderRequest);
        // 收货人
        ZtoOrderNoReceiverRequest receiverRequest = new ZtoOrderNoReceiverRequest();
        receiverRequest.setName(wmSoEntity.getContactName());
        receiverRequest.setMobile(wmSoEntity.getContactTel());
        receiverRequest.setAddress(wmSoEntity.getContactAddr());
        receiverRequest.setCity(wmSoEntity.getDef17().replaceAll(":", ","));
        request.setReceiver(receiverRequest);

        return ztoInterfaceService.getMailInfo(request, url, params);
    }

    private ZtoGetMarkResponse getWayBillInfoZtoMark(BanQinWmSoEntity wmSoEntity, String mailNo, String url, String params) {
        ZtoGetMarkRequest request = new ZtoGetMarkRequest();
        request.setUnionCode(mailNo);

        String[] shipArea = wmSoEntity.getOwnerArea().split(":", -1);
        request.setSend_province(shipArea[0]);
        request.setSend_city(shipArea[1]);
        request.setSend_district(shipArea[2]);

        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        request.setReceive_province(receiveArea[0]);
        request.setReceive_city(receiveArea[1]);
        request.setReceive_district(receiveArea[2]);
        request.setReceive_address(wmSoEntity.getContactAddr());

        return ztoInterfaceService.getMark(request, url, params);
    }

    private YundaCreateOrderResponse getWayBillInfoYunda(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        YundaCreateOrderRequest entity = new YundaCreateOrderRequest();
        entity.setOrder_serial_no(toId);
        entity.setKhddh(toId);
        entity.setNbckh(toId);
        entity.setOrder_type("common");
        YundaCreateOrderCustomerRequest sender = new YundaCreateOrderCustomerRequest();
        sender.setName(wmSoEntity.getOwnerShortName());
        sender.setCompany(wmSoEntity.getOwnerShortName());
        sender.setCity(wmSoEntity.getOwnerArea().replaceAll(":", ","));
        String[] sendArea = wmSoEntity.getOwnerArea().split(":", -1);
        sender.setAddress(sendArea[0] + sendArea[1] + sendArea[2] + wmSoEntity.getOwnerAddress());
        sender.setMobile(wmSoEntity.getOwnerTel());
        entity.setSender(sender);
        YundaCreateOrderCustomerRequest receiver = new YundaCreateOrderCustomerRequest();
        receiver.setName(wmSoEntity.getContactName());
        receiver.setCity(wmSoEntity.getDef17().replaceAll(":", ","));
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        receiver.setAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());
        receiver.setMobile(wmSoEntity.getContactTel());
        entity.setReceiver(receiver);
        entity.setCus_area1("SO单号：" + wmSoEntity.getSoNo() + "\\n 订单号：" + wmSoEntity.getDef16());
        List<YundaCreateOrderRequest> entityList = Lists.newArrayList();
        entityList.add(entity);
        return yundaInterfaceService.createOrder(entityList, url, params);
    }

    public StoOrderCreateResponse getWayBillInfoSto(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        String[] shipArea = wmSoEntity.getOwnerArea().split(":", -1);
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);

        SenderDO sender = new SenderDO();
        sender.setName(wmSoEntity.getOwnerShortName());
        sender.setTel(wmSoEntity.getOwnerTel());
        sender.setProvince(shipArea[0]);
        sender.setCity(shipArea[1]);
        sender.setArea(shipArea[2]);
        sender.setAddress(wmSoEntity.getOwnerAddress());

        ReceiverDO receiver = new ReceiverDO();
        receiver.setName(wmSoEntity.getContactName());
        receiver.setTel(wmSoEntity.getContactTel());
        receiver.setProvince(receiveArea[0]);
        receiver.setCity(receiveArea[1]);
        receiver.setArea(receiveArea[2]);
        receiver.setAddress(wmSoEntity.getContactAddr());

        CargoDO cargo = new CargoDO();
        cargo.setBattery("10");
        cargo.setGoodsType("大件");
        cargo.setGoodsName("普货");

        CustomerDO customer = new CustomerDO();
        customer.setCustomerName("666666000001");
        customer.setSiteCode("666666");
        customer.setSitePwd("abc123");

        StoOrderCreateRequest request = new StoOrderCreateRequest();
        request.setOrderNo(toId);
        request.setOrderSource("BQSTO");
        request.setBillType("00");
        request.setOrderType("01");
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setCargo(cargo);
        request.setCustomer(customer);
        return stoInterfaceService.orderCreate(request, url, params);
    }

    private YTOCreateOrderResponseData getWayBillInfoYto(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        YTOCreateOrderRequest entity = new YTOCreateOrderRequest();
        entity.setLogisticProviderID("YTO");
        entity.setTxLogisticID(toId);
        entity.setOrderType(1);             // 订单类型 - 普通订单
        entity.setServiceType(1L);          // 服务类型 - 上门揽收
        YTOCreateOrderSenderRequest senderRequest = new YTOCreateOrderSenderRequest();
        senderRequest.setName(wmSoEntity.getOwnerShortName());
        senderRequest.setMobile(wmSoEntity.getOwnerTel());
        String[] sendArea = wmSoEntity.getOwnerArea().split(":", -1);
        senderRequest.setProv(sendArea[0]);
        senderRequest.setCity(sendArea[1] + "," + sendArea[2]);
        senderRequest.setAddress(wmSoEntity.getOwnerAddress());
        entity.setSender(senderRequest);
        YTOCreateOrderReceiverRequest receiverRequest = new YTOCreateOrderReceiverRequest();
        receiverRequest.setName(wmSoEntity.getContactName());
        receiverRequest.setMobile(wmSoEntity.getContactTel());
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
        receiverRequest.setProv(receiveArea[0]);
        receiverRequest.setCity(receiveArea[1] + "," + receiveArea[2]);
        receiverRequest.setAddress(wmSoEntity.getContactAddr());
        entity.setReceiver(receiverRequest);
        entity.setItemName("普货");
        entity.setNumber(12);

        return ytoInterfaceService.createOrder(entity, url, params);
    }

    private PrintBaseResp<PrintImgData> getWayBillInfoKd100(BanQinWmSoEntity wmSoEntity, String toId, String url, String params) {
        String[] shipArea = wmSoEntity.getOwnerArea().split(":", -1);
        String[] receiveArea = wmSoEntity.getDef17().split(":", -1);

        PrintImgParam printImgParam = new PrintImgParam();
        printImgParam.setKuaidicom(wmSoEntity.getCarrierCode());
        printImgParam.setOrderId(toId);
        printImgParam.setType("10");
        printImgParam.setWidth("76");
        printImgParam.setHeight("130");

        printImgParam.setSendManName(wmSoEntity.getOwnerShortName());
        printImgParam.setSendManMobile(wmSoEntity.getOwnerTel());
        printImgParam.setSendManPrintAddr(shipArea[0] + shipArea[1] + shipArea[2] + wmSoEntity.getOwnerAddress());

        printImgParam.setRecManName(wmSoEntity.getContactName());
        printImgParam.setRecManMobile(wmSoEntity.getContactTel());
        printImgParam.setRecManPrintAddr(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());

        printImgParam.setCount(BigDecimal.valueOf(wmSoEntity.getQtyTotal()).stripTrailingZeros().toPlainString());

        String remarks = (StringUtils.isNotBlank(wmSoEntity.getDef5()) ? ("订单号:" + wmSoEntity.getDef5() + "\\n") : "")
                + (StringUtils.isNotBlank(wmSoEntity.getSoNo()) ? ("SO单号:" + wmSoEntity.getSoNo() + "\\n") : "")
                + (StringUtils.isNotBlank(wmSoEntity.getRemarks()) ? wmSoEntity.getRemarks() : "");
        printImgParam.setRemark(remarks);

        return kd100InterfaceService.createOrder(printImgParam, url, params);
    }

    public List<String> getPrintListYunda(YundaCreateOrderResponse response, BanQinWmSoEntity wmSoEntity, String reportName) {
        List<YundaPrintEntity> printList = Lists.newArrayList();
        if (response != null) {
            List<YundaCreateOrderResponseData> responseDataList = response.getResponse();
            if (CollectionUtil.isNotEmpty(responseDataList)) {
                YundaCreateOrderResponseData responseData = responseDataList.get(0);
                if ("1".equals(responseData.getStatus())) {
                    String pdfInfo = UnicodeUtils.unicodeToString(responseData.getPdf_info());
                    pdfInfo = pdfInfo.substring(pdfInfo.indexOf("{"), pdfInfo.indexOf("}") + 1);
                    YundaPrintEntity printEntity = JSONObject.parseObject(pdfInfo, YundaPrintEntity.class);
                    printEntity.setCus_area1("SO单号：" + wmSoEntity.getSoNo());
                    printEntity.setCus_area2("订单号：" + wmSoEntity.getDef16());
                    printEntity.setPrintNum("1");
                    printEntity.setLabel(wmSoEntity.getOrderSeq());
                    printList.add(printEntity);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(printList)) {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(printList);
            return backstagePrintService.getImageList(reportName, jrDataSource);
        } else {
            return null;
        }
    }

    public List<String> getPrintListSto(StoOrderCreateResponse response, BanQinWmSoEntity wmSoEntity, String reportName) {
        List<StoPrintEntity> printList = Lists.newArrayList();
        StoPrintEntity printEntity = new StoPrintEntity();
        printEntity.setMailno(response.getWaybillNo());
        printEntity.setBigWord(response.getBigWord());
        printEntity.setPackagePlace(response.getPackagePlace());
        printEntity.setReceiverName(wmSoEntity.getContactName());
        printEntity.setReceiverMobile(wmSoEntity.getContactTel());
        printEntity.setReceiverAddress(wmSoEntity.getDef17().replaceAll(":", "") + wmSoEntity.getContactAddr());
        printEntity.setSenderName(wmSoEntity.getOwnerShortName());
        printEntity.setSenderMobile(wmSoEntity.getOwnerTel());
        printEntity.setSenderAddress(wmSoEntity.getOwnerArea().replaceAll(":", "") + wmSoEntity.getOwnerAddress());
        printEntity.setCusArea1("SO单号：" + wmSoEntity.getSoNo());
        printEntity.setCusArea2("订单号：" + wmSoEntity.getDef16());
        printEntity.setLabel(wmSoEntity.getOrderSeq());
        printList.add(printEntity);
        if (CollectionUtil.isNotEmpty(printList)) {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(printList);
            return backstagePrintService.getImageList(reportName, jrDataSource);
        } else {
            return null;
        }
    }

    public List<String> getPrintListYto(YTOCreateOrderResponseData responseData, BanQinWmSoEntity wmSoEntity, String reportName) {
        List<YtoPrintEntity> printList = Lists.newArrayList();
        if (responseData != null) {
            if ("200".equals(responseData.getCode())) {
                YtoPrintEntity printEntity = new YtoPrintEntity();
                printEntity.setMailno(responseData.getMailNo());
                printEntity.setOriginateOrgCode(responseData.getOriginateOrgCode());
                printEntity.setShortAddress(responseData.getDistributeInfo().getShortAddress());
                if (responseData.getDistributeInfo() != null) {
                    printEntity.setPackageCenterCode(responseData.getDistributeInfo().getPackageCenterCode());
                    printEntity.setPackageCenterName(responseData.getDistributeInfo().getPackageCenterName());
                }
                printEntity.setReceiverName(wmSoEntity.getContactName());
                printEntity.setReceiverMobile(wmSoEntity.getContactTel());
                printEntity.setReceiverAddress(wmSoEntity.getDef17().replaceAll(":", "") + wmSoEntity.getContactAddr());
                printEntity.setSenderName(wmSoEntity.getOwnerShortName());
                printEntity.setSenderMobile(wmSoEntity.getOwnerTel());
                printEntity.setSenderAddress(wmSoEntity.getOwnerArea().replaceAll(":", "") + wmSoEntity.getOwnerAddress());
                printEntity.setQrCode(responseData.getQrCode());
                printEntity.setCusArea1("SO单号：" + wmSoEntity.getSoNo());
                printEntity.setCusArea2("订单号：" + wmSoEntity.getDef16());
                printEntity.setLabel(wmSoEntity.getOrderSeq());
                printList.add(printEntity);
            }
        }
        if (CollectionUtil.isNotEmpty(printList)) {
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(printList);
            return backstagePrintService.getImageList(reportName, jrDataSource);
        } else {
            return null;
        }
    }

    public List<String> getPrintListKd100(PrintBaseResp<PrintImgData> responseData) {
        if (responseData != null && "200".equals(responseData.getReturnCode())) {
            return kd100InterfaceService.getBase64Img(responseData.getData().getImgBase64());
        } else {
            return null;
        }
    }

    @Transactional
    public void cancelPack(String allocId, boolean isCancelCheck, String orgId) {
        BanQinWmSoAlloc wmSoAlloc = wmSoAllocService.getByAllocId(allocId, orgId);
        if (wmSoAlloc == null)
            throw new WarehouseException("无效的分配ID[" + allocId + "]");
        if (WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(wmSoAlloc.getStatus()))
            throw new WarehouseException("分配ID[" + wmSoAlloc.getAllocId() + "]已发运");
        BanQinWmSoEntity wmSoEntity = wmSoHeaderService.findEntityBySoNo(wmSoAlloc.getSoNo(), wmSoAlloc.getOrgId());

        String checkStatus = wmSoAlloc.getCheckStatus();
        String checkOp = wmSoAlloc.getCheckOp();
        Date checkTime = wmSoAlloc.getCheckTime();
        User user = UserUtils.getUser();
        if (isCancelCheck) {
            /*if (StringUtils.isNotBlank(wmSoAlloc.getCheckOp()) && wmSoAlloc.getCheckTime() != null) {
                checkStatus = WmsCodeMaster.CHECK_NEW.getCode();
            } else {
                checkStatus = WmsCodeMaster.CHECK_NOT.getCode();
            }*/
            // 此处需要根据装箱标记来判断是否需要复核
            checkStatus = WmsCodeMaster.CHECK_NOT.getCode();
            checkOp = null;
            checkTime = null;
        }
        wmSoAllocService.cancelPack(wmSoAlloc.getId(), checkStatus, checkOp, checkTime, user.getId());
        // 删除序列号
        wmSoSerialService.removeByAllocId(allocId, orgId);
        wmPackSerialService.removeByAllocId(allocId, orgId);
        // 更新SO单头的打包状态
        outboundCommonService.updatePackStatus(wmSoEntity);
    }

    public String getReportName(String mailType) {
        String result = null;
        switch (mailType) {
            case WmsConstants.MAIl_BS_1:
                result = "bestExpressBill";
                break;
            case WmsConstants.MAIl_BS_2:
                result = "bestExpressBill2";
                break;
            case WmsConstants.MAIl_SF:
                result = "sfExpressBill";
                break;
            case WmsConstants.MAIl_ZT:
                result = "selfExpressBill";
                break;
            case WmsConstants.MAIl_ZTO:
                result = "ztoExpressBill";
                break;
            case WmsConstants.MAIl_YUNDA:
                result = "yundaExpressBill";
                break;
            case WmsConstants.MAIl_STO:
                result = "stoExpressBill";
                break;
            case WmsConstants.MAIl_YTO:
                result = "ytoExpressBill";
                break;
        }
        return result;
    }
}