package com.yunyou.modules.sys.common.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRulePaDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上架规则明细Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRulePaDetailService extends CrudService<SysWmsRulePaDetailMapper, SysWmsRulePaDetail> {
    @Autowired
    @Lazy
    private SysWmsRulePaHeaderService sysWmsRulePaHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    @Transactional
    public void save(SysWmsRulePaDetail entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRulePaHeaderService.findSync(new SysWmsRulePaHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRulePaDetail entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRulePaHeaderService.findSync(new SysWmsRulePaHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }
}