package com.yunyou.modules.monitor.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.interfaces.interactive.action.SyncSettlementDataAction;
import com.yunyou.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 结算商品同步
 *
 * @author zyf
 * @version 2019-07-05
 */
@DisallowConcurrentExecution
public class SyncSettlementSkuTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private volatile boolean synchronize = false;
    private SyncSettlementDataAction syncSettlementDataAction;

    @Override
    public void run() {
        if (synchronize) {
            return;
        }
        synchronize = true;
        try {
            serviceInit();
            syncSettlementDataAction.synchroSkuAutoDate();
        } catch (Exception e) {
            logger.error("S同步接口异常", e);
        } finally {
            synchronize = false;
        }
    }

    private void serviceInit() {
        if (syncSettlementDataAction == null) {
            syncSettlementDataAction = SpringContextHolder.getBean(SyncSettlementDataAction.class);
        }
    }
}
