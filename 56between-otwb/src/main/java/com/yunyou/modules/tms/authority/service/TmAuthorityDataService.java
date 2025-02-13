package com.yunyou.modules.tms.authority.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.entity.TmAuthorityData;
import com.yunyou.modules.tms.authority.mapper.TmAuthorityDataMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yunyou.modules.sys.OfficeType.*;

/**
 * TMS身份权限数据Service
 * 当期含有4种身份：组织中心、调度中心、网点、承运商
 */
@Service
@Transactional(readOnly = true)
public class TmAuthorityDataService extends BaseService {
    @Autowired
    private TmAuthorityDataMapper mapper;

    public List<TmAuthorityData> findList(TmAuthorityData tmAuthorityData) {
        List<TmAuthorityData> rsList = Lists.newArrayList();

        if (tmAuthorityData.getType() == null) {
            // 组织中心
            List<TmAuthorityData> orgCenterData = mapper.findOrgCenterData(tmAuthorityData);
            if (CollectionUtil.isNotEmpty(orgCenterData)) {
                for (TmAuthorityData o : orgCenterData) {
                    o.setType(ORG_CENTER);
                    rsList.add(o);
                }
            }
            // 调度中心
            List<TmAuthorityData> dispatchCenterData = mapper.findDispatchCenterData(tmAuthorityData);
            if (CollectionUtil.isNotEmpty(dispatchCenterData)) {
                for (TmAuthorityData o : dispatchCenterData) {
                    o.setType(DISPATCH_CENTER);
                    rsList.add(o);
                }
            }
            // 网点
            List<TmAuthorityData> outletData = mapper.findOutletData(tmAuthorityData);
            if (CollectionUtil.isNotEmpty(outletData)) {
                for (TmAuthorityData o : outletData) {
                    o.setType(OUTLET);
                    rsList.add(o);
                }
            }
            // 承运商
            List<TmAuthorityData> carrierData = mapper.findCarrierData(tmAuthorityData);
            if (CollectionUtil.isNotEmpty(carrierData)) {
                for (TmAuthorityData o : carrierData) {
                    o.setType(CARRIER);
                    rsList.add(o);
                }
            }
        } else {
            switch (tmAuthorityData.getType()) {
                case ORG_CENTER:
                    rsList = mapper.findOrgCenterData(tmAuthorityData);
                    if (CollectionUtil.isNotEmpty(rsList)) {
                        for (TmAuthorityData o : rsList) {
                            o.setType(ORG_CENTER);
                        }
                    }
                    break;
                case DISPATCH_CENTER:
                    rsList = mapper.findDispatchCenterData(tmAuthorityData);
                    if (CollectionUtil.isNotEmpty(rsList)) {
                        for (TmAuthorityData o : rsList) {
                            o.setType(DISPATCH_CENTER);
                        }
                    }
                    break;
                case OUTLET:
                    rsList = mapper.findOutletData(tmAuthorityData);
                    if (CollectionUtil.isNotEmpty(rsList)) {
                        for (TmAuthorityData o : rsList) {
                            o.setType(OUTLET);
                        }
                    }
                    break;
                case CARRIER:
                    rsList = mapper.findCarrierData(tmAuthorityData);
                    if (CollectionUtil.isNotEmpty(rsList)) {
                        for (TmAuthorityData o : rsList) {
                            o.setType(CARRIER);
                        }
                    }
                    break;
                default:
            }
        }
        return rsList;
    }

    public void validator(TmAuthorityData tmAuthorityData) {
        if (tmAuthorityData.getType() == null) {
            throw new TmsException("type is not null");
        }
        if (StringUtils.isBlank(tmAuthorityData.getTableName())) {
            throw new TmsException("tableName is not null");
        }
        if (StringUtils.isBlank(tmAuthorityData.getBusinessId())) {
            throw new TmsException("businessId is not null");
        }
        if (StringUtils.isBlank(tmAuthorityData.getRelationId())) {
            throw new TmsException("relationId is not null");
        }
    }

    @Transactional
    public void insert(TmAuthorityData tmAuthorityData) {
        switch (tmAuthorityData.getType()) {
            case ORG_CENTER:
                mapper.insertOrgCenterData(tmAuthorityData);
                break;
            case DISPATCH_CENTER:
                mapper.insertDispatchCenterData(tmAuthorityData);
                break;
            case OUTLET:
                mapper.insertOutletData(tmAuthorityData);
                break;
            case CARRIER:
                mapper.insertCarrierData(tmAuthorityData);
                break;
            default:
        }
    }

    @Transactional
    public void delete(TmAuthorityData tmAuthorityData) {
        List<TmAuthorityData> list = this.findList(tmAuthorityData);
        for (TmAuthorityData o : list) {
            switch (o.getType()) {
                case ORG_CENTER:
                    mapper.deleteOrgCenterData(o);
                    break;
                case DISPATCH_CENTER:
                    mapper.deleteDispatchCenterData(o);
                    break;
                case OUTLET:
                    mapper.deleteOutletData(o);
                    break;
                case CARRIER:
                    mapper.deleteCarrierData(o);
                    break;
                default:
            }
        }
    }
}
