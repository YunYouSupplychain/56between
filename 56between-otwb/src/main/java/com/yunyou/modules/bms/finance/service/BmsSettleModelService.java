package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.finance.entity.BmsSettleModel;
import com.yunyou.modules.bms.finance.entity.BmsSettleModelDetailParams;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsSettleModelEntity;
import com.yunyou.modules.bms.finance.mapper.BmsSettleModelMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结算模型Service
 *
 * @author Jianhua Liu
 * @version 2019-06-13
 */
@Service
@Transactional(readOnly = true)
public class BmsSettleModelService extends CrudService<BmsSettleModelMapper, BmsSettleModel> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BmsSettleModelDetailService bmsSettleModelDetailService;

    @Override
    @Transactional
    public void save(BmsSettleModel bmsSettleModel) {
        if (StringUtils.isBlank(bmsSettleModel.getSettleModelCode())) {
            bmsSettleModel.setSettleModelCode(noService.getDocumentNo(GenNoType.BMS_SETTLEMENT_MODEL_NO.name()));
        }
        super.save(bmsSettleModel);
    }

    @Override
    @Transactional
    public void delete(BmsSettleModel bmsSettleModel) {
        // 删除模型明细
        bmsSettleModelDetailService.deleteByModelCode(bmsSettleModel.getSettleModelCode(), bmsSettleModel.getOrgId());
        // 删除模型
        super.delete(bmsSettleModel);
    }

    /**
     * 描述：根据模型编码获取模型
     */
    public BmsSettleModel get(String settleModelCode, String orgId) {
        return mapper.getEntityByCode(settleModelCode, orgId);
    }

    /**
     * 描述：根据ID获取模型扩展实体
     */
    public BmsSettleModelEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据模型编码获取模型扩展实体
     */
    public BmsSettleModelEntity getEntity(String settleModelCode, String orgId) {
        BmsSettleModelEntity entity = mapper.getEntityByCode(settleModelCode, orgId);
        if (entity != null) {
            entity.setDetailList(bmsSettleModelDetailService.findEntityBySettleModelCode(entity.getSettleModelCode(), entity.getOrgId()));
        }
        return entity;
    }

    /**
     * 描述：分页查询
     */
    public Page<BmsSettleModel> findPage(Page<BmsSettleModel> page, BmsSettleModelEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    /**
     * 描述：查找符合的合同明细
     */
    public List<BmsSettleModelDetailEntity> findContractSubject(BmsSettleModelEntity entity) {
        return mapper.findContractSubject(entity);
    }

    /**
     * 描述：模型复制
     * <p>
     * create by Jianhua on 2019/10/16
     */
    @Transactional
    public void copy(String settleModelCode, String orgId) {
        // 模型头部信息
        BmsSettleModelEntity entity = this.getEntity(settleModelCode, orgId);
        entity.setId(null);
        entity.setSettleModelCode(null);
        entity.setOrgId(orgId);
        this.save(entity);
        // 模型明细信息
        for (BmsSettleModelDetailEntity detailEntity : entity.getDetailList()) {
            detailEntity.setId(null);
            detailEntity.setSettleModelCode(entity.getSettleModelCode());
            detailEntity.setOrgId(orgId);
            if (CollectionUtil.isNotEmpty(detailEntity.getIncludeParams())) {
                for (BmsSettleModelDetailParams params : detailEntity.getIncludeParams()) {
                    params.setId(null);
                    params.setFkId(detailEntity.getId());
                    params.setOrgId(orgId);
                }
            }
            if (CollectionUtil.isNotEmpty(detailEntity.getExcludeParams())) {
                for (BmsSettleModelDetailParams params : detailEntity.getExcludeParams()) {
                    params.setId(null);
                    params.setFkId(detailEntity.getId());
                    params.setOrgId(orgId);
                }
            }
            bmsSettleModelDetailService.saveEntity(detailEntity);
        }
    }
}