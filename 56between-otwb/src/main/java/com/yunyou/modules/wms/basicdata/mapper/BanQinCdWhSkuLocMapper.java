package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLocEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品拣货位MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhSkuLocMapper extends BaseMapper<BanQinCdWhSkuLoc> {

    void deleteByHeaderId(String headerId);

    List<BanQinCdWhSkuLocEntity> getRpSkuQuery(BanQinCdWhSkuLocEntity entity);

    List<BanQinCdWhSkuLocEntity> findPage(BanQinCdWhSkuLocEntity entity);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<BanQinCdWhSkuLoc> list);
}