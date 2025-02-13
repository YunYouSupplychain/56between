package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmTransportOrderLabel;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderLabelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输订单标签信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderLabelService extends CrudService<TmTransportOrderLabelMapper, TmTransportOrderLabel> {

    public String getNewNo(String transportNo, String orgId) {
        String newNo;
        String maxNo = mapper.getMaxNo(transportNo, orgId);
        if (StringUtils.isBlank(maxNo) || transportNo.equals(maxNo)) {
            newNo = transportNo + String.format("%06d", 1);
        } else {
            newNo = transportNo + String.format("%06d", Integer.parseInt(maxNo.substring(transportNo.length())) + 1);
        }
        return newNo;
    }

    public TmTransportOrderLabel getByTransportNoAndLabelNo(String transportNo, String labelNo, String orgId) {
        return mapper.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
    }

    public List<TmTransportOrderLabel> findHasCanReceiveLabel(String transportNo, String orgId) {
        TmTransportOrderLabel condition = new TmTransportOrderLabel();
        condition.setOrgId(orgId);
        condition.setTransportNo(transportNo);
        condition.setStatus("10");
        return super.findList(condition);
    }

}