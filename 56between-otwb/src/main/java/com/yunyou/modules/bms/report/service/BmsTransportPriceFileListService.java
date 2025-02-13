package com.yunyou.modules.bms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsTransportPriceFileList;
import com.yunyou.modules.bms.report.mapper.BmsTransportPriceFileListMapper;
import org.springframework.stereotype.Service;

@Service
public class BmsTransportPriceFileListService extends CrudService<BmsTransportPriceFileListMapper, BmsTransportPriceFileList> {
    @Override
    public Page<BmsTransportPriceFileList> findPage(Page<BmsTransportPriceFileList> page, BmsTransportPriceFileList entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }
}
