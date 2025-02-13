package com.yunyou.modules.wms.outbound.entity;

import java.util.List;

/**
 * 描述：波次单扩展Entity
 *
 * @auther: Jianhua on 2019/2/14
 */
public class BanQinWmWvEntity extends BanQinWmWvHeader {
    // 发运单列表
    private List<BanQinWmSoEntity> soHeaderList;
    // 波次明细列表
    private List<BanQinWmSoDetailEntity> detailList;
    // 预配明细列表
    private List<BanQinWmSoPreallocEntity> preallocList;
    // 分配明细列表
    private List<BanQinWmSoAllocEntity> allocList;
    // 预配明细
    private BanQinWmSoPreallocEntity preallocEntity;
    // 分配明细
    private BanQinWmSoAllocEntity allocEntity;

    public List<BanQinWmSoEntity> getSoHeaderList() {
        return soHeaderList;
    }

    public void setSoHeaderList(List<BanQinWmSoEntity> soHeaderList) {
        this.soHeaderList = soHeaderList;
    }

    public List<BanQinWmSoDetailEntity> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<BanQinWmSoDetailEntity> detailList) {
        this.detailList = detailList;
    }

    public List<BanQinWmSoPreallocEntity> getPreallocList() {
        return preallocList;
    }

    public void setPreallocList(List<BanQinWmSoPreallocEntity> preallocList) {
        this.preallocList = preallocList;
    }

    public List<BanQinWmSoAllocEntity> getAllocList() {
        return allocList;
    }

    public void setAllocList(List<BanQinWmSoAllocEntity> allocList) {
        this.allocList = allocList;
    }

    public BanQinWmSoPreallocEntity getPreallocEntity() {
        return preallocEntity;
    }

    public void setPreallocEntity(BanQinWmSoPreallocEntity preallocEntity) {
        this.preallocEntity = preallocEntity;
    }

    public BanQinWmSoAllocEntity getAllocEntity() {
        return allocEntity;
    }

    public void setAllocEntity(BanQinWmSoAllocEntity allocEntity) {
        this.allocEntity = allocEntity;
    }
}
