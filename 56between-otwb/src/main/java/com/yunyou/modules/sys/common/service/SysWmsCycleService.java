package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsCycle;
import com.yunyou.modules.sys.common.mapper.SysWmsCycleMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 循环级别Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsCycleService extends CrudService<SysWmsCycleMapper, SysWmsCycle> {
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    public Page<SysWmsCycle> findPage(Page<SysWmsCycle> page, SysWmsCycle entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsCycle> findGrid(Page<SysWmsCycle> page, SysWmsCycle entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(SysWmsCycle entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsCycle entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeCycle(entity.getCycleCode(), entity.getDataSet());
        }
    }
}