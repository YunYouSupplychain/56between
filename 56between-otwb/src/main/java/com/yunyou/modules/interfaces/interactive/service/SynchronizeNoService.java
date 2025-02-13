package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.service.TmTransportOrderDeliveryService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SynchronizeNoService extends BaseService {
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchroWayBillNo(String taskNo, String soNo, String orgId, String carrierCode, String carrierName) {
        if (StringUtils.isBlank(soNo) || StringUtils.isBlank(taskNo) || StringUtils.isBlank(orgId)) {
            return;
        }
        List<BanQinWmSoAllocEntity> allocEntityList = wmSoAllocService.getEntityBySoNo(soNo, orgId);
        List<String> wayBillNoList = allocEntityList.stream().map(BanQinWmSoAlloc::getTrackingNo).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(wayBillNoList)) {
            return;
        }
        String wayBillNo = wayBillNoList.stream().filter(StringUtils::isNotBlank).distinct().sorted().collect(Collectors.joining(","));
        OmTaskHeader toOmsItem = omTaskHeaderService.getByTaskNoAndWarehouse(taskNo, orgId);
        if (toOmsItem == null) {
            return;
        }
        toOmsItem.setLogisticsNo(wayBillNo);
        omTaskHeaderService.save(toOmsItem);

        OmTaskHeader condition = new OmTaskHeader();
        condition.setLotNum(toOmsItem.getLotNum());
        condition.setTaskType(OmsConstants.OMS_TASK_TYPE_03);
        condition.setOrgId(toOmsItem.getOrgId());
        List<OmTaskHeader> toTmsTaskList = omTaskHeaderService.findList(condition);
        if (CollectionUtil.isEmpty(toTmsTaskList)) {
            return;
        }
        OmTaskHeader toTmsItem = toTmsTaskList.get(0);

        TmTransportOrderHeader query = new TmTransportOrderHeader();
        query.setSourceNo(toTmsItem.getTaskNo());
        query.setIsNeedDispatch(Global.N);
        query.setDataSource(SystemAliases.OMS.getCode());
        query.setOrgId(toTmsItem.getWarehouse());
        List<TmTransportOrderHeader> list = tmTransportOrderHeaderService.findList(query);
        for (TmTransportOrderHeader o : list) {
            o.setTrackingNo(wayBillNo);
            tmTransportOrderHeaderService.save(o);

            TmTransportOrderDelivery orderDelivery = tmTransportOrderDeliveryService.getByNo(o.getTransportNo(), o.getOrgId());
            orderDelivery.setCarrierCode(carrierCode);
            tmTransportOrderDeliveryService.save(orderDelivery);
        }
    }

}
