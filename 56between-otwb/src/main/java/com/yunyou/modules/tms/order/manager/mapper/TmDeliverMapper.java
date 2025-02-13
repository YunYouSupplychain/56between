package com.yunyou.modules.tms.order.manager.mapper;

import java.util.List;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.extend.TmDeliverEntity;

@MyBatisMapper
public interface TmDeliverMapper extends BaseMapper<TmDeliverEntity> {

    List<TmDeliverEntity> findPage(TmDeliverEntity entity);

}
