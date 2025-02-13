package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdSerialEntity;

import java.util.List;

/**
 * 序列号调整MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmAdSerialMapper extends BaseMapper<BanQinWmAdSerial> {
	Double wmAdAbsSerialQuery(BanQinWmAdSerial banQinWmAdSerial);

	List<BanQinWmAdSerialEntity> findPage(BanQinWmAdSerialEntity entity);
}