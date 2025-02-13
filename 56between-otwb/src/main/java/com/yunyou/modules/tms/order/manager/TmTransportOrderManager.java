package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.mapper.TmTransportOrderMapper;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

/**
 * 描述：运输订单业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmTransportOrderManager extends BaseService {
    @Autowired
    private TmTransportOrderMapper mapper;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderCostService tmTransportOrderCostService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmTransportOrderSkuManager tmTransportOrderSkuManager;
    @Autowired
    private TmTransportOrderLabelManager tmTransportOrderLabelManager;
    @Autowired
    private TmTransportOrderRouteManager tmTransportOrderRouteManager;
    @Autowired
    private TmReceiveManager tmReceiveManager;
    @Autowired
    private TmDeliverManager tmDeliverManager;
    @Autowired
    private TmTotalManager tmTotalManager;
    @Autowired
    private TmUpdateStatusManager tmUpdateStatusManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private SynchronizedNoService noService;

    public TmTransportOrderEntity getEntity(String id) {
        TmTransportOrderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOrderDelivery(tmTransportOrderDeliveryService.getEntity(entity.getTransportNo(), entity.getOrgId()));
            entity.setTmTransportOrderSkuList(mapper.findSkuList(new TmTransportOrderSkuEntity(entity.getTransportNo(), entity.getOrgId())));
            entity.setTmTransportOrderCostList(mapper.findCostList(new TmTransportOrderCostEntity(entity.getTransportNo(), entity.getOrgId())));
        }
        return this.setExtendValue(entity);
    }

    public List<TmTransportOrderSkuEntity> findList(TmTransportOrderSkuEntity qEntity) {
        return mapper.findSkuList(qEntity);
    }

    /**
     * 查找可揽收(新建状态)的订单标签
     */
    public List<TmTransportOrderLabel> findCanReceiveLabel(String transportNo, String orgId) {
        return mapper.findCanReceiveLabel(transportNo, orgId);
    }

    public List<TmTransportOrderCostEntity> findCostList(TmTransportOrderCostEntity qEntity) {
        return mapper.findCostList(qEntity);
    }

    public List<TmTransportOrderLabelEntity> findCanDispatchLabel(String transportNo, String orgId) {
        return mapper.findCanDispatchLabel(transportNo, orgId);
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderEntity> findPage(Page page, TmTransportOrderEntity qEntity) {
        qEntity.setPage(page);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        List<TmTransportOrderEntity> list = mapper.findOrderPage(qEntity);
        list.forEach(this::setExtendValue);
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderSkuEntity> findPage(Page page, TmTransportOrderSkuEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findSkuPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderLabelEntity> findPage(Page page, TmTransportOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findLabelPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderCostEntity> findPage(Page page, TmTransportOrderCostEntity qEntity) {
        qEntity.setPage(page);
        page.setList(mapper.findCostPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderRouteEntity> findPage(Page page, TmTransportOrderRouteEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findRoutePage(qEntity));
        return page;
    }

    /**
     * 描述：设置实体扩展值
     */
    private TmTransportOrderEntity setExtendValue(TmTransportOrderEntity entity) {
        if (entity == null) {
            return entity;
        }
        if (StringUtils.isNotBlank(entity.getReceiveOutletCode())) {// 揽收网点
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getReceiveOutletCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setReceiveOutletName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getPrincipalCode())) {// 委托方
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getPrincipalCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setPrincipalName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getCustomerCode())) {// 客户
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getCustomerCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setCustomerName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getOutletCode())) {// 配送网点
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getOutletCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setOutletName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getShipCode())) {// 发货方
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getShipCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setShipName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getShipCityId())) {// 发货城市
            entity.setShipCity(areaService.getFullName(entity.getShipCityId()));
        }
        if (StringUtils.isNotBlank(entity.getConsigneeCode())) {// 收货方
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getConsigneeCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setConsigneeName(tmTransportObj.getTransportObjName());
            }
        }
        if (StringUtils.isNotBlank(entity.getConsigneeCityId())) {// 目的地城市
            entity.setConsigneeCity(areaService.getFullName(entity.getConsigneeCityId()));
        }
        entity.setOrderDelivery(tmTransportOrderDeliveryService.getEntity(entity.getTransportNo(), entity.getOrgId()));
        List<String> checkList = mapper.checkHasDispatch(entity.getTransportNo(), entity.getBaseOrgId());
        entity.setHasDispatch(CollectionUtil.isNotEmpty(checkList) ? Global.Y : Global.N);
        return entity;
    }

    /**
     * 推荐配送网点
     */
    private String recommendOutlet(String cityId, String baseOrgId) {
        Area area = areaService.get(cityId);
        if (area == null) {
            return null;
        }

        String outletCode = null, areaId = area.getId();
        List<String> areaIds = Lists.newArrayList();
        String[] ids = area.getParentIds().split(",");
        for (int i = ids.length - 1; i >= 0; i--) {
            if (StringUtils.isNotBlank(ids[i])) {
                areaIds.add(ids[i]);
            }
        }

        while (StringUtils.isBlank(outletCode) && StringUtils.isNotBlank(areaId)) {
            List<String> outletCodes = mapper.findOutletByAreaId(areaId, baseOrgId);
            if (CollectionUtil.isNotEmpty(outletCodes)) {
                outletCode = outletCodes.get(0);
                areaId = null;
            } else {
                areaIds.remove(areaId);
                if (CollectionUtil.isNotEmpty(areaIds)) {
                    areaId = areaIds.get(0);
                } else {
                    areaId = null;
                }
            }
        }
        return outletCode;
    }

    @Transactional
    public void saveEntity(TmTransportOrderEntity entity) {
        if (entity.getOrderTime() == null) {
            throw new TmsException("订单时间不能为空");
        }
        if (StringUtils.isBlank(entity.getOrderType())) {
            throw new TmsException("订单类型不能为空");
        }
        if (StringUtils.isBlank(entity.getOrderStatus())) {
            throw new TmsException("订单状态不能为空");
        }
        if (StringUtils.isBlank(entity.getTransportMethod())) {
            throw new TmsException("运输方式不能为空");
        }
        if (StringUtils.isBlank(entity.getCustomerCode())) {
            throw new TmsException("客户不能为空");
        }
        /*if (StringUtils.isBlank(entity.getShipCityId()) && StringUtils.isBlank(entity.getShipCityName())) {
            throw new TmsException("发货城市或发货城市名称不能为空");
        }
        if (StringUtils.isBlank(entity.getConsigneeCityId()) && StringUtils.isBlank(entity.getConsigneeCityName())) {
            throw new TmsException("目的地城市或收货城市名称不能为空");
        }*/
        Office organizationCenter = UserUtils.getOrgCenter(entity.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("订单{0}不是新建状态，无法操作", entity.getTransportNo()));
        }
        if (StringUtils.isBlank(entity.getTransportNo())) {
            entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        }
        if (StringUtils.isBlank(entity.getReceiveOutletCode())) {
            entity.setReceiveOutletCode(TmsConstants.DEFAULT_DELIVERY_SITE);
        }
        if (StringUtils.isBlank(entity.getDataSource())) {
            entity.setDataSource(SystemAliases.TMS.getCode());
        }
        // 推荐配送网点
        if (StringUtils.isBlank(entity.getOutletCode())) {
            entity.setOutletCode(this.recommendOutlet(entity.getConsigneeCityId(), entity.getBaseOrgId()));
        }
        if (StringUtils.isBlank(entity.getDef9())) {
            entity.setDef9("STMS");// 业务订单类型：STMS-系统TMS业务订单
        }
        tmTransportOrderHeaderService.save(entity);
        if (entity.getOrderDelivery() != null) {
            TmTransportOrderDeliveryEntity orderDelivery = entity.getOrderDelivery();
            orderDelivery.setTransportNo(entity.getTransportNo());
            orderDelivery.setOrgId(entity.getOrgId());
            orderDelivery.setBaseOrgId(entity.getBaseOrgId());
            tmTransportOrderDeliveryService.save(orderDelivery);
        }
        // 生成授权数据
        if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
            tmAuthorityManager.genTransportAuthorityData(entity.getTransportNo(), TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), entity.getId());
        }
    }

    @Transactional
    public void removeEntity(TmTransportOrderEntity entity) {
        if (TmsConstants.DS_01.equals(entity.getDataSource())) {
            throw new TmsException(MessageFormat.format("运输订单【{0}】来源于调度计划，请至调度计划操作", entity.getTransportNo()));
        }
        if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]不是新建状态，不能操作", entity.getTransportNo()));
        }
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), entity.getId());
        // 删除标签信息
        List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(entity.getTransportNo(), entity.getOrgId()));
        for (TmTransportOrderLabel orderLabel : orderLabels) {
            tmTransportOrderLabelManager.remove(orderLabel);
        }
        // 删除明细信息
        tmTransportOrderSkuManager.removeAll(tmTransportOrderSkuService.findList(new TmTransportOrderSku(entity.getTransportNo(), entity.getOrgId())));
        // 删除配送信息
        List<TmTransportOrderDelivery> orderDeliveries = tmTransportOrderDeliveryService.findList(new TmTransportOrderDelivery(entity.getTransportNo(), entity.getOrgId()));
        for (TmTransportOrderDelivery orderDelivery : orderDeliveries) {
            tmTransportOrderDeliveryService.delete(orderDelivery);
        }
        // 删除费用信息
        List<TmTransportOrderCost> orderCosts = tmTransportOrderCostService.findList(new TmTransportOrderCost(entity.getTransportNo(), entity.getOrgId()));
        for (TmTransportOrderCost orderCost : orderCosts) {
            tmTransportOrderCostService.delete(orderCost);
        }
        // 删除主体信息
        tmTransportOrderHeaderService.delete(new TmTransportOrderHeader(entity.getId()));
    }

    /**
     * 审核
     */
    @Transactional
    public void audit(String id) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
        if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]不是新建状态，无法操作", orderHeader.getTransportNo()));
        }
        List<TmTransportOrderSku> orderSkuList = tmTransportOrderSkuService.findList(new TmTransportOrderSku(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        if (CollectionUtil.isEmpty(orderSkuList)) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]未维护商品明细，无法操作", orderHeader.getTransportNo()));
        }

        orderHeader.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_10);
        tmTransportOrderHeaderService.save(orderHeader);

        // 统计数量、重量、体积
        tmTotalManager.totalByTransport(orderHeader.getTransportNo());

        // TMS参数：运输订单审核时自动生成默认标签，取消审核时则删除标签(Y:是 N:否)
        final String AUTO_GEN_DEFAULT_LABEL = SysControlParamsUtils.getValue(SysParamConstants.AUTO_GEN_DEFAULT_LABEL, orderHeader.getOrgId());
        if (Global.Y.equals(AUTO_GEN_DEFAULT_LABEL)) {
            // TMS参数：运输订单自动生成默认标签是否按明细生成(Y:是 N:否)
            final String GEN_DEFAULT_LABEL_BY_TRANSPORT_DETAIL = SysControlParamsUtils.getValue(SysParamConstants.GEN_DEFAULT_LABEL_BY_TRANSPORT_DETAIL, orderHeader.getOrgId());
            if (Global.Y.equals(GEN_DEFAULT_LABEL_BY_TRANSPORT_DETAIL)) {
                for (TmTransportOrderSku orderSku : orderSkuList) {
                    this.addLabel(orderHeader.getTransportNo(), orderSku.getLineNo(), null, orderSku.getQty(), orderSku.getWeight(), orderSku.getCubic());
                }
            } else {
                TmTransportOrderDelivery orderDelivery = tmTransportOrderDeliveryService.getByNo(orderHeader.getTransportNo(), orderHeader.getOrgId());
                this.addLabel(orderHeader.getTransportNo(), null, null, orderDelivery.getTotalEaQty(), orderDelivery.getTotalWeight(), orderDelivery.getTotalCubic());
            }
        }
    }

    /**
     * 取消审核
     */
    @Transactional
    public void cancelAudit(String id) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);
        if (!TmsConstants.TRANSPORT_ORDER_STATUS_10.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]不是审核状态，无法操作", orderHeader.getTransportNo()));
        }
        // TMS参数：运输订单审核时自动生成默认标签，取消审核时则删除标签(Y:是 N:否)
        final String AUTO_GEN_DEFAULT_LABEL = SysControlParamsUtils.getValue(SysParamConstants.AUTO_GEN_DEFAULT_LABEL, orderHeader.getOrgId());
        if (Global.Y.equals(AUTO_GEN_DEFAULT_LABEL)) {
            // 取消审核，删除标签
            List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(orderHeader.getTransportNo(), orderHeader.getOrgId()));
            for (TmTransportOrderLabel orderLabel : orderLabels) {
                tmTransportOrderLabelManager.remove(orderLabel);
            }
        }
        orderHeader.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);
        tmTransportOrderHeaderService.save(orderHeader);
    }

    @Transactional
    public void addLabel(String transportNo, String lineNo, String labelNo, Double qty, Double weight, Double cubic) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("订单[{0}]未审核，无法操作", orderHeader.getTransportNo()));
        }
        if (TmsConstants.TRANSPORT_ORDER_STATUS_90.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("订单[{0}]已取消，无法操作", orderHeader.getTransportNo()));
        }
        if (TmsConstants.TRANSPORT_ORDER_STATUS_99.equals(orderHeader.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("订单[{0}]已关闭，无法操作", orderHeader.getTransportNo()));
        }
        if (StringUtils.isBlank(labelNo)) {
            labelNo = tmTransportOrderLabelService.getNewNo(transportNo, orderHeader.getOrgId());
        } else {
            TmTransportOrderLabel orderLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getOrgId());
            if (orderLabel != null) {
                throw new TmsException(MessageFormat.format("订单[{0}]标签号[{1}]已存在", orderHeader.getTransportNo(), labelNo));
            }
        }
        TmTransportOrderSku orderSku;
        if (StringUtils.isNotBlank(lineNo)) {
            List<TmTransportOrderSku> orderSkus = tmTransportOrderSkuService.findList(new TmTransportOrderSku(transportNo, lineNo, orderHeader.getOrgId()));
            if (CollectionUtil.isEmpty(orderSkus)) {
                throw new TmsException(MessageFormat.format("订单[{0}]行号[{1}]不存在", transportNo, lineNo));
            }
            orderSku = orderSkus.get(0);
        } else {
            orderSku = new TmTransportOrderSku();
        }

        // 插入标签
        TmTransportOrderLabel orderLabel = new TmTransportOrderLabel();
        orderLabel.setTransportNo(orderHeader.getTransportNo());
        orderLabel.setCustomerNo(orderHeader.getCustomerNo());
        orderLabel.setOwnerCode(orderHeader.getCustomerCode());
        orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_00);
        orderLabel.setLineNo(lineNo);
        orderLabel.setSkuCode(orderSku.getSkuCode());
        orderLabel.setLabelNo(labelNo);
        orderLabel.setQty(qty);
        orderLabel.setWeight(weight);
        orderLabel.setCubic(cubic);
        orderLabel.setOrgId(orderHeader.getOrgId());
        orderLabel.setBaseOrgId(orderHeader.getBaseOrgId());
        orderLabel.setDef1(orderSku.getDef1());
        orderLabel.setDef2(orderSku.getDef2());
        orderLabel.setDef3(orderSku.getDef3());
        orderLabel.setDef4(orderSku.getDef4());
        orderLabel.setDef5(orderSku.getDef5());
        orderLabel.setDef6(orderSku.getDef6());
        orderLabel.setDef7(orderSku.getDef7());
        orderLabel.setDef8(orderSku.getDef8());
        orderLabel.setDef9(orderSku.getDef9());
        orderLabel.setDef10(orderSku.getDef10());
        tmTransportOrderLabelService.save(orderLabel);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(orderLabel);

        // 插入初始运输网点路由
        TmTransportOrderRoute orderRoute = new TmTransportOrderRoute();
        orderRoute.setTransportNo(transportNo);
        orderRoute.setLabelNo(labelNo);
        orderRoute.setNowOutletCode(orderHeader.getShipCode());
        orderRoute.setNextOutletCode(orderHeader.getReceiveOutletCode());
        orderRoute.setOwnerCode(orderHeader.getCustomerCode());
        orderRoute.setSkuCode(orderSku.getSkuCode());
        orderRoute.setBaseOrgId(orderHeader.getBaseOrgId());
        orderRoute.setPreAllocDispatchNo(TmsConstants.NULL);// 设置默认值
        orderRoute.setDispatchNo(TmsConstants.NULL);// 设置默认值
        tmTransportOrderRouteService.save(orderRoute);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(orderRoute);
        // 更新运输订单状态
        tmUpdateStatusManager.updateTransport(orderHeader.getTransportNo());
        // TMS参数：运输订单添加标签时揽收网点自动收货(Y:是 N:否)
        final String ADD_LABEL_OUTLET_AUTO_RECEIVE = SysControlParamsUtils.getValue(SysParamConstants.ADD_LABEL_OUTLET_AUTO_RECEIVE, orderHeader.getOrgId());
        if (Global.Y.equals(ADD_LABEL_OUTLET_AUTO_RECEIVE)) {
            tmReceiveManager.receiveByLabel(transportNo, labelNo, orderHeader.getReceiveOutletCode());
            tmTransportOrderTrackService.saveTrackNode(transportNo, labelNo, TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE, UserUtils.getUser());
        }
    }

    /**
     * 描述：按标签揽收
     */
    @Transactional
    public void receive(String transportNo, String labelNo, String receiveOutletCode) {
        tmReceiveManager.receiveByLabel(transportNo, labelNo, receiveOutletCode);
        tmTransportOrderTrackService.saveTrackNode(transportNo, labelNo, TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE, UserUtils.getUser());
    }

    /**
     * 描述：取消揽收
     */
    @Transactional
    public void cancelReceive(String transportNo) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);

        // 查找状态为“已收货”的标签
        TmTransportOrderLabel qEntity = new TmTransportOrderLabel();
        qEntity.setTransportNo(orderHeader.getTransportNo());
        qEntity.setStatus(TmsConstants.ORDER_LABEL_STATUS_10);
        qEntity.setOrgId(orderHeader.getOrgId());
        List<TmTransportOrderLabel> orderLabelList = tmTransportOrderLabelService.findList(qEntity);
        for (TmTransportOrderLabel orderLabel : orderLabelList) {
            tmReceiveManager.cancelReceive(transportNo, orderLabel.getLabelNo());
            tmTransportOrderTrackService.removeReceiveTrackNode(transportNo, orderLabel.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE, orderHeader.getReceiveOutletCode());
        }
    }

    /**
     * 回单 - 按运输订单
     */
    @Transactional
    public void receiptByTransport(TmTransportReceiptInfo tmTransportReceiptInfo) {
        // 查找可回单标签
        List<TmTransportOrderLabel> orderLabelList = mapper.findCanReceiptLabel(tmTransportReceiptInfo.getTransportNo(), tmTransportReceiptInfo.getOrgId());
        for (TmTransportOrderLabel orderLabel : orderLabelList) {
            tmTransportReceiptInfo.setLabelNo(orderLabel.getLabelNo());
            this.receiptByLabel(tmTransportReceiptInfo);
        }
    }

    /**
     * 回单 - 按标签
     */
    @Transactional
    public void receiptByLabel(TmTransportReceiptInfo tmTransportReceiptInfo) {
        String transportNo = tmTransportReceiptInfo.getTransportNo();
        String labelNo = tmTransportReceiptInfo.getLabelNo();
        String orgId = tmTransportReceiptInfo.getOrgId();

        TmTransportOrderLabel orderLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(transportNo, labelNo, orgId);
        if (TmsConstants.ORDER_LABEL_STATUS_00.equals(orderLabel.getStatus()) || TmsConstants.ORDER_LABEL_STATUS_10.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签号[{1}]未签收，无法操作", transportNo, labelNo));
        }
        if (TmsConstants.ORDER_LABEL_STATUS_30.equals(orderLabel.getStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]标签号[{1}]已回单，无法操作", transportNo, labelNo));
        }
        // 更新运输订单标签回单信息
        orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_30);
        orderLabel.setReceiptBy(tmTransportReceiptInfo.getReceiptBy());
        orderLabel.setReceiptTime(tmTransportReceiptInfo.getReceiptTime());
        orderLabel.setReceiptRemarks(tmTransportReceiptInfo.getRemarks());
        tmTransportOrderLabelService.save(orderLabel);
        // 更新回单信息
        tmTransportOrderDeliveryService.updateReceiptInfo(tmTransportReceiptInfo);
        // 更新运输订单状态
        tmUpdateStatusManager.updateTransport(transportNo);
    }

    @Transactional
    public void copy(String id) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(id);

        TmTransportOrderEntity entity = new TmTransportOrderEntity();
        BeanUtils.copyProperties(orderHeader, entity);
        entity.setId(TmsConstants.NULL_STRING);
        entity.setRecVer(0);
        entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);

        TmTransportOrderDelivery orderDelivery = tmTransportOrderDeliveryService.getByNo(orderHeader.getTransportNo(), orderHeader.getOrgId());
        TmTransportOrderDeliveryEntity orderDeliveryEntity = new TmTransportOrderDeliveryEntity();
        BeanUtils.copyProperties(orderDelivery, orderDeliveryEntity);
        orderDeliveryEntity.setId(TmsConstants.NULL_STRING);
        orderDeliveryEntity.setSignBy(null);
        orderDeliveryEntity.setSignStatus(Global.N);
        orderDeliveryEntity.setSignTime(null);
        orderDeliveryEntity.setSignRemarks(null);
        orderDeliveryEntity.setReceiptBy(null);
        orderDeliveryEntity.setReceiptStatus(Global.N);
        orderDeliveryEntity.setReceiptTime(null);
        orderDeliveryEntity.setReceiptRemarks(null);
        entity.setOrderDelivery(orderDeliveryEntity);
        this.saveEntity(entity);

        List<TmTransportOrderSku> orderSkuList = tmTransportOrderSkuService.findList(new TmTransportOrderSku(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        for (TmTransportOrderSku orderSku : orderSkuList) {
            orderSku.setId(TmsConstants.NULL_STRING);
            orderSku.setRecVer(0);
            orderSku.setTransportNo(entity.getTransportNo());
            tmTransportOrderSkuService.save(orderSku);
        }
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String transportNo, String orgId, String baseOrgId) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        if (orderHeader == null) {
            return;
        }
        // 删除运输订单
        tmTransportOrderHeaderService.delete(orderHeader);
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), orderHeader.getId());
        // 删除路由跟踪
        TmTransportOrderTrack orderTrack = new TmTransportOrderTrack();
        orderTrack.setTransportNo(transportNo);
        orderTrack.setOrgId(orgId);
        tmTransportOrderTrackService.deleteAll(tmTransportOrderTrackService.findList(orderTrack));
        // 删除收发货记录
        tmReceiveManager.forceRemove(transportNo, orgId, baseOrgId);
        tmDeliverManager.forceRemove(transportNo, orgId, baseOrgId);
        // 删除运输订单路由
        List<TmTransportOrderRoute> orderRoutes = tmTransportOrderRouteService.findList(new TmTransportOrderRoute(transportNo, baseOrgId));
        for (TmTransportOrderRoute orderRoute : orderRoutes) {
            tmTransportOrderRouteManager.remove(orderRoute);
        }
        // 删除运输订单标签
        List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(transportNo, orgId));
        for (TmTransportOrderLabel orderLabel : orderLabels) {
            tmTransportOrderLabelManager.remove(orderLabel);
        }
        // 删除运输订单明细
        mapper.deleteSkuByNo(transportNo, orgId);
        // 删除运输订单配送
        mapper.deleteDeliveryByNo(transportNo, orgId);
    }

    /**
     * 更新物流单号到订单
     *
     * @param taskNo      作业任务号
     * @param logisticsNo 物流单号
     * @param orgId       机构ID
     */
    @Transactional
    public void updateLogisticsNo(String taskNo, String logisticsNo, String orgId) {
        TmTransportOrderHeader query = new TmTransportOrderHeader();
        query.setSourceNo(taskNo);
        query.setOrgId(orgId);
        List<TmTransportOrderHeader> list = tmTransportOrderHeaderService.findList(query);
        for (TmTransportOrderHeader o : list) {
            o.setTrackingNo(logisticsNo);
            tmTransportOrderHeaderService.save(o);
        }
    }

    /**
     * 校验是否存在可调度的标签
     * @param transportNo
     * @param baseOrgId
     * @return
     */
    public Boolean checkCanDirect(String transportNo, String baseOrgId) {
        List<String> checkHasDispatch = mapper.checkHasDispatch(transportNo, baseOrgId);
        List<String> checkHasDispatchNonDepart = mapper.checkHasDispatchNonDepart(transportNo, baseOrgId);
        return CollectionUtil.isEmpty(checkHasDispatch) || (CollectionUtil.isNotEmpty(checkHasDispatch) && CollectionUtil.isNotEmpty(checkHasDispatchNonDepart));
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderLabelEntity> findDirectLabelPage(Page page, TmTransportOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);

        List<TmTransportOrderLabelEntity> directLabelList = mapper.findDirectLabelPage(qEntity);
        directLabelList.forEach(t -> {
            if (StringUtils.isNotBlank(t.getShipCityId())) {// 发货城市
                t.setShipCity(areaService.getFullName(t.getShipCityId()));
            }
            if (StringUtils.isNotBlank(t.getConsigneeCityId())) {// 目的地城市
                t.setConsigneeCity(areaService.getFullName(t.getConsigneeCityId()));
            }
        });
        page.setList(directLabelList);
        return page;
    }

    public TmTransportOrderLabelEntity getLabelEntity(String id) {
        return mapper.getLabelEntity(id);
    }
}
