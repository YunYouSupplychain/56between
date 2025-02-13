package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonSkuBarcode;

import java.util.List;

/**
 * 商品条码MAPPER接口
 */
@MyBatisMapper
public interface SysCommonSkuBarcodeMapper extends BaseMapper<SysCommonSkuBarcode> {

    List<SysCommonSkuBarcode> findPage(SysCommonSkuBarcode entity);

    void batchInsert(List<SysCommonSkuBarcode> list);
}