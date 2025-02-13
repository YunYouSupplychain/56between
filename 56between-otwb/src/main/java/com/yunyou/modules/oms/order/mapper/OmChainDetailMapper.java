package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 供应链订单明细表MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-17
 */
@MyBatisMapper
public interface OmChainDetailMapper extends BaseMapper<OmChainDetail> {

    Integer getMaxLineNo(@Param("chainId") String chainId, @Param("orgId") String orgId);

    OmChainDetail getByLineNo(@Param("chainNo") String chainNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);
}