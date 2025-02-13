package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.SysControlParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统控制参数MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-08-12
 */
@MyBatisMapper
public interface SysControlParamsMapper extends BaseMapper<SysControlParams> {

    List<SysControlParams> findPage(SysControlParams entity);

    SysControlParams getByCode(@Param("code") String code, @Param("orgId") String orgId);

    SysControlParams getByOnlyCode(@Param("code") String code);
}