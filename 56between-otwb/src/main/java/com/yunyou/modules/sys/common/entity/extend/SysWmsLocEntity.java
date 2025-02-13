package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysWmsLoc;

import java.util.List;

public class SysWmsLocEntity extends SysWmsLoc {

    private int sumLength;
    private List<SysWmsLoc> locList;

    public int getSumLength() {
        return sumLength;
    }

    public void setSumLength(int sumLength) {
        this.sumLength = sumLength;
    }

    public List<SysWmsLoc> getLocList() {
        return locList;
    }

    public void setLocList(List<SysWmsLoc> locList) {
        this.locList = locList;
    }
}
