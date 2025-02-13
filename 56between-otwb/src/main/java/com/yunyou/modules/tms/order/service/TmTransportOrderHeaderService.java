package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmTransportOrderHeader;
import com.yunyou.modules.tms.order.mapper.TmTransportOrderHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 运输订单基本信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderHeaderService extends CrudService<TmTransportOrderHeaderMapper, TmTransportOrderHeader> {

    public TmTransportOrderHeader getByNo(String transportNo) {
        return mapper.getByNo(transportNo);
    }

    public TmTransportOrderHeader getByCustomerNo(String customerNo, String orgId) {
        return mapper.getByCustomerNo(customerNo, orgId);
    }

    public Integer findUnSignCount() {
        return mapper.findUnSignCount();
    }

    public List<TmTransportOrderHeader> findUnSignMailNo(int offset, int num) {
        return mapper.findUnSignMailNo(offset, num);
    }

}