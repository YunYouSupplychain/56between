package com.yunyou.modules.oms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmRequisitionHeader;
import com.yunyou.modules.oms.order.mapper.OmRequisitionHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OmRequisitionHeaderService extends CrudService<OmRequisitionHeaderMapper, OmRequisitionHeader> {
}