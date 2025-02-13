package com.yunyou.modules.oms.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.oms.common.OmsException;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 描述：执行截单定时任务
 *
 * @author Jianhua on 2020-2-11
 */
@DisallowConcurrentExecution
public class AutomateInterceptTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(AutomateInterceptTask.class);

    @Override
    public void run() {
        AutomateService automateService = SpringContextHolder.getBean(AutomateService.class);
        List<String> chainIds = automateService.findInterceptChainId();
        for (String chainId : chainIds) {
            try {
                automateService.intercept(chainId);
            } catch (OmsException e) {
                logger.info("定时任务 - 供应链拦截：chainId=[" + chainId + "]，" + e.getMessage());
            } catch (Exception e) {
                logger.error("定时任务 - 供应链拦截：chainId=" + chainId, e);
            }
        }
    }
}
