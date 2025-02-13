package com.yunyou.modules.oms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.mapper.OmTaskDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OmTaskDetailService extends CrudService<OmTaskDetailMapper, OmTaskDetail> {

}
