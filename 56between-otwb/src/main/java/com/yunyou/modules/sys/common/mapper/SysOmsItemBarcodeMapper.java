package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsItemBarcode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品条码表MAPPER接口
 */
@MyBatisMapper
public interface SysOmsItemBarcodeMapper extends BaseMapper<SysOmsItemBarcode> {

    void batchInsert(List<SysOmsItemBarcode> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}