package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSparePoScanInfo;
import com.yunyou.modules.tms.spare.mapper.TmSparePoScanInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSparePoScanInfoService extends CrudService<TmSparePoScanInfoMapper, TmSparePoScanInfo> {

}
