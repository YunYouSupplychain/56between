package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 波次单明细MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmWvDetailMapper extends BaseMapper<BanQinWmWvDetail> {
    
	void updateWvDetailStatusByWave(@Param("waveNos") List<String> waveNos, @Param("userId") String userId, @Param("orgId") String orgId);
	
	void updateWvDetailStatusBySo(@Param("soNos") List<String> soNos, @Param("userId") String userId, @Param("orgId") String orgId);
	
	List<String> checkSoCreateWaveOrder(@Param("soNos") List<String> soNos, @Param("orgId") String orgId);
}