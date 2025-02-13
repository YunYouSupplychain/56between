package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmItem;
import com.yunyou.modules.oms.basic.entity.extend.OmItemEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-15
 */
@MyBatisMapper
public interface OmItemMapper extends BaseMapper<OmItem> {

    List<OmItemEntity> findPage(OmItemEntity entity);

    List<OmItemEntity> findGrid(OmItemEntity entity);

    List<OmItemEntity> findSkuGrid(OmItemEntity entity);

    OmItemEntity getEntity(String id);

    OmItem getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<OmItem> list);
}