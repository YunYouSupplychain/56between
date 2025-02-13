package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 快递单号导入更新Entity
 * @author zyf
 * @version 2020-04-13
 */
public class TmExpressInfoImportDetail extends DataEntity<TmExpressInfoImportDetail> {
	
	private static final long serialVersionUID = 1L;
	private String importNo;		// 导入批次号
	private String customerNo;		// 客户订单号
	private String mailNo;		// 面单号
	private Date transDate;		// 运输时间
	private String carrierCode;		// 承运商编码
	private String orgId;		// 机构ID

	public TmExpressInfoImportDetail() {
		super();
	}

	public TmExpressInfoImportDetail(String id){
		super(id);
	}

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}
	
	@ExcelField(title="客户订单号", align=2, sort=8)
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	@ExcelField(title="面单号", align=2, sort=9)
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="运输时间(yyyy-MM-dd HH:mm:ss)", align=2, sort=10)
	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	@ExcelField(title="承运商编码", align=2, sort=11)
	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}