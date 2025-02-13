package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合件MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@MyBatisMapper
public interface BanQinCdWhBomHeaderMapper extends BaseMapper<BanQinCdWhBomHeader> {

    BanQinCdWhBomEntity getEntity(String id);

    BanQinCdWhBomEntity getEntityByOwnerAndParentSkuAndKitType(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);

    List<BanQinCdWhBomEntity> findPage(BanQinCdWhBomEntity entity);

    Long checkIsReferenced(BanQinCdWhBomHeader cdWhBomHeader);

    List<BanQinCdWhBomEntity> findGrid(BanQinCdWhBomEntity entity);
}