package com.yunyou.modules.oms.basic.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.basic.entity.OmSkuClassification;
import com.yunyou.modules.oms.basic.mapper.OmSkuClassificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OmSkuClassificationService extends CrudService<OmSkuClassificationMapper, OmSkuClassification> {

    @Override
    public Page<OmSkuClassification> findPage(Page<OmSkuClassification> page, OmSkuClassification entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<OmSkuClassification> findGrid(Page<OmSkuClassification> page, OmSkuClassification entity) {
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
