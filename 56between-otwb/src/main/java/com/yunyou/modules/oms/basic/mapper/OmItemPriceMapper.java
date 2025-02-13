package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmItemPrice;
import com.yunyou.modules.oms.basic.entity.OmItemPriceEntity;

import java.util.List;

/**
 * 商品价格MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-20
 */
@MyBatisMapper
public interface OmItemPriceMapper extends BaseMapper<OmItemPrice> {

    OmItemPriceEntity getEntity(String id);

    OmItemPriceEntity findEntity(OmItemPrice omItemPrice);

    List<OmItemPriceEntity> findPage(OmItemPriceEntity entity);

    List<OmItemPriceEntity> popData(OmItemPriceEntity entity);

    List<OmItemPriceEntity> findValidityTermData(OmItemPrice omItemPrice);

    List<String> popSkuData(OmItemPriceEntity entity);

    List<OmItemPriceEntity> popDataAll(OmItemPriceEntity entity);
}