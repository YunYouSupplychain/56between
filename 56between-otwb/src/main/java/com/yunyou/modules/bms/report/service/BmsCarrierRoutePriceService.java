package com.yunyou.modules.bms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsCarrierRoutePrice;
import com.yunyou.modules.bms.report.mapper.BmsCarrierRoutePriceMapper;
import org.springframework.stereotype.Service;

@Service
public class BmsCarrierRoutePriceService extends CrudService<BmsCarrierRoutePriceMapper, BmsCarrierRoutePrice> {
    @Override
    public Page<BmsCarrierRoutePrice> findPage(Page<BmsCarrierRoutePrice> page, BmsCarrierRoutePrice entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }
}
