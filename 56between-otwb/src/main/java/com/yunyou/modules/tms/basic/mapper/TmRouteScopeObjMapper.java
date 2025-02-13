package com.yunyou.modules.tms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.tms.basic.entity.TmRouteScopeObj;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeObjEntity;

import java.util.List;

@MyBatisMapper
public interface TmRouteScopeObjMapper extends BaseMapper<TmRouteScopeObj> {
    void remove(String routeScopeId);

    List<TmRouteScopeObjEntity> findEntity(TmRouteScopeObjEntity entity);
}
