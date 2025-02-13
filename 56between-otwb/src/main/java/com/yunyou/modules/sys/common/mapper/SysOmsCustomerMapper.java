package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsCustomer;
import com.yunyou.modules.sys.common.entity.extend.SysOmsCustomerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户MAPPER接口
 */
@MyBatisMapper
public interface SysOmsCustomerMapper extends BaseMapper<SysOmsCustomer> {

    SysOmsCustomerEntity getEntity(String id);

    List<SysOmsCustomerEntity> findPage(SysOmsCustomerEntity entity);

    List<SysOmsCustomerEntity> findGrid(SysOmsCustomerEntity entity);

    void batchInsert(List<SysOmsCustomer> list);

    void remove(@Param("customerNo") String customerNo, @Param("dataSet") String dataSet);
}