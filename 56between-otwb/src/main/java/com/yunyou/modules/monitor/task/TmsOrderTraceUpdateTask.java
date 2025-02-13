package com.yunyou.modules.monitor.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.interfaces.InterfaceConstant;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.monitor.entity.Task;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.manager.TmUpdateJobManager;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时任务 - 快递节点信息更新
 *
 * @author liujianhua
 */
@DisallowConcurrentExecution
public class TmsOrderTraceUpdateTask extends Task {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private volatile boolean synchronize = false;
    private InterfaceLogService interfaceLogService;
    private TmUpdateJobManager tmUpdateJobManager;
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;

    @Override
    public void run() {
        if (synchronize) {
            return;
        }
        synchronize = true;
        try {
            interfaceLogService = SpringContextHolder.getBean(InterfaceLogService.class);
            tmUpdateJobManager = SpringContextHolder.getBean(TmUpdateJobManager.class);
            tmTransportOrderHeaderService = SpringContextHolder.getBean(TmTransportOrderHeaderService.class);
            // 1 取所有签收时间为空的运输订单
            Integer unSignCount = tmTransportOrderHeaderService.findUnSignCount();
            if (unSignCount > 0) {
                int limitF = 0;
                for (int i = 0; i < unSignCount; i += 1000) {
                    List<TmTransportOrderHeader> unSignList = tmTransportOrderHeaderService.findUnSignMailNo(limitF, 1000);
                    for (TmTransportOrderHeader tmTransportOrderHeader : unSignList) {
                        try {
                            if (tmUpdateJobManager.updateTmsWayBillTrace(tmTransportOrderHeader)) {
                                limitF--;
                            }
                        } catch (Exception e) {
                            logger.error("Tms运单跟踪信息更新异常", e);
                            interfaceLogService.saveLog(InterfaceConstant.KD_TRACE_TIMER, "N", e.getMessage(), tmTransportOrderHeader.getCustomerNo(), null, "定时器处理异常", InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
                        }
                    }
                    limitF += 1000;
                }
            }
        } catch (Exception e) {
            logger.error("Tms运单跟踪信息更新异常", e);
            interfaceLogService.saveLog(InterfaceConstant.KD_TRACE_TIMER, "N", e.getMessage(), null, null, "定时器异常", InterfaceConstant.BEST_USER, InterfaceConstant.HANDLE_DIR_RECEIVE);
        } finally {
            synchronize = false;
        }
    }

}
