package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.TmReceiveLabel;
import com.yunyou.modules.tms.order.entity.extend.TmReceiveEntity;

import java.util.List;

@MyBatisMapper
public interface TmReceiveMapper extends BaseMapper<TmReceiveLabel> {

    List<TmReceiveEntity> findPage(TmReceiveEntity entity);

}
