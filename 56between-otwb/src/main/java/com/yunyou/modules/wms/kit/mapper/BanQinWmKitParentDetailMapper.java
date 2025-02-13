package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitParentDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加工单父件明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@MyBatisMapper
public interface BanQinWmKitParentDetailMapper extends BaseMapper<BanQinWmKitParentDetail> {

    List<BanQinWmKitParentDetailEntity> findPage(BanQinWmKitParentDetail wmKitParentDetail);

    BanQinWmKitParentDetailEntity getEntity(String id);

    Long getMaxLineNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    List<BanQinWmKitParentDetailEntity> getEntityByKitNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    BanQinWmKitParentDetailEntity getEntityByKitNoAndParentLineNo(@Param("kitNo") String kitNo, @Param("parentLineNo") String parentLineNo, @Param("orgId") String orgId);
}