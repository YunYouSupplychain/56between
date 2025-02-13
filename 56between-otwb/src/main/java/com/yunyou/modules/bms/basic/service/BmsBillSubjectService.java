package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillSubject;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillSubjectEntity;
import com.yunyou.modules.bms.basic.mapper.BmsBillSubjectMapper;
import com.yunyou.modules.bms.common.BmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 费用科目Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsBillSubjectService extends CrudService<BmsBillSubjectMapper, BmsBillSubject> {

    @SuppressWarnings("unchecked")
    public Page<BmsBillSubjectEntity> findPage(Page page, BmsBillSubject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<BmsBillSubject> findGrid(Page<BmsBillSubject> page, BmsBillSubjectEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public BmsBillSubject getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    public void checkSaveBefore(BmsBillSubject bmsBillSubject) {
        BmsBillSubject billSubject = getByCode(bmsBillSubject.getBillSubjectCode(), bmsBillSubject.getOrgId());
        if (billSubject != null && !billSubject.getId().equals(bmsBillSubject.getId())) {
            throw new BmsException("费用科目代码已存在");
        }
    }

    @Transactional
    public void remove(String billSubjectCode, String orgId) {
        mapper.remove(billSubjectCode, orgId);
    }
}