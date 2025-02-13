package com.yunyou.modules.wms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmRepInvValidityEntity;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepInvValidityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmRepInvValidityService extends BaseService {
    @Autowired
    private BanQinWmRepInvValidityMapper mapper;

    public Page<WmRepInvValidityEntity> findPage(Page page, WmRepInvValidityEntity entity) {
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

}
