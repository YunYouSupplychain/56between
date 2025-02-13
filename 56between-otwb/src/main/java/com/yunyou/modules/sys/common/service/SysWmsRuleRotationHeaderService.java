package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleRotationHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存周转规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleRotationHeaderService extends CrudService<SysWmsRuleRotationHeaderMapper, SysWmsRuleRotationHeader> {
    @Autowired
    private SysWmsRuleRotationDetailService sysWmsRuleRotationDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleRotationHeader> findPage(Page<SysWmsRuleRotationHeader> page, SysWmsRuleRotationHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsRuleRotationHeader> findGrid(Page<SysWmsRuleRotationHeader> page, SysWmsRuleRotationHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsRuleRotationHeader> findSync(SysWmsRuleRotationHeader entity) {
        List<SysWmsRuleRotationHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setRuleRotationDetailList(sysWmsRuleRotationDetailService.findList(new SysWmsRuleRotationDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    public SysWmsRuleRotationHeader getByCode(String ruleCode, String dataSet) {
        return mapper.getByCode(ruleCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsRuleRotationHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleRotationHeader entity) {
        sysWmsRuleRotationDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeRuleRotation(entity.getRuleCode(), entity.getDataSet());
        }
    }
}