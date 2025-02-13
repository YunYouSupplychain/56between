package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContractStoragePrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓储价格MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsContractStoragePriceMapper extends BaseMapper<BmsContractStoragePrice> {

    List<BmsContractStoragePrice> findPage(BmsContractStoragePrice entity);

    List<BmsContractStoragePrice> findByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    List<BmsContractStoragePrice> findByFkId(@Param("fkId") String fkId);

    BmsContractStoragePrice getByFkIdAndSku(@Param("fkId") String fkId, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    BmsContractStoragePrice getByFkIdAndSkuClass(@Param("fkId") String fkId, @Param("skuClass") String skuClass, @Param("orgId") String orgId);

    BmsContractStoragePrice getByFkId(@Param("fkId") String fkId, @Param("orgId") String orgId);

    void deleteByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    void deleteByHeaderId(@Param("fkId") String fkId);

    /**
     * 获取合同单价
     *
     * @param fkId     合同明细ID
     * @param skuClass 品类
     * @param skuCode  商品编码
     * @param orgId    机构ID
     * @return 单价
     */
    BmsContractPrice getContractPrice(@Param("fkId") String fkId,
                                      @Param("skuClass") String skuClass,
                                      @Param("skuCode") String skuCode,
                                      @Param("orgId") String orgId);
}