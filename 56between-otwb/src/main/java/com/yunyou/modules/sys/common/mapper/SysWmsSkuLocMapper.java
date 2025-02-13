package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsSkuLoc;
import com.yunyou.modules.sys.common.entity.extend.SysWmsSkuLocEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品拣货位MAPPER接口
 */
@MyBatisMapper
public interface SysWmsSkuLocMapper extends BaseMapper<SysWmsSkuLoc> {

    List<SysWmsSkuLocEntity> findPage(SysWmsSkuLocEntity entity);

    void deleteByHeaderId(String headerId);

    void batchInsert(List<SysWmsSkuLoc> list);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("dataSet") String dataSet);

}