package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhAreaMapper extends BaseMapper<BanQinCdWhArea> {
    List<BanQinCdWhArea> findPage(BanQinCdWhArea cdWhArea);

    BanQinCdWhArea getByCode(@Param("areaCode") String areaCode, @Param("orgId") String orgId);

    void remove(@Param("areaCode") String areaCode, @Param("orgId") String orgId);
}