package com.yunyou.modules.tms.order.manager.mapper;

import java.util.List;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderInboundInfoEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderOutboundInfoEntity;
import org.apache.ibatis.annotations.Param;

@MyBatisMapper
public interface TmRepairOrderMapper extends BaseMapper<TmRepairOrderEntity> {

    TmRepairOrderEntity getEntity(String id);

    List<TmRepairOrderEntity> findEntityList(TmRepairOrderEntity qEntity);

    List<TmRepairOrderEntity> findPage(TmRepairOrderEntity qEntity);

    List<TmRepairOrderDetailEntity> findDetailList(TmRepairOrderDetailEntity tmRepairOrderDetailEntity);

    TmRepairOrderEntity getByNo(@Param("repairNo") String repairNo, @Param("orgId") String orgId);

    List<TmRepairOrderInboundInfoEntity> findInboundInfoList(TmRepairOrderInboundInfoEntity qEntity);

    List<TmRepairOrderOutboundInfoEntity> findOutboundInfoList(TmRepairOrderOutboundInfoEntity qEntity);
}
