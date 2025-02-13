package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinWmCarrierTypeRelationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 承运商类型关系MAPPER接口
 *
 * @author zyf
 * @version 2020-01-07
 */
@MyBatisMapper
public interface BanQinWmCarrierTypeRelationMapper extends BaseMapper<BanQinWmCarrierTypeRelation> {

    BanQinWmCarrierTypeRelationEntity getEntity(String id);

    List<BanQinWmCarrierTypeRelationEntity> findPage(BanQinWmCarrierTypeRelationEntity entity);
    List<BanQinWmCarrierTypeRelationEntity> findEntity(BanQinWmCarrierTypeRelationEntity entity);
    void remove(@Param("carrierCode") String carrierCode, @Param("orgId") String orgId);
}