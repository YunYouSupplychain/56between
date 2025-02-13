package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSparePoHeader;
import com.yunyou.modules.tms.spare.mapper.TmSparePoHeaderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSparePoHeaderService extends CrudService<TmSparePoHeaderMapper, TmSparePoHeader> {

}
