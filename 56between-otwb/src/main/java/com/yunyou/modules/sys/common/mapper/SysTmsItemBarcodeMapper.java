package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsItemBarcode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品条码信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsItemBarcodeMapper extends BaseMapper<SysTmsItemBarcode> {

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

    void batchInsert(List<SysTmsItemBarcode> list);
}