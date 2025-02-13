package com.yunyou.modules.sys.common.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.common.entity.SysCommonSkuClassification;
import com.yunyou.modules.sys.common.entity.extend.SysCommonSkuClassificationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface SysCommonSkuClassificationMapper extends BaseMapper<SysCommonSkuClassification> {
    SysCommonSkuClassificationEntity getEntity(@Param("id") String id);

    List<SysCommonSkuClassificationEntity> findPage(SysCommonSkuClassificationEntity entity);

    List<SysCommonSkuClassification> findGrid(SysCommonSkuClassification entity);

    List<SysCommonSkuClassification> findSync(SysCommonSkuClassificationEntity entity);
}
