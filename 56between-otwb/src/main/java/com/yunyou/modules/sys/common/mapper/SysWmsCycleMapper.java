package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysWmsCycle;

import java.util.List;

/**
 * 循环级别MAPPER接口
 */
@MyBatisMapper
public interface SysWmsCycleMapper extends BaseMapper<SysWmsCycle> {
    List<SysWmsCycle> findPage(SysWmsCycle entity);
    List<SysWmsCycle> findGrid(SysWmsCycle entity);
}