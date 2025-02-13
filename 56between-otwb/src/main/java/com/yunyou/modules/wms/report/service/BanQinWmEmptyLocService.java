package com.yunyou.modules.wms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.report.mapper.BanQinWmEmptyLocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmEmptyLocService extends BaseService {
    @Autowired
    private BanQinWmEmptyLocMapper mapper;

    public Page<BanQinCdWhLoc> findPage(Page page, BanQinCdWhLoc entity) {
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

}
