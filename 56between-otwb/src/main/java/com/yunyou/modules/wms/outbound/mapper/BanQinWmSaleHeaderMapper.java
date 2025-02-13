package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleHeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售单MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSaleHeaderMapper extends BaseMapper<BanQinWmSaleHeader> {
	BanQinWmSaleEntity getEntity(String id);

	List<BanQinWmSaleEntity> findPage(BanQinWmSaleEntity entity);
    
	void updateWmSaleHeaderStatus(@Param("saleNos") List<String> saleNos, @Param("userId") String usserId, @Param("orgID") String orgId);
	
	void deleteWmSaleHeader(@Param("saleNos") List<String> saleNos, @Param("orgId") String orgId);
	
	List<BanQinWmSaleHeader> getBySaleNoArray(@Param("saleNos") List<String> saleNos, @Param("orgId") String orgId);
	
	List<String> checkWmSaleExistsSo(@Param("saleNos") List<String> saleNos, @Param("orgId") String orgId);
	
	List<String> checkSaleStatus(@Param("saleNos") List<String> saleNos, @Param("status") List<String> status, @Param("auditStatus") List<String> auditStatus, @Param("orgId") String orgId);

    List<String> checkWmSaleExistsUnCloseSo(@Param("saleNo") String saleNo, @Param("orgId") String orgId);
}