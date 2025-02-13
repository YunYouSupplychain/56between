package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmSaleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售订单明细表MAPPER接口
 * @author WMJ
 * @version 2019-04-17
 */
@MyBatisMapper
public interface OmSaleDetailMapper extends BaseMapper<OmSaleDetail> {

    List<OmSaleDetail> findDetailsByOwnerAndSku(@Param("owner") String owner, @Param("skuCode") String skuCode, @Param("warehouse") String warehouse, @Param("status") String status);

    void saveAll(List<OmSaleDetail> list);

    List<OmSaleDetail> findDetailList(OmSaleDetail entity);
}