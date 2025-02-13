package com.yunyou.modules.wms.kit.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;
import com.yunyou.modules.wms.kit.mapper.BanQinCdWhBomStepMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组合件加工工序Service
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhBomStepService extends CrudService<BanQinCdWhBomStepMapper, BanQinCdWhBomStep> {

    public BanQinCdWhBomStep get(String id) {
        return super.get(id);
    }

    public List<BanQinCdWhBomStep> findList(BanQinCdWhBomStep banQinCdWhBomStep) {
        return super.findList(banQinCdWhBomStep);
    }

    public Page<BanQinCdWhBomStep> findPage(Page<BanQinCdWhBomStep> page, BanQinCdWhBomStep banQinCdWhBomStep) {
        dataRuleFilter(banQinCdWhBomStep);
        banQinCdWhBomStep.setPage(page);
        page.setList(mapper.findPage(banQinCdWhBomStep));
        return page;
    }

    @Transactional
    public void save(BanQinCdWhBomStep banQinCdWhBomStep) {
        super.save(banQinCdWhBomStep);
    }

    @Transactional
    public void delete(BanQinCdWhBomStep banQinCdWhBomStep) {
        super.delete(banQinCdWhBomStep);
    }

    /**
     * 描述：获取新的明细行号
     * <p>
     * create by Jianhua on 2019/8/20
     */
    public String getNewLineNo(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        Integer maxLineNo = mapper.getMaxLineNo(ownerCode, parentSkuCode, kitType, orgId);
        return String.format("%02d", (maxLineNo == null ? 0 : maxLineNo) + 1);
    }

    @Transactional
    public void deleteByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        mapper.deleteByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
    }

    public List<BanQinCdWhBomStep> getByOwnerAndParentSkuAndKitType(String ownerCode, String parentSkuCode, String kitType, String orgId) {
        BanQinCdWhBomStep cdWhBomStep = new BanQinCdWhBomStep();
        cdWhBomStep.setOwnerCode(ownerCode);
        cdWhBomStep.setParentSkuCode(parentSkuCode);
        cdWhBomStep.setKitType(kitType);
        cdWhBomStep.setOrgId(orgId);
        return this.findList(cdWhBomStep);
    }
}