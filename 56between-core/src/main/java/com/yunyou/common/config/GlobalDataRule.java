package com.yunyou.common.config;

import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.collection.CollectionUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 全局数据权限过滤
 *
 * @author WMJ
 */
public class GlobalDataRule {

    private static JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");

    /**
     * 获取数据权限SQL
     */
    public static String getDataRuleSql(String columnName, String orgId) {
        StringBuilder builder = new StringBuilder();
        // 防止SQL注入
        List<String> hasExists = jdbcTemplate.queryForList("SELECT 1 FROM sys_office WHERE id = ?", String.class, orgId);
        if (CollectionUtil.isEmpty(hasExists)) {
            builder.append(" AND ").append(columnName);
            builder.append(" = '###'");
            return builder.toString();
        }
        builder.append(" AND ").append(columnName);
        List<String> ids = jdbcTemplate.queryForList("SELECT id FROM sys_office WHERE parent_ids like concat('%', ?, '%')", String.class, orgId);
        if (CollectionUtil.isEmpty(ids)) {
            builder.append(" = '").append(orgId).append("' ");
        } else {
            builder.append(" IN ('").append(orgId).append("'");
            for (String id : ids) {
                builder.append(",").append("'").append(id).append("'");
            }
            builder.append(") ");
        }
        return builder.toString();
    }

}
