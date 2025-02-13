package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装MAPPER接口
 *
 * @author WMJ
 * @version 2019-05-30
 */
@MyBatisMapper
public interface SysCommonPackageMapper extends BaseMapper<SysCommonPackage> {

    List<SysCommonPackage> findPage(SysCommonPackage entity);

    List<SysCommonPackage> findGrid(SysCommonPackage entity);

    List<SysCommonPackage> findSync(SysCommonPackage entity);

    SysCommonPackage getByCode(@Param("packCode") String packCode, @Param("dataSet") String dataSet);

    void batchInsert(List<SysCommonPackage> list);
}