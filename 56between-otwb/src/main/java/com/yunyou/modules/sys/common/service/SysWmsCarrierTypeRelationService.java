package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsCarrierTypeRelation;
import com.yunyou.modules.sys.common.entity.extend.SysWmsCarrierTypeRelationEntity;
import com.yunyou.modules.sys.common.mapper.SysWmsCarrierTypeRelationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 承运商类型关系Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsCarrierTypeRelationService extends CrudService<SysWmsCarrierTypeRelationMapper, SysWmsCarrierTypeRelation> {
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysWmsCarrierTypeRelationEntity> findPage(Page page, SysWmsCarrierTypeRelationEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysWmsCarrierTypeRelationEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @Override
    @Transactional
    public void save(SysWmsCarrierTypeRelation entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsCarrierTypeRelation entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeCarrierTypeRelation(entity.getCarrierCode(), entity.getDataSet());
        }
    }
}