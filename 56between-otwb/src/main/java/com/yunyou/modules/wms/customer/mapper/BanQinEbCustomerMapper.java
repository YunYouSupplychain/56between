package com.yunyou.modules.wms.customer.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinEbCustomerMapper extends BaseMapper<BanQinEbCustomer> {
    List<BanQinEbCustomer> findPage(BanQinEbCustomer ebCustomer);

    BanQinEbCustomer find(@Param("customerNo") String customerNo, @Param("type") String type, @Param("orgId") String orgId);

    List<BanQinEbCustomer> getExistCustomer(@Param("ownerCodeList") List<String> ownerCodeList, @Param("orgId") String orgId);

    BanQinEbCustomer getByCode(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    void remove(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    void batchInsert(List<BanQinEbCustomer> list);
}