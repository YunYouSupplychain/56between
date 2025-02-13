package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonCustomerContacts;

import java.util.List;

/**
 * 客户联系人MAPPER接口
 */
@MyBatisMapper
public interface SysCommonCustomerContactsMapper extends BaseMapper<SysCommonCustomerContacts> {

    List<SysCommonCustomerContacts> findByCustomerIds(List<String> list);

    List<SysCommonCustomerContacts> findByHeadId(String headId);

    void batchInsert(List<SysCommonCustomerContacts> list);

    void deleteByCustomerIds(List<String> list);
}