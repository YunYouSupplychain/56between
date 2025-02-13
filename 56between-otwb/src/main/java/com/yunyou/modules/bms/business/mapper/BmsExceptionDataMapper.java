package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsExceptionData;
import com.yunyou.modules.bms.business.entity.extend.BmsExceptionDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@MyBatisMapper
public interface BmsExceptionDataMapper extends BaseMapper<BmsExceptionData> {

    List<BmsExceptionData> findPage(BmsExceptionDataEntity entity);

    void batchInsert(@Param("items") List<BmsExceptionData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的异常数据
     *
     * @param sql 筛选条件
     * @return 异常数据
     */
    List<BmsExceptionData> findCalcData(@Param("sql") String sql);
}