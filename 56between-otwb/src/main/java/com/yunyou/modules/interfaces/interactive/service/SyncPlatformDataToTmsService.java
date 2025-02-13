package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.modules.tms.basic.entity.*;
import com.yunyou.modules.tms.basic.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.common.entity.*;
import com.yunyou.modules.sys.common.service.SysTmsOutletRelationService;
import com.yunyou.modules.tms.basic.entity.extend.TmBusinessRouteEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportScopeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SyncPlatformDataToTmsService {
    @Autowired
    private SysTmsOutletRelationService sysTmsOutletRelationService;
    @Autowired
    private TmTransportObjService tmTransportObjService;
    @Autowired
    private TmItemService tmItemService;
    @Autowired
    private TmItemBarcodeService tmItemBarcodeService;
    @Autowired
    private TmBusinessRouteService tmBusinessRouteService;
    @Autowired
    private TmCarrierRouteRelationService tmCarrierRouteRelationService;
    @Autowired
    private TmDriverService tmDriverService;
    @Autowired
    private TmDriverQualificationService tmDriverQualificationService;
    @Autowired
    private TmFittingService tmFittingService;
    @Autowired
    private TmTransportEquipmentTypeService tmTransportEquipmentTypeService;
    @Autowired
    private TmTransportEquipmentSpaceService tmTransportEquipmentSpaceService;
    @Autowired
    private TmTransportScopeService tmTransportScopeService;
    @Autowired
    private TmTransportObjScopeService tmTransportObjScopeService;
    @Autowired
    private TmVehicleService tmVehicleService;
    @Autowired
    private TmVehiclePartService tmVehiclePartService;
    @Autowired
    private TmVehicleQualificationService tmVehicleQualificationService;
    @Autowired
    private TmOutletRelationService tmOutletRelationService;

    /**
     * 同步客户
     */
    @Transactional
    public void sync(SysTmsTransportObj entity, String orgId) {
        tmTransportObjService.remove(entity.getTransportObjCode(), orgId);

        TmTransportObj newEntity = new TmTransportObj();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmTransportObjService.save(newEntity);
    }

    /**
     * 同步商品
     */
    @Transactional
    public void sync(SysTmsItem entity, String orgId) {
        tmItemService.remove(entity.getOwnerCode(), entity.getSkuCode(), orgId);

        TmItem newEntity = new TmItem();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmItemService.save(newEntity);
        for (SysTmsItemBarcode o : entity.getBarcodeList()) {
            TmItemBarcode tmItemBarcode = new TmItemBarcode();
            BeanUtils.copyProperties(o, tmItemBarcode);
            tmItemBarcode.setId(null);
            tmItemBarcode.setIsNewRecord(false);
            tmItemBarcode.setOrgId(orgId);
            tmItemBarcodeService.save(tmItemBarcode);
        }
    }

    /**
     * 同步业务路线
     */
    @Transactional
    public void sync(SysTmsBusinessRoute entity, String orgId) {
        tmBusinessRouteService.remove(entity.getCode(), orgId);

        TmBusinessRouteEntity newEntity = new TmBusinessRouteEntity();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmBusinessRouteService.save(newEntity);
    }

    /**
     * 同步承运商路由
     */
    @Transactional
    public void sync(SysTmsCarrierRouteRelation entity, String orgId) {
        tmCarrierRouteRelationService.remove(entity.getOldCarrierCode(), entity.getCode(), orgId);

        TmCarrierRouteRelation newEntity = new TmCarrierRouteRelation();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmCarrierRouteRelationService.save(newEntity);
    }

    /**
     * 同步司机
     */
    @Transactional
    public void sync(SysTmsDriver entity, String orgId) {
        tmDriverService.remove(entity.getCode(), orgId);

        TmDriver newEntity = new TmDriver();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmDriverService.save(newEntity);
        for (SysTmsDriverQualification qualification : entity.getDriverQualificationList()) {
            TmDriverQualification tmDriverQualification = new TmDriverQualification();
            BeanUtils.copyProperties(qualification, tmDriverQualification);
            tmDriverQualification.setId(null);
            tmDriverQualification.setIsNewRecord(false);
            tmDriverQualification.setOrgId(orgId);
            tmDriverQualificationService.save(tmDriverQualification);
        }
    }

    /**
     * 同步备件
     */
    @Transactional
    public void sync(SysTmsFitting entity, String orgId) {
        tmFittingService.remove(entity.getFittingCode(), orgId);

        TmFitting newEntity = new TmFitting();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmFittingService.save(newEntity);
    }

    /**
     * 同步运输设备类型
     */
    @Transactional
    public void sync(SysTmsTransportEquipmentType entity, String orgId) {
        tmTransportEquipmentTypeService.remove(entity.getTransportEquipmentTypeCode(), orgId);

        TmTransportEquipmentType newEntity = new TmTransportEquipmentType();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmTransportEquipmentTypeService.save(newEntity);
        for (SysTmsTransportEquipmentSpace space : entity.getTransportEquipmentSpaceList()) {
            TmTransportEquipmentSpace tmTransportEquipmentSpace = new TmTransportEquipmentSpace();
            BeanUtils.copyProperties(space, tmTransportEquipmentSpace);
            tmTransportEquipmentSpace.setId(null);
            tmTransportEquipmentSpace.setIsNewRecord(false);
            tmTransportEquipmentSpace.setOrgId(orgId);
            tmTransportEquipmentSpaceService.save(tmTransportEquipmentSpace);
        }
    }

    /**
     * 同步业务服务范围
     */
    @Transactional
    public void sync(SysTmsTransportScope entity, String orgId) {
        tmTransportScopeService.remove(entity.getCode(), orgId);

        TmTransportScope newEntity = new TmTransportScope();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmTransportScopeService.save(newEntity);
        tmTransportScopeService.saveArea(new TmTransportScopeEntity(newEntity.getId(), entity.getAreaList()));
    }

    /**
     * 同步业务对象服务范围
     */
    @Transactional
    public void sync(SysTmsTransportObjScope entity, String orgId) {
        tmTransportObjScopeService.remove(entity.getOldTransportObjCode(), entity.getOldTransportScopeCode(), entity.getOldTransportScopeType(), orgId);

        TmTransportObjScope newEntity = new TmTransportObjScope();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmTransportObjScopeService.save(newEntity);
    }

    /**
     * 同步车辆
     */
    @Transactional
    public void sync(SysTmsVehicle entity, String orgId) {
        tmVehicleService.remove(entity.getCarNo(), orgId);

        TmVehicle newEntity = new TmVehicle();
        BeanUtils.copyProperties(entity, newEntity);
        newEntity.setId(null);
        newEntity.setIsNewRecord(false);
        newEntity.setOrgId(orgId);
        tmVehicleService.save(newEntity);
        for (SysTmsVehiclePart part : entity.getVehiclePartList()) {
            TmVehiclePart tmVehiclePart = new TmVehiclePart();
            BeanUtils.copyProperties(part, tmVehiclePart);
            tmVehiclePart.setId(null);
            tmVehiclePart.setIsNewRecord(false);
            tmVehiclePart.setOrgId(orgId);
            tmVehiclePartService.save(tmVehiclePart);
        }
        for (SysTmsVehicleQualification qualification : entity.getVehicleQualificationList()) {
            TmVehicleQualification tmVehicleQualification = new TmVehicleQualification();
            BeanUtils.copyProperties(qualification, tmVehicleQualification);
            tmVehicleQualification.setId(null);
            tmVehicleQualification.setIsNewRecord(false);
            tmVehicleQualification.setOrgId(orgId);
            tmVehicleQualificationService.save(tmVehicleQualification);
        }
    }

    /**
     * 同步网点拓扑图
     */
    @Transactional
    public void sync(SysTmsOutletRelation entity, String orgId) {
        TmOutletRelation tmOutletRelation = null;
        String newParentId = null;
        if (StringUtils.isNotBlank(entity.getOldCode())) {
            List<TmOutletRelation> list = tmOutletRelationService.findList(new TmOutletRelation(entity.getOldCode(), orgId));
            for (TmOutletRelation o : list) {
                String commonParentCodes = sysTmsOutletRelationService.getParentCodes(entity.getOldParentIds());
                String parentCodes = tmOutletRelationService.getParentCodes(o.getParentIds());
                if (commonParentCodes.equals(parentCodes) && entity.getOldCode().equals(o.getCode())) {
                    tmOutletRelation = o;
                    break;
                }
            }
        }
        if (!"0".equals(entity.getParentId())) {
            SysTmsOutletRelation parent = sysTmsOutletRelationService.get(entity.getParentId());
            List<TmOutletRelation> list = tmOutletRelationService.findList(new TmOutletRelation(parent.getCode(), orgId));
            for (TmOutletRelation o : list) {
                String commonParentCodes = sysTmsOutletRelationService.getParentCodes(entity.getParentIds());
                String parentCodes = tmOutletRelationService.getParentCodes(o.getParentIds());
                if (commonParentCodes.equals(parentCodes) && parent.getCode().equals(o.getCode())) {
                    newParentId = o.getParentIds() + o.getId() + ",";
                    break;
                }
            }
        }
        if (tmOutletRelation == null) {
            tmOutletRelation = new TmOutletRelation();
        }
        if (StringUtils.isNotBlank(newParentId)) {
            tmOutletRelation.setParent(new TmOutletRelation(newParentId));
        }
        tmOutletRelation.setCode(entity.getCode());
        tmOutletRelation.setName(entity.getName());
        tmOutletRelation.setOrgId(orgId);
        tmOutletRelationService.save(tmOutletRelation);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysTmsTransportObj... list) {
        List<TmTransportObj> tmTransportObjList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysTmsTransportObj entity : list) {
                TmTransportObj tmTransportObj = new TmTransportObj();
                BeanUtils.copyProperties(entity, tmTransportObj);
                tmTransportObj.setId(IdGen.uuid());
                tmTransportObj.setUpdateDate(date);
                tmTransportObj.setOrgId(orgId);
                tmTransportObjList.add(tmTransportObj);
            }
        }
        tmTransportObjService.batchInsert(tmTransportObjList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysTmsItem... list) {
        List<TmItem> tmItemList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            for (SysTmsItem entity : list) {
                TmItem tmItem = new TmItem();
                BeanUtils.copyProperties(entity, tmItem);
                tmItem.setId(IdGen.uuid());
                tmItem.setUpdateDate(date);
                tmItem.setOrgId(orgId);
                tmItemList.add(tmItem);
            }
        }
        tmItemService.batchInsert(tmItemList);
    }

    /**
     * 仅供批量新增且确认数据不会出现重复同步使用
     */
    @Transactional
    public void syncInsert(List<String> orgIds, SysTmsOutletRelation... list) {
        List<TmOutletRelation> tmOutletRelationList = Lists.newArrayList();
        Date date = new Date();

        for (String orgId : orgIds) {
            tmOutletRelationService.removeAll(orgId);
            Map<String, TmOutletRelation> rsMap = Maps.newHashMap();
            rsMap.put("0,", new TmOutletRelation());

            List<SysTmsOutletRelation> collect = Arrays.asList(list);
            Map<String, List<SysTmsOutletRelation>> map = collect.stream().collect(Collectors.groupingBy(SysTmsOutletRelation::getParentIds));
            map.keySet().stream().sorted(Comparator.comparingInt(String::length)).forEach(parentIds -> {
                List<SysTmsOutletRelation> relations = map.get(parentIds);
                TmOutletRelation parent = rsMap.get(parentIds);
                for (SysTmsOutletRelation relation : relations) {
                    TmOutletRelation tmOutletRelation = new TmOutletRelation();
                    tmOutletRelation.setId(IdGen.uuid());
                    tmOutletRelation.setCreateBy(relation.getCreateBy());
                    tmOutletRelation.setCreateDate(relation.getCreateDate());
                    tmOutletRelation.setUpdateBy(relation.getUpdateBy());
                    tmOutletRelation.setUpdateDate(date);
                    tmOutletRelation.setRecVer(relation.getRecVer());
                    tmOutletRelation.setCode(relation.getCode());
                    tmOutletRelation.setName(relation.getName());
                    tmOutletRelation.setSort(relation.getSort());
                    tmOutletRelation.setParent(parent);
                    tmOutletRelation.setParentIds(StringUtils.isBlank(parent.getParentIds()) ? "0," : parent.getParentIds() + parent.getId() + ",");
                    tmOutletRelation.setOrgId(orgId);
                    tmOutletRelationList.add(tmOutletRelation);
                    rsMap.put(parentIds + relation.getId() + ",", tmOutletRelation);
                }
            });
        }
        tmOutletRelationService.batchInsert(tmOutletRelationList);
    }

    @Transactional
    public void removeCustomer(String customerNo, String orgId) {
        tmTransportObjService.remove(customerNo, orgId);
    }

    @Transactional
    public void removeSku(String ownerCode, String skuCode, String orgId) {
        tmItemService.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void removeCarrierRouteRelation(String carrierCode, String code, String orgId) {
        tmCarrierRouteRelationService.remove(carrierCode, code, orgId);
    }

    @Transactional
    public void removeDriver(String code, String orgId) {
        tmDriverService.remove(code, orgId);
    }

    @Transactional
    public void removeFitting(String fittingCode, String orgId) {
        tmFittingService.remove(fittingCode, orgId);
    }

    @Transactional
    public void removeEquipmentType(String transportEquipmentTypeCode, String orgId) {
        tmTransportEquipmentTypeService.remove(transportEquipmentTypeCode, orgId);
    }

    @Transactional
    public void removeTransportObjScope(String transportObjCode, String transportScopeCode, String transportScopeType, String orgId) {
        tmTransportObjScopeService.remove(transportObjCode, transportScopeCode, transportScopeType, orgId);
    }

    @Transactional
    public void removeTransportScope(String code, String orgId) {
        tmTransportScopeService.remove(code, orgId);
    }

    @Transactional
    public void removeVehicle(String carNo, String orgId) {
        tmVehicleService.remove(carNo, orgId);
    }

    @Transactional
    public void removeBusinessRoute(String code, String orgId) {
        tmBusinessRouteService.remove(code, orgId);
    }

    @Transactional
    public void removeOutletRelation(String code, String parentIds, String orgId) {
        List<TmOutletRelation> list = tmOutletRelationService.findList(new TmOutletRelation(code, orgId));
        for (TmOutletRelation o : list) {
            String commonParentCodes = sysTmsOutletRelationService.getParentCodes(parentIds);
            String parentCodes = tmOutletRelationService.getParentCodes(o.getParentIds());
            // 找出机构中的该节点
            if (commonParentCodes.equals(parentCodes) && code.equals(o.getCode())) {
                tmOutletRelationService.delete(o);
                break;
            }
        }
    }
}
