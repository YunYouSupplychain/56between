package com.yunyou.modules.oms.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmChainHeaderService;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 描述：自动生成任务、分配、下发
 * DisallowConcurrentExecution:禁止并发执行多个相同定义的JobDetail
 *
 * @author liujianhua created on 2019-12-24
 */
@DisallowConcurrentExecution
public class OmsAutomateTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(OmsAutomateTask.class);
    private final AutomateService automateService = SpringContextHolder.getBean(AutomateService.class);
    private final OmChainHeaderService omChainHeaderService = SpringContextHolder.getBean(OmChainHeaderService.class);
    private final OmTaskHeaderService omTaskHeaderService = SpringContextHolder.getBean(OmTaskHeaderService.class);
    private final PushTaskService pushTaskService = SpringContextHolder.getBean(PushTaskService.class);

    @Override
    public void run() {
        List<String> chainIds = automateService.findChainId();
        for (String chainId : chainIds) {
            OmChainHeader omChainHeader = omChainHeaderService.get(chainId);
            if (omChainHeader == null) {
                continue;
            }
            try {
                automateService.createTask(omChainHeader);
            } catch (OmsException e) {
                omChainHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_E);
                omChainHeaderService.save(omChainHeader);
                continue;
            }
            try {
                automateService.allocateTask(omChainHeader.getChainNo(), omChainHeader.getOrgId());
            } catch (Exception ignored) {
            }
        }

        List<String> taskIds = automateService.findTaskId();
        for (String taskId : taskIds) {
            OmTaskHeader omTaskHeader = omTaskHeaderService.get(taskId);
            if (omTaskHeader == null) {
                continue;
            }
            try {
                pushTaskService.pushTask(omTaskHeader);
            } catch (Exception e) {
                omTaskHeader.setHandleStatus(OmsConstants.HANDLE_STATUS_E);
                omTaskHeaderService.save(omTaskHeader);
            }
        }
    }
}
