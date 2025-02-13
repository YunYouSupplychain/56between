package com.yunyou.modules.tms.order.manager;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderHeader;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderLabel;
import com.yunyou.modules.tms.order.entity.TmDispatchOrderSite;
import com.yunyou.modules.tms.order.entity.TmTransportOrderRoute;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderSiteEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchSiteSelectLabelEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmDispatchOrderMapper;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderLabelService;
import com.yunyou.modules.tms.order.service.TmDispatchOrderSiteService;
import com.yunyou.modules.tms.order.service.TmTransportOrderRouteService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TmDispatchOrderSiteManager extends BaseService {
    @Autowired
    private TmDispatchOrderMapper mapper;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmDispatchOrderSiteService tmDispatchOrderSiteService;
    @Autowired
    private TmDispatchOrderLabelService tmDispatchOrderLabelService;
    @Autowired
    private TmTransportOrderRouteService tmTransportOrderRouteService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private TmDispatchOrderLabelManager tmDispatchOrderLabelManager;
    @Autowired
    private TmTotalManager tmTotalManager;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;

    public List<TmDispatchOrderSiteEntity> findList(TmDispatchOrderSiteEntity qEntity) {
        return mapper.findSiteList(qEntity);
    }

    @Transactional
    public void save(TmDispatchOrderSite orderSite) {
        if (StringUtils.isBlank(orderSite.getDispatchNo())) {
            throw new TmsException("所属派车单号不能为空");
        }
        if (StringUtils.isBlank(orderSite.getOutletCode())) {
            throw new TmsException("配送对象不能为空");
        }
        if (StringUtils.isBlank(orderSite.getReceiveShip())) {
            throw new TmsException("提货/送货不能为空");
        }
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(orderSite.getDispatchNo());
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]不是新建状态，无法操作");
        }
        if (orderSite.getReceiveShip().contains(TmsConstants.RECEIVE) && orderHeader.getDispatchOutletCode().equals(orderSite.getOutletCode())) {
            throw new TmsException("提货配送对象不能与派车网点相同");
        }
        TmDispatchOrderSite qEntity = new TmDispatchOrderSite();
        qEntity.setDispatchNo(orderSite.getDispatchNo());
        qEntity.setOutletCode(orderSite.getOutletCode());
        qEntity.setOrgId(orderSite.getOrgId());
        List<TmDispatchOrderSite> list = tmDispatchOrderSiteService.findList(qEntity);
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(orderSite.getId()))) {
                throw new TmsException(MessageFormat.format("派车单【{0}】配送对象【{1}】已存在", orderSite.getDispatchNo(), orderSite.getOutletCode()));
            }
        }
        if (StringUtils.isNotBlank(orderSite.getId())) {
            TmDispatchOrderSiteEntity oldSite = tmDispatchOrderSiteService.getEntity(orderSite.getId());
            // 配送网点 或 提货/送货标识发生变化
            if (!oldSite.getOutletCode().equals(orderSite.getOutletCode()) || !oldSite.getReceiveShip().equals(orderSite.getReceiveShip())) {
                this.removeAssociatedLabel(oldSite);
            }
        }
        tmDispatchOrderSiteService.save(orderSite);
        // 生成授权数据
        tmAuthorityManager.genAuthorityData(orderSite);
    }

    @Transactional
    public void remove(TmDispatchOrderSite orderSite) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(orderSite.getDispatchNo());
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + orderHeader.getDispatchNo() + "]不是新建状态，无法操作");
        }
        // 删除关联绑定在该网点的派车标签记录
        this.removeAssociatedLabel(orderSite);
        // 删除配送网点信息
        tmDispatchOrderSiteService.delete(orderSite);
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_DISPATCH_ORDER_SITE.getValue(), orderSite.getId());
    }

    /**
     * 删除关联绑定在该网点的派车标签记录
     */
    @Transactional
    public void removeAssociatedLabel(TmDispatchOrderSite orderSite) {
        List<TmDispatchOrderLabel> orderLabels = tmDispatchOrderLabelService.findByDispatchNoAndSiteCode(orderSite.getDispatchNo(), orderSite.getOutletCode(), orderSite.getOrgId());
        for (TmDispatchOrderLabel orderLabel : orderLabels) {
            tmDispatchOrderLabelManager.remove(orderLabel);
        }
    }

    @Transactional
    public void save(List<TmDispatchOrderSiteEntity> entities) {
        if (CollectionUtil.isEmpty(entities)) {return;}
        for (TmDispatchOrderSiteEntity entity : entities) {
            if (entity.getId() == null) {continue;}
            this.save(entity);
        }
    }

    @Transactional
    public void remove(List<TmDispatchOrderSite> entities) {
        for (TmDispatchOrderSite entity : entities) {
            if (entity.getId() == null) {continue;}
            this.remove(entity);
        }
    }

    public List<TmDispatchSiteSelectLabelEntity> selectLabelForLeft(TmDispatchSiteSelectLabelEntity qEntity) {
        List<TmDispatchSiteSelectLabelEntity> rsList = Lists.newArrayList();
        // TMS参数：网络站点配送模式(Y:是 N:否)
        final String SITE_DISPATCH_MODE = SysControlParamsUtils.getValue(SysParamConstants.SITE_DISPATCH_MODE, qEntity.getOrgId());
        if (!Global.Y.equals(SITE_DISPATCH_MODE)) {// 非配送模式中，如果配送点是收货方，则只筛选该收货方的标签
            TmTransportObjEntity entity = tmTransportObjService.getEntity(qEntity.getDispatchSiteOutletCode(), qEntity.getOrgId());
            if (entity != null && "CONSIGNEE".equals(entity.getTransportObjType())) {
                qEntity.setConsigneeCode(entity.getTransportObjCode());
            }
        }
        if ("page1".equals(qEntity.getDataType())) {// 页签1
            if (TmsConstants.RECEIVE.equals(qEntity.getReceiveShip())) {// 提货点
                List<TmDispatchSiteSelectLabelEntity> entities = mapper.selectLabel(qEntity);
                // 当前配载配送点 = 现在网点
                rsList.addAll(entities.stream().filter(o -> qEntity.getDispatchSiteOutletCode().contains(o.getNowOutletCode())).collect(Collectors.toList()));
            } else if (TmsConstants.SHIP.equals(qEntity.getReceiveShip())) {// 送货点
                List<TmDispatchSiteSelectLabelEntity> entities = mapper.selectLabel(qEntity);
                if (Global.Y.equals(SITE_DISPATCH_MODE)) {
                    // 当前配载配送点之前的提货配送点 = 现在网点 且 当前配载配送点 ！= 现在网点
                    rsList.addAll(entities.stream().filter(o -> qEntity.getRcvOutletCodeList().contains(o.getNowOutletCode()) && !qEntity.getDispatchSiteOutletCode().equals(o.getNowOutletCode())).collect(Collectors.toList()));
                } else {
                    // 当前配载配送点之前的提货配送点 = 现在网点 且 当前配载配送点 ！= 现在网点 且 当前配载配送点 = 下一站网点
                    rsList.addAll(entities.stream().filter(o -> qEntity.getRcvOutletCodeList().contains(o.getNowOutletCode()) && !qEntity.getDispatchSiteOutletCode().equals(o.getNowOutletCode()) && qEntity.getDispatchSiteOutletCode().equals(o.getNextOutletCode())).collect(Collectors.toList()));
                }
            }
        } else if ("page2".equals(qEntity.getDataType())) {// 页签2
            if (TmsConstants.RECEIVE.equals(qEntity.getReceiveShip())) {// 提货点
                List<TmDispatchSiteSelectLabelEntity> entities = mapper.selectLabel(qEntity);
                // 当前配载配送点 ！= 现在网点
                rsList = entities.stream().filter(o -> !qEntity.getDispatchSiteOutletCode().equals(o.getNowOutletCode())).collect(Collectors.toList());
            } else if (TmsConstants.SHIP.equals(qEntity.getReceiveShip())) {// 送货点
                List<TmDispatchSiteSelectLabelEntity> entities = mapper.selectLabel(qEntity);
                if (Global.Y.equals(SITE_DISPATCH_MODE)) {
                    // !(当前配载配送点之前的提货配送点 = 现在网点 且 当前配载配送点 ！= 现在网点)
                    rsList.addAll(entities.stream().filter(o -> !(qEntity.getRcvOutletCodeList().contains(o.getNowOutletCode()) && !qEntity.getDispatchSiteOutletCode().equals(o.getNowOutletCode()))).collect(Collectors.toList()));
                } else {
                    // !(当前配载配送点之前的提货配送点 = 现在网点 且 当前配载配送点 ！= 现在网点 且 当前配载配送点 = 下一站网点)
                    rsList = entities.stream().filter(o -> !(qEntity.getRcvOutletCodeList().contains(o.getNowOutletCode()) && !qEntity.getDispatchSiteOutletCode().equals(o.getNowOutletCode()) && qEntity.getDispatchSiteOutletCode().equals(o.getNextOutletCode()))).collect(Collectors.toList());
                }
            }
        } else if ("page3".equals(qEntity.getDataType())) {// 页签3
            if (TmsConstants.SHIP.equals(qEntity.getReceiveShip())) {// 送货点
                List<TmDispatchSiteSelectLabelEntity> entities = mapper.selectLabelForLeft3(qEntity);
                if (CollectionUtil.isNotEmpty(entities)) {
                    rsList.addAll(entities);
                }
            }
        }
        for (TmDispatchSiteSelectLabelEntity o : rsList) {
            o.setReceiveShip(qEntity.getReceiveShip());
            o.setDispatchNo(qEntity.getDispatchNo());
            o.setDispatchSiteOutletCode(qEntity.getDispatchSiteOutletCode());
            o.setOrgId(qEntity.getOrgId());
            o.setNowCity(areaService.getFullName(o.getNowCityId()));
            o.setNextCity(areaService.getFullName(o.getNextCityId()));
        }
        return rsList;
    }

    public List<TmDispatchSiteSelectLabelEntity> selectLabelForRight(TmDispatchSiteSelectLabelEntity qEntity) {
        List<TmDispatchSiteSelectLabelEntity> list = mapper.selectedLabel(qEntity);
        for (TmDispatchSiteSelectLabelEntity o : list) {
            o.setNowCity(areaService.getFullName(o.getNowCityId()));
            o.setNextCity(areaService.getFullName(o.getNextCityId()));

            if (TmsConstants.RECEIVE.equals(o.getReceiveShip())) {
                TmTransportObjEntity entity = tmTransportObjService.getEntity(o.getDispatchSiteOutletCode(), o.getBaseOrgId());
                o.setNowOutletCode(entity.getTransportObjCode());
                o.setNowOutletName(entity.getTransportObjName());

                TmDispatchOrderLabel orderLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(o.getDispatchNo(), o.getLabelNo(), TmsConstants.SHIP, o.getOrgId());
                if (orderLabel != null) {
                    TmTransportObjEntity next = tmTransportObjService.getEntity(orderLabel.getDispatchSiteOutletCode(), o.getBaseOrgId());
                    o.setNextOutletCode(next.getTransportObjCode());
                    o.setNextOutletName(next.getTransportObjName());
                } else {
                    TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(o.getTransportNo(), o.getLabelNo(), o.getBaseOrgId());
                    if (orderRoute != null && StringUtils.isNotBlank(orderRoute.getNextOutletCode())) {
                        TmTransportObjEntity next = tmTransportObjService.getEntity(orderRoute.getNextOutletCode(), o.getBaseOrgId());
                        o.setNextOutletCode(next.getTransportObjCode());
                        o.setNextOutletName(next.getTransportObjName());
                    }
                }
            } else if (TmsConstants.SHIP.equals(o.getReceiveShip())) {
                TmTransportObjEntity entity = tmTransportObjService.getEntity(o.getDispatchSiteOutletCode(), o.getBaseOrgId());
                o.setNextOutletCode(entity.getTransportObjCode());
                o.setNextOutletName(entity.getTransportObjName());

                TmDispatchOrderLabel orderLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(o.getDispatchNo(), o.getLabelNo(), TmsConstants.RECEIVE, o.getOrgId());
                if (orderLabel != null) {
                    TmTransportObjEntity now = tmTransportObjService.getEntity(orderLabel.getDispatchSiteOutletCode(), o.getBaseOrgId());
                    o.setNowOutletCode(now.getTransportObjCode());
                    o.setNowOutletName(now.getTransportObjName());
                }
            }
        }
        return list;
    }

    /**
     * 描述：派车单配送网点标签绑定
     */
    @Transactional
    public void bindSiteLabel(TmDispatchSiteSelectLabelEntity entity) {
        String dispatchNo = entity.getDispatchNo();
        String transportNo = entity.getTransportNo();
        String customerNo = entity.getCustomerNo();
        String labelNo = entity.getLabelNo();
        String receiveShip = entity.getReceiveShip();
        String ownerCode = entity.getCustomerCode();
        String skuCode = entity.getSkuCode();
        String dispatchSiteOutletCode = entity.getDispatchSiteOutletCode();
        Double qty = entity.getQty();
        Double weight = entity.getWeight();
        Double cubic = entity.getCubic();

        // 校验派车单是否新建
        TmDispatchOrderHeader tmDispatchOrderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        // 校验该标签是否已被其它派车单绑定
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, tmDispatchOrderHeader.getBaseOrgId());
        if (orderRoute == null) {
            throw new TmsException("运输订单[" + transportNo + "]标签号[" + labelNo + "]找不到");
        }
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(tmDispatchOrderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + dispatchNo + "]不是新建状态，无法操作");
        }
        if (!TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo()) && !dispatchNo.equals(orderRoute.getPreAllocDispatchNo())) {
            throw new TmsException("运输订单[" + transportNo + "]标签号[" + labelNo + "]已被其它派车单绑定");
        }
        if (TmsConstants.NULL.equals(orderRoute.getPreAllocDispatchNo())) {
            // 运输网点路由标签tm_transport_order_route绑定派车单号
            orderRoute.setPreAllocDispatchNo(dispatchNo);
            tmTransportOrderRouteService.save(orderRoute);
            // 标签网点路由信息加入配送网点授权信息
            tmAuthorityManager.addOutletAuthorityData(dispatchSiteOutletCode, tmDispatchOrderHeader.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
        }

        // 校验receiveShip标识的标签在派车标签中是否已存在，存在则更新配送网点，不存在则插入
        TmDispatchOrderLabel tmDispatchOrderLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, receiveShip, tmDispatchOrderHeader.getOrgId());
        if (tmDispatchOrderLabel == null) {
            tmDispatchOrderLabel = new TmDispatchOrderLabel();
            tmDispatchOrderLabel.setDispatchNo(dispatchNo);
            tmDispatchOrderLabel.setDispatchSiteOutletCode(dispatchSiteOutletCode);
            tmDispatchOrderLabel.setLabelNo(labelNo);
            tmDispatchOrderLabel.setReceiveShip(receiveShip);
            tmDispatchOrderLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
            tmDispatchOrderLabel.setBaseOrgId(tmDispatchOrderHeader.getBaseOrgId());
            tmDispatchOrderLabel.setOrgId(tmDispatchOrderHeader.getOrgId());
            tmDispatchOrderLabel.setTransportNo(transportNo);
            tmDispatchOrderLabel.setCustomerNo(customerNo);
            tmDispatchOrderLabel.setOwnerCode(ownerCode);
            tmDispatchOrderLabel.setSkuCode(skuCode);
            tmDispatchOrderLabel.setQty(qty);
            tmDispatchOrderLabel.setWeight(weight);
            tmDispatchOrderLabel.setCubic(cubic);
            tmDispatchOrderLabelManager.save(tmDispatchOrderLabel);
        } else {
            tmDispatchOrderLabel.setDispatchSiteOutletCode(dispatchSiteOutletCode);
            tmDispatchOrderLabelManager.save(tmDispatchOrderLabel);
        }

        // 如果绑定送货派车标签，检查其提货派车标签是否存在，不存在则默认从派车网点提货
        if (TmsConstants.SHIP.equals(receiveShip)) {
            TmDispatchOrderLabel receiveLabel = tmDispatchOrderLabelService.getByNoAndLabelAndRS(dispatchNo, labelNo, TmsConstants.RECEIVE, tmDispatchOrderHeader.getOrgId());
            if (receiveLabel == null) {
                receiveLabel = new TmDispatchOrderLabel();
                receiveLabel.setDispatchNo(dispatchNo);
                receiveLabel.setDispatchSiteOutletCode(tmDispatchOrderHeader.getDispatchOutletCode());
                receiveLabel.setLabelNo(labelNo);
                receiveLabel.setReceiveShip(TmsConstants.RECEIVE);
                receiveLabel.setStatus(TmsConstants.DISPATCH_LABEL_STATUS_00);
                receiveLabel.setBaseOrgId(tmDispatchOrderHeader.getBaseOrgId());
                receiveLabel.setOrgId(tmDispatchOrderHeader.getOrgId());
                receiveLabel.setTransportNo(transportNo);
                receiveLabel.setCustomerNo(customerNo);
                receiveLabel.setOwnerCode(ownerCode);
                receiveLabel.setSkuCode(skuCode);
                receiveLabel.setQty(qty);
                receiveLabel.setWeight(weight);
                receiveLabel.setCubic(cubic);
                tmDispatchOrderLabelManager.save(receiveLabel);
                // 标签网点路由信息加入配送网点授权信息
                tmAuthorityManager.addOutletAuthorityData(tmDispatchOrderHeader.getDispatchOutletCode(), tmDispatchOrderHeader.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue(), orderRoute.getId());
            }
        }
        // 统计数量、重量、体积
        tmTotalManager.totalByDispatch(dispatchNo);
    }

    /**
     * 描述：取消派车单配送网点标签绑定
     *
     * @param dispatchNo             派车单号
     * @param dispatchSiteOutletCode 配送网点
     * @param transportNo            运输单号
     * @param labelNo                标签号
     */
    @Transactional
    public void unbindSiteLabel(String dispatchNo, String dispatchSiteOutletCode, String transportNo, String labelNo) {
        TmDispatchOrderHeader orderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        if (!TmsConstants.DISPATCH_ORDER_STATUS_00.equals(orderHeader.getDispatchStatus())) {
            throw new TmsException("派车单[" + dispatchNo + "]非新建状态，无法操作");
        }
        TmTransportOrderRoute orderRoute = tmTransportOrderRouteService.getByTransportNoAndLabelNo(transportNo, labelNo, orderHeader.getBaseOrgId());
        if (!TmsConstants.NULL.equals(orderRoute.getDispatchNo())) {
            throw new TmsException("派车单[" + dispatchNo + "]已发车，无法操作");
        }
        TmDispatchOrderLabel orderLabel = tmDispatchOrderLabelService.getByNoAndOutletAndLabel(dispatchNo, dispatchSiteOutletCode, labelNo, orderHeader.getOrgId());
        if (orderLabel != null) {
            tmDispatchOrderLabelManager.remove(orderLabel);
        }
        // 统计数量、重量、体积
        tmTotalManager.totalByDispatch(dispatchNo);
    }

    public TmDispatchOrderSite getByDispatchNoAndOutletCode(String dispatchNo, String outletCode, String orgId) {
        return tmDispatchOrderSiteService.getByDispatchNoAndOutletCode(dispatchNo, outletCode, orgId);
    }
}