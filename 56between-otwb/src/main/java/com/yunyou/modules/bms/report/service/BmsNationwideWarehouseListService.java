package com.yunyou.modules.bms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsNationwideWarehouseList;
import com.yunyou.modules.bms.report.mapper.BmsNationwideWarehouseListMapper;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmsNationwideWarehouseListService extends CrudService<BmsNationwideWarehouseListMapper, BmsNationwideWarehouseList> {
    @Autowired
    private AreaService areaService;

    @Override
    public Page<BmsNationwideWarehouseList> findPage(Page<BmsNationwideWarehouseList> page, BmsNationwideWarehouseList entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<BmsNationwideWarehouseList> list = mapper.findList(entity);
        for (BmsNationwideWarehouseList o : list) {
            o.setArea(areaService.getFullName(o.getAreaId()));
        }
        page.setList(list);
        return page;
    }
}
