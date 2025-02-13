package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToOmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysOmsClerk;
import com.yunyou.modules.sys.common.mapper.SysOmsClerkMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务员Service
 */
@Service
@Transactional(readOnly = true)
public class SysOmsClerkService extends CrudService<SysOmsClerkMapper, SysOmsClerk> {
    @Autowired
    @Lazy
    private SyncPlatformDataToOmsAction syncPlatformDataToOmsAction;

    public Page<SysOmsClerk> findPage(Page<SysOmsClerk> page, SysOmsClerk entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysOmsClerk> findGrid(Page<SysOmsClerk> page, SysOmsClerk entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysOmsClerk findByCode(String code, String dataSet) {
        List<SysOmsClerk> list = mapper.findList(new SysOmsClerk(null, code, dataSet));
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return new SysOmsClerk();
        }
    }

    @Override
    @Transactional
    public void save(SysOmsClerk entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysOmsClerk entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToOmsAction.removeClerk(entity.getClerkCode(), entity.getDataSet());
        }
    }
}