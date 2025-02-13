package com.yunyou.modules.tms.applet.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.http.HttpClientUtil;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.MapUtil;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.app.entity.extend.TmAppUserInfoEntity;
import com.yunyou.modules.tms.applet.AppConstants;
import com.yunyou.modules.tms.applet.entity.extend.*;
import com.yunyou.modules.tms.applet.entity.request.*;
import com.yunyou.modules.tms.applet.service.TmAppService;
import com.yunyou.modules.tms.common.TmsConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * app后台Service
 *
 * @author zyf
 * @version 2020-06-30
 */
@Service
public class TmAppAction extends BaseAction {
    @Autowired
    private TmAppService tmAppService;

    /**
     * app注册
     */
    public ResultMessage appRegister(TmAppUserInfo userInfo) {
        ResultMessage msg = new ResultMessage("注册成功！");

        TmAppUserInfoEntity checkEntity = tmAppService.getUserByLoginName(userInfo.getLoginName());
        if (checkEntity != null) {
            msg.setSuccess(false);
            msg.setMessage("用户名已存在！");
            return msg;
        }
        userInfo.setStatus("00");
        tmAppService.saveAppUser(userInfo);

        return msg;
    }

    /**
     * app登录
     */
    public ResultMessage appLogin(String code, String password, String loginType) {
        if (AppConstants.LOGIN_TYPE_NAME.equals(loginType)) {
            return appLoginUseName(code, password);
        } else if (AppConstants.LOGIN_TYPE_WX.equals(loginType)) {
            return appLoginUseWx(code);
        } else {
            return new ResultMessage(false, "非法操作！");
        }
    }

    /**
     * app登录
     */
    public ResultMessage appLoginUseName(String loginName, String password) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfoEntity userInfo = tmAppService.getUserByLoginName(loginName);
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户不存在！");
            return msg;
        }
        if (!userInfo.getPassword().equals(password)) {
            msg.setSuccess(false);
            msg.setMessage("密码错误！");
            return msg;
        }

        msg.setData(userInfo);
        return msg;
    }

    /**
     * app登录
     */
    public ResultMessage appLoginUseWx(String code) {
        ResultMessage msg = new ResultMessage();

        String responseString = HttpClientUtil.getInstance().sendHttpGet(TmsConstants.WX_APP_LOGIN_URL
                + "?appid=" + TmsConstants.WX_APP_ID + "&secret=" + TmsConstants.WX_APP_SECRET
                + "&js_code=" + code + "&grant_type=authorization_code");
        if (StringUtils.isBlank(responseString)) {
            msg.setSuccess(false);
            msg.setMessage("网络异常！");
            return msg;
        }
        JSONObject json = JSONObject.parseObject(responseString);
        String openId = json.getString("openid");
        if (StringUtils.isBlank(openId)) {
            msg.setSuccess(false);
            msg.setMessage("微信登陆异常：" + json.getInteger("errcode") + "--" + json.getString("errmsg"));
            return msg;
        }

        TmAppUserInfoEntity userInfo = tmAppService.getUserByWx(openId);
        if (userInfo == null) {
            userInfo = new TmAppUserInfoEntity();
            userInfo.setLoginName(openId);
            userInfo.setPassword("123456");
            userInfo.setName(openId);
            userInfo.setStatus("00");
            userInfo.setDef1(openId);
            tmAppService.saveAppUser(userInfo);
        }

        msg.setData(userInfo);
        return msg;
    }

    /**
     * 校验用户名是否存在
     */
    public ResultMessage checkUser(String loginName) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfoEntity userInfo = tmAppService.getUserByLoginName(loginName);
        if (userInfo != null) {
            msg.setSuccess(false);
            msg.setMessage("用户名已存在！");
        }

        return msg;
    }

    /**
     * 保存用户信息
     */
    public ResultMessage saveUserInfo(TmAppUserInfo entity) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfoEntity userInfo = tmAppService.getUserByLoginName(entity.getLoginName());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户不存在！");
            return msg;
        }
        userInfo.setDef2(entity.getDef2());
        userInfo.setLoginName(entity.getDef3());
        userInfo.setName(entity.getName());
        userInfo.setRemarks(entity.getRemarks());
        tmAppService.saveAppUser(userInfo);

        msg.setData(userInfo);
        return msg;
    }

    /**
     * 需求下单-查询商品
     */
    public ResultMessage querySkuList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsCustomer()) || StringUtils.isBlank(userInfo.getTransportObjCode())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String tmOrgId = userInfo.getBaseOrgId();
        String customerCode = userInfo.getTransportObjCode();

        List<TmItemEntity> tmItemEntityList = tmAppService.querySkuList(customerCode, request.getSkuCode(), request.getSkuName(), tmOrgId);
        if (CollectionUtil.isEmpty(tmItemEntityList)) {
            msg.setData("");
            return msg;
        }
        List<Map<String, Object>> skuList = Lists.newArrayList();
        Map<String, List<TmItemEntity>> typeSkuMap = tmItemEntityList.stream()
                .collect(Collectors.groupingBy(item -> StringUtils.isBlank(item.getSkuType()) ? "其他" : item.getSkuType()));
        for (Map.Entry<String, List<TmItemEntity>> entry : typeSkuMap.entrySet()) {
            String skuType = DictUtils.getDictLabel(entry.getKey(), "TMS_SKU_TYPE", "其他");
            Map<String, Object> skuTypeMap = Maps.newLinkedHashMap();
            List<Map<String, Object>> skus = Lists.newArrayList();
            for (TmItemEntity item : entry.getValue()) {
                Map<String, Object> skuMap = Maps.newLinkedHashMap();
                skuMap.put("skuCode", item.getSkuCode());
                skuMap.put("skuName", item.getSkuName());
                skuMap.put("unit", item.getUnit());
                skuMap.put("describe", item.getRemarks());
                skuMap.put("check", "");
                skus.add(skuMap);
            }
            skuTypeMap.put("type", skuType);
            skuTypeMap.put("skus", skus);
            skuList.add(skuTypeMap);
        }

        msg.setData(skuList);
        return msg;
    }

    /**
     * 需求下单-生成需求订单
     */
    public ResultMessage createCustomerOrder(TmAppCreateCustomerOrderRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsCustomer()) || StringUtils.isBlank(userInfo.getTransportObjCode())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOwnerCode(userInfo.getTransportObjCode());
        request.setOperator(userInfo.getLoginName());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOrgId(userInfo.getOrgId());
        tmAppService.createCustomerOrder(request);

        return msg;
    }

    /**
     * 需求订单列表
     */
    public ResultMessage queryCustomerOrder(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsCustomer()) || StringUtils.isBlank(userInfo.getTransportObjCode())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String customerCode = userInfo.getTransportObjCode();
        TmDemandPlanHeaderEntity condition = new TmDemandPlanHeaderEntity();
        condition.setOwnerCode(customerCode);
        condition.setStatus(request.getStatus());
        condition.setOrgId(userInfo.getOrgId());
        List<TmDemandPlanHeaderEntity> tmDemandPlanList = tmAppService.findTmDemandPlanList(condition);
        for (TmDemandPlanHeaderEntity entity : tmDemandPlanList) {
            entity.setDetailList(tmAppService.findTmDemandPlanDetailList(new TmDemandPlanDetailEntity(entity.getPlanOrderNo(), entity.getOrgId())));
        }

        msg.setData(tmDemandPlanList);
        return msg;
    }

    /**
     * 删除需求订单
     */
    public ResultMessage deleteCustomerOrder(TmAppDeleteCustomerOrderRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsCustomer()) || StringUtils.isBlank(userInfo.getTransportObjCode())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        try {
            tmAppService.deleteCustomerOrder(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 派车单列表
     */
    public ResultMessage queryDispatchOrder(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String driverCode = userInfo.getDriver();
        TmDispatchOrderEntity condition = new TmDispatchOrderEntity();
        condition.setDispatchStatus(request.getStatus());
        condition.setDriver(driverCode);
        condition.setOrgId(userInfo.getOrgId());
        List<TmDispatchOrderEntity> dispatchOrderList = tmAppService.findTmDispatchOrderList(condition);
        for (TmDispatchOrderEntity entity : dispatchOrderList) {
            // 拼接网点信息
            List<TmDispatchOrderSiteEntity> tmDispatchOrderSiteList = tmAppService.findTmDispatchOrderSiteList(entity.getDispatchNo(), entity.getOrgId());
            String dispatchOutletName = tmDispatchOrderSiteList.stream().filter(t -> TmsConstants.RECEIVE.equals(t.getReceiveShip())).map(TmDispatchOrderSiteEntity::getOutletName).collect(Collectors.joining(","));
            String receiveOutletName = tmDispatchOrderSiteList.stream().filter(t -> TmsConstants.SHIP.equals(t.getReceiveShip())).map(TmDispatchOrderSiteEntity::getOutletName).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(dispatchOutletName)) {
                entity.setDispatchOutletName(dispatchOutletName);
            }
            if (StringUtils.isNotBlank(receiveOutletName)) {
                entity.setReceiveOutletName(receiveOutletName);
            }
            if (entity.getDispatchOutletName() == null) {
                entity.setDispatchOutletName("");
            }
            if (entity.getReceiveOutletName() == null) {
                entity.setReceiveOutletName("");
            }
            // 获取出车安全检查表状态
            TmVehicleSafetyCheckEntity vehicleSafetyCheck = tmAppService.getVehicleSafetyCheckIntraday(entity.getCarNo(), entity.getDispatchTime(), entity.getOrgId());
            if (vehicleSafetyCheck != null) {
                entity.setDepartureOdometerNumber(vehicleSafetyCheck.getDepartureOdometerNumber());
                entity.setIsSafetyCheck(vehicleSafetyCheck.getConfirmConclusion());
            }
            // 获取车辆检查项状态
            TmVehicleEntity con = new TmVehicleEntity();
            con.setCarNo(entity.getCarNo());
            con.setOrgId(entity.getBaseOrgId());
            List<TmVehicleEntity> tmVehicleList = tmAppService.findTmVehicleList(con);
            if (CollectionUtil.isNotEmpty(tmVehicleList)) {
                TmVehicleEntity tmVehicle = tmVehicleList.get(0);
                entity.setMileage(tmVehicle.getMileage() == null ? BigDecimal.ZERO : BigDecimal.valueOf(tmVehicle.getMileage()));

                Map<String, String> checkItemStatusMap = getVehicleCheckItemStatus(tmVehicle);
                if (MapUtil.isNotEmpty(checkItemStatusMap)) {
                    if (checkItemStatusMap.containsValue(AppConstants.VEHICLE_CHECK_ITEM_STATUS_ERROR)) {
                        entity.setVehicleCheckItemStatus(AppConstants.VEHICLE_CHECK_ITEM_STATUS_ERROR);
                    } else if (checkItemStatusMap.containsValue(AppConstants.VEHICLE_CHECK_ITEM_STATUS_WARN)) {
                        entity.setVehicleCheckItemStatus(AppConstants.VEHICLE_CHECK_ITEM_STATUS_WARN);
                    } else {
                        entity.setVehicleCheckItemStatus(AppConstants.VEHICLE_CHECK_ITEM_STATUS_NORMAL);
                    }
                }
            }
            // 获取车辆维修状态
            TmRepairOrderEntity repairCon = new TmRepairOrderEntity();
            repairCon.setIsUnConfirm(Global.Y);
            repairCon.setCarNo(entity.getCarNo());
            repairCon.setBaseOrgId(userInfo.getBaseOrgId());
            List<TmRepairOrderEntity> unConfirmOrderList = tmAppService.findTmRepairOrderList(repairCon);
            if (CollectionUtil.isNotEmpty(unConfirmOrderList)) {
                entity.setRepairStatus(AppConstants.VEHICLE_REPAIR_STATUS_REPAIR);
            } else {
                entity.setRepairStatus(AppConstants.VEHICLE_REPAIR_STATUS_NORMAL);
            }
        }

        msg.setData(dispatchOrderList);
        return msg;
    }

    /**
     * 获取派车单信息
     */
    public ResultMessage getDispatchOrder(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmDispatchOrderEntity condition = new TmDispatchOrderEntity();
        condition.setDispatchNo(request.getDispatchNo());
        condition.setOrgId(userInfo.getOrgId());
        List<TmDispatchOrderEntity> dispatchOrderList = tmAppService.findTmDispatchOrderList(condition);
        if (CollectionUtil.isEmpty(dispatchOrderList)) {
            msg.setSuccess(false);
            msg.setMessage("未找到派车单，请刷新数据！");
            return msg;
        }

        msg.setData(dispatchOrderList.get(0));
        return msg;
    }

    /**
     * 获取车辆信息
     */
    public ResultMessage getVehicleInfo(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmVehicleEntity condition = new TmVehicleEntity();
        condition.setCarNo(request.getVehicleNo());
        condition.setOrgId(userInfo.getBaseOrgId());
        List<TmVehicleEntity> tmVehicleList = tmAppService.findTmVehicleList(condition);
        if (CollectionUtil.isEmpty(tmVehicleList)) {
            msg.setSuccess(false);
            msg.setMessage("未找到派车单，请刷新数据！");
            return msg;
        }
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("vehicleInfo", tmVehicleList.get(0));
        map.put("checkItemStatus", getVehicleCheckItemStatus(tmVehicleList.get(0)));

        msg.setData(map);
        return msg;
    }

    /**
     * 保存派车单现金费用信息
     */
    public ResultMessage saveDispatchOrderCashAmount(TmAppDispatchOrderSaveRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.saveDispatchOrderCashAmount(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 获取出车安全检查表
     */
    public ResultMessage getVehicleSafetyCheckIntraday(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmVehicleSafetyCheckEntity entity;
        if (StringUtils.isNotBlank(request.getDispatchId())) {
            TmDispatchOrderEntity dispatchOrder = tmAppService.getDispatchOrder(request.getDispatchId());
            entity = tmAppService.getVehicleSafetyCheckIntraday(dispatchOrder.getCarNo(), dispatchOrder.getDispatchTime(), userInfo.getOrgId());
        } else if (StringUtils.isNotBlank(request.getId())) {
            entity = tmAppService.getTmVehicleSafetyCheck(request.getId());
        } else {
            entity = tmAppService.getVehicleSafetyCheckIntraday(request.getVehicleNo(), new Date(), userInfo.getOrgId());
        }

        msg.setData(entity);
        return msg;
    }

    /**
     * 保存出车安全检查表
     */
    public ResultMessage saveVehicleSafetyCheck(TmAppCreateVehicleSafetyCheckRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.saveVehicleSafetyCheck(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 发车
     */
    public ResultMessage depart(TmAppDepartRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        /*TmDispatchOrderEntity dispatchOrder = tmAppService.getDispatchOrder(request.getDispatchId());
        TmVehicleSafetyCheckEntity vehicleSafetyCheck = tmAppService.getVehicleSafetyCheckIntraday(dispatchOrder.getCarNo(), dispatchOrder.getDispatchTime(), userInfo.getOrgId());
        if (vehicleSafetyCheck == null) {
            msg.setSuccess(false);
            msg.setMessage("未填写出车安全检查表，无法发车！");
            return msg;
        }
        if (!AppConstants.VEHICLE_SAFETY_CHECK_STATUS_0.equals(vehicleSafetyCheck.getConfirmConclusion())) {
            msg.setSuccess(false);
            msg.setMessage("安全检查未通过，无法发车！");
            return msg;
        }
        // 获取车辆维修状态
        TmRepairOrderEntity repairCon = new TmRepairOrderEntity();
        repairCon.setIsUnConfirm(Global.Y);
        repairCon.setCarNo(request.getVehicleNo());
        repairCon.setBaseOrgId(userInfo.getBaseOrgId());
        List<TmRepairOrderEntity> unConfirmOrderList = tmAppMapper.findTmRepairOrderList(repairCon);
        if (CollectionUtil.isNotEmpty(unConfirmOrderList)) {
            j.setSuccess(false);
            j.setMsg("存在未处理的维修工单，无法发车！");
            return j;
        }*/
        try {
            tmAppService.depart(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 派车单关闭
     */
    public ResultMessage finish(TmAppDispatchOrderFinishRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.finish(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 派车单网点列表
     */
    public ResultMessage queryDispatchOrderSiteList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String dispatchNo = request.getDispatchNo();
        List<TmDispatchOrderSiteEntity> dispatchOrderSiteList = tmAppService.findTmDispatchOrderSiteList(dispatchNo, userInfo.getOrgId());
        for (TmDispatchOrderSiteEntity entity : dispatchOrderSiteList) {
            String[] rsFlag = entity.getReceiveShip().split(",");
            if (rsFlag.length > 1) {
                String receiveShip = DictUtils.getDictLabel(rsFlag[0], "TMS_RECEIVE_SHIP", "") + "/"
                        + DictUtils.getDictLabel(rsFlag[1], "TMS_RECEIVE_SHIP", "");
                entity.setReceiveShip(receiveShip);
            } else {
                entity.setReceiveShip(DictUtils.getDictLabel(entity.getReceiveShip(), "TMS_RECEIVE_SHIP", ""));
            }
            // 网点类型（区分签收/交接操作）
            /*if (entity.getOutletType().contains("CUSTOMER")) {
                entity.setOutletType("CUSTOMER");
            } else {
                entity.setOutletType("OUTLET");
            }*/
            entity.setOutletType("OUTLET");
            // 统计订单数
            TmDispatchTransportOrderEntity orderCon = new TmDispatchTransportOrderEntity();
            orderCon.setDispatchNo(dispatchNo);
            orderCon.setDispatchSiteOutletCode(entity.getOutletCode());
            orderCon.setOrgId(userInfo.getOrgId());
            List<TmDispatchTransportOrderEntity> orderList = tmAppService.findDispatchTransportOrderList(orderCon);
            if (CollectionUtil.isNotEmpty(orderList)) {
                entity.setOrderCount((double) orderList.size());
            } else {
                entity.setOrderCount(0d);
            }
            // 统计标签数
            TmDispatchOrderLabelEntity labelCon = new TmDispatchOrderLabelEntity();
            labelCon.setDispatchNo(dispatchNo);
            labelCon.setDispatchSiteOutletCode(entity.getOutletCode());
            labelCon.setOrgId(userInfo.getOrgId());
            List<TmDispatchOrderLabelEntity> labelList = tmAppService.findTmDispatchOrderLabelList(labelCon);
            if (CollectionUtil.isNotEmpty(labelList)) {
                entity.setLabelCount((double) labelList.size());
            } else {
                entity.setLabelCount(0d);
            }
        }

        msg.setData(dispatchOrderSiteList);
        return msg;
    }

    /**
     * 派车单客户订单列表
     */
    public ResultMessage queryDispatchTransportOrderList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmDispatchTransportOrderEntity condition = new TmDispatchTransportOrderEntity();
        condition.setDispatchNo(request.getDispatchNo());
        condition.setDispatchSiteOutletCode(request.getOutletCode());
        condition.setReceiveShip(request.getStatus());
        condition.setOrgId(userInfo.getOrgId());
        List<TmDispatchTransportOrderEntity> dispatchTransportOrderList = tmAppService.findDispatchTransportOrderList(condition);
        for (TmDispatchTransportOrderEntity orderEntity : dispatchTransportOrderList) {
            TmDispatchOrderLabelEntity labelCon = new TmDispatchOrderLabelEntity();
            labelCon.setDispatchNo(orderEntity.getDispatchNo());
            labelCon.setTransportNo(orderEntity.getTransportNo());
            labelCon.setDispatchSiteOutletCode(orderEntity.getDispatchSiteOutletCode());
            labelCon.setReceiveShip(request.getStatus());
            labelCon.setOrgId(orderEntity.getOrgId());
            List<TmDispatchOrderLabelEntity> dispatchTransportOrderDetailList = tmAppService.findTmDispatchOrderLabelList(labelCon);
            long newCount = dispatchTransportOrderDetailList.stream().filter(t -> TmsConstants.HANDOVER_LABEL_STATUS_00.equals(t.getStatus())).count();
            if (newCount == 0) {
                orderEntity.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_20);
            } else if (newCount == dispatchTransportOrderDetailList.size()) {
                orderEntity.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_00);
            } else if (newCount > 0) {
                orderEntity.setStatus(TmsConstants.HANDOVER_ORDER_STATUS_10);
            }
        }

        msg.setData(dispatchTransportOrderList);
        return msg;
    }

    /**
     * 派车单客户订单明细列表
     */
    public ResultMessage queryDispatchTransportOrderDetailList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        /*TmDispatchTransportOrderDetailEntity condition = new TmDispatchTransportOrderDetailEntity();
        condition.setTransportNo(request.getTransportNo());
        condition.setOrgId(userInfo.getOrgId());
        List<TmDispatchTransportOrderDetailEntity> dispatchTransportOrderDetailList = tmAppMapper.findDispatchTransportOrderDetailList(condition);*/

        TmHandoverOrderEntity con = new TmHandoverOrderEntity();
        con.setDispatchNo(request.getDispatchNo());
        con.setDeliveryOutletCode(request.getOutletCode());
        con.setOrgId(userInfo.getOrgId());
        List<TmHandoverOrderEntity> tmHandoverOrderList = tmAppService.findTmHandoverOrderList(con);
        if (CollectionUtil.isNotEmpty(tmHandoverOrderList)) {
            TmHandoverOrderSkuEntity skuCon = new TmHandoverOrderSkuEntity();
            skuCon.setHandoverNo(tmHandoverOrderList.get(0).getHandoverNo());
            skuCon.setTransportNo(request.getTransportNo());
            skuCon.setOrgId(userInfo.getOrgId());
            msg.setData(tmAppService.findTmHandoverOrderSkuList(skuCon));
        }

        return msg;
    }

    /**
     * 派车单标签列表
     */
    public ResultMessage queryDispatchLabelList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmDispatchOrderLabelEntity condition = new TmDispatchOrderLabelEntity();
        condition.setDispatchNo(request.getDispatchNo());
        condition.setTransportNo(request.getTransportNo());
        condition.setDispatchSiteOutletCode(request.getOutletCode());
        condition.setReceiveShip(request.getStatus());
        condition.setOrgId(userInfo.getOrgId());
        msg.setData(tmAppService.findTmDispatchOrderLabelList(condition));

        return msg;
    }

    /**
     * 网点交接
     */
    public ResultMessage handoverConfirm(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            if (Global.Y.equals(request.getIsLabel())) {
                tmAppService.handoverConfirmLabel(request);
            } else if (Global.N.equals(request.getIsLabel())) {
                tmAppService.handoverConfirm(request);
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 揽收 / 签收
     */
    public ResultMessage receiveOrSignConfirm(TmAppHandoverConfirmRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            if (Global.Y.equals(request.getIsLabel())) {
                if (TmsConstants.RECEIVE.equals(request.getRsFlag())) {
                    tmAppService.receiveConfirmLabel(request);
                } else if (TmsConstants.SHIP.equals(request.getRsFlag())) {
                    tmAppService.signConfirmLabel(request);
                }
            } else if (Global.N.equals(request.getIsLabel())) {
                if (TmsConstants.RECEIVE.equals(request.getRsFlag())) {
                    tmAppService.receiveConfirm(request);
                } else if (TmsConstants.SHIP.equals(request.getRsFlag())) {
                    tmAppService.signConfirm(request);
                }
            }
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 保存图片附件表
     */
    public ResultMessage saveTmAttachement(TmAppAttachementRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.saveTmAttachement(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 异常上报
     */
    public ResultMessage exceptionConfirm(TmAppExceptionConfirmRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        request.setRegisterPerson(userInfo.getName());
        request.setRegisterTime(new Date());
        msg.setData(tmAppService.exceptionConfirm(request));

        return msg;
    }

    /**
     * 异常处理单列表
     */
    public ResultMessage queryExceptionHandleBill(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmExceptionHandleBillEntity condition = new TmExceptionHandleBillEntity();
        condition.setBillStatus(request.getStatus());
        condition.setTransportNo(request.getTransportNo());
        condition.setDispatchNo(request.getDispatchNo());
        condition.setRegisterPerson(userInfo.getName());
        condition.setOrgId(userInfo.getOrgId());
        msg.setData(tmAppService.findTmExceptionHandleBillList(condition));

        return msg;
    }

    /**
     * 交接单列表
     */
    public ResultMessage queryHandoverOrder(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsCustomer()) || StringUtils.isBlank(userInfo.getTransportObjCode())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmHandoverOrderEntity condition = new TmHandoverOrderEntity();
        condition.setStatus(request.getStatus());
        condition.setDeliveryOutletCode(userInfo.getTransportObjCode());
        condition.setOrgId(userInfo.getOrgId());
        List<TmHandoverOrderEntity> handoverOrderList = tmAppService.findTmHandoverOrderList(condition);
        for (TmHandoverOrderEntity entity : handoverOrderList) {
            TmHandoverOrderSkuEntity skuCon = new TmHandoverOrderSkuEntity();
            skuCon.setHandoverNo(entity.getHandoverNo());
            skuCon.setOrgId(entity.getOrgId());
            entity.setSkuList(tmAppService.findTmHandoverOrderSkuList(skuCon));
        }
        msg.setData(handoverOrderList);

        return msg;
    }

    /**
     * 维修单-查询车辆
     */
    public ResultMessage queryVehicleList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String tmOrgId = userInfo.getBaseOrgId();
        Date today = new Date();
        TmDispatchOrderEntity dispatchCon = new TmDispatchOrderEntity();
        dispatchCon.setDispatchTimeFm(DateUtil.beginOfDate(today));
        dispatchCon.setDispatchTimeTo(DateUtil.endOfDate(today));
        dispatchCon.setDriver(userInfo.getDriver());
        dispatchCon.setOrgId(userInfo.getOrgId());
        List<TmDispatchOrderEntity> dispatchOrderList = tmAppService.findTmDispatchOrderList(dispatchCon);
        if (CollectionUtil.isEmpty(dispatchOrderList)) {
            msg.setData("");
            return msg;
        }
        List<String> carNoList = dispatchOrderList.stream().map(TmDispatchOrderEntity::getCarNo).distinct().collect(Collectors.toList());
        TmVehicleEntity con = new TmVehicleEntity();
        con.setCarNoList(carNoList);
        con.setOrgId(tmOrgId);
        List<TmVehicleEntity> tmVehicleEntityList = tmAppService.findTmVehicleList(con);
        if (CollectionUtil.isEmpty(tmVehicleEntityList)) {
            msg.setData("");
            return msg;
        }
        List<Map<String, Object>> vehicleList = Lists.newArrayList();
        Map<String, List<TmVehicleEntity>> carrierVehicleMap = tmVehicleEntityList.stream()
                .collect(Collectors.groupingBy(item ->
                        StringUtils.isBlank(item.getTransportEquipmentTypeName()) ? "其他" : item.getTransportEquipmentTypeName()));
        for (Map.Entry<String, List<TmVehicleEntity>> entry : carrierVehicleMap.entrySet()) {
            Map<String, Object> vehicleMap = Maps.newLinkedHashMap();
            List<Map<String, Object>> carList = Lists.newArrayList();
            for (TmVehicleEntity item : entry.getValue()) {
                Map<String, Object> carMap = Maps.newLinkedHashMap();
                carMap.put("carNo", item.getCarNo());
                carMap.put("carrierCode", item.getCarrierCode());
                carMap.put("carrierName", item.getCarrierName());
                carList.add(carMap);
            }
            vehicleMap.put("carType", entry.getKey());
            vehicleMap.put("carList", carList);
            vehicleList.add(vehicleMap);
        }
        msg.setData(vehicleList);
        // put("vehicleList", "");
        return msg;
    }

    /**
     * 维修单-查询车辆（全查）
     */
    public ResultMessage queryVehicleListAll(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String tmOrgId = userInfo.getBaseOrgId();
        TmVehicleEntity con = new TmVehicleEntity();
        con.setOrgId(tmOrgId);
        List<TmVehicleEntity> tmVehicleEntityList = tmAppService.findTmVehicleList(con);
        if (CollectionUtil.isEmpty(tmVehicleEntityList)) {
            msg.setData("");
            return msg;
        }
        List<Map<String, Object>> vehicleList = Lists.newArrayList();
        Map<String, List<TmVehicleEntity>> carrierVehicleMap = tmVehicleEntityList.stream()
                .collect(Collectors.groupingBy(item ->
                        StringUtils.isBlank(item.getTransportEquipmentTypeName()) ? "其他" : item.getTransportEquipmentTypeName()));
        for (Map.Entry<String, List<TmVehicleEntity>> entry : carrierVehicleMap.entrySet()) {
            Map<String, Object> vehicleMap = Maps.newLinkedHashMap();
            List<Map<String, Object>> carList = Lists.newArrayList();
            for (TmVehicleEntity item : entry.getValue()) {
                Map<String, Object> carMap = Maps.newLinkedHashMap();
                carMap.put("carNo", item.getCarNo());
                carMap.put("carrierCode", item.getCarrierCode());
                carMap.put("carrierName", item.getCarrierName());
                carList.add(carMap);
            }
            vehicleMap.put("carType", entry.getKey());
            vehicleMap.put("carList", carList);
            vehicleList.add(vehicleMap);
        }
        msg.setData(vehicleList);

        return msg;
    }

    /**
     * 保存维修工单信息
     */
    public ResultMessage saveRepairOrder(TmAppSaveRepairOrderRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        msg.setData(tmAppService.saveRepairOrder(request));

        return msg;
    }

    /**
     * 维修单列表
     */
    public ResultMessage queryRepairOrder(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        String driverCode = userInfo.getDriver();
        String tmOrgId = userInfo.getBaseOrgId();
        Date today = new Date();
        TmDispatchOrderEntity dispatchCon = new TmDispatchOrderEntity();
        dispatchCon.setDispatchTimeFm(DateUtil.beginOfDate(today));
        dispatchCon.setDispatchTimeTo(DateUtil.endOfDate(today));
        dispatchCon.setDriver(driverCode);
        dispatchCon.setOrgId(userInfo.getOrgId());
        List<String> carNoList = tmAppService.findTmDispatchOrderList(dispatchCon)
                .stream().map(TmDispatchOrderEntity::getCarNo)
                .distinct().collect(Collectors.toList());

        TmRepairOrderEntity condition = new TmRepairOrderEntity();
        condition.setCarNoList(carNoList);
        condition.setStatus(request.getStatus());
        condition.setBaseOrgId(tmOrgId);
        msg.setData(tmAppService.findTmRepairOrderList(condition));

        return msg;
    }

    /**
     * 维修完成确认
     */
    public ResultMessage finishRepairOrder(TmAppSaveRepairOrderRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.finishRepairOrder(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 获取车辆检查项状态
     */
    private Map<String, String> getVehicleCheckItemStatus(TmVehicleEntity vehicle) {
        Map<String, String> checkStatusMap = Maps.newHashMap();
        checkStatusMap.put("annualReviewExpiryTime", checkExpiryTime(vehicle.getAnnualReviewExpiryTime(), 30));
        checkStatusMap.put("twoDimensionExpiryTime", checkExpiryTime(vehicle.getTwoDimensionExpiryTime(), 30));
        checkStatusMap.put("tankInspectionExpiryTime", checkExpiryTime(vehicle.getTankInspectionExpiryTime(), 30));
        checkStatusMap.put("insuranceExpiryTime", checkExpiryTime(vehicle.getInsuranceExpiryTime(), 30));
        checkStatusMap.put("drivingLicenseExpiryTime", checkExpiryTime(vehicle.getDrivingLicenseExpiryTime(), 30));
        checkStatusMap.put("operatingLicenseExpiryTime", checkExpiryTime(vehicle.getOperatingLicenseExpiryTime(), 30));
        checkStatusMap.put("vehicleMaintenanceMileage", checkMileage(vehicle.getMileage(), vehicle.getVehicleMaintenanceMileage(), 1000));
        checkStatusMap.put("vehicleApplyGreaseMileage", checkMileage(vehicle.getMileage(), vehicle.getVehicleApplyGreaseMileage(), 1000));
        checkStatusMap.put("vehicleOilChangeMileage", checkMileage(vehicle.getMileage(), vehicle.getVehicleOilChangeMileage(), 1000));
        return checkStatusMap;
    }

    /**
     * 校验有效期
     */
    private String checkExpiryTime(Date expiryTime, int warnDays) {
        if (expiryTime == null) {
            return null;
        }
        Date now = new Date();
        if (now.after(expiryTime)) {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_ERROR;
        } else if (now.after(DateUtil.subDays(expiryTime, warnDays))) {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_WARN;
        } else {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_NORMAL;
        }
    }

    /**
     * 校验里程数
     */
    private String checkMileage(Double mileage, Integer checkMileage, int warnMileage) {
        if (mileage == null || checkMileage == null) {
            return null;
        }
        if (mileage > checkMileage) {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_ERROR;
        } else if (mileage > checkMileage - warnMileage) {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_WARN;
        } else {
            return AppConstants.VEHICLE_CHECK_ITEM_STATUS_NORMAL;
        }
    }

    /**
     * 获取派车装罐信息
     */
    public ResultMessage getDispatchTankInfo(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmDispatchTankInfoEntity con = new TmDispatchTankInfoEntity();
        con.setDispatchNo(request.getDispatchNo());
        con.setOutletCode(request.getOutletCode());
        con.setTransportNo(request.getTransportNo());
        con.setOrgId(userInfo.getOrgId());
        con.setBaseOrgId(userInfo.getBaseOrgId());
        List<TmDispatchTankInfoEntity> tmDispatchTankInfoList = tmAppService.findTmDispatchTankInfoList(con);
        if (CollectionUtil.isNotEmpty(tmDispatchTankInfoList)) {
            msg.setData(tmDispatchTankInfoList.get(0));
        } else {
            msg.setData(null);
        }

        return msg;
    }

    /**
     * 保存派车装罐信息
     */
    public ResultMessage saveDispatchTankInfo(TmAppCreateDispatchTankInfoRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        request.setOrgId(userInfo.getOrgId());
        request.setBaseOrgId(userInfo.getBaseOrgId());
        request.setOperator(userInfo.getLoginName());
        try {
            tmAppService.saveDispatchTankInfo(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

    /**
     * 司机工资列表（每月）
     */
    public ResultMessage driverSalaryList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!"Y".equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        List<Map<String, Object>> returnList = Lists.newArrayList();
        BigDecimal totalAmount = BigDecimal.ZERO;

        String driverCode = userInfo.getDriver();
        Date searchDate = request.getSearchDate();
        Date salaryDateFm = DateUtil.beginOfMonth(searchDate);
        Date salaryDateTo = DateUtil.endOfMonth(searchDate);

        TmMiddleSalaryEntity con = new TmMiddleSalaryEntity();
        con.setPersonCode(driverCode);
        con.setSalaryDateFm(salaryDateFm);
        con.setSalaryDateTo(salaryDateTo);
        con.setBaseOrgId(userInfo.getBaseOrgId());
        Map<Date, List<TmMiddleSalaryEntity>> daySalaryMap = tmAppService.findTmMiddleSalaryList(con).stream()
                .collect(Collectors.groupingBy(TmMiddleSalaryEntity::getSalaryDate));
        for (Map.Entry<Date, List<TmMiddleSalaryEntity>> entry : daySalaryMap.entrySet()) {
            BigDecimal salaryAmount = entry.getValue().stream().map(TmMiddleSalaryEntity::getSalaryAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal tankAmount = entry.getValue().stream().map(TmMiddleSalaryEntity::getTankAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            double amount = salaryAmount.add(tankAmount).doubleValue();
            totalAmount = totalAmount.add(BigDecimal.valueOf(amount));

            Map<String, Object> map = Maps.newHashMap();
            map.put("date", entry.getKey());
            map.put("amount", BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP));
            returnList.add(map);
        }
        Map<String, Object> returnMap = Maps.newLinkedHashMap();
        returnMap.put("salaryList", returnList);
        returnMap.put("totalAmount", totalAmount);
        msg.setData(returnMap);

        return msg;
    }

    /**
     * 司机工资明细（日）
     */
    public ResultMessage driverSalaryDayInfo(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsDriver()) || StringUtils.isBlank(userInfo.getDriver())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        List<Map<String, Object>> salaryList = Lists.newArrayList();
        List<Map<String, Object>> tankList = Lists.newArrayList();
        BigDecimal totalAmount = BigDecimal.ZERO;

        String driverCode = userInfo.getDriver();
        Date searchDate = request.getSearchDate();
        Date salaryDateFm = DateUtil.beginOfDate(searchDate);
        Date salaryDateTo = DateUtil.endOfDate(searchDate);

        TmMiddleSalaryEntity con = new TmMiddleSalaryEntity();
        con.setPersonCode(driverCode);
        con.setSalaryDateFm(salaryDateFm);
        con.setSalaryDateTo(salaryDateTo);
        con.setBaseOrgId(userInfo.getBaseOrgId());
        List<TmMiddleSalaryEntity> tmMiddleSalaryList = tmAppService.findTmMiddleSalaryList(con);
        for (TmMiddleSalaryEntity salaryEntity : tmMiddleSalaryList) {
            Map<String, Object> returnMap = Maps.newLinkedHashMap();
            if (TmsConstants.DRIVER_SALARY_TYPE_TANK.equals(salaryEntity.getSalaryType())) {
                returnMap.put("tankQty", salaryEntity.getTankQty());
                returnMap.put("tankPrice", salaryEntity.getTankPrice());
                returnMap.put("tankAmount", salaryEntity.getTankAmount());
                tankList.add(returnMap);
                totalAmount = totalAmount.add(salaryEntity.getTankAmount());
            } else {
                returnMap.put("salaryType", DictUtils.getDictLabel(salaryEntity.getSalaryType(), "TMS_DRIVER_SALARY_TYPE", "其他"));
                returnMap.put("duration", salaryEntity.getDuration());
                returnMap.put("salaryCoefficient", salaryEntity.getSalaryCoefficient());
                returnMap.put("salaryBase", salaryEntity.getSalaryBase());
                returnMap.put("salaryAmount", salaryEntity.getSalaryAmount());
                salaryList.add(returnMap);
                totalAmount = totalAmount.add(salaryEntity.getSalaryAmount());
            }
        }
        Map<String, Object> returnMap = Maps.newLinkedHashMap();
        returnMap.put("salaryList", salaryList);
        returnMap.put("tankList", tankList);
        returnMap.put("totalAmount", totalAmount);
        msg.setData(returnMap);

        return msg;
    }

    /**
     * 安全检查列表
     */
    public ResultMessage queryCheckList(TmAppOrderQueryRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsSafetyChecker())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        Date now = new Date();
        // 获取出车安全检查表状态
        TmVehicleSafetyCheckEntity condition = new TmVehicleSafetyCheckEntity();
        condition.setIsSign(request.getStatus());
        condition.setBaseOrgId(userInfo.getBaseOrgId());
        condition.setCheckDateFm(DateUtil.subDays(now, 3));
        condition.setCheckDateTo(DateUtil.endOfDate(now));
        List<TmVehicleSafetyCheckEntity> safetyCheckList = tmAppService.findTmVehicleSafetyCheckList(condition);
        for (TmVehicleSafetyCheckEntity entity : safetyCheckList) {
            entity.setIsSign(request.getStatus());
        }
        msg.setData(safetyCheckList);

        return msg;
    }

    /**
     * 出车安全检查确认
     */
    public ResultMessage safetyCheckConfirm(TmAppCreateVehicleSafetyCheckRequest request) {
        ResultMessage msg = new ResultMessage();

        TmAppUserInfo userInfo = tmAppService.getUserByUserId(request.getUserId());
        if (userInfo == null) {
            msg.setSuccess(false);
            msg.setMessage("用户错误，请重新登录！");
            return msg;
        }
        if (!Global.Y.equals(userInfo.getIsSafetyChecker())) {
            msg.setSuccess(false);
            msg.setMessage("用户权限变更，请重新登录！");
            return msg;
        }
        TmVehicleSafetyCheckEntity safetyCheckEntity = tmAppService.getTmVehicleSafetyCheck(request.getId());
        BeanUtils.copyProperties(safetyCheckEntity, request);
        request.setOperator(userInfo.getLoginName());
        request.setSafetySign(userInfo.getName());
        try {
            tmAppService.saveVehicleSafetyCheck(request);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
        }

        return msg;
    }

}