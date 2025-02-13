package com.yunyou.core.persistence;

import com.yunyou.common.utils.Reflections;
import com.yunyou.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 数据Entity类
 */
public abstract class TreeEntity<T> extends DataEntity<T> {
    private static final long serialVersionUID = 1L;
    /**
     * 父级
     */
    protected T parent;
    /**
     * 所有父级ID
     */
    @Length(min = 1, max = 2000)
    protected String parentIds;
    /**
     * 名称
     */
    @Length(min = 1, max = 100)
    protected String name;
    /**
     * 排序
     */
    protected Integer sort;
    /**
     * 是否有子节点
     */
    private boolean hasChildren;

    public TreeEntity() {
        super();
        this.sort = 30;
    }

    public TreeEntity(String id) {
        super(id);
    }

    /**
     * 父对象，只能通过子类实现，父类实现mybatis无法读取
     */
    @JsonBackReference
    @NotNull
    public abstract T getParent();

    /**
     * 父对象，只能通过子类实现，父类实现mybatis无法读取
     */
    public abstract void setParent(T parent);

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        String id = null;
        if (parent != null) {
            id = (String) Reflections.getFieldValue(parent, "id");
        }
        return StringUtils.isNotBlank(id) ? id : "0";
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

}
