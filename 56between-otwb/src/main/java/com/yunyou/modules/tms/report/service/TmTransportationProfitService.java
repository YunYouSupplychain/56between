package com.yunyou.modules.tms.report.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.report.entity.TmTransportationProfit;
import com.yunyou.modules.tms.report.mapper.TmTransportationProfitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TmTransportationProfitService extends BaseService {
    @Autowired
    private TmTransportationProfitMapper mapper;

    public Page<TmTransportationProfit> findPage(Page<TmTransportationProfit> page, TmTransportationProfit qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        page.setList(mapper.findList(qEntity));
        return page;
    }
}
