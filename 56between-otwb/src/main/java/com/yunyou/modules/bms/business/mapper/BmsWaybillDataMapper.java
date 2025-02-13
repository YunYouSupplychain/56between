package com.yunyou.modules.bms.business.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.business.entity.BmsWaybillData;
import com.yunyou.modules.bms.business.entity.extend.BmsWaybillDataEntity;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运单数据MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2021/8/31
 */
@MyBatisMapper
public interface BmsWaybillDataMapper extends BaseMapper<BmsWaybillData> {

    List<BmsWaybillData> findPage(BmsWaybillDataEntity entity);

    void batchInsert(@Param("items") List<BmsWaybillData> items);

    void deleteByCondition(BmsPullDataCondition condition);

    /**
     * 获取费用计算的运单数据
     *
     * @param sql 筛选条件
     * @return 运单数据
     */
    List<BmsWaybillData> findCalcData(@Param("sql") String sql);
}