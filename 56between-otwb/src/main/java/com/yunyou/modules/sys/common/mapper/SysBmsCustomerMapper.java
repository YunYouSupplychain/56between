package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 */
@MyBatisMapper
public interface SysBmsCustomerMapper extends BaseMapper<SysBmsCustomer> {

    List<SysBmsCustomer> findPage(SysBmsCustomer entity);

    List<SysBmsCustomer> findGrid(SysBmsCustomer entity);

    SysBmsCustomer getByCode(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);

    void batchInsert(List<SysBmsCustomer> list);

    void remove(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);
}