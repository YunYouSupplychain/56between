package com.yunyou.modules.sys.common.entity.extend;

import com.yunyou.modules.sys.common.entity.SysBmsSubject;

public class SysBmsSubjectEntity extends SysBmsSubject {
    private static final long serialVersionUID = -4746257267253062312L;
    private String billCategoryLabel;
    private String billModuleLabel;

    public String getBillCategoryLabel() {
        return billCategoryLabel;
    }

    public void setBillCategoryLabel(String billCategoryLabel) {
        this.billCategoryLabel = billCategoryLabel;
    }

    public String getBillModuleLabel() {
        return billModuleLabel;
    }

    public void setBillModuleLabel(String billModuleLabel) {
        this.billModuleLabel = billModuleLabel;
    }

}
