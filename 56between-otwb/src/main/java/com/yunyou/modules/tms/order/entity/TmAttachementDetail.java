package com.yunyou.modules.tms.order.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 附件信息Entity
 * @author zyf
 * @version 2020-04-07
 */
public class TmAttachementDetail extends DataEntity<TmAttachementDetail> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 关联单号
	private String orderType;		// 单据类型
	private String uploadPerson;		// 上传人
	private Date uploadTime;		// 上传时间
	private String uploadPath;		// 上传路径
	private String fileName;		// 文件名
	private String fileUrl;		// 文件地址
	private String orgId;		// 机构ID
	private String labelNo;		// 标签号
	
	public TmAttachementDetail() {
		super();
	}

	public TmAttachementDetail(String id){
		super(id);
	}

	@ExcelField(title="关联单号", align=2, sort=7)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@ExcelField(title="单据类型", align=2, sort=8)
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	@ExcelField(title="上传人", align=2, sort=9)
	public String getUploadPerson() {
		return uploadPerson;
	}

	public void setUploadPerson(String uploadPerson) {
		this.uploadPerson = uploadPerson;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="上传时间", align=2, sort=10)
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	@ExcelField(title="上传路径", align=2, sort=11)
	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	
	@ExcelField(title="文件名", align=2, sort=12)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@ExcelField(title="文件地址", align=2, sort=13)
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	@ExcelField(title="机构ID", align=2, sort=14)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}
}