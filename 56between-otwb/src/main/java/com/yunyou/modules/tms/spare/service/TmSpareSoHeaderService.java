package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSpareSoHeader;
import com.yunyou.modules.tms.spare.mapper.TmSpareSoHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSpareSoHeaderService extends CrudService<TmSpareSoHeaderMapper, TmSpareSoHeader> {

}
