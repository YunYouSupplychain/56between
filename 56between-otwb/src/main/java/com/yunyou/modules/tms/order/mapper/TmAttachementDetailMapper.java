package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 附件信息MAPPER接口
 * @author zyf
 * @version 2020-04-07
 */
@MyBatisMapper
public interface TmAttachementDetailMapper extends BaseMapper<TmAttachementDetail> {

    void deleteByOrderNo(@Param("orderNo") String orderNo, @Param("orderType") String orderType, @Param("orgId") String orgId);
}