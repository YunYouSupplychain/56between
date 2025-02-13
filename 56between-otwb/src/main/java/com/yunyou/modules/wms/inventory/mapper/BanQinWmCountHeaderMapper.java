package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeaderEntity;
import com.yunyou.modules.wms.report.entity.CountTaskLabel;

import java.util.List;

/**
 * 库存盘点MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmCountHeaderMapper extends BaseMapper<BanQinWmCountHeader> {
	BanQinWmCountHeaderEntity getEntity(String id);

	List<BanQinWmCountHeaderEntity> findPage(BanQinWmCountHeaderEntity entity);

	List<CountTaskLabel> getCountTaskLabel(List<String> ids);
}