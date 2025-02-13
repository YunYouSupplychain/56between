package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品费用明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-19
 */
@MyBatisMapper
public interface BmsBillDetailMapper extends BaseMapper<BmsBillDetail> {

    BmsBillDetailEntity getEntity(String id);

    List<BmsBillDetail> findPage(BmsBillDetailEntity entity);

    Map<String, BigDecimal> getTotal(BmsBillDetail entity);

    List<BmsBillDetail> findTransportPage(BmsBillDetailEntity entity);

    void batchInsert(@Param("items") List<BmsBillDetail> items);

    void batchDelete(@Param("ids") List<String> ids);

    List<BmsBillDetail> findBySettleModelAndBusinessData(@Param("settleModelCode") String settleModelCode, @Param("fmDate") Date fmDate, @Param("toDate") Date toDate, @Param("orgId") String orgId);

    List<BmsBillDetail> findByContractAndBusinessData(@Param("sysContractNo") String sysContractNo, @Param("fmDate") Date fmDate, @Param("toDate") Date toDate, @Param("orgId") String orgId);
}