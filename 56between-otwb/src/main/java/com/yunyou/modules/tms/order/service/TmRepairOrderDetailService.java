package com.yunyou.modules.tms.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmRepairOrderDetail;
import com.yunyou.modules.tms.order.mapper.TmRepairOrderDetailMapper;

@Service
@Transactional(readOnly = true)
public class TmRepairOrderDetailService extends CrudService<TmRepairOrderDetailMapper, TmRepairOrderDetail> {
}
