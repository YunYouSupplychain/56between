package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmRepairOrderInboundInfo;
import com.yunyou.modules.tms.order.mapper.TmRepairOrderInboundInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmRepairOrderInboundInfoService extends CrudService<TmRepairOrderInboundInfoMapper, TmRepairOrderInboundInfo> {
}
