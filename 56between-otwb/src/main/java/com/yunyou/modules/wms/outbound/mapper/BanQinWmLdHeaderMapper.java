package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmLdHeader;
import com.yunyou.modules.wms.outbound.entity.extend.PrintLdOrder;
import com.yunyou.modules.wms.report.entity.WmStoreCheckAcceptOrderLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装车单MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmLdHeaderMapper extends BaseMapper<BanQinWmLdHeader> {
	BanQinWmLdEntity getEntity(String id);
	List<BanQinWmLdEntity> findPage(BanQinWmLdHeader banQinWmLdHeader);
	List<BanQinWmLdEntity> findEntity(BanQinWmLdEntity entity);

	List<WmStoreCheckAcceptOrderLabel> getStoreCheckAcceptOrder(List<String> ldOrderIds);

    List<PrintLdOrder> getLdOrder(@Param("ids") String[] ids);
}