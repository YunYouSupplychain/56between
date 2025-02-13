package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsSku;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算商品MAPPER接口
 */
@MyBatisMapper
public interface SysBmsSkuMapper extends BaseMapper<SysBmsSku> {

    SysBmsSkuEntity getEntity(String id);

    SysBmsSku getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

    List<SysBmsSku> findPage(SysBmsSku entity);

    List<SysBmsSkuEntity> findGrid(SysBmsSkuEntity entity);

    void batchInsert(List<SysBmsSku> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}