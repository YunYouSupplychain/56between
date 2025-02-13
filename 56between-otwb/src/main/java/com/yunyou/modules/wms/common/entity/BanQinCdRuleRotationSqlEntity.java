package com.yunyou.modules.wms.common.entity;

/**
 * 周转规则配置 拼SQL 结果集
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinCdRuleRotationSqlEntity {
	// order by排序sql
	private String orderBySql;
	// where条件sql
	private String whereSql;

	public String getOrderBySql() {
		return orderBySql;
	}

	public void setOrderBySql(String orderBySql) {
		this.orderBySql = orderBySql;
	}

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

}