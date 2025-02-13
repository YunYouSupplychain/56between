package com.yunyou.modules.tms.report.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.report.entity.TmGasRecentSchedule;
import com.yunyou.modules.tms.report.entity.TmGetActualQty;
import com.yunyou.modules.tms.report.mapper.TmGasRecentScheduleMapper;
import com.yunyou.common.utils.number.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TmGasRecentScheduleService extends BaseService {
    @Autowired
    private TmGasRecentScheduleMapper mapper;

    public Page<TmGasRecentSchedule> findPage(Page<TmGasRecentSchedule> page, TmGasRecentSchedule qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DEMAND_PLAN_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        List<TmGasRecentSchedule> list = mapper.findList(qEntity);
        for (TmGasRecentSchedule o : list) {
            o.setDailyAverageQty(this.getDailyAverageQty(o));
        }
        page.setList(list);
        return page;
    }

    private double getDailyAverageQty(TmGasRecentSchedule entity) {
        double result = 0D;
        Date date = new Date();
        Date fmDate = DateUtil.beginOfDate(DateUtil.subDays(date, 29));
        Date toDate = DateUtil.endOfDate(date);

        TmGetActualQty query = new TmGetActualQty();
        query.setFmDate(fmDate);
        query.setToDate(toDate);
        query.setOwnerCode(entity.getOwnerCode());
        query.setSkuCode(entity.getSkuCode());
        query.setBaseOrgId(entity.getBaseOrgId());
        query.setOrgId(entity.getOrgId());
        List<Double> values = mapper.getActualQty(query);
        for (Double v : values) {
            result = BigDecimalUtil.add(result, v);
        }
        return BigDecimalUtil.div(result, 30, 4, BigDecimal.ROUND_HALF_UP);
    }
}
