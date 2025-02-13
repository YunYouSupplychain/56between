package com.yunyou.modules.oms.order.manager;

import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.service.*;
import com.yunyou.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应链订单删除Service
 *
 * @author liujianhua
 * @version 2022.7.29
 */
@Service
@Transactional(readOnly = true)
public class OmChainRemoveManager {
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    @Autowired
    private OmOrderAppendixService omOrderAppendixService;
    @Autowired
    private OmChainDetailService omChainDetailService;
    @Autowired
    private OmOrderDetailAppendixService omOrderDetailAppendixService;
    @Autowired
    private OmPoHeaderService omPoHeaderService;
    @Autowired
    private OmSaleHeaderService omSaleHeaderService;

    @Transactional
    public void delete(OmChainHeader omChainHeader, User operator) {
        if (!(OmsConstants.OMS_CO_STATUS_00.equals(omChainHeader.getStatus()))) {
            throw new OmsException("非新建状态，不能删除");
        }
        omChainHeaderService.delete(omChainHeader);
        omOrderAppendixService.deleteByOrderNo(omChainHeader.getChainNo(), omChainHeader.getOrgId());
        omChainDetailService.delete(new OmChainDetail(null, omChainHeader.getId()));
        omOrderDetailAppendixService.deleteByOrderNoAndLine(omChainHeader.getChainNo(), null, omChainHeader.getOrgId());

        // 更新来源订单状态
        this.updateStatusById(omChainHeader.getSourceOrderId(), omChainHeader.getSourceOrderType(), operator);
    }

    @Transactional
    public void updateStatusById(String sourceOrderId, String sourceOrderType, User operator) {
        if (OmsConstants.OMS_SOURCE_TYPE_PO.equals(sourceOrderType)) {
            omPoHeaderService.updateStatusById(sourceOrderId, OmsConstants.OMS_PO_STATUS_30, operator);
        } else if (OmsConstants.OMS_SOURCE_TYPE_SO.equals(sourceOrderType)) {
            omSaleHeaderService.updateStatusById(sourceOrderId, OmsConstants.OMS_SO_STATUS_30, operator);
        }
    }
}
