package com.yunyou.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.modules.sys.utils.UserUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单Entity
 *
 * @author yunyou
 * @version 2016-05-15
 */
public class Menu extends DataEntity<Menu> {

    private static final long serialVersionUID = 1L;
    private Menu parent;    // 父级菜单
    private String parentIds; // 所有父级编号
    private List<Menu> children;    // 父级菜单
    private String name;    // 名称
    private String href;    // 链接
    private String target;    // 目标（ mainFrame、_blank、_self、_parent、_top）
    private String icon;    // 图标
    private Integer sort;    // 排序
    private String isShow;    // 是否在菜单中显示（1：显示；0：不显示）
    private String type; //按钮类型
    private String permission; // 权限标识
    private boolean hasChildren;
    private List<DataRule> dataRuleList;

    private String userId;

    public Menu() {
        super();
        this.sort = 30;
        this.isShow = "1";
        this.type = "1";
    }

    public Menu(String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    @JsonIgnore
    public boolean hasPermisson() {
        List<Menu> menuList = UserUtils.getMenuList();
        for (Menu menu : menuList) {
            if (menu.getId().equals(this.getId()))
                return true;
        }
        return false;
    }

    @JsonIgnore
    public static void sortList(List<Menu> list, List<Menu> sourceList, String parentId, boolean cascade) {
        for (int i = 0; i < sourceList.size(); i++) {
            Menu e = sourceList.get(i);
            if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(parentId)) {
                list.add(e);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j = 0; j < sourceList.size(); j++) {
                        Menu child = sourceList.get(j);
                        if (child.getParent() != null && child.getParent().getId() != null
                            && child.getParent().getId().equals(e.getId())) {
                            sortList(list, sourceList, e.getId(), true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    public static String getRootId() {
        return "1";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<DataRule> getDataRuleList() {
        return dataRuleList;
    }

    public void setDataRuleList(List<DataRule> dataRuleList) {
        this.dataRuleList = dataRuleList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}