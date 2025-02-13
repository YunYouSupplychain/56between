package com.yunyou.modules.wms.report.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmAllOrderEntity;
import com.yunyou.modules.wms.report.mapper.BanQinWmAllOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmAllOrderService extends BaseService {
    @Autowired
    private BanQinWmAllOrderMapper mapper;

    public Page<WmAllOrderEntity> findPage(Page page, WmAllOrderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("och.org_id", entity.getOrgId()));
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

}
