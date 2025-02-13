package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleAllocHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleAllocDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分配规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleAllocDetailService extends CrudService<SysWmsRuleAllocDetailMapper, SysWmsRuleAllocDetail> {
    @Autowired
    @Lazy
    private SysWmsRuleAllocHeaderService sysWmsRuleAllocHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    @Transactional
    public void save(SysWmsRuleAllocDetail entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleAllocHeaderService.findSync(new SysWmsRuleAllocHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleAllocDetail entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleAllocHeaderService.findSync(new SysWmsRuleAllocHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }
}