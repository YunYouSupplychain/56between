package com.yunyou.modules.tms.authority.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.authority.entity.TmAuthorityData;

import java.util.List;

@MyBatisMapper
public interface TmAuthorityDataMapper {

    List<TmAuthorityData> findOrgCenterData(TmAuthorityData tmAuthorityData);
    List<TmAuthorityData> findDispatchCenterData(TmAuthorityData tmAuthorityData);
    List<TmAuthorityData> findOutletData(TmAuthorityData tmAuthorityData);
    List<TmAuthorityData> findCarrierData(TmAuthorityData tmAuthorityData);
    
    void insertOrgCenterData(TmAuthorityData tmAuthorityData);
    void insertDispatchCenterData(TmAuthorityData tmAuthorityData);
    void insertOutletData(TmAuthorityData tmAuthorityData);
    void insertCarrierData(TmAuthorityData tmAuthorityData);

    void deleteOrgCenterData(TmAuthorityData tmAuthorityData);
    void deleteDispatchCenterData(TmAuthorityData tmAuthorityData);
    void deleteOutletData(TmAuthorityData tmAuthorityData);
    void deleteCarrierData(TmAuthorityData tmAuthorityData);
}
