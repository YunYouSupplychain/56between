package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsWeighMachineInfo;
import com.yunyou.modules.sys.common.entity.extend.SysWmsWeighMachineInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 称重设备表MAPPER接口
 */
@MyBatisMapper
public interface SysWmsWeighMachineInfoMapper extends BaseMapper<SysWmsWeighMachineInfo> {

    SysWmsWeighMachineInfoEntity getEntity(String id);

    List<SysWmsWeighMachineInfoEntity> findPage(SysWmsWeighMachineInfoEntity entity);

    SysWmsWeighMachineInfoEntity getBySN(@Param("machineNo") String machineNo);

}