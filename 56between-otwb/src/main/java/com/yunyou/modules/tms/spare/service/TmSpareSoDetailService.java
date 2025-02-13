package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSpareSoDetail;
import com.yunyou.modules.tms.spare.mapper.TmSpareSoDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSpareSoDetailService extends CrudService<TmSpareSoDetailMapper, TmSpareSoDetail> {

}
