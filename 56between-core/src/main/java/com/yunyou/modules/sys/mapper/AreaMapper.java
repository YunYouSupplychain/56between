package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.TreeMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域MAPPER接口
 *
 * @author yunyou
 * @version 2017-05-16
 */
@MyBatisMapper
public interface AreaMapper extends TreeMapper<Area> {

    List<Area> getAreaByUserId(@Param(value = "userId") String userId);

    List<Area> getAreaByOfficeId(@Param(value = "officeId") String officeId);

    List<Area> findCountToArea();

    List<Area> findAreaByName(@Param(value = "name") String name);

    Area getByCode(@Param("code") String code);
}
