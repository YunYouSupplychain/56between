package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.service.TmTransportOrderLabelService;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TmDirectDispatchManager extends BaseService {
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmDispatchOrderLabelManager tmDispatchOrderLabelManager;
    @Autowired
    private TmTotalManager tmTotalManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmUpdateStatusManager tmUpdateStatusManager;

    /**
     * 描述：调度派车(选择运输订单直接生成派车单)
     */
    @Transactional
    public String directDispatch(TmDirectDispatch tmDirectDispatch) {
        if (StringUtils.isBlank(tmDirectDispatch.getIds())) {
            throw new TmsException("请选择记录");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getCarrierCode())) {
            throw new TmsException("未指定承运商");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getDispatchOutletCode())) {
            throw new TmsException("未指定派车网点");
        }
        String[] ids = tmDirectDispatch.getIds().split(",");
        String dispatchOutletCode = tmDirectDispatch.getDispatchOutletCode();
        Date dispatchTime = tmDirectDispatch.getDispatchTime() == null ? new Date() : tmDirectDispatch.getDispatchTime();
        String dispatchType = StringUtils.isBlank(tmDirectDispatch.getDispatchType()) ? "5" : tmDirectDispatch.getDispatchType();

        StringBuilder msg = new StringBuilder();
        List<TmTransportOrderEntity> entities = Lists.newArrayList();
        for (String id : ids) {
            TmTransportOrderEntity entity = tmTransportOrderManager.getEntity(id);
            if (entity == null) {
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】未审核", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_40.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已签收", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_50.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已回单", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_90.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已取消", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_99.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已关闭", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (StringUtils.isBlank(entity.getReceiveOutletCode())) {
                entity.setReceiveOutletCode(TmsConstants.DEFAULT_DELIVERY_SITE);
            }
            if (StringUtils.isBlank(entity.getOutletCode())) {
                entity.setOutletCode(TmsConstants.DEFAULT_RECEIVE_SITE);
            }
            // 提货网点未揽收
            if (TmsConstants.TRANSPORT_ORDER_STATUS_10.equals(entity.getOrderStatus())) {
                List<TmTransportOrderLabel> canReceiveLabel =
                        tmTransportOrderManager.findCanReceiveLabel(entity.getTransportNo(), entity.getOrgId());
                for (TmTransportOrderLabel orderLabel : canReceiveLabel) {
                    tmTransportOrderManager.receive(entity.getTransportNo(), orderLabel.getLabelNo(), entity.getReceiveOutletCode());
                }
            }
            // 查找能调度派车使用的运输订单标签
            List<TmTransportOrderLabelEntity> orderLabels =
                    tmTransportOrderManager.findCanDispatchLabel(entity.getTransportNo(), entity.getOrgId());
            if (CollectionUtil.isNotEmpty(orderLabels)) {
                entity.setTmTransportOrderLabelList(orderLabels);
                entities.add(entity);
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            throw new TmsException(msg.toString());
        }
        if (CollectionUtil.isEmpty(entities)) {
            throw new TmsException("运输订单已全部调度派车");
        }

        // 生成派车单头
        TmDispatchOrderEntity entity = new TmDispatchOrderEntity();
        entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        entity.setDispatchTime(dispatchTime);
        entity.setDispatchType(dispatchType);
        entity.setTransportType(TmsConstants.TRANSPORT_METHOD_3);
        entity.setDispatchOutletCode(dispatchOutletCode);
        entity.setDispatcher(tmDirectDispatch.getDispatcher());
        entity.setShift(tmDirectDispatch.getShift());
        entity.setPlatform(tmDirectDispatch.getPlatform());
        entity.setStartAreaId(tmDirectDispatch.getStartAreaId());
        entity.setEndAreaId(tmDirectDispatch.getEndAreaId());
        entity.setCarrierCode(tmDirectDispatch.getCarrierCode());
        entity.setCarNo(tmDirectDispatch.getCarNo());
        entity.setDriver(tmDirectDispatch.getDriver());
        entity.setDriverTel(tmDirectDispatch.getDriverTel());
        entity.setIsException(Global.N);
        entity.setIsAppInput(Global.N);
        entity.setDataSource(SystemAliases.TMS.getCode());
        entity.setRemarks(tmDirectDispatch.getRemarks());
        entity.setBaseOrgId(tmDirectDispatch.getBaseOrgId());
        entity.setOrgId(tmDirectDispatch.getOrgId());
        tmDispatchOrderManager.saveEntity(entity);

        entity = tmDispatchOrderManager.getEntity(entity.getId());
        // 提货网点
        List<String> rOutletCodes = entities.stream().map(TmTransportOrderEntity::getReceiveOutletCode)
                .distinct().filter(o -> !dispatchOutletCode.equals(o)).collect(Collectors.toList());
        // 送货网点
        List<String> sOutletCodes = entities.stream().map(TmTransportOrderEntity::getOutletCode)
                .distinct().collect(Collectors.toList());
        // 生成派车配送点
        int i = 1;
        for (String outletCode : rOutletCodes) {
            TmDispatchOrderSiteEntity orderSite = new TmDispatchOrderSiteEntity();
            orderSite.setDispatchSeq(i++);
            orderSite.setDispatchNo(entity.getDispatchNo());
            if (sOutletCodes.stream().anyMatch(outletCode::equals)) {
                orderSite.setReceiveShip(TmsConstants.RECEIVE + "," + TmsConstants.SHIP);
                sOutletCodes.remove(outletCode);
            } else {
                orderSite.setReceiveShip(TmsConstants.RECEIVE);
            }
            orderSite.setOutletCode(outletCode);
            orderSite.setBaseOrgId(entity.getBaseOrgId());
            orderSite.setOrgId(entity.getOrgId());
            tmDispatchOrderSiteManager.save(orderSite);
        }
        for (String outletCode : sOutletCodes) {
            TmDispatchOrderSiteEntity orderSite = new TmDispatchOrderSiteEntity();
            orderSite.setDispatchSeq(i++);
            orderSite.setDispatchNo(entity.getDispatchNo());
            orderSite.setReceiveShip(TmsConstants.SHIP);
            orderSite.setOutletCode(outletCode);
            orderSite.setBaseOrgId(entity.getBaseOrgId());
            orderSite.setOrgId(entity.getOrgId());
            tmDispatchOrderSiteManager.save(orderSite);
        }

        // 生成派车标签
        for (TmTransportOrderEntity order : entities) {
            for (TmTransportOrderLabel orderLabel : order.getTmTransportOrderLabelList()) {
                TmDispatchOrderLabel rDispatchLabel = new TmDispatchOrderLabel();
                rDispatchLabel.setLabelNo(orderLabel.getLabelNo());
                rDispatchLabel.setDispatchNo(entity.getDispatchNo());
                rDispatchLabel.setDispatchSiteOutletCode(order.getReceiveOutletCode());
                rDispatchLabel.setReceiveShip(TmsConstants.RECEIVE);
                rDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
                rDispatchLabel.setTransportNo(order.getTransportNo());
                rDispatchLabel.setCustomerNo(order.getCustomerNo());
                rDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
                rDispatchLabel.setSkuCode(orderLabel.getSkuCode());
                rDispatchLabel.setQty(orderLabel.getQty());
                rDispatchLabel.setWeight(orderLabel.getWeight());
                rDispatchLabel.setCubic(orderLabel.getCubic());
                rDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
                rDispatchLabel.setOrgId(entity.getOrgId());
                tmDispatchOrderLabelManager.save(rDispatchLabel);

                TmDispatchOrderLabel sDispatchLabel = new TmDispatchOrderLabel();
                sDispatchLabel.setLabelNo(orderLabel.getLabelNo());
                sDispatchLabel.setDispatchNo(entity.getDispatchNo());
                sDispatchLabel.setDispatchSiteOutletCode(order.getOutletCode());
                sDispatchLabel.setReceiveShip(TmsConstants.SHIP);
                sDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
                sDispatchLabel.setTransportNo(order.getTransportNo());
                sDispatchLabel.setCustomerNo(order.getCustomerNo());
                sDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
                sDispatchLabel.setSkuCode(orderLabel.getSkuCode());
                sDispatchLabel.setQty(orderLabel.getQty());
                sDispatchLabel.setWeight(orderLabel.getWeight());
                sDispatchLabel.setCubic(orderLabel.getCubic());
                sDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
                sDispatchLabel.setOrgId(entity.getOrgId());
                tmDispatchOrderLabelManager.save(sDispatchLabel);

                // 标签路由绑定派车单
                TmTransportOrderRoute orderRoute =
                        tmTransportOrderRouteService.getByTransportNoAndLabelNo(order.getTransportNo(), orderLabel.getLabelNo(), order.getBaseOrgId());
                if (!TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo())
                        && !entity.getDispatchNo().equals(orderRoute.getPreAllocDispatchNo())) {
                    throw new TmsException("运输订单[" + order.getTransportNo() + "]标签号[" + orderLabel.getLabelNo() + "]已被其它派车单绑定");
                }
                if (TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo())) {
                    // 运输网点路由标签tm_transport_order_route绑定派车单号
                    orderRoute.setPreAllocDispatchNo(entity.getDispatchNo());
                    tmTransportOrderRouteService.save(orderRoute);
                    // 标签网点路由信息加入配送网点授权信息
                    tmAuthorityManager.addOutletAuthorityData(order.getReceiveOutletCode(), entity.getBaseOrgId(),
                            TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                    tmAuthorityManager.addOutletAuthorityData(order.getOutletCode(), entity.getBaseOrgId(),
                            TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                }
                // 更新运输订单标签状态
                orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_15);
                tmTransportOrderLabelService.save(orderLabel);
            }
        }
        // 统计数量、重量、体积
        tmTotalManager.totalByDispatch(entity.getDispatchNo());
        // 审核
        tmDispatchOrderManager.audit(entity.getId());
        return entity.getDispatchNo();
    }

    /**
     * 描述：校验待调度的运输订单
     */
    public boolean checkOrders(String[] ids) {
        if (ids.length == 0) {
            throw new TmsException("请选择记录");
        }
        StringBuilder msg = new StringBuilder();
        List<TmTransportOrderEntity> entities = Lists.newArrayList();
        for (String id : ids) {
            TmTransportOrderEntity entity = tmTransportOrderManager.getEntity(id);
            if (entity == null) {
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】未审核", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_40.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已签收", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_50.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已回单", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_90.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已取消", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (TmsConstants.TRANSPORT_ORDER_STATUS_99.equals(entity.getOrderStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】已关闭", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (!tmTransportOrderManager.checkCanDirect(entity.getTransportNo(), entity.getBaseOrgId())) {
                msg.append(MessageFormat.format("运输订单【{0}】无可配载数据", entity.getTransportNo())).append("<br>");
                continue;
            }
            if (StringUtils.isBlank(entity.getReceiveOutletCode())) {
                entity.setReceiveOutletCode(TmsConstants.DEFAULT_DELIVERY_SITE);
            }
            if (StringUtils.isBlank(entity.getOutletCode())) {
                entity.setOutletCode(TmsConstants.DEFAULT_RECEIVE_SITE);
            }
            entities.add(entity);
        }
        if (entities.stream().map(TmTransportOrderHeader::getReceiveOutletCode).distinct().count() > 1) {
            msg.append("运输订单存在多个提货网点，不能进行新建派车调度").append("<br>");
        }
        if (StringUtils.isNotBlank(msg)) {
            throw new TmsException(msg.toString());
        }
        return entities.stream().anyMatch(t -> Global.Y.equals(t.getHasDispatch()));
    }

    /**
     * 描述：校验待调度的运输订单
     */
    public boolean checkVehicle(TmDirectDispatch entity) {
        if (StringUtils.isBlank(entity.getCarNo())) {
            throw new TmsException("车牌号不能为空！");
        }
        TmDispatchOrderEntity con = new TmDispatchOrderEntity();
        con.setCarNo(entity.getCarNo());
        con.setStatusList(ImmutableList.of(TmsConstants.DISPATCH_ORDER_STATUS_00, TmsConstants.DISPATCH_ORDER_STATUS_10));
        List<TmDispatchOrderEntity> dispatchOrderList = tmDispatchOrderManager.findEntityList(con);
        return CollectionUtil.isNotEmpty(dispatchOrderList);
    }

    /**
     * 描述：新建调度派车
     */
    @Transactional
    public String directDispatchNew(TmDirectDispatch tmDirectDispatch) {
        if (StringUtils.isBlank(tmDirectDispatch.getLabelIds())) {
            throw new TmsException("请选择记录");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getCarrierCode())) {
            throw new TmsException("未指定承运商");
        }
        if (StringUtils.isBlank(tmDirectDispatch.getDispatchOutletCode())) {
            throw new TmsException("未指定派车网点");
        }
        String[] labelIds = tmDirectDispatch.getLabelIds().split(",");
        Date dispatchTime = tmDirectDispatch.getDispatchTime() == null ? new Date() : tmDirectDispatch.getDispatchTime();
        String dispatchType = StringUtils.isBlank(tmDirectDispatch.getDispatchType()) ? "5" : tmDirectDispatch.getDispatchType();

        StringBuilder msg = new StringBuilder();
        List<TmTransportOrderLabelEntity> labels = Lists.newArrayList();
        for (String labelId : labelIds) {
            TmTransportOrderLabelEntity label = tmTransportOrderManager.getLabelEntity(labelId);
            if (label == null) {
                continue;
            }
            if (TmsConstants.ORDER_LABEL_STATUS_20.equals(label.getStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】标签【{1}】已签收", label.getTransportNo(), label.getLabelNo())).append("<br>");
                continue;
            }
            if (TmsConstants.ORDER_LABEL_STATUS_30.equals(label.getStatus())) {
                msg.append(MessageFormat.format("运输订单【{0}】标签【{1}】已回单", label.getTransportNo(), label.getLabelNo())).append("<br>");
                continue;
            }
            if (StringUtils.isBlank(label.getReceiveOutletCode())) {
                label.setReceiveOutletCode(TmsConstants.DEFAULT_DELIVERY_SITE);
            }
            if (StringUtils.isBlank(label.getOutletCode())) {
                label.setOutletCode(TmsConstants.DEFAULT_RECEIVE_SITE);
            }
            // 提货网点未揽收，自动揽收
            if (TmsConstants.ORDER_LABEL_STATUS_00.equals(label.getStatus())) {
                tmTransportOrderManager.receive(label.getTransportNo(), label.getLabelNo(), label.getReceiveOutletCode());
                label = tmTransportOrderManager.getLabelEntity(labelId);
            }
            labels.add(label);
        }
        if (StringUtils.isNotBlank(msg)) {
            throw new TmsException(msg.toString());
        }
        if (CollectionUtil.isEmpty(labels)) {
            throw new TmsException("已全部调度派车");
        }
        // 把已配载的标签取消绑定
        this.unbindLabel(labels.stream().filter(t -> TmsConstants.ORDER_LABEL_STATUS_15.equals(t.getStatus())).collect(Collectors.toList()));

        // 生成派车单头
        TmDispatchOrderEntity entity;
        TmDispatchOrderEntity con = new TmDispatchOrderEntity();
        con.setCarNo(tmDirectDispatch.getCarNo());
        con.setStatusList(ImmutableList.of(TmsConstants.DISPATCH_ORDER_STATUS_00, TmsConstants.DISPATCH_ORDER_STATUS_10));
        List<TmDispatchOrderEntity> exitsList = tmDispatchOrderManager.findEntityList(con);
        if (Global.Y.equals(tmDirectDispatch.getIsNew()) || CollectionUtil.isEmpty(exitsList)) {
            entity = new TmDispatchOrderEntity();
            entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
            entity.setDispatchTime(dispatchTime);
            entity.setDispatchType(dispatchType);
            entity.setTransportType(TmsConstants.TRANSPORT_METHOD_3);
            entity.setDispatchOutletCode(tmDirectDispatch.getDispatchOutletCode());
            entity.setDispatcher(tmDirectDispatch.getDispatcher());
            entity.setShift(tmDirectDispatch.getShift());
            entity.setPlatform(tmDirectDispatch.getPlatform());
            entity.setStartAreaId(tmDirectDispatch.getStartAreaId());
            entity.setEndAreaId(tmDirectDispatch.getEndAreaId());
            entity.setCarrierCode(tmDirectDispatch.getCarrierCode());
            entity.setCarNo(tmDirectDispatch.getCarNo());
            entity.setDriver(tmDirectDispatch.getDriver());
            entity.setDriverTel(tmDirectDispatch.getDriverTel());
            entity.setIsException(Global.N);
            entity.setIsAppInput(Global.N);
            entity.setDataSource(SystemAliases.TMS.getCode());
            entity.setRemarks(tmDirectDispatch.getRemarks());
            entity.setBaseOrgId(tmDirectDispatch.getBaseOrgId());
            entity.setOrgId(tmDirectDispatch.getOrgId());
            tmDispatchOrderManager.saveEntity(entity);
        } else {
            entity = exitsList.get(0);
            if (TmsConstants.DISPATCH_ORDER_STATUS_10.equals(entity.getDispatchStatus())) {
                tmDispatchOrderManager.cancelAudit(entity.getId());
            }
        }
        entity = tmDispatchOrderManager.getEntity(entity.getId());
        String dispatchOutletCode = entity.getDispatchOutletCode();
        // 提货网点
        List<String> rOutletCodes = labels.stream().map(TmTransportOrderLabelEntity::getReceiveOutletCode)
                .distinct().filter(o -> !dispatchOutletCode.equals(o)).collect(Collectors.toList());
        // 送货网点
        List<String> sOutletCodes = labels.stream().map(TmTransportOrderLabelEntity::getOutletCode)
                .distinct().collect(Collectors.toList());
        // 生成派车配送点
        int i = entity.getTmDispatchOrderSiteList().size() + 1;
        for (String outletCode : rOutletCodes) {
            TmDispatchOrderSite orderSite = tmDispatchOrderSiteManager.getByDispatchNoAndOutletCode(entity.getDispatchNo(), outletCode, entity.getOrgId());
            if (null == orderSite) {
                orderSite = new TmDispatchOrderSiteEntity();
                orderSite.setDispatchSeq(i++);
                orderSite.setDispatchNo(entity.getDispatchNo());
                orderSite.setOutletCode(outletCode);
                orderSite.setBaseOrgId(entity.getBaseOrgId());
                orderSite.setOrgId(entity.getOrgId());
                orderSite.setReceiveShip(TmsConstants.RECEIVE);
            }
            if (TmsConstants.SHIP.equals(orderSite.getReceiveShip())) {
                orderSite.setReceiveShip(TmsConstants.RECEIVE + "," + TmsConstants.SHIP);
            }
            tmDispatchOrderSiteManager.save(orderSite);
        }
        for (String outletCode : sOutletCodes) {
            TmDispatchOrderSite orderSite = tmDispatchOrderSiteManager.getByDispatchNoAndOutletCode(entity.getDispatchNo(), outletCode, entity.getOrgId());
            if (null == orderSite) {
                orderSite = new TmDispatchOrderSiteEntity();
                orderSite.setDispatchSeq(i++);
                orderSite.setDispatchNo(entity.getDispatchNo());
                orderSite.setOutletCode(outletCode);
                orderSite.setBaseOrgId(entity.getBaseOrgId());
                orderSite.setOrgId(entity.getOrgId());
                orderSite.setReceiveShip(TmsConstants.SHIP);
            }
            if (TmsConstants.RECEIVE.equals(orderSite.getReceiveShip())) {
                orderSite.setReceiveShip(TmsConstants.RECEIVE + "," + TmsConstants.SHIP);
            }
            tmDispatchOrderSiteManager.save(orderSite);
        }

        // 生成派车标签
        for (TmTransportOrderLabelEntity orderLabel : labels) {
            TmDispatchOrderLabel rDispatchLabel = new TmDispatchOrderLabel();
            rDispatchLabel.setLabelNo(orderLabel.getLabelNo());
            rDispatchLabel.setDispatchNo(entity.getDispatchNo());
            rDispatchLabel.setDispatchSiteOutletCode(orderLabel.getReceiveOutletCode());
            rDispatchLabel.setReceiveShip(TmsConstants.RECEIVE);
            rDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
            rDispatchLabel.setTransportNo(orderLabel.getTransportNo());
            rDispatchLabel.setCustomerNo(orderLabel.getCustomerNo());
            rDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
            rDispatchLabel.setSkuCode(orderLabel.getSkuCode());
            rDispatchLabel.setQty(orderLabel.getQty());
            rDispatchLabel.setWeight(orderLabel.getWeight());
            rDispatchLabel.setCubic(orderLabel.getCubic());
            rDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
            rDispatchLabel.setOrgId(entity.getOrgId());
            tmDispatchOrderLabelManager.save(rDispatchLabel);

            TmDispatchOrderLabel sDispatchLabel = new TmDispatchOrderLabel();
            sDispatchLabel.setLabelNo(orderLabel.getLabelNo());
            sDispatchLabel.setDispatchNo(entity.getDispatchNo());
            sDispatchLabel.setDispatchSiteOutletCode(orderLabel.getOutletCode());
            sDispatchLabel.setReceiveShip(TmsConstants.SHIP);
            sDispatchLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
            sDispatchLabel.setTransportNo(orderLabel.getTransportNo());
            sDispatchLabel.setCustomerNo(orderLabel.getCustomerNo());
            sDispatchLabel.setOwnerCode(orderLabel.getOwnerCode());
            sDispatchLabel.setSkuCode(orderLabel.getSkuCode());
            sDispatchLabel.setQty(orderLabel.getQty());
            sDispatchLabel.setWeight(orderLabel.getWeight());
            sDispatchLabel.setCubic(orderLabel.getCubic());
            sDispatchLabel.setBaseOrgId(entity.getBaseOrgId());
            sDispatchLabel.setOrgId(entity.getOrgId());
            tmDispatchOrderLabelManager.save(sDispatchLabel);
            // 标签路由绑定派车单
            TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId());
            if (!TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo()) && !entity.getDispatchNo().equals(orderRoute.getPreAllocDispatchNo())) {
                throw new TmsException("运输订单[" + orderLabel.getTransportNo() + "]标签号[" + orderLabel.getLabelNo() + "]已被其它派车单绑定");
            }
            if (TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo())) {
                // 运输网点路由标签tm_transport_order_route绑定派车单号
                orderRoute.setPreAllocDispatchNo(entity.getDispatchNo());
                tmTransportOrderRouteService.save(orderRoute);
                // 标签网点路由信息加入配送网点授权信息
                tmAuthorityManager.addOutletAuthorityData(orderLabel.getReceiveOutletCode(), entity.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                tmAuthorityManager.addOutletAuthorityData(orderLabel.getOutletCode(), entity.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
            }
            // 更新运输订单标签状态
            orderLabel.setStatus(TmsConstants.ORDER_LABEL_STATUS_15);
            tmTransportOrderLabelService.save(orderLabel);
        }
        // 更新运输订单状态
        labels.stream().map(TmTransportOrderLabelEntity::getTransportNo).distinct().forEach(t -> tmUpdateStatusManager.updateTransport(t));
        // 统计数量、重量、体积
        tmTotalManager.totalByDispatch(entity.getDispatchNo());
        // 审核
        tmDispatchOrderManager.audit(entity.getId());
        return entity.getDispatchNo();
    }

    @Transactional
    public void unbindLabel(List<TmTransportOrderLabelEntity> labelList) {
        List<String> dispatchNos = Lists.newArrayList();
        labelList.forEach(t -> {
            TmDispatchOrderLabelEntity con = new TmDispatchOrderLabelEntity();
            con.setLabelNo(t.getLabelNo());
            con.setBaseOrgId(t.getBaseOrgId());
            List<TmDispatchOrderLabelEntity> dispatchLabels = tmDispatchOrderManager.findList(con);
            if (CollectionUtil.isNotEmpty(dispatchLabels)) {
                dispatchLabels.forEach(d -> tmDispatchOrderLabelManager.remove(d));
                if (!dispatchNos.contains(dispatchLabels.get(0).getDispatchNo() + ":" + dispatchLabels.get(0).getOrgId())) {
                    dispatchNos.add(dispatchLabels.get(0).getDispatchNo() + ":" + dispatchLabels.get(0).getOrgId());
                }
            }
        });
        dispatchNos.forEach(t -> {
            String[] split = t.split(":");
            // 统计数量、重量、体积
            tmTotalManager.totalByDispatch(split[0]);
            // 重新生成交接单
            tmHandoverOrderManager.removeByDispatch(split[0], split[1]);
            tmHandoverOrderManager.createByDispatch(split[0]);
        });
    }
}
