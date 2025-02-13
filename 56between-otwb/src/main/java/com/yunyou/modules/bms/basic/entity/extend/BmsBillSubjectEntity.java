package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsBillSubject;

/**
 * 描述：费用科目扩展实体
 *
 * @author Jianhua
 * @version 2019/7/24
 */
public class BmsBillSubjectEntity extends BmsBillSubject {

    private static final long serialVersionUID = -4746257267253062312L;
    // 机构名称
    private String orgName;
    // 费用模块显示值
    private String billModuleLabel;
    // 费用类别显示值
    private String billCategoryLabel;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

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
