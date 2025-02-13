package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysOmsCarrierServiceScope;
import com.yunyou.modules.sys.common.entity.extend.SysOmsCarrierServiceScopeEntity;
import com.yunyou.modules.sys.common.mapper.SysOmsCarrierServiceScopeMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 承运商服务范围Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsCarrierServiceScopeService extends CrudService<SysOmsCarrierServiceScopeMapper, SysOmsCarrierServiceScope> {
    @Autowired
    @Lazy
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysOmsCarrierServiceScopeEntity> findPage(Page page, SysOmsCarrierServiceScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysOmsCarrierServiceScopeEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    @Override
    @Transactional
    public void save(SysOmsCarrierServiceScope entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsCarrierServiceScope entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.removeCarrierServiceScope(entity.getOwnerCode(), entity.getCarrierCode(), entity.getGroupCode(), entity.getDataSet());
        }
    }
}