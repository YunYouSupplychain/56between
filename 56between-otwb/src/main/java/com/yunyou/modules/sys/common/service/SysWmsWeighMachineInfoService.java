package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsWeighMachineInfo;
import com.yunyou.modules.sys.common.entity.extend.SysWmsWeighMachineInfoEntity;
import com.yunyou.modules.sys.common.mapper.SysWmsWeighMachineInfoMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 称重设备表Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsWeighMachineInfoService extends CrudService<SysWmsWeighMachineInfoMapper, SysWmsWeighMachineInfo> {
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysWmsWeighMachineInfoEntity> findPage(Page page, SysWmsWeighMachineInfoEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysWmsWeighMachineInfoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public SysWmsWeighMachineInfoEntity getBySN(String machineNo) {
        return mapper.getBySN(machineNo);
    }

    @Override
    @Transactional
    public void save(SysWmsWeighMachineInfo entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsWeighMachineInfo entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeWeighMachine(entity.getMachineNo(), entity.getOrgId(), entity.getDataSet());
        }
    }
}