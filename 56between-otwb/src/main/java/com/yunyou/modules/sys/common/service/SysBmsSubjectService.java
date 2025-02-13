package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysBmsSubject;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSubjectEntity;
import com.yunyou.modules.sys.common.mapper.SysBmsSubjectMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 费用科目Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsSubjectService extends CrudService<SysBmsSubjectMapper, SysBmsSubject> {
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysBmsSubjectEntity> findPage(Page page, SysBmsSubjectEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysBmsSubjectEntity> findGrid(Page page, SysBmsSubjectEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysBmsSubject getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }

    public void checkSaveBefore(SysBmsSubject bmsBillSubject) {
        SysBmsSubject billSubject = getByCode(bmsBillSubject.getBillSubjectCode(), bmsBillSubject.getDataSet());
        if (billSubject != null && !billSubject.getId().equals(bmsBillSubject.getId())) {
            throw new BmsException("费用科目代码已存在");
        }
    }

    @Override
    @Transactional
    public void save(SysBmsSubject entity) {
        this.checkSaveBefore(entity);
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysBmsSubject entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.removeSubject(entity.getBillSubjectCode(), entity.getDataSet());
        }
    }
}