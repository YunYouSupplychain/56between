package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysOmsBusinessServiceScope;
import com.yunyou.modules.sys.entity.Area;

import java.util.List;

/**
 * 业务服务范围MAPPER接口
 */
@MyBatisMapper
public interface SysOmsBusinessServiceScopeMapper extends BaseMapper<SysOmsBusinessServiceScope> {

    List<SysOmsBusinessServiceScope> findPage(SysOmsBusinessServiceScope entity);

    List<SysOmsBusinessServiceScope> findGrid(SysOmsBusinessServiceScope entity);

    List<SysOmsBusinessServiceScope> findSync(SysOmsBusinessServiceScope entity);

    List<Area> getArea(String id);

    void deleteArea(SysOmsBusinessServiceScope entity);

    void insertArea(SysOmsBusinessServiceScope entity);
}