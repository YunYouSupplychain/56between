package com.yunyou.modules.wms.crossDock.entity;

import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;

import java.util.List;

/**
 * 越库入库单列表Entity
 * @author WMJ
 * @version 2020-02-17
 */
public class BanQinWmCrossDockDetailEntity {
    private List<BanQinGetCrossDockAsnDetailQueryEntity> getCrossDockAsnDetailQueryItem;
    private List<BanQinGetCrossDockSoDetailQueryEntity> getCrossDockSoDetailQueryItem;

    private List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity;
    private List<BanQinWmSoDetailEntity> wmSoDetailEntity;

    public List<BanQinGetCrossDockAsnDetailQueryEntity> getGetCrossDockAsnDetailQueryItem() {
        return getCrossDockAsnDetailQueryItem;
    }

    public void setGetCrossDockAsnDetailQueryItem(List<BanQinGetCrossDockAsnDetailQueryEntity> getCrossDockAsnDetailQueryItem) {
        this.getCrossDockAsnDetailQueryItem = getCrossDockAsnDetailQueryItem;
    }

    public List<BanQinGetCrossDockSoDetailQueryEntity> getGetCrossDockSoDetailQueryItem() {
        return getCrossDockSoDetailQueryItem;
    }

    public void setGetCrossDockSoDetailQueryItem(List<BanQinGetCrossDockSoDetailQueryEntity> getCrossDockSoDetailQueryItem) {
        this.getCrossDockSoDetailQueryItem = getCrossDockSoDetailQueryItem;
    }

    public List<BanQinWmAsnDetailReceiveEntity> getWmAsnDetailReceiveEntity() {
        return wmAsnDetailReceiveEntity;
    }

    public void setWmAsnDetailReceiveEntity(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity) {
        this.wmAsnDetailReceiveEntity = wmAsnDetailReceiveEntity;
    }

    public List<BanQinWmSoDetailEntity> getWmSoDetailEntity() {
        return wmSoDetailEntity;
    }

    public void setWmSoDetailEntity(List<BanQinWmSoDetailEntity> wmSoDetailEntity) {
        this.wmSoDetailEntity = wmSoDetailEntity;
    }
}
