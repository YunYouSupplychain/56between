package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContractSkuPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractSkuPriceEntity;
import com.yunyou.modules.bms.basic.mapper.BmsContractSkuPriceMapper;
import com.yunyou.modules.bms.common.BmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同商品价格Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsContractSkuPriceService extends CrudService<BmsContractSkuPriceMapper, BmsContractSkuPrice> {

    @Override
    public Page<BmsContractSkuPrice> findPage(Page<BmsContractSkuPrice> page, BmsContractSkuPrice entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public BmsContractSkuPriceEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public List<BmsContractSkuPrice> findByContract(String sysContractNo, String orgId) {
        return mapper.findByContract(sysContractNo, orgId);
    }

    public BmsContractSkuPrice getContractPrice(String sysContractNo, String skuClass, String skuCode, String orgId) {
        BmsContractSkuPrice bmsContractSkuPrice = null;
        if (StringUtils.isNotBlank(skuCode)) {
            bmsContractSkuPrice = mapper.getContractPrice1(sysContractNo, skuCode, orgId);
        }
        if (bmsContractSkuPrice == null && StringUtils.isNotBlank(skuClass)) {
            bmsContractSkuPrice = mapper.getContractPrice2(sysContractNo, skuClass, orgId);
        }
        if (bmsContractSkuPrice == null) {
            bmsContractSkuPrice = mapper.getContractPrice3(sysContractNo, orgId);
        }
        if (bmsContractSkuPrice == null) {
            bmsContractSkuPrice = new BmsContractSkuPrice();
        }
        return bmsContractSkuPrice;
    }

    public void checkSaveBefore(BmsContractSkuPrice entity) {
        if (StringUtils.isBlank(entity.getSysContractNo())) {
            throw new BmsException("系统合同号不能为空");
        }
        if (StringUtils.isNotBlank(entity.getSkuCode())) {
            BmsContractSkuPrice skuPrice = mapper.getContractPrice1(entity.getSysContractNo(), entity.getSkuCode(), entity.getOrgId());
            if (skuPrice != null && !skuPrice.getId().equals(entity.getId())) {
                throw new BmsException("合同【" + entity.getSysContractNo() + "】商品【" + entity.getSkuCode() + "】已存在");
            }
        } else if (StringUtils.isNotBlank(entity.getSkuClass())) {
            BmsContractSkuPrice skuPrice = mapper.getContractPrice2(entity.getSysContractNo(), entity.getSkuClass(), entity.getOrgId());
            if (skuPrice != null && !skuPrice.getId().equals(entity.getId())) {
                throw new BmsException("合同【" + entity.getSysContractNo() + "】品类【" + entity.getSkuClass() + "】已存在");
            }
        } else if (StringUtils.isBlank(entity.getSkuCode()) && StringUtils.isBlank(entity.getSkuClass())) {
            BmsContractSkuPrice skuPrice = mapper.getContractPrice3(entity.getSysContractNo(), entity.getOrgId());
            if (skuPrice != null && !skuPrice.getId().equals(entity.getId())) {
                throw new BmsException("合同【" + entity.getSysContractNo() + "】公共价格已存在");
            }
        }
        if (entity.getPrice() == null && entity.getTaxPrice() == null) {
            throw new BmsException("未税单价与含税价格至少有一项不能为空");
        }
    }

    @Override
    @Transactional
    public void save(BmsContractSkuPrice entity) {
        this.checkSaveBefore(entity);
        super.save(entity);
    }

    @Transactional
    public void deleteByContract(String sysContractNo, String orgId) {
        mapper.deleteByContract(sysContractNo, orgId);
    }
}