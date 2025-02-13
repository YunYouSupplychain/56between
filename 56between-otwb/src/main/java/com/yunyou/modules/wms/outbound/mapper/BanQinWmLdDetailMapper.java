package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.*;

import java.util.List;

/**
 * 装车单明细MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmLdDetailMapper extends BaseMapper<BanQinWmLdDetail> {
    
    List<BanQinWmLdDetailEntity> findEntity(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
    
	List<BanQinWmLdDetailEntity> getWmAllocByLdNo(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<String> getWmAllocIdForLdDetail(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<BanQinWmLdDetailEntity> getWmLdForSoOrder(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<BanQinWmLdDetailEntity> getWmLdForSoTraceId(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<BanQinWmLdDetailEntity> getWmSoAllocByLdNo(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	Integer isGeneratorLd(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<String> checkIsGenLdBySoNos(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<String> checkIsGenLdByToIds(BanQinWmLdDetailEntity banQinWmLdDetailEntity);
	
	List<String> checkIsGenLdByAllocIds(BanQinWmLdDetailEntity banQinWmLdDetailEntity);

	List<BanQinWmSoAllocToSoOrderQueryEntity> wmSoAllocToSoOrderQuery(BanQinWmSoAllocToSoOrderQueryEntity entity);

	List<BanQinWmLdDetailEntity> wmSoAllocToLdDetailQuery(BanQinWmLdDetailEntity entity);

}