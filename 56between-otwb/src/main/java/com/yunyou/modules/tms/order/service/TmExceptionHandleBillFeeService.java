package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmExceptionHandleBillFee;
import com.yunyou.modules.tms.order.mapper.TmExceptionHandleBillFeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异常处理单费用Service
 * @author ZYF
 * @version 2020-07-29
 */
@Service
@Transactional(readOnly = true)
public class TmExceptionHandleBillFeeService extends CrudService<TmExceptionHandleBillFeeMapper, TmExceptionHandleBillFee> {

    @Transactional
    public void deleteDetail(String billNo, String orgId) {
        mapper.deleteDetail(billNo, orgId);
    }

}