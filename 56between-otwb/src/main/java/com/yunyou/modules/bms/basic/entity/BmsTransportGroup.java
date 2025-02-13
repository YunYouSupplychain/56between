package com.yunyou.modules.bms.basic.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportPriceEntity;

import java.util.List;

/**
 * 描述：运输价格体系
 *
 * @author liujianhua
 * @version 2019-11-14
 */
public class BmsTransportGroup extends DataEntity<BmsTransportGroup> {

    private static final long serialVersionUID = -218306506707675064L;
    // 运输体系编码
    private String transportGroupCode;
    // 运输体系名称
    private String transportGroupName;
    // 机构ID
    private String orgId;
    // 运输价格
    private List<BmsTransportPriceEntity> transportPrices = Lists.newArrayList();

    public BmsTransportGroup() {
    }

    public BmsTransportGroup(String id) {
        super(id);
    }

    public String getTransportGroupCode() {
        return transportGroupCode;
    }

    public void setTransportGroupCode(String transportGroupCode) {
        this.transportGroupCode = transportGroupCode;
    }

    public String getTransportGroupName() {
        return transportGroupName;
    }

    public void setTransportGroupName(String transportGroupName) {
        this.transportGroupName = transportGroupName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<BmsTransportPriceEntity> getTransportPrices() {
        return transportPrices;
    }

    public void setTransportPrices(List<BmsTransportPriceEntity> transportPrices) {
        this.transportPrices = transportPrices;
    }
}
