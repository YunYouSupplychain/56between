package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装MAPPER接口
 *
 * @author WMJ
 * @version 2019-04-19
 */
@MyBatisMapper
public interface OmPackageMapper extends BaseMapper<OmPackage> {

    List<OmPackage> findPage(OmPackage entity);

    OmPackage findByPackageCode(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void remove(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void batchInsert(List<OmPackage> list);
}