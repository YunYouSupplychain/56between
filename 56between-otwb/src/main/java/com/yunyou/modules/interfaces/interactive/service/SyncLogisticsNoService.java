package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SyncLogisticsNoService extends BaseService {
    @Autowired
    protected BanQinWmSoAllocService wmSoAllocService;
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchroWayBillNo(String taskNo, String soNo, String orgId) {
        if (StringUtils.isBlank(soNo) || StringUtils.isBlank(taskNo) || StringUtils.isBlank(orgId)) {
            return;
        }
        List<BanQinWmSoAllocEntity> allocEntityList = wmSoAllocService.getEntityBySoNo(soNo, orgId);
        String logisticsNo = allocEntityList.stream().map(BanQinWmSoAlloc::getTrackingNo).filter(StringUtils::isNotBlank).distinct().collect(Collectors.joining(","));
        if (StringUtils.isBlank(logisticsNo)) {
            return;
        }
        OmTaskHeader qEntity = new OmTaskHeader();
        qEntity.setTaskNo(taskNo);
        qEntity.setWarehouse(orgId);
        List<OmTaskHeader> toOmsTaskList = omTaskHeaderService.findList(qEntity);
        if (CollectionUtil.isEmpty(toOmsTaskList) || toOmsTaskList.size() <= 0) {
            return;
        }
        OmTaskHeader toOmsItem = toOmsTaskList.get(0);
        toOmsItem.setLogisticsNo(logisticsNo);
        omTaskHeaderService.save(toOmsItem);

        List<OmTaskHeader> toTmsTaskList = omTaskHeaderService.getBatchTask(OmsConstants.OMS_TASK_TYPE_03, Collections.singletonList(toOmsItem.getLotNum()), toOmsItem.getOrgId());
        for (OmTaskHeader toTmsItem : toTmsTaskList) {
            tmTransportOrderManager.updateLogisticsNo(toTmsItem.getTaskNo(), logisticsNo, toTmsItem.getWarehouse());
        }
    }

}
