package com.yunyou.modules.wms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.wms.report.entity.WmRepCombineEntity;
import com.yunyou.modules.wms.report.mapper.BanQinWmRepCombineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanQinWmRepCombineService extends BaseService {
    @Autowired
    private BanQinWmRepCombineMapper mapper;

    public Page<WmRepCombineEntity> findPage(Page page, WmRepCombineEntity entity) {
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

}
