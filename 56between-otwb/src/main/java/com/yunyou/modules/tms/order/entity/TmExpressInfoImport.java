package com.yunyou.modules.tms.order.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

/**
 * 快递单号导入更新Entity
 * @author zyf
 * @version 2020-04-13
 */
public class TmExpressInfoImport extends DataEntity<TmExpressInfoImport> {
	
	private static final long serialVersionUID = 1L;
	private String importNo;		// 导入批次号
	private String fileName;		// 文件名
	private String importReason;		// 导入原因
	private String orgId;		// 机构ID

	public TmExpressInfoImport() {
		super();
	}

	public TmExpressInfoImport(String id){
		super(id);
	}

	@ExcelField(title="导入批次号", align=2, sort=7)
	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
	}
	
	@ExcelField(title="文件名", align=2, sort=8)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@ExcelField(title="导入原因", align=2, sort=9)
	public String getImportReason() {
		return importReason;
	}

	public void setImportReason(String importReason) {
		this.importReason = importReason;
	}
	
	@ExcelField(title="机构ID", align=2, sort=10)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}