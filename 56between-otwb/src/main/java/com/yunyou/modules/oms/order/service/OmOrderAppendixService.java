package com.yunyou.modules.oms.order.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmChainHeader;
import com.yunyou.modules.oms.order.entity.OmOrderAppendix;
import com.yunyou.modules.oms.order.mapper.OmOrderAppendixMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单补充数据Service
 *
 * @author zyf
 * @version 2021-05-28
 */
@Service
@Transactional(readOnly = true)
public class OmOrderAppendixService extends CrudService<OmOrderAppendixMapper, OmOrderAppendix> {

    public OmOrderAppendix getByChainNo(String chainNo, String orgId) {
        OmOrderAppendix con = new OmOrderAppendix();
        con.setOrderNo(chainNo);
        con.setOrgId(orgId);
        List<OmOrderAppendix> list = mapper.findList(con);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public OmOrderAppendix initChainInfo(OmChainHeader omChainHeader) {
        OmOrderAppendix omOrderAppendix = new OmOrderAppendix();
        omOrderAppendix.setOrderNo(omChainHeader.getChainNo());
        omOrderAppendix.setType(omChainHeader.getBusinessOrderType());
        omOrderAppendix.setOrgId(omChainHeader.getOrgId());
        omOrderAppendix.setCustomerNo(omChainHeader.getCustomerNo());
        omOrderAppendix.setPushOrgId(omChainHeader.getWarehouse());
        return omOrderAppendix;
    }

    @Transactional
    public void deleteByOrderNo(String orderNo, String orgId) {
        mapper.deleteByOrderNo(orderNo, orgId);
    }
}