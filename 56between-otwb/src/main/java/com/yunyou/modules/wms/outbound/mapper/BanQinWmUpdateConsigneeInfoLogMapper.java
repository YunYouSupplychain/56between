package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmUpdateConsigneeInfoLog;
import com.yunyou.modules.wms.outbound.entity.BanQinWmUpdateConsigneeInfoLogEntity;

import java.util.List;

/**
 * 更新收货信息日志
 * @author WMJ
 * @version 2020-03-19
 */
@MyBatisMapper
public interface BanQinWmUpdateConsigneeInfoLogMapper extends BaseMapper<BanQinWmUpdateConsigneeInfoLog> {
    List<BanQinWmUpdateConsigneeInfoLogEntity> findPage(BanQinWmUpdateConsigneeInfoLogEntity entity);
}