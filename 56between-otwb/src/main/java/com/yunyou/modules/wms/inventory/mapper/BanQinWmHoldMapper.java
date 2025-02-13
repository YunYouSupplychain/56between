package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHold;
import com.yunyou.modules.wms.inventory.entity.BanQinWmHoldEntity;

import java.util.List;

/**
 * 库存冻结MAPPER接口
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmHoldMapper extends BaseMapper<BanQinWmHold> {
    
    BanQinWmHoldEntity getEntity(String id);
    
    List<BanQinWmHoldEntity> findPage(BanQinWmHoldEntity banQinWmHoldEntity);
}