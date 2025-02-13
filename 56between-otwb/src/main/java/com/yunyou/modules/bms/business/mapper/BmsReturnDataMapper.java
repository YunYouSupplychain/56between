package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsReturnData;
import com.yunyou.modules.bms.business.entity.extend.BmsReturnDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退货数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-05
 */
@MyBatisMapper
public interface BmsReturnDataMapper extends BaseMapper<BmsReturnData> {

    List<BmsReturnData> findPage(BmsReturnDataEntity entity);

    void batchInsert(@Param("items") List<BmsReturnData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的退货数据
     *
     * @param sql 筛选条件
     * @return 退货数据
     */
    List<BmsReturnData> findCalcData(@Param("sql") String sql);
}