package com.yunyou.modules.sys.common.entity;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.sys.entity.Area;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域Entity
 */
public class SysCommonRegion extends DataEntity<SysCommonRegion> {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "区域编码不能为空")
    private String code;// 区域编码
    @NotNull(message = "区域名称不能为空")
    private String name;// 区域名称
    @NotNull(message = "数据套不能为空")
    private String dataSet;// 数据套
    // 区域地点列表
    private List<Area> areaList = Lists.newArrayList();

    public SysCommonRegion() {
        super();
    }

    public SysCommonRegion(String id) {
        super(id);
    }

    public SysCommonRegion(String id, String dataSet) {
        super(id);
        this.dataSet = dataSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAreaIds() {
        return CollectionUtil.isNotEmpty(areaList) ? areaList.stream().map(Area::getId).collect(Collectors.joining(",")) : "";
    }

    public void setAreaIds(String areaIds) {
        areaList = Lists.newArrayList();
        if (areaIds != null) {
            String[] ids = StringUtils.split(areaIds, ",");
            for (String areaId : ids) {
                areaList.add(new Area(areaId));
            }
        }
    }
}