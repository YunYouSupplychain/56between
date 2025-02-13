package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObjectDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 开票对象明细MAPPER接口
 */
@MyBatisMapper
public interface SysBmsInvoiceObjectDetailMapper extends BaseMapper<SysBmsInvoiceObjectDetail> {

    SysBmsInvoiceObjectDetail getByCode(@Param("code") String code, @Param("dataSet") String dataSet);

}