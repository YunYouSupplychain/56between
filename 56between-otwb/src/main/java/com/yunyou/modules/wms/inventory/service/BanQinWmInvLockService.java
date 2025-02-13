package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.config.Global;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLock;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvLockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

/**
 * 库存操作悲观锁记录表Service
 * @author WMJ
 * @version 2019-10-16
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvLockService extends CrudService<BanQinWmInvLockMapper, BanQinWmInvLock> {
	protected static String SQL_GET_LOT_LOCK = "select 1 from WM_INV_LOCK WMLK " + " where limit 1 " + " and WMLK.LOT_NUM = {lotNum} " + " and WMLK.ORG_ID= {orgId} " + " for update ";
	protected static String SQL_GET_MAIN_LOCK = "select 1 from WM_INV_LOCK WMLK " + " where limit 1 " + " and WMLK.LOT_NUM= 'INVENTORY_MAIN_LOCK' " + "   for update ";
	protected Object mainLock = new Object();

	// 数据库驱动
	protected String driverClassName = Global.getConfig("jdbc.driver");
	// 数据库连接串
	protected String url = Global.getConfig("jdbc.url");
	// 数据库用户名
	protected String username = Global.getConfig("jdbc.username");
	// 数据库密码
	protected String password = Global.getConfig("jdbc.password");

	public BanQinWmInvLock get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmInvLock> findList(BanQinWmInvLock banQinWmInvLock) {
		return super.findList(banQinWmInvLock);
	}
	
	@Transactional
	public void save(BanQinWmInvLock banQinWmInvLock) {
		super.save(banQinWmInvLock);
	}
	
	/**
	 * 获取数据库悲观锁
	 * @param lotNum 需要锁的商品批次号
	 */
	@Transactional
	public void getLock(String lotNum, String orgId) {
		/**
		 * 系统控制参数：WM_LOCK_MAX_WAIT(库存操作锁最大等待时间，单位：秒) 注意：
		 * 1、该参数设置需为正整数
		 * 2、不设置该参数，或设置值不合法，系统将采用默认值20秒
		 */
		String maxWaitSeconds = "20";
		if (maxWaitSeconds == null || !maxWaitSeconds.matches("\\d+")) { // 如果不配置该系统控制参数，则默认20秒
			maxWaitSeconds = "20";
		}
		SQL_GET_LOT_LOCK = SQL_GET_LOT_LOCK.replace("{lotNum}", lotNum).replace("{orgId}", orgId);
		List<Object> objects = mapper.execSelectSql(SQL_GET_LOT_LOCK + " wait " + maxWaitSeconds);
		if (objects.size() < 1) {
			// 如果不存在，则先获取公共锁，然后插入记录; 注意：这部分需及时提交，故放在新事物中
			synchronized (mainLock) {
				Connection conn = null;
				PreparedStatement mainPst = null;
				ResultSet mainRs = null;
				PreparedStatement lotPst = null;
				ResultSet lotRs = null;
				try {
					Class.forName(driverClassName).newInstance();
					conn = DriverManager.getConnection(url, username, password);
					final String mainSql = SQL_GET_MAIN_LOCK + " wait " + maxWaitSeconds;
					mainPst = conn.prepareStatement(mainSql);
					mainRs = mainPst.executeQuery();

					// 为避免在获取公共锁过程中，其他并发线程插入了该LOT，在获取公共锁后再检查一遍LOT是否存在，以解决并发问题
					final String locSql = SQL_GET_LOT_LOCK + " wait " + maxWaitSeconds;
					lotPst = conn.prepareStatement(locSql);
					lotRs = lotPst.executeQuery();
					if (!lotRs.next()) {
						if (logger.isInfoEnabled()) {
							logger.info("新增库存表操作悲观锁:lotNum=" + lotNum + ",orgId=" + orgId);
						}
						// 添加数据库锁记录
						BanQinWmInvLock invLockModel = new BanQinWmInvLock();
						invLockModel.setLotNum(lotNum);
						invLockModel.setOrgId(orgId);
						this.save(invLockModel);
					}
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				} finally {
					this.close(mainRs, mainPst, null);
					this.close(lotRs, lotPst, null);
					this.close(null, null, conn);
				}
			}

			// 在插入后重新竞争获取锁
			SQL_GET_LOT_LOCK = SQL_GET_LOT_LOCK.replace("{lotNum}", lotNum).replace("{orgId}", orgId);
			mapper.execSelectSql(SQL_GET_LOT_LOCK + " wait " + maxWaitSeconds);
		}
	}

	/**
	 * 关闭资源
	 * @param rs 待关闭的ResultSet
	 * @param pst 待关闭的PreparedStatement
	 * @param conn 待关闭的Connection
	 */
	protected void close(ResultSet rs, PreparedStatement pst, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}