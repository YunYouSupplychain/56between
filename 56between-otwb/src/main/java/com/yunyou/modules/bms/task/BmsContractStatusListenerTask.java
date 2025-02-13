package com.yunyou.modules.bms.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;

/**
 * 合同状态监听定时任务
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@DisallowConcurrentExecution
public class BmsContractStatusListenerTask extends Task {
    @Override
    public void run() throws InterruptedException {
        SpringContextHolder.getBean(BmsContractService.class).updateInvalidContractStatus();
    }
}
