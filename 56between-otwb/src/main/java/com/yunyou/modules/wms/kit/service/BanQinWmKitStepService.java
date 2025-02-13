package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomStep;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitStep;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitStepEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmKitStepMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 加工工序Service
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmKitStepService extends CrudService<BanQinWmKitStepMapper, BanQinWmKitStep> {
    @Autowired
    @Lazy
    private BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    private BanQinCdWhBomStepService banQinCdWhBomStepService;

    public BanQinWmKitStep get(String id) {
        return super.get(id);
    }

    public List<BanQinWmKitStep> findList(BanQinWmKitStep banQinWmKitStep) {
        return super.findList(banQinWmKitStep);
    }

    public Page<BanQinWmKitStep> findPage(Page<BanQinWmKitStep> page, BanQinWmKitStep banQinWmKitStep) {
        return super.findPage(page, banQinWmKitStep);
    }

    @Transactional
    public void save(BanQinWmKitStep banQinWmKitStep) {
        super.save(banQinWmKitStep);
    }

    @Transactional
    public void delete(BanQinWmKitStep banQinWmKitStep) {
        super.delete(banQinWmKitStep);
    }

    /**
     * 描述：根据加工单号、父件加工结果行号获取最新行号
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public String getNewLineNo(String kitNo, String kitLineNo, String orgId) {
        Long maxLineNo = mapper.getMaxLineNo(kitNo, kitLineNo, orgId);
        if (maxLineNo == null) {
            maxLineNo = 0L;
        }
        return String.format("%02d", maxLineNo + 1);
    }

    /**
     * 描述：根据加工单号、父件加工结果行号获取父件结果加工工序信息Model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitStep> getByKitNoAndKitLineNo(String kitNo, String kitLineNo, String orgId) {
        BanQinWmKitStep model = new BanQinWmKitStep();
        model.setKitNo(kitNo);
        model.setKitLineNo(kitLineNo);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据加工单号、父件加工结果行号获取父件结果加工工序信息Entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitStepEntity> getEntityByKitNoAndKitLineNo(String kitNo, String[] kitLineNos, String orgId) {
        return mapper.getEntityByKitNoAndKitLineNo(kitNo, kitLineNos, orgId);
    }

    /**
     * 描述：保存加工工序(根据父件加工结果明细初始信息)
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public List<BanQinWmKitStepEntity> saveEntityByResult(BanQinWmKitResultDetailEntity resultEntity) throws WarehouseException {
        List<BanQinWmKitStepEntity> resultEntities = Lists.newArrayList();
        List<BanQinWmKitStepEntity> entities = this.getEntityByKitNoAndKitLineNo(resultEntity.getKitNo(), new String[]{resultEntity.getKitLineNo()}, resultEntity.getOrgId());
        if (CollectionUtil.isNotEmpty(entities)) {
            // 有记录，先删除，再新增
            for (BanQinWmKitStepEntity entity : entities) {
                this.delete(entity);
            }
        }
        BanQinWmKitHeader headerModel = banQinWmKitHeaderService.getByKitNo(resultEntity.getKitNo(), resultEntity.getOrgId());
        // 查询基础加工工序
        List<BanQinCdWhBomStep> stepModels = banQinCdWhBomStepService.getByOwnerAndParentSkuAndKitType(resultEntity.getOwnerCode(), resultEntity.getParentSkuCode(), headerModel.getKitType(), resultEntity.getOrgId());
        if (CollectionUtil.isNotEmpty(stepModels)) {
            // 新增保存加工工序
            for (BanQinCdWhBomStep stepModel : stepModels) {
                BanQinWmKitStep model = new BanQinWmKitStep();
                BeanUtils.copyProperties(stepModel, model);
                model.setId(IdGen.uuid());
                model.setIsNewRecord(true);
                model.setHeaderId(resultEntity.getHeaderId());
                model.setKitNo(resultEntity.getKitNo());
                model.setKitLineNo(resultEntity.getKitLineNo());
                model.setOwnerCode(resultEntity.getOwnerCode());
                model.setParentSkuCode(resultEntity.getParentSkuCode());
                model.setPackCode(resultEntity.getPackCode());
                model.setUom(resultEntity.getUom());
                // 加工数量
                model.setQtyKit(resultEntity.getQtyPlanUom());
                model.setQtyKitEa(resultEntity.getQtyPlanEa());
                model.setStepLineNo(getNewLineNo(model.getKitNo(), model.getKitLineNo(), model.getOrgId()));// 行号
                // 保存
                this.save(model);
            }
            resultEntities = getEntityByKitNoAndKitLineNo(resultEntity.getKitNo(), new String[]{resultEntity.getKitLineNo()}, resultEntity.getOrgId());
        }
        return resultEntities;
    }

    /**
     * 描述：界面加工工序修改保存操作
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public List<BanQinWmKitStepEntity> saveEntity(List<BanQinWmKitStepEntity> stepEntitys) throws WarehouseException {
        List<BanQinWmKitStep> models = Lists.newArrayList();
        for (BanQinWmKitStepEntity stepEntity : stepEntitys) {
            // 如果操作数!=源加工数，那么拆分加工工序记录
            if (!stepEntity.getQtyKitEa().equals(stepEntity.getQtyOpEa())) {
                BanQinWmKitStep newModel = new BanQinWmKitStep();
                BeanUtils.copyProperties(stepEntity, newModel);
                newModel.setId(IdGen.uuid());
                newModel.setIsNewRecord(true);
                newModel.setRecVer(0);
                newModel.setKitOp(null);
                newModel.setKitTime(null);
                newModel.setStepLineNo(getNewLineNo(newModel.getKitNo(), newModel.getKitLineNo(), newModel.getOrgId()));// 行号
                newModel.setQtyKitEa(newModel.getQtyKitEa() - stepEntity.getQtyOpEa());
                newModel.setQtyKit(newModel.getQtyKitEa() / stepEntity.getUomQty());
                models.add(newModel);
            }
            BanQinWmKitStep model = new BanQinWmKitStep();
            BeanUtils.copyProperties(stepEntity, model);
            model.setQtyKitEa(stepEntity.getQtyOpEa());
            model.setQtyKit(model.getQtyKitEa() / stepEntity.getUomQty());
            models.add(model);
        }
        for (BanQinWmKitStep wmKitStep : models) {
            this.save(wmKitStep);
        }
        return getEntityByKitNoAndKitLineNo(stepEntitys.get(0).getKitNo(), new String[]{stepEntitys.get(0).getKitLineNo()}, stepEntitys.get(0).getOrgId());
    }

}