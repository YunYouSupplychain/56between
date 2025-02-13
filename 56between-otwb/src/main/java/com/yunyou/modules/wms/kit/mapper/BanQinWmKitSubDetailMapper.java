package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitSubDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加工单子件明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@MyBatisMapper
public interface BanQinWmKitSubDetailMapper extends BaseMapper<BanQinWmKitSubDetail> {

    List<BanQinWmKitSubDetailEntity> findPage(BanQinWmKitSubDetail wmKitSubDetail);

    List<BanQinWmKitSubDetailEntity> getEntityByKitNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);

    BanQinWmKitSubDetailEntity getEntityByKitNoAndSubLineNo(@Param("kitNo") String kitNo, @Param("subLineNo") String subLineNo, @Param("orgId") String orgId);

    Long getMaxLineNo(@Param("kitNo") String kitNo, @Param("orgId") String orgId);
}