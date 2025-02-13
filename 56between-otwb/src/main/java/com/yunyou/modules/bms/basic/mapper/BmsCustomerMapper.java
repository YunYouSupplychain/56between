package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-06-11
 */
@MyBatisMapper
public interface BmsCustomerMapper extends BaseMapper<BmsCustomer> {

    List<BmsCustomer> findPage(BmsCustomer entity);

    List<BmsCustomer> findGrid(BmsCustomer entity);

    BmsCustomer getByCodeAndType(@Param("customerNo") String customerNo, @Param("type") String type, @Param("orgId") String orgId);

    BmsCustomer getByCode(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    void remove(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    void batchInsert(List<BmsCustomer> list);

    String getRegionCode(@Param("customerNo") String customerNo, @Param("orgId") String orgId);
}