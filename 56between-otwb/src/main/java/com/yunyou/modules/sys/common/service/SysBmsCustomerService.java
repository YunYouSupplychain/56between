package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.common.entity.SysBmsCustomer;
import com.yunyou.modules.sys.common.mapper.SysBmsCustomerMapper;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户Service
 */
@Service
@Transactional(readOnly = true)
public class SysBmsCustomerService extends CrudService<SysBmsCustomerMapper, SysBmsCustomer> {
    @Autowired
    private AreaService areaService;

    @Override
    public Page<SysBmsCustomer> findPage(Page<SysBmsCustomer> page, SysBmsCustomer entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SysBmsCustomer> findGrid(Page<SysBmsCustomer> page, SysBmsCustomer entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public SysBmsCustomer getByCode(String customerNo, String dataSet) {
        return mapper.getByCode(customerNo, dataSet);
    }

    @Override
    @Transactional
    public void save(SysBmsCustomer sysBmsCustomer) {
        if (StringUtils.isNotBlank(sysBmsCustomer.getAreaId())) {
            Area area = areaService.get(sysBmsCustomer.getAreaId());
            if (area == null) {
                throw new BmsException("未找到所属城市");
            }
            sysBmsCustomer.setAreaCode(area.getCode());
        }
        super.save(sysBmsCustomer);
    }

    @Transactional
    public void remove(String customerNo, String dataSet) {
        mapper.remove(customerNo, dataSet);
    }

    @Transactional
    public void batchInsert(List<SysBmsCustomer> list) {
        if (CollectionUtil.isEmpty(list)) return;
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

}