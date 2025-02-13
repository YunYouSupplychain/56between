package com.yunyou.modules.tms.report.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.report.entity.TmGasDailyStatistics;
import com.yunyou.modules.tms.report.mapper.TmGasDailyStatisticsMapper;
import org.springframework.stereotype.Service;

@Service
public class TmGasDailyStatisticsService extends CrudService<TmGasDailyStatisticsMapper, TmGasDailyStatistics> {

    public Page<TmGasDailyStatistics> findPage(Page<TmGasDailyStatistics> page, TmGasDailyStatistics qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        if (qEntity.getQueryDate() != null) {
            qEntity.setFmDate(DateUtil.beginOfDate(qEntity.getQueryDate()));
            qEntity.setToDate(DateUtil.endOfDate(qEntity.getQueryDate()));
        }
        page.setList(mapper.findPage(qEntity));
        return page;
    }
}
