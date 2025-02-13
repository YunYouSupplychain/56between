package com.yunyou.modules.tms.order.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;
import com.yunyou.modules.tms.order.mapper.TmDispatchOrderLabelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 派车单标签Service
 *
 * @author liujianhua
 * @version 2020-03-11
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderLabelService extends CrudService<TmDispatchOrderLabelMapper, TmDispatchOrderLabel> {

    public TmDispatchOrderLabel getByNoAndLabelAndRS(String dispatchNo, String labelNo, String receiveShip, String orgId) {
        return mapper.getByNoAndLabelAndRS(dispatchNo, labelNo, receiveShip, orgId);
    }

    public TmDispatchOrderLabel getByNoAndOutletAndLabel(String dispatchNo, String dispatchOutletCode, String labelNo, String orgId) {
        return mapper.getByNoAndOutletAndLabel(dispatchNo, dispatchOutletCode, labelNo, orgId);
    }

    public List<TmDispatchOrderLabel> findByDispatchNoAndSiteCode(String dispatchNo, String dispatchSiteOutletCode, String orgId) {
        TmDispatchOrderLabel qEntity = new TmDispatchOrderLabel();
        qEntity.setDispatchNo(dispatchNo);
        qEntity.setDispatchSiteOutletCode(dispatchSiteOutletCode);
        qEntity.setOrgId(orgId);
        return super.findList(qEntity);
    }

    @Transactional
    public void saveByUser(TmDispatchOrderLabel entity, User user) {
        if (entity.getIsNewRecord()){
            entity.setId(IdGen.uuid());
            entity.setCreateBy(user);
            entity.setCreateDate(new Date());
            entity.setUpdateBy(user);
            entity.setUpdateDate(new Date());
            mapper.insert(entity);
        }else{
            entity.setUpdateBy(user);
            entity.setUpdateDate(new Date());
            mapper.update(entity);
        }
    }

    public boolean existTransportLabel(String transportNo, String labelNo, String baseOrgId) {
        return mapper.existTransportLabel(transportNo, labelNo, baseOrgId);
    }
}