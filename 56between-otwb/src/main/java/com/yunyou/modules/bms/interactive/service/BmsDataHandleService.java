package com.yunyou.modules.bms.interactive.service;

import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.bms.business.entity.*;
import com.yunyou.modules.bms.business.service.*;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：BMS数据处理Service
 *
 * @author Jianhua
 * @version 2019/6/4
 */
@Service
@Transactional(readOnly = true)
public class BmsDataHandleService extends BaseService {
    @Autowired
    private BmsInboundDataService bmsInboundDataService;
    @Autowired
    private BmsOutboundDataService bmsOutboundDataService;
    @Autowired
    private BmsInventoryDataService bmsInventoryDataService;
    @Autowired
    private BmsWaybillDataService bmsWaybillDataService;
    @Autowired
    private BmsDispatchOrderDataService bmsDispatchOrderDataService;
    @Autowired
    private BmsDispatchDataService bmsDispatchDataService;
    @Autowired
    private BmsExceptionDataService bmsExceptionDataService;
    @Autowired
    private BmsReturnDataService bmsReturnDataService;
    // 拉取WMS数据服务
    @Autowired
    private BmsPullFromWmsService bmsPullFromWmsService;
    // 拉取TMS数据服务
    @Autowired
    private BmsPullFromTmsService bmsPullFromTmsService;

    /**
     * 描述：拉取入库数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void inboundDataHandle(BmsPullDataCondition condition) {
        bmsInboundDataService.deleteByCondition(condition);
        List<BmsInboundData> data = Lists.newArrayList();
        if (SystemAliases.WMS.getCode().equals(condition.getDataSources())) {
            data = bmsPullFromWmsService.pullInboundData(condition);
        } else if (StringUtils.isBlank(condition.getDataSources())) {
            data.addAll(bmsPullFromWmsService.pullInboundData(condition));
        }
        data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsInboundDataService.batchInsert(data);
    }

    /**
     * 描述：拉取出库数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void outboundDataHandle(BmsPullDataCondition condition) {
        bmsOutboundDataService.deleteByCondition(condition);
        List<BmsOutboundData> data = Lists.newArrayList();
        if (SystemAliases.WMS.getCode().equals(condition.getDataSources())) {
            data = bmsPullFromWmsService.pullOutboundData(condition);
        } else if (StringUtils.isBlank(condition.getDataSources())) {
            data.addAll(bmsPullFromWmsService.pullOutboundData(condition));
        }
        data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsOutboundDataService.batchInsert(data);
    }

    /**
     * 描述：拉取库存数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void inventoryDataHandle(BmsPullDataCondition condition) {
        bmsInventoryDataService.deleteByCondition(condition);
        List<BmsInventoryData> data = Lists.newArrayList();
        if (SystemAliases.WMS.getCode().equals(condition.getDataSources())) {
            data = bmsPullFromWmsService.pullInventoryData(condition);
        } else if (StringUtils.isBlank(condition.getDataSources())) {
            data.addAll(bmsPullFromWmsService.pullInventoryData(condition));
        }
        data = data.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsInventoryDataService.batchInsert(data);
    }

    /**
     * 描述：拉取运单数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void waybillDataHandle(BmsPullDataCondition condition) {
        bmsWaybillDataService.deleteByCondition(condition);
        List<BmsWaybillData> data = bmsPullFromTmsService.pullWaybillData(condition);
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsWaybillDataService.batchInsert(data);
    }

    /**
     * 描述：拉去派车单数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void dispatchOrderDataHandle(BmsPullDataCondition condition) {
        bmsDispatchOrderDataService.deleteByCondition(condition);
        List<BmsDispatchOrderData> data = bmsPullFromTmsService.pullDispatchOrderData(condition);
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsDispatchOrderDataService.batchInsert(data);
    }

    /**
     * 描述：拉取派车配载数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void dispatchDataHandle(BmsPullDataCondition condition) {
        bmsDispatchDataService.deleteByCondition(condition);
        List<BmsDispatchData> data = bmsPullFromTmsService.pullDispatchData(condition);
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        bmsDispatchDataService.batchInsert(data);
    }
}
