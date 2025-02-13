package com.yunyou.modules.wms.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * WMS工具类
 * @author WMJ
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class WmsUtil { 
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取明细表的最大行号
     * @param tableName 表名
     * @param columnName 外键列名
     * @param columnValue 外键值
     */
    public String getMaxLineNo(String tableName, String columnName, String columnValue) {
        String result = "0001";
        String sql = "select line_no from " + tableName + " where " + columnName + " = '" + columnValue + "'" + " order by line_no desc limit 1";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (CollectionUtil.isNotEmpty(list)) {
            result = (String)list.get(0).get("line_no");
            result = String.format("%04d", Integer.parseInt(result) + 1);
        }
        return result;
    }
    
}
