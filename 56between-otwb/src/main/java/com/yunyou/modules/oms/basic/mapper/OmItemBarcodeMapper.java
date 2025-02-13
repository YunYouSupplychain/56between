package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmItemBarcode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品条码表MAPPER接口
 * @author WMJ
 * @version 2019-04-15
 */
@MyBatisMapper
public interface OmItemBarcodeMapper extends BaseMapper<OmItemBarcode> {

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<OmItemBarcode> list);
}