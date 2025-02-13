package com.yunyou.modules.wms.task.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.report.entity.PutawayTaskLabel;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上架任务MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-26
 */
@MyBatisMapper
public interface BanQinWmTaskPaMapper extends BaseMapper<BanQinWmTaskPa> {

    Integer getMaxLineNo(@Param("paId") String paId, @Param("orgId") String orgId);
    
    BanQinWmTaskPaEntity getEntity(String id);
    
    List<BanQinWmTaskPaEntity> findPage(BanQinWmTaskPaEntity banQinWmTaskPaEntity);

    List<BanQinWmTaskPaEntity> findGrid(BanQinWmTaskPaEntity banQinWmTaskPaEntity);

    List<PutawayTaskLabel> getPaTaskReport(List<String> ids);

    List<BanQinWmTaskPaEntity> rfPaGetPATaskByTaskNoQuery(@Param("paId") String paId, @Param("orderNo") String orderNo, @Param("zoneCode") String zoneCode, @Param("orgId") String orgId);

    void updatePaIdByKitRemoveTaskPa(@Param("paId") String paId, @Param("orgId") String orgId);

    List<BanQinWmTaskPaEntity> paCountQuery(BanQinWmTaskPaEntity banQinWmTaskPaEntity);
}