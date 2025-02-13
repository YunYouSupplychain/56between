package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmCarrierServiceScope;
import com.yunyou.modules.oms.basic.entity.extend.OmCarrierServiceScopeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 承运商服务范围MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-10-23
 */
@MyBatisMapper
public interface OmCarrierServiceScopeMapper extends BaseMapper<OmCarrierServiceScope> {

    OmCarrierServiceScopeEntity getEntity(String id);

    List<OmCarrierServiceScopeEntity> findPage(OmCarrierServiceScopeEntity entity);

    void remove(@Param("ownerCode") String ownerCode, @Param("carrierCode") String carrierCode, @Param("groupCode") String groupCode, @Param("orgId") String orgId);
}