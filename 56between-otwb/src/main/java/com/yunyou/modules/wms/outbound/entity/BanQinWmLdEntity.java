package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 装车单LDEntity
 * @author WMJ
 * @version 2019/02/20
 */
public class BanQinWmLdEntity extends BanQinWmLdHeader {
	private String soNo;
	private String vehicleType;
	// 货主名称
	private String ownerName;
	// 承运商名称
	private String carrierName;
	private String carrierTel;
	private String carrierAddress;
	private String carrierContactName;
	private String carrierContactTel;
	private String carrierContactAddress;
	// 装车交接人中文名称
	private String deliverOpName;
	private Date fmLdTimeFm;
	private Date fmLdTimeTo;
	private Date toLdTimeFm;
	private Date toLdTimeTo;
	private Date deliverTimeFm;
	private Date deliverTimeTo;
	private Date createDateFm;
	private Date createDateTo;

	private String traceId;
	private String orgName;

	// 查询条件
	private List<String> statusList;

	// 未装车明细
	private List<BanQinWmLdDetailEntity> ldDetail10Entity;
	// 已装车明细
	private List<BanQinWmLdDetailEntity> ldDetail40Entity;
	// 装车-订单
	private List<BanQinWmLdDetailEntity> soOrderQueryItem;
	// 装车-包裹
	private List<BanQinWmLdDetailEntity> traceIdQueryItem;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getDeliverOpName() {
		return deliverOpName;
	}

	public void setDeliverOpName(String deliverOpName) {
		this.deliverOpName = deliverOpName;
	}

	public String getCarrierTel() {
		return carrierTel;
	}

	public void setCarrierTel(String carrierTel) {
		this.carrierTel = carrierTel;
	}

	public String getCarrierAddress() {
		return carrierAddress;
	}

	public void setCarrierAddress(String carrierAddress) {
		this.carrierAddress = carrierAddress;
	}

	public String getCarrierContactName() {
		return carrierContactName;
	}

	public void setCarrierContactName(String carrierContactName) {
		this.carrierContactName = carrierContactName;
	}

	public String getCarrierContactTel() {
		return carrierContactTel;
	}

	public void setCarrierContactTel(String carrierContactTel) {
		this.carrierContactTel = carrierContactTel;
	}

	public String getCarrierContactAddress() {
		return carrierContactAddress;
	}

	public void setCarrierContactAddress(String carrierContactAddress) {
		this.carrierContactAddress = carrierContactAddress;
	}

    public List<BanQinWmLdDetailEntity> getLdDetail10Entity() {
        return ldDetail10Entity;
    }

    public void setLdDetail10Entity(List<BanQinWmLdDetailEntity> ldDetail10Entity) {
        this.ldDetail10Entity = ldDetail10Entity;
    }

    public List<BanQinWmLdDetailEntity> getLdDetail40Entity() {
        return ldDetail40Entity;
    }

    public void setLdDetail40Entity(List<BanQinWmLdDetailEntity> ldDetail40Entity) {
        this.ldDetail40Entity = ldDetail40Entity;
    }

    public List<BanQinWmLdDetailEntity> getSoOrderQueryItem() {
        return soOrderQueryItem;
    }

    public void setSoOrderQueryItem(List<BanQinWmLdDetailEntity> soOrderQueryItem) {
        this.soOrderQueryItem = soOrderQueryItem;
    }

    public List<BanQinWmLdDetailEntity> getTraceIdQueryItem() {
        return traceIdQueryItem;
    }

    public void setTraceIdQueryItem(List<BanQinWmLdDetailEntity> traceIdQueryItem) {
        this.traceIdQueryItem = traceIdQueryItem;
    }

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	@Override
	public String getVehicleType() {
		return vehicleType;
	}

	@Override
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFmLdTimeFm() {
		return fmLdTimeFm;
	}

	public void setFmLdTimeFm(Date fmLdTimeFm) {
		this.fmLdTimeFm = fmLdTimeFm;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFmLdTimeTo() {
		return fmLdTimeTo;
	}

	public void setFmLdTimeTo(Date fmLdTimeTo) {
		this.fmLdTimeTo = fmLdTimeTo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getToLdTimeFm() {
		return toLdTimeFm;
	}

	public void setToLdTimeFm(Date toLdTimeFm) {
		this.toLdTimeFm = toLdTimeFm;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getToLdTimeTo() {
		return toLdTimeTo;
	}

	public void setToLdTimeTo(Date toLdTimeTo) {
		this.toLdTimeTo = toLdTimeTo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeliverTimeFm() {
		return deliverTimeFm;
	}

	public void setDeliverTimeFm(Date deliverTimeFm) {
		this.deliverTimeFm = deliverTimeFm;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeliverTimeTo() {
		return deliverTimeTo;
	}

	public void setDeliverTimeTo(Date deliverTimeTo) {
		this.deliverTimeTo = deliverTimeTo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDateFm() {
		return createDateFm;
	}

	public void setCreateDateFm(Date createDateFm) {
		this.createDateFm = createDateFm;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}
}