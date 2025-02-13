package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchOrderDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：派车单数据Mapper
 */
@MyBatisMapper
public interface BmsDispatchOrderDataMapper extends BaseMapper<BmsDispatchOrderData> {

    List<BmsDispatchOrderData> findPage(BmsDispatchOrderDataEntity entity);

    BmsDispatchOrderDataEntity getEntity(String id);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的派车单数据据
     *
     * @param sql 筛选条件
     * @return 派车单数据
     */
    List<BmsDispatchOrderData> findCalcData(@Param("sql") String sql);
}
