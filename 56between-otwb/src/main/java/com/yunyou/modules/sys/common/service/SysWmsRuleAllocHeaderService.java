package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleAllocHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分配规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleAllocHeaderService extends CrudService<SysWmsRuleAllocHeaderMapper, SysWmsRuleAllocHeader> {
    @Autowired
    private SysWmsRuleAllocDetailService sysWmsRuleAllocDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleAllocHeader> findPage(Page<SysWmsRuleAllocHeader> page, SysWmsRuleAllocHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRuleAllocHeader> findGrid(Page<SysWmsRuleAllocHeader> page, SysWmsRuleAllocHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRuleAllocHeader> findSync(SysWmsRuleAllocHeader entity) {
        List<SysWmsRuleAllocHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setRuleAllocDetailList(sysWmsRuleAllocDetailService.findList(new SysWmsRuleAllocDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    public SysWmsRuleAllocHeader getByCode(String ruleCode, String dataSet) {
        return mapper.getByCode(ruleCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsRuleAllocHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleAllocHeader entity) {
        sysWmsRuleAllocDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRuleAlloc(entity.getRuleCode(), entity.getDataSet());
        }
    }
}