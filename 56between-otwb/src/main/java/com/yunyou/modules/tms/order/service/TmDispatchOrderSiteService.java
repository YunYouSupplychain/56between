package com.yunyou.modules.tms.order.service;

import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderSite;
import com.yunyou.modules.tms.order.mapper.TmDispatchOrderSiteMapper;

/**
 * 派车单配送点Service
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderSiteService extends CrudService<TmDispatchOrderSiteMapper, TmDispatchOrderSite> {

    public TmDispatchOrderSite getByDispatchNoAndOutletCode(String dispatchNo, String outletCode, String orgId) {
        return mapper.getByDispatchNoAndOutletCode(dispatchNo, outletCode, orgId);
    }

    public TmDispatchOrderSiteEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

}