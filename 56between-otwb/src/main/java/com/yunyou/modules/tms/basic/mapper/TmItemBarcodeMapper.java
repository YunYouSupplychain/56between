package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmItemBarcode;
import org.apache.ibatis.annotations.Param;

/**
 * 商品条码信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmItemBarcodeMapper extends BaseMapper<TmItemBarcode> {

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);
}