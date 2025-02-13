package com.yunyou.modules.tms.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmRepairOrderHeader;
import com.yunyou.modules.tms.order.mapper.TmRepairOrderHeaderMapper;

@Service
@Transactional(readOnly = true)
public class TmRepairOrderHeaderService extends CrudService<TmRepairOrderHeaderMapper, TmRepairOrderHeader> {

    public TmRepairOrderHeader getByNo(String repairNo) {
        return mapper.getByNo(repairNo);
    }
}
