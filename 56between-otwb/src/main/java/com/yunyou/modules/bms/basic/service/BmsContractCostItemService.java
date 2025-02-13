package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContractCostItem;
import com.yunyou.modules.bms.basic.entity.BmsContractDetailTermsParams;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractCostItemEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import com.yunyou.modules.bms.basic.mapper.BmsContractCostItemMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同明细Service
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@Service
@Transactional(readOnly = true)
public class BmsContractCostItemService extends CrudService<BmsContractCostItemMapper, BmsContractCostItem> {
    @Autowired
    private BmsContractStoragePriceService bmsContractStoragePriceService;
    @Autowired
    private BmsContractDetailTermsParamsService bmsContractDetailTermsParamsService;

    public BmsContractCostItemEntity getEntity(String id) {
        BmsContractCostItemEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setIncludeParams(bmsContractDetailTermsParamsService.findIncludeByFkId(entity.getId()));
            entity.setExcludeParams(bmsContractDetailTermsParamsService.findExcludeByFkId(entity.getId()));
        }
        return entity;
    }

    public List<BmsContractCostItemEntity> findByContract(String sysContractNo, String orgId) {
        List<BmsContractCostItemEntity> list = mapper.findByContract(sysContractNo, orgId);
        for (BmsContractCostItemEntity o : list) {
            o.setIncludeParams(bmsContractDetailTermsParamsService.findIncludeByFkId(o.getId()));
            o.setExcludeParams(bmsContractDetailTermsParamsService.findExcludeByFkId(o.getId()));
            o.setStoragePrices(bmsContractStoragePriceService.findByFkId(o.getId()));
        }
        return list;
    }

    private BmsContractCostItem getOnly(String sysContractNo, String billSubjectCode, String billTermsCode, String orgId) {
        return mapper.getOnly(sysContractNo, billSubjectCode, billTermsCode, orgId);
    }

    public void checkSaveBefore(BmsContractCostItemEntity entity) {
        if (BmsContractCostItem.DEL_FLAG_NORMAL.equals(entity.getDelFlag())) {
            BmsContractCostItem item = this.getOnly(entity.getSysContractNo(), entity.getBillSubjectCode(), entity.getBillTermsCode(), entity.getOrgId());
            if (item != null && !item.getId().equals(entity.getId())) {
                throw new BmsException("合同[" + entity.getSysContractNo() + "]费用科目[" + entity.getBillSubjectCode() + "]计费条款[" + entity.getBillTermsCode() + "]已存在");
            }
        }
        // 校验是否已被结算模型引用
        if (this.isCited(entity.getId(), entity.getOrgId())) {
            throw new BmsException("已被结算模型引用，无法更改");
        }
    }

    @Transactional
    public void saveEntity(BmsContractCostItemEntity entity) {
        this.checkSaveBefore(entity);
        this.save(entity);

        List<String> oldParamIds = bmsContractDetailTermsParamsService.findList(new BmsContractDetailTermsParams(entity.getId(), entity.getOrgId()))
                .stream().map(BmsContractDetailTermsParams::getId).collect(Collectors.toList());
        List<BmsContractDetailTermsParamsEntity> includeParams = entity.getIncludeParams();
        if (CollectionUtil.isNotEmpty(includeParams)) {
            for (BmsContractDetailTermsParams includeParam : includeParams) {
                if (StringUtils.isNotBlank(includeParam.getId())) {
                    oldParamIds.remove(includeParam.getId());
                }

                includeParam.setFkId(entity.getId());
                includeParam.setIncludeOrExclude(BmsConstants.INCLUDE);
                includeParam.setOrgId(entity.getOrgId());
                bmsContractDetailTermsParamsService.save(includeParam);
            }
        }
        List<BmsContractDetailTermsParamsEntity> excludeParams = entity.getExcludeParams();
        if (CollectionUtil.isNotEmpty(excludeParams)) {
            for (BmsContractDetailTermsParams excludeParam : excludeParams) {
                if (StringUtils.isNotBlank(excludeParam.getId())) {
                    oldParamIds.remove(excludeParam.getId());
                }

                excludeParam.setFkId(entity.getId());
                excludeParam.setIncludeOrExclude(BmsConstants.EXCLUDE);
                excludeParam.setOrgId(entity.getOrgId());
                bmsContractDetailTermsParamsService.save(excludeParam);
            }
        }
        oldParamIds.forEach(id -> bmsContractDetailTermsParamsService.delete(new BmsContractDetailTermsParams(id)));
    }

    @Override
    @Transactional
    public void delete(BmsContractCostItem entity) {
        // 校验是否已被结算模型引用
        if (this.isCited(entity.getId(), entity.getOrgId())) {
            throw new BmsException("已被结算模型引用，无法删除");
        }
        // 删除仓储价格
        bmsContractStoragePriceService.deleteByHeaderId(entity.getId());
        // 删除合同明细条款明细
        bmsContractDetailTermsParamsService.deleteByFkId(entity.getId());
        // 删除合同明细
        super.delete(entity);
    }

    @Transactional
    public void deleteByContract(String sysContractNo, String orgId) {
        mapper.deleteByContract(sysContractNo, orgId);
    }

    /**
     * 是否引用
     *
     * @param id    合同明细ID
     * @param orgId 机构ID
     */
    public boolean isCited(String id, String orgId) {
        int count = mapper.isCited(id, orgId);
        return count > 0;
    }
}