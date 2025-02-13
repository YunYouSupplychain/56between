package com.yunyou.modules.bms.report.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsReportAccountReceivableEntity;
import com.yunyou.modules.bms.report.entity.BmsReportAccountReceivableQuery;
import com.yunyou.modules.bms.report.mapper.BmsReportAccountReceivableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BmsReportAccountReceivableService extends CrudService<BmsReportAccountReceivableMapper, BmsReportAccountReceivableEntity> {

    public Page<BmsReportAccountReceivableEntity> findPage(Page page, BmsReportAccountReceivableQuery query) {
        query.setDataScope(GlobalDataRule.getDataRuleSql("bio.org_id", query.getOrgId()));
        query.setPage(page);
        page.setList(mapper.findPage(query));
        return page;
    }
}
