package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinCdWhBomHeaderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组合件Service
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhBomHeaderService extends CrudService<BanQinCdWhBomHeaderMapper, BanQinCdWhBomHeader> {

    public BanQinCdWhBomHeader get(String id) {
        return super.get(id);
    }

    public List<BanQinCdWhBomHeader> findList(BanQinCdWhBomHeader banQinCdWhBomHeader) {
        return super.findList(banQinCdWhBomHeader);
    }

    public Page<BanQinCdWhBomEntity> findPage(Page page, BanQinCdWhBomEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinCdWhBomHeader banQinCdWhBomHeader) {
        super.save(banQinCdWhBomHeader);
    }

    @Transactional
    public void delete(BanQinCdWhBomHeader banQinCdWhBomHeader) {
        super.delete(banQinCdWhBomHeader);
    }

    /**
     * 描述：保存前校验
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public void checkSaveBefore(BanQinCdWhBomHeader cdWhBomHeader) {
        if (StringUtils.isBlank(cdWhBomHeader.getOwnerCode())) {
            throw new RuntimeException("货主不能为空");
        }
        if (StringUtils.isBlank(cdWhBomHeader.getParentSkuCode())) {
            throw new RuntimeException("父件不能为空");
        }
        if (StringUtils.isBlank(cdWhBomHeader.getKitType())) {
            throw new RuntimeException("加工类型不能为空");
        }
        BanQinCdWhBomEntity entity = getEntityByOwnerAndParentSkuAndKitType(cdWhBomHeader.getOwnerCode(), cdWhBomHeader.getParentSkuCode(), cdWhBomHeader.getKitType(), cdWhBomHeader.getOrgId());
        if (entity != null && !entity.getId().equals(cdWhBomHeader.getId())) {
            throw new RuntimeException("货主" + cdWhBomHeader.getOwnerCode() + "组合件" + cdWhBomHeader.getParentSkuCode() + "加工类型" + cdWhBomHeader.getKitType() + "重复，不能操作");
        }
    }

    public BanQinCdWhBomEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据货主、组合件编码、加工类型获取组合件信息
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomEntity getEntityByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        return mapper.getEntityByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
    }

    /**
     * 描述：校验是否被引用
     * <p>
     * create by Jianhua on 2019/8/19
     */
    public void checkIsReferenced(BanQinCdWhBomHeader cdWhBomHeader) {
        Long l = mapper.checkIsReferenced(cdWhBomHeader);
        if (l != null && l > 0) {
            throw new WarehouseException("货主" + cdWhBomHeader.getOwnerCode() + "商品" + cdWhBomHeader.getParentSkuCode() + "加工类型" + cdWhBomHeader.getKitType() + "被引用，不能操作");
        }
    }

    public Page<BanQinCdWhBomEntity> findGrid(Page page, BanQinCdWhBomEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    /**
     * 描述：保存BOM 信息
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public void saveBomHeader(BanQinCdWhBomEntity entity) throws WarehouseException {
        checkSaveBefore(entity);
        this.save(entity);
    }

    /**
     * 描述：根据货主、组合件编码、加工类型获取组合件信息
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public BanQinCdWhBomHeader getByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        BanQinCdWhBomHeader model = new BanQinCdWhBomHeader();
        model.setOwnerCode(ownerCode);
        model.setParentSkuCode(parentSkuCode);
        model.setKitType(kitType);
        model.setOrgId(orgId);
        List<BanQinCdWhBomHeader> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 校验商品是否已经存在于组合件父件记录中，存在则不允许修改，不存在可修改
     * @return
     */
    public boolean checkIsParent(String ownerCode, String skuCode, String orgId) {
        BanQinCdWhBomHeader model = new BanQinCdWhBomHeader();
        model.setOwnerCode(ownerCode);
        model.setParentSkuCode(skuCode);
        model.setOrgId(orgId);
        return !CollectionUtil.isEmpty(this.findList(model));
    }
}