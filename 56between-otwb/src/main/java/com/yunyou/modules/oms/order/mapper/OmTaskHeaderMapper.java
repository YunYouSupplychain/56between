package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.entity.OmTaskHeaderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应链作业任务MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-21
 */
@MyBatisMapper
public interface OmTaskHeaderMapper extends BaseMapper<OmTaskHeader> {

    OmTaskHeaderEntity getEntity(String id);

    List<OmTaskHeaderEntity> findPage(OmTaskHeaderEntity entity);

    void updateStatus(OmTaskHeader omTaskHeader);

    String getMaxLotNumBySourceNo(@Param("sourceNo") String sourceNo, @Param("orgId") String orgId);

    List<OmTaskHeader> getByChainNo(@Param("chainNo") String chainNo, @Param("orgId") String orgId);

    List<OmTaskHeader> getByAssociatedTaskId(@Param("associatedTaskId") String associatedTaskId);

    List<OmTaskHeader> getBatchTask(@Param("taskType") String taskType, @Param("lotNums") List<String> lotNums, @Param("orgId") String orgId);

    List<String> findTaskIdByChainIdForTimer(@Param("chainNo") String chainNo, @Param("orgId") String orgId);

    List<String> findCanPushTaskIdForTimer(@Param("status") String status, @Param("taskType") String taskType);

    OmTaskHeader getByTaskNoAndWarehouse(@Param("taskNo") String taskNo, @Param("warehouse") String warehouse);

    void updateAssociatedTask(@Param("id") String id, @Param("associatedTaskId") String associatedTaskId, @Param("delFlag") String delFlag);
}