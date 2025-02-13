package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.config.Global;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.number.BigDecimalUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.oms.basic.entity.OmBusinessOrderTypeRelation;
import com.yunyou.modules.oms.basic.service.OmBusinessOrderTypeRelationService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmTaskDetail;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.SysParamConstants;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.tms.basic.entity.TmItem;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.service.TmItemService;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.entity.TmPreTransportOrderSku;
import com.yunyou.modules.tms.order.entity.TmTransportOrderSku;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderDeliveryEntity;
import com.yunyou.modules.tms.order.entity.extend.TmPreTransportOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderDeliveryEntity;
import com.yunyou.modules.tms.order.entity.extend.TmTransportOrderEntity;
import com.yunyou.modules.tms.order.manager.TmPreTransportOrderManager;
import com.yunyou.modules.tms.order.manager.TmTransportOrderManager;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmPreTransportOrderSkuService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderSkuService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PushTmsService extends BaseService {
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private TmItemService tmItemService;
    @Autowired
    private TmPreTransportOrderHeaderService tmPreTransportOrderHeaderService;
    @Autowired
    private TmPreTransportOrderSkuService tmPreTransportOrderSkuService;
    @Autowired
    private TmPreTransportOrderManager tmPreTransportOrderManager;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmTransportOrderManager tmTransportOrderManager;
    @Autowired
    private TmTransportOrderSkuService tmTransportOrderSkuService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private BanQinCdWhPackageService banQinCdWhPackageService;
    @Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;

    private Boolean checkHasTransport(String businessOrderType, String orgId) {
        if (StringUtils.isBlank(businessOrderType) || StringUtils.isBlank(orgId)) {
            return false;
        }
        List<OmBusinessOrderTypeRelation> orderTypeList = omBusinessOrderTypeRelationService.getByBusinessOrderType(businessOrderType, orgId);
        if (CollectionUtil.isNotEmpty(orderTypeList)) {
            return orderTypeList.stream().anyMatch(t -> OmsConstants.OMS_TASK_TYPE_03.equals(t.getOrderType()));
        } else {
            return false;
        }
    }

    private TmTransportObj getCustomer(String customerNo, String type, String orgId) {
        if (StringUtils.isBlank(customerNo) || StringUtils.isBlank(orgId)) {
            return new TmTransportObj();
        }
        TmTransportObj tmTransportObj = tmTransportObjService.getByCode(customerNo, orgId);
        if (tmTransportObj == null || !tmTransportObj.getTransportObjType().contains(type)) {
            throw new GlobalException(SystemAliases.TMS.getDesc() + DictUtils.getDictLabel(type, "TMS_TRANSPORT_OBJ_TYPE", "") + "[" + customerNo + "]不存在");
        }
        return tmTransportObj;
    }

    private TmItem getSku(String ownerCode, String skuCode, String orgId) {
        if (StringUtils.isBlank(ownerCode) || StringUtils.isBlank(skuCode) || StringUtils.isBlank(orgId)) {
            return new TmItem();
        }
        TmItem tmItem = tmItemService.getByOwnerAndSku(ownerCode, skuCode, orgId);
        if (tmItem == null) {
            throw new OmsException(SystemAliases.TMS.getDesc() + "货主[" + ownerCode + "]商品[" + skuCode + "]不存在");
        }
        return tmItem;
    }

    private void setPackQtyByWms(TmTransportOrderSku orderSku, String packCode, String orgId) {
        BanQinCdWhPackageEntity pack = banQinCdWhPackageService.findByPackageCode(packCode, orgId);
        if (pack != null && CollectionUtil.isNotEmpty(pack.getCdWhPackageRelations())) {
            for (BanQinCdWhPackageRelation detail : pack.getCdWhPackageRelations()) {
                if ("EA".equals(detail.getCdprUnit())) {
                    orderSku.setEaQuantity(detail.getCdprQuantity());
                } else if ("IP".equals(detail.getCdprUnit())) {
                    orderSku.setIpQuantity(detail.getCdprQuantity());
                } else if ("CS".equals(detail.getCdprUnit())) {
                    orderSku.setCsQuantity(detail.getCdprQuantity());
                } else if ("PL".equals(detail.getCdprUnit())) {
                    orderSku.setPlQuantity(detail.getCdprQuantity());
                } else if ("Ot".equals(detail.getCdprUnit())) {
                    orderSku.setOtQuantity(detail.getCdprQuantity());
                }
            }
        } else {
            orderSku.setEaQuantity(1d);
            orderSku.setIpQuantity(1d);
            orderSku.setCsQuantity(1d);
            orderSku.setPlQuantity(1d);
            orderSku.setOtQuantity(1d);
        }
    }

    @Transactional
    public void deletePreTransportTask(String sourceNo, String dataSource, String orgId) {
        String getSql = "select concat_ws(',', transport_no, order_status) from tm_pre_transport_order_header where source_no = '" + sourceNo + "' and data_source = '" + dataSource + "' and org_id = '" + orgId + "'";
        List<Object> objects = tmPreTransportOrderHeaderService.executeSelectSql(getSql);
        if (CollectionUtil.isEmpty(objects) || objects.size() <= 0) {
            return;
        }
        for (Object object : objects) {
            String item = (String) object;
            String[] itemList = item.split(",");
            String transportNo = itemList[0];
            String status = itemList[1];
            if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(status)) {
                throw new OmsException(SystemAliases.TMS.getDesc() + "已进行操作，无法删除");
            }
            String deleteSql = "delete from tm_pre_transport_order_header where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            String deleteDeliverySql = "delete from tm_pre_transport_order_delivery where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            String deleteDetailSql = "delete from tm_pre_transport_order_sku where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            tmPreTransportOrderHeaderService.executeDeleteSql(deleteSql);
            tmPreTransportOrderHeaderService.executeDeleteSql(deleteDeliverySql);
            tmPreTransportOrderHeaderService.executeDeleteSql(deleteDetailSql);
        }
    }

    @Transactional
    public void deleteTransportTask(String sourceNo, String dataSource, String orgId) {
        String getSql = "select concat_ws(',', transport_no, order_status) from tm_transport_order_header where source_no = '" + sourceNo + "' and data_source = '" + dataSource + "' and org_id = '" + orgId + "'";
        List<Object> objects = tmTransportOrderHeaderService.executeSelectSql(getSql);
        if (CollectionUtil.isEmpty(objects) || objects.size() <= 0) {
            return;
        }
        for (Object object : objects) {
            String item = (String) object;
            String[] itemList = item.split(",");
            String transportNo = itemList[0];
            String status = itemList[1];
            if (!TmsConstants.TRANSPORT_ORDER_STATUS_00.equals(status)) {
                throw new OmsException(SystemAliases.TMS.getDesc() + "已进行操作，无法删除");
            }
            String deleteSql = "delete from tm_transport_order_header where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            String deleteDeliverySql = "delete from tm_transport_order_delivery where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            String deleteDetailSql = "delete from tm_transport_order_sku where transport_no = '" + transportNo + "' and org_id = '" + orgId + "'";
            tmTransportOrderHeaderService.executeDeleteSql(deleteSql);
            tmTransportOrderHeaderService.executeDeleteSql(deleteDeliverySql);
            tmTransportOrderHeaderService.executeDeleteSql(deleteDetailSql);
        }
    }

    @Transactional
    public void pushTaskToPreTransport(OmTaskHeader omTaskHeader) {
        if (!(OmsConstants.OMS_TASK_STATUS_20.equals(omTaskHeader.getStatus()) || OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus()))) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]不是确认或已下发状态，无法下发");
        }
        this.deletePreTransportTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());

        String orgId = omTaskHeader.getWarehouse();

        TmTransportObj principal = this.getCustomer(omTaskHeader.getPrincipal(), CustomerType.CUSTOMER.getCode(), orgId);
        TmTransportObj customer = this.getCustomer(omTaskHeader.getOwner(), CustomerType.OWNER.getCode(), orgId);
        TmTransportObj consignee = this.getCustomer(StringUtils.isBlank(omTaskHeader.getConsigneeCode()) ? omTaskHeader.getCustomer() : omTaskHeader.getConsigneeCode(), CustomerType.CONSIGNEE.getCode(), orgId);
        TmTransportObj shipper = this.getCustomer(omTaskHeader.getShipperCode(), CustomerType.SHIPPER.getCode(), orgId);

        TmPreTransportOrderEntity entity = new TmPreTransportOrderEntity();

        entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        entity.setOrderTime(omTaskHeader.getOrderDate());
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);
        entity.setOrderType(omTaskHeader.getPushOrderType());
        entity.setTransportMethod(StringUtils.isBlank(omTaskHeader.getTransportMode()) ? TmsConstants.TRANSPORT_METHOD_3 : omTaskHeader.getTransportMode());

        entity.setPrincipalCode(principal.getTransportObjCode());
        entity.setCustomerCode(customer.getTransportObjCode());

        entity.setShipCode(shipper.getTransportObjCode());
        entity.setShipper(StringUtils.isBlank(omTaskHeader.getShipper()) ? shipper.getContact() : omTaskHeader.getShipper());
        entity.setShipperTel(StringUtils.isBlank(omTaskHeader.getShipperTel()) ? shipper.getTel() : omTaskHeader.getShipperTel());
        entity.setShipAddress(StringUtils.isBlank(omTaskHeader.getShipperAddress()) ? shipper.getAddress() : omTaskHeader.getShipperAddress());
        entity.setConsigneeCode(consignee.getTransportObjCode());
        entity.setConsignee(StringUtils.isBlank(omTaskHeader.getConsignee()) ? consignee.getContact() : omTaskHeader.getConsignee());
        entity.setConsigneeTel(StringUtils.isBlank(omTaskHeader.getConsigneeTel()) ? consignee.getTel() : omTaskHeader.getConsigneeTel());
        entity.setConsigneeAddress(StringUtils.isBlank(omTaskHeader.getConsigneeAddress()) ? consignee.getAddress() : omTaskHeader.getConsigneeAddress());

        if (StringUtils.isNotBlank(consignee.getTransportObjType()) && consignee.getTransportObjType().contains(CustomerType.OUTLET.getCode())) {
            entity.setOutletCode(consignee.getTransportObjCode());
        }
        entity.setCustomerNo(omTaskHeader.getCustomerNo());
        entity.setTrackingNo(omTaskHeader.getLogisticsNo());
        entity.setSourceNo(omTaskHeader.getTaskNo());
        entity.setDataSource(SystemAliases.OMS.getCode());
        entity.setBaseOrgId(orgId);
        entity.setOrgId(orgId);

        entity.setDef9(omTaskHeader.getBusinessOrderType());
        entity.setDef10(omTaskHeader.getDef10());

        TmPreTransportOrderDeliveryEntity orderDelivery = new TmPreTransportOrderDeliveryEntity();
        orderDelivery.setCarrierCode(omTaskHeader.getCarrier());
        orderDelivery.setPlanArriveTime(omTaskHeader.getArrivalTime());
        entity.setOrderDelivery(orderDelivery);
        tmPreTransportOrderManager.saveEntity(entity);

        List<OmTaskDetail> details = omTaskHeader.getOmTaskDetailList();
        for (int i = 0; i < details.size(); i++) {
            OmTaskDetail detail = details.get(i);
            TmItem tmItem = this.getSku(omTaskHeader.getOwner(), detail.getSkuCode(), orgId);
            double weight = tmItem.getGrossweight() == null ? 0 : tmItem.getGrossweight();
            double cubic = tmItem.getCubic() == null ? 0 : tmItem.getCubic();

            TmPreTransportOrderSku tmTransportOrderSku = new TmPreTransportOrderSku();
            tmTransportOrderSku.setTransportNo(entity.getTransportNo());
            tmTransportOrderSku.setLineNo(String.format("%04d", i + 1));
            tmTransportOrderSku.setOwnerCode(omTaskHeader.getOwner());
            tmTransportOrderSku.setSkuCode(detail.getSkuCode());
            tmTransportOrderSku.setQty(detail.getQty().doubleValue());
            tmTransportOrderSku.setWeight(BigDecimalUtil.mul(weight, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setCubic(BigDecimalUtil.mul(cubic, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setBaseOrgId(orgId);
            tmTransportOrderSku.setOrgId(orgId);
            tmPreTransportOrderSkuService.save(tmTransportOrderSku);
        }
    }

    @Transactional
    public void pushTaskToTransport(OmTaskHeader omTaskHeader) {
        if (!(OmsConstants.OMS_TASK_STATUS_20.equals(omTaskHeader.getStatus()) || OmsConstants.OMS_TASK_STATUS_40.equals(omTaskHeader.getStatus()))) {
            throw new OmsException("[" + omTaskHeader.getTaskNo() + "]不是确认或已下发状态，无法下发");
        }
        this.deleteTransportTask(omTaskHeader.getTaskNo(), SystemAliases.OMS.getCode(), omTaskHeader.getWarehouse());

        String orgId = omTaskHeader.getWarehouse();

        TmTransportObj principal = this.getCustomer(omTaskHeader.getPrincipal(), CustomerType.CUSTOMER.getCode(), orgId);
        TmTransportObj customer = this.getCustomer(omTaskHeader.getOwner(), CustomerType.OWNER.getCode(), orgId);
        TmTransportObj consignee = this.getCustomer(StringUtils.isBlank(omTaskHeader.getConsigneeCode()) ? omTaskHeader.getCustomer() : omTaskHeader.getConsigneeCode(), CustomerType.CONSIGNEE.getCode(), orgId);
        TmTransportObj shipper = this.getCustomer(omTaskHeader.getShipperCode(), CustomerType.SHIPPER.getCode(), orgId);

        TmTransportOrderEntity entity = new TmTransportOrderEntity();

        entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        entity.setOrderTime(omTaskHeader.getOrderDate());
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);
        entity.setOrderType(omTaskHeader.getPushOrderType());
        entity.setTransportMethod(StringUtils.isBlank(omTaskHeader.getTransportMode()) ? TmsConstants.TRANSPORT_METHOD_3 : omTaskHeader.getTransportMode());

        entity.setPrincipalCode(principal.getTransportObjCode());
        entity.setCustomerCode(customer.getTransportObjCode());

        entity.setShipCode(shipper.getTransportObjCode());
        entity.setShipper(StringUtils.isBlank(omTaskHeader.getShipper()) ? shipper.getContact() : omTaskHeader.getShipper());
        entity.setShipperTel(StringUtils.isBlank(omTaskHeader.getShipperTel()) ? shipper.getTel() : omTaskHeader.getShipperTel());
        entity.setShipAddress(StringUtils.isBlank(omTaskHeader.getShipperAddress()) ? shipper.getAddress() : omTaskHeader.getShipperAddress());
        entity.setConsigneeCode(consignee.getTransportObjCode());
        entity.setConsignee(StringUtils.isBlank(omTaskHeader.getConsignee()) ? consignee.getContact() : omTaskHeader.getConsignee());
        entity.setConsigneeTel(StringUtils.isBlank(omTaskHeader.getConsigneeTel()) ? consignee.getTel() : omTaskHeader.getConsigneeTel());
        entity.setConsigneeAddress(StringUtils.isBlank(omTaskHeader.getConsigneeAddress()) ? consignee.getAddress() : omTaskHeader.getConsigneeAddress());

        if (StringUtils.isNotBlank(consignee.getTransportObjType()) && consignee.getTransportObjType().contains(CustomerType.OUTLET.getCode())) {
            entity.setOutletCode(consignee.getTransportObjCode());
        }

        entity.setCustomerNo(omTaskHeader.getCustomerNo());
        entity.setTrackingNo(omTaskHeader.getLogisticsNo());
        entity.setSourceNo(omTaskHeader.getTaskNo());
        entity.setDataSource(SystemAliases.OMS.getCode());
        entity.setBaseOrgId(orgId);
        entity.setOrgId(orgId);

        entity.setDef9(omTaskHeader.getBusinessOrderType());
        entity.setDef10(omTaskHeader.getDef10());

        TmTransportOrderDeliveryEntity orderDelivery = new TmTransportOrderDeliveryEntity();
        orderDelivery.setCarrierCode(omTaskHeader.getCarrier());
        orderDelivery.setPlanArriveTime(omTaskHeader.getArrivalTime());
        entity.setOrderDelivery(orderDelivery);
        tmTransportOrderManager.saveEntity(entity);

        List<OmTaskDetail> details = omTaskHeader.getOmTaskDetailList();
        for (int i = 0; i < details.size(); i++) {
            OmTaskDetail detail = details.get(i);
            TmItem tmItem = this.getSku(omTaskHeader.getOwner(), detail.getSkuCode(), orgId);
            double weight = tmItem.getGrossweight() == null ? 0 : tmItem.getGrossweight();
            double cubic = tmItem.getCubic() == null ? 0 : tmItem.getCubic();

            TmTransportOrderSku tmTransportOrderSku = new TmTransportOrderSku();
            tmTransportOrderSku.setTransportNo(entity.getTransportNo());
            tmTransportOrderSku.setLineNo(String.format("%04d", i + 1));
            tmTransportOrderSku.setOwnerCode(omTaskHeader.getOwner());
            tmTransportOrderSku.setSkuCode(detail.getSkuCode());
            tmTransportOrderSku.setQty(detail.getQty().doubleValue());
            tmTransportOrderSku.setWeight(BigDecimalUtil.mul(weight, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setCubic(BigDecimalUtil.mul(cubic, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setBaseOrgId(orgId);
            tmTransportOrderSku.setOrgId(orgId);
            tmTransportOrderSkuService.save(tmTransportOrderSku);
        }
        // 系统控制参数：下发时自动审核
        final String PUSH_TO_TMS_AUDIT = SysControlParamsUtils.getValue(SysParamConstants.PUSH_TO_TMS_AUDIT, orgId);
        if (Global.Y.equals(PUSH_TO_TMS_AUDIT)) {
            tmTransportOrderManager.audit(entity.getId());
        }
    }

    @Transactional
    public void pushWmsToTransport(BanQinWmSoEntity wmSoEntity) {
        if (!checkHasTransport(wmSoEntity.getDef13(), wmSoEntity.getOrgId())) {
            throw new GlobalException("此业务订单类型不支持下发运输");
        }
        this.deleteTransportTask(wmSoEntity.getSoNo(), SystemAliases.WMS.getCode(), wmSoEntity.getOrgId());

        String orgId = wmSoEntity.getOrgId();
        Office org = officeService.get(orgId);

        TmTransportObj rOutlet = tmTransportObjService.getByCode(org.getCode(), orgId);
        if (rOutlet == null) {
            rOutlet = new TmTransportObj();
            rOutlet.setTransportObjCode(org.getCode());
            rOutlet.setTransportObjName(org.getName());
            rOutlet.setTransportObjType(CustomerType.OUTLET.getCode() + ",");
            rOutlet.setOrgId(orgId);
            tmTransportObjService.save(rOutlet);
        } else if (!rOutlet.getTransportObjType().contains(CustomerType.OUTLET.getCode())) {
            rOutlet.setTransportObjType(rOutlet.getTransportObjType() + (rOutlet.getTransportObjType().endsWith(",") ? "" : ",") + CustomerType.OUTLET.getCode() + ",");
            tmTransportObjService.save(rOutlet);
        }
        TmTransportObj customer = this.getCustomer(wmSoEntity.getOwnerCode(), CustomerType.OWNER.getCode(), orgId);
        TmTransportObj consignee = this.getCustomer(wmSoEntity.getConsigneeCode(), CustomerType.CONSIGNEE.getCode(), orgId);
        this.getCustomer(wmSoEntity.getCarrierCode(), CustomerType.CARRIER.getCode(), orgId);

        TmTransportOrderEntity entity = new TmTransportOrderEntity();
        TmTransportOrderDeliveryEntity orderDelivery = new TmTransportOrderDeliveryEntity();

        entity.setTransportNo(noService.getDocumentNo(GenNoType.TM_TRANSPORT_NO.name()));
        entity.setOrderTime(wmSoEntity.getOrderTime());
        entity.setOrderStatus(TmsConstants.TRANSPORT_ORDER_STATUS_00);
        entity.setOrderType("1");

        entity.setPrincipalCode(wmSoEntity.getOwnerCode());
        entity.setCustomerCode(customer.getTransportObjCode());
        entity.setConsigneeCode(consignee.getTransportObjCode());
        entity.setConsignee(StringUtils.isBlank(wmSoEntity.getContactName()) ? consignee.getContact() : wmSoEntity.getContactName());
        entity.setConsigneeTel(StringUtils.isBlank(wmSoEntity.getContactTel()) ? consignee.getTel() : wmSoEntity.getContactTel());
        entity.setConsigneeAddress(StringUtils.isBlank(wmSoEntity.getContactAddr()) ? consignee.getAddress() : wmSoEntity.getContactAddr());

        if (StringUtils.isNotBlank(consignee.getTransportObjType()) && consignee.getTransportObjType().contains(CustomerType.OUTLET.getCode())) {
            entity.setOutletCode(consignee.getTransportObjCode());
        }
        entity.setReceiveOutletCode(rOutlet.getTransportObjCode());

        entity.setCustomerNo(wmSoEntity.getDef5());
        entity.setTrackingNo(wmSoEntity.getTrackingNo());
        entity.setSourceNo(wmSoEntity.getSoNo());
        entity.setDataSource(SystemAliases.WMS.getCode());
        entity.setBaseOrgId(orgId);
        entity.setOrgId(orgId);

        entity.setTransportMethod(TmsConstants.TRANSPORT_METHOD_3);
        entity.setOrderSource(wmSoEntity.getDef4());
        entity.setChannel(wmSoEntity.getDef6());
        entity.setServiceMode(wmSoEntity.getDef7());
        entity.setPoNo(wmSoEntity.getDef5());

        entity.setDef1(wmSoEntity.getDef5() + "-" + wmSoEntity.getConsigneeCode());
        entity.setDef2(wmSoEntity.getDef8());
        entity.setDef3(wmSoEntity.getDef9());
        entity.setDef4(wmSoEntity.getDef10());
        entity.setDef5(wmSoEntity.getDef11());
        entity.setDef6(wmSoEntity.getDef12());
        entity.setDef8(wmSoEntity.getDef2());
        entity.setDef9(wmSoEntity.getDef13());
        entity.setDef10(wmSoEntity.getDef14());

        orderDelivery.setCarrierCode(wmSoEntity.getCarrierCode());
        entity.setOrderDelivery(orderDelivery);
        tmTransportOrderManager.saveEntity(entity);

        List<BanQinWmSoAllocEntity> allocList = wmSoEntity.getAllocList();
        for (int i = 0; i < allocList.size(); i++) {
            BanQinWmSoAllocEntity allocEntity = allocList.get(i);
            TmItem tmItem = this.getSku(entity.getCustomerCode(), allocEntity.getSkuCode(), orgId);
            double weight = tmItem.getGrossweight() == null ? 0 : tmItem.getGrossweight();
            double cubic = tmItem.getCubic() == null ? 0 : tmItem.getCubic();

            TmTransportOrderSku tmTransportOrderSku = new TmTransportOrderSku();
            tmTransportOrderSku.setTransportNo(entity.getTransportNo());
            tmTransportOrderSku.setLineNo(String.format("%04d", i + 1));
            tmTransportOrderSku.setOwnerCode(entity.getCustomerCode());
            tmTransportOrderSku.setSkuCode(allocEntity.getSkuCode());
            tmTransportOrderSku.setQty(allocEntity.getQtyEa());
            tmTransportOrderSku.setWeight(BigDecimalUtil.mul(weight, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setCubic(BigDecimalUtil.mul(cubic, tmTransportOrderSku.getQty()));
            tmTransportOrderSku.setBaseOrgId(orgId);
            tmTransportOrderSku.setOrgId(orgId);
            tmTransportOrderSku.setLotInfo(allocEntity.getLotNum());
            setPackQtyByWms(tmTransportOrderSku, allocEntity.getPackCode(), orgId);

            tmTransportOrderSku.setDef3(allocEntity.getDef1());
            tmTransportOrderSku.setDef4(allocEntity.getDef2());
            tmTransportOrderSku.setDef6(allocEntity.getQtyUom() + "");
            tmTransportOrderSku.setDef7(allocEntity.getUom());
            tmTransportOrderSku.setDef8(allocEntity.getQtySoEa() + "");
            tmTransportOrderSkuService.save(tmTransportOrderSku);
        }
        // 系统控制参数：下发时自动审核
        final String PUSH_TO_TMS_AUDIT = SysControlParamsUtils.getValue(SysParamConstants.PUSH_TO_TMS_AUDIT, orgId);
        if ("Y".equals(PUSH_TO_TMS_AUDIT)) {
            tmTransportOrderManager.audit(entity.getId());
        }
    }
}
