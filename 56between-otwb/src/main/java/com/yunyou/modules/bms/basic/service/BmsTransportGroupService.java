package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsTransportGroup;
import com.yunyou.modules.bms.basic.entity.BmsTransportSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportGroupEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportPriceEntity;
import com.yunyou.modules.bms.basic.mapper.BmsTransportGroupMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：运输价格体系Service
 *
 * @author liujianhua created on 2019-11-14
 */
@Service
@Transactional(readOnly = true)
public class BmsTransportGroupService extends CrudService<BmsTransportGroupMapper, BmsTransportGroup> {
    @Autowired
    private BmsTransportPriceService bmsTransportPriceService;
    @Autowired
    private BmsTransportSteppedPriceService bmsTransportSteppedPriceService;
    @Autowired
    private SynchronizedNoService noService;

    /**
     * 描述：根据ID查询运输价格体系
     */
    @Override
    public BmsTransportGroup get(String id) {
        BmsTransportGroup bmsTransportGroup = super.get(id);
        if (bmsTransportGroup != null) {
            bmsTransportGroup.setTransportPrices(bmsTransportPriceService.findByFkId(bmsTransportGroup.getId()));
        }
        return bmsTransportGroup;
    }

    /**
     * 描述：根据ID查询运输价格体系
     */
    public BmsTransportGroupEntity getEntity(String id) {
        BmsTransportGroupEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTransportPrices(bmsTransportPriceService.findByFkId(entity.getId()));
        }
        return entity;
    }

    /**
     * 描述：根据编码查询运输价格体系
     */
    public BmsTransportGroupEntity getByCode(String transportGroupCode, String orgId) {
        return mapper.getByCode(transportGroupCode, orgId);
    }

    /**
     * 描述：分页查询
     */
    public Page<BmsTransportGroup> findPage(Page<BmsTransportGroup> page, BmsTransportGroupEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    /**
     * 描述：保存运输价格体系
     */
    @Override
    @Transactional
    public void save(BmsTransportGroup bmsTransportGroup) {
        if (StringUtils.isBlank(bmsTransportGroup.getTransportGroupCode())) {
            bmsTransportGroup.setTransportGroupCode(noService.getDocumentNo(GenNoType.BMS_TRANSPORT_GROUP.name()));
        }
        super.save(bmsTransportGroup);
    }

    /**
     * 描述：根据ID删除运输价格体系
     */
    @Override
    @Transactional
    public void delete(BmsTransportGroup entity) {
        if (StringUtils.isBlank(entity.getId())) {
            return;
        }
        // 删除关联的运输价格
        bmsTransportPriceService.deleteByFkId(entity.getId());
        // 删除运输价格体系
        super.delete(entity);
    }

    /**
     * 描述：运输价格体系复制
     */
    @Transactional
    public void copy(String id, String orgId) {
        BmsTransportGroup bmsTransportGroup = this.get(id);
        bmsTransportGroup.setRemarks("CP-" + bmsTransportGroup.getTransportGroupCode());
        bmsTransportGroup.setId(null);
        bmsTransportGroup.setTransportGroupCode(null);
        bmsTransportGroup.setOrgId(orgId);
        this.save(bmsTransportGroup);
        // 运输价格
        for (BmsTransportPriceEntity bmsTransportPriceEntity : bmsTransportGroup.getTransportPrices()) {
            bmsTransportPriceEntity.setId(null);
            bmsTransportPriceEntity.setFkId(bmsTransportGroup.getId());
            bmsTransportPriceEntity.setOrgId(orgId);
            bmsTransportPriceService.save(bmsTransportPriceEntity);

            // 运输阶梯价格
            for (BmsTransportSteppedPrice bmsTransportSteppedPrice : bmsTransportPriceEntity.getSteppedPrices()) {
                bmsTransportSteppedPrice.setId(null);
                bmsTransportSteppedPrice.setFkId(bmsTransportPriceEntity.getId());
                bmsTransportPriceEntity.setOrgId(orgId);
                bmsTransportSteppedPriceService.save(bmsTransportSteppedPrice);
            }
        }

    }
}