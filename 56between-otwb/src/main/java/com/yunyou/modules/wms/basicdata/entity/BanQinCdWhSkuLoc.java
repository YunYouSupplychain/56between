package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;

/**
 * 商品拣货位Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhSkuLoc extends DataEntity<BanQinCdWhSkuLoc> {
	
	private static final long serialVersionUID = 1L;
	private String ownerCode;		// 货主编码
	private String skuCode;		// 商品编码
	private String locCode;		// 库位编码
	private String skuLocType;		// 商品拣货位类型（CS、EA、PC）
	private Double minRp;		// 最小补货数
	private String rpUom;		// 补货单位（PL、CS、EA）
	private Double maxLimit;		// 库存上限
	private Double minLimit;		// 库存下限
	private String isOverAlloc;		// 是否超量分配
	private String isAutoRp;		// 是否自动生成补货任务（预留扩展）
	private String isRpAlloc;		// 是否补被占用库存
	private String isOverRp;		// 是否超量补货
	private String isFmRs;		// 是否从存储位补货
	private String isFmCs;		// 是否从箱拣货位补货
	private String orgId;		// 分公司
	private String headerId;		// 商品Id
	
	public BanQinCdWhSkuLoc() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhSkuLoc(String id){
		super(id);
	}

	@NotNull(message="货主编码不能为空")
	@ExcelField(title="货主编码", align=2, sort=2)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	@NotNull(message="商品编码不能为空")
	@ExcelField(title="商品编码", align=2, sort=3)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	@NotNull(message="库位编码不能为空")
	@ExcelField(title="库位编码", align=2, sort=4)
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	@NotNull(message="商品拣货位类型不能为空")
	@ExcelField(title="商品拣货位类型", align=2, sort=5)
	public String getSkuLocType() {
		return skuLocType;
	}

	public void setSkuLocType(String skuLocType) {
		this.skuLocType = skuLocType;
	}
	
	@ExcelField(title="最小补货数", align=2, sort=6)
	public Double getMinRp() {
		return minRp;
	}

	public void setMinRp(Double minRp) {
		this.minRp = minRp;
	}
	
	@ExcelField(title="补货单位", align=2, sort=7)
	public String getRpUom() {
		return rpUom;
	}

	public void setRpUom(String rpUom) {
		this.rpUom = rpUom;
	}
	
	@ExcelField(title="库存上限", align=2, sort=8)
	public Double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
	}
	
	@ExcelField(title="库存下限", align=2, sort=9)
	public Double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}
	
	@ExcelField(title="是否超量分配", align=2, sort=10)
	public String getIsOverAlloc() {
		return isOverAlloc;
	}

	public void setIsOverAlloc(String isOverAlloc) {
		this.isOverAlloc = isOverAlloc;
	}
	
	@ExcelField(title="是否自动生成补货任务（预留扩展）", align=2, sort=11)
	public String getIsAutoRp() {
		return isAutoRp;
	}

	public void setIsAutoRp(String isAutoRp) {
		this.isAutoRp = isAutoRp;
	}
	
	@ExcelField(title="是否补被占用库存", align=2, sort=12)
	public String getIsRpAlloc() {
		return isRpAlloc;
	}

	public void setIsRpAlloc(String isRpAlloc) {
		this.isRpAlloc = isRpAlloc;
	}
	
	@ExcelField(title="是否超量补货", align=2, sort=13)
	public String getIsOverRp() {
		return isOverRp;
	}

	public void setIsOverRp(String isOverRp) {
		this.isOverRp = isOverRp;
	}
	
	@ExcelField(title="是否从存储位补货", align=2, sort=14)
	public String getIsFmRs() {
		return isFmRs;
	}

	public void setIsFmRs(String isFmRs) {
		this.isFmRs = isFmRs;
	}
	
	@ExcelField(title="是否从箱拣货位补货", align=2, sort=15)
	public String getIsFmCs() {
		return isFmCs;
	}

	public void setIsFmCs(String isFmCs) {
		this.isFmCs = isFmCs;
	}

	@ExcelField(title="分公司", align=2, sort=23)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="商品Id", align=2, sort=24)
	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
	
}