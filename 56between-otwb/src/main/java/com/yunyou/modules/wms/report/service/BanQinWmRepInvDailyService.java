package com.yunyou.modules.wms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyByZoneEntity;
import com.yunyou.modules.wms.report.entity.WmRepInvDailyEntity;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepInvDailyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmRepInvDailyService extends BaseService {
    @Autowired
    private BanQinWmRepInvDailyMapper mapper;

    @SuppressWarnings("unchecked")
    public Page<WmRepInvDailyEntity> findPage(Page page, WmRepInvDailyEntity entity) {
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<WmRepInvDailyByZoneEntity> findPageByZone(Page page, WmRepInvDailyByZoneEntity qEntity) {
        qEntity.setPage(page);
        dataRuleFilter(qEntity);
        page.setList(mapper.findPageByZone(qEntity));
        return page;
    }

}
