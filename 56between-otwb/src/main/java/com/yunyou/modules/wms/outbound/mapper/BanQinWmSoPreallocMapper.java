package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPreallocEntity;

import java.util.List;

/**
 * 预配明细MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSoPreallocMapper extends BaseMapper<BanQinWmSoPrealloc> {
    
	List<BanQinWmSoPreallocEntity> findEntity(BanQinWmSoPreallocEntity banQinWmSoPreallocEntity);
	
	List<BanQinWmSoPreallocEntity> getWmInterceptSoPrealloc(BanQinWmSoPreallocEntity banQinWmSoPreallocEntity);
	
	List<BanQinWmSoPreallocEntity> getWmSoPreallocByNo(BanQinWmSoPreallocEntity banQinWmSoPreallocEntity);
}