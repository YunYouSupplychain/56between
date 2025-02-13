package com.yunyou.modules.oms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.order.entity.OmSaleHeader;
import com.yunyou.modules.oms.order.entity.OmSaleHeaderEntity;
import com.yunyou.modules.oms.report.entity.OmShipOrderLabel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 销售订单MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-17
 */
@MyBatisMapper
public interface OmSaleHeaderMapper extends BaseMapper<OmSaleHeader> {

    OmSaleHeaderEntity getEntity(String id);

    List<OmSaleHeaderEntity> findPage(OmSaleHeaderEntity entity);

    void updateAmount(OmSaleHeader omSaleHeader);

    void updateStatus(OmSaleHeader omSaleHeader);

    void updateAuditStatus(OmSaleHeader omSaleHeader);

    void updateStatusById(OmSaleHeader omSaleHeader);

    List<OmSaleHeaderEntity> getUnAssociatedPoData(OmSaleHeaderEntity entity);

    void updateAppAnnex(@Param("id") String id, @Param("file") String file);

    void updateAnnex(@Param("id") String id, @Param("file") String file);

    void associatedPo(@Param("saleNos") String[] saleNos, @Param("poNo") String poNo, @Param("orgId") String orgId);

    void unAssociatedPo(@Param("poNo") String poNo, @Param("orgId") String orgId);

    List<OmShipOrderLabel> getShipOrder(@Param("ids")List<String> ids);

    List<Map<String, Object>> saleOrderCountAndAmountByDay(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("orgId") String orgId);

    Map<String, Object> findSaleOrderCountAndAmount(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("orgId") String orgId);

}