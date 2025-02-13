package com.yunyou.modules.tms.order.manager;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.authority.TmAuthorityTable;
import com.yunyou.modules.tms.authority.entity.TmAuthorityData;
import com.yunyou.modules.tms.authority.service.TmAuthorityDataService;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static com.yunyou.modules.sys.OfficeType.*;

/**
 * 运输数据认证服务
 *
 * @author liujianhua
 * @version 2022.8.3
 */
@Service
@Transactional(readOnly = true)
public class TmAuthorityManager extends BaseService {
    @Autowired
    private TmAuthorityDataService tmAuthorityDataService;
    @Autowired
    private TmPreTransportOrderHeaderService tmPreTransportOrderHeaderService;
    @Autowired
    private TmPreTransportOrderDeliveryService tmPreTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderDeliveryService tmTransportOrderDeliveryService;
    @Autowired
    private TmTransportOrderLabelService tmTransportOrderLabelService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private OfficeService officeService;

    @Transactional
    public void addAuthorityData(TmAuthorityData tmAuthorityData) {
        tmAuthorityDataService.validator(tmAuthorityData);

        // 检查数据是否已存在，存在则不再插入
        List<TmAuthorityData> list = tmAuthorityDataService.findList(tmAuthorityData);
        if (CollectionUtil.isNotEmpty(list)) {
            if (logger.isDebugEnabled()) {
                logger.debug(MessageFormat.format("数据已存在 {0}", JSON.toJSONString(tmAuthorityData)));
            }
            return;
        }
        tmAuthorityDataService.insert(tmAuthorityData);
    }

    @Transactional
    public void addOutletAuthorityData(String outletCode, String baseOrgId, String tableName, String businessId) {
        if (StringUtils.isBlank(outletCode) || StringUtils.isBlank(baseOrgId)) {
            return;
        }
        TmTransportObj entity = tmTransportObjService.getByCode(outletCode, baseOrgId);
        if (entity != null) {
            this.addAuthorityData(new TmAuthorityData(OUTLET, tableName, businessId, entity.getId()));
        }
    }

    @Transactional
    public void addCarrierAuthorityData(String carrierCode, String baseOrgId, String tableName, String businessId) {
        if (StringUtils.isBlank(carrierCode) || StringUtils.isBlank(baseOrgId)) {
            return;
        }
        TmTransportObj entity = tmTransportObjService.getByCode(carrierCode, baseOrgId);
        if (entity != null) {
            this.addAuthorityData(new TmAuthorityData(CARRIER, tableName, businessId, entity.getId()));
        }
    }

    @Transactional
    public void genPreTransportAuthorityData(String preTransportNo, String tableName, String businessId) {
        TmPreTransportOrderHeader orderHeader = tmPreTransportOrderHeaderService.getByNo(preTransportNo);
        String opOrgId = orderHeader.getOrgId();
        String baseOrgId = orderHeader.getBaseOrgId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getReceiveOutletCode(), baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getOutletCode(), baseOrgId, tableName, businessId);
        TmPreTransportOrderDelivery orderDelivery = tmPreTransportOrderDeliveryService.getByNo(orderHeader.getTransportNo(), orderHeader.getOrgId());
        if (orderDelivery != null) {
            this.addCarrierAuthorityData(orderDelivery.getCarrierCode(), baseOrgId, tableName, businessId);
        }
    }

    @Transactional
    public void genTransportAuthorityData(String transportNo, String tableName, String businessId) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.getByNo(transportNo);
        String opOrgId = orderHeader.getOrgId();
        String baseOrgId = orderHeader.getBaseOrgId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getReceiveOutletCode(), baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getOutletCode(), baseOrgId, tableName, businessId);
        TmTransportOrderDelivery orderDelivery = tmTransportOrderDeliveryService.getByNo(orderHeader.getTransportNo(), orderHeader.getOrgId());
        if (orderDelivery != null) {
            this.addCarrierAuthorityData(orderDelivery.getCarrierCode(), baseOrgId, tableName, businessId);
        }
    }

    @Transactional
    public void genAuthorityData(TmTransportOrderLabel orderLabel) {
        String tableName = TmAuthorityTable.TM_TRANSPORT_ORDER_LABEL.getValue();
        this.genTransportAuthorityData(orderLabel.getTransportNo(), tableName, orderLabel.getId());
    }

    @Transactional
    public void genAuthorityData(TmTransportOrderRoute orderRoute) {
        String tableName = TmAuthorityTable.TM_TRANSPORT_ORDER_ROUTE.getValue();
        this.genTransportAuthorityData(orderRoute.getTransportNo(), tableName, orderRoute.getId());
        this.addOutletAuthorityData(orderRoute.getPreOutletCode(), orderRoute.getBaseOrgId(), tableName, orderRoute.getId());
        this.addOutletAuthorityData(orderRoute.getNowOutletCode(), orderRoute.getBaseOrgId(), tableName, orderRoute.getId());
        this.addOutletAuthorityData(orderRoute.getNextOutletCode(), orderRoute.getBaseOrgId(), tableName, orderRoute.getId());
    }

    @Transactional
    public void genAuthorityData(TmDeliverLabel deliverLabel) {
        String tableName = TmAuthorityTable.TM_DELIVER_LABEL.getValue();
        this.genTransportAuthorityData(deliverLabel.getTransportNo(), tableName, deliverLabel.getId());
        this.addOutletAuthorityData(deliverLabel.getDeliverOutletCode(), deliverLabel.getBaseOrgId(), tableName, deliverLabel.getId());
    }

    @Transactional
    public void genAuthorityData(TmReceiveLabel entity) {
        String tableName = TmAuthorityTable.TM_RECEIVE_LABEL.getValue();
        this.genTransportAuthorityData(entity.getTransportNo(), tableName, entity.getId());
        this.addOutletAuthorityData(entity.getReceiveOutletCode(), entity.getBaseOrgId(), tableName, entity.getId());
    }

    @Transactional
    public void genAuthorityData(TmRepairOrderHeader orderHeader) {
        String opOrgId = orderHeader.getOrgId();
        String baseOrgId = orderHeader.getBaseOrgId();
        String tableName = TmAuthorityTable.TM_REPAIR_ORDER_HEADER.getValue();
        String businessId = orderHeader.getId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
    }

    @Transactional
    public void addTransportAuthorityData(String outletCode, String orderId) {
        TmTransportOrderHeader orderHeader = tmTransportOrderHeaderService.get(orderId);
        this.addOutletAuthorityData(outletCode, orderHeader.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_HEADER.getValue(), orderId);

        List<TmTransportOrderLabel> orderLabels = tmTransportOrderLabelService.findList(new TmTransportOrderLabel(orderHeader.getTransportNo(), orderHeader.getOrgId()));
        for (TmTransportOrderLabel orderLabel : orderLabels) {
            this.addOutletAuthorityData(outletCode, orderHeader.getBaseOrgId(), TmAuthorityTable.TM_TRANSPORT_ORDER_LABEL.getValue(), orderLabel.getId());
        }
    }

    @Transactional
    public void genDispatchAuthorityData(String dispatchNo, String tableName, String businessId) {
        TmDispatchOrderHeader dispatchOrderHeader = tmDispatchOrderHeaderService.getByNo(dispatchNo);
        String opOrgId = dispatchOrderHeader.getOrgId();
        String baseOrgId = dispatchOrderHeader.getBaseOrgId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(dispatchOrderHeader.getDispatchOutletCode(), baseOrgId, tableName, businessId);
        this.addCarrierAuthorityData(dispatchOrderHeader.getCarrierCode(), baseOrgId, tableName, businessId);
    }

    @Transactional
    public void genAuthorityData(TmDispatchOrderSite orderSite) {
        String tableName = TmAuthorityTable.TM_DISPATCH_ORDER_SITE.getValue();
        this.genDispatchAuthorityData(orderSite.getDispatchNo(), tableName, orderSite.getId());
        this.addOutletAuthorityData(orderSite.getOutletCode(), orderSite.getBaseOrgId(), tableName, orderSite.getId());
    }

    @Transactional
    public void genAuthorityData(TmDispatchOrderLabel orderLabel) {
        String tableName = TmAuthorityTable.TM_DISPATCH_ORDER_LABEL.getValue();
        this.genDispatchAuthorityData(orderLabel.getDispatchNo(), tableName, orderLabel.getId());
        this.addOutletAuthorityData(orderLabel.getDispatchSiteOutletCode(), orderLabel.getBaseOrgId(), tableName, orderLabel.getId());
    }

    @Transactional
    public void genAuthorityData(TmDispatchPlanHeader dispatchPlanHeader) {
        String opOrgId = dispatchPlanHeader.getOrgId();
        String baseOrgId = dispatchPlanHeader.getBaseOrgId();
        String tableName = TmAuthorityTable.TM_DISPATCH_PLAN_HEADER.getValue();
        String businessId = dispatchPlanHeader.getId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(dispatchPlanHeader.getDispatchOutletCode(), baseOrgId, tableName, businessId);
        this.addCarrierAuthorityData(dispatchPlanHeader.getCarrierCode(), baseOrgId, tableName, businessId);
    }

    @Transactional
    public void genAuthorityData(TmDemandPlanHeader demandPlanHeader) {
        String opOrgId = demandPlanHeader.getOrgId();
        String baseOrgId = demandPlanHeader.getBaseOrgId();
        String tableName = TmAuthorityTable.TM_DEMAND_PLAN_HEADER.getValue();
        String businessId = demandPlanHeader.getId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
    }

    @Transactional
    public void genAuthorityData(TmHandoverOrderHeader orderHeader) {
        String opOrgId = orderHeader.getOrgId();
        String baseOrgId = orderHeader.getBaseOrgId();
        String tableName = TmAuthorityTable.TM_HANDOVER_ORDER_HEADER.getValue();
        String businessId = orderHeader.getId();

        this.remove(tableName, businessId);
        this.genAuthorityDataByOpOrg(opOrgId, baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getDispatchOutletCode(), baseOrgId, tableName, businessId);
        this.addOutletAuthorityData(orderHeader.getDeliveryOutletCode(), baseOrgId, tableName, businessId);
        this.addCarrierAuthorityData(orderHeader.getCarrierCode(), baseOrgId, tableName, businessId);
    }

    @Transactional
    public void genAuthorityDataByOpOrg(String opOrgId, String baseOrgId, String tableName, String businessId) {
        // 组织中心
        Office organizationCenter = UserUtils.getOrgCenter(opOrgId);
        if (StringUtils.isNotBlank(organizationCenter.getId())) {
            this.addAuthorityData(new TmAuthorityData(ORG_CENTER, tableName, businessId, organizationCenter.getId()));
        }
        // 调度中心
        Office dispatchCenter = UserUtils.getDispatchCenter(opOrgId);
        if (StringUtils.isNotBlank(dispatchCenter.getId())) {
            this.addAuthorityData(new TmAuthorityData(DISPATCH_CENTER, tableName, businessId, dispatchCenter.getId()));
        }
        // 网点、承运商
        Office office = officeService.get(opOrgId);
        if (OUTLET.getValue().equals(office.getType()) || WAREHOUSE.getValue().equals(office.getType())) {
            // 机构类型为网点或仓库的都归于网点
            List<TmTransportObj> outletMatchObjList = tmTransportObjService.findOutletMatchObjByOrgId(opOrgId, baseOrgId);
            for (TmTransportObj transportObj : outletMatchObjList) {
                this.addAuthorityData(new TmAuthorityData(OUTLET, tableName, businessId, transportObj.getId()));
            }
        } else if (CARRIER.getValue().equals(office.getType())) {
            List<TmTransportObj> carrierMatchObjList = tmTransportObjService.findCarrierMatchObjByOrgId(opOrgId, baseOrgId);
            for (TmTransportObj transportObj : carrierMatchObjList) {
                this.addAuthorityData(new TmAuthorityData(CARRIER, tableName, businessId, transportObj.getId()));
            }
        }
    }

    @Transactional
    public void removeOutletAuthorityData(String outletCode, String baseOrgId, String tableName, String businessId) {
        if (StringUtils.isBlank(outletCode) || StringUtils.isBlank(baseOrgId)) {
            return;
        }
        TmTransportObj entity = tmTransportObjService.getByCode(outletCode, baseOrgId);
        if (entity != null) {
            tmAuthorityDataService.delete(new TmAuthorityData(OUTLET, tableName, businessId, entity.getId()));
        }
    }

    @Transactional
    public void remove(String tableName, String businessId) {
        tmAuthorityDataService.delete(new TmAuthorityData(tableName, businessId));
    }
}
