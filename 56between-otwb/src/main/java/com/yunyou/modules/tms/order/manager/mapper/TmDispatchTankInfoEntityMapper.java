package com.yunyou.modules.tms.order.manager.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchTankInfoEntity;

import java.util.List;

@MyBatisMapper
public interface TmDispatchTankInfoEntityMapper extends BaseMapper<TmDispatchTankInfoEntity> {

    Page<TmDispatchTankInfoEntity> findPage(TmDispatchTankInfoEntity entity);

    List<TmDispatchTankInfoEntity> findEntityList(TmDispatchTankInfoEntity entity);

    TmDispatchTankInfoEntity getEntity(String id);

}
