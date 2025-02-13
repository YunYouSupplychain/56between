package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmExceptionHandleBill;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillFeeEntity;

import java.util.List;

@MyBatisMapper
public interface TmExceptionHandleBillEntityMapper extends BaseMapper<TmExceptionHandleBill> {

    List<TmExceptionHandleBillEntity> findPage(TmExceptionHandleBillEntity entity);

    List<TmExceptionHandleBillEntity> findHeaderList(TmExceptionHandleBillEntity entity);

    List<TmExceptionHandleBillDetailEntity> findDetailList(TmExceptionHandleBillDetailEntity entity);

    List<TmExceptionHandleBillFeeEntity> findFeeList(TmExceptionHandleBillFeeEntity entity);

    TmExceptionHandleBillEntity getEntity(String id);

}
