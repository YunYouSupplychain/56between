package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmTransportEquipmentType;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportEquipmentTypeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运输设备类型MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmTransportEquipmentTypeMapper extends BaseMapper<TmTransportEquipmentType> {

    TmTransportEquipmentTypeEntity getEntity(String id);

    List<TmTransportEquipmentTypeEntity> findPage(TmTransportEquipmentType tmTransportEquipmentType);

    List<TmTransportEquipmentTypeEntity> findGrid(TmTransportEquipmentTypeEntity entity);

    TmTransportEquipmentType getByCode(@Param("transportEquipmentTypeCode") String transportEquipmentTypeCode, @Param("orgId") String orgId);

    void remove(@Param("transportEquipmentTypeCode") String transportEquipmentTypeCode, @Param("orgId") String orgId);
}