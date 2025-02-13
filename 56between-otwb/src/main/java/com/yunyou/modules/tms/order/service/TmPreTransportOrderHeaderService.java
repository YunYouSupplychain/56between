package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderHeader;
import com.yunyou.modules.tms.order.mapper.TmPreTransportOrderHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运输订单基本信息Service
 *
 * @author liujianhua
 * @version 2020-03-04
 */
@Service
@Transactional(readOnly = true)
public class TmPreTransportOrderHeaderService extends CrudService<TmPreTransportOrderHeaderMapper, TmPreTransportOrderHeader> {

    public TmPreTransportOrderHeader getByNo(String transportNo) {
        return mapper.getByNo(transportNo);
    }
}