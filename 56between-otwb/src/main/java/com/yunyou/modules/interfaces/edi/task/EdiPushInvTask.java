package com.yunyou.modules.interfaces.edi.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.interfaces.edi.entity.EdiLog;
import com.yunyou.modules.interfaces.edi.service.EdiLogService;
import com.yunyou.modules.interfaces.edi.service.PushToEdiService;
import com.yunyou.modules.interfaces.edi.utils.EdiConstant;
import com.yunyou.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class EdiPushInvTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final PushToEdiService pushToEdiService = SpringContextHolder.getBean(PushToEdiService.class);
    private final EdiLogService logService = SpringContextHolder.getBean(EdiLogService.class);
    private volatile boolean synchronize = false;

    @Override
    public void run() {
        if (synchronize) {
            return;
        }
        synchronize = true;
        try {
            pushToEdiService.pushToEdi(EdiConstant.EDI_TYPE_INV);
        } catch (Exception e) {
            logger.error("EDI推送库存数据报错！", e);
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_INV, "", "", e));
        }
        try {
            pushToEdiService.pushToEdi(EdiConstant.EDI_TYPE_PLAN_INV);
        } catch (Exception e) {
            logger.error("EDI推送库存数据报错！", e);
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_PLAN_INV, "", "", e));
        } finally {
            synchronize = false;
        }
    }

}