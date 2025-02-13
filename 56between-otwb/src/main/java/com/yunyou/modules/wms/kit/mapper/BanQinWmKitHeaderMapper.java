package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 加工单MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@MyBatisMapper
public interface BanQinWmKitHeaderMapper extends BaseMapper<BanQinWmKitHeader> {

    BanQinWmKitEntity getEntity(String id);

    BanQinWmKitEntity getEntityByKitNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    void updateStatus(BanQinWmKitHeader model);
}