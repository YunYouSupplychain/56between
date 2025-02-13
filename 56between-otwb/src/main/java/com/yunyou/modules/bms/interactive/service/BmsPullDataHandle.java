package com.yunyou.modules.bms.interactive.service;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liujianhua
 * @Description BMS拉取业务数据处理类
 */
@Service
public class BmsPullDataHandle {
    private Logger logger = LoggerFactory.getLogger(BmsPullDataHandle.class);
    @Autowired
    private BmsDataHandleService bmsDataHandleService;

    /**
     * 描述：拉取数据，将日期范围按天循环处理
     */
    public ResultMessage dataHandle(BmsPullDataCondition condition) {
        if (condition.getFmDate() == null || condition.getToDate() == null) {
            return new ResultMessage(false, "未指定同步数据日期范围");
        }
        try {
            Date fmDate = DateUtil.beginOfDate(condition.getFmDate());
            Date toDate = DateUtil.endOfDate(condition.getToDate());
            Date startDate = fmDate;
            while (!startDate.after(toDate)) {
                Date endDate = DateUtil.endOfDate(startDate);
                condition.setFmDate(startDate);
                condition.setToDate(endDate.after(toDate) ? toDate : endDate);

                switch (condition.getDateType()) {
                    case BmsPullDataCondition.DATA_TYPE_INBOUND:
                        bmsDataHandleService.inboundDataHandle(condition);
                        break;
                    case BmsPullDataCondition.DATA_TYPE_OUTBOUND:
                        bmsDataHandleService.outboundDataHandle(condition);
                        break;
                    case BmsPullDataCondition.DATA_TYPE_INVENTORY:
                        bmsDataHandleService.inventoryDataHandle(condition);
                        break;
                    case BmsPullDataCondition.DATA_TYPE_WAYBILL_ORDER:
                        bmsDataHandleService.waybillDataHandle(condition);
                        break;
                    case BmsPullDataCondition.DATA_TYPE_DISPATCH_ORDER:
                        bmsDataHandleService.dispatchOrderDataHandle(condition);
                        break;
                    case BmsPullDataCondition.DATA_TYPE_DISPATCH:
                        bmsDataHandleService.dispatchDataHandle(condition);
                        break;
                    default:
                        break;
                }
                startDate = DateUtil.beginOfDate(DateUtil.addDays(endDate, 1));
            }
        } catch (Exception e) {
            String jsonString = JSON.toJSONString(condition);
            logger.error("BMS拉取数据异常，相关参数：{}", jsonString, e);
            return new ResultMessage(false, "BMS拉取数据异常，相关参数：" + jsonString);
        }
        return new ResultMessage(true);
    }
}
