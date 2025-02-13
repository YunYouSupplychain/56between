package com.yunyou.modules.wms.inventory.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLoc;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批次库位库存表MAPPER接口
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinWmInvLotLocMapper extends BaseMapper<BanQinWmInvLotLoc> {
    
    List<BanQinWmInvLotLocEntity> findPage(BanQinWmInvLotLocEntity banQinWmInvLotLocEntity);
    
	List<BanQinWmInvLotAtt> getPalletQty1ByLocCode(BanQinWmInvLotLoc banQinWmInvLotLoc);
	
	Double getWmInvGrossWeight(BanQinWmInvLotLoc banQinWmInvLotLoc);
	
	Double getWmInvCubic(BanQinWmInvLotLoc banQinWmInvLotLoc);

    List<BanQinWmInvLotLoc> notAvailableHoldQuery(BanQinWmInvLotLoc wmInvLotLoc);

    List<BanQinWmInvLotLocEntity> rfMVGetMovementDetailQuery(@Param("locCode") String locCode, @Param("traceId") String traceId, @Param("lotNum") String lotNum, @Param("skuCode") String skuCode, @Param("orgId") String orgId);

    List<BanQinWmInvLotLocEntity> rfInvGetSkuDetailQuery(@Param("locCode") String locCode, @Param("traceId") String traceId, @Param("lotNum") String lotNum, @Param("skuCode") String skuCode, @Param("orgId") String orgId);
}