package com.yunyou.modules.tms.basic.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 商品信息Entity
 * @author liujianhua
 * @version 2020-02-20
 */
public class TmItem extends DataEntity<TmItem> {
	
	private static final long serialVersionUID = 1L;
	private String skuCode;		// 商品编码
	private String skuName;		// 商品名称
	private String ownerCode;	// 货主编码
	private String skuType;		// 商品类型
	private String skuClass;    // 品类
	private Double grossweight;	// 毛重
	private Double netweight;	// 净重
	private Double cubic;		// 体积
	private Double length;		// 长
	private Double width;		// 宽
	private Double height;		// 高
	private Double price;		// 单价
	private String currency;	// 币种
	private String unit;		// 单位
	private String orgId;		// 机构ID
    private String skuModel;    // 商品型号
    private String def1;
    private String def2;
    private String def3;
    private String def4;
    private String def5;

	public TmItem() {
		super();
	}

	public TmItem(String id){
		super(id);
	}

	public TmItem(String ownerCode, String skuCode, String orgId) {
		this.ownerCode = ownerCode;
		this.skuCode = skuCode;
		this.orgId = orgId;
	}

	@ExcelField(title="商品编码", align=2, sort=7)
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	@ExcelField(title="商品名称", align=2, sort=8)
	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	@ExcelField(title="货主编码", align=2, sort=9)
	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	@ExcelField(title="商品类型", dictType="TMS_SKU_TYPE", align=2, sort=10)
	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}

	public String getSkuClass() {
		return skuClass;
	}

	public void setSkuClass(String skuClass) {
		this.skuClass = skuClass;
	}

	@ExcelField(title="毛重", align=2, sort=11)
	public Double getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(Double grossweight) {
		this.grossweight = grossweight;
	}
	
	@ExcelField(title="净重", align=2, sort=12)
	public Double getNetweight() {
		return netweight;
	}

	public void setNetweight(Double netweight) {
		this.netweight = netweight;
	}
	
	@ExcelField(title="体积", align=2, sort=13)
	public Double getCubic() {
		return cubic;
	}

	public void setCubic(Double cubic) {
		this.cubic = cubic;
	}
	
	@ExcelField(title="长", align=2, sort=14)
	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}
	
	@ExcelField(title="宽", align=2, sort=15)
	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}
	
	@ExcelField(title="高", align=2, sort=16)
	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}
	
	@ExcelField(title="单价", align=2, sort=17)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@ExcelField(title="币种", dictType="SYS_CURRENCY", align=2, sort=18)
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@ExcelField(title="单位", dictType="TMS_UNIT", align=2, sort=19)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="机构ID", align=2, sort=20)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getSkuModel() {
        return skuModel;
    }

    public void setSkuModel(String skuModel) {
        this.skuModel = skuModel;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }
}