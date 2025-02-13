package com.yunyou.modules.monitor.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingConstant;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.wms.interfaces.service.BanQinWmInterfaceUpdateService;
import com.yunyou.modules.wms.weigh.entity.BanQinWmMiddleWeigh;
import com.yunyou.modules.wms.weigh.service.BanQinWmMiddleWeighService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时任务 - WMS自动发货
 *
 * @author liujianhua
 */
@DisallowConcurrentExecution
public class WmsAutoShipOrderTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private volatile boolean synchronize = false;
    private BanQinWmMiddleWeighService banQinWmMiddleWeighService;
    private BanQinWmInterfaceUpdateService banQinWmInterfaceUpdateService;

    @Override
    public void run() {
        if (synchronize) {
            return;
        }
        synchronize = true;
        try {
            banQinWmMiddleWeighService = SpringContextHolder.getBean(BanQinWmMiddleWeighService.class);
            banQinWmInterfaceUpdateService = SpringContextHolder.getBean(BanQinWmInterfaceUpdateService.class);
            List<BanQinWmMiddleWeigh> middleInfoList = banQinWmMiddleWeighService.findListByStatus(BanQinWeighingConstant.STATUS_Y, BanQinWeighingConstant.STATUS_N);
            if (CollectionUtil.isNotEmpty(middleInfoList)) {
                for (BanQinWmMiddleWeigh weighInfo : middleInfoList) {
                    banQinWmInterfaceUpdateService.autoShipByMiddle(weighInfo);
                }
            }
        } catch (Exception e) {
            logger.error("Wms自动发货异常", e);
        } finally {
            synchronize = false;
        }
    }

}
