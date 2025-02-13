package com.yunyou.modules.wms.inbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailNew;
import org.apache.ibatis.annotations.Param;

/**
 * 采购单明细MAPPER接口
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmPoDetailNewMapper extends BaseMapper<BanQinWmPoDetailNew> {

    void removeByPoId(@Param("poIds") String[] poIds);
}