package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsBusinessRoute;
import com.yunyou.modules.sys.common.entity.extend.SysTmsBusinessRouteEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsBusinessRouteMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SysTmsBusinessRouteService extends CrudService<SysTmsBusinessRouteMapper, SysTmsBusinessRoute> {
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsBusinessRouteEntity> findPage(Page page, SysTmsBusinessRouteEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsBusinessRouteEntity> findGrid(Page page, SysTmsBusinessRouteEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findGrid(qEntity));
        return page;
    }

    public SysTmsBusinessRouteEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public SysTmsBusinessRouteEntity getByCode(String code, String dataSet) {
        return mapper.getByCode(code, dataSet);
    }

    @Override
    @Transactional
    public void save(SysTmsBusinessRoute entity) {
        if (StringUtils.isBlank(entity.getCode())) {
            throw new TmsException("编码不能为空");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw new TmsException("路线不能为空");
        }
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsBusinessRoute entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeBusinessRoute(entity.getCode(), entity.getDataSet());
        }
    }
}
