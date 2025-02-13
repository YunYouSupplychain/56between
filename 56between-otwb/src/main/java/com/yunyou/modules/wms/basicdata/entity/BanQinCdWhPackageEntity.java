package com.yunyou.modules.wms.basicdata.entity;

import java.util.List;

/**
 * 描述：包装扩展Entity
 *
 * @auther: Jianhua on 2019/1/28
 */
public class BanQinCdWhPackageEntity extends BanQinCdWhPackage {

    private List<BanQinCdWhPackageRelation> cdWhPackageRelations;

    public List<BanQinCdWhPackageRelation> getCdWhPackageRelations() {
        return cdWhPackageRelations;
    }

    public void setCdWhPackageRelations(List<BanQinCdWhPackageRelation> cdWhPackageRelations) {
        this.cdWhPackageRelations = cdWhPackageRelations;
    }
}
