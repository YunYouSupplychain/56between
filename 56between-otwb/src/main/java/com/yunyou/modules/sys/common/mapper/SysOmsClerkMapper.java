package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsClerk;

import java.util.List;

/**
 * 业务员MAPPER接口
 */
@MyBatisMapper
public interface SysOmsClerkMapper extends BaseMapper<SysOmsClerk> {

    List<SysOmsClerk> findPage(SysOmsClerk entity);

    List<SysOmsClerk> findGrid(SysOmsClerk entity);
}