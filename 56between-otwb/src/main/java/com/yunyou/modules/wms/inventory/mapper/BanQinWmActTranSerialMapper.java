package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerialEntity;

import java.util.List;

/**
 * 库存序列号交易MAPPER接口
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmActTranSerialMapper extends BaseMapper<BanQinWmActTranSerial> {
	List<BanQinWmActTranSerialEntity> findPage(BanQinWmActTranSerialEntity entity);
}