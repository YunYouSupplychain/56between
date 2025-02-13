package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsTransportScope;
import com.yunyou.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务服务范围MAPPER接口
 */
@MyBatisMapper
public interface SysTmsTransportScopeMapper extends BaseMapper<SysTmsTransportScope> {

    List<SysTmsTransportScope> findPage(SysTmsTransportScope entity);

    List<SysTmsTransportScope> findGrid(SysTmsTransportScope entity);

    List<SysTmsTransportScope> findSync(SysTmsTransportScope entity);

    List<Area> findArea(String id);

    void insertArea(SysTmsTransportScope entity);

    void deleteAreaByScope(String id);

    List<Area> findAreaByScopeCode(@Param("code") String code, @Param("dataSet") String dataSet);
}