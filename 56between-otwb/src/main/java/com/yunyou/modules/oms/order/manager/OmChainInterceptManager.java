package com.yunyou.modules.oms.order.manager;

import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.inv.service.OmSaleInventoryService;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.order.service.OmTaskDetailService;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 供应链订单拦截Service
 *
 * @author liujianhua
 * @version 2022.7.29
 */
@Service
@Transactional(readOnly = true)
public class OmChainInterceptManager {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskDetailService omTaskDetailService;
    @Autowired
    private OmSaleInventoryService omSaleInventoryService;
    @Autowired
    private PushTaskService pushTaskService;

    /**
     * 描述：截单
     */
    @Transactional
    public void intercept(String chainId) {
        OmChainHeader omChainHeader = omChainHeaderService.get(chainId);
        if (omChainHeader == null) {
            return;
        }

        // Ⅰ 查看是否能拦截(拦截状态为：10拦截 90拦截失败才能做拦截)
        if (!(OmsConstants.OMS_INTERCEPT_STATUS_10.equals(omChainHeader.getInterceptStatus())
                || OmsConstants.OMS_INTERCEPT_STATUS_90.equals(omChainHeader.getInterceptStatus()))) {
            return;
        }
        List<OmTaskHeader> omTaskHeaders = omTaskHeaderService.getByChainNo(omChainHeader.getChainNo(), omChainHeader.getOrgId());
        for (OmTaskHeader omTaskHeader : omTaskHeaders) {
            // Ⅱ 查看是否已下发，若已下发，检查wms so是否已完成截单，若未完成则跳出，等待完成后再执行
            if (OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus())) {
                if (omTaskHeaderService.isPushSoOrderInterceptionSuccess(omTaskHeader.getChainNo(), omTaskHeader.getTaskNo(), omTaskHeader.getWarehouse())) {
                    throw new OmsException("[" + omChainHeader.getChainNo() + "]订单[" + omTaskHeader.getTaskNo() + "]任务已下发，需先拦截成功下游订单后才可执行");
                }
                pushTaskService.cancelPush(omTaskHeader.getId());
                omTaskHeader = omTaskHeaderService.get(omTaskHeader.getId());
            }
            // Ⅲ 检查是否已分配，若已分配，加回库存，插入交易记录
            if (OmsConstants.OMS_TASK_STATUS_25.equals(omTaskHeader.getStatus())
                    || OmsConstants.OMS_TASK_STATUS_30.equals(omTaskHeader.getStatus())
                    || OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus())) {
                List<OmTaskDetail> detailList = omTaskDetailService.findList(new OmTaskDetail(null, omTaskHeader.getId()));
                for (OmTaskDetail omTaskDetail : detailList) {
                    if (!OmsConstants.OMS_TK_ALLOC_STATUS_10.equals(omTaskDetail.getAllocStatus())) {
                        continue;
                    }
                    // 更新销售库存分配数量
                    omSaleInventoryService.updateAllocQty(omTaskHeader.getOwner(), omTaskDetail.getSkuCode(), -omTaskDetail.getQty().doubleValue(),
                            omTaskHeader.getWarehouse(), omTaskHeader.getOrgId(), OmsConstants.OMS_OP_TYPE_INTERCEPT, omTaskHeader.getTaskNo());
                    // 更新任务明细分配状态
                    omTaskDetail.setAllocStatus(OmsConstants.OMS_TK_ALLOC_STATUS_90);
                    omTaskDetailService.save(omTaskDetail);
                }
            }
            // Ⅳ 任务状态置为“取消”
            omTaskHeader.setStatus(OmsConstants.OMS_TASK_STATUS_90);
            omTaskHeaderService.save(omTaskHeader);
        }
        // Ⅵ 供应链订单状态直接置为“取消”，截单状态置为“截单成功”，截单完成
        omChainHeader.setInterceptTime(new Date());
        omChainHeader.setInterceptStatus(OmsConstants.OMS_INTERCEPT_STATUS_99);
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_90);
        // 去除明细更新
        omChainHeader.setOmChainDetailList(Lists.newArrayList());
        omChainHeaderService.save(omChainHeader);
    }
}
