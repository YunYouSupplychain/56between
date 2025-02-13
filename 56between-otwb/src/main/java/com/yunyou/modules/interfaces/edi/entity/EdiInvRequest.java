package com.yunyou.modules.interfaces.edi.entity;

import java.util.List;

public class EdiInvRequest {

    private List<EdiInvInfo> invList;

    public EdiInvRequest() {
    }

    public EdiInvRequest(List<EdiInvInfo> invList) {
        this.invList = invList;
    }

    public List<EdiInvInfo> getInvList() {
        return invList;
    }

    public void setInvList(List<EdiInvInfo> invList) {
        this.invList = invList;
    }
}
