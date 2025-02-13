package com.yunyou.modules.tms.basic.service;

import com.yunyou.core.service.CrudService;
import com.yunyou.modules.tms.basic.entity.TmRouteScopeObj;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeObjEntity;
import com.yunyou.modules.tms.basic.mapper.TmRouteScopeObjMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TmRouteScopeObjService extends CrudService<TmRouteScopeObjMapper, TmRouteScopeObj> {

    @Transactional
    public void remove(String routeScopeId) {
        mapper.remove(routeScopeId);
    }

    public List<TmRouteScopeObjEntity> findEntity(TmRouteScopeObjEntity entity){
        return mapper.findEntity(entity);
    }
}
