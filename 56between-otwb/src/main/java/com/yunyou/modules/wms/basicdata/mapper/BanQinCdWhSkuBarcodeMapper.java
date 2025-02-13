package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品条码MAPPER接口
 * @author WMJ
 * @version 2019-10-28
 */
@MyBatisMapper
public interface BanQinCdWhSkuBarcodeMapper extends BaseMapper<BanQinCdWhSkuBarcode> {

    void deleteByHeadId(String headId);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<BanQinCdWhSkuBarcode> list);
}