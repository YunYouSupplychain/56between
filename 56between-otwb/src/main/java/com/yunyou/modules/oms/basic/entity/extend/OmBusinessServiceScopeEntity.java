package com.yunyou.modules.oms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.oms.basic.entity.OmBusinessServiceScope;
import com.yunyou.modules.sys.entity.Area;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class OmBusinessServiceScopeEntity extends OmBusinessServiceScope {
    // 业务服务范围区域
    private List<Area> areaList = Lists.newArrayList();
    // 机构名称
    private String orgName;

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<String> getAreaIdList() {
        List<String> areaIdList = Lists.newArrayList();
        for (Area area : areaList) {
            areaIdList.add(area.getId());
        }
        return areaIdList;
    }

    public void setAreaIdList(List<String> areaIdList) {
        areaList = Lists.newArrayList();
        for (String areaId : areaIdList) {
            Area area = new Area();
            area.setId(areaId);
            areaList.add(area);
        }
    }

    public String getAreaIds() {
        return StringUtils.join(getAreaIdList(), ",");
    }

    public void setAreaIds(String areaIds) {
        areaList = Lists.newArrayList();
        if (areaIds != null) {
            String[] ids = StringUtils.split(areaIds, ",");
            setAreaIdList(Lists.newArrayList(ids));
        }
    }
}
