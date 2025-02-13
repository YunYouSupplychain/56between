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
public class EdiPushOrderTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PushToEdiService pushToEdiService = SpringContextHolder.getBean(PushToEdiService.class);
    private EdiLogService logService = SpringContextHolder.getBean(EdiLogService.class);
    private volatile boolean synchronize = false;

    @Override
    public void run() {
        if (synchronize) {
            return;
        }
        synchronize = true;
        try {
            pushToEdiService.pushToEdi(EdiConstant.EDI_TYPE_ASN);
        } catch (Exception e) {
            logger.error("EDI推送ASN单据数据报错！", e);
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_ASN, "", "", e));
        }
        try {
            pushToEdiService.pushToEdi(EdiConstant.EDI_TYPE_SO);
        } catch (Exception e) {
            logger.error("EDI推送SO单据数据报错！", e);
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_SO, "", "", e));
        }
        try {
            pushToEdiService.pushToEdi(EdiConstant.EDI_TYPE_DO);
        } catch (Exception e) {
            logger.error("EDI推送DO单据数据报错！", e);
            logService.saveLog(EdiLog.failure(EdiConstant.EDI_TYPE_DO, "", "", e));
        } finally {
            synchronize = false;
        }
    }

}