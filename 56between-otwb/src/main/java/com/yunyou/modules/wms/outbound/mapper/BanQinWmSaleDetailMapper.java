package com.yunyou.modules.wms.outbound.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售单明细MAPPER接口
 * @author WMJ
 * @version 2019-02-14
 */
@MyBatisMapper
public interface BanQinWmSaleDetailMapper extends BaseMapper<BanQinWmSaleDetail> {
	
    List<BanQinWmSaleDetail> getDetailBySaleNo(@Param("saleNos") String[] saleNos, @Param("status") String status, @Param("orgId") String orgId);

    void deleteWmSaleDetailBySaleNo(@Param("orgId") String orgId, @Param("saleNos") List<String> saleNos);

    List<BanQinWmSaleDetail> getDetailBySaleNoAndLineNos(@Param("saleNo") String saleNo, @Param("lineNos") List<String> lineNos, @Param("orgId") String orgId);

    void deleteWmSaleDetailBySaleNoAndLineNo(@Param("saleNo") String saleNo, @Param("lineNo") List<String> lineNos, @Param("orgId") String orgId);
    
    List<BanQinWmSaleDetail> checkSaleDetailStatus(@Param("saleNo") String saleNo, @Param("lineNos") List<String> lineNos, @Param("lineStatus") List<String> lineStatus,
                                                   @Param("saleStatus") List<String> saleStatus, @Param("auditStatus") List<String> auditStatus, @Param("orgId") String orgId);
    
    List<BanQinWmSaleDetail> checkSaleDetailExistsSo(@Param("saleNo") String saleNo, @Param("lineNo") List<String> lineNos, @Param("orgId") String orgId);
}