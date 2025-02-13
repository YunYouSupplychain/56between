package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinCountSerialQuery;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerialEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 序列号库存表MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmInvSerialMapper extends BaseMapper<BanQinWmInvSerial> {
    
	List<BanQinWmInvSerial> getByAsnNoAndLineNo(BanQinWmAsnSerial banQinWmAsnSerial);
	
	void deleteByAsnNoAndLineNo(BanQinWmAsnSerial banQinWmAsnSerial);
	
	void deleteByShip(@Param(value = "allocIds")List<String> allocIds, @Param(value = "orgId")String orgId);
	
	List<BanQinWmInvSerial> getByAllocId(@Param(value = "allocIds")List<String> allocIds, @Param(value = "orgId")String orgId);
	
	List<BanQinWmInvSerial> countSerialQuery(BanQinCountSerialQuery banQinWmInvSerial);

	List<BanQinWmInvSerialEntity> findPage(BanQinWmInvSerialEntity entity);
}