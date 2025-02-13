package com.yunyou.modules.sys.common.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsLotDetail;
import com.yunyou.modules.sys.common.entity.SysWmsLotHeader;
import com.yunyou.modules.sys.common.mapper.SysWmsLotHeaderMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批次属性Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsLotHeaderService extends CrudService<SysWmsLotHeaderMapper, SysWmsLotHeader> {
    @Autowired
    private SysWmsLotDetailService sysWmsLotDetailService;
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    @Override
    public Page<SysWmsLotHeader> findPage(Page<SysWmsLotHeader> page, SysWmsLotHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsLotHeader> findGrid(Page<SysWmsLotHeader> page, SysWmsLotHeader entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysWmsLotHeader> findSync(SysWmsLotHeader entity) {
        List<SysWmsLotHeader> list = mapper.findSync(entity);
        list.forEach(o -> o.setCdWhLotDetailList(sysWmsLotDetailService.findList(new SysWmsLotDetail(o.getId(), o.getDataSet()))));
        return list;
    }

    public SysWmsLotHeader getByCode(String lotCode, String dataSet) {
        return mapper.getByCode(lotCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsLotHeader entity) {
        super.save(entity);
        for (SysWmsLotDetail detail : entity.getCdWhLotDetailList()) {
            detail.setHeaderId(entity.getId());
            detail.setLotCode(entity.getLotCode());
            detail.setDataSet(entity.getDataSet());
            sysWmsLotDetailService.save(detail);
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsLotHeader entity) {
        sysWmsLotDetailService.deleteByHeaderId(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeLotAtt(entity.getLotCode(), entity.getDataSet());
        }
    }
}