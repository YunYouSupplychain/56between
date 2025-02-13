package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmDriverQualification;
import org.apache.ibatis.annotations.Param;

/**
 * 运输人员资质信息MAPPER接口
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@MyBatisMapper
public interface TmDriverQualificationMapper extends BaseMapper<TmDriverQualification> {

    void remove(@Param("driverCode") String driverCode, @Param("orgId") String orgId);
}