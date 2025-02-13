package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSparePoDetail;
import com.yunyou.modules.tms.spare.mapper.TmSparePoDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSparePoDetailService extends CrudService<TmSparePoDetailMapper, TmSparePoDetail> {

}
