package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.gps.GpsNewestLocationInfo;
import com.yunyou.modules.interfaces.gps.GpsRunTrackInfo;
import com.yunyou.modules.interfaces.gps.GpsUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.common.map.geo.Point;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.mapper.TmDispatchOrderMapper;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderLabelService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderSiteService;
import com.yunyou.modules.tms.order.service.TmTransportOrderTrackService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：派车单业务处理类(事务管理)
 */
@Service
@Transactional(readOnly = true)
public class TmDispatchOrderManager extends CrudService<TmDispatchOrderMapper, TmDispatchOrderHeader> {
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderSiteService tmDispatchOrderSiteService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmTransportOrderTrackService tmTransportOrderTrackService;
    @Autowired
    private TmDispatchOrderSiteManager tmDispatchOrderSiteManager;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmDeliverManager tmDeliverManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TmLabelLogManager tmLabelLogManager;

    public TmDispatchOrderEntity getEntity(String id) {
        TmDispatchOrderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setTmDispatchOrderSiteList(tmDispatchOrderSiteManager.findList(new TmDispatchOrderSiteEntity(entity.getDispatchNo(), entity.getOrgId())));
        }
        return entity;
    }

    public List<TmDispatchOrderEntity> findEntityList(TmDispatchOrderEntity entity) {
        dataRuleFilter(entity);
        return mapper.findOrderList(entity);
    }

    public List<TmDispatchOrderLabelEntity> findList(TmDispatchOrderLabelEntity qEntity) {
        return mapper.findLabelList(qEntity);
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderEntity> findPage(Page page, TmDispatchOrderEntity qEntity) {
        dataRuleFilter(qEntity);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderLabelEntity> findPage(Page page, TmDispatchOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findLabelPage(qEntity));
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmTransportOrderEntity> findTransportPage(Page page, TmDispatchOrderLabelEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        List<TmTransportOrderEntity> list = mapper.findTransportPage(qEntity);
        for (TmTransportOrderEntity o : list) {
            o.setShipCity(areaService.getFullName(o.getShipCityId()));
            o.setConsigneeCity(areaService.getFullName(o.getConsigneeCityId()));
        }
        page.setList(list);
        return page;
    }

    public Page<TmCarrierFreight> findCarrierFreight(Page<TmCarrierFreight> page, TmCarrierFreight qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findCarrierFreight(qEntity));
        return page;
    }

    @Transactional
    public void saveEntity(TmDispatchOrderEntity entity) {
        if (entity.getDispatchTime() == null) {
            throw new TmsException("派车时间不能为空");
        }
        if (StringUtils.isBlank(entity.getDispatchType())) {
            throw new TmsException("派车单类型不能为空");
        }
        if (StringUtils.isBlank(entity.getCarrierCode())) {
            throw new TmsException("承运商不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(entity.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        if (StringUtils.isBlank(entity.getDispatchNo())) {
            entity.setDispatchNo(noService.getDocumentNo(GenNoType.TM_DISPATCH_NO.name()));
        }
        if (StringUtils.isBlank(entity.getDataSource())) {
            entity.setDataSource(SystemAliases.TMS.getCode());
        }
        if (StringUtils.isBlank(entity.getFeedBackStatus())) {
            entity.setFeedBackStatus(TmsConstants.DISPATCH_FEEDBACK_STATUS_00);
        }
        tmDispatchOrderHeaderService.save(entity);
        if (TmsConstants.DISPATCH_ORDER_STATUS_00.equals(entity.getDispatchStatus())) {
            tmAuthorityManager.genDispatchAuthorityData(entity.getDispatchNo(), TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), entity.getId());
        }
    }

    @Transactional
    public void removeEntity(TmDispatchOrderEntity entity) {
        if (TmsConstants.DS_01.equals(entity.getDataSource())) {
            throw new TmsException(MessageFormat.format("派车单【{0}】来源于调度计划，请至调度计划操作", entity.getDispatchNo()));
        }
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(entity.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单【{0}】不是新建状态，无法操作", entity.getDispatchNo()));
        }
        tmDispatchOrderSiteManager.remove(tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(entity.getDispatchNo(), entity.getOrgId())));
        tmDispatchOrderHeaderService.delete(new TmDispatchOrderHeader(entity.getId()));
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), entity.getId());
    }

    /**
     * 审核
     */
    @Transactional
    public void audit(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]不是新建状态，无法操作");
        }
        List<TmDispatchOrderLabelEntity> orderLabels = this.findList(new TmDispatchOrderLabelEntity(orderHeader.getDispatchNo(), orderHeader.getOrgId()));
        if (CollectionUtil.isEmpty(orderLabels)) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]未配载订单，无法操作");
        }
        // 校验配载标签中所有的提货标签是否存在对应的收货标签
        List<TmDispatchOrderLabel> rOrderLabels = orderLabels.stream().filter(o -> TmsConstants.RECEIVE.equals(o.getReceiveShip())).collect(Collectors.toList());
        Map<String, List<TmDispatchOrderLabel>> map = orderLabels.stream().collect(Collectors.groupingBy(TmDispatchOrderLabel::getLabelNo));
        for (TmDispatchOrderLabel orderLabel : rOrderLabels) {
            if (map.get(orderLabel.getLabelNo()).size() == 1) {
                throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]标签[" + orderLabel.getLabelNo() + "]仅配载提货点未配载送货点，无法操作");
            }
        }
        orderHeader.setDispatchStatus(TmsConstants.TRANSPORT_ORDER_STATUS_10);
        tmDispatchOrderHeaderService.save(orderHeader);

        // 生成交接单
        tmHandoverOrderManager.createByDispatch(orderHeader.getDispatchNo());
    }

    /**
     * 取消审核
     */
    @Transactional
    public void cancelAudit(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (!TmsConstants.DISPATCH_ORDER_STATUS_10.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单[{0}]不是审核状态，无法操作", orderHeader.getDispatchNo()));
        }
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        tmDispatchOrderHeaderService.save(orderHeader);

        // 删除交接单
        tmHandoverOrderManager.removeByDispatch(orderHeader.getDispatchNo(), orderHeader.getOrgId());
    }

    /**
     * 发车
     */
    @Transactional
    public void depart(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (orderHeader == null) {
            throw new TmsException("无效的订单");
        }
        if (!TmsConstants.DISPATCH_ORDER_STATUS_10.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException(MessageFormat.format("派车单[{0}]不是审核状态，无法操作", orderHeader.getDispatchNo()));
        }
        // TMS参数：派车单发车前网点自动发货(Y:是 N:否)
        final String DEPART_OUTLET_AUTO_SHIP = SysControlParamsUtils.getValue(SysParamConstants.DEPART_OUTLET_AUTO_SHIP, orderHeader.getOrgId());

        // 校验派车网点的标签是否已发货
        TmDispatchOrderLabel qEntity = new TmDispatchOrderLabel();
        qEntity.setDispatchNo(orderHeader.getDispatchNo());
        qEntity.setDispatchSiteOutletCode(orderHeader.getDispatchOutletCode());
        qEntity.setReceiveShip(TmsConstants.RECEIVE);
        qEntity.setOrgId(orderHeader.getOrgId());
        List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findList(qEntity);
        for (TmDispatchOrderLabel orderLabel : orderLabels) {
            if (tmDeliverManager.isDelivery(orderLabel.getDispatchNo(), orderLabel.getDispatchSiteOutletCode(), orderLabel.getLabelNo())) {
                continue;
            }
            // 派车单发车前网点自动发货
            if (Global.Y.equals(DEPART_OUTLET_AUTO_SHIP)) {
                // 模拟网点发货：当地网点是派车网点，找到标签“提货/送货”标识为“送货S”的配送网点（下一个送往网点）
                TmDispatchOrderLabel shipLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(orderLabel.getDispatchNo(), orderLabel.getLabelNo(), TmsConstants.SHIP, orderLabel.getOrgId());
                tmDeliverManager.deliverByLabel(orderLabel.getDispatchNo(), orderLabel.getTransportNo(), orderLabel.getLabelNo(), orderHeader.getDispatchOutletCode(), shipLabel.getDispatchSiteOutletCode());
                tmTransportOrderTrackService.saveTrackNode(orderLabel.getTransportNo(), orderLabel.getLabelNo(), TmsConstants.TRANSPORT_TRACK_NODE_SHIP, UserUtils.getUser());
            } else {
                // 网点未发货，则实物尚未装运上车，此时不允许发车
                throw new TmsException(MessageFormat.format("标签[{1}]在网点[{0}]未发货", orderHeader.getDispatchOutletCode(), orderLabel.getLabelNo()));
            }
        }

        // 更新派车单标签状态“已发车”
        List<TmDispatchOrderLabel> dispatchOrderLabelList =
                tmDispatchOrderLabelService.findByDispatchNoAndSiteCode(orderHeader.getDispatchNo(), null, orderHeader.getOrgId());
        for (TmDispatchOrderLabel orderLabel : dispatchOrderLabelList) {
            orderLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_05);
            tmDispatchOrderLabelService.save(orderLabel);
        }

        // 更新派车单状态“已发车”
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_20);
        orderHeader.setDepartureTime(new Date());
        tmDispatchOrderHeaderService.save(orderHeader);

        // 插入已发车标签履历记录
        tmLabelLogManager.saveDepartureLog(orderHeader.getDispatchNo(), orderHeader.getOrgId());
    }

    /**
     * 关闭（app回场）（不校验交接单状态）
     */
    @Transactional
    public void close(String id, User user) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);
        if (orderHeader == null) {
            return;
        }
        orderHeader.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_99);
        orderHeader.setUpdateBy(user);
        tmDispatchOrderHeaderService.save(orderHeader);
    }

    @Transactional
    public void copy(String id) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.get(id);

        TmDispatchOrderEntity entity = new TmDispatchOrderEntity();
        BeanUtils.copyProperties(orderHeader, entity);
        entity.setId("");
        entity.setRecVer(0);
        entity.setDispatchNo(noService.getDocumentNo(GenNoType.TM_DISPATCH_NO.name()));
        entity.setDispatchStatus(TmsConstants.DISPATCH_ORDER_STATUS_00);
        entity.setDepartureTime(null);
        entity.setIsAppInput(Global.N);
        entity.setIsException(Global.N);
        entity.setTotalQty(null);
        entity.setTotalWeight(null);
        entity.setTotalCubic(null);
        entity.setTotalAmount(null);
        this.saveEntity(entity);

        List<TmDispatchOrderSite> orderSiteList = tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(orderHeader.getDispatchNo(), orderHeader.getOrgId()));
        for (TmDispatchOrderSite orderSite : orderSiteList) {
            orderSite.setId("");
            orderSite.setRecVer(0);
            orderSite.setDispatchNo(entity.getDispatchNo());
            tmDispatchOrderSiteManager.save(orderSite);
        }
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchOrderEntity> transportCheckDispatchPage(Page page, TmDispatchOrderEntity qEntity) {
        TmDispatchOrderLabelEntity labelEntity = new TmDispatchOrderLabelEntity();
        labelEntity.setTransportNo(qEntity.getTransportNo());
        labelEntity.setBaseOrgId(qEntity.getBaseOrgId());

        List<TmDispatchOrderLabelEntity> labelEntityList = this.findList(labelEntity);
        if (CollectionUtil.isNotEmpty(labelEntityList)) {
            List<String> dispatchNoList = labelEntityList.stream().map(TmDispatchOrderLabel::getDispatchNo).distinct().collect(Collectors.toList());
            qEntity.setDispatchNoList(dispatchNoList);

            dataRuleFilter(qEntity);
            String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), qEntity.getOrgId());
            qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
            qEntity.setPage(page);
            page.setList(mapper.findPage(qEntity));
            return page;
        } else {
            return page;
        }
    }

    /**
     * 描述：强制删除
     */
    @Transactional
    public void forceRemove(String dispatchNo, String orgId, String baseOrgId) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        if (orderHeader != null) {
            if (TmsConstants.DISPATCH_ORDER_STATUS_99.equals(orderHeader.getDispatchStatus())) {
                throw new TmsException("该调度计划对应的派车单已经关闭，无法撤回");
            }
            // 校验是否全部交接
            TmHandoverOrderHeader qEntity = new TmHandoverOrderHeader();
            qEntity.setDispatchNo(dispatchNo);
            qEntity.setBaseOrgId(baseOrgId);
            qEntity.setOrgId(orgId);
            List<TmHandoverOrderHeader> handoverOrders = tmHandoverOrderManager.findList(qEntity);
            if (handoverOrders.stream().allMatch(o -> TmsConstants.HANDOVER_ORDER_STATUS_20.equals(o.getStatus()))) {
                throw new TmsException("该调度计划对应的派车单已经全部交接，无法撤回");
            }
            // 删除派车单
            tmDispatchOrderHeaderService.delete(new TmDispatchOrderHeader(orderHeader.getId()));
            // 删除授权数据信息
            tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_HEADER.getValue(), orderHeader.getId());
            // 删除关联交接单
            tmHandoverOrderManager.forceRemove(dispatchNo, orgId, baseOrgId);
            // 删除派车标签
            List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findList(new TmDispatchOrderLabel(dispatchNo, orgId));
            for (TmDispatchOrderLabel orderLabel : orderLabels) {
                tmDispatchOrderLabelService.delete(orderLabel);
                // 删除授权数据信息
                tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_LABEL.getValue(), orderLabel.getId());
            }
            // 删除派车配送点
            List<TmDispatchOrderSite> orderSites = tmDispatchOrderSiteService.findList(new TmDispatchOrderSite(dispatchNo, orgId));
            for (TmDispatchOrderSite orderSite : orderSites) {
                tmDispatchOrderSiteService.delete(orderSite);
                // 删除授权数据信息
                tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_SITE.getValue(), orderSite.getId());
            }
        }
    }

    public Map<String, Point[]> findVehicleTracks(String transportNo, String baseOrgId, String orgId) throws Exception {
        Map<String, Point[]> rsMap = Maps.newHashMap();
        if (Global.isDemoMode()) {
            Point[] points = new Point[]{
                    new Point(120.268977, 30.884965),
                    new Point(120.268892, 30.885251),
                    new Point(120.268887, 30.885258),
                    new Point(120.269037, 30.884153),
                    new Point(120.26914, 30.884016),
                    new Point(120.269217, 30.884033),
                    new Point(120.268907, 30.883476),
                    new Point(120.310848, 30.886992),
                    new Point(120.340342, 30.92658),
                    new Point(120.366013, 30.941459),
                    new Point(120.419081, 30.959468),
                    new Point(120.501562, 30.995451),
                    new Point(120.538502, 31.042902),
                    new Point(120.605327, 31.10356),
                    new Point(120.603715, 31.165119),
                    new Point(120.607457, 31.208175),
                    new Point(120.550004, 31.194774),
                    new Point(120.453347, 31.241125),
                    new Point(120.439637, 31.334695),
                    new Point(120.48584, 31.42705),
                    new Point(120.575552, 31.470238),
                    new Point(120.627305, 31.5027),
                    new Point(120.6481, 31.539041),
                    new Point(120.648863, 31.53896),
                    new Point(120.650742, 31.541096),
                    new Point(120.631533, 31.531648),
                    new Point(120.604462, 31.459856),
                    new Point(120.564647, 31.465426),
                    new Point(120.557766, 31.461841),
                    new Point(120.557674, 31.461701),
                    new Point(120.554967, 31.458856),
                    new Point(120.511517, 31.449135),
                    new Point(120.441866, 31.374003),
                    new Point(120.439087, 31.342232),
                    new Point(120.452736, 31.253683),
                    new Point(120.520488, 31.193482),
                    new Point(120.606437, 31.193937),
                    new Point(120.596152, 31.13931),
                    new Point(120.603722, 31.112595),
                    new Point(120.566709, 31.063787),
                    new Point(120.508549, 31.022082),
                    new Point(120.455005, 30.976945),
                    new Point(120.367291, 30.941986),
                    new Point(120.308301, 30.93458),
                    new Point(120.222469, 30.928465),
                    new Point(120.160247, 30.935463),
                    new Point(120.145421, 30.886779),
                    new Point(120.143283, 30.837486),
                    new Point(120.091695, 30.84586),
                    new Point(120.057041, 30.852737),
                    new Point(120.05747, 30.852885),
                    new Point(120.057366, 30.852911),
                    new Point(120.056963, 30.849853),
                    new Point(120.075281, 30.827852),
                    new Point(120.112157, 30.8486),
                    new Point(120.14484, 30.852562),
                    new Point(120.153332, 30.885531),
                    new Point(120.213846, 30.878648),
                    new Point(120.254687, 30.880595),
                    new Point(120.26895, 30.884487)
            };
            rsMap.put("苏EUP325", points);
            return rsMap;
        }

        List<TmDispatchVehicleEntity> entities = mapper.findDispatchVehicles(transportNo, baseOrgId, orgId);
        for (TmDispatchVehicleEntity entity : entities) {
            String manufacturer = entity.getGpsManufacturer();
            String vehicleNo = entity.getVehicleNo();
            Date from = entity.getDispatchTime();
            Date to = TmsConstants.DISPATCH_ORDER_STATUS_99.equals(entity.getDispatchStatus()) ? entity.getLastTravelTime() : new Date();
            List<GpsRunTrackInfo> tracks = GpsUtils.getVehicleTrackInfo(manufacturer, vehicleNo, from, to);
            if (CollectionUtil.isNotEmpty(tracks)) {
                Point[] points = tracks.stream().map(o -> new Point(o.getLon(), o.getLat())).toArray(Point[]::new);
                rsMap.put(vehicleNo, points);
            }
        }
        return rsMap;
    }

    public List<GpsNewestLocationInfo> findVehicleLocation(String transportNo, String baseOrgId, String orgId) throws Exception {
        List<GpsNewestLocationInfo> list = Lists.newArrayList();

        if (Global.isDemoMode()) {
            GpsNewestLocationInfo info = new GpsNewestLocationInfo();
            info.setNo("苏EUP325");
            info.setTime(DateUtils.getDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
            info.setSpeed(20);
            info.setCourse(75);
            info.setLon(120.271218);
            info.setLat(30.884393);
            info.setAddress("湖州市吴兴区织东路28号");
            return Lists.newArrayList(info);
        }

        List<TmDispatchVehicleEntity> entities = mapper.findDispatchVehicles(transportNo, baseOrgId, orgId);
        Map<String, List<TmDispatchVehicleEntity>> map = entities.stream().collect(Collectors.groupingBy(TmDispatchVehicleEntity::getGpsManufacturer));
        for (Map.Entry<String, List<TmDispatchVehicleEntity>> entry : map.entrySet()) {
            String gpsManufacturer = entry.getKey();
            List<String> vehicleNos = entry.getValue().stream().map(TmDispatchVehicleEntity::getVehicleNo).distinct().collect(Collectors.toList());
            List<GpsNewestLocationInfo> infos = GpsUtils.getVehicleNewestLocationInfo(gpsManufacturer, vehicleNos);
            if (CollectionUtil.isNotEmpty(infos)) {
                list.addAll(infos);
            }
        }
        return list;
    }

    public List<TmNoReturnVehicleInfo> findNoReturnVehicle() {
        return mapper.findNoReturnVehicle();
    }

    public List<TmDispatchVehicleEntity> findRunningVehicle(String orgId) {
        return mapper.findRunningVehicle(orgId);
    }
}
