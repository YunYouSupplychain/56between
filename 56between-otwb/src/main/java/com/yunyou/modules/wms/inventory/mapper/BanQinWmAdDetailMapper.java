package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetailEntity;

import java.util.List;

/**
 * 调整单明细MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmAdDetailMapper extends BaseMapper<BanQinWmAdDetail> {
    
	List<BanQinWmAdDetailEntity> wmAdCheckSerialQuery(BanQinWmAdDetail banQinWmAdDetail);
	
	List<BanQinWmAdDetailEntity> findPage(BanQinWmAdDetailEntity banQinWmAdDetailEntity);
}