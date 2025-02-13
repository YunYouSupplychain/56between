package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysCommonSkuClassification;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuClassificationEntity;
import com.yunyou.modules.sys.common.mapper.SysCommonSkuClassificationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysCommonSkuClassificationService extends CrudService<SysCommonSkuClassificationMapper, SysCommonSkuClassification> {
    @Autowired
    @Lazy
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    public SysCommonSkuClassificationEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @SuppressWarnings("unchecked")
    public Page<SysCommonSkuClassificationEntity> findPage(Page page, SysCommonSkuClassificationEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysCommonSkuClassification> findGrid(Page<SysCommonSkuClassification> page, SysCommonSkuClassification entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysCommonSkuClassification> findSync(SysCommonSkuClassificationEntity qEntity) {
        return mapper.findSync(qEntity);
    }

    @Override
    @Transactional
    public void save(SysCommonSkuClassification entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.syncSkuClassification(Collections.singletonList(entity));
        }
    }

    @Override
    @Transactional
    public void delete(SysCommonSkuClassification entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataCommonAction.removeSkuClassification(entity.getCode(), entity.getDataSet());
        }
    }
}
