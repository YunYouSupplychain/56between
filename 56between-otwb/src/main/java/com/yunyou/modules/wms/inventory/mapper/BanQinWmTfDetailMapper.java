package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetailEntity;

import java.util.List;

/**
 * 转移单明细MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmTfDetailMapper extends BaseMapper<BanQinWmTfDetail> {
    
	List<BanQinWmTfDetailEntity> wmTfIsSerialQuery(BanQinWmTfDetailEntity banQinWmTfDetailEntity);

    BanQinWmTfDetailEntity getEntity(String id);
    
    List<BanQinWmTfDetailEntity> findPage(BanQinWmTfDetailEntity banQinWmTfDetailEntity);
}