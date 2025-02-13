package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsContractDetailTermsParams;
import com.yunyou.modules.bms.basic.service.BmsContractDetailTermsParamsService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetail;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetailParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailParamsEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelEntity;
import com.yunyou.modules.bms.finance.mapper.BmsSettleModelDetailMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：结算模型明细Service
 *
 * @author Jianhua
 */
@Service
@Transactional(readOnly = true)
public class BmsSettleModelDetailService extends CrudService<BmsSettleModelDetailMapper, BmsSettleModelDetail> {
    @Autowired
    private BmsSettleModelDetailParamsService bmsSettleModelDetailParamsService;
    @Autowired
    private BmsContractDetailTermsParamsService bmsContractDetailTermsParamsService;

    public BmsSettleModelDetailEntity getEntity(String id) {
        BmsSettleModelDetailEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setIncludeParams(bmsSettleModelDetailParamsService.findIncludeByFkId(entity.getId()));
            entity.setExcludeParams(bmsSettleModelDetailParamsService.findExcludeByFkId(entity.getId()));
        }
        return entity;
    }

    public BmsSettleModelDetail getOnly(String settleModelCode, String contractCostItemId, String orgId) {
        return mapper.getOnly(settleModelCode, contractCostItemId, orgId);
    }

    public List<BmsSettleModelDetailEntity> findEntityBySettleModelCode(String settleModelCode, String orgId) {
        List<BmsSettleModelDetailEntity> entities = mapper.findBySettleModelCode(settleModelCode, orgId);
        if (CollectionUtil.isNotEmpty(entities)) {
            for (BmsSettleModelDetailEntity entity : entities) {
                entity.setIncludeParams(bmsSettleModelDetailParamsService.findIncludeByFkId(entity.getId()));
                entity.setExcludeParams(bmsSettleModelDetailParamsService.findExcludeByFkId(entity.getId()));
            }
        }
        return entities;
    }

    /**
     * 描述：添加模型明细
     */
    @Transactional
    public void addDetail(BmsSettleModelEntity entity) {
        List<BmsSettleModelDetailEntity> detailList = entity.getDetailList();
        if (CollectionUtil.isEmpty(detailList)) {
            return;
        }
        // 新增的模型明细
        List<BmsSettleModelDetailEntity> insertList = Lists.newArrayList();
        // 同模型+合同明细+机构过滤
        for (BmsSettleModelDetailEntity detailEntity : detailList) {
            BmsSettleModelDetail bmsSettleModelDetail = this.getOnly(entity.getSettleModelCode(), detailEntity.getContractCostItemId(), entity.getOrgId());
            if (bmsSettleModelDetail == null) {
                insertList.add(detailEntity);
            }
        }
        for (BmsSettleModelDetailEntity detailEntity : insertList) {
            List<BmsSettleModelDetailParamsEntity> includeParams = Lists.newArrayList();
            List<BmsSettleModelDetailParamsEntity> excludeParams = Lists.newArrayList();

            List<BmsContractDetailTermsParams> params = bmsContractDetailTermsParamsService.findList(new BmsContractDetailTermsParams(detailEntity.getContractCostItemId(), entity.getOrgId()));
            for (BmsContractDetailTermsParams parameter : params) {
                if (BmsConstants.INCLUDE.equals(parameter.getIncludeOrExclude())) {
                    BmsSettleModelDetailParamsEntity includeParam = new BmsSettleModelDetailParamsEntity();
                    BeanUtils.copyProperties(parameter, includeParam);
                    includeParam.setId(null);
                    includeParam.setFkId(detailEntity.getId());
                    includeParam.setIncludeOrExclude(BmsConstants.INCLUDE);
                    includeParam.setOrgId(entity.getOrgId());
                    includeParams.add(includeParam);
                } else if (BmsConstants.EXCLUDE.equals(parameter.getIncludeOrExclude())) {
                    BmsSettleModelDetailParamsEntity excludeParam = new BmsSettleModelDetailParamsEntity();
                    BeanUtils.copyProperties(parameter, excludeParam);
                    excludeParam.setId(null);
                    excludeParam.setFkId(detailEntity.getId());
                    excludeParam.setIncludeOrExclude(BmsConstants.EXCLUDE);
                    excludeParam.setOrgId(entity.getOrgId());
                    excludeParams.add(excludeParam);
                }
            }
            detailEntity.setSettleModelCode(entity.getSettleModelCode());
            detailEntity.setOrgId(entity.getOrgId());
            detailEntity.setIncludeParams(includeParams);
            detailEntity.setExcludeParams(excludeParams);
            this.saveEntity(detailEntity);
        }
    }

    /**
     * 描述：保存模型明细
     */
    @Transactional
    public void saveEntity(BmsSettleModelDetailEntity entity) {
        // 保存模型明细
        this.save(entity);
        // 保存模型明细参数
        this.saveParameter(entity);
    }

    /**
     * 描述：保存模型明细参数
     */
    @Transactional
    public void saveParameter(BmsSettleModelDetailEntity entity) {
        if (entity == null || StringUtils.isBlank(entity.getId())) {
            return;
        }
        List<BmsSettleModelDetailParamsEntity> includeParams = entity.getIncludeParams();
        if (CollectionUtil.isNotEmpty(includeParams)) {
            for (BmsSettleModelDetailParams includeParam : includeParams) {
                includeParam.setFkId(entity.getId());
                includeParam.setIncludeOrExclude(BmsConstants.INCLUDE);
                includeParam.setOrgId(entity.getOrgId());
                bmsSettleModelDetailParamsService.save(includeParam);
            }
        }
        List<BmsSettleModelDetailParamsEntity> excludeParams = entity.getExcludeParams();
        if (CollectionUtil.isNotEmpty(excludeParams)) {
            for (BmsSettleModelDetailParams excludeParam : excludeParams) {
                excludeParam.setFkId(entity.getId());
                excludeParam.setIncludeOrExclude(BmsConstants.EXCLUDE);
                excludeParam.setOrgId(entity.getOrgId());
                bmsSettleModelDetailParamsService.save(excludeParam);
            }
        }
    }

    /**
     * 描述：删除模型明细
     */
    @Override
    @Transactional
    public void delete(BmsSettleModelDetail entity) {
        // 删除关联参数
        bmsSettleModelDetailParamsService.deleteByFkId(entity.getId());
        // 删除模型明细
        super.delete(entity);
    }

    /**
     * 描述：根据模型编码+机构ID删除模型明细
     */
    @Transactional
    public void deleteByModelCode(String settleModelCode, String orgId) {
        List<BmsSettleModelDetailEntity> entities = mapper.findBySettleModelCode(settleModelCode, orgId);
        for (BmsSettleModelDetailEntity entity : entities) {
            this.delete(entity);
        }
    }
}