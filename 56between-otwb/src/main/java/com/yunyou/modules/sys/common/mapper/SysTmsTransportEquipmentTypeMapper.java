package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsTransportEquipmentType;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportEquipmentTypeEntity;

import java.util.List;

/**
 * 运输设备类型MAPPER接口
 */
@MyBatisMapper
public interface SysTmsTransportEquipmentTypeMapper extends BaseMapper<SysTmsTransportEquipmentType> {

    SysTmsTransportEquipmentTypeEntity getEntity(String id);

    List<SysTmsTransportEquipmentTypeEntity> findPage(SysTmsTransportEquipmentTypeEntity entity);

    List<SysTmsTransportEquipmentTypeEntity> findGrid(SysTmsTransportEquipmentTypeEntity entity);

    List<SysTmsTransportEquipmentType> findSync(SysTmsTransportEquipmentType entity);
}