package com.yunyou.modules.tms.order.service;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.basic.entity.TmVehicleType;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.basic.service.TmVehicleTypeService;
import com.yunyou.modules.tms.order.entity.TmTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderDeliveryEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportReceiptInfo;
import com.yunyou.modules.tms.order.entity.extend.TmTransportSignInfo;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderDeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运输订单配送信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderDeliveryService extends CrudService<TmTransportOrderDeliveryMapper, TmTransportOrderDelivery> {
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private TmVehicleTypeService tmVehicleTypeService;

    public TmTransportOrderDelivery getByNo(String transportNo, String orgId) {
        return mapper.getByNo(transportNo, orgId);
    }

    public TmTransportOrderDeliveryEntity getEntity(String transportNo, String orgId) {
        TmTransportOrderDeliveryEntity entity = mapper.getEntityByNo(transportNo, orgId);
        if (entity != null) {
            if (StringUtils.isNotBlank(entity.getCarrierCode())) {
                TmTransportObjEntity carrier = tmTransportObjService.getEntity(entity.getCarrierCode(), entity.getBaseOrgId());
                if (carrier != null) {
                    entity.setCarrierName(carrier.getTransportObjName());
                }
            }
            if (StringUtils.isNotBlank(entity.getCarType())) {
                TmVehicleType tmVehicleType = tmVehicleTypeService.getByNo(entity.getCarType(), entity.getBaseOrgId());
                if (tmVehicleType != null) {
                    entity.setCarTypeName(tmVehicleType.getName());
                }
            }
        }
        return entity;
    }

    @Transactional
    public void updateSignInfo(TmTransportSignInfo tmTransportSignInfo) {
        TmTransportOrderDelivery orderDelivery = this.getByNo(tmTransportSignInfo.getTransportNo(), tmTransportSignInfo.getOrgId());
        if (orderDelivery == null) {
            return;
        }
        orderDelivery.setSignStatus(Global.Y);
        orderDelivery.setSignBy(tmTransportSignInfo.getSignBy());
        orderDelivery.setSignTime(tmTransportSignInfo.getSignTime());
        orderDelivery.setSignRemarks(tmTransportSignInfo.getRemarks());
        this.save(orderDelivery);
    }

    @Transactional
    public void updateReceiptInfo(TmTransportReceiptInfo tmTransportReceiptInfo) {
        TmTransportOrderDelivery orderDelivery = this.getByNo(tmTransportReceiptInfo.getTransportNo(), tmTransportReceiptInfo.getOrgId());
        if (orderDelivery == null) {
            return;
        }
        orderDelivery.setReceiptStatus(Global.Y);
        orderDelivery.setReceiptBy(tmTransportReceiptInfo.getReceiptBy());
        orderDelivery.setReceiptTime(tmTransportReceiptInfo.getReceiptTime());
        orderDelivery.setReceiptRemarks(tmTransportReceiptInfo.getRemarks());
        this.save(orderDelivery);
    }
}