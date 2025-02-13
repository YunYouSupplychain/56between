package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitResultDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitParentDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmKitResultDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 加工单结果明细Service
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmKitResultDetailService extends CrudService<BanQinWmKitResultDetailMapper, BanQinWmKitResultDetail> {
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;

    public BanQinWmKitResultDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmKitResultDetail> findList(BanQinWmKitResultDetail banQinWmKitResultDetail) {
        return super.findList(banQinWmKitResultDetail);
    }

    public Page<BanQinWmKitResultDetailEntity> findPage(Page page, BanQinWmKitResultDetailEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        List<BanQinWmKitResultDetailEntity> list = mapper.findPage(entity);
        if (CollectionUtil.isNotEmpty(list)) {
            for (BanQinWmKitResultDetailEntity detail : list) {
                detail.setQtyCurrentKitUom(detail.getQtyPlanUom() - detail.getQtyCompleteUom());
                detail.setQtyCurrentKitEa(detail.getQtyPlanEa() - detail.getQtyCompleteEa());
            }
         }
        page.setList(list);
        return page;
    }

    @Transactional
    public void save(BanQinWmKitResultDetail banQinWmKitResultDetail) {
        super.save(banQinWmKitResultDetail);
    }

    @Transactional
    public void delete(BanQinWmKitResultDetail banQinWmKitResultDetail) {
        super.delete(banQinWmKitResultDetail);
    }

    /**
     * 描述：根据加工单号获取最新行号
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public String getNewLineNo(String kitNo, String orgId) {
        Long maxLineNo = mapper.getMaxLineNo(kitNo, orgId);
        if (maxLineNo == null) {
            maxLineNo = 0L;
        }
        return String.format("%02d", maxLineNo + 1);
    }

    /**
     * 描述：根据加工单号、加工结果行号获取父件加工结果明细Model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitResultDetail getByKitNoAndKitLineNo(String kitNo, String kitLineNo, String orgId) {
        BanQinWmKitResultDetail model = new BanQinWmKitResultDetail();
        model.setKitNo(kitNo);
        model.setKitLineNo(kitLineNo);
        model.setOrgId(orgId);
        List<BanQinWmKitResultDetail> list = this.findList(model);
        if(CollectionUtil.isNotEmpty(list) && list.size() == 1){
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据加工单号、父件明细行号获取父件加工结果明细Models
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitResultDetail> getByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) {
        BanQinWmKitResultDetail model = new BanQinWmKitResultDetail();
        model.setKitNo(kitNo);
        model.setParentLineNo(parentLineNo);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据加工单号获取父件加工结果明细Entities
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitResultDetailEntity> getEntityByKitNo(String kitNo, String orgId) {
        return mapper.getEntityByKitNo(kitNo, orgId);
    }

    /**
     * 描述：根据加工单号、加工结果行号获取父件加工结果明细Entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitResultDetailEntity getEntityByKitNoAndKitLineNo(String kitNo, String kitLineNo, String orgId) {
        BanQinWmKitResultDetailEntity entity = mapper.getEntityByKitNoAndKitLineNo(kitNo, kitLineNo, orgId);
        if (entity != null) {
            // 完全加工
            if (entity.getStatus().equals(WmsCodeMaster.KIT_FULL_KIT.getCode())) {
                entity.setQtyCurrentKitEa(entity.getQtyCompleteEa());
                entity.setQtyCurrentKitUom(entity.getQtyCompleteUom());
            }
            // 未加工如果可加工数为空，那么重新计算
            else if (entity.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode()) && entity.getQtyCompleteEa() == 0) {
                List<BanQinWmKitSubDetail> subModels = this.banQinWmKitSubDetailService.getByKitNoAndParentLineNo(kitNo, entity.getParentLineNo(), orgId);
                // 获取可加工数
                Double mixNum = Double.MAX_VALUE;
                for (BanQinWmKitSubDetail subModel : subModels) {
                    if (subModel.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                        // 如果是辅料，不参与计算
                        continue;
                    }
                    Double temp = subModel.getQtyPkEa() / subModel.getQtyBomEa();
                    // 向下取整
                    temp = Math.floor(temp);
                    if (temp <= mixNum) {
                        mixNum = temp;
                    }
                }
                if (mixNum.compareTo(entity.getQtyPlanEa()) < 0) {
                    entity.setQtyCurrentKitEa(mixNum);
                } else {
                    entity.setQtyCurrentKitEa(entity.getQtyPlanEa());
                }
                entity.setQtyCurrentKitUom(entity.getQtyCurrentKitEa() / entity.getUomQty());
            }
        }
        return entity;
    }

    /**
     * 保存加工单父件加工结果(初始数据来源父件明细)
     */
    @Transactional
    public BanQinWmKitResultDetailEntity saveEntity(BanQinWmKitParentDetailEntity parentEntity) throws WarehouseException {
        List<BanQinWmKitResultDetail> models = this.getByKitNoAndParentLineNo(parentEntity.getKitNo(), parentEntity.getParentLineNo(), parentEntity.getOrgId());
        if (models.size() > 1) {
            return new BanQinWmKitResultDetailEntity();
        }
        BanQinWmKitResultDetail model = new BanQinWmKitResultDetail();
        BeanUtils.copyProperties(parentEntity, model);
        if (models.size() == 0) {
            // 记录0，表示新增
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            model.setHeaderId(parentEntity.getHeaderId());
            // 新增保存
            model.setKitLineNo(this.getNewLineNo(model.getKitNo(), model.getOrgId()));
            model.setStatus(WmsCodeMaster.KIT_NEW.getCode());// 状态00
            model.setSubSelectCode(WmsCodeMaster.SUB_KIT_RT.getCode());// 按周转选择
            model.setKitLoc(parentEntity.getPlanKitLoc());// 加工台
            model.setKitTraceId(WmsConstants.TRACE_ID);
        } else if (models.size() == 1) {
            // 编辑保存
            BanQinWmKitResultDetail modelTemp = models.get(0);
            model.setId(modelTemp.getId());
            model.setCreateBy(modelTemp.getCreateBy());
            model.setCreateDate(modelTemp.getCreateDate());
            model.setKitLineNo(modelTemp.getKitLineNo());
            model.setKitLoc(parentEntity.getPlanKitLoc());// 加工台
        }
        this.save(model);
        return getEntityByKitNoAndKitLineNo(model.getKitNo(), model.getKitLineNo(), model.getOrgId());
    }

    /**
     * 描述：根据加工单号、跟踪号获取父件加工结果明细Models
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitResultDetail> getByKitNoAndTraceId(String kitNo, String kitTraceId, String status, String orgId) {
        BanQinWmKitResultDetail model = new BanQinWmKitResultDetail();
        model.setKitNo(kitNo);
        if (!WmsConstants.TRACE_ID.equals(kitTraceId)) {
            model.setKitTraceId(kitTraceId);
        }
        model.setStatus(status);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    public BanQinWmKitResultDetailEntity getEntity(String id) {
        return mapper.getEntity(id);
    }
}