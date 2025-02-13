package com.yunyou.modules.oms.basic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品价格Entity
 * @author Jianhua Liu
 * @version 2019-05-20
 */
public class OmItemPrice extends DataEntity<OmItemPrice> {
	
	private static final long serialVersionUID = 1L;
	private String customerNo;// 客户编码
	private String skuCode;// 商品编码
	private String channel;// 渠道
	private String priceType;// 价格类型
	private BigDecimal purchaseMultiple;// 采购倍数
	private BigDecimal saleMultiple;// 销售倍数
	private String unit;// 单位(不含税)
	private String auxiliaryUnit;// 辅助单位
	private BigDecimal discount;// 折扣(比例)
	private BigDecimal taxPrice;// 含税单价
	private BigDecimal price;// 单价
	private Date effectiveTime;// 生效时间
	private Date expirationTime;// 失效时间
	private String isEnable;// 是否启用
	private String isAllowAdjustment;// 是否允许调整
	private String orgId;// 机构ID
	private BigDecimal convertRatio;// 换算比例
	private Double taxRate;         // 税率
	private String auditStatus;		// 审核状态
    private Date auditDate;     // 审核时间
    private String auditor;		// 审核人
	private String logisticsMuqType; // 物流管理单位数量种类(辅助单位数量/单位数量)
	private String ownerCode;       // 货主编码

	public OmItemPrice() {
		super();
	}

	public OmItemPrice(String id){
		super(id);
	}

	@ExcelField(title="客户编码", align=2, sort=7)
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	@ExcelField(title="商品编码", align=2, sort=8)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="渠道", dictType="OMS_CHANNEL", align=2, sort=9)
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	@ExcelField(title="价格类型", dictType="OMS_PRICE_TYPE", align=2, sort=10)
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	@ExcelField(title="采购倍数", align=2, sort=11)
	public BigDecimal getPurchaseMultiple() {
		return purchaseMultiple;
	}

	public void setPurchaseMultiple(BigDecimal purchaseMultiple) {
		this.purchaseMultiple = purchaseMultiple;
	}
	
	@ExcelField(title="销售倍数", align=2, sort=12)
	public BigDecimal getSaleMultiple() {
		return saleMultiple;
	}

	public void setSaleMultiple(BigDecimal saleMultiple) {
		this.saleMultiple = saleMultiple;
	}
	
	@ExcelField(title="单位(不含税)", align=2, sort=13)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="辅助单位", align=2, sort=14)
	public String getAuxiliaryUnit() {
		return auxiliaryUnit;
	}

	public void setAuxiliaryUnit(String auxiliaryUnit) {
		this.auxiliaryUnit = auxiliaryUnit;
	}
	
	@NotNull(message="折扣(比例)不能为空")
	@ExcelField(title="折扣(比例)", align=2, sort=15)
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	@NotNull(message="含税单价不能为空")
	@ExcelField(title="含税单价", align=2, sort=16)
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}
	
	@NotNull(message="单价不能为空")
	@ExcelField(title="单价", align=2, sort=17)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="生效时间不能为空")
	@ExcelField(title="生效时间", align=2, sort=18)
	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="失效时间", align=2, sort=19)
	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	@ExcelField(title="是否启用", dictType="SYS_YES_NO", align=2, sort=20)
	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	@ExcelField(title="是否允许调整", dictType="SYS_YES_NO", align=2, sort=21)
	public String getIsAllowAdjustment() {
		return isAllowAdjustment;
	}

	public void setIsAllowAdjustment(String isAllowAdjustment) {
		this.isAllowAdjustment = isAllowAdjustment;
	}
	
	@ExcelField(title="平台ID", align=2, sort=22)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public BigDecimal getConvertRatio() {
		return convertRatio;
	}

	public void setConvertRatio(BigDecimal convertRatio) {
		this.convertRatio = convertRatio;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

	public String getLogisticsMuqType() {
		return logisticsMuqType;
	}

	public void setLogisticsMuqType(String logisticsMuqType) {
		this.logisticsMuqType = logisticsMuqType;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
}