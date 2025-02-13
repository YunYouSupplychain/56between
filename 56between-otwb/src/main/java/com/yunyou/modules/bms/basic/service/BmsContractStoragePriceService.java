package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContractStoragePrice;
import com.yunyou.modules.bms.basic.entity.BmsContractStorageSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import com.yunyou.modules.bms.basic.mapper.BmsContractStoragePriceMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓储价格Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsContractStoragePriceService extends CrudService<BmsContractStoragePriceMapper, BmsContractStoragePrice> {
    @Autowired
    private BmsContractStorageSteppedPriceService bmsContractStorageSteppedPriceService;

    @Override
    public Page<BmsContractStoragePrice> findPage(Page<BmsContractStoragePrice> page, BmsContractStoragePrice entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public List<BmsContractStoragePrice> findByContract(String sysContractNo, String orgId) {
        return mapper.findByContract(sysContractNo, orgId);
    }

    public List<BmsContractStoragePrice> findByFkId(String fkId) {
        List<BmsContractStoragePrice> list = mapper.findByFkId(fkId);
        for (BmsContractStoragePrice o : list) {
            o.setSteppedPrices(bmsContractStorageSteppedPriceService.findByFkId(o.getId()));
        }
        return list;
    }

    /**
     * 获取合同单价
     *
     * @param fkId     合同明细ID
     * @param skuClass 品类
     * @param skuCode  商品编码
     * @param orgId    机构ID
     * @return 单价
     */
    public BmsContractPrice getContractPrice(String fkId, String skuClass, String skuCode, String orgId) {
        BmsContractPrice bmsContractPrice = mapper.getContractPrice(fkId, skuClass, skuCode, orgId);
        if (bmsContractPrice == null && StringUtils.isNotBlank(skuClass)) {
            bmsContractPrice = mapper.getContractPrice(fkId, null, skuCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(skuCode)) {
            bmsContractPrice = mapper.getContractPrice(fkId, skuClass, null, orgId);
        }
        if (bmsContractPrice == null) {
            bmsContractPrice = mapper.getContractPrice(fkId, null, null, orgId);
        }
        if (bmsContractPrice != null) {
            bmsContractPrice.setSteppedPrices(bmsContractStorageSteppedPriceService.findSteppedPriceByFkId(bmsContractPrice.getId()));
        }
        return bmsContractPrice;
    }

    public BmsContractStoragePrice getByFkIdAndSku(String fkId, String skuCode, String orgId) {
        return mapper.getByFkIdAndSku(fkId, skuCode, orgId);
    }

    public BmsContractStoragePrice getByFkIdAndSkuClass(String fkId, String skuClass, String orgId) {
        return mapper.getByFkIdAndSkuClass(fkId, skuClass, orgId);
    }

    public BmsContractStoragePrice getByFkId(String fkId, String orgId) {
        return mapper.getByFkId(fkId, orgId);
    }

    public void checkSaveBefore(BmsContractStoragePrice entity) {
        if (StringUtils.isBlank(entity.getFkId())) {
            throw new BmsException("系统合同不能为空");
        }
        if (StringUtils.isNotBlank(entity.getSkuCode())) {
            BmsContractStoragePrice storagePrice = this.getByFkIdAndSku(entity.getFkId(), entity.getSkuCode(), entity.getOrgId());
            if (storagePrice != null && !storagePrice.getId().equals(entity.getId())) {
                throw new BmsException("商品【" + entity.getSkuCode() + "】已存在");
            }
        } else if (StringUtils.isNotBlank(entity.getSkuClass())) {
            BmsContractStoragePrice storagePrice = this.getByFkIdAndSkuClass(entity.getFkId(), entity.getSkuClass(), entity.getOrgId());
            if (storagePrice != null && !storagePrice.getId().equals(entity.getId())) {
                throw new BmsException("品类【" + entity.getSkuClass() + "】已存在");
            }
        } else {
            BmsContractStoragePrice storagePrice = this.getByFkId(entity.getFkId(), entity.getOrgId());
            if (storagePrice != null && !storagePrice.getId().equals(entity.getId())) {
                throw new BmsException("价格已存在");
            }
        }
        // 启用阶梯价格
        if (BmsConstants.YES.equals(entity.getIsUseStep())) {
            if (CollectionUtil.isNotEmpty(entity.getSteppedPrices())) {
                List<BmsContractStorageSteppedPrice> steppedPrices = entity.getSteppedPrices().stream()
                        .filter(e -> e.getId() != null)
                        .sorted(Comparator.comparing(BmsContractStorageSteppedPrice::getFm))
                        .collect(Collectors.toList());
                BmsContractStorageSteppedPrice preSteppedPrice = steppedPrices.get(0);
                if (preSteppedPrice.getFm() == null) {
                    throw new BmsException("从值不能为空");
                }
                // 到值为空表示无穷大，如果还存在下一条抛出“阶梯价格区间出现交叉范围”
                if (preSteppedPrice.getTo() != null) {
                    // 从值大于等于到值
                    if (preSteppedPrice.getTo().compareTo(preSteppedPrice.getFm()) <= 0) {
                        throw new BmsException("阶梯价格区间从值必须小于到值");
                    }
                } else if (steppedPrices.size() > 1) {
                    throw new BmsException("阶梯价格区间出现交叉范围");
                }
                for (int i = 1; i < steppedPrices.size(); i++) {
                    BmsContractStorageSteppedPrice steppedPrice = steppedPrices.get(i);
                    if (steppedPrice.getFm() == null) {
                        throw new BmsException("从值不能为空");
                    }
                    // 从值小于上一条到值
                    if (steppedPrice.getFm().compareTo(preSteppedPrice.getTo()) < 0) {
                        throw new BmsException("阶梯价格区间出现交叉范围");
                    }
                    // 到值为空表示无穷大，如果还存在下一条抛出“阶梯价格区间出现交叉范围”
                    if (steppedPrice.getTo() != null && steppedPrice.getFm().compareTo(steppedPrice.getTo()) >= 0) {
                        throw new BmsException("阶梯价格区间从值必须小于到值");
                    } else if (steppedPrice.getTo() == null && (i + 1) < steppedPrices.size()) {
                        throw new BmsException("阶梯价格区间出现交叉范围");
                    }
                    preSteppedPrice = steppedPrice;
                }
            }
        }
    }

    @Override
    @Transactional
    public void save(BmsContractStoragePrice entity) {
        this.checkSaveBefore(entity);
        if (StringUtils.isBlank(entity.getIsUseStep())) {
            entity.setIsUseStep(BmsConstants.NO);
        }
        super.save(entity);
        for (BmsContractStorageSteppedPrice bmsContractStorageSteppedPrice : entity.getSteppedPrices()) {
            if (bmsContractStorageSteppedPrice.getId() == null) {
                continue;
            }
            bmsContractStorageSteppedPrice.setFkId(entity.getId());
            bmsContractStorageSteppedPrice.setOrgId(entity.getOrgId());
            bmsContractStorageSteppedPriceService.save(bmsContractStorageSteppedPrice);
        }
    }

    @Override
    @Transactional
    public void delete(BmsContractStoragePrice entity) {
        bmsContractStorageSteppedPriceService.deleteByFkId(entity.getId());
        super.delete(entity);
    }

    @Transactional
    public void deleteByContract(String sysContractNo, String orgId) {
        mapper.deleteByContract(sysContractNo, orgId);
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

}