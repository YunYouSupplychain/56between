package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSkuPriceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同商品价格MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsContractSkuPriceMapper extends BaseMapper<BmsContractSkuPrice> {

    void deleteByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    List<BmsContractSkuPrice> findPage(BmsContractSkuPrice entity);

    BmsContractSkuPriceEntity getEntity(String id);

    List<BmsContractSkuPrice> findByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    BmsContractSkuPrice getContractPrice1(@Param("sysContractNo") String sysContractNo,
                                          @Param("skuCode") String skuCode,
                                          @Param("orgId") String orgId);

    BmsContractSkuPrice getContractPrice2(@Param("sysContractNo") String sysContractNo,
                                          @Param("skuClass") String skuClass,
                                          @Param("orgId") String orgId);

    BmsContractSkuPrice getContractPrice3(@Param("sysContractNo") String sysContractNo,
                                          @Param("orgId") String orgId);
}