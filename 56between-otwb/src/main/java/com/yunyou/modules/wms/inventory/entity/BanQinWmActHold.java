package com.yunyou.modules.wms.inventory.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import java.util.Date;

/**
 * 冻结记录Entity
 * @author WMJ
 * @version 2019-01-28
 */
public class BanQinWmActHold extends DataEntity<BanQinWmActHold> {
	
	private static final long serialVersionUID = 1L;
	private String holdId;		// 冻结ID
	private String lineNo;		// 行号
	private String status;		// 冻结状态(99冻结，90释放)
	private String holdType;		// 冻结方式
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String lotNum;		// 批次号
	private String locCode;		// 库位编码
	private String traceId;		// 跟踪号
	private String reasonCode;		// 原因编码
	private String reason;		// 原因描述
	private String isAllowMv;		// 是否可移动
	private String isAllowAd;		// 是否可调整
	private String isAllowTf;		// 是否可转移
	private String op;		// 操作人
	private Date opTime;		// 操作时间
	private String orgId;		// 分公司
	private String orgName;
    
    // 扩展字段
    private String ownerName;
    private String skuName;
    private Date opTimeFrom;
    private Date opTimeTo;
	
	public BanQinWmActHold() {
		super();
		this.recVer = 0;
	}

	public BanQinWmActHold(String id){
		super(id);
	}

	@ExcelField(title="冻结ID", align=2, sort=2)
	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	
	@ExcelField(title="行号", align=2, sort=3)
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
	@ExcelField(title="冻结状态(99冻结，90释放)", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="冻结方式", align=2, sort=5)
	public String getHoldType() {
		return holdType;
	}

	public void setHoldType(String holdType) {
		this.holdType = holdType;
	}
	
	@ExcelField(title="货主编码", align=2, sort=6)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品编码", align=2, sort=7)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="批次号", align=2, sort=8)
	public String getLotNum() {
		return lotNum;
	}

	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	
	@ExcelField(title="库位编码", align=2, sort=9)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	
	@ExcelField(title="跟踪号", align=2, sort=10)
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	
	@ExcelField(title="原因编码", align=2, sort=11)
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	@ExcelField(title="原因描述", align=2, sort=12)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@ExcelField(title="是否可移动", align=2, sort=13)
	public String getIsAllowMv() {
		return isAllowMv;
	}

	public void setIsAllowMv(String isAllowMv) {
		this.isAllowMv = isAllowMv;
	}
	
	@ExcelField(title="是否可调整", align=2, sort=14)
	public String getIsAllowAd() {
		return isAllowAd;
	}

	public void setIsAllowAd(String isAllowAd) {
		this.isAllowAd = isAllowAd;
	}
	
	@ExcelField(title="是否可转移", align=2, sort=15)
	public String getIsAllowTf() {
		return isAllowTf;
	}

	public void setIsAllowTf(String isAllowTf) {
		this.isAllowTf = isAllowTf;
	}
	
	@ExcelField(title="操作人", align=2, sort=16)
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=17)
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@ExcelField(title="分公司", align=2, sort=25)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOpTimeFrom() {
        return opTimeFrom;
    }

    public void setOpTimeFrom(Date opTimeFrom) {
        this.opTimeFrom = opTimeFrom;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOpTimeTo() {
        return opTimeTo;
    }

    public void setOpTimeTo(Date opTimeTo) {
        this.opTimeTo = opTimeTo;
    }

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}