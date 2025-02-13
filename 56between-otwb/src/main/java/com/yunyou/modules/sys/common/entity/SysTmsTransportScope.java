package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.sys.entity.Area;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 业务服务范围Entity
 */
public class SysTmsTransportScope extends DataEntity<SysTmsTransportScope> {

    private static final long serialVersionUID = 1L;
    private String code;// 服务范围编码
    private String name;// 服务范围名称
    private String dataSet;// 数据套
    // 业务服务范围区域
    private List<Area> areaList = Lists.newArrayList();

    private String dataSetName;// 数据套名称

    public SysTmsTransportScope() {
        super();
    }

    public SysTmsTransportScope(String id) {
        super(id);
    }

    public SysTmsTransportScope(String code, String dataSet) {
        this.code = code;
        this.dataSet = dataSet;
    }

    @ExcelField(title = "服务范围编码", align = 2, sort = 7)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ExcelField(title = "服务范围名称", align = 2, sort = 8)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }
}