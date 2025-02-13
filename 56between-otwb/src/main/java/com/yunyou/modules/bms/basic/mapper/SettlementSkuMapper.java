package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.SettlementSku;
import com.yunyou.modules.bms.basic.entity.extend.SettlementSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 结算商品MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
@MyBatisMapper
public interface SettlementSkuMapper extends BaseMapper<SettlementSku> {

    SettlementSkuEntity getEntity(String id);

    SettlementSku getByOwnerAndSku(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    List<SettlementSku> findPage(SettlementSkuEntity entity);

    List<SettlementSku> findGrid(SettlementSkuEntity entity);

    List<SettlementSku> findGridDataAndSupplier(SettlementSkuEntity entity);

    void remove(@Param("ownerCode") String ownerCode, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    void batchInsert(List<SettlementSku> list);
}