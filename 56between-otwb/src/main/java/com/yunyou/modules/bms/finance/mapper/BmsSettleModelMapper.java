package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsSettleModel;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算模型MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-13
 */
@MyBatisMapper
public interface BmsSettleModelMapper extends BaseMapper<BmsSettleModel> {

    List<BmsSettleModel> findPage(BmsSettleModelEntity entity);

    BmsSettleModelEntity getEntity(String id);

    BmsSettleModelEntity getEntityByCode(@Param("settleModelCode") String settleModelCode, @Param("orgId") String orgId);

    List<BmsSettleModelDetailEntity> findContractSubject(BmsSettleModelEntity entity);
}