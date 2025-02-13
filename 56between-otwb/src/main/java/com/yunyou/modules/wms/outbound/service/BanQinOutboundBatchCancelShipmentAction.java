package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.entity.OmInventoryUpdateEntity;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批量取消发货
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
public class BanQinOutboundBatchCancelShipmentAction extends BaseAction {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 取消发货
    @Autowired
    protected BanQinOutboundCancelShipmentService outboundCancelShipmentService;
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
    // OMS下发
    @Autowired
    private PushTaskService pushTaskService;

    /**
     * 取消发货
     */
    public ResultMessage outboundBatchCancelShipment(String processByCode, List<String> noList, String orgId) {
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
        // 按波次单号/按出单号 获取分配明细
        List<BanQinWmSoAllocEntity> wmSoAllocEntityList = wmSoAllocService.getEntityByProcessByCode(processByCode, noList, WmsCodeMaster.ALLOC_FULL_SHIPPING.getCode(), orgId);
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
        message = outboundBatchCancelShipment(wmSoAllocEntityList);
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
     * 按分配明细批量取消发货
     */
    public ResultMessage outboundBatchCancelShipment(List<BanQinWmSoAllocEntity> wmSoAllocEntityList) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmSoAllocEntity> successList = Lists.newArrayList();
        for (BanQinWmSoAllocEntity wmSoAllocEntity : wmSoAllocEntityList) {
            // 如果是直接越库匹配，并且操作步骤是收货+分配+拣货+发货，则提示
            if (StringUtils.isNotEmpty(wmSoAllocEntity.getCdType()) && wmSoAllocEntity.getCdType().equals(WmsCodeMaster.CD_TYPE_DIRECT.getCode())
                && wmSoAllocEntity.getCdOutStep().equals(WmsCodeMaster.RCV_ALLOC_PK_SP.getCode())) {
                msg.addMessage(wmSoAllocEntity.getAllocId() + "越库执行同时做了发货，只能做取消越库");
                msg.setSuccess(false);
                continue;
            }
            try {
                // 已经装车单，不能取消发运
                // (只针对发运->生成装车单流程，按扭非装车单界面，isLd=Y)
                // 装车单界面，isLd=null
                if (wmLdDetailService.CheckIsGeneratorLdByAllocId(wmSoAllocEntity.getAllocId(), wmSoAllocEntity.getOrgId())) {
                    throw new WarehouseException(wmSoAllocEntity.getAllocId() + "已经装车，不能取消发运");
                }
                // 取消发货
                outboundCancelShipmentService.outboundCancelShipment(wmSoAllocEntity);
                successList.add(wmSoAllocEntity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (CollectionUtil.isNotEmpty(successList)) {
            // 更新OMS销售库存发货数量
            new Thread(() -> updateOmsInventory(successList)).start();
        }
        // 消息处理
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
            Map<String, List<BanQinWmSoAllocEntity>> skuMap = list.stream().collect(Collectors.groupingBy(BanQinWmSoAllocEntity::getSkuCode));
            BanQinWmSoHeader soHeader = wmSoHeaderService.findBySoNo(list.get(0).getSoNo(), list.get(0).getOrgId());
            skuMap.values().forEach(result -> {
                OmInventoryUpdateEntity updateEntity = new OmInventoryUpdateEntity();
                final BanQinWmSoAllocEntity entity = result.get(0);
                double sumShipQty = result.stream().mapToDouble(BanQinWmSoAllocEntity::getQtyEa).sum();
                updateEntity.setShipQty(sumShipQty);
                updateEntity.setOwnerCode(entity.getOwnerCode());
                updateEntity.setSkuCode(entity.getSkuCode());
                updateEntity.setWarehouse(entity.getOrgId());
                updateEntity.setTaskNo(soHeader.getDef3());
                entityList.add(updateEntity);
            });
            try {
                pushTaskService.omUpdateShipQty(entityList);
            } catch (Exception e) {
                logger.error("OMS销售库存更新异常", e);
            }
        }
    }

}