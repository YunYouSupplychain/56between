package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmExpressInfoImportDetail;
import com.yunyou.modules.tms.order.mapper.TmExpressInfoImportDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 快递单号导入更新Service
 * @author zyf
 * @version 2020-04-13
 */
@Service
@Transactional(readOnly = true)
public class TmExpressInfoImportDetailService extends CrudService<TmExpressInfoImportDetailMapper, TmExpressInfoImportDetail> {

}