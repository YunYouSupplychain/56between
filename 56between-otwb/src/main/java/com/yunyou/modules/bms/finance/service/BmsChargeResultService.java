package com.yunyou.modules.bms.finance.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.finance.entity.BmsChargeResult;
import com.yunyou.modules.bms.finance.mapper.BmsChargeResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 费用结果Service
 *
 * @author liujianhua
 * @version 2022.8.11
 */
@Service
@Transactional(readOnly = true)
public class BmsChargeResultService extends CrudService<BmsChargeResultMapper, BmsChargeResult> {
}
