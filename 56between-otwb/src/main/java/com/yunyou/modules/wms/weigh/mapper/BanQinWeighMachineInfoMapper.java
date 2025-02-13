package com.yunyou.modules.wms.weigh.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfo;
import com.yunyou.modules.wms.weigh.entity.BanQinWeighMachineInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 称重设备表MAPPER接口
 * @author zyf
 * @version 2019-09-21
 */
@MyBatisMapper
public interface BanQinWeighMachineInfoMapper extends BaseMapper<BanQinWeighMachineInfo> {

    BanQinWeighMachineInfoEntity getEntity(String id);

    List<BanQinWeighMachineInfoEntity> findPage(BanQinWeighMachineInfoEntity entity);

    BanQinWeighMachineInfoEntity getBySN(@Param("machineNo") String machineNo);

    void remove(@Param("machineNo") String machineNo, @Param("orgId") String orgId);

}