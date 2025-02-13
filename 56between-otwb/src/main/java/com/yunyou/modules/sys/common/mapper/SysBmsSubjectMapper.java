package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysBmsSubject;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSubjectEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用科目MAPPER接口
 */
@MyBatisMapper
public interface SysBmsSubjectMapper extends BaseMapper<SysBmsSubject> {

    List<SysBmsSubjectEntity> findPage(SysBmsSubjectEntity entity);

    List<SysBmsSubjectEntity> findGrid(SysBmsSubjectEntity entity);

    SysBmsSubject getByCode(@Param("code") String code, @Param("dataSet") String dataSet);
}