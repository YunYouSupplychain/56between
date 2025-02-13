package com.yunyou.modules.tms.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.mapper.TmDispatchOrderHeaderMapper;

/**
 * 派车单Service
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderHeaderService extends CrudService<TmDispatchOrderHeaderMapper, TmDispatchOrderHeader> {

    public TmDispatchOrderHeader getByNo(String dispatchNo) {
        return mapper.getByNo(dispatchNo);
    }
}