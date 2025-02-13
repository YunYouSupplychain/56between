package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.service.TmVehicleService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.mapper.TmDispatchPlanMapper;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 调度计划业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchPlanManager extends BaseService {
    @Autowired
    private TmDispatchPlanMapper mapper;
    @Autowired
    private TmDispatchPlanHeaderService tmDispatchPlanHeaderService;
    @Autowired
    private TmDispatchPlanDetailService tmDispatchPlanDetailService;
    @Autowired
    private TmDispatchPlanConfigService tmDispatchPlanConfigService;
    @Autowired
    private TmDispatchPlanVehicleService tmDispatchPlanVehicleService;
    @Autowired
    private TmDispatchPlanRelationService tmDispatchPlanRelationService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmVehicleService tmVehicleService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmDispatchOrderLabelManager tmDispatchOrderLabelManager;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmDemandPlanManager tmDemandPlanManager;

    @SuppressWarnings("unchecked")
    public Page<TmDispatchPlanEntity> findPage(Page page, TmDispatchPlanEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DISPATCH_PLAN_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @Transactional
    public void saveEntity(TmDispatchPlanEntity entity) {
        if (StringUtils.isBlank(entity.getPlanNo())) {
            entity.setPlanNo(noService.getDocumentNo(GenNoType.TM_DISPATCH_PLAN_NO.name()));
        }
        tmDispatchPlanHeaderService.save(entity);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(entity);
    }

    public TmDispatchPlanEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public TmDispatchPlanConfigEntity getConfigEntity(String id) {
        return mapper.getConfigEntity(id);
    }

    @Transactional
    public void removeEntity(TmDispatchPlanEntity entity) {
        // 删除授权数据
        tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_PLAN_HEADER.getValue(), entity.getId());
        // 删除主体信息
        tmDispatchPlanHeaderService.delete(entity);
        // 删除明细信息
        mapper.deleteDetailByPlanNo(entity.getPlanNo(), entity.getOrgId());
        // 删除配置信息
        mapper.deleteConfigByPlanNo(entity.getPlanNo(), entity.getOrgId());
        // 删除车辆信息
        mapper.deleteVehicleByPlanNo(entity.getPlanNo(), entity.getOrgId());
        // 删除关联关系
        List<String> demandPlanIds = tmDispatchPlanRelationService.findDemandByDispatch(entity.getId());
        for (String demandId : demandPlanIds) {
            TmDemandPlanEntity demandEntity = tmDemandPlanManager.getEntity(demandId);
            if (null != demandEntity) {
                demandEntity.setStatus("00");
                tmDemandPlanManager.saveEntity(demandEntity);
            }
        }
        tmDispatchPlanRelationService.deleteByDispatch(entity.getId());
    }

    @Transactional
    public void removeConfigEntity(TmDispatchPlanConfigEntity entity) {
        tmDispatchPlanConfigService.delete(entity);
    }

    @Transactional
    public void saveDetailEntity(TmDispatchPlanDetailEntity entity) {
        tmDispatchPlanDetailService.save(entity);
    }

    @Transactional
    public void saveVehicleAll(List<TmDispatchPlanVehicle> list) {
        for (TmDispatchPlanVehicle vehicle : list) {
            tmDispatchPlanVehicleService.save(vehicle);
        }
    }

    @Transactional
    public ResultMessage createInitPlan(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        TmDemandPlanEntity planEntity = new TmDemandPlanEntity();
        planEntity.setArrivalTimeFm(entity.getDemandBeginTime());
        planEntity.setArrivalTimeTo(entity.getDemandEndTime());
        planEntity.setOrgId(entity.getOrgId());
        List<TmDemandPlanEntity> headerList = tmDemandPlanManager.findHeaderList(planEntity);
        headerList = headerList.stream().filter(s -> "00".equals(s.getStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(headerList)) {
            msg.setSuccess(false);
            msg.setMessage("未找到符合条件的需求计划数据");
            return msg;
        }
        // 保存计划单头
        TmDispatchPlanEntity saveEntity = new TmDispatchPlanEntity();
        BeanUtils.copyProperties(entity, saveEntity);
        saveEntity.setStatus("00");
        saveEntity.setIsNewRecord(false);
        this.saveEntity(saveEntity);
        Map<String, List<TmDemandPlanEntity>> headerMap = headerList.stream().collect(Collectors.groupingBy(s -> s.getArrivalTime() + s.getOrgId()));
        for (List<TmDemandPlanEntity> header : headerMap.values()) {
            TmDemandPlanEntity headerE = header.get(0);
            List<TmDemandPlanDetailEntity> detailList = ListUtil.newArrayList();
            for (TmDemandPlanEntity t : header) {
                detailList.addAll(tmDemandPlanManager.findDetailByPlanNo(t.getPlanOrderNo(), t.getOrgId()));
            }
            Map<String, List<TmDemandPlanDetailEntity>> skuMap = detailList.stream().collect(Collectors.groupingBy(s -> s.getOwnerCode() + s.getSkuCode()));
            for (List<TmDemandPlanDetailEntity> detail : skuMap.values()) {
                TmDemandPlanDetailEntity detailE = detail.get(0);
                BigDecimal sumPlanQty = detail.stream().map(TmDemandPlanDetailEntity::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                TmDispatchPlanDetailEntity detailEntity = new TmDispatchPlanDetailEntity();
                detailEntity.setPlanNo(entity.getPlanNo());
                detailEntity.setArrivalTime(headerE.getArrivalTime());
                detailEntity.setOrgId(headerE.getOrgId());
                detailEntity.setBaseOrgId(headerE.getBaseOrgId());
                detailEntity.setOwnerCode(detailE.getOwnerCode());
                detailEntity.setSkuCode(detailE.getSkuCode());
                detailEntity.setPlanQty(sumPlanQty);
                detailEntity.setPlanNo(saveEntity.getPlanNo());
                detailEntity.setIsDispatch("N");
                this.saveDetailEntity(detailEntity);
            }
            // 更新需求计划状态
            for (TmDemandPlanEntity t : header) {
                t.setStatus("10");
                tmDemandPlanManager.saveEntity(t);
                tmDispatchPlanRelationService.save(new TmDispatchPlanRelation(saveEntity.getId(), t.getId()));
            }
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public TmDispatchPlanEntity getHeaderByPlanNo(String planNo, String orgId, String baseOrgId) {
        TmDispatchPlanEntity entity = new TmDispatchPlanEntity();
        entity.setPlanNo(planNo);
        entity.setOrgId(orgId);
        entity.setBaseOrgId(baseOrgId);
        List<TmDispatchPlanEntity> list = mapper.findHeaderList(entity);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public TmDispatchPlanDetailEntity getDetailByPlanNoAndSkuAndTime(String planNo, String ownerCode, String skuCode, Date arrivalTime, String orgId) {
        TmDispatchPlanDetailEntity entity = new TmDispatchPlanDetailEntity();
        entity.setPlanNo(planNo);
        entity.setOwnerCode(ownerCode);
        entity.setSkuCode(skuCode);
        entity.setOrgId(orgId);
        entity.setArrivalTime(arrivalTime);
        List<TmDispatchPlanDetailEntity> detailList = mapper.findDetailList(entity);
        return CollectionUtil.isNotEmpty(detailList) ? detailList.get(0) : null;
    }

    public List<TmDispatchPlanDetailEntity> getDetailByPlanNo(String planNo, String orgId, String baseOrgId) {
        TmDispatchPlanDetailEntity entity = new TmDispatchPlanDetailEntity();
        entity.setPlanNo(planNo);
        entity.setOrgId(orgId);
        entity.setBaseOrgId(baseOrgId);
        return mapper.findDetailList(entity);
    }

    public List<TmDispatchPlanDetailEntity> getDetailList(TmDispatchPlanDetailEntity entity) {
        return mapper.findDetailList(entity);
    }

    public List<TmDispatchPlanDetailEntity> getDetailGrid(TmDispatchPlanDetailEntity qEntity) {
        return mapper.findDetailGrid(qEntity);
    }

    public List<TmDispatchPlanConfigEntity> getConfigList(TmDispatchPlanConfigEntity entity) {
        return mapper.findConfigList(entity);
    }

    public List<TmDispatchPlanConfigEntity> getConfigByPlanNo(String planNo, String orgId, String baseOrgId) {
        TmDispatchPlanConfigEntity entity = new TmDispatchPlanConfigEntity();
        entity.setPlanNo(planNo);
        entity.setOrgId(orgId);
        entity.setBaseOrgId(baseOrgId);
        return mapper.findConfigList(entity);
    }

    public List<TmDispatchPlanConfigEntity> getConfigByPlanNoAndCarNoAndTrip(String planNo, String vehicleNo, String trip, String orgId, String baseOrgId) {
        TmDispatchPlanConfigEntity entity = new TmDispatchPlanConfigEntity();
        entity.setPlanNo(planNo);
        entity.setOrgId(orgId);
        entity.setVehicleNo(vehicleNo);
        entity.setTrip(trip);
        entity.setBaseOrgId(baseOrgId);
        return mapper.findConfigList(entity);
    }

    public List<TmDispatchPlanConfigEntity> getConfigByPlanNoAndSku(String planNo, String ownerCode, String skuCode, String orgId, String baseOrgId) {
        TmDispatchPlanConfigEntity entity = new TmDispatchPlanConfigEntity();
        entity.setPlanNo(planNo);
        entity.setOrgId(orgId);
        entity.setDeliveryPoint(ownerCode);
        entity.setSkuCode(skuCode);
        entity.setBaseOrgId(baseOrgId);
        return mapper.findConfigList(entity);
    }

    public TmDispatchPlanConfigEntity getConfigByUniqueKey(String planNo, String ownerCode, String skuCode, String vehicleNo, String trip, String wareRoom, String orgId, String baseOrgId) {
        TmDispatchPlanConfigEntity entity = new TmDispatchPlanConfigEntity();
        entity.setPlanNo(planNo);
        entity.setDeliveryPoint(ownerCode);
        entity.setSkuCode(skuCode);
        entity.setVehicleNo(vehicleNo);
        entity.setTrip(trip);
        entity.setWareRoom(wareRoom);
        entity.setOrgId(orgId);
        entity.setBaseOrgId(baseOrgId);
        List<TmDispatchPlanConfigEntity> list = mapper.findConfigList(entity);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    public List<String> getVehicleByPlanNo(String planNo, String orgId) {
        return mapper.findVehicleByPlanNo(planNo, orgId);
    }

    public List<TmDispatchPlanVehicleEntity> getVehicle(TmDispatchPlanVehicleEntity entity) {
        return mapper.getVehicle(entity);
    }

    public List<TmDispatchPlanVehicleEntity> getVehicleGrid(TmDispatchPlanVehicleEntity qEntity) {
        return mapper.getVehicleGrid(qEntity);
    }

    @Transactional
    public void dispatchAll(List<TmDispatchPlanConfigEntity> list) {
        // 校验提货点和送货点不能相同、司机和押送员不能相同
        for (TmDispatchPlanConfigEntity entity : list) {
            if (entity.getPickUpPoint().equals(entity.getDeliveryPoint())) {
                throw new TmsException("提货点和送货点不能相同");
            }
        }
        // 车牌号缓存
        Map<String, TmVehicle> vehicleMap = MapUtil.newHashMap();
        // 按客户、商品、车牌号、车次、仓室、提货网点合并
        Map<String, List<TmDispatchPlanConfigEntity>> collect = list.stream().collect(Collectors.groupingBy(s -> s.getDeliveryPoint() + s.getSkuCode() + s.getVehicleNo() + s.getTrip() + s.getWareRoom() + s.getPickUpPoint()));
        for (List<TmDispatchPlanConfigEntity> configList : collect.values()) {
            BigDecimal sumQty = configList.stream().map(TmDispatchPlanConfigEntity::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            TmDispatchPlanConfigEntity entity = configList.get(0);
            // 查找已经配置存在的配载信息，是，累加配载量
            TmDispatchPlanConfigEntity oldEntity = this.getConfigByUniqueKey(entity.getPlanNo(), entity.getDeliveryPoint(), entity.getSkuCode(), entity.getVehicleNo(), entity.getTrip(), entity.getWareRoom(), entity.getOrgId(), entity.getBaseOrgId());
            if (null == oldEntity) {
                // 根据车牌号获取默认司机、副驾驶
                TmVehicle tmVehicle = vehicleMap.get(entity.getVehicleNo());
                if (null == tmVehicle) {
                    tmVehicle = tmVehicleService.getByNo(entity.getVehicleNo(), entity.getBaseOrgId());
                    vehicleMap.put(entity.getVehicleNo(), tmVehicle);
                }
                entity.setId(null);
                entity.setIsNewRecord(false);
                entity.setRecVer(0);
                entity.setQty(sumQty);
                if (null != tmVehicle) {
                    entity.setDriver(tmVehicle.getMainDriver());
                    entity.setEscort(tmVehicle.getCopilot());
                }
                this.saveConfigEntity(entity);
                continue;
            }
            if (StringUtils.isNotBlank(oldEntity.getTransportNo()) || StringUtils.isNotBlank(oldEntity.getDispatchNo())) {
                throw new TmsException("追加的配载信息已经审核，无法操作！");
            }
            oldEntity.setQty(oldEntity.getQty().add(sumQty));
            this.saveConfigEntity(oldEntity);
        }
        // 更新调度计划明细的状态
        for (TmDispatchPlanConfigEntity entity : list) {
            TmDispatchPlanDetailEntity detailEntity = this.getDetailByPlanNoAndSkuAndTime(entity.getPlanNo(), entity.getDeliveryPoint(), entity.getSkuCode(), entity.getArrivalTime(), entity.getOrgId());
            if (null == detailEntity || "Y".equals(detailEntity.getIsDispatch())) {
                throw new TmsException("数据已过期");
            }
            detailEntity.setIsDispatch("Y");
            this.tmDispatchPlanDetailService.save(detailEntity);
        }
    }

    @Transactional
    public void saveConfigEntity(TmDispatchPlanConfigEntity entity) {
        tmDispatchPlanConfigService.save(entity);
    }

    @Transactional
    public void saveEditConfig(List<TmDispatchPlanConfigEntity> list) {
        // 剔除已经审核过的数据
        list = list.stream().filter(s -> StringUtils.isBlank(s.getTransportNo()) && StringUtils.isBlank(s.getDispatchNo())).collect(Collectors.toList());
        // 过滤修改的数据
        List<TmDispatchPlanConfigEntity> editList = list.stream().filter(s -> StringUtils.isNotEmpty(s.getId())).collect(Collectors.toList());
        for (TmDispatchPlanConfigEntity editEntity : editList) {
            if (StringUtils.isEmpty(editEntity.getDeliveryPoint()) && StringUtils.isEmpty(editEntity.getSkuCode()) && null == editEntity.getQty() && StringUtils.isEmpty(editEntity.getPickUpPoint())) {
                tmDispatchPlanConfigService.delete(editEntity);
            } else {
                tmDispatchPlanConfigService.save(editEntity);
            }
        }
        // 过滤新增的数据
        List<TmDispatchPlanConfigEntity> addList = list.stream().filter(s -> StringUtils.isEmpty(s.getId())).collect(Collectors.toList());
        for (TmDispatchPlanConfigEntity addEntity : addList) {
            if (StringUtils.isNotEmpty(addEntity.getDeliveryPoint()) && StringUtils.isNotEmpty(addEntity.getSkuCode()) && null != addEntity.getQty() && StringUtils.isNotEmpty(addEntity.getPickUpPoint())) {
                tmDispatchPlanConfigService.save(addEntity);
            }
        }
    }

    @Transactional
    public void audit(List<TmDispatchPlanConfigEntity> list) {
        // 先取消审核
        cancelAudit(list);
        TmDispatchPlanConfigEntity configEntity = list.get(0);
        // 获取单头信息
        TmDispatchPlanEntity header = getHeaderByPlanNo(configEntity.getPlanNo(), configEntity.getOrgId(), configEntity.getBaseOrgId());
        // 派车单
        TmDispatchOrderEntity doOrder = createDoOrder(list.get(0), header);
        tmDispatchOrderManager.saveEntity(doOrder);
        doOrder = tmDispatchOrderManager.getEntity(doOrder.getId());
                // 配送标签
        tmDispatchOrderSiteManager.save(createDoSite(list, doOrder));
        // 运输订单
        for (TmDispatchPlanConfigEntity entity : list) {
            // 创建
            TmTransportOrderEntity toOrder = createToOrder(entity, header);
            tmTransportOrderManager.saveEntity(toOrder);
            toOrder = tmTransportOrderManager.getEntity(toOrder.getId());
            tmTransportOrderSkuService.save(createToOrderSku(entity, toOrder));
            // 审核
            tmTransportOrderManager.audit(toOrder.getId());
            // 揽收
            List<TmTransportOrderLabel> orderLabels = tmTransportOrderManager.findCanReceiveLabel(toOrder.getTransportNo(), toOrder.getOrgId());
            for (TmTransportOrderLabel orderLabel : orderLabels) {
                tmTransportOrderManager.receive(toOrder.getTransportNo(), orderLabel.getLabelNo(), toOrder.getReceiveOutletCode());
            }
            // 派车标签
            orderLabels = tmTransportOrderLabelService.findHasCanReceiveLabel(toOrder.getTransportNo(), toOrder.getOrgId());
            for (TmTransportOrderLabel orderLabel : orderLabels) {
                tmDispatchOrderLabelManager.save(createDoLabel(orderLabel, entity, doOrder.getDispatchNo(), "R"));
                tmDispatchOrderLabelManager.save(createDoLabel(orderLabel, entity, doOrder.getDispatchNo(), "S"));
                // 校验该标签是否已被其它派车单绑定
                TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(toOrder.getTransportNo(), orderLabel.getLabelNo(), orderLabel.getBaseOrgId());
                // 运输网点路由标签tm_transport_order_route绑定派车单号
                orderRoute.setPreAllocDispatchNo(doOrder.getDispatchNo());
                tmTransportOrderRouteService.save(orderRoute);
                // 标签网点路由信息加入配送网点授权信息
                tmAuthorityManager.addOutletAuthorityData(entity.getPickUpPoint(), orderLabel.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
                tmAuthorityManager.addOutletAuthorityData(entity.getDeliveryPoint(), orderLabel.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
            }
            // 反写运输订单号、派车单号
            mapper.execUpdateSql("update tm_dispatch_plan_config set transport_no ='" + toOrder.getTransportNo() + "', dispatch_no ='" + doOrder.getDispatchNo() + "' where id ='" + entity.getId() + "'");
        }
        // 派车单审核
        tmDispatchOrderManager.audit(doOrder.getId());
        // 调度计划状态更新
        updateDispatchPlanStatus(configEntity.getPlanNo(), configEntity.getOrgId(), configEntity.getBaseOrgId(), "10");
    }

    private TmTransportOrderEntity createToOrder(TmDispatchPlanConfigEntity config, TmDispatchPlanEntity header) {
        TmTransportOrderEntity entity = new TmTransportOrderEntity();
        entity.setOrderTime(new Date());
        entity.setOrderType("1");
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);
        entity.setTransportMethod("3");
        entity.setCustomerCode(config.getDeliveryPoint());
        entity.setReceiveOutletCode(config.getPickUpPoint());
        entity.setOutletCode(config.getDeliveryPoint());
        entity.setDispatchPlanNo(header.getPlanNo());
        entity.setOrgId(config.getOrgId());
        entity.setBaseOrgId(config.getBaseOrgId());
        entity.setDataSource(TmsConstants.DS_01);
        return entity;
    }

    private TmTransportOrderSku createToOrderSku(TmDispatchPlanConfigEntity config, TmTransportOrderEntity header) {
        TmTransportOrderSku entity = new TmTransportOrderSku();
        entity.setTransportNo(header.getTransportNo());
        entity.setOwnerCode(config.getDeliveryPoint());
        entity.setSkuCode(config.getSkuCode());
        entity.setQty(config.getQty().doubleValue());
        entity.setOrgId(config.getOrgId());
        entity.setBaseOrgId(config.getBaseOrgId());
        return entity;
    }

    private TmDispatchOrderEntity createDoOrder(TmDispatchPlanConfigEntity config, TmDispatchPlanEntity header) {
        TmDispatchOrderEntity entity = new TmDispatchOrderEntity();
        entity.setDispatchTime(new Date());
        entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        entity.setDispatchType("5");
        entity.setIsException("N");
        entity.setIsAppInput("N");
        entity.setTransportType("3");
        entity.setDispatchOutletCode(header.getDispatchOutletCode());
        entity.setCarrierCode(header.getCarrierCode());
        entity.setCarNo(config.getVehicleNo());
        entity.setDriver(config.getDriver());
        entity.setCopilot(config.getEscort());
        entity.setDispatchPlanNo(config.getPlanNo());
        entity.setOrgId(config.getOrgId());
        entity.setBaseOrgId(config.getBaseOrgId());
        entity.setDataSource(TmsConstants.DS_01);
        entity.setTrip(config.getTrip());
        entity.setRemarks(config.getRemarks());
        return entity;
    }

    private List<TmDispatchOrderSiteEntity> createDoSite(List<TmDispatchPlanConfigEntity> list, TmDispatchOrderEntity doOrder) {
        List<TmDispatchOrderSiteEntity> result = ListUtil.newArrayList();
        // 提货网点
        List<String> pickList = list.stream().map(TmDispatchPlanConfigEntity::getPickUpPoint).distinct().collect(Collectors.toList());
        int i = 1;
        for (String code : pickList) {
            TmDispatchOrderSiteEntity entity = new TmDispatchOrderSiteEntity();
            entity.setDispatchNo(doOrder.getDispatchNo());
            entity.setDispatchSeq(i);
            entity.setOutletCode(code);
            entity.setReceiveShip("R");
            entity.setOrgId(doOrder.getOrgId());
            entity.setBaseOrgId(doOrder.getBaseOrgId());
            entity.setId("");
            result.add(entity);
            i++;
        }
        // 送货网点
        List<String> deliveryList = list.stream().map(TmDispatchPlanConfigEntity::getDeliveryPoint).distinct().collect(Collectors.toList());
        for (String code : deliveryList) {
            TmDispatchOrderSiteEntity entity = new TmDispatchOrderSiteEntity();
            entity.setDispatchNo(doOrder.getDispatchNo());
            entity.setDispatchSeq(i);
            entity.setOutletCode(code);
            entity.setReceiveShip("S");
            entity.setOrgId(doOrder.getOrgId());
            entity.setBaseOrgId(doOrder.getBaseOrgId());
            entity.setId("");
            result.add(entity);
            i++;
        }

        return result;
    }

    private TmDispatchOrderLabel createDoLabel(TmTransportOrderLabel orderLabel, TmDispatchPlanConfigEntity entity, String doNo, String type) {
        TmDispatchOrderLabel label = new TmDispatchOrderLabel();
        label.setLabelNo(orderLabel.getLabelNo());
        label.setDispatchNo(doNo);
        label.setTransportNo(orderLabel.getTransportNo());
        label.setReceiveShip(type);
        label.setDispatchSiteOutletCode("R".equals(type) ? entity.getPickUpPoint() : entity.getDeliveryPoint());
        label.setOwnerCode(entity.getDeliveryPoint());
        label.setSkuCode(entity.getSkuCode());
        label.setQty(entity.getQty().doubleValue());
        label.setCabin(entity.getWareRoom());
        label.setOrgId(orderLabel.getOrgId());
        label.setBaseOrgId(orderLabel.getBaseOrgId());
        label.setStatus("00");
        return label;
    }

    @Transactional
    public void cancelAudit(List<TmDispatchPlanConfigEntity> list) {
        TmDispatchPlanConfigEntity entity = list.get(0);
        // 判断派车单
        TmDispatchOrderHeader doOrder = tmDispatchOrderHeaderService.getByNo(entity.getDispatchNo());
        if (null != doOrder) {
            if ("20".equals(doOrder.getDispatchStatus())) {
                throw new TmsException("该调度计划对应的派车单已经发车，无法取消审核");
            }
            if ("90".equals(doOrder.getDispatchStatus())) {
                throw new TmsException("该调度计划对应的派车单已经取消，无法取消审核");
            }
            if ("99".equals(doOrder.getDispatchStatus())) {
                throw new TmsException("该调度计划对应的派车单已经关闭，无法取消审核");
            }
            if ("10".equals(doOrder.getDispatchStatus())) {
                doOrder.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
                tmDispatchOrderHeaderService.save(doOrder);
                tmHandoverOrderManager.removeByDispatch(doOrder.getDispatchNo(), doOrder.getOrgId());
            }
            // 删除派车单
            TmDispatchOrderEntity doEntity = new TmDispatchOrderEntity();
            BeanUtils.copyProperties(doOrder, doEntity);
            tmDispatchOrderManager.removeEntity(doEntity);
        }
        // 运输订单
        for (TmDispatchPlanConfigEntity configEntity : list) {
            TmTransportOrderHeader toOrder = tmTransportOrderHeaderService.getByNo(configEntity.getTransportNo());
            if (null != toOrder) {
                // 取消揽收
                tmTransportOrderManager.cancelReceive(toOrder.getTransportNo());
                // 取消审核
                tmTransportOrderManager.cancelAudit(toOrder.getId());
                // 删除
                tmTransportOrderManager.removeEntity(tmTransportOrderManager.getEntity(toOrder.getId()));
            }
        }
        // 更新运输订单号、派车单号
        for (TmDispatchPlanConfigEntity configEntity : list) {
            mapper.execUpdateSql("update tm_dispatch_plan_config set transport_no = null, dispatch_no = null where id ='" + configEntity.getId() + "'");
        }

        // 判断调度计划配置是否全部没有派车单号和运输订单号
        List<TmDispatchPlanConfigEntity> byNoList = this.getConfigByPlanNo(entity.getPlanNo(), entity.getOrgId(), entity.getBaseOrgId());
        long hasAudit = byNoList.stream().filter(s -> StringUtils.isNotBlank(s.getTransportNo()) || StringUtils.isNotBlank(s.getDispatchNo())).count();
        if (hasAudit == 0) {
            this.updateDispatchPlanStatus(entity.getPlanNo(), entity.getOrgId(), entity.getBaseOrgId(), "00");
        }
    }

    @Transactional
    public void updateDispatchPlanStatus(String planNo, String orgId, String baseOrgId, String status) {
        TmDispatchPlanEntity entity = this.getHeaderByPlanNo(planNo, orgId, baseOrgId);
        if (null == entity) {
            throw new TmsException("数据已过期");
        }
        entity.setStatus(status);
        this.saveEntity(entity);
    }

    @Transactional
    public ResultMessage addDemandPlan(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage("操作成功");
        if (StringUtils.isEmpty(entity.getDemandPlanNos())) {
            return msg;
        }
        TmDispatchPlanEntity planHeader = this.getHeaderByPlanNo(entity.getPlanNo(), entity.getOrgId(), entity.getBaseOrgId());
        if (null == planHeader) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期");
            return msg;
        }
        List<TmDispatchPlanDetailEntity> details = this.getDetailByPlanNo(entity.getPlanNo(), entity.getOrgId(), entity.getBaseOrgId());
        Map<String, TmDispatchPlanDetailEntity> detailMap = details.stream().collect(Collectors.toMap(s -> (s.getOwnerCode() + s.getSkuCode() + s.getArrivalTime()), Function.identity()));
        List<TmDemandPlanEntity> headerList = ListUtil.newArrayList();
        for (String demandNo : entity.getDemandPlanNos().split(",")) {
            TmDemandPlanEntity planEntity = new TmDemandPlanEntity();
            planEntity.setPlanOrderNo(demandNo);
            planEntity.setOrgId(entity.getOrgId());
            planEntity.setStatus("00");
            headerList.addAll(tmDemandPlanManager.findHeaderList(planEntity));
        }
        Map<String, List<TmDemandPlanEntity>> headerMap = headerList.stream().collect(Collectors.groupingBy(s -> s.getArrivalTime() + s.getOrgId()));
        for (List<TmDemandPlanEntity> header : headerMap.values()) {
            TmDemandPlanEntity headerE = header.get(0);
            List<TmDemandPlanDetailEntity> detailList = ListUtil.newArrayList();
            for (TmDemandPlanEntity t : header) {
                detailList.addAll(tmDemandPlanManager.findDetailByPlanNo(t.getPlanOrderNo(), t.getOrgId()));
            }
            Map<String, List<TmDemandPlanDetailEntity>> skuMap = detailList.stream().collect(Collectors.groupingBy(s -> s.getOwnerCode() + s.getSkuCode()));
            for (List<TmDemandPlanDetailEntity> detail : skuMap.values()) {
                TmDemandPlanDetailEntity detailE = detail.get(0);
                BigDecimal sumPlanQty = detail.stream().map(TmDemandPlanDetailEntity::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 判断是否存在和当前商品、到达时间相同的记录，是，合并
                TmDispatchPlanDetailEntity oldDetailEntity = detailMap.get(detailE.getOwnerCode() + detailE.getSkuCode() + headerE.getArrivalTime());
                if (null != oldDetailEntity) {
                    oldDetailEntity.setPlanQty(oldDetailEntity.getPlanQty().add(sumPlanQty));
                    this.saveDetailEntity(oldDetailEntity);
                    continue;
                }
                TmDispatchPlanDetailEntity detailEntity = new TmDispatchPlanDetailEntity();
                detailEntity.setPlanNo(entity.getPlanNo());
                detailEntity.setArrivalTime(headerE.getArrivalTime());
                detailEntity.setOrgId(headerE.getOrgId());
                detailEntity.setBaseOrgId(headerE.getBaseOrgId());
                detailEntity.setOwnerCode(detailE.getOwnerCode());
                detailEntity.setSkuCode(detailE.getSkuCode());
                detailEntity.setPlanQty(sumPlanQty);
                detailEntity.setPlanNo(entity.getPlanNo());
                detailEntity.setIsDispatch("N");
                this.saveDetailEntity(detailEntity);
            }
            // 更新需求计划状态
            for (TmDemandPlanEntity t : header) {
                t.setStatus("10");
                tmDemandPlanManager.saveEntity(t);
                tmDispatchPlanRelationService.save(new TmDispatchPlanRelation(planHeader.getId(), t.getId()));
            }
        }

        msg.setMessage("操作成功");
        return msg;
    }

    @Transactional
    public ResultMessage addVehicle(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        List<String> list = this.getVehicleByPlanNo(entity.getPlanNo(), entity.getOrgId());
        if (StringUtils.isNotBlank(entity.getCarNos())) {
            List<TmDispatchPlanVehicle> vehicleSaveList = ListUtil.newArrayList();
            for (String carNo : entity.getCarNos().split(",")) {
                if (!list.contains(carNo)) {
                    TmDispatchPlanVehicle vehicle = new TmDispatchPlanVehicle();
                    vehicle.setPlanNo(entity.getPlanNo());
                    vehicle.setVehicleNo(carNo);
                    vehicle.setOrgId(entity.getOrgId());
                    vehicle.setBaseOrgId(entity.getBaseOrgId());
                    vehicleSaveList.add(vehicle);
                }
            }
            if (CollectionUtil.isNotEmpty(vehicleSaveList)) {
                this.saveVehicleAll(vehicleSaveList);
            }
        }

        return msg;
    }

    @Transactional
    public void recall(String planNo, String vehicleNo, String trip, String orgId, String baseOrgId) {
        // 先查找车次配置信息
        List<TmDispatchPlanConfigEntity> list = this.getConfigByPlanNoAndCarNoAndTrip(planNo, vehicleNo, trip, orgId, baseOrgId);
        if (CollectionUtil.isNotEmpty(list)) {
            for (TmDispatchPlanConfigEntity o : list) {
                tmDispatchOrderManager.forceRemove(o.getDispatchNo(), orgId, baseOrgId);
                tmTransportOrderManager.forceRemove(o.getTransportNo(), orgId, baseOrgId);
                // 更新运输订单号、派车单号
                mapper.execUpdateSql("update tm_dispatch_plan_config set transport_no = null, dispatch_no = null where id ='" + o.getId() + "'");
            }
            // 判断调度计划配置是否全部没有派车单号和运输订单号
            List<TmDispatchPlanConfigEntity> byNoList = this.getConfigByPlanNo(planNo, orgId, baseOrgId);
            long hasAudit = byNoList.stream().filter(s -> StringUtils.isNotBlank(s.getTransportNo()) || StringUtils.isNotBlank(s.getDispatchNo())).count();
            if (hasAudit == 0) {
                this.updateDispatchPlanStatus(planNo, orgId, baseOrgId, "00");
            }
        }
    }
}