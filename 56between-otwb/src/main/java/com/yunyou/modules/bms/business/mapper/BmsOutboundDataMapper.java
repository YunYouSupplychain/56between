package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsOutboundData;
import com.yunyou.modules.bms.business.entity.extend.BmsOutboundDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@MyBatisMapper
public interface BmsOutboundDataMapper extends BaseMapper<BmsOutboundData> {

    List<BmsOutboundData> findPage(BmsOutboundDataEntity entity);

    void batchInsert(@Param("items") List<BmsOutboundData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的出库数据
     *
     * @param sql 筛选条件
     * @return 出库数据
     */
    List<BmsOutboundData> findCalcData(@Param("sql") String sql);
}