package com.yunyou.modules.bms.finance.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.finance.entity.BmsChargeDetail;
import com.yunyou.modules.bms.finance.mapper.BmsChargeDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 费用明细Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
@Transactional(readOnly = true)
public class BmsChargeDetailService extends CrudService<BmsChargeDetailMapper, BmsChargeDetail> {
}
