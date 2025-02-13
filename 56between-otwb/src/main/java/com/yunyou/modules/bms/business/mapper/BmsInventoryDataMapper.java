package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsInventoryData;
import com.yunyou.modules.bms.business.entity.extend.BmsInventoryDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@MyBatisMapper
public interface BmsInventoryDataMapper extends BaseMapper<BmsInventoryData> {

    List<BmsInventoryData> findPage(BmsInventoryDataEntity entity);

    void batchInsert(@Param("items") List<BmsInventoryData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的库存数据
     *
     * @param sql 筛选条件
     * @return 库存数据
     */
    List<BmsInventoryData> findCalcData(@Param("sql") String sql);
}