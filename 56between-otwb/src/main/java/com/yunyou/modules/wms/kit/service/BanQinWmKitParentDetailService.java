package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitParentDetailEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmKitParentDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 加工单父件明细Service
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmKitParentDetailService extends CrudService<BanQinWmKitParentDetailMapper, BanQinWmKitParentDetail> {
    @Autowired
    protected BanQinWmsCommonService wmCommon;

    public BanQinWmKitParentDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmKitParentDetail> findList(BanQinWmKitParentDetail banQinWmKitParentDetail) {
        return super.findList(banQinWmKitParentDetail);
    }

    public Page<BanQinWmKitParentDetailEntity> findPage(Page page, BanQinWmKitParentDetailEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmKitParentDetail banQinWmKitParentDetail) {
        super.save(banQinWmKitParentDetail);
    }

    @Transactional
    public void delete(BanQinWmKitParentDetail banQinWmKitParentDetail) {
        super.delete(banQinWmKitParentDetail);
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
     * 描述：根据加工单号、父件明细行号获取父件明细Model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitParentDetail getByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) {
        BanQinWmKitParentDetail model = new BanQinWmKitParentDetail();
        model.setKitNo(kitNo);
        model.setParentLineNo(parentLineNo);
        model.setOrgId(orgId);
        List<BanQinWmKitParentDetail> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据加工单号、父件明细行号获取父件明细Models
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitParentDetail> getByKitNo(String kitNo, String orgId) {
        BanQinWmKitParentDetail model = new BanQinWmKitParentDetail();
        model.setKitNo(kitNo);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据ID获取Entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitParentDetailEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据加工单号、父件明细行号获取父件明细entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitParentDetailEntity getEntityByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) {
        return mapper.getEntityByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
    }

    /**
     * 描述：根据加工单号获取父件明细Entities
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitParentDetailEntity> getEntityByKitNo(String kitNo, String orgId) {
        return mapper.getEntityByKitNo(kitNo, orgId);
    }

    /**
     * 描述：保存父件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitParentDetailEntity saveKitParent(BanQinWmKitParentDetailEntity entity) throws WarehouseException {
        // 新增获取行号
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(IdGen.uuid());
            entity.setIsNewRecord(true);
            entity.setParentLineNo(getNewLineNo(entity.getKitNo(), entity.getOrgId()));// 行号
            entity.setStatus(WmsCodeMaster.KIT_NEW.getCode());// 状态00
            entity.setIsCreateSub(WmsConstants.NO);// 是否生成子件
        }
        // 保存
        this.save(entity);
        return entity;
    }

    /**
     * 描述：按加工单删除父件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public void removeByKitNo(String kitNo, String orgId) throws WarehouseException {
        List<BanQinWmKitParentDetail> models = getByKitNo(kitNo, orgId);
        // 循环商品明细
        for (BanQinWmKitParentDetail model : models) {
            // 如果明细状态不是新增或者取消状态，那么不能删除
            if ((!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) && (!model.getStatus().equals(WmsCodeMaster.KIT_CANCEL.getCode()))) {
                throw new WarehouseException("[" + kitNo + "][" + model.getParentLineNo() + "]不是创建或者取消状态，不能操作");
            }
            if (model.getIsCreateSub().equals(WmsConstants.YES)) {
                throw new WarehouseException(model.getParentLineNo() + "已经生成子件，不能操作");
            }
            this.delete(model);
        }
    }

    /**
     * 描述：根据加工单、父件行号删除父件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public void removeByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        BanQinWmKitParentDetail model = getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        // 创建状态
        if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            throw new WarehouseException("[" + kitNo + "][" + model.getParentLineNo() + "]不是创建或者取消状态，不能操作");
        }
        if (model.getIsCreateSub().equals(WmsConstants.YES)) {
            throw new WarehouseException(model.getParentLineNo() + "已经生成子件，不能操作");
        }
        this.delete(model);
    }

    /**
     * 描述：状态更新
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public void updateStatus(ActionCode actionCode, Double qtyOpEa, BanQinWmKitParentDetail model) {
        if (actionCode.equals(ActionCode.KIN)) {
            // 加工
            model.setQtyKitEa(model.getQtyKitEa() + qtyOpEa);// 实际加工数
        } else if (actionCode.equals(ActionCode.CANCEL_KIN)) {
            // 取消加工
            model.setQtyKitEa(model.getQtyKitEa() - qtyOpEa);
        }

        if (model.getQtyPlanEa().equals(model.getQtyKitEa())) {
            model.setStatus(WmsCodeMaster.KIT_FULL_KIT.getCode());// 完全加工
        } else if (model.getQtyKitEa() > 0 && model.getQtyPlanEa().compareTo(model.getQtyKitEa()) > 0) {
            // 如果数量不相等，那么状态更新为部分加工
            model.setStatus(WmsCodeMaster.KIT_PART_KIT.getCode());// 部分加工
        } else if (model.getQtyKitEa() == 0) {
            // 如果加工数=0，那么状态==00
            model.setStatus(WmsCodeMaster.KIT_NEW.getCode());// 创建
        }
        this.save(model);
    }

}