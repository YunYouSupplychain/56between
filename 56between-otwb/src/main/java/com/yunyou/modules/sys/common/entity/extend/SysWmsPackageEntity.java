package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysWmsPackage;
import com.yunyou.modules.sys.common.entity.SysWmsPackageRelation;

import java.util.List;

/**
 * 描述：包装扩展Entity
 */
public class SysWmsPackageEntity extends SysWmsPackage {

    private List<SysWmsPackageRelation> cdWhPackageRelations;

    public List<SysWmsPackageRelation> getCdWhPackageRelations() {
        return cdWhPackageRelations;
    }

    public void setCdWhPackageRelations(List<SysWmsPackageRelation> cdWhPackageRelations) {
        this.cdWhPackageRelations = cdWhPackageRelations;
    }
}
