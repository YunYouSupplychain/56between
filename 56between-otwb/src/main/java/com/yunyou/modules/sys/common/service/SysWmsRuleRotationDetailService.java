package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleRotationHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleRotationDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存周转规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleRotationDetailService extends CrudService<SysWmsRuleRotationDetailMapper, SysWmsRuleRotationDetail> {
    @Autowired
    @Lazy
    private SysWmsRuleRotationHeaderService sysWmsRuleRotationHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsRuleRotationDetail> findPage(Page<SysWmsRuleRotationDetail> page, SysWmsRuleRotationDetail entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(SysWmsRuleRotationDetail entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleRotationHeaderService.findSync(new SysWmsRuleRotationHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleRotationDetail entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleRotationHeaderService.findSync(new SysWmsRuleRotationHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

}