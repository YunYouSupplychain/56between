package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitResultDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加工单结果明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@MyBatisMapper
public interface BanQinWmKitResultDetailMapper extends BaseMapper<BanQinWmKitResultDetail> {

    List<BanQinWmKitResultDetailEntity> findPage(BanQinWmKitResultDetail wmKitResultDetail);

    Long getMaxLineNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    List<BanQinWmKitResultDetailEntity> getEntityByKitNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    BanQinWmKitResultDetailEntity getEntityByKitNoAndKitLineNo(@Param("kitNo") String kitNo, @Param("kitLineNo") String kitLineNo, @Param("orgId") String orgId);

    BanQinWmKitResultDetailEntity getEntity(String id);
}