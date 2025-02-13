package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.order.entity.extend.TmVehicleSafetyCheckEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmVehicleSafetyCheckEntityMapper;
import com.yunyou.modules.tms.order.service.TmVehicleSafetyCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 出车安全检查表业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmVehicleSafetyCheckManager extends BaseService {
    @Autowired
    private TmVehicleSafetyCheckEntityMapper mapper;
    @Autowired
    private TmVehicleSafetyCheckService tmVehicleSafetyCheckService;

    public TmVehicleSafetyCheckEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<TmVehicleSafetyCheckEntity> findPage(Page page, TmVehicleSafetyCheckEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public List<TmVehicleSafetyCheckEntity> findEntityList(TmVehicleSafetyCheckEntity entity) {
        return mapper.findEntityList(entity);
    }

    @Transactional
    public TmVehicleSafetyCheckEntity saveEntity(TmVehicleSafetyCheckEntity entity) {
        tmVehicleSafetyCheckService.save(entity);
        return getEntity(entity.getId());
    }

    /**
     * 获取当天的出车安全检查表
     */
    public TmVehicleSafetyCheckEntity getVehicleSafetyCheckIntraday(String vehicleNo, String baseOrgId) {
        Date now = new Date();
        TmVehicleSafetyCheckEntity condition = new TmVehicleSafetyCheckEntity();
        condition.setVehicleNo(vehicleNo);
        condition.setBaseOrgId(baseOrgId);
        condition.setCheckDateFm(DateUtil.beginOfDate(now));
        condition.setCheckDateTo(DateUtil.endOfDate(now));
        List<TmVehicleSafetyCheckEntity> list = this.findEntityList(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

}