package com.yunyou.modules.bms.interactive.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.BmsDispatchOrderData;
import com.yunyou.modules.bms.business.entity.BmsWaybillData;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.mapper.BmsPullFromTmsMapper;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：BMS拉取TMS数据Service
 *
 * @author Jianhua
 * @version 2019/6/3
 */
@Service
@Transactional(readOnly = true)
public class BmsPullFromTmsService extends BaseService {
    @Autowired
    private BmsPullFromTmsMapper mapper;
    @Autowired
    private AreaService areaService;

    /**
     * 描述：拉取运单数据
     */
    public List<BmsWaybillData> pullWaybillData(BmsPullDataCondition condition) {
        List<BmsWaybillData> list = mapper.pullWaybillData(condition);
        for (BmsWaybillData o : list) {
            if (StringUtils.isNotBlank(o.getShipCityId()) && StringUtils.isBlank(o.getShipCityName())) {
                o.setShipCityName(areaService.getFullName(o.getShipCityId()));
            }
            if (StringUtils.isNotBlank(o.getConsigneeCityId()) && StringUtils.isBlank(o.getConsigneeCityName())) {
                o.setConsigneeCityName(areaService.getFullName(o.getConsigneeCityId()));
            }
        }
        return list;
    }

    /**
     * 描述：拉取派车单数据
     */
    public List<BmsDispatchOrderData> pullDispatchOrderData(BmsPullDataCondition condition) {
        List<BmsDispatchOrderData> list = mapper.pullDispatchOrderData(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            for (BmsDispatchOrderData bmsDispatchOrderData : list) {
                bmsDispatchOrderData.setSiteList(mapper.pullDispatchOrderStoreData(bmsDispatchOrderData.getOrderNo(), bmsDispatchOrderData.getOrgId()));
            }
        }
        return list;
    }

    /**
     * 描述：拉取派车数据
     */
    public List<BmsDispatchData> pullDispatchData(BmsPullDataCondition condition) {
        return mapper.pullDispatchData(condition);
    }
}
