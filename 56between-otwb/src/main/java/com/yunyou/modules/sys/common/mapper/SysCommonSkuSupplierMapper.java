package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonSkuSupplier;

import java.util.List;

/**
 * 商品供应商MAPPER接口
 */
@MyBatisMapper
public interface SysCommonSkuSupplierMapper extends BaseMapper<SysCommonSkuSupplier> {

    void batchInsert(List<SysCommonSkuSupplier> list);

}