package com.yunyou.modules.tms.applet.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.app.entity.extend.TmAppUserInfoEntity;
import com.yunyou.modules.tms.app.service.TmAppUserInfoService;
import com.yunyou.modules.tms.applet.AppConstants;
import com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderLabelEntity;
import com.yunyou.modules.tms.applet.entity.extend.TmHandoverOrderEntity;
import com.yunyou.modules.tms.applet.entity.extend.TmHandoverOrderSkuEntity;
import com.yunyou.modules.tms.applet.entity.extend.*;
import com.yunyou.modules.tms.applet.entity.request.*;
import com.yunyou.modules.tms.applet.mapper.TmAppMapper;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.service.TmVehicleService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.order.entity.*;
import com.yunyou.modules.tms.order.entity.extend.TmDemandPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchTankInfoEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillEntity;
import com.yunyou.modules.tms.order.entity.extend.TmRepairOrderEntity;
import com.yunyou.modules.tms.order.entity.extend.TmVehicleSafetyCheckEntity;
import com.yunyou.modules.tms.order.entity.extend.*;
import com.yunyou.modules.tms.order.manager.*;
import com.yunyou.modules.tms.order.service.TmDispatchOrderHeaderService;
import com.yunyou.modules.tms.order.service.TmTransportOrderHeaderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * app后台Service
 *
 * @author zyf
 * @version 2020-06-30
 */
@Service
@Transactional(readOnly = true)
public class TmAppService extends BaseService {
    @Autowired
    private TmAppMapper tmAppMapper;
    @Autowired
    private TmAppUserInfoService tmAppUserInfoService;
    @Autowired
    private TmDispatchOrderHeaderService tmDispatchOrderHeaderService;
    @Autowired
    private TmTransportOrderHeaderService tmTransportOrderHeaderService;
    @Autowired
    private TmVehicleService tmVehicleService;
    @Autowired
    private TmDemandPlanManager tmDemandPlanManager;
    @Autowired
    private TmVehicleSafetyCheckManager tmVehicleSafetyCheckManager;
    @Autowired
    private TmDispatchOrderManager tmDispatchOrderManager;
    @Autowired
    private TmDispatchTankInfoManager tmDispatchTankInfoManager;
    @Autowired
    private TmHandoverOrderManager tmHandoverOrderManager;
    @Autowired
    private TmRepairOrderManager tmRepairOrderManager;
    @Autowired
    private TmExceptionHandleBillManager tmExceptionHandleBillManager;

    public TmAppUserInfoEntity getUserByLoginName(String loginName) {
        return tmAppUserInfoService.getByType(loginName, AppConstants.LOGIN_TYPE_NAME);
    }

    public TmAppUserInfoEntity getUserByWx(String openId) {
        return tmAppUserInfoService.getByType(openId, AppConstants.LOGIN_TYPE_WX);
    }

    public TmAppUserInfo getUserByUserId(String userId) {
        return tmAppUserInfoService.get(userId);
    }

    public List<TmItemEntity> querySkuList(String ownerCode, String skuCode, String skuName, String orgId) {
        return tmAppMapper.querySkuList(ownerCode, orgId, skuCode, skuName);
    }

    public List<TmDemandPlanHeaderEntity> findTmDemandPlanList(TmDemandPlanHeaderEntity condition) {
        return tmAppMapper.findTmDemandPlanList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmDemandPlanDetailEntity> findTmDemandPlanDetailList(com.yunyou.modules.tms.applet.entity.extend.TmDemandPlanDetailEntity condition) {
        return tmAppMapper.findTmDemandPlanDetailList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderEntity> findTmDispatchOrderList(com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderEntity condition) {
        return tmAppMapper.findTmDispatchOrderList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderSiteEntity> findTmDispatchOrderSiteList(String dispatchNo, String orgId) {
        return tmAppMapper.findTmDispatchOrderSiteList(new com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderSiteEntity(dispatchNo, orgId));
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmRepairOrderEntity> findTmRepairOrderList(com.yunyou.modules.tms.applet.entity.extend.TmRepairOrderEntity condition) {
        return tmAppMapper.findTmRepairOrderList(condition);
    }

    public List<TmDispatchTransportOrderEntity> findDispatchTransportOrderList(TmDispatchTransportOrderEntity condition) {
        return tmAppMapper.findDispatchTransportOrderList(condition);
    }

    public List<TmDispatchOrderLabelEntity> findTmDispatchOrderLabelList(TmDispatchOrderLabelEntity condition) {
        return tmAppMapper.findTmDispatchOrderLabelList(condition);
    }

    public List<TmHandoverOrderEntity> findTmHandoverOrderList(TmHandoverOrderEntity condition) {
        return tmAppMapper.findTmHandoverOrderList(condition);
    }

    public List<TmHandoverOrderSkuEntity> findTmHandoverOrderSkuList(TmHandoverOrderSkuEntity condition) {
        return tmAppMapper.findTmHandoverOrderSkuList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmExceptionHandleBillEntity> findTmExceptionHandleBillList(com.yunyou.modules.tms.applet.entity.extend.TmExceptionHandleBillEntity condition) {
        return tmAppMapper.findTmExceptionHandleBillList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmDispatchTankInfoEntity> findTmDispatchTankInfoList(com.yunyou.modules.tms.applet.entity.extend.TmDispatchTankInfoEntity condition) {
        return tmAppMapper.findTmDispatchTankInfoList(condition);
    }

    public List<TmMiddleSalaryEntity> findTmMiddleSalaryList(TmMiddleSalaryEntity condition) {
        return tmAppMapper.findTmMiddleSalaryList(condition);
    }

    public List<com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity> findTmVehicleSafetyCheckList(com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity condition) {
        return tmAppMapper.findTmVehicleSafetyCheckList(condition);
    }

    public List<TmTransportObjEntity> findSiteList(TmTransportObjEntity condition) {
        return tmAppMapper.findSiteList(condition);
    }

    public TmVehicle getVehicle(String vehicleNo, String orgId) {
        return tmVehicleService.getByNo(vehicleNo, orgId);
    }

    public List<TmVehicleEntity> findTmVehicleList(TmVehicleEntity entity) {
        return tmAppMapper.findTmVehicleList(entity);
    }

    public com.yunyou.modules.tms.applet.entity.extend.TmDispatchOrderEntity getDispatchOrder(String dispatchId) {
        return tmAppMapper.getDispatchOrder(dispatchId);
    }

    public com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity getTmVehicleSafetyCheck(String vehicleSafetyCheckId) {
        return tmAppMapper.getTmVehicleSafetyCheck(vehicleSafetyCheckId);
    }

    /**
     * 获取当天的出车安全检查表
     */
    public com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity getVehicleSafetyCheckIntraday(String vehicleNo, Date checkDate, String orgId) {
        com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity condition = new com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity();
        condition.setVehicleNo(vehicleNo);
        condition.setCheckDateFm(DateUtil.beginOfDate(checkDate));
        condition.setCheckDateTo(DateUtil.endOfDate(checkDate));
        condition.setOrgId(orgId);
        List<com.yunyou.modules.tms.applet.entity.extend.TmVehicleSafetyCheckEntity> list = tmAppMapper.findTmVehicleSafetyCheckList(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public void saveAppUser(TmAppUserInfo userInfo) {
        tmAppUserInfoService.save(userInfo);
    }

    /**
     * 需求下单-生成需求订单
     */
    @Transactional
    public void createCustomerOrder(TmAppCreateCustomerOrderRequest request) {
        User user = new User(request.getOperator());
        Date date = new Date();

        TmDemandPlanEntity planHeaderEntity = new TmDemandPlanEntity();
        planHeaderEntity.setStatus("00");
        planHeaderEntity.setOrderTime(request.getOrderTime());
        planHeaderEntity.setOwnerCode(request.getOwnerCode());
        planHeaderEntity.setArrivalTime(request.getArriveTime());
        planHeaderEntity.setBaseOrgId(request.getBaseOrgId());
        planHeaderEntity.setOrgId(request.getOrgId());
        planHeaderEntity.setCreateBy(user);
        planHeaderEntity.setCreateDate(date);
        planHeaderEntity.setUpdateBy(user);
        planHeaderEntity.setUpdateDate(date);
        planHeaderEntity = tmDemandPlanManager.saveEntity(planHeaderEntity);
        if (CollectionUtil.isNotEmpty(request.getSkuList())) {
            List<TmDemandPlanDetailEntity> detailList = Lists.newArrayList();
            for (TmAppCreateCustomerOrderSkuListRequest skuDetail : request.getSkuList()) {
                if (skuDetail.getQty() == null || skuDetail.getQty() == 0) {
                    continue;
                }
                TmDemandPlanDetailEntity detailEntity = new TmDemandPlanDetailEntity();
                detailEntity.setPlanOrderNo(planHeaderEntity.getPlanOrderNo());
                detailEntity.setOwnerCode(request.getOwnerCode());
                detailEntity.setSkuCode(skuDetail.getSkuCode());
                detailEntity.setQty(BigDecimal.valueOf(skuDetail.getQty()));
                detailEntity.setBaseOrgId(request.getBaseOrgId());
                detailEntity.setOrgId(request.getOrgId());
                detailEntity.setId(IdGen.uuid());
                detailEntity.setCreateBy(user);
                detailEntity.setCreateDate(date);
                detailEntity.setUpdateBy(user);
                detailEntity.setUpdateDate(date);
                detailEntity.setIsNewRecord(true);
                detailList.add(detailEntity);
            }
            tmDemandPlanManager.saveDetailList(detailList);
        }
    }

    /**
     * 删除需求订单
     */
    @Transactional
    public void deleteCustomerOrder(TmAppDeleteCustomerOrderRequest request) {
        TmDemandPlanEntity condition = new TmDemandPlanEntity();
        condition.setPlanOrderNo(request.getPlanOrderNo());
        condition.setOrgId(request.getOrgId());
        List<TmDemandPlanEntity> tmDemandPlanList = tmDemandPlanManager.findHeaderList(condition);
        if (CollectionUtil.isEmpty(tmDemandPlanList)) {
            throw new TmsException("数据不存在，请刷新！");
        }
        if ("10".equals(tmDemandPlanList.get(0).getStatus())) {
            throw new TmsException("已生成订单，无法删除！");
        }
        tmDemandPlanManager.removeEntity(tmDemandPlanManager.getEntity(tmDemandPlanList.get(0).getId()));
    }

    /**
     * 保存出车安全检查表
     */
    @Transactional
    public void saveVehicleSafetyCheck(TmAppCreateVehicleSafetyCheckRequest request) {
        TmVehicleSafetyCheckEntity saveEntity = new TmVehicleSafetyCheckEntity();
        if (StringUtils.isNotBlank(request.getId())) {
            saveEntity = tmVehicleSafetyCheckManager.getEntity(request.getId());
        } else {
            saveEntity.setCreateBy(new User(request.getOperator()));
            saveEntity.setCreateDate(new Date());
        }
        BeanUtils.copyProperties(request, saveEntity);
        saveEntity.setUpdateBy(new User(request.getOperator()));
        saveEntity.setUpdateDate(new Date());
        saveEntity.setCheckDate(new Date());
        tmVehicleSafetyCheckManager.saveEntity(saveEntity);
    }

    /**
     * 发车
     */
    @Transactional
    public void depart(TmAppDepartRequest request) {
        tmDispatchOrderManager.depart(request.getDispatchId());
    }

    /**
     * 派车单关闭
     */
    @Transactional
    public void finish(TmAppDispatchOrderFinishRequest request) {
        tmDispatchOrderManager.close(request.getDispatchId(), new User(request.getOperator()));
        TmVehicleSafetyCheckEntity tmVehicleSafetyCheck = tmVehicleSafetyCheckManager.getVehicleSafetyCheckIntraday(request.getCarNo(), request.getBaseOrgId());
        if (tmVehicleSafetyCheck != null) {
            tmVehicleSafetyCheck.setUpdateBy(new User(request.getOperator()));
            tmVehicleSafetyCheck.setUpdateDate(new Date());
            tmVehicleSafetyCheck.setReturnOdometerNumber(BigDecimal.valueOf(request.getOdometerNumber()));
            tmVehicleSafetyCheckManager.saveEntity(tmVehicleSafetyCheck);
        }
        if (request.getOdometerNumber() != null) {
            TmVehicle tmVehicle = tmVehicleService.getByNo(request.getCarNo(), request.getBaseOrgId());
            if (tmVehicle != null) {
                if (tmVehicle.getMileage() != null && request.getOdometerNumber() < tmVehicle.getMileage()) {
                    throw new TmsException("里程数不能小于该车辆里程数！");
                }
                tmVehicle.setMileage(request.getOdometerNumber());
                tmVehicle.setUpdateBy(new User(request.getOperator()));
                tmVehicle.setUpdateDate(new Date());
                tmVehicleService.save(tmVehicle);
            }
        }
    }

    /**
     * 网点交接 - 运输订单
     */
    @Transactional
    public void handoverConfirm(TmAppHandoverConfirmRequest request) {
        User user = new User(request.getOperator());
        tmHandoverOrderManager.handoverConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getOutletCode(), request.getConfirmPerson(), request.getRemarks(), request.getRsFlag(), request.getOrgId(), user);
        TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getOutletCode(), request.getOrgId());
        // 保存交接商品信息
        List<TmHandoverOrderSku> skuList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(request.getSkuList())) {
            for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), request.getRsFlag(), handoverOrder.getOrgId());
                handoverOrderSku.setActualQty(BigDecimal.valueOf(skuRequest.getActualQty()));
                handoverOrderSku.setUnloadingTime(BigDecimal.valueOf(skuRequest.getUnloadingTime()));
                skuList.add(handoverOrderSku);
            }
            tmHandoverOrderManager.batchSaveSku(skuList, user);
        }
    }

    /**
     * 揽收 - 运输订单
     */
    @Transactional
    public void receiveConfirm(TmAppHandoverConfirmRequest request) {
        User user = new User(request.getOperator());
        tmHandoverOrderManager.appReceiveConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getConfirmPerson(), request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), user);
        TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getConfirmPerson(), request.getOrgId());
        // 保存交接商品信息
        List<TmHandoverOrderSku> skuList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(request.getSkuList())) {
            for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), TmsConstants.RECEIVE, handoverOrder.getOrgId());
                handoverOrderSku.setActualQty(BigDecimal.valueOf(skuRequest.getActualQty()));
                handoverOrderSku.setUnloadingTime(BigDecimal.valueOf(skuRequest.getUnloadingTime()));
                skuList.add(handoverOrderSku);
            }
            tmHandoverOrderManager.batchSaveSku(skuList, user);
        }
    }

    /**
     * 签收 - 运输订单
     */
    @Transactional
    public void signConfirm(TmAppHandoverConfirmRequest request) {
        User user = new User(request.getOperator());
        tmHandoverOrderManager.appSignConfirmByTransportOrder(request.getDispatchNo(), request.getTransportNo(), request.getOutletCode(), request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), user);
        TmHandoverOrderHeader handoverOrder = tmHandoverOrderManager.getHandoverByDispatchAndOutlet(request.getDispatchNo(), request.getOutletCode(), request.getOrgId());
        List<TmHandoverOrderSku> skuList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(request.getSkuList())) {
            for (TmAppHandoverConfirmSkuListRequest skuRequest : request.getSkuList()) {
                TmHandoverOrderSku handoverOrderSku = tmHandoverOrderManager.getHandoverSku(handoverOrder.getHandoverNo(), request.getTransportNo(), skuRequest.getOwnerCode(), skuRequest.getSkuCode(), TmsConstants.SHIP, handoverOrder.getOrgId());
                handoverOrderSku.setActualQty(BigDecimal.valueOf(skuRequest.getActualQty()));
                handoverOrderSku.setUnloadingTime(BigDecimal.valueOf(skuRequest.getUnloadingTime()));
                skuList.add(handoverOrderSku);
            }
            tmHandoverOrderManager.batchSaveSku(skuList, user);
        }
    }

    /**
     * 网点交接 - 标签
     */
    @Transactional
    public void handoverConfirmLabel(TmAppHandoverConfirmRequest request) {
        tmHandoverOrderManager.handoverConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                request.getConfirmPerson(), request.getRemarks(), request.getRsFlag(), request.getOrgId(), new User(request.getOperator()));
    }

    /**
     * 揽收 - 标签
     */
    @Transactional
    public void receiveConfirmLabel(TmAppHandoverConfirmRequest request) {
        tmHandoverOrderManager.appReceiveConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), new User(request.getOperator()));
    }

    /**
     * 签收 - 标签
     */
    @Transactional
    public void signConfirmLabel(TmAppHandoverConfirmRequest request) {
        tmHandoverOrderManager.appSignConfirmByDispatchLabel(request.getDispatchNo(), request.getOutletCode(), request.getLabelNo(),
                request.getOrgId(), request.getConfirmPerson(), request.getRemarks(), new User(request.getOperator()));
    }

    /**
     * 附件保存
     */
    @Transactional
    public void saveTmAttachement(TmAppAttachementRequest request) {
        if (TmsConstants.IMP_UPLOAD_TYPE_HANDOVER.equals(request.getOrderType())) {
            // 交接单图片保存
            tmHandoverOrderManager.saveAppImgDetail(request.getDispatchNo(), request.getOutletCode(), request.getOrderType(), request.getLabelNo(), request.getOrgId(),
                    request.getFileName(), request.getImgFilePath(), request.getImgUrl(), new User(request.getOperator()));
        } else if (TmsConstants.IMP_UPLOAD_TYPE_EXCEPTION.equals(request.getOrderType())) {
            // 异常单图片保存
            tmExceptionHandleBillManager.saveImgDetail(request.getOrderNo(), request.getOrgId(), request.getFileName(), request.getImgFilePath(),
                    request.getImgUrl(), new User(request.getOperator()));
        } else {
            throw new TmsException("未知的图片类型！");
        }
    }

    /**
     * 保存派车单现金费用信息
     */
    @Transactional
    public void saveDispatchOrderCashAmount(TmAppDispatchOrderSaveRequest request) {
        TmDispatchOrderEntity con = new TmDispatchOrderEntity();
        con.setDispatchNo(request.getDispatchNo());
        con.setOrgId(request.getOrgId());
        List<TmDispatchOrderEntity> dispatchOrderList = tmDispatchOrderManager.findEntityList(con);
        if (CollectionUtil.isEmpty(dispatchOrderList)) {
            throw new TmsException("派车单不存在！");
        }
        TmDispatchOrderEntity dispatchOrder = dispatchOrderList.get(0);
        dispatchOrder.setCashAmount(request.getCashAmount());
        dispatchOrder.setRemarks(request.getRemarks());
        dispatchOrder.setUpdateBy(new User(request.getOperator()));
        dispatchOrder.setUpdateDate(new Date());
        tmDispatchOrderManager.saveEntity(dispatchOrder);
    }

    /**
     * 保存维修工单信息
     */
    @Transactional
    public TmRepairOrderHeader saveRepairOrder(TmAppSaveRepairOrderRequest request) {
        TmRepairOrderEntity saveEntity = new TmRepairOrderEntity();
        BeanUtils.copyProperties(request, saveEntity);
        saveEntity.setCreateBy(new User(request.getOperator()));
        saveEntity.setCreateDate(new Date());
        saveEntity.setUpdateBy(new User(request.getOperator()));
        saveEntity.setUpdateDate(new Date());
        String id = tmRepairOrderManager.saveForUnRepair(saveEntity);
        return tmRepairOrderManager.get(id);
    }

    /**
     * 维修完成确认
     */
    @Transactional
    public void finishRepairOrder(TmAppSaveRepairOrderRequest request) {
        TmRepairOrderEntity saveEntity = tmRepairOrderManager.getEntity(request.getId());
        saveEntity.setStatus("99");
        saveEntity.setUpdateBy(new User(request.getOperator()));
        saveEntity.setUpdateDate(new Date());
        tmRepairOrderManager.saveForUnRepair(saveEntity);
    }

    /**
     * 保存派车装罐信息
     */
    @Transactional
    public void saveDispatchTankInfo(TmAppCreateDispatchTankInfoRequest request) {
        TmDispatchTankInfoEntity saveEntity = new TmDispatchTankInfoEntity();
        if (StringUtils.isNotBlank(request.getId())) {
            saveEntity = tmDispatchTankInfoManager.getEntity(request.getId());
        } else {
            saveEntity.setCreateBy(new User(request.getOperator()));
            saveEntity.setCreateDate(new Date());
        }
        BeanUtils.copyProperties(request, saveEntity);
        saveEntity.setUpdateBy(new User(request.getOperator()));
        saveEntity.setUpdateDate(new Date());
        tmDispatchTankInfoManager.saveEntity(saveEntity);
    }

    /**
     * 异常上报
     */
    @Transactional
    public TmExceptionHandleBillEntity exceptionConfirm(TmAppExceptionConfirmRequest request) {
        TmTransportOrderHeader transportOrderHeader = tmTransportOrderHeaderService.getByNo(request.getTransportNo());
        TmDispatchOrderHeader dispatchOrderHeader = tmDispatchOrderHeaderService.getByNo(request.getDispatchNo());
        TmExceptionHandleBillEntity saveEntity = new TmExceptionHandleBillEntity();
        BeanUtils.copyProperties(request, saveEntity);
        saveEntity.setHappenTime(request.getHappenDate());
        saveEntity.setCustomerNo(transportOrderHeader.getCustomerNo());
        saveEntity.setConsigneeCode(transportOrderHeader.getConsigneeCode());
        saveEntity.setConsigneeName(transportOrderHeader.getConsignee());
        saveEntity.setConsigneePhone(transportOrderHeader.getConsigneeTel());
        saveEntity.setCustomerCode(transportOrderHeader.getCustomerCode());
        saveEntity.setCarNo(dispatchOrderHeader.getCarNo());
        saveEntity.setDriver(dispatchOrderHeader.getDriver());
        saveEntity.setCreateBy(new User(request.getOperator()));
        saveEntity.setCreateDate(new Date());
        saveEntity.setUpdateBy(new User(request.getOperator()));
        saveEntity.setUpdateDate(new Date());
        return tmExceptionHandleBillManager.saveEntity(saveEntity);
    }

}