package com.yunyou.modules.interfaces.edi.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.interfaces.edi.entity.EdiDispatchOrderInfo;
import com.yunyou.modules.interfaces.edi.entity.EdiSendOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface EdiSendOrderInfoMapper {

    void save(EdiSendOrderInfo log);

    List<EdiSendOrderInfo> findList(EdiSendOrderInfo log);

    List<EdiSendOrderInfo> findUnSendDataByType(@Param("ediType") String ediType);

    void deleteByIds(@Param("ids") List<String> ids);

    List<EdiDispatchOrderInfo> findUnSendDataByDo(@Param("baseOrgId") String baseOrgId);

}
