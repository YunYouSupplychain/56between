package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmExceptionHandleBill;
import com.yunyou.modules.tms.order.mapper.TmExceptionHandleBillMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异常处理单Service
 * @author ZYF
 * @version 2020-07-29
 */
@Service
@Transactional(readOnly = true)
public class TmExceptionHandleBillService extends CrudService<TmExceptionHandleBillMapper, TmExceptionHandleBill> {

}