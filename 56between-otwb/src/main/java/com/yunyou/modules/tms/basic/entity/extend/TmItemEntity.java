package com.yunyou.modules.tms.basic.entity.extend;

import com.yunyou.modules.tms.basic.entity.TmItem;

public class TmItemEntity extends TmItem {
    private static final long serialVersionUID = -4413834319888396327L;

    /*多字段匹配查询*/
    private String codeAndName;

    private String ownerName; // 货主名称

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
