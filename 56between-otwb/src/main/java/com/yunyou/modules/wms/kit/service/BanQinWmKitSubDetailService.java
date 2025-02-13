package com.yunyou.modules.wms.kit.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitSubDetailEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmKitSubDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 加工单子件明细Service
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmKitSubDetailService extends CrudService<BanQinWmKitSubDetailMapper, BanQinWmKitSubDetail> {
    // 仓储公共类
    @Autowired
    protected BanQinWmsCommonService wmCommon;

    public BanQinWmKitSubDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmKitSubDetail> findList(BanQinWmKitSubDetail banQinWmKitSubDetail) {
        return super.findList(banQinWmKitSubDetail);
    }

    public Page<BanQinWmKitSubDetailEntity> findPage(Page page, BanQinWmKitSubDetailEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmKitSubDetail banQinWmKitSubDetail) {
        super.save(banQinWmKitSubDetail);
    }

    @Transactional
    public void delete(BanQinWmKitSubDetail banQinWmKitSubDetail) {
        super.delete(banQinWmKitSubDetail);
    }

    /**
     * 描述：根据加工单号获取子件明细Models
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitSubDetail> getByKitNo(String kitNo, String orgId) {
        BanQinWmKitSubDetail model = new BanQinWmKitSubDetail();
        model.setKitNo(kitNo);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据加工单号、子件行号获取子件Model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitSubDetail getByKitNoAndSubLineNo(String kitNo, String subLineNo, String orgId) {
        BanQinWmKitSubDetail model = new BanQinWmKitSubDetail();
        model.setKitNo(kitNo);
        model.setSubLineNo(subLineNo);
        model.setOrgId(orgId);
        List<BanQinWmKitSubDetail> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据加工单号、父件明细行号获取子件明细Models
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitSubDetail> getByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) {
        BanQinWmKitSubDetail model = new BanQinWmKitSubDetail();
        model.setKitNo(kitNo);
        model.setParentLineNo(parentLineNo);
        model.setOrgId(orgId);
        return this.findList(model);
    }

    /**
     * 描述：根据加工单号获取子件明细Entities
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitSubDetailEntity> getEntityByKitNo(String kitNo, String orgId) {
        return mapper.getEntityByKitNo(kitNo, orgId);

    }

    /**
     * 描述：根据加工单号、子件行号获取子件entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitSubDetailEntity getEntityByKitNoAndSubLineNo(String kitNo, String subLineNo, String orgId) {
        return mapper.getEntityByKitNoAndSubLineNo(kitNo, subLineNo, orgId);
    }

    /**
     * 描述：保存子件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitSubDetailEntity saveKitSub(BanQinWmKitSubDetailEntity entity) throws WarehouseException {
        if (!entity.getStatus().equals(WmsCodeMaster.SUB_KIT_NEW.getCode())) {
            throw new WarehouseException("[" + entity.getKitNo() + "][" + entity.getSubLineNo() + "]不是创建状态，不能操作");
        }
        this.save(entity);
        return this.getEntityByKitNoAndSubLineNo(entity.getKitNo(), entity.getSubLineNo(), entity.getOrgId());
    }

    /**
     * 描述：根据加工单号、子件行号获取子件明细 用于分配等业务查询
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public List<BanQinWmKitSubDetailEntity> getEntityByKitNoAndSubLineNos(String kitNo, List<String> subLineNos, String orgId) {
        List<BanQinWmKitSubDetailEntity> subDetailEntities = Lists.newArrayList();
        for (String subLineNo : subLineNos) {
            BanQinWmKitSubDetailEntity entity = getEntityByKitNoAndSubLineNo(kitNo, subLineNo, orgId);
            if (entity != null) {
                subDetailEntities.add(entity);
            }
        }
        return subDetailEntities;

    }

    /**
     * 描述：计算加工单子件明细行状态
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public String getStatus(BanQinWmKitSubDetail model) {
        // 状态默认00
        String status = WmsCodeMaster.SUB_KIT_NEW.getCode();
        // 发运70、80
        if (model.getQtyKitEa() > 0) {
            if (model.getQtyPlanEa().equals(model.getQtyKitEa())) {
                // 完成加工:订货数量=加工数量
                status = WmsCodeMaster.SUB_KIT_FULL_KIT.getCode();
            } else {
                // 部分加工
                status = WmsCodeMaster.SUB_KIT_PART_KIT.getCode();
            }
        }
        // 拣货50、60
        else if (model.getQtyPkEa() > 0) {
            if (model.getQtyPlanEa().equals(model.getQtyPkEa())) {
                // 完成拣货:订货数量=拣货数量
                status = WmsCodeMaster.SUB_KIT_FULL_PICKING.getCode();
            } else {
                // 部分拣货
                status = WmsCodeMaster.SUB_KIT_PART_PICKING.getCode();
            }
        }
        // 分配30、40
        else if (model.getQtyAllocEa() > 0) {
            if (model.getQtyPlanEa().equals(model.getQtyAllocEa())) {
                // 完成分配:订货数量=分配数量
                status = WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode();
            } else {
                // 部分分配
                status = WmsCodeMaster.SUB_KIT_PART_ALLOC.getCode();
            }
        }
        return status;
    }

    /**
     * 描述：更新状态
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void updateStatus(ActionCode actionCode, Double qtyOpEa, BanQinWmKitSubDetail model) throws WarehouseException {
        if (actionCode.equals(ActionCode.ONESTEP_ALLOCATION)) {
            // 分配时，分配数 +
            model.setQtyAllocEa(model.getQtyAllocEa() + qtyOpEa);
            // 所有数量 = 分配数+拣货数+加工数
            Double qtyAll = model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyKitEa();
            // 分配数量 + 分配操作数量 > 订货数量
            // (并发时，如果多次分配，那么控制分配数量<预计加工数量)
            // (并发时，如果多次分配(部分分配的情况)，怎么控制不重复分配？)
            if (qtyAll > model.getQtyPlanEa()) {
                throw new WarehouseException(model.getSubLineNo() + "数据已过期");
            }
        } else if (actionCode.equals(ActionCode.CANCEL_ALLOCATION)) {
            // 分配数<操作数，不能操作
            if (model.getQtyAllocEa() < qtyOpEa) {
                throw new WarehouseException("数据已过期");
            }
            // 取消分配，分配数 -
            model.setQtyAllocEa(model.getQtyAllocEa() - qtyOpEa);
        } else if (actionCode.equals(ActionCode.PICKING)) {
            // 拣货确认，分配数 - 拣货数 +
            model.setQtyAllocEa(model.getQtyAllocEa() - qtyOpEa);
            model.setQtyPkEa(model.getQtyPkEa() + qtyOpEa);
            // 所有数量 = 分配数+拣货数+加工数
            Double qtyAll = model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyKitEa();
            if (qtyAll > model.getQtyPlanEa()) {
                throw new WarehouseException(model.getSubLineNo() + "数据已过期");
            }
        } else if (actionCode.equals(ActionCode.CANCEL_PICKING)) {
            // 拣货数<操作数，不能操作
            if (model.getQtyPkEa() < qtyOpEa) {
                throw new WarehouseException("数据已过期");
            }
            // 取消拣货，拣货数 -
            model.setQtyPkEa(model.getQtyPkEa() - qtyOpEa);
        } else if (actionCode.getCode().equals("KOUT")) {
            if (model.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                // 如果是辅料,加工确认，子件出库，加工数+
                model.setQtyKitEa(model.getQtyKitEa() + qtyOpEa);
            } else {
                // 拣货数<操作数，不能操作
                if (model.getQtyPkEa() < qtyOpEa) {
                    throw new WarehouseException(model.getSubLineNo() + "可加工数不足，不能操作");
                }
                // 加工确认，子件出库，拣货数-，加工数+
                model.setQtyPkEa(model.getQtyPkEa() - qtyOpEa);
                model.setQtyKitEa(model.getQtyKitEa() + qtyOpEa);
            }

        } else if (actionCode.getCode().equals("CANCEL_KOUT")) {
            // 加工数<操作数，不能操作
            if (model.getQtyKitEa() < qtyOpEa) {
                throw new WarehouseException(model.getSubLineNo() + "没有足够的加工数取消，不能操作");
            }
            if (model.getSubSkuType().equals(WmsCodeMaster.SUB_SKU_TYPE_ACC.getCode())) {
                // 如果是辅料,取消加工，子件出库，加工数-
                model.setQtyKitEa(model.getQtyKitEa() - qtyOpEa);
            } else {
                // 取消加工，子件加库，拣货数+,加工数-
                model.setQtyPkEa(model.getQtyPkEa() + qtyOpEa);
                model.setQtyKitEa(model.getQtyKitEa() - qtyOpEa);
            }
        }
        model.setStatus(getStatus(model));
        this.save(model);
    }

    /**
     * 描述：根据加工单号获取最新行号
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public String getNewLineNo(String kitNo, String orgId) {
        Long maxLineNo = mapper.getMaxLineNo(kitNo, orgId);
        if (maxLineNo == null) {
            maxLineNo = 0L;
        }
        return String.format("%04d", maxLineNo + 1);
    }
}