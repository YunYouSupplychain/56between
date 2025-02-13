package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsSkuClassification;
import com.yunyou.modules.bms.basic.mapper.BmsSkuClassificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BmsSkuClassificationService extends CrudService<BmsSkuClassificationMapper, BmsSkuClassification> {

    @Override
    public Page<BmsSkuClassification> findPage(Page<BmsSkuClassification> page, BmsSkuClassification entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<BmsSkuClassification> findGrid(Page<BmsSkuClassification> page, BmsSkuClassification entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Transactional
    public void remove(String code, String orgId) {
        mapper.remove(code, orgId);
    }
}
