package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合件加工工序MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@MyBatisMapper
public interface BanQinCdWhBomStepMapper extends BaseMapper<BanQinCdWhBomStep> {

    Integer getMaxLineNo(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);

    void deleteByOwnerAndParentSkuAndKitType(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);

    List<BanQinCdWhBomStep> findPage(BanQinCdWhBomStep entity);
}