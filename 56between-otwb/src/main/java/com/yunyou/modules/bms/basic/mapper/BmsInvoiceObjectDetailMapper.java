package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsInvoiceObjectDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 开票对象明细MAPPER接口
 * @author zqs
 * @version 2019-02-18
 */
@MyBatisMapper
public interface BmsInvoiceObjectDetailMapper extends BaseMapper<BmsInvoiceObjectDetail> {

    BmsInvoiceObjectDetail getByCode(@Param("code") String code, @Param("orgId") String orgId);

}