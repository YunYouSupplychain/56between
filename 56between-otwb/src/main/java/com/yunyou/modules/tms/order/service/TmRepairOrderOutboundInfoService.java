package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmRepairOrderOutboundInfo;
import com.yunyou.modules.tms.order.mapper.TmRepairOrderOutboundInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmRepairOrderOutboundInfoService extends CrudService<TmRepairOrderOutboundInfoMapper, TmRepairOrderOutboundInfo> {
}
