package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 派车配载数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-07-16
 */
@MyBatisMapper
public interface BmsDispatchDataMapper extends BaseMapper<BmsDispatchData> {

    List<BmsDispatchData> findPage(BmsDispatchDataEntity entity);

    void batchInsert(@Param("items") List<BmsDispatchData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的配载数据
     *
     * @param sql 筛选条件
     * @return 配载数据
     */
    List<BmsDispatchData> findCalcData(@Param("sql") String sql);
}