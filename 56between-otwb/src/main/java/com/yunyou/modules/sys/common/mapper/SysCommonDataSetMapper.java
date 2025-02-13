package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;
import com.yunyou.modules.sys.common.entity.extend.SysCommonDataSetEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据套MAPPER接口
 */
@MyBatisMapper
public interface SysCommonDataSetMapper extends BaseMapper<SysCommonDataSet> {
    List<SysCommonDataSet> findPage(SysCommonDataSet entity);

    List<SysCommonDataSet> findGrid(SysCommonDataSet entity);

    SysCommonDataSet getByCode(@Param("code") String code);

    SysCommonDataSet getDefault();

    SysCommonDataSetEntity getEntity(String id);
}