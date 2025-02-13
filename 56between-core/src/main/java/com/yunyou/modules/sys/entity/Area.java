package com.yunyou.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.yunyou.core.persistence.TreeEntity;

/**
 * 区域Entity
 * @author yunyou
 * @version 2016-05-15
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	public static final String province = "省";
	public static final String city = "市";
	private String code; 	// 区域编码
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	private String officeId;
	private String officeName;
	private String provinceName;
	private String cityName;
	private String countyName;
	
	public Area(){
		super();
		this.setIdType(IDTYPE_AUTO);
		this.sort = 30;
	}

	public Area(String id){
		super(id);
	}
	
	@Override
    public Area getParent() {
		return parent;
	}

	@Override
	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	@Override
	public String toString() {
		return name;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
}