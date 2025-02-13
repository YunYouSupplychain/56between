package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsSkuBarcode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品条码MAPPER接口
 */
@MyBatisMapper
public interface SysWmsSkuBarcodeMapper extends BaseMapper<SysWmsSkuBarcode> {

    List<SysWmsSkuBarcode> findPage(SysWmsSkuBarcode entity);

    void deleteByHeadId(String headId);

    void batchInsert(List<SysWmsSkuBarcode> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);
}