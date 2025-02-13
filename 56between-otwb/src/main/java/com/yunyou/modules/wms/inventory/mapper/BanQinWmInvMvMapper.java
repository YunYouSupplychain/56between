package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvMvEntity;

import java.util.List;

/**
 * 库存移动MAPPER接口
 * @author WMJ
 * @version 2019-06-20
 */
@MyBatisMapper
public interface BanQinWmInvMvMapper extends BaseMapper<BanQinWmInvMvEntity> {
    List<BanQinWmInvMvEntity> findPage(BanQinWmInvMvEntity entity);
}