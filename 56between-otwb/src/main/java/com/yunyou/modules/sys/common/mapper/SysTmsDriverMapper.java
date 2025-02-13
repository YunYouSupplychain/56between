package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysTmsDriver;
import com.yunyou.modules.sys.common.entity.extend.SysTmsDriverEntity;

import java.util.List;

/**
 * 运输人员信息MAPPER接口
 */
@MyBatisMapper
public interface SysTmsDriverMapper extends BaseMapper<SysTmsDriver> {

    SysTmsDriverEntity getEntity(String id);

    List<SysTmsDriverEntity> findPage(SysTmsDriverEntity entity);

    List<SysTmsDriverEntity> findGrid(SysTmsDriverEntity entity);

    List<SysTmsDriver> findSync(SysTmsDriver entity);
}