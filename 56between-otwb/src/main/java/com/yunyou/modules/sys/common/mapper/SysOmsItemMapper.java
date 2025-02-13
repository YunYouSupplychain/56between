package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsItem;
import com.yunyou.modules.sys.common.entity.extend.SysOmsItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息MAPPER接口
 */
@MyBatisMapper
public interface SysOmsItemMapper extends BaseMapper<SysOmsItem> {

    SysOmsItem getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

    List<SysOmsItemEntity> findPage(SysOmsItemEntity entity);

    Double getPackageQty(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet, @Param("unitLevel") String unitLevel);

    List<SysOmsItemEntity> findGrid(SysOmsItemEntity entity);

    SysOmsItemEntity getEntity(String id);

    void batchInsert(List<SysOmsItem> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}