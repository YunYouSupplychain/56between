package com.yunyou.modules.tms.order.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmExceptionHandleBillDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 异常处理单明细MAPPER接口
 * @author ZYF
 * @version 2020-07-29
 */
@MyBatisMapper
public interface TmExceptionHandleBillDetailMapper extends BaseMapper<TmExceptionHandleBillDetail> {

    void deleteDetail(@Param("billNo") String billNo, @Param("orgId") String orgId);

}