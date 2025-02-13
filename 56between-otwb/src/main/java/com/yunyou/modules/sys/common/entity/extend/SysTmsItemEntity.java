package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysTmsItem;

public class SysTmsItemEntity extends SysTmsItem {
    private static final long serialVersionUID = -4413834319888396327L;

    private String ownerName; // 货主名称

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
