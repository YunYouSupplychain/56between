package com.yunyou.modules.oms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.oms.basic.entity.OmSkuClassification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface OmSkuClassificationMapper extends BaseMapper<OmSkuClassification> {

    List<OmSkuClassification> findPage(OmSkuClassification entity);

    List<OmSkuClassification> findGrid(OmSkuClassification entity);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}
