package com.yunyou.modules.wms.crossDock.entity;

import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;

import java.util.List;

/**
 * 越库入库单列表Entity
 * @author WMJ
 * @version 2020-02-17
 */
public class BanQinWmReceiveEntity extends BanQinWmAsnDetailReceiveQueryEntity {
    private List<BanQinWmSoAllocEntity> wmSoAllocEntity;
    private List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntity;
    private List<BanQinWmSoDetailEntity> wmSoDetailEntity;

    public List<BanQinWmSoAllocEntity> getWmSoAllocEntity() {
        return wmSoAllocEntity;
    }

    public void setWmSoAllocEntity(List<BanQinWmSoAllocEntity> wmSoAllocEntity) {
        this.wmSoAllocEntity = wmSoAllocEntity;
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
