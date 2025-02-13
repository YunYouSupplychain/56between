package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmCountHeaderEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCount;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盘点任务MAPPER接口
 * @author WMJ
 * @version 2019-01-28
 */
@MyBatisMapper
public interface BanQinWmTaskCountMapper extends BaseMapper<BanQinWmTaskCount> {
    BanQinWmTaskCountEntity getEntity(String id);

    List<BanQinWmTaskCountEntity> findPage(BanQinWmTaskCountEntity entity);

    List<BanQinWmTaskCount> getInvCountTask(BanQinWmCountHeaderEntity entity);

    List<BanQinWmTaskCount> getReCreateCount(BanQinWmTaskCount wmTaskCount);

    List<BanQinWmTaskCount> getByIds(List<String> list);
    
    List<BanQinWmTaskCount> getByHeaderId(@Param(value = "headerId") String headerId, @Param(value = "orgId") String orgId);

    void deleteByHeaderId(String headerId);

    List<BanQinWmTaskCountEntity> rfTCGetTaskCountDetailQuery(@Param("zoneCode") String zoneCode, @Param("lane") String lane, @Param("locCode") String locCode, @Param("countNo") String countNo, @Param("orgId") String orgId);
}