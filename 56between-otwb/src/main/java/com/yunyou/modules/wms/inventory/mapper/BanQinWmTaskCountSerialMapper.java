package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountSerialEntity;

import java.util.List;

/**
 * 序列号盘点任务MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmTaskCountSerialMapper extends BaseMapper<BanQinWmTaskCountSerial> {
	List<BanQinWmTaskCountSerialEntity> findPage(BanQinWmTaskCountSerialEntity entity);
}