package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSparePartInv;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePartInvEntity;
import com.yunyou.modules.tms.spare.mapper.TmSparePartInvMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSparePartInvService extends CrudService<TmSparePartInvMapper, TmSparePartInv> {

    public TmSparePartInvEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmSparePartInv getOnly(String barcode, String baseOrgId) {
        return mapper.getOnly(barcode, baseOrgId);
    }

    public TmSparePartInvEntity getOnlyEntity(String barcode, String baseOrgId) {
        return mapper.getOnlyEntity(barcode, baseOrgId);
    }

    public void deleteByNo(String barcode, String baseOrgId) {
        mapper.deleteByNo(barcode, baseOrgId);
    }

    @SuppressWarnings("unchecked")
    public Page<TmSparePartInvEntity> findPage(Page page, TmSparePartInvEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }
}
