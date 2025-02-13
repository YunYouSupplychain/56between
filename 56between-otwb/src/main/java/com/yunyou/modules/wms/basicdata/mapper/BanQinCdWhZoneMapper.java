package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhZone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库区MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhZoneMapper extends BaseMapper<BanQinCdWhZone> {
    List<BanQinCdWhZone> findPage(BanQinCdWhZone cdWhZone);

    void remove(@Param("zoneCode") String zoneCode, @Param("orgId") String orgId);
}