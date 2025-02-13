package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonCustomer;
import com.yunyou.modules.sys.common.entity.extend.SysCommonCustomerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户信息MAPPER接口
 */
@MyBatisMapper
public interface SysCommonCustomerMapper extends BaseMapper<SysCommonCustomer> {

    List<SysCommonCustomerEntity> findPage(SysCommonCustomerEntity entity);

    List<SysCommonCustomerEntity> findGrid(SysCommonCustomerEntity entity);

    List<SysCommonCustomer> findSync(SysCommonCustomerEntity entity);

    SysCommonCustomerEntity getEntity(String id);

    List<SysCommonCustomer> getByIds(@Param("ids") List<String> ids);

    SysCommonCustomer getByCode(@Param("code") String code, @Param("dataSet") String dataSet);

    void batchInsert(List<SysCommonCustomer> list);
}