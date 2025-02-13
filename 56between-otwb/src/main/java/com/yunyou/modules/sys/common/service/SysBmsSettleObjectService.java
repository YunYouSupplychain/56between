package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToBmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysBmsSettleObject;
import com.yunyou.modules.sys.common.mapper.SysBmsSettleObjectMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结算对象Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsSettleObjectService extends CrudService<SysBmsSettleObjectMapper, SysBmsSettleObject> {
    @Autowired
    private SyncPlatformDataToBmsAction syncPlatformDataToBmsAction;

    @Override
    public Page<SysBmsSettleObject> findPage(Page<SysBmsSettleObject> page, SysBmsSettleObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysBmsSettleObject> findGrid(Page<SysBmsSettleObject> page, SysBmsSettleObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysBmsSettleObject getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }

    public void checkSaveBefore(SysBmsSettleObject bmsSettlementObject) {
        SysBmsSettleObject settlementObject = getByCode(bmsSettlementObject.getSettleObjectCode(), bmsSettlementObject.getDataSet());
        if (settlementObject != null && !settlementObject.getId().equals(bmsSettlementObject.getId())) {
            throw new BmsException("结算对象代码已存在");
        }
    }

    @Override
    @Transactional
    public void save(SysBmsSettleObject entity) {
        this.checkSaveBefore(entity);
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysBmsSettleObject entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToBmsAction.removeSettleObject(entity.getSettleObjectCode(), entity.getDataSet());
        }
    }
}