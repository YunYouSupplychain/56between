package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库位MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhLocMapper extends BaseMapper<BanQinCdWhLoc> {
    List<BanQinCdWhLoc> findPage(BanQinCdWhLoc cdWhLoc);

    List<BanQinCdWhLoc> getExistLoc(@Param("locCodeList") List<String> locCodeList, @Param("orgId") String orgId);

    BanQinCdWhLoc getByCode(@Param("locCode") String locCode, @Param("orgId") String orgId);

    void remove(@Param("locCode") String locCode, @Param("orgId") String orgId);
}