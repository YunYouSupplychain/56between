package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存每日快照MAPPER接口
 * @author ZYF
 * @version 2021/04/25
 */
@MyBatisMapper
public interface BanQinWmInvSnapshotMapper {
	// 清除批次库存快照
	void clearInvLotSnapshot(@Param("invDate") String invDate);
	// 生成批次库存快照
	void initInvLotSnapshot(@Param("invDate") String invDate);

	// 清除批次库位库存快照
	void clearInvLotLocSnapshot(@Param("invDate") String invDate, @Param("orgId") String orgId);
	// 生成批次库位库存快照
	void initInvLotLocSnapshot(@Param("invDate") String invDate);
	// 根据库存交易生成批次库位库存快照
	void generateInvLotLocSnapshotByActTran(@Param("invDate") String invDate, @Param("date") String date, @Param("orgId") String orgId);
}