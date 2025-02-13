package com.yunyou.modules.interfaces.edi.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.interfaces.edi.entity.EdiInvInfo;
import com.yunyou.modules.interfaces.edi.entity.EdiPlanInvInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface EdiMapper {

    List<EdiInvInfo> findInvEdi(@Param("orgId") String orgId);

    List<EdiPlanInvInfo> findPlanInvEdi(@Param("orgId") String orgId);
}