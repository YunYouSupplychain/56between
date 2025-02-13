package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算模型明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-13
 */
@MyBatisMapper
public interface BmsSettleModelDetailMapper extends BaseMapper<BmsSettleModelDetail> {

    void deleteByModelCode(@Param("settleModelCode") String settleModelCode, @Param("orgId") String orgId);

    BmsSettleModelDetailEntity getEntity(String id);

    BmsSettleModelDetail getOnly(@Param("settleModelCode") String settleModelCode, @Param("contractCostItemId") String contractCostItemId, @Param("orgId") String orgId);

    List<BmsSettleModelDetailEntity> findBySettleModelCode(@Param("settleModelCode") String settleModelCode, @Param("orgId") String orgId);
}