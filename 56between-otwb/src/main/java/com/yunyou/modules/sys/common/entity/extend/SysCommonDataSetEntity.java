package com.yunyou.modules.sys.common.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;

import java.util.List;

public class SysCommonDataSetEntity extends SysCommonDataSet {

    private List<SysDataSetOrgRelationEntity> relationList = Lists.newArrayList();

    public List<SysDataSetOrgRelationEntity> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<SysDataSetOrgRelationEntity> relationList) {
        this.relationList = relationList;
    }
}
