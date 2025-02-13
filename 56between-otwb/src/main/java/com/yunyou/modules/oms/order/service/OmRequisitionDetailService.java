package com.yunyou.modules.oms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmRequisitionDetail;
import com.yunyou.modules.oms.order.mapper.OmRequisitionDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OmRequisitionDetailService extends CrudService<OmRequisitionDetailMapper, OmRequisitionDetail> {
}