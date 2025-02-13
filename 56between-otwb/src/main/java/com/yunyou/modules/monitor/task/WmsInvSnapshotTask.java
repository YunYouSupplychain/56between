package com.yunyou.modules.monitor.task;

import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvSnapshotService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 每日库存快照定时任务
 *
 * @author liujianhua
 */
@DisallowConcurrentExecution
public class WmsInvSnapshotTask extends Task {
    private Logger logger = LoggerFactory.getLogger(WmsInvSnapshotTask.class);

    @Override
    public void run() {
        BanQinWmInvSnapshotService wmInvSnapshotService = SpringContextHolder.getBean(BanQinWmInvSnapshotService.class);
        String date = DateUtils.formatDate(new Date());

        try {
            wmInvSnapshotService.generateInvLotSnapshot(date);
        } catch (RuntimeException e) {
            logger.error("每日批次库存快照备份失败", e);
        }
        try {
            wmInvSnapshotService.generateInvLotLocSnapshot(date);
        } catch (RuntimeException e) {
            logger.error("每日批次库位库存快照备份失败", e);
        }
    }
}