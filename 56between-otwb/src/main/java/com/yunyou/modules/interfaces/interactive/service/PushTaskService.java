package com.yunyou.modules.interfaces.interactive.service;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.interactive.entity.OmInventoryUpdateEntity;
import com.yunyou.modules.oms.basic.service.OmBusinessOrderTypeRelationService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.order.service.OmTaskDetailService;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：OMS推送任务Service
 *
 */
@Service
@Transactional(readOnly = true)
public class PushTaskService {
    @Autowired
    private PushWmsService pushWmsService;
    @Autowired
    private PushTmsService pushTmsService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;
    private final Object syn = '1';

    /**
     * 校验三级地址
     */
    private void checkLevel3Address(String taskNo, String level3Address) {
        if (StringUtils.isBlank(level3Address)) {
            throw new OmsException("[" + taskNo + "]任务收货人区域为空");
        }
        String[] areaAddrs = level3Address.split(":");
        if (areaAddrs.length < 3 || Arrays.stream(areaAddrs).anyMatch(StringUtils::isBlank)) {
            throw new OmsException("[" + taskNo + "]任务收货人区域格式维护错误！");
        }
    }

    /**
     * 描述：取消下发
     */
    @Transactional
    public void cancelPush(String taskId) {
        OmTaskHeader omTaskHeader = omTaskHeaderService.get(taskId);
        if (omTaskHeader == null) {
            return;
        }
        if (!OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus())) {
            return;
        }
        this.cancelPushTask(omTaskHeader);
    }

    /**
     * 描述：下发任务
     */
    @Transactional
    public void pushTask(OmTaskHeader omTaskHeader) {
        if (omChainHeaderService.isIntercepted(omTaskHeader.getChainNo(), omTaskHeader.getOrgId())) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]任务订单已拦截，无法操作");
        }
        // 出库任务下发时校验收货人三级地址
        if (OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType()) && SystemAliases.WMS.getCode().equals(omTaskHeader.getPushSystem())) {
            final String isCheckThreeLevelAddr = SysControlParamsUtils.getValue(SysParamConstants.IS_CHECK_THREE_LEVEL_ADDR, omTaskHeader.getOrgId());
            if (OmsConstants.OMS_Y.equals(isCheckThreeLevelAddr)) {
                this.checkLevel3Address(omTaskHeader.getTaskNo(), omTaskHeader.getConsigneeAddressArea());
            }
        }
        omTaskHeader.setOmTaskDetailList(omTaskDetailService.findList(new OmTaskDetail(null, omTaskHeader.getId())));
        this.pushTaskToDownstream(omTaskHeader);

        // 同时下发同批次运输任务
        if (!OmsConstants.OMS_TASK_TYPE_03.equals(omTaskHeader.getTaskType())) {
            // 系统控制参数：是否同时下发同批次运输任务
            final String IS_SEND_BATCH_TRANS_TASK = SysControlParamsUtils.getValue(SysParamConstants.IS_SEND_BATCH_TRANS_TASK, omTaskHeader.getOrgId());
            if (OmsConstants.OMS_Y.equals(IS_SEND_BATCH_TRANS_TASK)) {
                List<OmTaskHeader> batchTask = omTaskHeaderService.getBatchTask(OmsConstants.OMS_TASK_TYPE_03, Lists.newArrayList(omTaskHeader.getLotNum()), omTaskHeader.getOrgId());
                if (CollectionUtil.isEmpty(batchTask)) {
                    return;
                }
                batchTask.stream().filter(o -> OmsConstants.OMS_TASK_STATUS_20.equals(o.getStatus())).forEach(this::pushTaskToDownstream);
            }
        }
    }

    /**
     * 描述：下发任务
     */
    @Transactional
    public void pushTaskToDownstream(OmTaskHeader omTaskHeader) {
        if (SystemAliases.WMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
            pushWmsService.pushTaskToOutput(omTaskHeader);
        } else if (SystemAliases.WMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_02.equals(omTaskHeader.getTaskType())) {
            pushWmsService.pushTaskToInput(omTaskHeader);
        } else if (SystemAliases.TMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_03.equals(omTaskHeader.getTaskType())) {
            // OMS参数：是否下发运输任务至运输计划订单(Y:是 N:否)
            String value = SysControlParamsUtils.getValue(SysParamConstants.IS_PUSH_PRE_TRANSPORT_ORDER, omTaskHeader.getOrgId());
            if (Global.Y.equals(value)) {
                pushTmsService.pushTaskToPreTransport(omTaskHeader);
            } else {
                pushTmsService.pushTaskToTransport(omTaskHeader);
            }
        } else {
            throw new OmsException("暂不支持该类型任务【" + omTaskHeader.getTaskType() + "】下发" + SystemAliases.value(omTaskHeader.getPushSystem()).getDesc());
        }
        omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_40);
        omTaskHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_Y);
        omTaskHeaderService.save(omTaskHeader);
    }

    @Transactional
    public void cancelPushTask(OmTaskHeader omTaskHeader) {
        if (SystemAliases.WMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
            pushWmsService.deleteOutputTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());
        } else if (SystemAliases.WMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_02.equals(omTaskHeader.getTaskType())) {
            pushWmsService.deleteInputTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());
        } else if (SystemAliases.TMS.getCode().equals(omTaskHeader.getPushSystem()) && OmsConstants.OMS_TASK_TYPE_03.equals(omTaskHeader.getTaskType())) {
            if (omBusinessOrderTypeRelationService.isOnlyTransport(omTaskHeader.getBusinessOrderType(), omTaskHeader.getOrgId())) {
                pushTmsService.deleteTransportTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());
            } else {
                pushTmsService.deletePreTransportTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());
            }
        } else {
            throw new OmsException("暂不支持该类型任务【" + omTaskHeader.getTaskType() + "】下发" + SystemAliases.value(omTaskHeader.getPushSystem()).getDesc());
        }
        if (OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_30);
            omTaskHeaderService.save(omTaskHeader);
        } else {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_20);
            omTaskHeaderService.save(omTaskHeader);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void omUpdateShipQty(List<OmInventoryUpdateEntity> entityList) {
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        String taskNo = entityList.get(0).getTaskNo();
        if (StringUtils.isBlank(taskNo)) {
            return;
        }
        OmTaskHeader condition = new OmTaskHeader();
        condition.setTaskNo(taskNo);
        List<OmTaskHeader> list = omTaskHeaderService.findList(condition);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        String orgId = list.get(0).getOrgId();
        for (OmInventoryUpdateEntity entity : entityList) {
            if (!entity.isNotEmpty()) {
                continue;
            }
            synchronized (syn) {
                omSaleInventoryService.updateAllocQty(entity.getOwnerCode(), entity.getSkuCode(), entity.getShipQty(), entity.getWarehouse(), orgId, OmsConstants.OMS_OP_TYPE_SHIP, entity.getTaskNo());
            }
        }
    }

}
