package com.yunyou.modules.sys.entity;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.DataEntity;
import org.apache.commons.text.StringEscapeUtils;

/**
 * 数据权限Entity
 *
 * @author lgf
 * @version 2017-04-02
 */
public class DataRule extends DataEntity<DataRule> {

    private static final long serialVersionUID = 1L;
    private String menuId;        // 所属菜单
    private String name;        // 数据规则名称
    private String className;   //实体类名
    private String field;        // 规则字段
    private String express;        // 规则条件 data_rule_express
    private String value;        // 规则值
    private String sqlSegment;        // 自定义sql

    public DataRule() {
        super();
    }

    public DataRule(String id) {
        super(id);
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSqlSegment() {
        return sqlSegment;
    }

    public void setSqlSegment(String sqlSegment) {
        this.sqlSegment = sqlSegment;
    }

    public String getDataScopeSql() {
        StringBuilder sqlBuffer = new StringBuilder();
        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(value)) {
            sqlBuffer.append(" AND ").append(field).append(" ").append(StringEscapeUtils.unescapeHtml4(express)).append(" ").append(value).append(" ");
        }
        if (StringUtils.isNotBlank(sqlSegment)) {
            sqlBuffer.append(" AND ").append(StringEscapeUtils.unescapeHtml4(sqlSegment)).append(" ");
        }

        return sqlBuffer.toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}