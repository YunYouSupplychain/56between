package com.yunyou.modules.tms.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDeliverLabel;
import com.yunyou.modules.tms.order.mapper.TmDeliverLabelMapper;

/**
 * 发货标签Service
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class TmDeliverLabelService extends CrudService<TmDeliverLabelMapper, TmDeliverLabel> {

}