package com.yunyou.modules.sys.common.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToWmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysWmsZone;
import com.yunyou.modules.sys.common.mapper.SysWmsZoneMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库区Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsZoneService extends CrudService<SysWmsZoneMapper, SysWmsZone> {
    @Autowired
    @Lazy
    private SyncPlatformDataToWmsAction syncPlatformDataToWmsAction;

    public Page<SysWmsZone> findPage(Page<SysWmsZone> page, SysWmsZone entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysWmsZone> findGrid(Page<SysWmsZone> page, SysWmsZone entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysWmsZone getByCode(String zoneCode, String dataSet) {
        return mapper.getByCode(zoneCode, dataSet);
    }

    @Override
    @Transactional
    public void save(SysWmsZone entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysWmsZone entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToWmsAction.removeZone(entity.getZoneCode(), entity.getDataSet());
        }
    }

    @Transactional
    public void importFile(List<SysWmsZone> importList, String dataSet) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            SysWmsZone data = importList.get(i);
            if (StringUtils.isBlank(data.getZoneCode())) {
                throw new GlobalException("第" + (i + 1) + "行，库区编码不能为空！");
            }
            if (StringUtils.isBlank(data.getZoneName())) {
                throw new GlobalException("第" + (i + 1) + "行，库区名称不能为空！");
            }
            if (StringUtils.isBlank(data.getType())) {
                throw new GlobalException("第" + (i + 1) + "行，库区类型不能为空！");
            }
            if (StringUtils.isBlank(data.getAreaCode())) {
                throw new GlobalException("第" + (i + 1) + "行，区域编码不能为空！");
            }
            SysWmsZone sysWmsZone = this.getByCode(data.getZoneCode(), dataSet);
            if (sysWmsZone != null) {
                throw new GlobalException("第" + (i + 1) + "行，库区已存在！");
            }
            data.setDataSet(dataSet);
            this.save(data);
        }
    }
}