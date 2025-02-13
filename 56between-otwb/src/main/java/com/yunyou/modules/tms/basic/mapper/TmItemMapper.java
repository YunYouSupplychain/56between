package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmItem;
import com.yunyou.modules.tms.basic.entity.extend.TmItemEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 商品信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmItemMapper extends BaseMapper<TmItem> {

    TmItemEntity getEntity(String id);

    List<TmItemEntity> findPage(TmItem tmItem);

    List<TmItemEntity> findGrid(TmItemEntity entity);

    TmItem getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<TmItem> list);
}