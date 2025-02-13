package com.yunyou.modules.tms.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 运输订单跟踪信息表Entity
 * @author zyf
 * @version 2020-04-10
 */
public class TmTransportOrderTrack extends DataEntity<TmTransportOrderTrack> {
	
	private static final long serialVersionUID = 1L;
	private String transportNo;// 运输单号
	private String customerNo;// 客户单号
	private String labelNo;// 标签号
	private String receiveOutletCode;// 收货网点编码
	private String receiveOutletName;// 收货网点名称
	private String deliverOutletCode;// 发货网点编码
	private String deliverOutletName;// 发货网点名称
	private String opPerson;// 操作人
	private String opNode;// 操作节点
	private String operation;// 操作内容
	private Date opTime;// 操作时间
	private String driver;// 司机
	private String phone;// 电话
	private Double qty;// 数量
	private String orgId;// 机构ID
	private String baseOrgId;// 基础数据机构ID
	private String dispatchNo;// 派车单号
	
	public TmTransportOrderTrack() {
		super();
	}

	public TmTransportOrderTrack(String id){
		super(id);
	}

	@ExcelField(title="运输单号", align=2, sort=7)
	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	
	@ExcelField(title="客户单号", align=2, sort=8)
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	@ExcelField(title="标签号", align=2, sort=9)
	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}
	
	@ExcelField(title="收货网点编码", align=2, sort=10)
	public String getReceiveOutletCode() {
		return receiveOutletCode;
	}

	public void setReceiveOutletCode(String receiveOutletCode) {
		this.receiveOutletCode = receiveOutletCode;
	}
	
	@ExcelField(title="收货网点名称", align=2, sort=11)
	public String getReceiveOutletName() {
		return receiveOutletName;
	}

	public void setReceiveOutletName(String receiveOutletName) {
		this.receiveOutletName = receiveOutletName;
	}
	
	@ExcelField(title="发货网点编码", align=2, sort=12)
	public String getDeliverOutletCode() {
		return deliverOutletCode;
	}

	public void setDeliverOutletCode(String deliverOutletCode) {
		this.deliverOutletCode = deliverOutletCode;
	}
	
	@ExcelField(title="发货网点名称", align=2, sort=13)
	public String getDeliverOutletName() {
		return deliverOutletName;
	}

	public void setDeliverOutletName(String deliverOutletName) {
		this.deliverOutletName = deliverOutletName;
	}
	
	@ExcelField(title="操作人", align=2, sort=14)
	public String getOpPerson() {
		return opPerson;
	}

	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}
	
	@ExcelField(title="操作节点", align=2, sort=15)
	public String getOpNode() {
		return opNode;
	}

	public void setOpNode(String opNode) {
		this.opNode = opNode;
	}
	
	@ExcelField(title="操作内容", align=2, sort=16)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=17)
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
	@ExcelField(title="司机", align=2, sort=18)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	@ExcelField(title="电话", align=2, sort=19)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="数量", align=2, sort=20)
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
	
//	@ExcelField(title="机构ID", align=2, sort=21)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
//	@ExcelField(title="基础数据机构ID", align=2, sort=22)
	public String getBaseOrgId() {
		return baseOrgId;
	}

	public void setBaseOrgId(String baseOrgId) {
		this.baseOrgId = baseOrgId;
	}

	public String getDispatchNo() {
		return dispatchNo;
	}

	public void setDispatchNo(String dispatchNo) {
		this.dispatchNo = dispatchNo;
	}
}