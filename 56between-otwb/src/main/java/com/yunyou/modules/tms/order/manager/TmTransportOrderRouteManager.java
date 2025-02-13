package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@Transactional(readOnly = true)
public class TmTransportOrderRouteManager extends BaseService {
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;

    @Transactional
    public void remove(TmTransportOrderRoute entity) {
        if (!TmsConstants.NULL.equals(entity.getPreAllocDispatchNo()) && TmsConstants.NULL.equals(entity.getDispatchNo())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]调度配载中，无法操作", entity.getTransportNo(), entity.getLabelNo(), entity.getDispatchNo()));
        }
        if (!TmsConstants.NULL.equals(entity.getDispatchNo())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签[{1}]在途，无法操作", entity.getTransportNo(), entity.getLabelNo()));
        }
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), entity.getId());
        tmTransportOrderRouteService.delete(entity);
    }
}
