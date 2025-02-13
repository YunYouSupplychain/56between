package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleWvHeaderService extends CrudService<SysWmsRuleWvHeaderMapper, SysWmsRuleWvHeader> {
    @Autowired
    private SysWmsRuleWvDetailService sysWmsRuleWvDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleWvHeader> findPage(Page<SysWmsRuleWvHeader> page, SysWmsRuleWvHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRuleWvHeader> findGrid(Page<SysWmsRuleWvHeader> page, SysWmsRuleWvHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRuleWvHeader> findSync(SysWmsRuleWvHeader entity) {
        List<SysWmsRuleWvHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setRuleWvDetailList(sysWmsRuleWvDetailService.findByHeaderId(o.getId())));
        return list;
    }

    @Override
    public SysWmsRuleWvHeader get(String id) {
        SysWmsRuleWvHeader sysWmsRuleWvHeader = super.get(id);
        if (sysWmsRuleWvHeader != null) {
            sysWmsRuleWvHeader.setRuleWvDetailList(sysWmsRuleWvDetailService.findByHeaderId(sysWmsRuleWvHeader.getId()));
        }
        return sysWmsRuleWvHeader;
    }

    @Override
    @Transactional
    public void save(SysWmsRuleWvHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleWvHeader entity) {
        sysWmsRuleWvDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRuleWv(entity.getRuleCode(), entity.getDataSet());
        }
    }
}