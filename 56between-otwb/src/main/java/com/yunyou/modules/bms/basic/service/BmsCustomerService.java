package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsCustomer;
import com.yunyou.modules.bms.basic.mapper.BmsCustomerMapper;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户Service
 *
 * @author Jianhua Liu
 * @version 2019-06-11
 */
@Service
@Transactional(readOnly = true)
public class BmsCustomerService extends CrudService<BmsCustomerMapper, BmsCustomer> {
    @Autowired
    private AreaService areaService;

    @Override
    public Page<BmsCustomer> findPage(Page<BmsCustomer> page, BmsCustomer entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<BmsCustomer> list = mapper.findPage(entity);
        list.forEach(o -> o.setAreaName(areaService.getFullName(o.getAreaId())));
        page.setList(list);
        return page;
    }

    public Page<BmsCustomer> findGrid(Page<BmsCustomer> page, BmsCustomer entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public BmsCustomer getByCodeAndType(String customerNo, String type, String orgId) {
        return mapper.getByCodeAndType(customerNo, type, orgId);
    }

    public BmsCustomer getByCode(String customerNo, String orgId) {
        return mapper.getByCode(customerNo, orgId);
    }

    public String getRegionCode(String code, String orgId) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(orgId)) {
            return null;
        }
        return mapper.getRegionCode(code, orgId);
    }

    @Override
    @Transactional
    public void save(BmsCustomer bmsCustomer) {
        if (StringUtils.isNotBlank(bmsCustomer.getAreaId())) {
            Area area = areaService.get(bmsCustomer.getAreaId());
            if (area == null) {
                throw new BmsException("未找到所属城市");
            }
            bmsCustomer.setAreaCode(area.getCode());
        }
        super.save(bmsCustomer);
    }

    @Transactional
    public void update(BmsCustomer bmsCustomer) {
        mapper.update(bmsCustomer);
    }

    @Transactional
    public void remove(String customerNo, String orgId) {
        mapper.remove(customerNo, orgId);
    }

    @Transactional
    public void batchInsert(List<BmsCustomer> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}