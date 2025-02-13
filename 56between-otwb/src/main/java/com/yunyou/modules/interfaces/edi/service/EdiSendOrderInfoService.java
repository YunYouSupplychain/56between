package com.yunyou.modules.interfaces.edi.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.modules.interfaces.edi.entity.EdiDispatchOrderInfo;
import com.yunyou.modules.interfaces.edi.entity.EdiSendOrderInfo;
import com.yunyou.modules.interfaces.edi.mapper.EdiSendOrderInfoMapper;
import com.yunyou.modules.interfaces.edi.utils.EdiConstant;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EdiSendOrderInfoService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EdiSendOrderInfoMapper mapper;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;

    @Transactional
    public void saveInfo(EdiSendOrderInfo entity) {
        entity.preInsert();
        mapper.save(entity);
    }

    public Page<EdiSendOrderInfo> findPage(Page<EdiSendOrderInfo> page, EdiSendOrderInfo entity) {
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }

    public List<EdiSendOrderInfo> findUnSendDataByType(String ediType) {
        return mapper.findUnSendDataByType(ediType);
    }

    @Transactional
    public void deleteByIds(List<String> ids) {
        mapper.deleteByIds(ids);
    }

    @Transactional
    public void saveByWmsAsn(List<BanQinWmAsnDetailReceiveEntity> receiveEntityList) {
        try {
            List<String> receiveIds = receiveEntityList.stream().map(BanQinWmAsnDetailReceiveEntity::getId).collect(Collectors.toList());
            List<BanQinWmAsnDetailReceiveEntity> list = banQinWmAsnDetailReceiveService.findEntityByIds(receiveIds);
            Office office = officeService.get(list.get(0).getOrgId());
            for (BanQinWmAsnDetailReceiveEntity receiveInfo : list) {
                EdiSendOrderInfo entity = new EdiSendOrderInfo();
                entity.setEdiType(EdiConstant.EDI_TYPE_ASN);
                entity.setIsSend("N");
                entity.setSourceId(receiveInfo.getId());
                entity.setOrgId(receiveInfo.getOrgId());
                entity.setWarehouse(office.getName());
                entity.setOrderNo(receiveInfo.getAsnNo());
                entity.setOrderDate(receiveInfo.getLotAtt03());
                entity.setOwnerCode(receiveInfo.getOwnerCode());
                entity.setOwnerName(receiveInfo.getOwnerName());
                entity.setLineNo(receiveInfo.getLineNo());
                entity.setSkuCode(receiveInfo.getSkuCode());
                entity.setSkuName(receiveInfo.getSkuName());
                entity.setSkuSpec(receiveInfo.getSkuSpec());
                entity.setQtyUnit(receiveInfo.getQtyUnit());
                entity.setQtyEa(receiveInfo.getQtyRcvEa());
                entity.setQtyBox(receiveInfo.getQtyRcvUom());
                entity.setLocCode(receiveInfo.getToLoc());
                entity.setLotNum(receiveInfo.getLotNum());
                entity.setLotAtt01(receiveInfo.getLotAtt01());
                entity.setLotAtt02(receiveInfo.getLotAtt02());
                entity.setLotAtt03(receiveInfo.getLotAtt03());
                entity.setLotAtt04(receiveInfo.getLotAtt04());
                entity.setLotAtt05(receiveInfo.getLotAtt05());
                entity.setLotAtt06(receiveInfo.getLotAtt06());
                entity.setLotAtt07(receiveInfo.getLotAtt07());
                entity.setLotAtt08(receiveInfo.getLotAtt08());
                entity.setLotAtt09(receiveInfo.getLotAtt09());
                entity.setLotAtt10(receiveInfo.getLotAtt10());
                entity.setLotAtt11(receiveInfo.getLotAtt11());
                entity.setLotAtt12(receiveInfo.getLotAtt12());
                entity.setRemarks(receiveInfo.getRemarks());
                this.saveInfo(entity);
            }
        } catch (Exception e) {
            logger.error("EDI接口数据中间表保存报错！" + e.getMessage());
        }
    }

    @Transactional
    public void saveByWmsSo(List<BanQinWmSoAllocEntity> shipEntityList) {
        try {
            List<String> shipIds = shipEntityList.stream().map(BanQinWmSoAllocEntity::getId).collect(Collectors.toList());
            List<BanQinWmSoAllocEntity> list = banQinWmSoAllocService.findEntityByIds(shipIds);
            Office office = officeService.get(list.get(0).getOrgId());
            for (BanQinWmSoAllocEntity shipInfo : list) {
                EdiSendOrderInfo entity = new EdiSendOrderInfo();
                entity.setEdiType(EdiConstant.EDI_TYPE_SO);
                entity.setIsSend("N");
                entity.setSourceId(shipInfo.getId());
                entity.setOrgId(shipInfo.getOrgId());
                entity.setWarehouse(office.getName());
                entity.setOrderNo(shipInfo.getSoNo());
                entity.setOrderDate(shipInfo.getShipTime());
                entity.setOwnerCode(shipInfo.getOwnerCode());
                entity.setOwnerName(shipInfo.getOwnerName());
                entity.setConsigneeCode(shipInfo.getConsigneeCode());
                entity.setConsigneeName(shipInfo.getConsigneeName());
                entity.setLineNo(shipInfo.getLineNo());
                entity.setSkuCode(shipInfo.getSkuCode());
                entity.setSkuName(shipInfo.getSkuName());
                entity.setSkuSpec(shipInfo.getSkuSpec());
                entity.setQtyUnit(shipInfo.getQtyUnit());
                entity.setQtyEa(shipInfo.getQtyEa());
                entity.setQtyBox(shipInfo.getQtyUom());
                entity.setLocCode(shipInfo.getLocCode());
                entity.setLotNum(shipInfo.getLotNum());
                entity.setLotAtt01(shipInfo.getLotAtt01());
                entity.setLotAtt02(shipInfo.getLotAtt02());
                entity.setLotAtt03(shipInfo.getLotAtt03());
                entity.setLotAtt04(shipInfo.getLotAtt04());
                entity.setLotAtt05(shipInfo.getLotAtt05());
                entity.setLotAtt06(shipInfo.getLotAtt06());
                entity.setLotAtt07(shipInfo.getLotAtt07());
                entity.setLotAtt08(shipInfo.getLotAtt08());
                entity.setLotAtt09(shipInfo.getLotAtt09());
                entity.setLotAtt10(shipInfo.getLotAtt10());
                entity.setLotAtt11(shipInfo.getLotAtt11());
                entity.setLotAtt12(shipInfo.getLotAtt12());
                entity.setRemarks(shipInfo.getRemarks());
                this.saveInfo(entity);
            }
        } catch (Exception e) {
            logger.error("EDI接口数据中间表保存报错！" + e.getMessage());
        }
    }

    public List<EdiDispatchOrderInfo> findUnSendDataByDo(String baseOrgId) {
        return mapper.findUnSendDataByDo(baseOrgId);
    }

}
