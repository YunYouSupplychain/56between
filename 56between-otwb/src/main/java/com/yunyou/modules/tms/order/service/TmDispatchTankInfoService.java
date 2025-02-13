package com.yunyou.modules.tms.order.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.order.entity.TmDispatchTankInfo;
import com.yunyou.modules.tms.order.mapper.TmDispatchTankInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 派车装罐信息Service
 * @author zyf
 * @version 2020-08-18
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchTankInfoService extends CrudService<TmDispatchTankInfoMapper, TmDispatchTankInfo> {

}