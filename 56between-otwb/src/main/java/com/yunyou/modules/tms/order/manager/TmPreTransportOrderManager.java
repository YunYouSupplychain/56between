package com.yunyou.modules.tms.order.manager;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityRule;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderCost;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderDelivery;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderHeader;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderCostEntity;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderDeliveryEntity;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderSkuEntity;
import com.yunyou.modules.tms.order.manager.mapper.TmPreTransportOrderMapper;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderCostService;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderDeliveryService;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderSkuService;
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
public class TmPreTransportOrderManager extends BaseService {
    @Autowired
    private TmPreTransportOrderMapper mapper;
    @Autowired
    private TmPreTransportOrderHeaderService tmPreTransportOrderHeaderService;
    @Autowired
    private TmPreTransportOrderDeliveryService tmPreTransportOrderDeliveryService;
    @Autowired
    private TmPreTransportOrderSkuService tmPreTransportOrderSkuService;
    @Autowired
    private TmPreTransportOrderCostService tmPreTransportOrderCostService;
    @Autowired
    private TmAuthorityManager tmAuthorityManager;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private AreaService areaService;

    public TmPreTransportOrderEntity getEntity(String id) {
        TmPreTransportOrderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            this.setExtendValue(entity);
        }
        return entity;
    }

    public List<TmPreTransportOrderSkuEntity> findList(TmPreTransportOrderSkuEntity qEntity) {
        return mapper.findSkuList(qEntity);
    }

    @SuppressWarnings("unchecked")
    public Page<TmPreTransportOrderEntity> findPage(Page page, TmPreTransportOrderEntity qEntity) {
        qEntity.setPage(page);
        String authorityScope = TmAuthorityRule.dataRule(TmAuthorityTable.TM_PRE_TRANSPORT_ORDER_HEADER.getValue(), qEntity.getOrgId());
        qEntity.setDataScope(StringUtils.isNotBlank(qEntity.getDataScope()) ? qEntity.getDataScope() + authorityScope : authorityScope);
        List<TmPreTransportOrderEntity> list = mapper.findOrderPage(qEntity);
        list.forEach(this::setExtendValue);
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmPreTransportOrderSkuEntity> findPage(Page page, TmPreTransportOrderSkuEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findSkuPage(qEntity));
        return page;
    }

    /**
     * 描述：设置实体扩展值
     */
    private void setExtendValue(TmPreTransportOrderEntity entity) {
        if (entity == null) {
            return;
        }
        // 揽收网点
        if (StringUtils.isNotBlank(entity.getReceiveOutletCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getReceiveOutletCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setReceiveOutletName(tmTransportObj.getTransportObjName());
            }
        }
        // 委托方
        if (StringUtils.isNotBlank(entity.getPrincipalCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getPrincipalCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setPrincipalName(tmTransportObj.getTransportObjName());
            }
        }
        // 客户
        if (StringUtils.isNotBlank(entity.getCustomerCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getCustomerCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setCustomerName(tmTransportObj.getTransportObjName());
            }
        }
        // 配送网点
        if (StringUtils.isNotBlank(entity.getOutletCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getOutletCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setOutletName(tmTransportObj.getTransportObjName());
            }
        }
        // 发货方
        if (StringUtils.isNotBlank(entity.getShipCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getShipCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setShipName(tmTransportObj.getTransportObjName());
            }
        }
        // 发货城市
        if (StringUtils.isNotBlank(entity.getShipCityId())) {
            entity.setShipCity(areaService.getFullName(entity.getShipCityId()));
        }
        // 收货方
        if (StringUtils.isNotBlank(entity.getConsigneeCode())) {
            TmTransportObj tmTransportObj = tmTransportObjService.getEntity(entity.getConsigneeCode(), entity.getBaseOrgId());
            if (tmTransportObj != null) {
                entity.setConsigneeName(tmTransportObj.getTransportObjName());
            }
        }
        // 目的地城市
        if (StringUtils.isNotBlank(entity.getConsigneeCityId())) {
            entity.setConsigneeCity(areaService.getFullName(entity.getConsigneeCityId()));
        }
        // 配送信息
        entity.setOrderDelivery(tmPreTransportOrderDeliveryService.getEntity(entity.getTransportNo(), entity.getOrgId()));
        // 商品信息
        entity.setTmTransportOrderSkuList(mapper.findSkuList(new TmPreTransportOrderSkuEntity(entity.getTransportNo(), entity.getOrgId())));
        // 费用信息
        entity.setTmTransportOrderCostList(mapper.findCostList(new TmPreTransportOrderCostEntity(entity.getTransportNo(), entity.getOrgId())));
    }

    @Transactional
    public TmPreTransportOrderEntity saveEntity(TmPreTransportOrderEntity entity) {
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
        tmPreTransportOrderHeaderService.save(entity);
        if (entity.getOrderDelivery() != null) {
            TmPreTransportOrderDeliveryEntity orderDelivery = entity.getOrderDelivery();
            orderDelivery.setTransportNo(entity.getTransportNo());
            orderDelivery.setOrgId(entity.getOrgId());
            orderDelivery.setBaseOrgId(entity.getBaseOrgId());
            tmPreTransportOrderDeliveryService.save(orderDelivery);
        }
        // 生成授权数据
        if (TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
            tmAuthorityManager.genPreTransportAuthorityData(entity.getTransportNo(), TmAuthorityTable.TM_PRE_TRANSPORT_ORDER_HEADER.getValue(), entity.getId());
        }
        return getEntity(entity.getId());
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
    public void removeEntity(TmPreTransportOrderEntity entity) {
        if (TmsConstants.DS_01.equals(entity.getDataSource())) {
            throw new TmsException(MessageFormat.format("运输订单【{0}】来源于调度计划，请至调度计划操作", entity.getTransportNo()));
        }
        if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(entity.getOrderStatus())) {
            throw new TmsException(MessageFormat.format("运输订单[{0}]不是新建状态，不能操作", entity.getTransportNo()));
        }
        // 删除授权数据信息
        tmAuthorityManager.remove(TmAuthorityTable.TM_PRE_TRANSPORT_ORDER_HEADER.getValue(), entity.getId());
        // 删除明细信息
        tmPreTransportOrderSkuService.remove(tmPreTransportOrderSkuService.findList(new TmPreTransportOrderSku(entity.getTransportNo(), entity.getOrgId())));
        // 删除配送信息
        List<TmPreTransportOrderDelivery> orderDeliveries = tmPreTransportOrderDeliveryService.findList(new TmPreTransportOrderDelivery(entity.getTransportNo(), entity.getOrgId()));
        for (TmPreTransportOrderDelivery orderDelivery : orderDeliveries) {
            tmPreTransportOrderDeliveryService.delete(orderDelivery);
        }
        // 删除费用信息
        List<TmPreTransportOrderCost> orderCosts = tmPreTransportOrderCostService.findList(new TmPreTransportOrderCost(entity.getTransportNo(), entity.getOrgId()));
        for (TmPreTransportOrderCost orderCost : orderCosts) {
            tmPreTransportOrderCostService.delete(orderCost);
        }
        // 删除主体信息
        tmPreTransportOrderHeaderService.delete(new TmPreTransportOrderHeader(entity.getId()));
    }

    @Transactional
    public void copy(String id) {
        TmPreTransportOrderHeader orderHeader = tmPreTransportOrderHeaderService.get(id);

        TmPreTransportOrderEntity entity = new TmPreTransportOrderEntity();
        BeanUtils.copyProperties(orderHeader, entity);
        entity.setId("");
        entity.setRecVer(0);
        entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);

        TmPreTransportOrderDelivery orderDelivery = tmPreTransportOrderDeliveryService.getByNo(orderHeader.getTransportNo(), orderHeader.getOrgId());
        TmPreTransportOrderDeliveryEntity orderDeliveryEntity = new TmPreTransportOrderDeliveryEntity();
        BeanUtils.copyProperties(orderDelivery, orderDeliveryEntity);
        orderDeliveryEntity.setId("");
        orderDeliveryEntity.setSignBy(null);
        orderDeliveryEntity.setSignStatus(Global.N);
        orderDeliveryEntity.setSignTime(null);
        orderDeliveryEntity.setSignRemarks(null);
        orderDeliveryEntity.setReceiptBy(null);
        orderDeliveryEntity.setReceiptStatus(Global.N);
        orderDeliveryEntity.setReceiptTime(null);
        orderDeliveryEntity.setReceiptRemarks(null);
        entity.setOrderDelivery(orderDeliveryEntity);
        entity = this.saveEntity(entity);

        List<TmPreTransportOrderSku> orderSkuList = tmPreTransportOrderSkuService.findList(new TmPreTransportOrderSku(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        for (TmPreTransportOrderSku orderSku : orderSkuList) {
            orderSku.setId("");
            orderSku.setRecVer(0);
            orderSku.setTransportNo(entity.getTransportNo());
            tmPreTransportOrderSkuService.save(orderSku);
        }
    }

}