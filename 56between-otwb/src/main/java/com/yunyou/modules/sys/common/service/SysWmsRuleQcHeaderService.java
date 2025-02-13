package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcClass;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleQcHeaderMapper;
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
public class SysWmsRuleQcHeaderService extends CrudService<SysWmsRuleQcHeaderMapper, SysWmsRuleQcHeader> {
    @Autowired
    private SysWmsRuleQcDetailService sysWmsRuleQcDetailService;
    @Autowired
    private SysWmsRuleQcClassService sysWmsRuleQcClassService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleQcHeader> findPage(Page<SysWmsRuleQcHeader> page, SysWmsRuleQcHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRuleQcHeader> findGrid(Page<SysWmsRuleQcHeader> page, SysWmsRuleQcHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRuleQcHeader> findSync(SysWmsRuleQcHeader entity) {
        List<SysWmsRuleQcHeader> list = mapper.findSync(entity);
        list.forEach(o -> {
            o.setRuleQcDetailList(sysWmsRuleQcDetailService.findList(new SysWmsRuleQcDetail(o.getId(), o.getDataSet())));
            o.setRuleQcClassList(sysWmsRuleQcClassService.findList(new SysWmsRuleQcClass(o.getId(), o.getDataSet())));
        });
        return list;
    }

    public SysWmsRuleQcHeader getByCode(String ruleCode, String dataSet) {
        return mapper.getByCode(ruleCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsRuleQcHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleQcHeader entity) {
        sysWmsRuleQcDetailService.deleteByHeaderId(entity.getId());
        sysWmsRuleQcClassService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRuleQc(entity.getRuleCode(), entity.getDataSet());
        }
    }
}