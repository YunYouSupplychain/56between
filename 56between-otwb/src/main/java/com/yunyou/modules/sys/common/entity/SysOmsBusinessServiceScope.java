package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.sys.entity.Area;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 业务服务范围Entity
 */
public class SysOmsBusinessServiceScope extends DataEntity<SysOmsBusinessServiceScope> {

    private static final long serialVersionUID = 1L;
    private String groupCode;// 组编码
    private String groupName;// 组名称
    private String dataSet;// 数据套
    // 业务服务范围区域
    private List<Area> areaList = Lists.newArrayList();

    // 扩展字段
    private String dataSetName;// 数据套名称

    public SysOmsBusinessServiceScope() {
        super();
    }

    public SysOmsBusinessServiceScope(String id) {
        super(id);
    }

    public SysOmsBusinessServiceScope(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
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

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}