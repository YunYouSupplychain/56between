package com.yunyou.modules.wms.kit.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;

import java.util.List;

/**
 * 描述：
 * <p>
 * create by Jianhua on 2019/8/21
 */
public class BanQinWmKitEntity extends BanQinWmKitHeader {
    private static final long serialVersionUID = 7883134702889280175L;
    // 货主名称
    private String ownerName;
    //审核人名称
    private String auditOpName;
    // 父件明细List
    private List<BanQinWmKitParentDetailEntity> parentEntitys = Lists.newArrayList();
    // 父件加工明细List
    private List<BanQinWmKitResultDetailEntity> resultEntitys = Lists.newArrayList();
    // 子件明细List
    private List<BanQinWmKitSubDetailEntity> subEntitys = Lists.newArrayList();
    // 子件加工明细List
    private List<BanQinWmTaskKitEntity> taskEntitys = Lists.newArrayList();
    // 父件明细entity
    private BanQinWmKitParentDetailEntity parentEntity;
    // 父件加工明细entity
    private BanQinWmKitResultDetailEntity resultEntity;
    // 子件明细entity
    private BanQinWmKitSubDetailEntity subEntity;
    // 子件加工明细entity
    private BanQinWmTaskKitEntity taskEntity;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAuditOpName() {
        return auditOpName;
    }

    public void setAuditOpName(String auditOpName) {
        this.auditOpName = auditOpName;
    }

    public List<BanQinWmKitParentDetailEntity> getParentEntitys() {
        return parentEntitys;
    }

    public void setParentEntitys(List<BanQinWmKitParentDetailEntity> parentEntitys) {
        this.parentEntitys = parentEntitys;
    }

    public List<BanQinWmKitResultDetailEntity> getResultEntitys() {
        return resultEntitys;
    }

    public void setResultEntitys(List<BanQinWmKitResultDetailEntity> resultEntitys) {
        this.resultEntitys = resultEntitys;
    }

    public List<BanQinWmKitSubDetailEntity> getSubEntitys() {
        return subEntitys;
    }

    public void setSubEntitys(List<BanQinWmKitSubDetailEntity> subEntitys) {
        this.subEntitys = subEntitys;
    }

    public List<BanQinWmTaskKitEntity> getTaskEntitys() {
        return taskEntitys;
    }

    public void setTaskEntitys(List<BanQinWmTaskKitEntity> taskEntitys) {
        this.taskEntitys = taskEntitys;
    }

    public BanQinWmKitParentDetailEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(BanQinWmKitParentDetailEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    public BanQinWmKitResultDetailEntity getResultEntity() {
        return resultEntity;
    }

    public void setResultEntity(BanQinWmKitResultDetailEntity resultEntity) {
        this.resultEntity = resultEntity;
    }

    public BanQinWmKitSubDetailEntity getSubEntity() {
        return subEntity;
    }

    public void setSubEntity(BanQinWmKitSubDetailEntity subEntity) {
        this.subEntity = subEntity;
    }

    public BanQinWmTaskKitEntity getTaskEntity() {
        return taskEntity;
    }

    public void setTaskEntity(BanQinWmTaskKitEntity taskEntity) {
        this.taskEntity = taskEntity;
    }
}
