package com.yunyou.modules.wms.basicdata.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.wms.basicdata.entity.CdWhSkuClassification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface CdWhSkuClassificationMapper extends BaseMapper<CdWhSkuClassification> {

    List<CdWhSkuClassification> findPage(CdWhSkuClassification entity);

    List<CdWhSkuClassification> findGrid(CdWhSkuClassification entity);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}
