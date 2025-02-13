package com.yunyou.modules.bms.report.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.report.entity.BmsContractAccount;
import com.yunyou.modules.bms.report.mapper.BmsContractAccountMapper;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmsContractAccountService extends CrudService<BmsContractAccountMapper, BmsContractAccount> {
    @Autowired
    private AreaService areaService;

    @Override
    public Page<BmsContractAccount> findPage(Page<BmsContractAccount> page, BmsContractAccount entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<BmsContractAccount> list = mapper.findList(entity);
        for (BmsContractAccount o : list) {
            o.setArea(areaService.getFullName(o.getAreaId()));
        }
        page.setList(list);
        return page;
    }
}
