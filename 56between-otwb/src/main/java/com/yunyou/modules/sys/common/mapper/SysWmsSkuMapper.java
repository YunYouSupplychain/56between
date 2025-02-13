package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsSku;
import com.yunyou.modules.sys.common.entity.extend.SysWmsSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品MAPPER接口
 */
@MyBatisMapper
public interface SysWmsSkuMapper extends BaseMapper<SysWmsSku> {
    SysWmsSkuEntity getEntity(String id);

    List<SysWmsSkuEntity> findPage(SysWmsSkuEntity sysWmsSkuEntity);

    List<SysWmsSkuEntity> findGrid(SysWmsSkuEntity sysWmsSkuEntity);

    SysWmsSku getByOwnerAndSkuCode(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

    void batchInsert(List<SysWmsSku> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}