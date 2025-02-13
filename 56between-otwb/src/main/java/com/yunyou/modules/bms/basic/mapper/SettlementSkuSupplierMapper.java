package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.SettlementSkuSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算商品-供应商MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
@MyBatisMapper
public interface SettlementSkuSupplierMapper extends BaseMapper<SettlementSkuSupplier> {

    List<SettlementSkuSupplier> findBySkuId(@Param("skuId") String skuId, @Param("orgId") String orgId);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<SettlementSkuSupplier> list);
}