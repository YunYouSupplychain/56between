package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包装MAPPER接口
 *
 * @author WMJ
 * @version 2019-01-25
 */
@MyBatisMapper
public interface BanQinCdWhPackageMapper extends BaseMapper<BanQinCdWhPackage> {

    List<BanQinCdWhPackage> findPage(BanQinCdWhPackage cdWhPackage);

    BanQinCdWhPackage findByPackageCode(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void remove(@Param("packCode") String packCode, @Param("orgId") String orgId);

    void batchInsert(List<BanQinCdWhPackage> list);
}