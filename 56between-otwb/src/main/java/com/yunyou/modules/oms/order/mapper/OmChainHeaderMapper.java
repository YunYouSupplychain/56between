package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmChainHeaderEntity;
import com.yunyou.modules.oms.report.entity.OmDelayOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应链订单MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-17
 */
@MyBatisMapper
public interface OmChainHeaderMapper extends BaseMapper<OmChainHeader> {

    OmChainHeaderEntity getEntity(String id);

    List<OmChainHeaderEntity> findPage(OmChainHeaderEntity entity);

    List<OmDelayOrderEntity> findDelayOrder(OmDelayOrderEntity entity);

    OmChainHeader getByCustomerNo(@Param("customerNo") String customerNo, @Param("orderType") String orderType, @Param("dataSource") String dataSource, @Param("orgId") String orgId);

    List<String> findChainIdForTimer();

    OmChainHeader getByNo(@Param("chainNo") String chainNo, @Param("orgId") String orgId);

    String isIntercepted(@Param("chainNo") String chainNo, @Param("orgId") String orgId);

    List<String> findInterceptChainIdForTimer();
}