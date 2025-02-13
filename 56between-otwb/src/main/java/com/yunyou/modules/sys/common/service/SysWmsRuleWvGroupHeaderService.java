package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvGroupHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则组Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleWvGroupHeaderService extends CrudService<SysWmsRuleWvGroupHeaderMapper, SysWmsRuleWvGroupHeader> {
    @Autowired
    private SysWmsRuleWvGroupDetailService sysWmsRuleWvGroupDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleWvGroupHeader> findPage(Page<SysWmsRuleWvGroupHeader> page, SysWmsRuleWvGroupHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRuleWvGroupHeader> findGrid(Page<SysWmsRuleWvGroupHeader> page, SysWmsRuleWvGroupHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRuleWvGroupHeader> findSync(SysWmsRuleWvGroupHeader entity) {
        List<SysWmsRuleWvGroupHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setWvGroupDetailList(sysWmsRuleWvGroupDetailService.findList(new SysWmsRuleWvGroupDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    @Override
    @Transactional
    public void save(SysWmsRuleWvGroupHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleWvGroupHeader entity) {
        sysWmsRuleWvGroupDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRuleWvGroup(entity.getGroupCode(), entity.getDataSet());
        }
    }

}