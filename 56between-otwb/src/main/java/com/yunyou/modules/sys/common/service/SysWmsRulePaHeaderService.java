package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRulePaHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 质检规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRulePaHeaderService extends CrudService<SysWmsRulePaHeaderMapper, SysWmsRulePaHeader> {
    @Autowired
    private SysWmsRulePaDetailService sysWmsRulePaDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRulePaHeader> findPage(Page<SysWmsRulePaHeader> page, SysWmsRulePaHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRulePaHeader> findGrid(Page<SysWmsRulePaHeader> page, SysWmsRulePaHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRulePaHeader> findSync(SysWmsRulePaHeader entity) {
        List<SysWmsRulePaHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setRulePaDetailList(sysWmsRulePaDetailService.findList(new SysWmsRulePaDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    public SysWmsRulePaHeader getByCode(String ruleCode, String dataSet) {
        return mapper.getByCode(ruleCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsRulePaHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRulePaHeader entity) {
        sysWmsRulePaDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRulePa(entity.getRuleCode(), entity.getDataSet());
        }
    }
}