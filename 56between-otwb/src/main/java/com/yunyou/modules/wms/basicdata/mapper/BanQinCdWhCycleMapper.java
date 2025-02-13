package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhCycle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 循环级别MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhCycleMapper extends BaseMapper<BanQinCdWhCycle> {
    List<BanQinCdWhCycle> findPage(BanQinCdWhCycle cdWhCycle);

    BanQinCdWhCycle getByCode(@Param("cycleCode") String cycleCode, @Param("orgId") String orgId);

    void remove(@Param("cycleCode") String cycleCode, @Param("orgId") String orgId);
}