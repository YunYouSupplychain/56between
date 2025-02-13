package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionDetailEntity;
import com.yunyou.modules.oms.order.entity.extend.OmRequisitionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface OmRequisitionMapper {

    OmRequisitionEntity getEntity(String id);

    OmRequisitionEntity getEntityByNo(@Param("reqNo") String reqNo, @Param("orgId") String orgId);

    OmRequisitionDetailEntity getDetailEntity(String id);

    OmRequisitionDetailEntity getDetailEntityByNoAndLineNo(@Param("reqNo") String reqNo, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    List<OmRequisitionDetailEntity> findDetailList(OmRequisitionDetailEntity qEntity);

    List<OmRequisitionEntity> findPage(OmRequisitionEntity qEntity);

    void removeByNo(@Param("reqNo") String reqNo, @Param("orgId") String orgId);
}