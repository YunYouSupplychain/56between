package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsSkuSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算商品-供应商MAPPER接口
 */
@MyBatisMapper
public interface SysBmsSkuSupplierMapper extends BaseMapper<SysBmsSkuSupplier> {

    List<SysBmsSkuSupplier> findBySkuId(@Param("skuId") String skuId, @Param("dataSet") String dataSet);

    void deleteByHeadId(@Param("skuId") String skuId);

    void batchInsert(List<SysBmsSkuSupplier> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}