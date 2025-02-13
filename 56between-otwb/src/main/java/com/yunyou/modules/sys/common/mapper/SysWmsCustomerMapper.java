package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 */
@MyBatisMapper
public interface SysWmsCustomerMapper extends BaseMapper<SysWmsCustomer> {
    List<SysWmsCustomer> findPage(SysWmsCustomer sysWmsCustomer);

    List<SysWmsCustomer> findGrid(SysWmsCustomer sysWmsCustomer);

    SysWmsCustomer getByCode(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);

    void batchInsert(List<SysWmsCustomer> list);

    void remove(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);
}