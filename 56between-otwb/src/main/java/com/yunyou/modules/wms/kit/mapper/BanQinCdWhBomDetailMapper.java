package com.yunyou.modules.wms.kit.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合件明细MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@MyBatisMapper
public interface BanQinCdWhBomDetailMapper extends BaseMapper<BanQinCdWhBomDetail> {

    BanQinCdWhBomDetailEntity getEntity(String id);

    List<BanQinCdWhBomDetailEntity> findPage(BanQinCdWhBomDetail entity);

    Long checkIsReferenced(BanQinCdWhBomDetail banQinCdWhBomDetail);

    List<BanQinCdWhBomDetailEntity> findSubSkuList(BanQinCdWhBomDetailEntity entity);

    List<BanQinCdWhBomDetail> getParentSku(BanQinCdWhBomDetail cdWhBomDetail);

    Integer getMaxLineNo(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);

    void deleteByOwnerAndParentSkuAndKitType(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);

    BanQinCdWhBomDetailEntity getEntityByOwnerAndParentSkuAndKitTypeAndLineNo(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("lineNo") String lineNo, @Param("orgId") String orgId);

    List<BanQinCdWhBomDetailEntity> getEntityByOwnerAndParentSkuAndKitType(@Param("ownerCode") String ownerCode, @Param("parentSkuCode") String parentSkuCode, @Param("kitType") String kitType, @Param("orgId") String orgId);
}