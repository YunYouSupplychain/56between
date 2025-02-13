package com.yunyou.modules.bms.interactive.task;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.service.BmsPullDataHandle;
import com.yunyou.modules.monitor.entity.Task;
import org.quartz.DisallowConcurrentExecution;

import java.util.ArrayList;
import java.util.Date;

/**
 * 描述：BMS拉取数据定时任务(DisallowConcurrentExecution:禁止并发执行多个相同定义的JobDetail)
 *
 * @author Jianhua
 * @version 2019/6/4
 */
@DisallowConcurrentExecution
public class BmsPullDataTask extends Task {
    @Override
    public void run() {
        ArrayList<Character> list = Lists.newArrayList(BmsPullDataCondition.DATA_TYPE_INBOUND, BmsPullDataCondition.DATA_TYPE_OUTBOUND, BmsPullDataCondition.DATA_TYPE_INVENTORY,
                BmsPullDataCondition.DATA_TYPE_DISPATCH, BmsPullDataCondition.DATA_TYPE_EXCEPTION, BmsPullDataCondition.DATA_TYPE_RETURN, BmsPullDataCondition.DATA_TYPE_DISPATCH_ORDER,
                BmsPullDataCondition.DATA_TYPE_WAYBILL_ORDER);
        BmsPullDataHandle bmsPullDataHandle = SpringContextHolder.getBean(BmsPullDataHandle.class);
        for (char dateType : list) {
            Date date = DateUtil.subMonths(new Date(), 1);// 上个月
            bmsPullDataHandle.dataHandle(new BmsPullDataCondition(dateType, DateUtil.beginOfMonth(date), DateUtil.endOfMonth(date)));
        }
    }
}
