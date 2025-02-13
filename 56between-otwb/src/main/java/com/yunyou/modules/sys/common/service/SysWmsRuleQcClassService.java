package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcClass;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleQcClassMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.service.WmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 质检规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleQcClassService extends CrudService<SysWmsRuleQcClassMapper, SysWmsRuleQcClass> {
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    @Lazy
    private SysWmsRuleQcHeaderService sysWmsRuleQcHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Transactional
    public void save(SysWmsRuleQcClass entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setLineNo(wmsUtil.getMaxLineNo("sys_wms_rule_qc_class", "header_id", entity.getHeaderId()));
        }
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleQcHeaderService.findSync(new SysWmsRuleQcHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleQcClass entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleQcHeaderService.findSync(new SysWmsRuleQcHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

}