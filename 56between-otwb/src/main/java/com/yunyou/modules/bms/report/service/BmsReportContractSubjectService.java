package com.yunyou.modules.bms.report.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectEntity;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectQuery;
import com.yunyou.modules.bms.report.mapper.BmsReportContractSubjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BmsReportContractSubjectService extends CrudService<BmsReportContractSubjectMapper, BmsReportContractSubjectEntity> {

    public List<BmsReportContractSubjectEntity> findList(BmsReportContractSubjectQuery query) {
        query.setDataScope(GlobalDataRule.getDataRuleSql("bc.org_id", query.getOrgId()));
        return mapper.findList(query);
    }

    public Page<BmsReportContractSubjectEntity> findPage(Page page, BmsReportContractSubjectQuery query) {
        query.setDataScope(GlobalDataRule.getDataRuleSql("bc.org_id", query.getOrgId()));
        query.setPage(page);
        page.setList(mapper.findPage(query));
        return page;
    }
}
