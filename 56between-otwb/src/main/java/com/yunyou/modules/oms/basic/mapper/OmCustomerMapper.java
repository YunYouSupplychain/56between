package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmCustomer;
import com.yunyou.modules.oms.basic.entity.extend.OmCustomerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface OmCustomerMapper extends BaseMapper<OmCustomer> {

    List<OmCustomerEntity> findPage(OmCustomerEntity entity);

    List<OmCustomerEntity> findGrid(OmCustomerEntity entity);

    OmCustomerEntity getEntity(String id);

    void remove(@Param("customerNo") String customerNo, @Param("orgId") String orgId);

    void batchInsert(List<OmCustomer> list);
}