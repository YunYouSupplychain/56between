package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsItem;
import com.yunyou.modules.sys.common.entity.extend.SysTmsItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsItemMapper extends BaseMapper<SysTmsItem> {

    SysTmsItemEntity getEntity(String id);

    List<SysTmsItemEntity> findPage(SysTmsItemEntity entity);

    List<SysTmsItemEntity> findGrid(SysTmsItemEntity entity);

    SysTmsItem getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

    void batchInsert(List<SysTmsItem> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}