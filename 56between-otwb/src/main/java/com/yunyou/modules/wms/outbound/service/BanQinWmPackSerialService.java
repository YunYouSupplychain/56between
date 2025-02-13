package com.yunyou.modules.wms.outbound.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackSerial;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackSerialEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmPackSerialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 打包序列号Service
 *
 * @author WMJ
 * @version 2020-05-13
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPackSerialService extends CrudService<BanQinWmPackSerialMapper, BanQinWmPackSerial> {

    public BanQinWmPackSerial get(String id) {
        return super.get(id);
    }

    public List<BanQinWmPackSerial> findList(BanQinWmPackSerial banQinWmPackSerial) {
        return super.findList(banQinWmPackSerial);
    }

    public Page<BanQinWmPackSerialEntity> findPage(Page page, BanQinWmPackSerialEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmPackSerial banQinWmPackSerial) {
        super.save(banQinWmPackSerial);
    }

    @Transactional
    public void delete(BanQinWmPackSerial banQinWmPackSerial) {
        super.delete(banQinWmPackSerial);
    }

    @Transactional
    public void removeByAllocId(String allocId, String orgId) {
        mapper.removeByAllocId(allocId, orgId);
    }

}