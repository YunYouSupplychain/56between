package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单明细MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmPoDetailMapper extends BaseMapper<BanQinWmPoDetail> {
    
    List<BanQinWmPoDetailEntity> findPage(BanQinWmPoDetailEntity banQinWmPoDetailEntity);
    
    List<BanQinWmPoDetailEntity> findEntity(BanQinWmPoDetailEntity banQinWmPoDetailEntity);

    List<BanQinWmPoDetail> findByPoNoAndLineNo(@Param("poNo") String poNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);

    String countPoStatus(@Param("poNo") String poNo, @Param("orgId") String orgId);

    void updateLogisticNo(@Param("poNo") String poNo, @Param("logisticNo") String logisticNo, @Param("orgId") String orgId);

    Integer getMaxLineNo(@Param("poNo") String poNo, @Param("orgId") String orgId);

    List<BanQinWmPoDetail> findExistsAsn(@Param("poNo") String poNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);

    void removeByPoNoAndLineNo(@Param("poNo") String poNo, @Param("lineNos") String[] lineNos, @Param("orgId") String orgId);
}