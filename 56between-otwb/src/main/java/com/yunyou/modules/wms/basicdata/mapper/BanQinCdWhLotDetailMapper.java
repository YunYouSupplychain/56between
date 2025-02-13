package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批次属性MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhLotDetailMapper extends BaseMapper<BanQinCdWhLotDetail> {
    void deleteByHeaderId(String headerId);

    List<BanQinCdWhLotDetail> findPage(BanQinCdWhLotDetail cdWhLotDetail);

    void remove(@Param("lotCode") String lotCode, @Param("orgId") String orgId);
}