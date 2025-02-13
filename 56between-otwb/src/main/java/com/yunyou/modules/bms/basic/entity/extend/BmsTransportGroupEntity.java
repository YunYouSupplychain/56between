package com.yunyou.modules.bms.basic.entity.extend;

import com.yunyou.modules.bms.basic.entity.BmsTransportGroup;

/**
 * 描述：运输价格体系扩展实体
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsTransportGroupEntity extends BmsTransportGroup {

    private static final long serialVersionUID = -1187591133416750523L;
    // 机构名称
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
