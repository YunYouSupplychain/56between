package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批次属性MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhLotHeaderMapper extends BaseMapper<BanQinCdWhLotHeader> {
    List<BanQinCdWhLotHeader> findPage(BanQinCdWhLotHeader cdWhLotHeader);

    BanQinCdWhLotHeader getByCode(@Param("lotCode") String lotCode, @Param("orgId") String orgId);

    void remove(@Param("lotCode") String lotCode, @Param("orgId") String orgId);
}