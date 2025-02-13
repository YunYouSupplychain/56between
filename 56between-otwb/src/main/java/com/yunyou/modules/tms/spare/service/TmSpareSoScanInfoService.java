package com.yunyou.modules.tms.spare.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.spare.entity.TmSpareSoScanInfo;
import com.yunyou.modules.tms.spare.mapper.TmSpareSoScanInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TmSpareSoScanInfoService extends CrudService<TmSpareSoScanInfoMapper, TmSpareSoScanInfo> {

}
