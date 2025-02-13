package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsContractDetailTermsParams;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface BmsContractDetailTermsParamsMapper extends BaseMapper<BmsContractDetailTermsParams> {

    void deleteByFkId(@Param("fkId") String fkId);

    void deleteByContract(@Param("sysContractNo") String sysContractNo, @Param("orgId") String orgId);

    List<BmsContractDetailTermsParamsEntity> findByFkIdAndIOE(@Param("fkId") String fkId, @Param("includeOrExclude") String includeOrExclude);
}
