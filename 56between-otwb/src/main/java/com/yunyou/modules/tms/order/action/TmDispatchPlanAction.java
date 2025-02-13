package com.yunyou.modules.tms.order.action;

import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.core.action.BaseAction;
import com.yunyou.core.persistence.Page;
import com.yunyou.modules.tms.basic.entity.TmTransportObj;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.entity.extend.TmVehicleEntity;
import com.yunyou.modules.tms.basic.service.TmTransportObjService;
import com.yunyou.modules.tms.basic.service.TmVehicleService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsUtil;
import com.yunyou.modules.tms.order.entity.TmDispatchPlanVehicle;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanConfigEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanEntity;
import com.yunyou.modules.tms.order.entity.extend.TmDispatchPlanVehicleEntity;
import com.yunyou.modules.tms.order.manager.TmDispatchPlanManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 调度计划业务处理类(非事务管理)
 */
@Service
public class TmDispatchPlanAction extends BaseAction {
    @Autowired
    private TmDispatchPlanManager tmDispatchPlanManager;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private TmVehicleService tmVehicleService;

    public Page<TmDispatchPlanEntity> findPage(Page<TmDispatchPlanEntity> page, TmDispatchPlanEntity entity) {
        return tmDispatchPlanManager.findPage(page, entity);
    }

    public Page<TmDispatchPlanDetailEntity> findResultPage(Page page, TmDispatchPlanDetailEntity entity) {
        entity.setPage(page);
        List<TmDispatchPlanDetailEntity> list = tmDispatchPlanManager.getDetailList(entity);
        // 按照客户商品合并
        Map<String, List<TmDispatchPlanDetailEntity>> collect = list.stream().collect(Collectors.groupingBy(s -> s.getOwnerCode() + s.getSkuCode()));
        List<TmDispatchPlanDetailEntity> result = ListUtil.newArrayList();
        for (List<TmDispatchPlanDetailEntity> detailList : collect.values()) {
            BigDecimal sumPlanQty = detailList.stream().map(TmDispatchPlanDetailEntity::getPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            TmDispatchPlanDetailEntity detailEntity = detailList.get(0);
            detailEntity.setPlanQty(sumPlanQty);
            result.add(detailEntity);
        }
        // 计算调度量
        for (TmDispatchPlanDetailEntity detail : result) {
            List<TmDispatchPlanConfigEntity> configList = tmDispatchPlanManager.getConfigByPlanNoAndSku(detail.getPlanNo(), detail.getOwnerCode(), detail.getSkuCode(), detail.getOrgId(), detail.getBaseOrgId());
            BigDecimal dispatchQty = configList.stream().map(TmDispatchPlanConfigEntity::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            detail.setDispatchQty(dispatchQty);
            detail.setUnDispatchQty(detail.getPlanQty().subtract(dispatchQty));
        }
        // 按照分类、客户、商品排序
        result.sort(Comparator.comparing(TmDispatchPlanDetailEntity::getClassification, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(TmDispatchPlanDetailEntity::getOwnerCode).thenComparing(TmDispatchPlanDetailEntity::getSkuCode));
        page.setList(result);
        return page;
    }

    public Page<TmDispatchPlanDetailEntity> findDetailPage(Page page, TmDispatchPlanDetailEntity entity) {
        entity.setPage(page);
        List<TmDispatchPlanDetailEntity> list = tmDispatchPlanManager.getDetailList(entity);
        // 按照分类、到达时间、客户、商品排序
        list.sort(Comparator.comparing(TmDispatchPlanDetailEntity::getClassification, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(TmDispatchPlanDetailEntity::getOwnerCode).thenComparing(TmDispatchPlanDetailEntity::getSkuCode));
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchPlanDetailEntity> findCustomerPage(Page page, TmDispatchPlanDetailEntity entity) {
        entity.setPage(page);
        entity.setIsOwner(Global.Y);
        List<TmDispatchPlanDetailEntity> list = tmDispatchPlanManager.getDetailGrid(entity);
        list = list.stream().filter(distinctByKey(TmDispatchPlanDetailEntity::getOwnerCode)).collect(toList());
        page.setCount(list.size());
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchPlanDetailEntity> findSkuPage(Page page, TmDispatchPlanDetailEntity entity) {
        entity.setPage(page);
        entity.setIsSku(Global.Y);
        List<TmDispatchPlanDetailEntity> list = tmDispatchPlanManager.getDetailGrid(entity);
        list = list.stream().filter(distinctByKey(s -> s.getOwnerCode() + s.getSkuCode())).collect(toList());
        page.setCount(list.size());
        page.setList(list);
        return page;
    }

    @SuppressWarnings("unchecked")
    public Page<TmDispatchPlanVehicleEntity> findVehiclePage(Page page, TmDispatchPlanVehicleEntity entity) {
        entity.setPage(page);
        page.setList(tmDispatchPlanManager.getVehicleGrid(entity));
        return page;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public ResultMessage createInitPlan(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            msg = tmDispatchPlanManager.createInitPlan(entity);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期");
        }
        return msg;
    }

    public ResultMessage removeEntitys(String ids) {
        ResultMessage msg = new ResultMessage("操作成功");
        String[] idArray = ids.split(",");
        int i = 0;
        for (String id : idArray) {
            TmDispatchPlanEntity entity = tmDispatchPlanManager.getEntity(id);
            if (null == entity) continue;
            if (!entity.getStatus().equals("00")) {
                i++;
                continue;
            }
            tmDispatchPlanManager.removeEntity(entity);
        }
        if (i > 0) {
            msg.setMessage("操作成功！其中有" + i + "条记录不是创建状态，无法删除！");
        }

        return msg;
    }

    /**
     * 根据条件过滤调度需求计划信息
     */
    public ResultMessage getDispatchInfoByLine(TmDispatchPlanDetailEntity entity) {
        ResultMessage msg = new ResultMessage();
        if (StringUtils.isNotBlank(entity.getLineCode())) {
            List<String> customerList = ListUtil.newArrayList("#");
            for (String line : entity.getLineCode().split(",")) {
                List<TmTransportObj> obj = tmTransportObjService.findCustomerMatchObjByLine(line, entity.getBaseOrgId());
                List<String> collect = obj.stream().map(TmTransportObj::getTransportObjCode).distinct().collect(toList());
                customerList.addAll(collect);
            }
            entity.setCustomerCodeList(customerList);
        }
        List<TmDispatchPlanConfigEntity> result = ListUtil.newArrayList();
        List<TmDispatchPlanDetailEntity> list = tmDispatchPlanManager.getDetailList(entity);
        for (TmDispatchPlanDetailEntity detailEntity : list) {
            TmDispatchPlanConfigEntity configEntity = new TmDispatchPlanConfigEntity();
            BeanUtils.copyProperties(detailEntity, configEntity);
            configEntity.setDeliveryPoint(detailEntity.getOwnerCode());
            configEntity.setDeliveryPointName(detailEntity.getOwnerName());
            configEntity.setId(null);
            configEntity.setIsNewRecord(false);
            configEntity.setRecVer(0);
            result.add(configEntity);
        }

        // 根据到达时间排序
        result.sort(Comparator.comparing(TmDispatchPlanConfigEntity::getArrivalTime));
        msg.setData(result);
        return msg;
    }

    public ResultMessage getDispatchInfo(String planNo, String orgId, String baseOrgId) {
        ResultMessage msg = new ResultMessage();
        List<TmDispatchPlanConfigEntity> result = ListUtil.newArrayList();
        // 判断是否调度过
        List<String> vehicleNoList = tmDispatchPlanManager.getVehicleByPlanNo(planNo, orgId);
        if (CollectionUtil.isNotEmpty(vehicleNoList)) {
            List<TmDispatchPlanConfigEntity> list = tmDispatchPlanManager.getConfigByPlanNo(planNo, orgId, baseOrgId);
            if (CollectionUtil.isNotEmpty(list)) {
                // 按车号、车次、仓室排序
                list.sort(Comparator.comparing(TmDispatchPlanConfigEntity::getVehicleNo).thenComparing(TmDispatchPlanConfigEntity::getTrip).thenComparing(TmDispatchPlanConfigEntity::getWareRoom));
                // 先按车号分组
                LinkedHashMap<String, List<TmDispatchPlanConfigEntity>> carMap = list.stream().collect(Collectors.groupingBy(TmDispatchPlanConfigEntity::getVehicleNo, LinkedHashMap::new, toList()));
                carMap.forEach((carNo, carList) -> {
                    // 再按车次分组
                    LinkedHashMap<String, List<TmDispatchPlanConfigEntity>> tripMap = carList.stream().collect(Collectors.groupingBy(TmDispatchPlanConfigEntity::getTrip, LinkedHashMap::new, toList()));
                    for (int i = 1; i <= 4; i++) {
                        String trip = String.valueOf(i);
                        if (!tripMap.containsKey(trip)) {
                            result.addAll(getNullTrip(carNo, i, i, baseOrgId));
                            continue;
                        }
                        // 继续判断仓室
                        Map<String, TmDispatchPlanConfigEntity> wareRoomMap = tripMap.get(trip).stream().collect(Collectors.toMap(TmDispatchPlanConfigEntity::getWareRoom, Function.identity()));
                        TmVehicleEntity tmVehicleEntity = tmVehicleService.getEntity(carNo, baseOrgId);
                        for (int j = 1; j <= 4; j++) {
                            String wareRoom = String.valueOf(j);
                            if (wareRoomMap.containsKey(wareRoom)) {
                                result.add(wareRoomMap.get(wareRoom));
                                continue;
                            }
                            TmDispatchPlanConfigEntity config = new TmDispatchPlanConfigEntity();
                            config.setVehicleNo(carNo);
                            config.setTrip(trip);
                            config.setWareRoom(wareRoom);
                            if (tmVehicleEntity != null) {
                                config.setVehicleType(tmVehicleEntity.getVehicleType());
                                config.setVehicleTypeDesc(tmVehicleEntity.getVehicleTypeDesc());
                                config.setEquipmentTypeCode(tmVehicleEntity.getTransportEquipmentTypeCode());
                                config.setEquipmentTypeName(tmVehicleEntity.getTransportEquipmentTypeName());
                            }
                            result.add(config);
                        }
                    }
                });
            }
            // 取差集，计算暂时未配载的车辆
            List<String> hasDispatch = list.stream().map(TmDispatchPlanConfigEntity::getVehicleNo).distinct().collect(toList());
            List<String> reduce = vehicleNoList.stream().filter(item -> !hasDispatch.contains(item)).collect(toList());
            for (String vehicleNo : reduce) {
                result.addAll(getNullTrip(vehicleNo, 1, 4, baseOrgId));
            }
            // 按车号、车次、仓室排序
            result.sort(Comparator.comparing(TmDispatchPlanConfigEntity::getVehicleNo).thenComparing(TmDispatchPlanConfigEntity::getTrip).thenComparing(TmDispatchPlanConfigEntity::getWareRoom));
            msg.setData(result);
            return msg;
        }

        TmDispatchPlanEntity headerEntity = tmDispatchPlanManager.getHeaderByPlanNo(planNo, orgId, baseOrgId);
        if (null != headerEntity) {
            List<TmVehicle> vehicleList = tmVehicleService.getEnableList(baseOrgId);
            // 过滤承运商是null和承运商是当前选择的
            vehicleList = vehicleList.stream().filter(s -> StringUtils.isEmpty(s.getCarrierCode()) || headerEntity.getCarrierCode().equals(s.getCarrierCode())).collect(toList());
            List<TmDispatchPlanVehicle> vehicleSaveList = ListUtil.newArrayList();
            for (TmVehicle vehicle : vehicleList) {
                result.addAll(getNullTrip(vehicle.getCarNo(), 1, 4, baseOrgId));
                TmDispatchPlanVehicle entity = new TmDispatchPlanVehicle();
                entity.setPlanNo(planNo);
                entity.setVehicleNo(vehicle.getCarNo());
                entity.setOrgId(orgId);
                entity.setBaseOrgId(baseOrgId);
                vehicleSaveList.add(entity);
            }
            if (CollectionUtil.isNotEmpty(vehicleSaveList)) {
                tmDispatchPlanManager.saveVehicleAll(vehicleSaveList);
            }
        }

        // 按车号、车次、仓室排序
        result.sort(Comparator.comparing(TmDispatchPlanConfigEntity::getVehicleNo).thenComparing(TmDispatchPlanConfigEntity::getTrip).thenComparing(TmDispatchPlanConfigEntity::getWareRoom));
        msg.setData(result);
        return msg;
    }

    private List<TmDispatchPlanConfigEntity> getNullTrip(String vehicleNo, int start, int trip, String baseOrgId) {
        List<TmDispatchPlanConfigEntity> result = ListUtil.newArrayList();
        TmVehicleEntity tmVehicleEntity = tmVehicleService.getEntity(vehicleNo, baseOrgId);
        for (int i = start; i <= trip; i++) {
            for (int j = 1; j <= 4; j++) {
                TmDispatchPlanConfigEntity config = new TmDispatchPlanConfigEntity();
                config.setVehicleNo(vehicleNo);
                config.setTrip(i + "");
                config.setWareRoom(j + "");
                if (tmVehicleEntity != null) {
                    config.setVehicleType(tmVehicleEntity.getVehicleType());
                    config.setVehicleTypeDesc(tmVehicleEntity.getVehicleTypeDesc());
                    config.setEquipmentTypeCode(tmVehicleEntity.getTransportEquipmentTypeCode());
                    config.setEquipmentTypeName(tmVehicleEntity.getTransportEquipmentTypeName());
                }
                result.add(config);
            }
        }

        return result;
    }

    public List<TmDispatchPlanConfigEntity> getConfigInfo(TmDispatchPlanConfigEntity entity) {
        List<TmDispatchPlanConfigEntity> result = ListUtil.newArrayList();
        List<TmDispatchPlanConfigEntity> configList = tmDispatchPlanManager.getConfigList(entity);
        // 过滤已经审核的车次
        configList = configList.stream().filter(s -> StringUtils.isBlank(s.getTransportNo()) && StringUtils.isBlank(s.getDispatchNo())).collect(toList());
        if (CollectionUtil.isNotEmpty(configList)) {
            if (StringUtils.isBlank(entity.getDeliveryPoint()) && StringUtils.isBlank(entity.getSkuCode()) && StringUtils.isBlank(entity.getWareRoom()) &&
                    StringUtils.isBlank(entity.getPickUpPoint()) && StringUtils.isBlank(entity.getLineCode()) && StringUtils.isBlank(entity.getDriver()) && StringUtils.isBlank(entity.getEscort())) {
                // 按车号、仓室排序
                configList.sort(Comparator.comparing(TmDispatchPlanConfigEntity::getVehicleNo).thenComparing(TmDispatchPlanConfigEntity::getWareRoom));
                // 先按车号分组
                LinkedHashMap<String, List<TmDispatchPlanConfigEntity>> carMap = configList.stream().collect(Collectors.groupingBy(TmDispatchPlanConfigEntity::getVehicleNo, LinkedHashMap::new, toList()));
                carMap.forEach((carNo, carList) -> {
                    TmDispatchPlanConfigEntity tripEntity = carList.get(0);
                    // 继续判断仓室
                    Map<String, TmDispatchPlanConfigEntity> wareRoomMap = carList.stream().collect(Collectors.toMap(TmDispatchPlanConfigEntity::getWareRoom, Function.identity()));
                    for (int j = 1; j <= 4; j++) {
                        String wareRoom = String.valueOf(j);
                        if (wareRoomMap.containsKey(wareRoom)) {
                            result.add(wareRoomMap.get(wareRoom));
                            continue;
                        }
                        TmDispatchPlanConfigEntity config = new TmDispatchPlanConfigEntity();
                        config.setVehicleNo(carNo);
                        config.setTrip(tripEntity.getTrip());
                        config.setWareRoom(wareRoom);
                        config.setRecVer(0);
                        config.setPlanNo(tripEntity.getPlanNo());
                        config.setOrgId(tripEntity.getOrgId());
                        config.setBaseOrgId(tripEntity.getBaseOrgId());
                        config.setVehicleType(tripEntity.getVehicleType());
                        config.setVehicleTypeDesc(tripEntity.getVehicleTypeDesc());
                        config.setEquipmentTypeCode(tripEntity.getEquipmentTypeCode());
                        config.setEquipmentTypeName(tripEntity.getEquipmentTypeName());
                        result.add(config);
                    }
                });
            } else {
                return configList;
            }
        }

        return result;
    }

    public List<TmDispatchPlanConfigEntity> getWareRoomConfigInfo(String planNo, String vehicleNo, String trip, String orgId, String baseOrgId) {
        List<TmDispatchPlanConfigEntity> result = ListUtil.newArrayList();
        List<TmDispatchPlanConfigEntity> list = tmDispatchPlanManager.getConfigByPlanNoAndCarNoAndTrip(planNo, vehicleNo, trip, orgId, baseOrgId);
        TmVehicleEntity tmVehicleEntity = tmVehicleService.getEntity(vehicleNo, baseOrgId);
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, TmDispatchPlanConfigEntity> map = list.stream().collect(Collectors.toMap(TmDispatchPlanConfigEntity::getWareRoom, Function.identity()));
            for (int i = 1; i <= 4; i++) {
                String wareRoom = String.valueOf(i);
                if (map.containsKey(wareRoom)) {
                    result.add(map.get(wareRoom));
                } else {
                    TmDispatchPlanConfigEntity config = new TmDispatchPlanConfigEntity();
                    config.setVehicleNo(vehicleNo);
                    config.setTrip(trip);
                    config.setWareRoom(wareRoom);
                    config.setOrgId(orgId);
                    config.setBaseOrgId(baseOrgId);
                    config.setPlanNo(planNo);
                    if (tmVehicleEntity != null) {
                        config.setVehicleType(tmVehicleEntity.getVehicleType());
                        config.setVehicleTypeDesc(tmVehicleEntity.getVehicleTypeDesc());
                        config.setEquipmentTypeCode(tmVehicleEntity.getTransportEquipmentTypeCode());
                        config.setEquipmentTypeName(tmVehicleEntity.getTransportEquipmentTypeName());
                    }
                    result.add(config);
                }
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                TmDispatchPlanConfigEntity config = new TmDispatchPlanConfigEntity();
                config.setVehicleNo(vehicleNo);
                config.setTrip(trip);
                config.setWareRoom(String.valueOf(i));
                config.setOrgId(orgId);
                config.setBaseOrgId(baseOrgId);
                config.setPlanNo(planNo);
                if (tmVehicleEntity != null) {
                    config.setVehicleType(tmVehicleEntity.getVehicleType());
                    config.setVehicleTypeDesc(tmVehicleEntity.getVehicleTypeDesc());
                    config.setEquipmentTypeCode(tmVehicleEntity.getTransportEquipmentTypeCode());
                    config.setEquipmentTypeName(tmVehicleEntity.getTransportEquipmentTypeName());
                }
                result.add(config);
            }
        }

        return result;
    }

    public ResultMessage dispatchAll(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            // 过滤空数据
            List<TmDispatchPlanConfigEntity> detailList = entity.getDetailList();
            List<TmDispatchPlanConfigEntity> collect = detailList.stream().filter(s -> null != s.getQty() && StringUtils.isNotBlank(s.getVehicleNo()) && StringUtils.isNotBlank(s.getWareRoom()) && StringUtils.isNotBlank(s.getPickUpPoint()) && StringUtils.isNotBlank(s.getTrip())).collect(toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                tmDispatchPlanManager.dispatchAll(collect);
            }
        } catch (DuplicateKeyException e) {
            msg.addMessage("[车号、车次、客户、商品、仓室]出现重复数据，无法配载");
        } catch (GlobalException e) {
            msg.addMessage(e.getMessage());
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            if (msg.getMessage().contains(TmsConstants.DATA_HAS_EXPIRED)) {
                msg.setMessage(TmsUtil.getMessage(msg.getMessage()) + TmsConstants.DATA_HAS_EXPIRED);
            }
            return msg;
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage saveEditConfig(List<TmDispatchPlanConfigEntity> list) {
        ResultMessage msg = new ResultMessage();
        try {
            tmDispatchPlanManager.saveEditConfig(list);
        } catch (DuplicateKeyException e) {
            msg.addMessage("[车号、车次、提货点、商品、仓室]出现重复数据，无法配载");
        } catch (GlobalException e) {
            msg.addMessage(e.getMessage());
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            if (msg.getMessage().contains(TmsConstants.DATA_HAS_EXPIRED)) {
                msg.setMessage(TmsUtil.getMessage(msg.getMessage()) + TmsConstants.DATA_HAS_EXPIRED);
            }
            return msg;
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage deleteConfig(String configId) {
        ResultMessage msg = new ResultMessage();
        TmDispatchPlanConfigEntity configEntity = tmDispatchPlanManager.getConfigEntity(configId);
        if (null == configEntity) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期！");
            return msg;
        }
        if (StringUtils.isNotBlank(configEntity.getTransportNo()) || StringUtils.isNotBlank(configEntity.getDispatchNo())) {
            msg.setSuccess(false);
            msg.setMessage("该条调度结果已审核，无法删除！");
            return msg;
        }
        tmDispatchPlanManager.removeConfigEntity(configEntity);
        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage audit(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        String[] tripArray = entity.getVehicleAndTrips().split(",");
        int nullCount = 0;
        for (String trips : tripArray) {
            String[] vehicle = trips.split("@");
            // 先查找车次配置信息
            List<TmDispatchPlanConfigEntity> list = tmDispatchPlanManager.getConfigByPlanNoAndCarNoAndTrip(entity.getPlanNo(), vehicle[0], vehicle[1], entity.getOrgId(), entity.getBaseOrgId());
            if (CollectionUtil.isEmpty(list)) {
                nullCount++;
                continue;
            }
            try {
                tmDispatchPlanManager.audit(list);
            } catch (GlobalException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (nullCount == tripArray.length) {
            msg.setSuccess(false);
            msg.setMessage("该单没有调度数据，无法审核");
            return msg;
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            if (msg.getMessage().contains(TmsConstants.DATA_HAS_EXPIRED)) {
                msg.setMessage(TmsUtil.getMessage(msg.getMessage()) + TmsConstants.DATA_HAS_EXPIRED);
            }
            return msg;
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage cancelAudit(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        String[] tripArray = entity.getVehicleAndTrips().split(",");
        int nullCount = 0;
        for (String trips : tripArray) {
            String[] vehicle = trips.split("@");
            // 先查找车次配置信息
            List<TmDispatchPlanConfigEntity> list = tmDispatchPlanManager.getConfigByPlanNoAndCarNoAndTrip(entity.getPlanNo(), vehicle[0], vehicle[1], entity.getOrgId(), entity.getBaseOrgId());
            if (CollectionUtil.isEmpty(list)) {
                nullCount++;
                continue;
            }
            try {
                tmDispatchPlanManager.cancelAudit(list);
            } catch (GlobalException e) {
                msg.addMessage(e.getMessage());
            }
        }
        if (nullCount == tripArray.length) {
            msg.setSuccess(false);
            msg.setMessage("该单没有调度数据，无法取消审核");
            return msg;
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            if (msg.getMessage().contains(TmsConstants.DATA_HAS_EXPIRED)) {
                msg.setMessage(TmsUtil.getMessage(msg.getMessage()) + TmsConstants.DATA_HAS_EXPIRED);
            }
            return msg;
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage addDemandPlan(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            tmDispatchPlanManager.addDemandPlan(entity);
        } catch (GlobalException e) {
            msg.addMessage(e.getMessage());
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            if (msg.getMessage().contains(TmsConstants.DATA_HAS_EXPIRED)) {
                msg.setMessage(TmsUtil.getMessage(msg.getMessage()) + TmsConstants.DATA_HAS_EXPIRED);
            }
            return msg;
        }

        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage addVehicle(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        try {
            tmDispatchPlanManager.addVehicle(entity);
        } catch (DuplicateKeyException e) {
            msg.addMessage(e.getMessage());
        }
        if (StringUtils.isNotBlank(msg.getMessage())) {
            msg.setSuccess(false);
            msg.setMessage("车牌号已经添加，不能再重复添加！");
            return msg;
        }
        msg.setMessage("操作成功");
        return msg;
    }

    public ResultMessage recall(TmDispatchPlanEntity entity) {
        ResultMessage msg = new ResultMessage();
        String[] vehicle = entity.getVehicleAndTrips().split("@");
        String planNo = entity.getPlanNo();
        String vehicleNo = vehicle[0];
        String trip = vehicle[1];
        String orgId = entity.getOrgId();
        String baseOrgId = entity.getBaseOrgId();

        try {
            tmDispatchPlanManager.recall(planNo, vehicleNo, trip, orgId, baseOrgId);
        } catch (GlobalException e) {
            msg.setSuccess(false);
            msg.setMessage(e.getMessage());
            return msg;
        }
        msg.setMessage("操作成功");
        return msg;
    }
}
