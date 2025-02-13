package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.entity.OmInventoryUpdateEntity;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.interfaces.edi.service.EdiSendOrderInfoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批量发运确认
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchShipmentAction extends BaseAction {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 发运确认
    @Autowired
    protected BanQinOutboundShipmentService outboundShipment;
    // 分配明细
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 关闭
    @Autowired
    protected BanQinOutboundCloseOrCancelService outboundCloseOrCancelService;
    // 装车单明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    @Autowired
    private PushTaskService pushTaskService;
    @Autowired
    private EdiSendOrderInfoService ediSendOrderInfoService;

    /**
     * 发运确认
     */
    public ResultMessage outboundBatchShipment(String processByCode, List<String> noList, String isLd, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 1、拦截、冻结状态校验
        // 拦截状态校验
        ResultMessage message = wmSoHeaderService.checkBatchInterceptStatus(processByCode, noList, orgId);
        if (!message.isSuccess()) {
            msg.addMessage(message.getMessage());
        }
        // 冻结状态校验
        message = wmSoHeaderService.checkBatchHoldStatus(processByCode, noList, orgId);
        if (!message.isSuccess()) {
            msg.addMessage(message.getMessage());
        }
        // 按波次单号 、按出单号 获取分配明细
        List<BanQinWmSoAllocEntity> wmSoAllocEntityList = wmSoAllocService.getEntityByProcessByCode(processByCode, noList, WmsCodeMaster.ALLOC_FULL_PICKING.getCode(), orgId);
        if (wmSoAllocEntityList.size() == 0) {
            msg.addMessage("没有可以操作的分配拣货明细");
            msg.setSuccess(false);
            return msg;
        }
        // 生成装车单明细校验
        List<String> soNoByLds = wmLdDetailService.checkIsGenLdBySoNos(noList, null, orgId);
        if (soNoByLds.size() > 0) {
            msg.addMessage("存在分配拣货记录已经生成装车单");
        }
        // 按分配明细拣货
        message = outboundBatchShipment(wmSoAllocEntityList);
        if (noList.size() > 1) {
            message.setMessage(BanQinWmsCommonService.getResultMessage(msg.getMessage()));
        }
        if (!message.isSuccess()) {
            msg.addMessage(message.getMessage());
            msg.setSuccess(false);
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

    /**
     * 按分配明细批量发运
     */
    public ResultMessage outboundBatchShipment(List<BanQinWmSoAllocEntity> wmSoAllocEntityList) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmSoAllocEntity> successList = Lists.newArrayList();
        Map<String, String> soAddrMap = MapUtil.newHashMap();
        for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntityList) {
            try {
                if (!soAddrMap.containsKey(wmSoAllocEntity.getSoNo())) {
                    BanQinWmSoHeader soHeader = wmSoHeaderService.findBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
                    if (null == soHeader) continue;
                    soAddrMap.put(soHeader.getSoNo(), getConsigneeAddr(soHeader));
                }
                if (StringUtils.isNotBlank(wmSoAllocEntity.getTrackingNo()) && !soAddrMap.get(wmSoAllocEntity.getSoNo()).equals(wmSoAllocEntity.getRemarks())) {
                    throw new WarehouseException("订单[" + wmSoAllocEntity.getSoNo() + "]的分配明细[" + wmSoAllocEntity.getAllocId() + "]面单收货地址和单头收货地址不匹配!");
                }
                // 已经装车单，不能发运确认(发运订单、波次计划、拣货任务界面不允许操作，只能在装车单界面按扭进行发货确认)
                if (wmLdDetailService.CheckIsGeneratorLdByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId())) {
                    throw new WarehouseException("分配明细[" + wmSoAllocEntity.getAllocId() + "]已经装车，不能发运确认!");
                }
                // 发运确认
                outboundShipment.outboundShipment(wmSoAllocEntity);
                // 装车单参数：拣货明细在何种状态下可以进行装车（PK: 完全拣货；SP: 完全发运）, 如果配置SP,不允许自动关闭，可能会生成装车单，进行装车交接。配置PK，允许自动关闭
                String loadAllowStatus = "PK";
                // 发货参数：完全发货后是否自动关闭SO（Y：自动关闭；N：不自动关闭）
                String shipAutoCloseSo = SysControlParamsUtils.getValue(ControlParamCode.SHIP_AUTO_CLOSE_SO.getCode(), wmSoAllocEntity.getOrgId());
                if (shipAutoCloseSo == null) {
                    shipAutoCloseSo = WmsConstants.NO;
                }
                if (WmsConstants.LOAD_ALLOW_STATUS_PK.equals(loadAllowStatus) && WmsConstants.YES.equals(shipAutoCloseSo)) {
                    BanQinWmSoHeader soHeaderModel = wmSoHeaderService.findBySoNo(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
                    if (soHeaderModel.getStatus().equals(WmsCodeMaster.SO_FULL_SHIPPING.getCode())) {// 完全关闭后自动关闭
                        outboundCloseOrCancelService.close(wmSoAllocEntity.getSoNo(), wmSoAllocEntity.getOrgId());
                    }
                }
                successList.add(wmSoAllocEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (CollectionUtil.isNotEmpty(successList)) {
            // WMS参数:是否调用外部接口（Y：调用；N：不调用）
            String isCallEdi = SysControlParamsUtils.getValue(ControlParamCode.WM_CALL_EDI.getCode(), null);
            if (isCallEdi == null) {
                isCallEdi = WmsConstants.NO;
            }
            if (isCallEdi.equals(WmsConstants.YES)) {
                // 推送EDI
                ediSendOrderInfoService.saveByWmsSo(successList);
            }
            // 更新OMS销售库存发货数量
            new Thread(() -> updateOmsInventory(successList)).start();
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

    /**
     * 更新OMS库存
     *
     * @param list 分配明细
     */
    private void updateOmsInventory(List<BanQinWmSoAllocEntity> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            List<OmInventoryUpdateEntity> entityList = Lists.newArrayList();
            Map<String, List<BanQinWmSoAllocEntity>> soMap = list.stream().collect(Collectors.groupingBy(s -> s.getSoNo() + "@" + s.getOrgId()));
            soMap.forEach((k, v) -> {
                String[] group = k.split("@", -1);
                BanQinWmSoHeader soHeader = wmSoHeaderService.findBySoNo(group[0], group[1]);
                Map<String, List<BanQinWmSoAllocEntity>> skuMap = v.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getSkuCode));
                skuMap.values().forEach(result -> {
                    BanQinWmSoAllocEntity entity = result.get(0);
                    OmInventoryUpdateEntity updateEntity = new OmInventoryUpdateEntity();
                    double sumShipQty = result.stream().mapToDouble(BanQinWmSoAllocEntity::getQtyEa).sum();
                    updateEntity.setShipQty(-sumShipQty);
                    updateEntity.setOwnerCode(entity.getOwnerCode());
                    updateEntity.setSkuCode(entity.getSkuCode());
                    updateEntity.setWarehouse(entity.getOrgId());
                    updateEntity.setTaskNo(soHeader.getDef3());
                    entityList.add(updateEntity);
                });
            });
            try {
                pushTaskService.omUpdateShipQty(entityList);
            } catch (Exception e) {
                logger.error("OMS销售库存更新异常", e);
            }
        }
    }

    private String getConsigneeAddr(BanQinWmSoHeader soHeader) {
        return (null != soHeader && StringUtils.isNotBlank(soHeader.getDef17())) ? (soHeader.getDef17().replace(":", "") + soHeader.getContactAddr()) : "";
    }
}