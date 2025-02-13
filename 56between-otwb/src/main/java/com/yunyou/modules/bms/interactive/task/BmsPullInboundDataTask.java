package com.yunyou.modules.bms.interactive.task;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.service.BmsPullDataHandle;
import com.yunyou.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;

import java.util.Date;

/**
 * 描述：BMS拉取入库数据定时任务(DisallowConcurrentExecution:禁止并发执行多个相同定义的JobDetail)
 *
 * @author Jianhua
 * @version 2019/6/4
 */
@DisallowConcurrentExecution
public class BmsPullInboundDataTask extends Task {
    @Override
    public void run() {
        Date date = DateUtil.subMonths(new Date(), 1);// 上个月
        SpringContextHolder.getBean(BmsPullDataHandle.class).dataHandle(new BmsPullDataCondition(BmsPullDataCondition.DATA_TYPE_INBOUND, DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date)));
    }
}
