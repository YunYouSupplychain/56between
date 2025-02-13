package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.BmsContractStoragePrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractCostItemEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractEntity;
import com.yunyou.modules.bms.basic.mapper.BmsContractMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：合同Service
 * <p>
 *
 * @author Jianhua
 * @version 2019/7/10
 */
@Service
@Transactional(readOnly = true)
public class BmsContractService extends CrudService<BmsContractMapper, BmsContract> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BmsContractCostItemService bmsContractCostItemService;
    @Autowired
    private BmsContractDetailTermsParamsService bmsContractDetailTermsParamsService;
    @Autowired
    private BmsContractStoragePriceService bmsContractStoragePriceService;
    @Autowired
    private BmsContractSkuPriceService bmsContractSkuPriceService;

    public BmsContractEntity getEntity(String id) {
        BmsContractEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setCostItems(bmsContractCostItemService.findByContract(entity.getSysContractNo(), entity.getOrgId()));
            entity.setSkuPriceList(bmsContractSkuPriceService.findByContract(entity.getSysContractNo(), entity.getOrgId()));
        }
        return entity;
    }

    @Override
    public Page<BmsContract> findPage(Page<BmsContract> page, BmsContract entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(BmsContract entity) {
        if (StringUtils.isBlank(entity.getSysContractNo())) {
            entity.setSysContractNo(noService.getDocumentNo(GenNoType.SYS_CONTRACT_NO.name()));
            entity.setContractStatus(BmsConstants.CONTRACT_NEW);
        }
        super.save(entity);
    }

    @Override
    @Transactional
    public void delete(BmsContract entity) {
        if (!BmsConstants.CONTRACT_NEW.equals(entity.getContractStatus())) {
            throw new BmsException("非新建状态，无法删除");
        }
        if (this.isCited(entity.getSysContractNo(), entity.getOrgId())) {
            throw new BmsException("已被结算模型引用，无法删除");
        }
        // 删除商品价格
        bmsContractSkuPriceService.deleteByContract(entity.getSysContractNo(), entity.getOrgId());
        // 删除仓储价格
        bmsContractStoragePriceService.deleteByContract(entity.getSysContractNo(), entity.getOrgId());
        // 删除合同明细条款明细
        bmsContractDetailTermsParamsService.deleteByContract(entity.getSysContractNo(), entity.getOrgId());
        // 删除合同明细
        bmsContractCostItemService.deleteByContract(entity.getSysContractNo(), entity.getOrgId());
        // 删除合同
        super.delete(entity);
    }

    public BmsContract getByContract(String sysContractNo, String orgId) {
        return mapper.getByContract(sysContractNo, orgId);
    }

    /**
     * 描述：合同复制
     * <p>
     * create by Jianhua on 2019/7/10
     */
    @Transactional
    public BmsContractEntity copy(String id, String settleObjectCode, String orgId) {
        BmsContract entity = this.get(id);
        List<BmsContractCostItemEntity> costItemEntities = bmsContractCostItemService.findByContract(entity.getSysContractNo(), entity.getOrgId());

        entity.setRemarks("CP-" + entity.getSysContractNo());
        entity.setContractStatus(BmsConstants.CONTRACT_NEW);
        entity.setSysContractNo(noService.getDocumentNo(GenNoType.SYS_CONTRACT_NO.name()));
        entity.setSettleObjectCode(settleObjectCode);
        entity.setOrgId(orgId);
        entity.setId(null);
        super.save(entity);
        for (BmsContractCostItemEntity o : costItemEntities) {
            List<BmsContractStoragePrice> storagePrices = o.getStoragePrices();

            o.setId(null);
            o.setSysContractNo(entity.getSysContractNo());
            o.setStoragePrices(null);
            o.setOrgId(orgId);
            o.setIncludeParams(o.getIncludeParams().stream().peek(e -> {
                e.setId(null);
                e.setFkId(null);
            }).collect(Collectors.toList()));
            o.setExcludeParams(o.getExcludeParams().stream().peek(e -> {
                e.setId(null);
                e.setFkId(null);
            }).collect(Collectors.toList()));
            bmsContractCostItemService.saveEntity(o);

            for (BmsContractStoragePrice storagePrice : storagePrices) {
                storagePrice.setId(null);
                storagePrice.setFkId(o.getId());
                storagePrice.setSteppedPrices(storagePrice.getSteppedPrices().stream().peek(e -> {
                    e.setId("");
                    e.setFkId(null);
                }).collect(Collectors.toList()));
                bmsContractStoragePriceService.save(storagePrice);
            }
        }
        return getEntity(entity.getId());
    }

    /**
     * 是否引用
     *
     * @param sysContractNo 系统合同号
     * @param orgId         机构ID
     */
    public boolean isCited(String sysContractNo, String orgId) {
        int count = mapper.isCited(sysContractNo, orgId);
        return count > 0;
    }

    /**
     * 更新失效合同状态
     */
    @Transactional
    public void updateInvalidContractStatus() {
        mapper.updateInvalidContractStatus();
    }
}