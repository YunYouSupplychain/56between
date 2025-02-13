package com.yunyou.modules.tms.basic.entity.extend;

import com.google.common.collect.Lists;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.tms.basic.entity.TmTransportScope;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TmTransportScopeEntity extends TmTransportScope {
    private static final long serialVersionUID = 5390569866323222736L;

    /*多字段匹配查询*/
    private String codeAndName;
    // 业务服务范围区域
    private List<Area> areaList = Lists.newArrayList();

    public TmTransportScopeEntity() {
    }

    public TmTransportScopeEntity(String id) {
        super(id);
    }

    public TmTransportScopeEntity(String id, List<Area> areaList) {
        super(id);
        this.areaList = areaList;
    }

    public String getCodeAndName() {
        return codeAndName;
    }

    public void setCodeAndName(String codeAndName) {
        this.codeAndName = codeAndName;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
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
            areaList.add(new Area(areaId));
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
