package com.yunyou.modules.oms.inv.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.inv.entity.OmSaleInventory;
import com.yunyou.modules.oms.inv.entity.OmSaleInventoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售库存MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-09
 */
@MyBatisMapper
public interface OmSaleInventoryMapper extends BaseMapper<OmSaleInventory> {

    void updateAllocQty(@Param("owner") String owner, @Param("skuCode") String skuCode,
                        @Param("addAllocQty") double addAllocQty, @Param("warehouse") String warehouse,
                        @Param("orgId") String orgId, @Param("recVer") Integer recVer);

    List<OmSaleInventoryEntity> findInv(OmSaleInventoryEntity entity);

    OmSaleInventoryEntity findOmSkuInv(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

}