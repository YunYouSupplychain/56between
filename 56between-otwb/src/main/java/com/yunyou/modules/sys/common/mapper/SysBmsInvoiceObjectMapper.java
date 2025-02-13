package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsInvoiceObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开票对象MAPPER接口
 */
@MyBatisMapper
public interface SysBmsInvoiceObjectMapper extends BaseMapper<SysBmsInvoiceObject> {
    List<SysBmsInvoiceObject> findPage(SysBmsInvoiceObject entity);

    List<SysBmsInvoiceObject> findGrid(SysBmsInvoiceObject entity);

    List<SysBmsInvoiceObject> findSync(SysBmsInvoiceObject entity);

    List<SysBmsInvoiceObject> getInvoiceObjectBySupplier(@Param("supplierCode") String supplierCode);
}