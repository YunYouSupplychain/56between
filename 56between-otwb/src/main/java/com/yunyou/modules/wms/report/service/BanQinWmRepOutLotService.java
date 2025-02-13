package com.yunyou.modules.wms.report.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmRepOutLotEntity;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepOutLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmRepOutLotService extends BaseService {
    @Autowired
    private BanQinWmRepOutLotMapper mapper;

    public Page<WmRepOutLotEntity> findPage(Page page, WmRepOutLotEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

}
