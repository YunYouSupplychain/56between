package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.CdWhSkuClassification;
import com.yunyou.modules.wms.basicdata.mapper.CdWhSkuClassificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CdWhSkuClassificationService extends CrudService<CdWhSkuClassificationMapper, CdWhSkuClassification> {

    @Override
    public Page<CdWhSkuClassification> findPage(Page<CdWhSkuClassification> page, CdWhSkuClassification entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<CdWhSkuClassification> findGrid(Page<CdWhSkuClassification> page, CdWhSkuClassification entity) {
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
