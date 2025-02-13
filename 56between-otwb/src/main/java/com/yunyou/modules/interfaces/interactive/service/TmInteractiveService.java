package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.interfaces.kdBest.entity.kdInspectionSubmit.request.KdInspectionSubmitReq;
import com.yunyou.modules.interfaces.kdBest.service.KdBestInterfaceService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoAllocService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TmInteractiveService {
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;
    @Autowired
    private KdBestInterfaceService kdBestInterfaceService;

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void kdBestInspectionSubmit(String mailNo, String orgId, String url, String params) {
        BanQinWmSoAllocEntity condition = new BanQinWmSoAllocEntity();
        condition.setTrackingNo(mailNo);
        condition.setOrgId(orgId);
        List<BanQinWmSoAllocEntity> allocList = banQinWmSoAllocService.findByEntity(condition);
        if (CollectionUtil.isNotEmpty(allocList)) {
            String soNo = allocList.get(0).getSoNo();
            BanQinWmSoEntity wmSoEntity = banQinWmSoHeaderService.findEntityBySoNo(soNo, orgId);
            KdInspectionSubmitReq request = new KdInspectionSubmitReq();
            request.setMailNo(mailNo);
            request.setAcceptManName(wmSoEntity.getContactName());
            request.setAcceptManPhone(wmSoEntity.getContactTel());
            String[] receiveArea = wmSoEntity.getDef17().split(":", -1);
            request.setAcceptProvinceName(receiveArea[0]);
            request.setAcceptCityName(receiveArea[1]);
            request.setAcceptCountyName(receiveArea[2]);
            request.setAcceptManAddress(receiveArea[0] + receiveArea[1] + receiveArea[2] + wmSoEntity.getContactAddr());

            request.setSendManName(wmSoEntity.getOwnerShortName());
            String[] sendArea = wmSoEntity.getOwnerArea().split(":", -1);
            request.setSendProvinceName(sendArea[0]);
            request.setSendCityName(sendArea[1]);
            request.setSendCountyName(sendArea[2]);
            kdBestInterfaceService.kdInspectionSubmit(request, url, params);
        }
    }
}
