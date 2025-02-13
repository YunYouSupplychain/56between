package com.yunyou.modules.wms.task.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRp;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRpEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 补货任务MAPPER接口
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmTaskRpMapper extends BaseMapper<BanQinWmTaskRp> {
	BanQinWmTaskRpEntity getEntity(String id);

	List<BanQinWmTaskRpEntity> findPage(BanQinWmTaskRpEntity banQinWmTaskRpEntity);

	List<BanQinWmTaskRpEntity> rfRPGetRpDetailQuery(@Param("rpId") String rpId, @Param("orgId") String orgId);
}