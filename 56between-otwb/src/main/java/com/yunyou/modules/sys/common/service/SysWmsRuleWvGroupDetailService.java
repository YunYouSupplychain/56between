package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvGroupDetailMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则组明细Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleWvGroupDetailService extends CrudService<SysWmsRuleWvGroupDetailMapper, SysWmsRuleWvGroupDetail> {
    @Autowired
    @Lazy
    private SysWmsRuleWvGroupHeaderService sysWmsRuleWvGroupHeaderService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public void batchSave(List<SysWmsRuleWvGroupDetail> wvGroupDetailList) {
        for (SysWmsRuleWvGroupDetail detail : wvGroupDetailList) {
            this.save(detail);
        }
        if (CollectionUtil.isNotEmpty(wvGroupDetailList)) {
            final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
            if ("Y".equals(IS_SAVING_PUSH)) {
                sysWmsRuleWvGroupHeaderService.findSync(new SysWmsRuleWvGroupHeader(wvGroupDetailList.get(0).getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
            }
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsRuleWvGroupDetail entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysWmsRuleWvGroupHeaderService.findSync(new SysWmsRuleWvGroupHeader(entity.getHeaderId())).forEach(syncPlatformDataToWmsAction::sync);
        }
    }
}