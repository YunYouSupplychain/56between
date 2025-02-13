package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加工任务MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-21
 */
@MyBatisMapper
public interface BanQinWmTaskKitMapper extends BaseMapper<BanQinWmTaskKit> {

    List<BanQinWmTaskKitEntity> findPage(BanQinWmTaskKit wmTaskKit);

    List<BanQinWmTaskKitEntity> getEntityByKitNoAndKitLineNo(@Param("kitNo") String kitNo, @Param("kitLineNo") String kitLineNo, @Param("orgId") String orgId);

    List<BanQinWmTaskKitEntity> getEntityByKitNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    BanQinWmTaskKitEntity getEntityByKitTaskId(@Param("kitTaskId") String kitTaskId, @Param("orgId") String orgId);

    List<BanQinWmTaskKitEntity> getEntityByKitNoAndSubLineNoAndStatus(@Param("kitNo") String kitNo, @Param("subLineNos") Object[] subLineNos, @Param("status") String status, @Param("orgId") String orgId);

    List<BanQinWmTaskKitEntity> getEntityByKitNoAndTaskIdAndStatus(@Param("kitNo") String kitNo, @Param("kitTaskIds") Object[] kitTaskIds, @Param("status") String status, @Param("orgId") String orgId);

    List<BanQinWmTaskKitEntity> getEntityByParentLineNoAndStatus(@Param("kitNo") String kitNo, @Param("parentLineNo") String parentLineNo, @Param("status") String status, @Param("orgId") String orgId);

    BanQinWmTaskKitEntity getEntity(String id);
}