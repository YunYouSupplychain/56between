package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmPickHeaderMapper;
import com.yunyou.modules.wms.report.entity.WmPickOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 拣货单Service
 *
 * @author ZYF
 * @version 2020-05-13
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPickHeaderService extends CrudService<BanQinWmPickHeaderMapper, BanQinWmPickHeader> {

    public BanQinWmPickHeader get(String id) {
        return super.get(id);
    }

    public BanQinWmPickHeaderEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<BanQinWmPickHeader> findList(BanQinWmPickHeader banQinWmPickHeader) {
        return super.findList(banQinWmPickHeader);
    }

    public Page<BanQinWmPickHeaderEntity> findPage(Page page, BanQinWmPickHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmPickHeader banQinWmPickHeader) {
        super.save(banQinWmPickHeader);
    }

    @Transactional
    public void delete(BanQinWmPickHeader banQinWmPickHeader) {
        super.delete(banQinWmPickHeader);
    }

    public BanQinWmPickHeader findFirst(BanQinWmPickHeader example) {
        List<BanQinWmPickHeader> list = mapper.findList(example);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public BanQinWmPickHeader findByPickNo(String pickNo, String orgId) {
        BanQinWmPickHeader example = new BanQinWmPickHeader();
        example.setPickNo(pickNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    /**
     * 描述： 查询拣货单Entity
     */
    public BanQinWmPickHeaderEntity findEntityByPickNo(String pickNo, String orgId) {
        BanQinWmPickHeaderEntity entity = null;

        BanQinWmPickHeader wmPickHeader = this.findByPickNo(pickNo, orgId);
        if (wmPickHeader != null) {
            entity = (BanQinWmPickHeaderEntity) wmPickHeader;
        }
        return entity;
    }

    @Transactional
    public BanQinWmPickHeader savePickHeader(BanQinWmPickHeader pickHeader) {
        this.save(pickHeader);
        return pickHeader;
    }

    public List<WmPickOrder> getPickOrder(List<String> pickIds) {
        return mapper.getPickOrder(pickIds);
    }
}