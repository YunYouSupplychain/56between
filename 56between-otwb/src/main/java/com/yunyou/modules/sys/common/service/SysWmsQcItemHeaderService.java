package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemDetail;
import com.yunyou.modules.sys.common.entity.SysWmsQcItemHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsQcItemHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 质检项Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsQcItemHeaderService extends CrudService<SysWmsQcItemHeaderMapper, SysWmsQcItemHeader> {
    @Autowired
    private SysWmsQcItemDetailService sysWmsQcItemDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsQcItemHeader> findPage(Page<SysWmsQcItemHeader> page, SysWmsQcItemHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsQcItemHeader> findGrid(Page<SysWmsQcItemHeader> page, SysWmsQcItemHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsQcItemHeader> findSync(SysWmsQcItemHeader entity) {
        List<SysWmsQcItemHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setQcItemDetailList(sysWmsQcItemDetailService.findList(new SysWmsQcItemDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    public SysWmsQcItemHeader getByCode(String itemGroupCode, String dataSet) {
        return mapper.getByCode(itemGroupCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsQcItemHeader entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToWmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsQcItemHeader entity) {
        sysWmsQcItemDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeQcItem(entity.getItemGroupCode(), entity.getDataSet());
        }
    }
}