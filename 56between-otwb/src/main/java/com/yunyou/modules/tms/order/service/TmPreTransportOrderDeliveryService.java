package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderDeliveryEntity;
import com.yunyou.modules.tms.order.mapper.TmPreTransportOrderDeliveryMapper;
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
public class TmPreTransportOrderDeliveryService extends CrudService<TmPreTransportOrderDeliveryMapper, TmPreTransportOrderDelivery> {
    @Autowired
    private TmTransportObjService tmTransportObjService;

    public TmPreTransportOrderDelivery getByNo(String transportNo, String orgId) {
        return mapper.getByNo(transportNo, orgId);
    }

    public TmPreTransportOrderDeliveryEntity getEntity(String transportNo, String orgId) {
        TmPreTransportOrderDeliveryEntity entity = mapper.getEntityByNo(transportNo, orgId);
        if (entity != null) {
            TmTransportObjEntity carrier = tmTransportObjService.getEntity(entity.getCarrierCode(), entity.getBaseOrgId());
            if (carrier != null) {
                entity.setCarrierName(carrier.getTransportObjName());
            }
        }
        return entity;
    }
}