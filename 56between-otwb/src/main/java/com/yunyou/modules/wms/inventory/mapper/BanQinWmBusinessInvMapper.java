package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInv;
import com.yunyou.modules.wms.inventory.entity.BanQinWmBusinessInvEntity;

import java.util.List;

/**
 * 业务库存MAPPER接口
 * @author WMJ
 * @version 2020-04-26
 */
@MyBatisMapper
public interface BanQinWmBusinessInvMapper extends BaseMapper<BanQinWmBusinessInv> {
    List<BanQinWmBusinessInvEntity> findPage(BanQinWmBusinessInvEntity entity);
}