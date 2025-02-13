package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysOmsBusinessOrderTypeRelation;
import com.yunyou.modules.sys.common.mapper.SysOmsBusinessOrderTypeRelationMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务类型-订单类型关联关系Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsBusinessOrderTypeRelationService extends CrudService<SysOmsBusinessOrderTypeRelationMapper, SysOmsBusinessOrderTypeRelation> {
    @Autowired
    @Lazy
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    @Override
    @Transactional
    public void save(SysOmsBusinessOrderTypeRelation entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsBusinessOrderTypeRelation entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.removeBusinessOrderTypeRelation(entity.getBusinessOrderType(), entity.getOrderType(), entity.getPushSystem(), entity.getDataSet());
        }
    }
}