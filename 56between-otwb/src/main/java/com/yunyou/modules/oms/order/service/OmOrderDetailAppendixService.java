package com.yunyou.modules.oms.order.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmChainDetail;
import com.yunyou.modules.oms.order.entity.OmOrderDetailAppendix;
import com.yunyou.modules.oms.order.mapper.OmOrderDetailAppendixMapper;
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
public class OmOrderDetailAppendixService extends CrudService<OmOrderDetailAppendixMapper, OmOrderDetailAppendix> {

    public OmOrderDetailAppendix getByChainNoAndLineNo(String chainNo, String lineNo, String orgId) {
        OmOrderDetailAppendix con = new OmOrderDetailAppendix();
        con.setOrderNo(chainNo);
        con.setLineNo(lineNo);
        con.setOrgId(orgId);
        List<OmOrderDetailAppendix> list = mapper.findList(con);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public OmOrderDetailAppendix initChainDetailInfo(OmChainDetail omChainDetail) {
        OmOrderDetailAppendix entity = new OmOrderDetailAppendix();
        entity.setOrderNo(omChainDetail.getChainNo());
        entity.setLineNo(omChainDetail.getLineNo());
        entity.setSkuCode(omChainDetail.getSkuCode());
        entity.setOrgId(omChainDetail.getOrgId());
        return entity;
    }

    @Transactional
    public void deleteByOrderNoAndLine(String orderNo, String lineNo, String orgId) {
        mapper.deleteByOrderNoAndLine(orderNo, lineNo, orgId);
    }
}