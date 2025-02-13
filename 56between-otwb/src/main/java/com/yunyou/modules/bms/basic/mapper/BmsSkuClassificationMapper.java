package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsSkuClassification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface BmsSkuClassificationMapper extends BaseMapper<BmsSkuClassification> {

    List<BmsSkuClassification> findPage(BmsSkuClassification entity);

    List<BmsSkuClassification> findGrid(BmsSkuClassification entity);

    void remove(@Param("code") String code, @Param("orgId") String orgId);

}
