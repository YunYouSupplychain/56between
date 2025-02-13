package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderSite;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 派车单配送点MAPPER接口
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@MyBatisMapper
public interface TmDispatchOrderSiteMapper extends BaseMapper<TmDispatchOrderSite> {

    TmDispatchOrderSite getByDispatchNoAndOutletCode(@Param("dispatchNo") String dispatchNo, @Param("outletCode") String outletCode, @Param("orgId") String orgId);

    TmDispatchOrderSiteEntity getEntity(@Param("id") String id);
}