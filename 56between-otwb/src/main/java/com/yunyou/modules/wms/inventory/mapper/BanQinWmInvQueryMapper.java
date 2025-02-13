package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvQuery;
import com.yunyou.modules.wms.inventory.entity.extend.BanQinWmInvExportEntity;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存查询MAPPER接口
 *
 * @author WMJ
 * @version 2019/02/28
 */
@MyBatisMapper
public interface BanQinWmInvQueryMapper extends BaseMapper<BanQinWmInvQuery> {
    List<BanQinWmInvQuery> findByOwner(BanQinWmInvQuery entity);

    List<BanQinWmInvQuery> findBySku(BanQinWmInvQuery entity);

    List<BanQinWmInvQuery> findByLot(BanQinWmInvQuery entity);

    List<BanQinWmInvQuery> findByLoc(BanQinWmInvQuery entity);

    List<BanQinWmInvQuery> findBySkuAndLoc(BanQinWmInvQuery entity);

    List<BanQinWmInvQuery> findByTraceId(BanQinWmInvQuery entity);

    List<BanQinWmInvExportEntity> findExportInfo(BanQinWmInvQuery entity);

    List<TraceLabel> getTraceLabel(@Param("ids") List<String> ids);
}