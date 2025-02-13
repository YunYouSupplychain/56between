package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleHeader;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSaleDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售单明细Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSaleDetailService extends CrudService<BanQinWmSaleDetailMapper, BanQinWmSaleDetail> {
    @Autowired
    @Lazy
    private BanQinWmSaleHeaderService wmSaleHeaderService;
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    private BanQinWmsCommonService wmsCommon;
    @Autowired
    @Lazy
    private BanQinOutboundCommonService outboundCommon;

    public BanQinWmSaleDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmSaleDetail> findList(BanQinWmSaleDetail banQinWmSaleDetail) {
        return super.findList(banQinWmSaleDetail);
    }

    public Page<BanQinWmSaleDetail> findPage(Page<BanQinWmSaleDetail> page, BanQinWmSaleDetail banQinWmSaleDetail) {
        return super.findPage(page, banQinWmSaleDetail);
    }

    @Transactional
    public void save(BanQinWmSaleDetail banQinWmSaleDetail) {
        super.save(banQinWmSaleDetail);
    }

    @Transactional
    public void delete(BanQinWmSaleDetail banQinWmSaleDetail) {
        super.delete(banQinWmSaleDetail);
    }

    public BanQinWmSaleDetail findFirst(BanQinWmSaleDetail example){
        List<BanQinWmSaleDetail> list = this.findList(example);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据单号，查询订单明细entity
     * @param saleNo
     * @return
     */
    public List<BanQinWmSaleDetailEntity> getEntityBySaleNo(String saleNo, String orgId) {
        List<BanQinWmSaleDetailEntity> result = Lists.newArrayList();
        BanQinWmSaleDetail con = new BanQinWmSaleDetail();
        con.setSaleNo(saleNo);
        con.setOrgId(orgId);
        List<BanQinWmSaleDetail> list = this.findList(con);
        for (BanQinWmSaleDetail model : list) {
            BanQinWmSaleDetailEntity entity = new BanQinWmSaleDetailEntity();
            BeanUtils.copyProperties(model, entity);
            result.add(entity);
        }
        return result;
    }

    /**
     * 查询可以生成SO单的SALE明细，过滤取消状态明细
     * @param saleNos
     * @return
     */
    public List<BanQinWmSaleDetailEntity> getDetailBySaleNos(String[] saleNos, String orgId) {
        List<BanQinWmSaleDetailEntity> detailList = Lists.newArrayList();
        List<BanQinWmSaleDetail> items = mapper.getDetailBySaleNo(saleNos, WmsCodeMaster.SALE_CANCEL.getCode(), orgId);
        for (BanQinWmSaleDetail item : items) {
            BanQinWmSaleDetailEntity entity = new BanQinWmSaleDetailEntity();
            BeanUtils.copyProperties(item, entity);
            detailList.add(entity);
        }
        return detailList;
    }
    
    /**
     * 描述： 查询销售单明细
     *
     * @param saleNo     销售单号
     * @param saleLineNo 销售单明细行号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSaleDetail findBySaleNoAndLineNo(String saleNo, String saleLineNo, String orgId) {
        BanQinWmSaleDetail example = new BanQinWmSaleDetail();
        example.setSaleNo(saleNo);
        example.setLineNo(saleLineNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    /**
     * 描述： 查询销售单明细
     *
     * @param saleNo     销售单号
     * @param saleLineNo 销售单明细行号
     * @param orgId
     */
    public BanQinWmSaleDetailEntity findEntityBySaleNoAndLineNo(String saleNo, String saleLineNo, String orgId) {
        BanQinWmSaleDetailEntity entity = new BanQinWmSaleDetailEntity();
        BanQinWmSaleDetail detail = findBySaleNoAndLineNo(saleNo, saleLineNo, orgId);
        BeanUtils.copyProperties(detail, entity);
        return entity;
    }

    /**
     * 根据单号和行号，删除
     *
     * @param saleNo
     * @param lineNos
     */
    @Transactional
    public ResultMessage removeBySaleNoAndLineNo(String saleNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
        BanQinWmSaleHeader wmSaleHeaderModel = wmSaleHeaderService.getBySaleNo(saleNo, orgId);
        if (!WmsCodeMaster.SALE_NEW.getCode().equals(wmSaleHeaderModel.getStatus())) {
            msg.addMessage(saleNo + "不是创建状态，不能操作");
            msg.setSuccess(false);
            return msg;
        }
        // 校验状态
        ResultMessage checkMsg = checkSaleDetailStatus(saleNo, new String[]{WmsCodeMaster.SALE_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()},
                lineNos, new String[]{WmsCodeMaster.SALE_NEW.getCode()}, orgId);
        Object[] checkLineNos = (Object[]) checkMsg.getData();
        if (checkLineNos.length > 0) {
            // 校验是否存在SO单
            errorMsg = checkSaleIsExistSo(saleNo, Arrays.asList(checkLineNos).toArray(new String[]{}), orgId);
            String[] updateLineNos = (String[]) errorMsg.getData();
            if (updateLineNos.length > 0) {
                List<String> lieNoList = Arrays.asList(updateLineNos);
                mapper.deleteWmSaleDetailBySaleNoAndLineNo(saleNo, lieNoList, orgId);
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage("非创建状态、已审核、已冻结的订单，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage("已生成SO单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 根据单号，删除
     *
     * @param saleNos
     */
    @Transactional
    public void removeBySaleNo(String[] saleNos, String orgId) {
        mapper.deleteWmSaleDetailBySaleNo(orgId, Arrays.asList(saleNos));
    }

    /**
     * 根据SALE单号和行号，查询销售明细
     *
     * @param saleNo
     * @param lineNos
     * @return
     */
    public List<BanQinWmSaleDetail> getBySaleNoAndLineNoArray(String saleNo, String[] lineNos, String orgId) {
        return mapper.getDetailBySaleNoAndLineNos(saleNo, Arrays.asList(lineNos), orgId);
    }

    /**
     * 保存销售明细
     * @param model
     * @return
     */
    @Transactional
    public ResultMessage saveSaleDetail(BanQinWmSaleDetail model) {
        ResultMessage msg = new ResultMessage();
        // 新增时，取数据库最大行号
        if (StringUtils.isEmpty(model.getId())) {
            model.setLineNo(wmsUtil.getMaxLineNo("wm_sale_detail", "head_id", model.getHeadId()));
            model.setStatus(WmsCodeMaster.SALE_NEW.getCode());
            model.setQtySoEa(0D);
            model.setQtyAllocEa(0D);
            model.setQtyPkEa(0D);
            model.setQtyPreallocEa(0D);
            model.setQtyShipEa(0D);
            model.setIsEdiSend(WmsConstants.NO);
            model.setEdiSendTime(null);
        }
        model.setPrice(model.getPrice() == null ? 0D : model.getPrice());
        // 生产日期
        Date lotAtt01 = model.getLotAtt01();
        // 失效日期
        Date lotAtt02 = model.getLotAtt02();
        // 入库日期
        Date lotAtt03 = model.getLotAtt03();
        // 校验日期正确性
        try {
            this.wmsCommon.checkProductAndExpiryDate(lotAtt01, lotAtt02, lotAtt03);
        } catch (WarehouseException e) {
            e.printStackTrace();
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;

    }

    /**
     * 取消订单
     *
     * @param wmSaleDetailModel
     * @return
     */
    @Transactional
    public void cancel(BanQinWmSaleDetail wmSaleDetailModel) {
        wmSaleDetailModel.setStatus(WmsCodeMaster.SALE_CANCEL.getCode());
        save(wmSaleDetailModel);
        // 更新头状态
        outboundCommon.updateSaleStatus(Lists.newArrayList(wmSaleDetailModel.getSaleNo()), wmSaleDetailModel.getOrgId());
    }

    /**
     * 查询存在SO单的SALE明细
     *
     * @param saleNo
     * @param lineNos
     * @return
     */
    public ResultMessage checkSaleIsExistSo(String saleNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 存在SO的单号
        List<BanQinWmSaleDetail> items = getSaleDetailExistsSo(saleNo, Arrays.asList(lineNos), orgId);
        List<String> returnLineNos = items.stream().map(BanQinWmSaleDetail::getLineNo).collect(Collectors.toList());
        Object[] minusLineNos = wmsCommon.minus(Arrays.asList(lineNos).toArray(), returnLineNos.toArray());
        String str = "";
        for (Object returnLineNo : returnLineNos) {
            str = str + returnLineNo.toString() + "\n";
        }
        msg.addMessage(str);
        msg.setData(minusLineNos);
        return msg;
    }

    /**
     * 查询存在SO单的SALE明细
     *
     * @param saleNo
     * @param lineNos
     */
    protected List<BanQinWmSaleDetail> getSaleDetailExistsSo(String saleNo, List<String> lineNos, String orgId) {
        return mapper.checkSaleDetailExistsSo(saleNo, lineNos, orgId);
    }

    /**
     * 校验传入的SALE的状态，返回符合状态的SALE，提示不符的SALE
     *
     * @param saleNo
     * @param saleStatus
     * @param auditStatus
     * @param lineNos
     * @param lineStatus
     * @return
     */
    public ResultMessage checkSaleDetailStatus(String saleNo, String[] saleStatus, String[] auditStatus, String[] lineNos, String[] lineStatus, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<BanQinWmSaleDetail> itemList = mapper.checkSaleDetailStatus(saleNo, Arrays.asList(lineNos), Arrays.asList(lineStatus), Arrays.asList(saleStatus), Arrays.asList(auditStatus), orgId);
        List<String> returnLineNos = itemList.stream().map(BanQinWmSaleDetail::getLineNo).collect(Collectors.toList());
        // 不符合条件的单号，提示
        Object[] minusLineNos = wmsCommon.minus(lineNos, returnLineNos.toArray());
        String str = "";
        for (Object minusLineNo : minusLineNos) {
            str = str + minusLineNo.toString() + "\n";
        }
        if (StringUtils.isNotEmpty(str)) {
            msg.addMessage(str);
        }
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 销售单明细更新 数量、状态更新
     * @param action
     * @param qtyEaOp
     * @param saleNo
     * @param saleLineNo
     * @throws WarehouseException
     */
    @Transactional
    public void updateByActionCode(ActionCode action, Double qtyEaOp, String saleNo, String saleLineNo, String orgId) throws WarehouseException {
        BanQinWmSaleDetail model = this.findBySaleNoAndLineNo(saleNo, saleLineNo, orgId);
        // 销售单行取消状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 销售单行已取消
            throw new WarehouseException("销售单行已取消", saleNo, saleLineNo);
        }
        // 预配
        if (action.equals(ActionCode.PREALLOCATION)) {
            // 销售单预配数量+
            model.setQtyPreallocEa(model.getQtyPreallocEa() + qtyEaOp);
            // 所有数量 = 预配数+分配数+拣货数+订货数
            Double qtyAll = model.getQtyPreallocEa() + model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyShipEa();
            // 预配数量+预配操作数量>订货数量，不能预配
            // (并发时，如果多次预配(部分预配的情况)，怎么控制不重复预配？)
            if (qtyAll > model.getQtySaleEa()) {
                throw new WarehouseException(saleNo + "-" + saleLineNo + "数据过期");
            }
        }
        // 两步分配/两步超量分配
        else if (action.equals(ActionCode.ALLOCATION) || action.equals(ActionCode.OVER_ALLOCATION)) {
            // 预配数量<分配操作数量(并发控制)
            if (model.getQtyPreallocEa() < qtyEaOp) {
                throw new WarehouseException(saleNo + "-" + saleLineNo + "数据过期");
            }
            // 预配数量-,分配数量+
            model.setQtyPreallocEa(model.getQtyPreallocEa() - qtyEaOp);
            model.setQtyAllocEa(model.getQtyAllocEa() + qtyEaOp);
            // 所有数量 = 预配数+分配数+拣货数+订货数
            Double qtyAll = model.getQtyPreallocEa() + model.getQtyAllocEa() + model.getQtyPkEa() + model.getQtyShipEa();
            if (qtyAll > model.getQtySaleEa()) {
                throw new WarehouseException(saleNo + "-" + saleLineNo + "数据过期");
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
            if (qtyAll > model.getQtySaleEa()) {
                throw new WarehouseException(saleNo + "-" + saleLineNo + "数据过期");
            }
        }
        // 拣货
        else if (action.equals(ActionCode.PICKING)) {
            // 分配数量 < 拣货操作数量
            // (并发时，如果取消分配，那么分配数量扣减，无法拣货；如果多次拣货，那么分配数量扣减多次，也无法拣货)
            // (并发时，如果多次拣货(部分拣货的情况)，怎么控制不重复拣货？)
            if (model.getQtyAllocEa() < qtyEaOp) {
                throw new WarehouseException("数据过期");
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
                throw new WarehouseException("数据过期");
            }
            // 拣货数量-,发货数量+
            model.setQtyPkEa(model.getQtyPkEa() - qtyEaOp);
            model.setQtyShipEa(model.getQtyShipEa() + qtyEaOp);
        }
        // 取消发货
        else if (action.equals(ActionCode.CANCEL_SHIPMENT)) {
            // 发货数<发货操作数，不能操作
            if (model.getQtyShipEa() < qtyEaOp) {
                throw new WarehouseException("数据过期");
            }
            // 发货数-，拣货数+
            model.setQtyPkEa(model.getQtyPkEa() + qtyEaOp);
            model.setQtyShipEa(model.getQtyShipEa() - qtyEaOp);
        }
        // 取消拣货
        else if (action.equals(ActionCode.CANCEL_PICKING)) {
            // 拣货数量<操作数
            if (model.getQtyPkEa() < qtyEaOp) {
                throw new WarehouseException("数据过期");
            }
            // 拣货数量- ,(拣货数量 = 分配数量,并发由分配明细校验)
            model.setQtyPkEa(model.getQtyPkEa() - qtyEaOp);
        }
        // 取消分配
        else if (action.equals(ActionCode.CANCEL_ALLOCATION)) {
            // 分配数量- ,(完全取消分配明细，分配数量 = 取消分配数量，并发由分配明细校验)
            // 分配数量<操作数
            if (model.getQtyAllocEa() < qtyEaOp) {
                throw new WarehouseException("数据过期");
            }
            model.setQtyAllocEa(model.getQtyAllocEa() - qtyEaOp);
        }
        // 取消预配
        else if (action.equals(ActionCode.CANCEL_PREALLOCATION)) {
            // 预配数量-,(完全取消预配明细，分配数量 = 取消分配数量，并发由分配明细校验)
            // 预配数量<操作数
            if (model.getQtyPreallocEa() < qtyEaOp) {
                throw new WarehouseException("数据过期");
            }
            model.setQtyPreallocEa(model.getQtyPreallocEa() - qtyEaOp);
        }
        // 计算销售单行状态
        String status = this.getStatus(model);
        model.setStatus(status);
        this.save(model);
    }

    /**
     * 计算销售单明细行状态
     * @param wmSaleDetailModel
     * @return
     */
    protected String getStatus(BanQinWmSaleDetail wmSaleDetailModel) {
        // 状态默认00
        String status = WmsCodeMaster.SO_NEW.getCode();
        // 发运70、80
        if (wmSaleDetailModel.getQtyShipEa() > 0) {
            if (wmSaleDetailModel.getQtySaleEa().equals(wmSaleDetailModel.getQtyShipEa())) {
                // 完成发运:订货数量=发运数量
                status = WmsCodeMaster.SO_FULL_SHIPPING.getCode();
            } else {
                // 部分发运
                status = WmsCodeMaster.SO_PART_SHIPPING.getCode();
            }
        }
        // 拣货50、60
        else if (wmSaleDetailModel.getQtyPkEa() > 0) {
            if (wmSaleDetailModel.getQtySaleEa().equals(wmSaleDetailModel.getQtyPkEa())) {
                // 完成拣货:订货数量=拣货数量
                status = WmsCodeMaster.SO_FULL_PICKING.getCode();
            } else {
                // 部分拣货
                status = WmsCodeMaster.SO_PART_PICKING.getCode();
            }
        }
        // 分配30、40
        else if (wmSaleDetailModel.getQtyAllocEa() > 0) {
            if (wmSaleDetailModel.getQtySaleEa().equals(wmSaleDetailModel.getQtyAllocEa())) {
                // 完成分配:订货数量=分配数量
                status = WmsCodeMaster.SO_FULL_ALLOC.getCode();
            } else {
                // 部分分配
                status = WmsCodeMaster.SO_PART_ALLOC.getCode();
            }
        }
        // 预配10、20
        else if (wmSaleDetailModel.getQtyPreallocEa() > 0) {
            if (wmSaleDetailModel.getQtySaleEa().equals(wmSaleDetailModel.getQtyPreallocEa())) {
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
     * 根据货主和商品获取WmSaleDetailModel
     * @param ownerCode
     * @param skuCode
     * @return
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmSaleDetail newModel = new BanQinWmSaleDetail();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmSaleDetail wmSaleDetailModel = this.findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != wmSaleDetailModel) {
            msg.setSuccess(false);
            msg.setData(wmSaleDetailModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }
    
}