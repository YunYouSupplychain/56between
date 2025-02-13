package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购单MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmPoHeaderMapper extends BaseMapper<BanQinWmPoHeader> {

    List<BanQinWmPoHeader> findExistsAsn(@Param("ids") String[] ids);

    BanQinWmPoEntity getEntity(String orgId);
    
    List<BanQinWmPoEntity> findPage(BanQinWmPoEntity banQinWmPoEntity);
}