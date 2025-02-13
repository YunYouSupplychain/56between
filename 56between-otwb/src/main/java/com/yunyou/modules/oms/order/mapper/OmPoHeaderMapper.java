package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmPoHeader;
import com.yunyou.modules.oms.order.entity.OmPoHeaderEntity;
import com.yunyou.modules.oms.order.entity.extend.OmPoPrintData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-16
 */
@MyBatisMapper
public interface OmPoHeaderMapper extends BaseMapper<OmPoHeader> {
    OmPoHeaderEntity getEntity(String id);

    List<OmPoHeaderEntity> findPage(OmPoHeaderEntity entity);

    void updateStatus(OmPoHeader omPoHeader);

    void updateAuditStatus(OmPoHeader omPoHeader);

    void updateStatusById(OmPoHeader omPoHeader);

    List<OmPoPrintData> getPoPrintData(@Param("ids") List<String> ids);
}