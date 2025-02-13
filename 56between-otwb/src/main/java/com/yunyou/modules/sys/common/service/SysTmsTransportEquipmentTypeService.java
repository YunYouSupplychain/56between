package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentSpace;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentType;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportEquipmentTypeEntity;
import com.yunyou.modules.sys.common.mapper.SysTmsTransportEquipmentTypeMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输设备类型Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsTransportEquipmentTypeService extends CrudService<SysTmsTransportEquipmentTypeMapper, SysTmsTransportEquipmentType> {
    @Autowired
    @Lazy
    private SysTmsTransportEquipmentSpaceService sysTmsTransportEquipmentSpaceService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportEquipmentTypeEntity> findPage(Page page, SysTmsTransportEquipmentTypeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<SysTmsTransportEquipmentTypeEntity> findGrid(Page page, SysTmsTransportEquipmentTypeEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public List<SysTmsTransportEquipmentType> findSync(SysTmsTransportEquipmentType entity) {
        List<SysTmsTransportEquipmentType> list = mapper.findSync(entity);
        list.forEach(o -> o.setTransportEquipmentSpaceList(sysTmsTransportEquipmentSpaceService.findList(new SysTmsTransportEquipmentSpace(o.getTransportEquipmentTypeCode(), o.getDataSet()))));
        return list;
    }

    public SysTmsTransportEquipmentTypeEntity getEntity(String id) {
        SysTmsTransportEquipmentTypeEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTransportEquipmentSpaceList(sysTmsTransportEquipmentSpaceService.findList(new SysTmsTransportEquipmentSpace(entity.getTransportEquipmentTypeCode(), entity.getDataSet())));
        }
        return entity;
    }

    public void saveValidator(SysTmsTransportEquipmentType sysTmsTransportEquipmentType) {
        if (StringUtils.isBlank(sysTmsTransportEquipmentType.getTransportEquipmentTypeCode())) {
            throw new TmsException("运输设备类型编码不能为空");
        }
        List<SysTmsTransportEquipmentType> list = this.findList(new SysTmsTransportEquipmentType(sysTmsTransportEquipmentType.getTransportEquipmentTypeCode(), sysTmsTransportEquipmentType.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsTransportEquipmentType.getId()))) {
                throw new TmsException("运输设备类型编码[" + sysTmsTransportEquipmentType.getTransportEquipmentTypeCode() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsTransportEquipmentType entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            this.findSync(entity).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Transactional
    public void delete(SysTmsTransportEquipmentType entity) {
        sysTmsTransportEquipmentSpaceService.deleteByEquipmentType(entity.getTransportEquipmentTypeCode(), entity.getDataSet());
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            syncPlatformDataToTmsAction.removeEquipmentType(entity.getTransportEquipmentTypeCode(), entity.getDataSet());
        }
    }

}