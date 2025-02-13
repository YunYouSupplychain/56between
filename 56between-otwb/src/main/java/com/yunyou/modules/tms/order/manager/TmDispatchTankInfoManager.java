package com.yunyou.modules.tms.order.manager;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchTankInfoEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmDispatchTankInfoEntityMapper;
import com.yunyou.modules.tms.order.service.TmDispatchTankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 派车装罐信息业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchTankInfoManager extends BaseService {
    @Autowired
    private TmDispatchTankInfoEntityMapper mapper;
    @Autowired
    private TmDispatchTankInfoService tmDispatchTankInfoService;

    public TmDispatchTankInfoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmDispatchTankInfoEntity> findPage(Page page, TmDispatchTankInfoEntity entity) {
        return mapper.findPage(entity);
    }

    public List<TmDispatchTankInfoEntity> findEntityList(TmDispatchTankInfoEntity entity) {
        return mapper.findEntityList(entity);
    }

    @Transactional
    public TmDispatchTankInfoEntity saveEntity(TmDispatchTankInfoEntity entity) {
        tmDispatchTankInfoService.save(entity);
        return getEntity(entity.getId());
    }

}