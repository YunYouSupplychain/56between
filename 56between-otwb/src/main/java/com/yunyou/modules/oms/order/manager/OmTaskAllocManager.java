package com.yunyou.modules.oms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.order.service.OmTaskDetailService;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业任务分配Service
 *
 * @author liujianhua
 * @version 2022.7.29
 */
@Service
@Transactional(readOnly = true)
public class OmTaskAllocManager extends BaseService {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;

    /**
     * 描述：作业任务分配
     */
    @Transactional
    public void allocate(String taskId) {
        OmTaskHeader omTaskHeader = omTaskHeaderService.get(taskId);
        if (omTaskHeader == null) {
            return;
        }
        if (!OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
            return;// 非出库任务，不执行分配
        }
        if (!OmsConstants.OMS_TASK_STATUS_20.equals(omTaskHeader.getStatus()) && !OmsConstants.OMS_TASK_STATUS_25.equals(omTaskHeader.getStatus())) {
            return;// 非确认或部分分配状态，不执行分配
        }
        if (omChainHeaderService.isIntercepted(omTaskHeader.getChainNo(), omTaskHeader.getOrgId())) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]任务订单已拦截，无法操作");
        }
        List<OmTaskDetail> omTaskDetailList = omTaskDetailService.findList(new OmTaskDetail(null, taskId));
        for (OmTaskDetail omTaskDetail : omTaskDetailList) {
            if (!OmsConstants.OMS_TK_ALLOC_STATUS_00.equals(omTaskDetail.getAllocStatus())) {
                continue;
            }
            // 校验可用库存是否满足待分配数量
            if (OmsConstants.OMS_Y.equals(omTaskHeader.getIsAvailableStock())) {
                double availableQty = omSaleInventoryService.getAvailableQty(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), omTaskHeader.getWarehouse());
                if (omTaskDetail.getQty().doubleValue() > availableQty) {
                    throw new OmsException("商品【" + omTaskDetail.getSkuCode() + "】库存不足，无法分配");
                }
            }
            // 更新销售库存分配数量
            omSaleInventoryService.updateAllocQty(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), omTaskDetail.getQty().doubleValue(),
                omTaskHeader.getWarehouse(), omTaskHeader.getOrgId(), OmsConstants.OMS_OP_TYPE_ALLOC, omTaskHeader.getTaskNo());
            // 更新任务明细分配状态
            omTaskDetail.setAllocStatus(OmsConstants.OMS_TK_ALLOC_STATUS_10);
            omTaskDetailService.save(omTaskDetail);
        }
        // 执行状态更新
        if (omTaskDetailList.stream().anyMatch(o -> OmsConstants.OMS_TK_ALLOC_STATUS_00.equals(o.getAllocStatus()))) {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_25);
            omTaskHeaderService.save(omTaskHeader);
        } else {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_30);
            omTaskHeaderService.save(omTaskHeader);
        }
    }

    /**
     * 描述：取消分配
     *
     * @author ZYF on 2019/6/12
     */
    @Transactional
    public void unAllocate(String taskId) {
        OmTaskHeader omTaskHeader = omTaskHeaderService.get(taskId);
        if (omTaskHeader == null) {
            return;
        }
        if (!OmsConstants.OMS_TASK_TYPE_01.equals(omTaskHeader.getTaskType())) {
            return;// 非出库任务，不执行取消分配
        }
        if (!OmsConstants.OMS_TASK_STATUS_30.equals(omTaskHeader.getStatus()) && !OmsConstants.OMS_TASK_STATUS_25.equals(omTaskHeader.getStatus())) {
            return;// 非分配状态，不执行取消分配
        }
        List<OmTaskDetail> omTaskDetailList = omTaskDetailService.findList(new OmTaskDetail(null, taskId));
        for (OmTaskDetail omTaskDetail : omTaskDetailList) {
            if (!OmsConstants.OMS_TK_ALLOC_STATUS_10.equals(omTaskDetail.getAllocStatus())) {
                continue;// 非已分配状态， 跳过
            }
            // 更新销售库存分配数量
            omSaleInventoryService.updateAllocQty(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), -omTaskDetail.getQty().doubleValue(),
                omTaskHeader.getWarehouse(), omTaskHeader.getOrgId(), OmsConstants.OMS_OP_TYPE_UNALLOC, omTaskHeader.getTaskNo());
            // 更新任务明细分配状态
            omTaskDetail.setAllocStatus(OmsConstants.OMS_TK_ALLOC_STATUS_00);
            omTaskDetailService.save(omTaskDetail);
        }
        // 执行状态更新
        if (omTaskDetailList.stream().anyMatch(o -> OmsConstants.OMS_TK_ALLOC_STATUS_10.equals(o.getAllocStatus()))) {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_25);
            omTaskHeaderService.save(omTaskHeader);
        } else {
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_20);
            omTaskHeaderService.save(omTaskHeader);
        }
    }
}
