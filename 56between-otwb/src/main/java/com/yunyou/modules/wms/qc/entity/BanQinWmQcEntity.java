package com.yunyou.modules.wms.qc.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 描述：质检单扩展Entity
 *
 * @auther: Jianhua on 2019/1/29
 */
public class BanQinWmQcEntity extends BanQinWmQcHeader {
    // 货主
    private String ownerName;
    // 仓库名称
    private String orgName;
    // 质检明细表信息对象集合
    private List<BanQinWmQcDetailEntity> wmQcDetailList = Lists.newArrayList();
    // 质检单商品表信息对象集合
    private List<BanQinWmQcSkuEntity> wmQcSkuList = Lists.newArrayList();

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<BanQinWmQcDetailEntity> getWmQcDetailList() {
        return wmQcDetailList;
    }

    public void setWmQcDetailList(List<BanQinWmQcDetailEntity> wmQcDetailList) {
        this.wmQcDetailList = wmQcDetailList;
    }

    public List<BanQinWmQcSkuEntity> getWmQcSkuList() {
        return wmQcSkuList;
    }

    public void setWmQcSkuList(List<BanQinWmQcSkuEntity> wmQcSkuList) {
        this.wmQcSkuList = wmQcSkuList;
    }
}
