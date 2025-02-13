package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderHeader;
import com.yunyou.modules.tms.order.entity.TmHandoverOrderLabel;
import com.yunyou.modules.tms.order.entity.extend.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface TmHandoverOrderMapper extends BaseMapper<TmHandoverOrderHeader> {

    TmHandoverOrderEntity getEntity(@Param("id") String id);

    List<TmHandoverOrderEntity> findPage(TmHandoverOrderEntity qEntity);

    List<TmHandoverOrderEntity> findEntityList(TmHandoverOrderEntity qEntity);

    List<TmHandoverOrderLabelEntity> findLabelList(TmHandoverOrderLabelEntity qEntity);

    List<TmHandoverOrderSkuEntity> findSkuList(TmHandoverOrderSkuEntity qEntity);

    TmHandoverOrderHeader getOrderByNo(@Param("handoverNo") String handoverNo, @Param("orgId") String orgId);

    TmHandoverOrderLabel getLabelByLabelNo(@Param("handoverNo") String handoverNo, @Param("labelNo") String labelNo, @Param("orgId") String orgId);

    void deleteLabelByHandoverNo(@Param("handoverNo") String handoverNo, @Param("orgId") String orgId);

    void deleteSkuByHandoverNo(@Param("handoverNo") String handoverNo, @Param("orgId") String orgId);
}
