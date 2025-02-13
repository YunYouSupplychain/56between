package com.yunyou.modules.bms.finance.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetailParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailParamsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface BmsSettleModelDetailParamsMapper extends BaseMapper<BmsSettleModelDetailParams> {

    void deleteByFkId(@Param("fkId") String fkId);

    List<BmsSettleModelDetailParamsEntity> findByFkIdAndIOE(@Param("fkId") String fkId, @Param("includeOrExclude") String includeOrExclude);
}
