package com.yunyou.modules.sys.entity.extend;

import com.yunyou.modules.sys.entity.Office;

/**
 * 机构扩展实体
 *
 * @author yunyou
 * @since 2023/7/11 13:48
 */
public class OfficeEntity extends Office {

    /*组织中心ID*/
    private String baseOrgId;

    public String getBaseOrgId() {
        return baseOrgId;
    }

    public void setBaseOrgId(String baseOrgId) {
        this.baseOrgId = baseOrgId;
    }
}
