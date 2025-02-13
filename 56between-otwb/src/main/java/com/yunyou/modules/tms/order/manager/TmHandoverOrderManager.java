package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.mapper.TmHandoverOrderMapper;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：交接单业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmHandoverOrderManager extends CrudService<TmHandoverOrderMapper, TmHandoverOrderHeader> {
    @Autowired
    private SynchronizedNoService noService; // 编号生成
    @Autowired
    private TmHandoverOrderHeaderService tmHandoverOrderHeaderService;// 交接单主表
    @Autowired
    private TmHandoverOrderLabelService tmHandoverOrderLabelService;// 交接单标签信息
    @Autowired
    private TmHandoverOrderSkuService tmHandoverOrderSkuService;    // 交接单商品信息
    @Autowired
    private TmAttachementDetailService tmAttachementDetailService;// 照片附件处理类
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmSignManager tmSignManager;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmDispatchOrderLabelManager tmDispatchOrderLabelManager;
    @Autowired
    private TmDeliverManager tmDeliverManager;// 网点发货处理类
    @Autowired
    private TmReceiveManager tmReceiveManager;// 网点收货处理类

    /**
     * 描述：分页查询交接单实体信息
     */
    @SuppressWarnings("unchecked")
    public Page<TmHandoverOrderEntity> findPage(Page page, TmHandoverOrderEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    /**
     * 描述：分页查询交接单标签信息
     */
    @SuppressWarnings("unchecked")
    public Page<TmHandoverOrderLabelEntity> findLabelPage(Page page, TmHandoverOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findLabelList(qEntity));
        return page;
    }

    /**
     * 描述：分页查询交接单商品信息
     */
    @SuppressWarnings("unchecked")
    public Page<TmHandoverOrderSkuEntity> findSkuPage(Page page, TmHandoverOrderSkuEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findSkuList(qEntity));
        return page;
    }

    /**
     * 描述：分页查询交接单图片信息
     */
    public Page<TmAttachementDetail> findImgPage(Page<TmAttachementDetail> page, TmAttachementDetail qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(tmAttachementDetailService.findList(qEntity));
        return page;
    }

    @Override
    public List<TmHandoverOrderHeader> findList(TmHandoverOrderHeader qEntity){
        return tmHandoverOrderHeaderService.findList(qEntity);
    }

    /**
     * 描述：根据交接单ID查询交接单实体信息
     */
    public TmHandoverOrderEntity getEntity(String id) {
        TmHandoverOrderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            TmHandoverOrderLabelEntity con = new TmHandoverOrderLabelEntity();
            con.setHandoverNo(entity.getHandoverNo());
            con.setOrgId(entity.getOrgId());
            entity.setTmHandoverOrderLabelList(mapper.findLabelList(con));
        }
        return entity;
    }

    /**
     * 根据派车单生成交接清单
     */
    @Transactional
    public void createByDispatch(String dispatchNo) {
        TmDispatchOrderHeader dispatchOrder = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        List<TmDispatchOrderSiteEntity> siteList = tmDispatchOrderSiteManager.findList(new TmDispatchOrderSiteEntity(dispatchNo, dispatchOrder.getOrgId()));
        for (TmDispatchOrderSiteEntity siteEntity : siteList) {
            TmHandoverOrderHeader handoverOrder = new TmHandoverOrderHeader();
            handoverOrder.setHandoverNo(noService.getDocumentNo(GenNoType.TM_HANDOVER_NO.name()));
            handoverOrder.setDispatchNo(dispatchNo);
            handoverOrder.setDispatchTime(dispatchOrder.getDispatchTime());
            handoverOrder.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_00);
            handoverOrder.setDispatchOutletCode(dispatchOrder.getDispatchOutletCode());
            handoverOrder.setDeliveryOutletCode(siteEntity.getOutletCode());
            handoverOrder.setCarrierCode(dispatchOrder.getCarrierCode());
            handoverOrder.setCarNo(dispatchOrder.getCarNo());
            handoverOrder.setDriver(dispatchOrder.getDriver());
            handoverOrder.setDriverTel(dispatchOrder.getDriverTel());
            handoverOrder.setReceivableQty(siteEntity.getLabelQty());
            handoverOrder.setActualQty(0L);
            handoverOrder.setTrip(dispatchOrder.getTrip());
            handoverOrder.setOrgId(dispatchOrder.getOrgId());
            handoverOrder.setBaseOrgId(dispatchOrder.getBaseOrgId());

            List<TmHandoverOrderLabel> saveLabelList = Lists.newArrayList();
            Map<String, TmHandoverOrderSku> saveSkuMap = Maps.newHashMap();

            TmDispatchOrderLabel con = new TmDispatchOrderLabel();
            con.setDispatchNo(dispatchNo);
            con.setDispatchSiteOutletCode(siteEntity.getOutletCode());
            con.setOrgId(dispatchOrder.getOrgId());
            List<TmDispatchOrderLabel> labelList = tmDispatchOrderLabelService.findList(con);
            for (TmDispatchOrderLabel labelEntity : labelList) {
                TmHandoverOrderLabel handoverOrderLabel = new TmHandoverOrderLabel();
                handoverOrderLabel.setHandoverNo(handoverOrder.getHandoverNo());
                handoverOrderLabel.setLabelNo(labelEntity.getLabelNo());
                handoverOrderLabel.setTransportNo(labelEntity.getTransportNo());
                handoverOrderLabel.setCustomerNo(labelEntity.getCustomerNo());
                handoverOrderLabel.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_00);
                handoverOrderLabel.setReceiveShip(labelEntity.getReceiveShip());
                handoverOrderLabel.setOrgId(handoverOrder.getOrgId());
                handoverOrderLabel.setBaseOrgId(handoverOrder.getBaseOrgId());
                saveLabelList.add(handoverOrderLabel);

                if (StringUtils.isNotBlank(labelEntity.getSkuCode())) {
                    TmTransportOrderLabel transportLabel = tmTransportOrderLabelService.getByTransportNoAndLabelNo(labelEntity.getTransportNo(), labelEntity.getLabelNo(), labelEntity.getOrgId());
                    String checkKey = labelEntity.getTransportNo() + "-" + transportLabel.getLineNo() + "-" + transportLabel.getOwnerCode() + "-" + transportLabel.getSkuCode();
                    TmHandoverOrderSku skuEntity;
                    if (saveSkuMap.containsKey(checkKey)) {
                        skuEntity = saveSkuMap.get(checkKey);
                        skuEntity.setOrderQty(skuEntity.getOrderQty().add(BigDecimal.valueOf(transportLabel.getQty())));
                    } else {
                        skuEntity = new TmHandoverOrderSku();
                        skuEntity.setHandoverNo(handoverOrder.getHandoverNo());
                        skuEntity.setTransportNo(transportLabel.getTransportNo());
                        skuEntity.setCustomerNo(transportLabel.getCustomerNo());
                        skuEntity.setLineNo(transportLabel.getLineNo());
                        skuEntity.setOwnerCode(transportLabel.getOwnerCode());
                        skuEntity.setSkuCode(transportLabel.getSkuCode());
                        skuEntity.setOrderQty(BigDecimal.valueOf(transportLabel.getQty()));
                        skuEntity.setActualQty(BigDecimal.ZERO);
                        skuEntity.setUnloadingTime(BigDecimal.ZERO);
                        skuEntity.setReceiveShip(labelEntity.getReceiveShip());
                        skuEntity.setOrgId(handoverOrder.getOrgId());
                        skuEntity.setBaseOrgId(handoverOrder.getBaseOrgId());
                    }
                    saveSkuMap.put(checkKey, skuEntity);
                }
            }

            List<TmHandoverOrderSku> saveSkuList = Lists.newArrayList(saveSkuMap.values());

            Map<String, List<TmDispatchOrderLabel>> rsLabelMap = labelList.stream().filter(t -> StringUtils.isBlank(t.getSkuCode())).collect(Collectors.groupingBy(TmDispatchOrderLabel::getReceiveShip));
            for (Map.Entry<String, List<TmDispatchOrderLabel>> rsLabel : rsLabelMap.entrySet()) {
                String rsFlag = rsLabel.getKey();
                List<String> transportNos = rsLabel.getValue().stream().map(TmDispatchOrderLabel::getTransportNo).distinct().collect(Collectors.toList());
                for (String transportNo : transportNos) {
                    TmTransportOrderHeader transportOrder = tmTransportOrderHeaderService.getByNo(transportNo);
                    List<TmTransportOrderSku> transportOrderSkuList = tmTransportOrderSkuService.findList(new TmTransportOrderSku(transportNo, transportOrder.getOrgId()));
                    for (TmTransportOrderSku transportOrderSku : transportOrderSkuList) {
                        TmHandoverOrderSku skuEntity = new TmHandoverOrderSku();
                        skuEntity.setHandoverNo(handoverOrder.getHandoverNo());
                        skuEntity.setTransportNo(transportNo);
                        skuEntity.setCustomerNo(transportOrder.getCustomerNo());
                        skuEntity.setOwnerCode(transportOrderSku.getOwnerCode());
                        skuEntity.setSkuCode(transportOrderSku.getSkuCode());
                        skuEntity.setOrderQty(BigDecimal.valueOf(transportOrderSku.getQty()));
                        skuEntity.setActualQty(BigDecimal.ZERO);
                        skuEntity.setUnloadingTime(BigDecimal.ZERO);
                        skuEntity.setReceiveShip(rsFlag);
                        skuEntity.setOrgId(handoverOrder.getOrgId());
                        skuEntity.setBaseOrgId(handoverOrder.getBaseOrgId());
                        saveSkuList.add(skuEntity);
                    }
                }
            }
            saveOrder(handoverOrder);
            batchSaveLabel(saveLabelList);
            batchSaveSku(saveSkuList, UserUtils.getUser());
        }
    }

    /**
     * 根据派车单删除交接清单
     */
    @Transactional
    public void removeByDispatch(String dispatchNo, String orgId) {
        TmHandoverOrderEntity condition = new TmHandoverOrderEntity();
        condition.setDispatchNo(dispatchNo);
        condition.setOrgId(orgId);
        List<TmHandoverOrderEntity> entityList = mapper.findEntityList(condition);
        for (TmHandoverOrderEntity entity : entityList) {
            removeOrder(entity);
            // 删除授权数据
            tmAuthorityManager.remove(TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue(), entity.getId());
        }
    }

    /**
     * 保存前校验
     */
    public void saveOrderValidator(TmHandoverOrderHeader order) {
        TmHandoverOrderHeader checkOrder = getHandoverByDispatchAndOutlet(order.getDispatchNo(), order.getDeliveryOutletCode(), order.getOrgId());
        if (checkOrder != null) {
            if (!TmsConstants.HANDOVER_ORDER_STATUS_00.equals(checkOrder.getStatus())) {
                throw new TmsException("派车单号[" + order.getDispatchNo() + "], 配送网点[" + order.getDeliveryOutletCode() + "]已生成交接单，且交接单状态不为新建！");
            }
            removeOrder(checkOrder);
        }
    }

    /**
     * 保存交接单
     */
    @Transactional
    public TmHandoverOrderEntity saveOrder(TmHandoverOrderHeader entity) {
        saveOrderValidator(entity);
        if (StringUtils.isBlank(entity.getHandoverNo())) {
            entity.setHandoverNo(noService.getDocumentNo(GenNoType.TM_HANDOVER_NO.name()));
        }

        TmHandoverOrderHeader order = new TmHandoverOrderHeader();
        BeanUtils.copyProperties(entity, order);
        if (StringUtils.isBlank(order.getId())) {
            order.setIsNewRecord(false);
        }
        tmHandoverOrderHeaderService.save(order);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(order);
        return this.getEntity(order.getId());
    }

    /**
     * 校验待删除交接单信息
     */
    public void removeOrderValidator(TmHandoverOrderHeader order) {
        if (!TmsConstants.HANDOVER_ORDER_STATUS_00.equals(order.getStatus())) {
            throw new TmsException("交接单[" + order.getHandoverNo() + "]状态不为新建, 无法删除！");
        }
    }

    /**
     * 批量删除交接单
     */
    @Transactional
    public void batchRemoveOrder(List<String> ids) {
        for (String id : ids) {
            removeOrder(tmHandoverOrderHeaderService.get(id));
        }
    }

    /**
     * 删除交接单
     */
    @Transactional
    public void removeOrder(TmHandoverOrderHeader order) {
        removeOrderValidator(order);
        tmHandoverOrderHeaderService.delete(order);
        mapper.deleteLabelByHandoverNo(order.getHandoverNo(), order.getOrgId());
        mapper.deleteSkuByHandoverNo(order.getHandoverNo(), order.getOrgId());
        tmAttachementDetailService.deleteByOrderNo(order.getHandoverNo(), TmsConstants.IMP_UPLOAD_TYPE_HANDOVER, order.getOrgId());
    }

    /**
     * 批量保存交接单标签信息
     */
    @Transactional
    public void batchSaveLabel(List<TmHandoverOrderLabel> labelList) {
        for (TmHandoverOrderLabel label : labelList) {
            saveLabel(label);
        }
    }

    /**
     * 保存交接单标签信息
     */
    @Transactional
    public void saveLabel(TmHandoverOrderLabel label) {
        tmHandoverOrderLabelService.save(label);
    }

    /**
     * 批量保存交接单商品信息
     */
    @Transactional
    public void batchSaveSku(List<TmHandoverOrderSku> skuList, User user) {
        for (TmHandoverOrderSku sku : skuList) {
            saveSku(sku, user);
        }
    }

    /**
     * 保存交接单商品信息
     */
    @Transactional
    public void saveSku(TmHandoverOrderSku sku, User user) {
        tmHandoverOrderSkuService.saveByUser(sku, user);
    }

    public TmHandoverOrderSkuEntity getHandoverSku(String handoverNo, String transportNo, String ownerCode, String skuCode, String receiveShip, String orgId) {
        TmHandoverOrderSkuEntity skuCon = new TmHandoverOrderSkuEntity();
        skuCon.setHandoverNo(handoverNo);
        skuCon.setTransportNo(transportNo);
        skuCon.setOwnerCode(ownerCode);
        skuCon.setSkuCode(skuCode);
        skuCon.setReceiveShip(receiveShip);
        skuCon.setOrgId(orgId);
        List<TmHandoverOrderSkuEntity> skuList = mapper.findSkuList(skuCon);
        return CollectionUtil.isNotEmpty(skuList) ? skuList.get(0) : null;
    }

    /**
     * 根据派车单号和配送网点，获取交接单
     */
    public TmHandoverOrderHeader getHandoverByDispatchAndOutlet(String dispatchNo, String outletCode, String orgId) {
        TmHandoverOrderHeader handoverCon = new TmHandoverOrderHeader();
        handoverCon.setDispatchNo(dispatchNo);
        handoverCon.setDeliveryOutletCode(outletCode);
        handoverCon.setOrgId(orgId);
        List<TmHandoverOrderHeader> handoverOrderList = tmHandoverOrderHeaderService.findList(handoverCon);
        if (CollectionUtil.isEmpty(handoverOrderList)) {
            return null;
        } else {
            return handoverOrderList.get(0);
        }
    }

    /**
     * 网点交接（根据派车单号和配送网点）
     */
    @Transactional
    public void handoverConfirmByDispatch(String dispatchNo, String outletCode, String nextOutletCode, String handoverPerson, String rsFlag, Long qty, String orgId, User user) {
        TmHandoverOrderHeader handoverOrderHeader = getHandoverByDispatchAndOutlet(dispatchNo, outletCode, orgId);
        if (handoverOrderHeader == null) {
            throw new TmsException("交接单不存在");
        }
        handoverOrderHeader.setActualQty(qty);
        handoverOrderHeader.setHandoverPerson(handoverPerson);
        handoverConfirmByHandover(handoverOrderHeader, rsFlag, nextOutletCode, user);
    }

    /**
     * 网点交接（根据派车单 和 运输订单）
     */
    @Transactional
    public void handoverConfirmByTransportOrder(String dispatchNo, String transportNo, String outletCode, String handoverPerson, String remarks, String rsFlag, String orgId, User user) {
        TmDispatchOrderLabel condition = new TmDispatchOrderLabel();
        condition.setDispatchNo(dispatchNo);
        condition.setTransportNo(transportNo);
        condition.setDispatchSiteOutletCode(outletCode);
        condition.setReceiveShip(rsFlag);
        condition.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
        condition.setOrgId(orgId);
        List<TmDispatchOrderLabel> labelList = tmDispatchOrderLabelService.findList(condition);
        if (CollectionUtil.isNotEmpty(labelList)) {
            for (TmDispatchOrderLabel label : labelList) {
                this.handoverConfirmByDispatchLabel(dispatchNo, outletCode, label.getLabelNo(), handoverPerson, remarks, rsFlag, orgId, user);
            }
        }
    }

    /**
     * 网点交接（根据派车单标签）
     */
    @Transactional
    public void handoverConfirmByDispatchLabel(String dispatchNo, String outletCode, String labelNo, String handoverPerson, String remarks, String rsFlag, String orgId, User user) {
        TmHandoverOrderHeader handoverOrder = this.getHandoverByDispatchAndOutlet(dispatchNo, outletCode, orgId);
        if (handoverOrder == null) {
            throw new TmsException("交接单不存在");
        }
        // 更新交接单标签状态
        TmHandoverOrderLabel handoverOrderLabel = mapper.getLabelByLabelNo(handoverOrder.getHandoverNo(), labelNo, handoverOrder.getOrgId());
        if (handoverOrderLabel != null) {
            handoverOrderLabel.setStatus(TmsConstants.HANDOVER_LABEL_STATUS_10);
            handoverOrderLabel.setRemarks(remarks);
            tmHandoverOrderLabelService.saveByUser(handoverOrderLabel, user);
        }
        // 更新交接单
        handoverOrder.setHandoverPerson(handoverPerson);
        handoverOrder.setRemarks(remarks);
        updateOrderStatusByLabel(handoverOrder, user);
        // 更新派车单标签状态
        TmDispatchOrderLabel labelEntity = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, rsFlag, orgId);
        tmDispatchOrderLabelManager.labelDelivery(labelEntity, user);
        // 更新交接商品明细数量
        TmHandoverOrderSkuEntity handoverSku = getHandoverSku(handoverOrder.getHandoverNo(), labelEntity.getTransportNo(), labelEntity.getOwnerCode(), labelEntity.getSkuCode(), rsFlag, handoverOrder.getOrgId());
        handoverSku.setActualQty(labelEntity.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(labelEntity.getQty()));
        saveSku(handoverSku, user);
        // 自动收/发
        if (TmsConstants.RECEIVE.equals(rsFlag)) {
            // TMS参数：交接时网点自动收、发(Y:是 N:否)
            final String HANDOVER_OUTLET_AUTO_R_S = SysControlParamsUtils.getValue(SysParamConstants.HANDOVER_OUTLET_AUTO_R_S, orgId);
            if (Global.Y.equals(HANDOVER_OUTLET_AUTO_R_S)) {
                TmDispatchOrderLabel shipLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, "S", orgId);
                // 模拟发货(网点)
                tmDeliverManager.deliverByLabel(dispatchNo, labelEntity.getTransportNo(), labelEntity.getLabelNo(), outletCode, shipLabel.getDispatchSiteOutletCode());
            }
            // 保存路由节点信息
            tmTransportOrderTrackService.saveTrackNode(labelEntity.getTransportNo(), labelEntity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SHIP, user);
        } else {
            // TMS参数：交接时网点自动收、发(Y:是 N:否)
            final String HANDOVER_OUTLET_AUTO_R_S = SysControlParamsUtils.getValue(SysParamConstants.HANDOVER_OUTLET_AUTO_R_S, orgId);
            if (Global.Y.equals(HANDOVER_OUTLET_AUTO_R_S)) {
                // 模拟收货(网点)
                tmReceiveManager.receiveByLabel(labelEntity.getTransportNo(), labelEntity.getLabelNo(), outletCode);
                // 保存路由节点信息
                tmTransportOrderTrackService.saveTrackNode(labelEntity.getTransportNo(), labelEntity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_ARRIVE, user);
            }
        }
    }

    /**
     * 网点交接（根据交接单）
     */
    @Transactional
    public void handoverConfirmByHandover(TmHandoverOrderHeader handoverOrder, String nextOutletCode, String rsFlag, User user) {
        // 获取交接单标签
        TmHandoverOrderLabel labelCon = new TmHandoverOrderLabel();
        labelCon.setHandoverNo(handoverOrder.getHandoverNo());
        labelCon.setOrgId(handoverOrder.getOrgId());
        labelCon.setStatus(TmsConstants.HANDOVER_LABEL_STATUS_00);
        List<TmHandoverOrderLabel> handoverLabelList = tmHandoverOrderLabelService.findList(labelCon);
        if (CollectionUtil.isNotEmpty(handoverLabelList)) {
            for (TmHandoverOrderLabel labelEntity : handoverLabelList) {
                handoverConfirmByLabel(handoverOrder, rsFlag, labelEntity.getLabelNo(), user);
            }
        }
    }

    /**
     * 网点交接（根据交接单标签）
     */
    @Transactional
    public void handoverConfirmByLabel(TmHandoverOrderHeader handoverOrder, String rsFlag, String labelNo, User user) {
        // 更新交接单标签状态
        TmHandoverOrderLabel handoverOrderLabel = mapper.getLabelByLabelNo(handoverOrder.getHandoverNo(), labelNo, handoverOrder.getOrgId());
        if (handoverOrderLabel != null) {
            handoverOrderLabel.setStatus(TmsConstants.HANDOVER_LABEL_STATUS_10);
            tmHandoverOrderLabelService.saveByUser(handoverOrderLabel, user);
        }
        // 更新交接单
        updateOrderStatusByLabel(handoverOrder, user);
        // 更新派车单标签状态
        TmDispatchOrderLabel labelEntity = tmDispatchOrderLabelService.getByNoAndLabelAndRS(handoverOrder.getDispatchNo(), labelNo, rsFlag, handoverOrder.getOrgId());
        tmDispatchOrderLabelManager.labelDelivery(labelEntity, user);
        // 更新交接商品明细数量
        TmHandoverOrderSkuEntity handoverSku = getHandoverSku(handoverOrder.getHandoverNo(), labelEntity.getTransportNo(), labelEntity.getOwnerCode(), labelEntity.getSkuCode(), rsFlag, handoverOrder.getOrgId());
        handoverSku.setActualQty(BigDecimal.valueOf(labelEntity.getQty()));
        saveSku(handoverSku, user);
        // 自动收/发
        if (TmsConstants.RECEIVE.equals(rsFlag)) {
            // TMS参数：交接时网点自动收、发(Y:是 N:否)
            final String HANDOVER_OUTLET_AUTO_R_S = SysControlParamsUtils.getValue(SysParamConstants.HANDOVER_OUTLET_AUTO_R_S, handoverOrder.getOrgId());
            if (Global.Y.equals(HANDOVER_OUTLET_AUTO_R_S)) {
                TmDispatchOrderLabel shipLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(handoverOrder.getDispatchNo(), labelNo, "S", handoverOrder.getOrgId());
                // 模拟发货(网点)
                tmDeliverManager.deliverByLabel(handoverOrder.getDispatchNo(), labelEntity.getTransportNo(), labelEntity.getLabelNo(), handoverOrder.getDeliveryOutletCode(), shipLabel.getDispatchSiteOutletCode());
            }
            // 保存路由节点信息
            tmTransportOrderTrackService.saveTrackNode(labelEntity.getTransportNo(), labelEntity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SHIP, user);
        } else {
            // TMS参数：交接时网点自动收、发(Y:是 N:否)
            final String HANDOVER_OUTLET_AUTO_R_S = SysControlParamsUtils.getValue(SysParamConstants.HANDOVER_OUTLET_AUTO_R_S, handoverOrder.getOrgId());
            if (Global.Y.equals(HANDOVER_OUTLET_AUTO_R_S)) {
                // 模拟收货(网点)
                tmReceiveManager.receiveByLabel(labelEntity.getTransportNo(), labelEntity.getLabelNo(), handoverOrder.getDeliveryOutletCode());
                // 保存路由节点信息
                tmTransportOrderTrackService.saveTrackNode(labelEntity.getTransportNo(), labelEntity.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_ARRIVE, user);
            }
        }
    }

    /**
     * 根据标签状态更新交接单头状态
     */
    @Transactional
    public void updateOrderStatusByLabel(TmHandoverOrderHeader handoverOrder, User user) {
        TmHandoverOrderLabel checkLabelCon = new TmHandoverOrderLabel();
        checkLabelCon.setHandoverNo(handoverOrder.getHandoverNo());
        checkLabelCon.setOrgId(handoverOrder.getOrgId());
        checkLabelCon.setStatus(TmsConstants.HANDOVER_LABEL_STATUS_00);
        List<TmHandoverOrderLabel> checkLabelList = tmHandoverOrderLabelService.findList(checkLabelCon);
        if (CollectionUtil.isNotEmpty(checkLabelList)) {
            handoverOrder.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_10);
        } else {
            handoverOrder.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_20);
        }
        tmHandoverOrderHeaderService.saveByUser(handoverOrder, user);
    }

    /**
     * app揽收-按派车单和配送网点
     */
    @Transactional
    public void appReceiveConfirmByDispatch(String dispatchNo, String outletCode, String nextOutletCode, String labelNo, String orgId, String receivePerson, String remarks, User user) {
        TmHandoverOrderHeader handoverOrder = getHandoverByDispatchAndOutlet(dispatchNo, outletCode, orgId);
        if (handoverOrder == null) {
            throw new TmsException("交接单不存在");
        }
        handoverOrder.setHandoverPerson(receivePerson);
        handoverOrder.setRemarks(remarks);
        if (TmsConstants.NULL.equals(labelNo)) {
            appReceiveConfirmByHandover(handoverOrder, nextOutletCode, user);
        } else {
            appReceiveConfirmByLabel(handoverOrder, labelNo, nextOutletCode, user);
        }
    }

    /**
     * app揽收-按运输订单
     */
    @Transactional
    public void appReceiveConfirmByTransportOrder(String dispatchNo, String transportNo, String outletCode, String orgId, String receivePerson, String remarks, User user) {
        TmDispatchOrderLabel condition = new TmDispatchOrderLabel();
        condition.setDispatchNo(dispatchNo);
        condition.setTransportNo(transportNo);
        condition.setDispatchSiteOutletCode(outletCode);
        condition.setReceiveShip(TmsConstants.RECEIVE);
        condition.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
        condition.setOrgId(orgId);
        List<TmDispatchOrderLabel> labelList = tmDispatchOrderLabelService.findList(condition);
        if (CollectionUtil.isNotEmpty(labelList)) {
            for (TmDispatchOrderLabel label : labelList) {
                this.appReceiveConfirmByDispatchLabel(dispatchNo, outletCode, label.getLabelNo(), orgId, receivePerson, remarks, user);
            }
        }
    }

    /**
     * app揽收-按派车单标签
     */
    @Transactional
    public void appReceiveConfirmByDispatchLabel(String dispatchNo, String outletCode, String labelNo, String orgId, String receivePerson, String remarks, User user) {
        // 保存路由节点信息
        TmDispatchOrderLabel labelEntity = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, TmsConstants.RECEIVE, orgId);
        tmTransportOrderTrackService.saveTrackNode(labelEntity.getTransportNo(), labelNo, TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE, user);
        handoverConfirmByDispatchLabel(dispatchNo, outletCode, labelNo, receivePerson, remarks, TmsConstants.RECEIVE, orgId, user);
    }

    /**
     * app揽收-按交接单
     */
    @Transactional
    public void appReceiveConfirmByHandover(TmHandoverOrderHeader handoverOrder, String nextOutletCode, User user) {
        TmHandoverOrderLabel labelCon = new TmHandoverOrderLabel();
        labelCon.setHandoverNo(handoverOrder.getHandoverNo());
        labelCon.setOrgId(handoverOrder.getOrgId());
        labelCon.setReceiveShip(TmsConstants.RECEIVE);
        List<TmHandoverOrderLabel> handoverLabelList = tmHandoverOrderLabelService.findList(labelCon);
        if (CollectionUtil.isNotEmpty(handoverLabelList)) {
            for (TmHandoverOrderLabel labelEntity : handoverLabelList) {
                appReceiveConfirmByLabel(handoverOrder, labelEntity.getLabelNo(), nextOutletCode, user);
            }
        }
    }

    /**
     * app揽收-按标签
     */
    @Transactional
    public void appReceiveConfirmByLabel(TmHandoverOrderHeader handoverOrder, String labelNo, String nextOutletCode, User user) {
        TmHandoverOrderLabel label = mapper.getLabelByLabelNo(handoverOrder.getHandoverNo(), labelNo, handoverOrder.getOrgId());
        handoverConfirmByLabel(handoverOrder, TmsConstants.RECEIVE, labelNo, user);
        // 保存路由节点信息
        tmTransportOrderTrackService.saveTrackNode(label.getTransportNo(), label.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_RECEIVE, user);
    }

    /**
     * app签收-按派车单和配送网点
     */
    @Transactional
    public void appSignConfirmByDispatch(String dispatchNo, String outletCode, String labelNo, String orgId, String signPerson, Long qty, String remarks, User user) {
        TmHandoverOrderHeader handoverOrder = getHandoverByDispatchAndOutlet(dispatchNo, outletCode, orgId);
        if (handoverOrder == null) {
            throw new TmsException("交接单不存在");
        }
        handoverOrder.setActualQty(qty);
        handoverOrder.setHandoverPerson(signPerson);
        if (TmsConstants.NULL.equals(labelNo)) {
            appSignConfirmByHandover(handoverOrder, signPerson, remarks, user);
        } else {
            appSignConfirmByLabel(handoverOrder, labelNo, signPerson, remarks, user);
        }
    }

    /**
     * app签收-按运输订单
     */
    @Transactional
    public void appSignConfirmByTransportOrder(String dispatchNo, String transportNo, String outletCode, String orgId, String signPerson, String remarks, User user) {
        TmDispatchOrderLabel condition = new TmDispatchOrderLabel();
        condition.setDispatchNo(dispatchNo);
        condition.setTransportNo(transportNo);
        condition.setDispatchSiteOutletCode(outletCode);
        condition.setReceiveShip(TmsConstants.SHIP);
        condition.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
        condition.setOrgId(orgId);
        List<TmDispatchOrderLabel> labelList = tmDispatchOrderLabelService.findList(condition);
        if (CollectionUtil.isNotEmpty(labelList)) {
            for (TmDispatchOrderLabel label : labelList) {
                this.appSignConfirmByDispatchLabel(dispatchNo, outletCode, label.getLabelNo(), orgId, signPerson, remarks, user);
            }
        }
    }

    /**
     * app签收-按派车单标签
     */
    @Transactional
    public void appSignConfirmByDispatchLabel(String dispatchNo, String outletCode, String labelNo, String orgId, String signPerson, String remarks, User user) {
        // 网点交接
        handoverConfirmByDispatchLabel(dispatchNo, outletCode, labelNo, signPerson, remarks, TmsConstants.SHIP, orgId, user);
        TmDispatchOrderLabel labelEntity = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, TmsConstants.SHIP, orgId);
        TmTransportSignInfo tmTransportSignInfo = new TmTransportSignInfo();
        tmTransportSignInfo.setTransportNo(labelEntity.getTransportNo());
        tmTransportSignInfo.setLabelNo(labelNo);
        tmTransportSignInfo.setSignBy(signPerson);
        tmTransportSignInfo.setSignTime(new Date());
        tmTransportSignInfo.setRemarks(remarks);
        tmTransportSignInfo.setOrgId(orgId);
        tmSignManager.signLabel(tmTransportSignInfo);
    }

    /**
     * app签收-按交接单
     */
    @Transactional
    public void appSignConfirmByHandover(TmHandoverOrderHeader handoverOrder, String signPerson, String remarks, User user) {
        TmHandoverOrderLabel labelCon = new TmHandoverOrderLabel();
        labelCon.setHandoverNo(handoverOrder.getHandoverNo());
        labelCon.setOrgId(handoverOrder.getOrgId());
        List<TmHandoverOrderLabel> handoverLabelList = tmHandoverOrderLabelService.findList(labelCon);
        if (CollectionUtil.isNotEmpty(handoverLabelList)) {
            for (TmHandoverOrderLabel labelEntity : handoverLabelList) {
                appSignConfirmByLabel(handoverOrder, labelEntity.getLabelNo(), signPerson, remarks, user);
            }
        }
    }

    /**
     * app签收-按标签
     */
    @Transactional
    public void appSignConfirmByLabel(TmHandoverOrderHeader handoverOrder, String labelNo, String signPerson, String remarks, User user) {
        TmHandoverOrderLabel label = mapper.getLabelByLabelNo(handoverOrder.getHandoverNo(), labelNo, handoverOrder.getOrgId());
        handoverConfirmByLabel(handoverOrder, TmsConstants.SHIP, labelNo, user);
        TmTransportSignInfo tmTransportSignInfo = new TmTransportSignInfo();
        tmTransportSignInfo.setTransportNo(label.getTransportNo());
        tmTransportSignInfo.setLabelNo(label.getLabelNo());
        tmTransportSignInfo.setSignBy(signPerson);
        tmTransportSignInfo.setSignTime(new Date());
        tmTransportSignInfo.setRemarks(remarks);
        tmTransportSignInfo.setOrgId(handoverOrder.getOrgId());
        tmSignManager.signLabel(tmTransportSignInfo);
    }

    /**
     * 保存APP上传的照片信息
     */
    @Transactional
    public void saveAppImgDetail(String dispatchNo, String outletCode, String orderType, String labelNo, String orgId,
                                 String fileName, String imgFilePath, String imgUrl, User user) {
        TmHandoverOrderHeader handoverOrder = this.getHandoverByDispatchAndOutlet(dispatchNo, outletCode, orgId);
        if (handoverOrder != null) {
            tmAttachementDetailService.saveAppImgDetail(user, handoverOrder.getHandoverNo(), orderType, labelNo, orgId, fileName, imgFilePath, imgUrl);
        }
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String dispatchNo, String orgId, String baseOrgId) {
        TmHandoverOrderHeader qEntity = new TmHandoverOrderHeader();
        qEntity.setDispatchNo(dispatchNo);
        qEntity.setOrgId(orgId);
        qEntity.setBaseOrgId(baseOrgId);
        List<TmHandoverOrderHeader> orderHeaders = this.findList(qEntity);
        for (TmHandoverOrderHeader orderHeader : orderHeaders) {
            tmHandoverOrderHeaderService.delete(orderHeader);
            mapper.deleteLabelByHandoverNo(orderHeader.getHandoverNo(), orderHeader.getOrgId());
            mapper.deleteSkuByHandoverNo(orderHeader.getHandoverNo(), orderHeader.getOrgId());
            tmAttachementDetailService.deleteByOrderNo(orderHeader.getHandoverNo(), TmsConstants.IMP_UPLOAD_TYPE_HANDOVER, orderHeader.getOrgId());
            // 删除授权数据
            tmAuthorityManager.remove(TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue(), orderHeader.getId());
        }
    }

    /**
     * 交接单交接
     *
     * @param tmHandoverOrderHeader 交接单信息
     */
    @Transactional
    public void handover(TmHandoverOrderHeader tmHandoverOrderHeader) {
        List<TmHandoverOrderLabel> labelList = tmHandoverOrderLabelService.findList(new TmHandoverOrderLabel(tmHandoverOrderHeader.getHandoverNo(), tmHandoverOrderHeader.getOrgId()));
        if (CollectionUtil.isEmpty(labelList)) {
            return;
        }
        User user = UserUtils.getUser();
        for (TmHandoverOrderLabel label : labelList) {
            if (!TmsConstants.HANDOVER_LABEL_STATUS_00.equals(label.getStatus())) {
                continue;
            }
            handoverConfirmByLabel(tmHandoverOrderHeader, label.getReceiveShip(), label.getLabelNo(), user);
            tmHandoverOrderHeader = tmHandoverOrderHeaderService.get(tmHandoverOrderHeader.getId());
        }
    }
}
