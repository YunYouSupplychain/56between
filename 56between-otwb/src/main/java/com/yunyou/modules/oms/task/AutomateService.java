package com.yunyou.modules.oms.task;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.manager.OmChainInterceptManager;
import com.yunyou.modules.oms.order.manager.OmTaskAllocManager;
import com.yunyou.modules.oms.order.manager.OmTaskGenManager;
import com.yunyou.modules.oms.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AutomateService extends BaseService {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmChainDetailService omChainDetailService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskGenManager omTaskGenManager;
    @Autowired
    private OmTaskAllocManager omTaskAllocManager;
    @Autowired
    private OmChainInterceptManager omChainInterceptManager;

    /**
     * 描述：任务生成 - 查询定时任务所满足的供应链订单ID
     *
     * @author Jianhua on 2020-2-11
     */
    List<String> findChainId() {
        return omChainHeaderService.findChainIdForTimer();
    }

    /**
     * 描述：任务下发 - 查询定时任务所满足的任务ID
     *
     * @author Jianhua on 2020-2-11
     */
    List<String> findTaskId() {
        return omTaskHeaderService.findCanPushTaskIdForTimer();
    }

    /**
     * 描述：任务生成 - 按供应链订单
     *
     * @author liujianhua created on 2019-12-25
     */
    @Transactional
    public void createTask(OmChainHeader omChainHeader) {
        if (StringUtils.isBlank(omChainHeader.getWarehouse())) {
            return;// 下发机构为空，跳过
        }
        // 设置计划任务数
        List<OmChainDetail> details = omChainDetailService.getDetails(omChainHeader.getId());
        details.forEach(o -> o.setPlanTaskQty(o.getQty()));
        omChainHeader.setOmChainDetailList(details);
        // 生成作业任务
        omTaskGenManager.genTask(omChainHeader);
    }

    /**
     * 描述：任务分配 - 按供应链订单
     *
     * @author liujianhua created on 2019-12-25
     */
    @Transactional
    public void allocateTask(String chainNo, String orgId) {
        List<String> taskIds = omTaskHeaderService.findTaskIdByChainIdForTimer(chainNo, orgId);
        for (String taskId : taskIds) {
            omTaskAllocManager.allocate(taskId);
        }
    }

    /**
     * 描述：截单 - 查询定时任务所满足的供应链订单ID(拦截状态为“拦截10”且是出库)
     *
     * @author Jianhua on 2020-2-12
     */
    public List<String> findInterceptChainId() {
        return omChainHeaderService.findInterceptChainIdForTimer();
    }

    /**
     * 描述：截单 - 按供应链订单
     *
     * @author Jianhua on 2020-2-12
     */
    @Transactional
    public void intercept(String chainId) {
        omChainInterceptManager.intercept(chainId);
    }
}
