package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmDriver;
import com.yunyou.modules.tms.basic.entity.extend.TmDriverEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运输人员信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmDriverMapper extends BaseMapper<TmDriver> {

    TmDriverEntity getEntity(String id);

    List<TmDriverEntity> findPage(TmDriver tmDriver);

    List<TmDriverEntity> findGrid(TmDriverEntity entity);

    TmDriver getByCode(@Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}