package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockSoDetail1QueryEntity;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockSoDetail2QueryEntity;
import com.yunyou.modules.wms.crossDock.entity.BanQinWmAsnDetailReceiveQueryEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 出库单明细Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoDetailService extends CrudService<BanQinWmSoDetailMapper, BanQinWmSoDetail> {
    @Autowired
    private BanQinWmSaleDetailService banQinWmSaleDetailService;
    @Autowired
    @Lazy
    private BanQinOutboundCommonService banQinOutboundCommonService;
    @Autowired
    private BanQinWmsCommonService wmsCommon;
    @Autowired
    private WmsUtil wmsUtil;

    public BanQinWmSoDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmSoDetail> findList(BanQinWmSoDetail banQinWmSoDetail) {
        return super.findList(banQinWmSoDetail);
    }

    public Page<BanQinWmSoDetailEntity> findPage(Page page, BanQinWmSoDetailEntity banQinWmSoDetailEntity) {
        dataRuleFilter(banQinWmSoDetailEntity);
        banQinWmSoDetailEntity.setPage(page);
        List<BanQinWmSoDetailEntity> list = mapper.findPage(banQinWmSoDetailEntity);
        page.setList(list);
        for (BanQinWmSoDetailEntity entity : list) {
            entity.setQtyAllocUom(entity.getQtyAllocEa() / entity.getUomQty());
            entity.setQtyPkUom(entity.getQtyPkEa() / entity.getUomQty());
            entity.setQtyPreallocUom(entity.getQtyPreallocEa() / entity.getUomQty());
            entity.setQtyShipUom(entity.getQtyShipEa() / entity.getUomQty());
            entity.setQtySoUom(entity.getQtySoEa() / entity.getUomQty());
        }
        return page;
    }

    @Transactional
    public void save(BanQinWmSoDetail banQinWmSoDetail) {
        super.save(banQinWmSoDetail);
    }

    @Transactional
    public void delete(BanQinWmSoDetail banQinWmSoDetail) {
        super.delete(banQinWmSoDetail);
    }

    public BanQinWmSoDetail findFirst(BanQinWmSoDetail example) {
        List<BanQinWmSoDetail> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetail> findBySoNo(String soNo, String orgId) {
        BanQinWmSoDetail example = new BanQinWmSoDetail();
        example.setSoNo(soNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param soNo   出库单号
     * @param lineNo 出库单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoDetail findBySoNoAndLineNo(String soNo, String lineNo, String orgId) {
        BanQinWmSoDetail example = new BanQinWmSoDetail();
        example.setSoNo(soNo);
        example.setLineNo(lineNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param soNo    出库单号
     * @param lineNos 出库单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetail> findBySoNoAndLineNos(String soNo, String[] lineNos, String orgId) {
        List<BanQinWmSoDetail> detailList = Lists.newArrayList();
        for (String lineNo : lineNos) {
            BanQinWmSoDetail wmSoDetail = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
            detailList.add(wmSoDetail);
        }
        return detailList;
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param waveNo 波次单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetail> findByWaveNo(String waveNo, String orgId) {
        return mapper.findByWaveNo(waveNo, orgId);
    }

    /**
     * 描述： 查询出库单明细Entity
     *
     * @param waveNo 波次单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetailEntity> findEntityByWaveNo(String waveNo, String orgId) {
        BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
        entity.setWaveNo(waveNo);
        entity.setOrgId(orgId);
        return mapper.findEntityList(entity);
    }

    /**
     * 描述： 查询出库单明细Entity
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetailEntity> findEntityBySoNo(String soNo, String orgId) {
        BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
        entity.setSoNo(soNo);
        entity.setOrgId(orgId);
        return mapper.findEntityList(entity);
    }

    /**
     * 描述： 查询出库单明细Entity
     *
     * @param soNo   出库单号
     * @param lineNo 出库单行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoDetailEntity findEntityBySoNoAndLineNo(String soNo, String lineNo, String orgId) {
        BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
        entity.setSoNo(soNo);
        entity.setLineNo(lineNo);
        entity.setOrgId(orgId);
        List<BanQinWmSoDetailEntity> result = mapper.findEntityList(entity);
        return CollectionUtil.isNotEmpty(result) ? result.get(0) : new BanQinWmSoDetailEntity();
    }

    /**
     * 描述： 保存出库单明细
     *
     * @param entity
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public BanQinWmSoDetailEntity saveSoDetail(BanQinWmSoDetailEntity entity) throws WarehouseException {
        BanQinWmSoDetail model = new BanQinWmSoDetail();
        BeanUtils.copyProperties(entity, model);
        // 新增获取行号
        if (StringUtils.isEmpty(model.getId())) {
            model.setIsNewRecord(true);
            model.setId(IdGen.uuid());
            String lineNo = wmsUtil.getMaxLineNo("wm_so_detail", "head_id", entity.getHeadId());
            model.setLineNo(lineNo);// 行号
            if (StringUtils.isEmpty(model.getOldLineNo())) {
                model.setOldLineNo(lineNo);// 原行号
            }
            model.setStatus(WmsCodeMaster.SO_NEW.getCode());// 状态00
            model.setQtyAllocEa(0D);// 新增默认值
            model.setQtyPkEa(0D);
            model.setQtyPreallocEa(0D);
            model.setQtyShipEa(0D);
        }
        // 如果是从销售单生成的ASN，更新订货数，是要同步到SALE单。
        else if (StringUtils.isNotEmpty(entity.getSaleNo()) && StringUtils.isNotEmpty(entity.getSaleLineNo())) {
            BanQinWmSoDetail query = this.findBySoNoAndLineNo(entity.getSoNo(), entity.getLineNo(), entity.getOrgId());
            Double qtySoEa = query.getQtySoEa();
            Double currentQtySoEa = entity.getQtySoEa();
            if (!qtySoEa.equals(currentQtySoEa)) {
                BanQinWmSaleDetail wmSaleDetailModel = banQinWmSaleDetailService.findBySaleNoAndLineNo(entity.getSaleNo(), entity.getSaleLineNo(), entity.getOrgId());
                // 订货数
                Double expectedQtyEaSale = wmSaleDetailModel.getQtySaleEa();
                Double expectedQtyEaSo = wmSaleDetailModel.getQtySoEa();
                if (expectedQtyEaSale < (expectedQtyEaSo - qtySoEa + currentQtySoEa)) {
                    throw new WarehouseException("订货数不能大于销售单的销售数");// 订货数不能大于销售单{0}的销售数
                } else {
                    wmSaleDetailModel.setQtySoEa(expectedQtyEaSo - qtySoEa + currentQtySoEa);
                    banQinWmSaleDetailService.save(wmSaleDetailModel);
                }
            }

        }
        // 保存
        this.save(model);

        return this.findEntityBySoNoAndLineNo(model.getSoNo(), model.getLineNo(), model.getOrgId());
    }

    /**
     * 描述： 删除出库单明细
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeBySoNo(String soNo, String orgId) throws WarehouseException {
        // 根据soNo获取商品明细
        List<BanQinWmSoDetail> list = this.findBySoNo(soNo, orgId);
        // 循环商品明细
        for (BanQinWmSoDetail model : list) {
            if ((!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) && (!model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode()))) {
                // {0}{1}不是创建或者取消状态，不能操作
                throw new WarehouseException("[" + model.getSoNo() + "][" + model.getLineNo() + "]不是创建或者取消状态，不能操作");
            }
            if (StringUtils.isNotEmpty(model.getCdType())) {
                throw new WarehouseException("已被越库标记，不能操作");
            }
            // 回填SALE单的已生成SO数
            banQinOutboundCommonService.backfillQtySo(model);
            // 删除
            this.delete(model);
        }
    }

    /**
     * 描述： 删除出库单明细
     *
     * @param soNo   出库单号
     * @param lineNo 出库单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeBySoNoAndLineNo(String soNo, String lineNo, String orgId) throws WarehouseException {
        BanQinWmSoDetail model = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
        if (!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
            throw new WarehouseException("[" + model.getSoNo() + "][" + model.getLineNo() + "]不是创建状态，不能操作");
        }
        // 回填SALE单的已生成SO数
        banQinOutboundCommonService.backfillQtySo(model);
        this.delete(model);
    }

    /**
     * 描述： 删除出库单明细
     *
     * @param soNo    出库单号
     * @param lineNos 出库单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public ResultMessage removeBySoNoAndLineNos(String soNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        for (String lineNo : lineNos) {
            try {
                this.removeBySoNoAndLineNo(soNo, lineNo, orgId);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (StringUtils.isNotEmpty(msg.getMessage())) {
            throw new RuntimeException(msg.getMessage());
        }
        return msg;
    }

    /**
     * 描述：查询出库单明细
     *
     * @param processByCode 按波次单号、按出库单号、按出库单号+行号
     * @param noList
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetailEntity> findEntityByProcessCode(String processByCode, List<String> noList, String orgId) {
        List<BanQinWmSoDetailEntity> entities = Lists.newArrayList();

        BanQinWmSoDetailEntity example = new BanQinWmSoDetailEntity();
        example.setOrgId(orgId);
        example.setHoldStatus(WmsCodeMaster.ODHL_NO_HOLD.getCode());// 没有冻结
        // 出库单审核控制参数
        example.setIsAudit(WmsConstants.YES);
        example.setIsIntercept(WmsConstants.YES);
        int size = noList.size();
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            for (int i = 0; i < size; i = i + 999) {
                if (size >= i + 999) {
                    example.setWaveNos(noList.subList(i, i + 999));
                } else {
                    example.setWaveNos(noList.subList(i, size));
                }
                List<BanQinWmSoDetailEntity> list = mapper.findEntityByNos(example);
                entities.addAll(list);
            }
        } else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            for (int i = 0; i < size; i = i + 999) {
                if (size >= i + 999) {
                    example.setSoNos(noList.subList(i, i + 999));
                } else {
                    example.setSoNos(noList.subList(i, size));
                }
                List<BanQinWmSoDetailEntity> list = mapper.findEntityByNos(example);
                entities.addAll(list);
            }
        } else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            for (int i = 0; i < size; i = i + 999) {
                if (size >= i + 999) {
                    example.setSoLineNos(noList.subList(i, i + 999));
                } else {
                    example.setSoLineNos(noList.subList(i, size));
                }
                List<BanQinWmSoDetailEntity> list = mapper.findEntityByNos(example);
                entities.addAll(list);
            }
        }
        return entities;
    }

    /**
     * 描述： 校验出库单明细行
     *
     * @param soNo   出库单号
     * @param lineNo 出库单行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoDetail checkStatus(String soNo, String lineNo, String orgId) throws WarehouseException {
        BanQinWmSoDetail model = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
        if (model == null) {
            // 查询不到出库单{0}行{0}
            throw new WarehouseException("查询不到出库单" + soNo + "行" + lineNo);
        }
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 出库单{0}明细行{1}已经取消
            throw new WarehouseException("出库单" + soNo + "明细行" + lineNo + "已经取消");
        }
        return model;
    }

    /**
     * 描述： 查询可进行拦截的明细
     * <p>过滤出库单行状态00、90</p>
     *
     * @param soNos 出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetail> findInterceptBySoNo(String[] soNos, String orgId) {
        return mapper.findInterceptBySoNo(soNos, orgId);
    }

    /**
     * 描述：出库单明细更新
     *
     * @param action
     * @param qtyEaOp
     * @param soNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public BanQinWmSoDetail updateByActionCode(ActionCode action, Double qtyEaOp, String soNo, String lineNo, String orgId) throws WarehouseException {
        BanQinWmSoDetail model = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
        // 出库单行取消状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 出库单行已取消
            throw new WarehouseException("[" + soNo + "][" + lineNo + "]已取消，不能操作");
        }
        // 预配
        if (action.equals(ActionCode.PREALLOCATION)) {
            // 出库单预配数量+
            model.setQtyPreallocEa(model.getQtyPreallocEa() + qtyEaOp);
            // 所有数量 = 预配数+分配数+拣货数+订货数
            Double qtyAll = model.getQtyPreallocEa() + model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyShipEa();
            // 预配数量+预配操作数量>订货数量，不能预配
            // (并发时，如果多次预配(部分预配的情况)，怎么控制不重复预配？)
            if (qtyAll > model.getQtySoEa()) {
                throw new WarehouseException(soNo + "-" + lineNo + "数据已过期");
            }
        }
        // 两步分配/两步超量分配
        else if (action.equals(ActionCode.ALLOCATION) || action.equals(ActionCode.OVER_ALLOCATION)) {
            // 预配数量<分配操作数量(并发控制)
            if (model.getQtyPreallocEa() < qtyEaOp) {
                throw new WarehouseException(soNo + "-" + lineNo + "数据已过期");
            }
            // 预配数量-,分配数量+
            model.setQtyPreallocEa(model.getQtyPreallocEa() - qtyEaOp);
            model.setQtyAllocEa(model.getQtyAllocEa() + qtyEaOp);
            // 所有数量 = 预配数+分配数+拣货数+订货数
            Double qtyAll = model.getQtyPreallocEa() + model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyShipEa();
            if (qtyAll > model.getQtySoEa()) {
                throw new WarehouseException(soNo + "-" + lineNo + "数据已过期");
            }
        }
        // 一步分配/一步超量分配
        else if (action.equals(ActionCode.ONESTEP_ALLOCATION) || action.equals(ActionCode.ONESTEP_OVER_ALLOCATION)) {
            // 分配数量+
            model.setQtyAllocEa(model.getQtyAllocEa() + qtyEaOp);
            // 所有数量 = 预配数+分配数+拣货数+订货数
            Double qtyAll = model.getQtyPreallocEa() + model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyShipEa();
            // 分配数量 + 分配操作数量 > 订货数量
            // (并发时，如果多次分配，那么控制分配数量<订货数量)
            // (并发时，如果多次分配(部分分配的情况)，怎么控制不重复分配？)
            if (qtyAll > model.getQtySoEa()) {
                throw new WarehouseException(soNo + "-" + lineNo + "数据已过期");
            }
        }
        // 拣货
        else if (action.equals(ActionCode.PICKING)) {
            // 分配数量 < 拣货操作数量
            // (并发时，如果取消分配，那么分配数量扣减，无法拣货；如果多次拣货，那么分配数量扣减多次，也无法拣货)
            // (并发时，如果多次拣货(部分拣货的情况)，怎么控制不重复拣货？)
            if (model.getQtyAllocEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            // 分配数量-，拣货数量+
            model.setQtyAllocEa(model.getQtyAllocEa() - qtyEaOp);
            model.setQtyPkEa(model.getQtyPkEa() + qtyEaOp);
        }
        // 发货
        else if (action.equals(ActionCode.SHIPMENT)) {
            // 拣货数量<发货操作数量
            // (并发时，如果取消拣货，校验状态60，无法发货；如果多次发货，校验状态80，也无法拣货。
            // 分配明细没有拣货、发货数量，只能通过分配明细的状态以及 版本号 控制并发)
            if (model.getQtyPkEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            // 拣货数量-,发货数量+
            model.setQtyPkEa(model.getQtyPkEa() - qtyEaOp);
            model.setQtyShipEa(model.getQtyShipEa() + qtyEaOp);
        }
        // 取消发货
        else if (action.equals(ActionCode.CANCEL_SHIPMENT)) {
            // 发货数<发货操作数，不能操作
            if (model.getQtyShipEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            // 发货数-，拣货数+
            model.setQtyPkEa(model.getQtyPkEa() + qtyEaOp);
            model.setQtyShipEa(model.getQtyShipEa() - qtyEaOp);
        }
        // 取消拣货
        else if (action.equals(ActionCode.CANCEL_PICKING)) {
            // 拣货数量<操作数
            if (model.getQtyPkEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            // 拣货数量- ,(拣货数量 = 分配数量,并发由分配明细校验)
            model.setQtyPkEa(model.getQtyPkEa() - qtyEaOp);
        }
        // 取消分配
        else if (action.equals(ActionCode.CANCEL_ALLOCATION)) {
            // 分配数量- ,(完全取消分配明细，分配数量 = 取消分配数量，并发由分配明细校验)
            // 分配数量<操作数
            if (model.getQtyAllocEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            model.setQtyAllocEa(model.getQtyAllocEa() - qtyEaOp);
        }
        // 取消预配
        else if (action.equals(ActionCode.CANCEL_PREALLOCATION)) {
            // 预配数量-,(完全取消预配明细，分配数量 = 取消分配数量，并发由分配明细校验)
            // 预配数量<操作数
            if (model.getQtyPreallocEa() < qtyEaOp) {
                throw new WarehouseException("数据已过期");
            }
            model.setQtyPreallocEa(model.getQtyPreallocEa() - qtyEaOp);
        }
        // 计算出库单行状态
        model.setStatus(this.getStatus(model));
        this.save(model);
        return this.findBySoNoAndLineNo(model.getSoNo(), model.getLineNo(), model.getOrgId());
    }

    /**
     * 描述： 计算出库单明细行状态
     *
     * @param wmSoDetailModel
     * @author Jianhua on 2019/2/15
     */
    public String getStatus(BanQinWmSoDetail wmSoDetailModel) {
        // 状态默认00
        String status = WmsCodeMaster.SO_NEW.getCode();
        // 发运70、80
        if (wmSoDetailModel.getQtyShipEa() > 0) {
            if (wmSoDetailModel.getQtySoEa().equals(wmSoDetailModel.getQtyShipEa())) {
                // 完成发运:订货数量=发运数量
                status = WmsCodeMaster.SO_FULL_SHIPPING.getCode();
            } else {
                // 部分发运
                status = WmsCodeMaster.SO_PART_SHIPPING.getCode();
            }
        }
        // 拣货50、60
        else if (wmSoDetailModel.getQtyPkEa() > 0) {
            if (wmSoDetailModel.getQtySoEa().equals(wmSoDetailModel.getQtyPkEa())) {
                // 完成拣货:订货数量=拣货数量
                status = WmsCodeMaster.SO_FULL_PICKING.getCode();
            } else {
                // 部分拣货
                status = WmsCodeMaster.SO_PART_PICKING.getCode();
            }
        }
        // 分配30、40
        else if (wmSoDetailModel.getQtyAllocEa() > 0) {
            if (wmSoDetailModel.getQtySoEa().equals(wmSoDetailModel.getQtyAllocEa())) {
                // 完成分配:订货数量=分配数量
                status = WmsCodeMaster.SO_FULL_ALLOC.getCode();
            } else {
                // 部分分配
                status = WmsCodeMaster.SO_PART_ALLOC.getCode();
            }
        }
        // 预配10、20
        else if (wmSoDetailModel.getQtyPreallocEa() > 0) {
            if (wmSoDetailModel.getQtySoEa().equals(wmSoDetailModel.getQtyPreallocEa())) {
                // 完成预配:订货数量=预配数量
                status = WmsCodeMaster.SO_FULL_PREALLOC.getCode();
            } else {
                // 部分预配
                status = WmsCodeMaster.SO_PART_PREALLOC.getCode();
            }
        }
        return status;
    }

    /**
     * 描述： 获取可用订货数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
     * <p>一般可用于可预配、手工预配、可手工分配、一步分配数量</p>
     *
     * @param model
     * @author Jianhua on 2019/2/15
     */
    public Double getAvailableQtySoEa(BanQinWmSoDetail model) {
        // 订单行可预配/可一步分配数量 = 订货数 - 已预配数 - 已分配数 - 已拣货数 - 已发货数
        return model.getQtySoEa() - model.getQtyPreallocEa() - model.getQtyAllocEa() - model.getQtyPkEa() - model.getQtyShipEa();
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param allocRule 分配规则
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoDetail> findByAllocRule(String allocRule, String orgId) {
        BanQinWmSoDetail example = new BanQinWmSoDetail();
        example.setOrgId(orgId);
        example.setAllocRule(allocRule);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询出库单明细
     * <p>供CdWhZone删除方法使用</p>
     *
     * @param zoneCode 库区编码
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage getByZoneCode(String zoneCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过zoneCode循环判断库区是否被调用
        BanQinWmSoDetail newModel = new BanQinWmSoDetail();
        // 设置查询对象的值
        newModel.setZoneCode(zoneCode);
        newModel.setOrgId(orgId);
        // 查询出调用库位的对象
        BanQinWmSoDetail soDetailModel = this.findFirst(newModel);
        // 若此调用对象不为空则说明已经被调用，退出循环并返回
        if (null != soDetailModel) {
            msg.setSuccess(false);
            msg.setData(soDetailModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 查询出库单明细
     * <p>供CdRulePreallocHeader删除方法使用</p>
     *
     * @param ruleCode 预配规则编码
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage getByPreallocRuleCode(String ruleCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 验证是否被WM_SO_DETAIL调用
        BanQinWmSoDetail newModel = new BanQinWmSoDetail();
        newModel.setPreallocRule(ruleCode);
        newModel.setOrgId(orgId);
        BanQinWmSoDetail wmSoDetailModel = this.findFirst(newModel);
        if (null != wmSoDetailModel) {
            msg.setSuccess(false);
            msg.setData(wmSoDetailModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 查询出库单明细
     * <p>供CdRuleRotationHeader删除方法使用</p>
     *
     * @param ruleCode 周转规则编码
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage getByRotationRuleCode(String ruleCode, String orgId) {
        ResultMessage msg = new ResultMessage();

        BanQinWmSoDetail wmSoDetail = new BanQinWmSoDetail();
        wmSoDetail.setRotationRule(ruleCode);
        wmSoDetail.setOrgId(orgId);
        BanQinWmSoDetail wmSoDetailModel = this.findFirst(wmSoDetail);
        if (null != wmSoDetailModel) {
            msg.setSuccess(false);
            msg.setData(wmSoDetailModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 查询出库单明细
     *
     * @param ownerCode 货主
     * @param skuCode   商品
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmSoDetail newModel = new BanQinWmSoDetail();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmSoDetail wmSoDetailModel = this.findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != wmSoDetailModel) {
            msg.setSuccess(false);
            msg.setData(wmSoDetailModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 更新装车状态
     *
     * @param soNo   出库单号
     * @param lineNo 出库单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateLdStatus(String soNo, String lineNo, String orgId) {
        String ldStatus = null;
        List<String> isNull = Lists.newArrayList();
        List<String> isNew = Lists.newArrayList();
        List<String> isLoad = Lists.newArrayList();
        List<String> isPartLoad = Lists.newArrayList();
        List<String> isPartDelivery = Lists.newArrayList();
        List<String> isFullDelivery = Lists.newArrayList();

        List<String> ldStatusList = mapper.findLdStatusBySoNoAndLineNo(soNo, lineNo, orgId);
        for (String status : ldStatusList) {
            if (status == null) {
                isNull.add(status);
            } else if (WmsCodeMaster.LD_NEW.getCode().equals(status)) {
                isNew.add(status);
            } else if (WmsCodeMaster.LD_PART_LOAD.getCode().equals(status)) {
                isPartLoad.add(status);
            } else if (WmsCodeMaster.LD_FULL_LOAD.getCode().equals(status)) {
                isLoad.add(status);
            } else if (WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(status)) {
                isFullDelivery.add(status);
            } else if (WmsCodeMaster.LD_PART_DELIVERY.getCode().equals(status)) {
                isPartDelivery.add(status);
            }
        }
        if (isNew.size() > 0 && isLoad.size() == 0 && isPartLoad.size() == 0 && isFullDelivery.size() == 0 && isPartDelivery.size() == 0) {
            ldStatus = WmsCodeMaster.LD_NEW.getCode();
        } else if (isPartLoad.size() > 0 || (isLoad.size() > 0 && isLoad.size() != ldStatusList.size() && isFullDelivery.size() == 0 && isPartDelivery.size() == 0)) {
            ldStatus = WmsCodeMaster.LD_PART_LOAD.getCode();
        } else if (ldStatusList.size() == isLoad.size()) {
            ldStatus = WmsCodeMaster.LD_FULL_LOAD.getCode();
        } else if (isPartDelivery.size() > 0 || (isFullDelivery.size() > 0 && ldStatusList.size() != isFullDelivery.size())) {
            ldStatus = WmsCodeMaster.LD_PART_DELIVERY.getCode();
        } else if (ldStatusList.size() == isFullDelivery.size()) {
            ldStatus = WmsCodeMaster.LD_FULL_DELIVERY.getCode();
        }
        if (ldStatus != null) {
            BanQinWmSoDetail model = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
            model.setLdStatus(ldStatus);
            this.save(model);
        } else if (isNull.size() == ldStatusList.size()) {
            BanQinWmSoDetail model = this.findBySoNoAndLineNo(soNo, lineNo, orgId);
            model.setLdStatus(ldStatus);
            this.save(model);
        }
    }

    /**
     * 描述： 更新装车状态
     * <p>未生成装车单的发运订单装车交接/取消交接</p>
     *
     * @param soNo   出库单号
     * @param status
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateLdStatusBySoNo(String soNo, String status, String orgId) throws WarehouseException {
        List<BanQinWmSoDetail> models = this.findBySoNo(soNo, orgId);
        for (BanQinWmSoDetail model : models) {
            model.setLdStatus(status);// 装车状态
            this.save(model);
        }
    }

    /**
     * 描述： 更新物流单号
     *
     * @param soNo
     * @param logisticNo
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateLogistic(String soNo, String logisticNo, String orgId) {
        List<BanQinWmSoDetail> wmSoDetails = findBySoNo(soNo, orgId);
        if (CollectionUtil.isNotEmpty(wmSoDetails)) {
            for (BanQinWmSoDetail detail : wmSoDetails) {
                detail.setLogisticNo(logisticNo);
                detail.setUpdateBy(UserUtils.getUser());
                detail.setUpdateDate(new Date());
                this.save(detail);
            }
        }
    }

    /**
     * 获取越库操作记录，按商品
     */
    public List<BanQinWmSoDetailEntity> getEntityByCdAndSku(BanQinWmSoDetailByCdQuery condition) {
        List<BanQinWmSoDetailEntity> entitys = new ArrayList<>();
        List<BanQinWmSoDetailByCdQuery> items = mapper.getEntityByCdAndSku(condition);
        if (items.size() > 0) {
            for (BanQinWmSoDetailByCdQuery item : items) {
                BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
                BeanUtils.copyProperties(item, entity);
                entitys.add(entity);
            }
        }
        return entitys;
    }

    /**
     * 获取越库操作记录，按明细行
     */
    public List<BanQinWmSoDetailEntity> getEntityByCdAndLineNo(List<String> soAndLineNos, String status, String isCd, String[] cdType, String orgId) {
        List<BanQinWmSoDetailEntity> entitys = new ArrayList<>();
        BanQinWmSoDetailByCdQuery condition = new BanQinWmSoDetailByCdQuery();
        condition.setSoAndLineNos(String.join(",", soAndLineNos));
        condition.setIsCd(isCd);// 未越库标记
        condition.setStatus(status);// 状态
        condition.setCdTypes(cdType);
        condition.setOrgId(orgId);
        List<BanQinWmSoDetailByCdQuery> items = mapper.getEntityByCdAndSku(condition);
        if (items.size() > 0) {
            for (BanQinWmSoDetailByCdQuery item : items) {
                BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
                BeanUtils.copyProperties(item, entity);
                entitys.add(entity);
            }
        }
        return entitys;
    }

    /**
     * 获取越库操作记录
     */
    public List<BanQinWmSoDetailEntity> getEntityByCrossDock(BanQinWmAsnDetailReceiveQueryEntity wmAsnDetailReceiveQueryItem) {
        List<BanQinWmSoDetailEntity> entitys = new ArrayList<>();
        // 如果是分拨越库，并且收货，则关联分配明细，按入库单号、收货明细行号查询
        if (wmAsnDetailReceiveQueryItem.getCdType().equals(WmsCodeMaster.CD_TYPE_INDIRECT.getCode()) && WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveQueryItem.getStatus())) {
            BanQinGetCrossDockSoDetail2QueryEntity condition = new BanQinGetCrossDockSoDetail2QueryEntity();
            condition.setAsnNo(wmAsnDetailReceiveQueryItem.getAsnNo());
            condition.setRcvLineNo(wmAsnDetailReceiveQueryItem.getRcvLineNo());
            condition.setIsCd("Y");
            condition.setOrgId(wmAsnDetailReceiveQueryItem.getOrgId());
            List<BanQinGetCrossDockSoDetail2QueryEntity> items = mapper.getCrossDockSoDetail2Query(condition);
            if (items.size() > 0) {
                for (BanQinGetCrossDockSoDetail2QueryEntity item : items) {
                    BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
                    BeanUtils.copyProperties(item, entity);
                    entitys.add(entity);
                }
            }
        } else {
            BanQinGetCrossDockSoDetail1QueryEntity condition = new BanQinGetCrossDockSoDetail1QueryEntity();
            // 如果是分拨越库，并且是创建，则按货主、商品查询
            if (StringUtils.isEmpty(wmAsnDetailReceiveQueryItem.getSoNo()) && StringUtils.isEmpty(wmAsnDetailReceiveQueryItem.getSoLineNo())) {
                condition.setSkuCode(wmAsnDetailReceiveQueryItem.getSkuCode());
                condition.setOwnerCode(wmAsnDetailReceiveQueryItem.getOwnerCode());
                condition.setIsUseable("Y");
                condition.setCdType(WmsCodeMaster.CD_TYPE_INDIRECT.getCode());// 分拨越库，创建状态
            } else {
                // 如果是直接越库，则按出库单号、商品行号查询
                condition.setSoNo(wmAsnDetailReceiveQueryItem.getSoNo());
                condition.setLineNo(wmAsnDetailReceiveQueryItem.getSoLineNo());
            }
            condition.setIsCd("Y");
            condition.setOrgId(wmAsnDetailReceiveQueryItem.getOrgId());
            List<BanQinGetCrossDockSoDetail1QueryEntity> items = mapper.getCrossDockSoDetail1Query(condition);
            if (items.size() > 0) {
                for (BanQinGetCrossDockSoDetail1QueryEntity item : items) {
                    BanQinWmSoDetailEntity entity = new BanQinWmSoDetailEntity();
                    BeanUtils.copyProperties(item, entity);
                    entitys.add(entity);
                }
            }
        }

        return entitys;
    }

    /**
     * 查询越库标记的订单,并且获取符合条件的订单号
     * @param soNos
     * @param orgId
     * @return
     */
    public ResultMessage checkSoExistCd(String[] soNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<String> errorSoNos = new ArrayList<>();
        List<String> items = getCheckSoExistCd(Arrays.asList(soNos), orgId);
        // 不符合条件的单号，提示
        String str = "";
        for (String item : items) {
            if (!errorSoNos.contains(item)) {
                errorSoNos.add(item);
            }
            if (!str.contains(item)) {
                str = str + item + "\n";
            }
        }
        // 符合条件的记录
        Object[] resultNos = wmsCommon.minus(soNos, errorSoNos.toArray());

        msg.addMessage(str);
        msg.setData(resultNos);
        return msg;
    }

    /**
     * 查询越库标记的订单
     * @param soNos
     * @param orgId
     * @return
     */
    protected List<String> getCheckSoExistCd(List<String> soNos, String orgId) {
        return mapper.checkSoExistCd(soNos, orgId);
    }

    /**
     * 订单校验列表数据
     */
    public Page<BanQinWmSoDetailEntity> findOrderCheckPage(Page page, BanQinWmSoDetailEntity banQinWmSoDetailEntity) {
        banQinWmSoDetailEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmSoDetailEntity.getOrgId()));
        dataRuleFilter(banQinWmSoDetailEntity);
        banQinWmSoDetailEntity.setPage(page);
        List<BanQinWmSoDetailEntity> list = mapper.findOrderCheckPage(banQinWmSoDetailEntity);
        page.setList(list);
        for (BanQinWmSoDetailEntity entity : list) {
            entity.setQtyAllocUom(entity.getQtyAllocEa() / entity.getUomQty());
            entity.setQtyPkUom(entity.getQtyPkEa() / entity.getUomQty());
            entity.setQtyPreallocUom(entity.getQtyPreallocEa() / entity.getUomQty());
            entity.setQtyShipUom(entity.getQtyShipEa() / entity.getUomQty());
            entity.setQtySoUom(entity.getQtySoEa() / entity.getUomQty());
        }
        return page;
    }
}