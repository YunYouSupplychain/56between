package com.yunyou.modules.oms.order.manager;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.*;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionEntity;
import com.yunyou.modules.oms.order.service.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作业任务删除Service
 *
 * @author liujianhua
 * @version 2022.7.29
 */
@Service
@Transactional(readOnly = true)
public class OmTaskRemoveManager {
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmChainDetailService omChainDetailService;
    @Autowired
    private OmRequisitionManager omRequisitionManager;
    @Autowired
    private OmRequisitionHeaderService omRequisitionHeaderService;
    @Autowired
    private OmRequisitionDetailService omRequisitionDetailService;
    @Autowired
    private OmTaskAllocManager omTaskAllocManager;
    @Autowired
    private PushTaskService pushTaskService;


    /**
     * 描述：删除任务及明细
     *
     * @param taskId          作业任务ID
     * @param ignoreSameBatch 忽略同批次任务处理
     * @param isReturnSource  回退源头订单信息
     */
    @Transactional
    public void remove(String taskId, boolean ignoreSameBatch, boolean isReturnSource) {
        OmTaskHeader omTaskHeader = omTaskHeaderService.get(taskId);
        if (omTaskHeader == null) {
            return;// 数据已过期，退出
        }
        // 判断任务是否是合并任务；若果是，退出
        List<OmTaskHeader> associatedTasks = omTaskHeaderService.getByAssociatedTaskId(omTaskHeader.getId());
        if (CollectionUtil.isNotEmpty(associatedTasks) && associatedTasks.size() > 0) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]合并任务，无法操作");
        }
        // 检查任务状态；1.如果已下发，尝试删除下发内容；成功，进行下一步；失败，退出
        pushTaskService.cancelPush(taskId);
        // 检查任务状态；2.如果是出库任务且已分配，执行取消分配，成功，进行下一步
        omTaskAllocManager.unAllocate(taskId);
        // 查询同批次作业任务，执行删除
        if (!ignoreSameBatch) {
            OmTaskHeader condition = new OmTaskHeader();
            condition.setLotNum(omTaskHeader.getLotNum());
            condition.setOrgId(omTaskHeader.getOrgId());
            List<OmTaskHeader> omTaskHeaders = omTaskHeaderService.findList(condition);
            omTaskHeaders.stream().filter(o -> !taskId.equals(o.getId())).forEach(o -> this.remove(o.getId(), true, false));
        }
        // 回退源头订单信息
        if (isReturnSource) {
            if (OmsConstants.TASK_SOURCE_CO.equals(omTaskHeader.getTaskSource())) {
                this.rollbackChainInDeleteTask(omTaskHeader);
            } else if (OmsConstants.TASK_SOURCE_RO.equals(omTaskHeader.getTaskSource())) {
                this.rollbackRequisitionInDeleteTask(omTaskHeader);
            }
        }
        // 执行任务删除
        omTaskHeaderService.delete(omTaskHeader);
    }

    /**
     * 在删除任务时回滚供应链订单信息
     */
    @Transactional
    public void rollbackChainInDeleteTask(OmTaskHeader omTaskHeader) {
        List<OmTaskDetail> omTaskDetailList = omTaskDetailService.findList(new OmTaskDetail(null, omTaskHeader.getId()));
        for (OmTaskDetail omTaskDetail : omTaskDetailList) {
            OmChainDetail omChainDetail = omChainDetailService.get(omTaskDetail.getChainNo(), omTaskDetail.getLineNo(), omTaskDetail.getOrgId());
            if (omChainDetail.getTaskQty().compareTo(omTaskDetail.getQty()) < 0) {
                omChainDetail.setTaskQty(BigDecimal.ZERO);
            } else {
                omChainDetail.setTaskQty(omChainDetail.getTaskQty().subtract(omTaskDetail.getQty()));
            }
            omChainDetailService.save(omChainDetail);
        }
        String[] chainNos = omTaskHeader.getChainNo().split(",");
        for (String chainNo : chainNos) {
            // 执行订单状态更新
            OmChainHeader omChainHeader = omChainHeaderService.get(chainNo, omTaskHeader.getOrgId());
            if (omChainHeader.getOmChainDetailList().stream().allMatch(o -> o.getTaskQty() == null || o.getTaskQty().compareTo(BigDecimal.ZERO) == 0)) {
                // 订单明细的任务数量都为空或零，则状态为“审核”
                omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_30);
                // 去除明细更新
                omChainHeader.setOmChainDetailList(Lists.newArrayList());
                omChainHeaderService.save(omChainHeader);
            } else if (omChainHeader.getOmChainDetailList().stream().anyMatch(o -> o.getTaskQty() != null && o.getTaskQty().compareTo(o.getQty()) == 0)) {
                // 订单明细的任务数量存在空或零，则状态为“部分生成任务”
                omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_35);
                // 去除明细更新
                omChainHeader.setOmChainDetailList(Lists.newArrayList());
                omChainHeaderService.save(omChainHeader);
            }
        }
    }

    /**
     * 在删除任务时回滚调拨订单信息
     */
    @Transactional
    public void rollbackRequisitionInDeleteTask(OmTaskHeader omTaskHeader) {
        List<OmTaskDetail> omTaskDetailList = omTaskDetailService.findList(new OmTaskDetail(null, omTaskHeader.getId()));
        for (OmTaskDetail omTaskDetail : omTaskDetailList) {
            OmRequisitionDetail omRequisitionDetail = omRequisitionManager.getDetailEntity(omTaskDetail.getChainNo(), omTaskDetail.getLineNo(), omTaskDetail.getOrgId());
            if (omRequisitionDetail.getTaskQty().compareTo(omTaskDetail.getQty()) < 0) {
                omRequisitionDetail.setTaskQty(BigDecimal.ZERO);
            } else {
                omRequisitionDetail.setTaskQty(omRequisitionDetail.getTaskQty().subtract(omTaskDetail.getQty()));
            }
            omRequisitionDetailService.save(omRequisitionDetail);
        }
        String[] reqNos = omTaskHeader.getChainNo().split(",");
        for (String reqNo : reqNos) {
            // 执行订单状态更新
            OmRequisitionEntity omRequisition = omRequisitionManager.getEntity(reqNo, omTaskHeader.getOrgId());
            if (omRequisition.getOmRequisitionDetailList().stream().allMatch(o -> o.getTaskQty() == null || o.getTaskQty().compareTo(BigDecimal.ZERO) == 0)) {
                // 订单明细的任务数量都为空或零，则状态为“审核”
                omRequisition.setStatus(OmsConstants.OMS_RO_STATUS_20);
                omRequisitionHeaderService.save(omRequisition);
            } else if (omRequisition.getOmRequisitionDetailList().stream().anyMatch(o -> o.getTaskQty() != null && o.getTaskQty().compareTo(o.getQty()) == 0)) {
                // 订单明细的任务数量存在空或零，则状态为“部分生成任务”
                omRequisition.setStatus(OmsConstants.OMS_RO_STATUS_35);
                omRequisitionHeaderService.save(omRequisition);
            }
        }
    }
}
