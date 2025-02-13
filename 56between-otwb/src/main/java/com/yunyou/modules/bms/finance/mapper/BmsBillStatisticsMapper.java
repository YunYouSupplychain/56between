package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillStatisticsEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 费用统计MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@MyBatisMapper
public interface BmsBillStatisticsMapper extends BaseMapper<BmsBillStatistics> {

    List<BmsBillStatistics> findPage(BmsBillStatisticsEntity entity);

    Map<String, BigDecimal> getTotal(BmsBillStatisticsEntity entity);

    void batchInsert(@Param("items") List<BmsBillStatistics> items);

    void batchDelete(@Param("ids") List<String> ids);

    void deleteByConfirmNo(@Param("confirmNo") String confirmNo, @Param("orgId") String orgId);
}