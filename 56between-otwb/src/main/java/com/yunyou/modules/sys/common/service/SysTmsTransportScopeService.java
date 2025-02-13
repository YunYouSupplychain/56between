package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsTransportScope;
import com.yunyou.modules.sys.common.mapper.SysTmsTransportScopeMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务范围Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsTransportScopeService extends CrudService<SysTmsTransportScopeMapper, SysTmsTransportScope> {
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    public Page<SysTmsTransportScope> findPage(Page<SysTmsTransportScope> page, SysTmsTransportScope entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysTmsTransportScope> findGrid(Page<SysTmsTransportScope> page, SysTmsTransportScope entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysTmsTransportScope> findSync(SysTmsTransportScope entity) {
        List<SysTmsTransportScope> list = mapper.findSync(entity);
        list.forEach(o -> o.setAreaList(mapper.findArea(o.getId())));
        return list;
    }

    @Override
    public SysTmsTransportScope get(String id) {
        SysTmsTransportScope entity = super.get(id);
        if (entity != null) {
            entity.setAreaList(mapper.findArea(id));
        }
        return entity;
    }

    public List<Area> findAreaByScopeCode(String code, String dataSet) {
        return mapper.findAreaByScopeCode(code, dataSet);
    }

    public void saveValidator(SysTmsTransportScope sysTmsTransportScope) {
        if (StringUtils.isBlank(sysTmsTransportScope.getCode())) {
            throw new TmsException("服务范围编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportScope.getName())) {
            throw new TmsException("服务范围名称不能为空");
        }
        List<SysTmsTransportScope> list = findList(new SysTmsTransportScope(sysTmsTransportScope.getCode(), sysTmsTransportScope.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsTransportScope.getId()))) {
                throw new TmsException("服务范围编码[" + sysTmsTransportScope.getCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsTransportScope entity) {
        mapper.deleteAreaByScope(entity.getId());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeTransportScope(entity.getCode(), entity.getDataSet());
        }
    }

    @Transactional
    public void saveArea(SysTmsTransportScope entity) {
        mapper.deleteAreaByScope(entity.getId());
        if (CollectionUtil.isNotEmpty(entity.getAreaList())) {
            List<Area> areaList = entity.getAreaList();
            int size = entity.getAreaList().size();
            for (int i = 0; i < size; i = i + 999) {
                if (size >= i + 999) {
                    entity.setAreaList(areaList.subList(i, i + 999));
                } else {
                    entity.setAreaList(areaList.subList(i, size));
                }
                mapper.insertArea(entity);
            }
        }
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

}