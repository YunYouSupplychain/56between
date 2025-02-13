package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsTransportObjScope;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjScopeEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsTransportObjScopeMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务对象服务范围Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsTransportObjScopeService extends CrudService<SysTmsTransportObjScopeMapper, SysTmsTransportObjScope> {
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportObjScopeEntity> findPage(Page page, SysTmsTransportObjScopeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public SysTmsTransportObjScopeEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<SysTmsTransportObjScope> findCarrierScope(String transportObjCode, String transportScopeType, String dataSet) {
        SysTmsTransportObjScope sysTmsTransportObjScope = new SysTmsTransportObjScope();
        sysTmsTransportObjScope.setTransportObjCode(transportObjCode);
        sysTmsTransportObjScope.setTransportScopeType(transportScopeType);
        sysTmsTransportObjScope.setDataSet(dataSet);
        return mapper.findCarrierScope(sysTmsTransportObjScope);
    }

    public void saveValidator(SysTmsTransportObjScope sysTmsTransportObjScope) {
        if (StringUtils.isBlank(sysTmsTransportObjScope.getTransportObjCode())) {
            throw new TmsException("业务对象不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportObjScope.getTransportScopeCode())) {
            throw new TmsException("业务服务范围不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportObjScope.getTransportScopeType())) {
            throw new TmsException("服务范围类型不能为空");
        }
        List<SysTmsTransportObjScope> list = findList(new SysTmsTransportObjScope(sysTmsTransportObjScope.getTransportScopeCode(), sysTmsTransportObjScope.getTransportObjCode(), sysTmsTransportObjScope.getTransportScopeType(), sysTmsTransportObjScope.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsTransportObjScope.getId()))) {
                throw new TmsException("业务对象[" + sysTmsTransportObjScope.getTransportObjCode() + "]业务服务范围[" + sysTmsTransportObjScope.getTransportScopeCode() + "]服务范围类型[" + sysTmsTransportObjScope.getTransportScopeType() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsTransportObjScope entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.sync(entity);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsTransportObjScope entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeTransportObjScope(entity.getTransportObjCode(), entity.getTransportScopeCode(), entity.getOldTransportScopeType(), entity.getDataSet());
        }
    }
}