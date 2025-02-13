package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsInboundData;
import com.yunyou.modules.bms.business.entity.extend.BmsInboundDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@MyBatisMapper
public interface BmsInboundDataMapper extends BaseMapper<BmsInboundData> {

    List<BmsInboundData> findPage(BmsInboundDataEntity entity);

    void batchInsert(@Param("items") List<BmsInboundData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的入库数据
     *
     * @param sql 筛选条件
     * @return 入库数据
     */
    List<BmsInboundData> findCalcData(@Param("sql") String sql);
}