package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderSiteData;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：派车单门店配送数据Mapper
 */
@MyBatisMapper
public interface BmsDispatchOrderSiteDataMapper extends BaseMapper<BmsDispatchOrderSiteData> {

    List<BmsDispatchOrderSiteData> findByNo(@Param("orderNo") String orderNo, @Param("orgId") String orgId, @Param("dataSources") String dataSources);

    List<BmsDispatchOrderSiteData> findByHeaderId(@Param("headerId") String headerId, @Param("orgId") String orgId);

    void deleteByCondition(BmsPullDataCondition condition);
}
