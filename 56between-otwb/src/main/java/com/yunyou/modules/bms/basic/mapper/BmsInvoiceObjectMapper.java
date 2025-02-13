package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsInvoiceObject;
import org.apache.ibatis.annotations.Param;

/**
 * 开票对象MAPPER接口
 *
 * @author zqs
 * @version 2019-02-18
 */
@MyBatisMapper
public interface BmsInvoiceObjectMapper extends BaseMapper<BmsInvoiceObject> {

    BmsInvoiceObject getByCode(@Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}