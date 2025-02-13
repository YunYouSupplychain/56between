package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsSettleObject;
import com.yunyou.modules.bms.basic.mapper.BmsSettleObjectMapper;
import com.yunyou.modules.bms.common.BmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结算对象Service
 *
 * @author zqs
 * @version 2018-06-15
 */
@Service
@Transactional(readOnly = true)
public class BmsSettleObjectService extends CrudService<BmsSettleObjectMapper, BmsSettleObject> {

    @Override
    public Page<BmsSettleObject> findPage(Page<BmsSettleObject> page, BmsSettleObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<BmsSettleObject> findGrid(Page<BmsSettleObject> page, BmsSettleObject entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public BmsSettleObject getByCode(String code, String orgId) {
        return mapper.getByCode(code, orgId);
    }

    public void checkSaveBefore(BmsSettleObject bmsSettleObject) {
        if (bmsSettleObject == null) {
            throw new BmsException("数据缺失");
        }
        BmsSettleObject settlementObject = getByCode(bmsSettleObject.getSettleObjectCode(), bmsSettleObject.getOrgId());
        if (settlementObject != null && !settlementObject.getId().equals(bmsSettleObject.getId())) {
            throw new BmsException("结算对象代码已存在");
        }
    }

    @Override
    @Transactional
    public void save(BmsSettleObject entity) {
        this.checkSaveBefore(entity);
        super.save(entity);
    }

    @Transactional
    public void remove(String settleCode, String orgId) {
        mapper.remove(settleCode, orgId);
    }
}