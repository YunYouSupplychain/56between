package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentSpace;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentType;
import com.yunyou.modules.sys.common.mapper.SysTmsTransportEquipmentSpaceMapper;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输设备空间信息Service
 */
@Service
@Transactional(readOnly = true)
public class SysTmsTransportEquipmentSpaceService extends CrudService<SysTmsTransportEquipmentSpaceMapper, SysTmsTransportEquipmentSpace> {
    @Autowired
    private SysTmsTransportEquipmentTypeService sysTmsTransportEquipmentTypeService;
    @Autowired
    @Lazy
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @Transactional
    public void deleteByEquipmentType(String transportEquipmentTypeCode, String dataSet) {
        mapper.deleteByEquipmentType(transportEquipmentTypeCode, dataSet);
    }

    public void saveValidator(SysTmsTransportEquipmentSpace sysTmsTransportEquipmentSpace) {
        if (StringUtils.isBlank(sysTmsTransportEquipmentSpace.getTransportEquipmentTypeCode())) {
            throw new TmsException("所属设备类型编码不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportEquipmentSpace.getTransportEquipmentNo())) {
            throw new TmsException("设备编号不能为空");
        }
        if (StringUtils.isBlank(sysTmsTransportEquipmentSpace.getTransportEquipmentNo())) {
            throw new TmsException("设备编号不能为空");
        }
        List<SysTmsTransportEquipmentSpace> list = findList(new SysTmsTransportEquipmentSpace(sysTmsTransportEquipmentSpace.getTransportEquipmentTypeCode(), sysTmsTransportEquipmentSpace.getTransportEquipmentNo(), sysTmsTransportEquipmentSpace.getDataSet()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(sysTmsTransportEquipmentSpace.getId()))) {
                throw new TmsException("设备编号[" + sysTmsTransportEquipmentSpace.getTransportEquipmentNo() + "]已存在");
            }
        }
    }

    @Override
    @Transactional
    public void save(SysTmsTransportEquipmentSpace entity) {
        super.save(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsTransportEquipmentTypeService.findSync(new SysTmsTransportEquipmentType(entity.getTransportEquipmentTypeCode(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }

    @Override
    @Transactional
    public void delete(SysTmsTransportEquipmentSpace entity) {
        super.delete(entity);
        final String IS_SAVING_PUSH = SysControlParamsUtils.getValue(SysParamConstants.IS_SAVING_PUSH);
        if ("Y".equals(IS_SAVING_PUSH)) {
            sysTmsTransportEquipmentTypeService.findSync(new SysTmsTransportEquipmentType(entity.getTransportEquipmentTypeCode(), entity.getDataSet())).forEach(syncPlatformDataToTmsAction::sync);
        }
    }
}