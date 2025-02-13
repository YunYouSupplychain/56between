package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsTransportPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportPriceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 承运价格MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-07-19
 */
@MyBatisMapper
public interface BmsTransportPriceMapper extends BaseMapper<BmsTransportPrice> {

    /**
     * 查找运输价格
     *
     * @param id 运输价格ID
     * @return 运输价格
     */
    BmsTransportPriceEntity getEntity(String id);

    /**
     * 根据运输价格体系ID查找运输价格
     *
     * @param fkId 运输价格体系ID
     * @return 运输价格
     */
    List<BmsTransportPriceEntity> findByFkId(@Param("fkId") String fkId);

    /**
     * 根据运输价格体系编码查找运输价格
     *
     * @param entity 查询参数
     * @return 运输价格
     */
    List<BmsTransportPriceEntity> findByTransportCode(BmsTransportPriceEntity entity);

    /**
     * 获取运输价格
     *
     * @param contractDetailId 合同明细ID
     * @param startPlaceCode   起点编码
     * @param endPlaceCode     终点编码
     * @param regionCode       区域编码
     * @param carTypeCode      车型编码
     * @param orgId            机构ID
     * @return 运输价格
     */
    BmsContractPrice getContractPrice(@Param("contractDetailId") String contractDetailId,
                                      @Param("startPlaceCode") String startPlaceCode,
                                      @Param("endPlaceCode") String endPlaceCode,
                                      @Param("regionCode") String regionCode,
                                      @Param("carTypeCode") String carTypeCode,
                                      @Param("orgId") String orgId);
}