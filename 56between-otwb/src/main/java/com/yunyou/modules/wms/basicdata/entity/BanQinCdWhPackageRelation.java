package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.core.persistence.DataEntity;
import com.yunyou.common.utils.excel.annotation.ExcelField;

/**
 * 包装明细Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhPackageRelation extends DataEntity<BanQinCdWhPackageRelation> {
	
	private static final long serialVersionUID = 1L;
	private String cdprUnit;		// 单位
	private String cdprUnitRate;		// 换算比例
	private String cdprIsMain;		// 是否主换算单位
	private String cdprSequencesNo;		// 序号
	private String cdprUnitLevel;		// 单位级别
	private Double cdprQuantity;		// 数量
	private String cdprDesc;		// 包装描述
	private String cdprMaterial;		// 包装材料
	private String cdprIsPackBox;		// 装箱
	private String cdprIsLableIn;		// 入库标签
	private String cdprIsLableOut;		// 出库标签
	private Double cdprLength;		// 长度
	private Double cdprWidth;		// 宽度
	private Double cdprHighth;		// 高度
	private Double cdprVolume;		// 体积
	private Double cdprWeight;		// 重量
	private String cdprWhCode;		// 仓库
	private String orgId;		// 分公司
	private Double cdprTi;		// 托盘每层可堆放箱数
	private Double cdprHi;		// 托盘可堆放层数
	private String pmCode;		// 代码
	private String cdprCdpaPmCode;		// 包装代码(平台级别)
	private String cdprPaId;		// 包装外键
    private String cdprIsDefault;		// 是否默认
    private String cdprIsReview;		// 是否需要复核
    private String packCode; // 包装代码用作查询
	private String codeAndName; // 模糊查询字段
	
	public BanQinCdWhPackageRelation() {
		super();
		this.cdprLength = 0.00d;
		this.cdprWidth = 0.00d;
		this.cdprHighth = 0.00d;
		this.cdprVolume = 0.00d;
		this.cdprWeight = 0.00d;
		this.cdprTi = 0d;
		this.cdprHi = 0d;
		this.recVer = 0;
	}

	public BanQinCdWhPackageRelation(String id){
		super(id);
	}

	@ExcelField(title="单位", align=2, sort=1)
	public String getCdprUnit() {
		return cdprUnit;
	}

	public void setCdprUnit(String cdprUnit) {
		this.cdprUnit = cdprUnit;
	}
	
	@ExcelField(title="换算比例", align=2, sort=2)
	public String getCdprUnitRate() {
		return cdprUnitRate;
	}

	public void setCdprUnitRate(String cdprUnitRate) {
		this.cdprUnitRate = cdprUnitRate;
	}
	
	@ExcelField(title="是否主换算单位", align=2, sort=3)
	public String getCdprIsMain() {
		return cdprIsMain;
	}

	public void setCdprIsMain(String cdprIsMain) {
		this.cdprIsMain = cdprIsMain;
	}
	
	@ExcelField(title="序号", align=2, sort=4)
	public String getCdprSequencesNo() {
		return cdprSequencesNo;
	}

	public void setCdprSequencesNo(String cdprSequencesNo) {
		this.cdprSequencesNo = cdprSequencesNo;
	}
	
	@ExcelField(title="单位级别", align=2, sort=5)
	public String getCdprUnitLevel() {
		return cdprUnitLevel;
	}

	public void setCdprUnitLevel(String cdprUnitLevel) {
		this.cdprUnitLevel = cdprUnitLevel;
	}
	
	@ExcelField(title="数量", align=2, sort=6)
	public Double getCdprQuantity() {
		return cdprQuantity;
	}

	public void setCdprQuantity(Double cdprQuantity) {
		this.cdprQuantity = cdprQuantity;
	}
	
	@ExcelField(title="包装描述", align=2, sort=7)
	public String getCdprDesc() {
		return cdprDesc;
	}

	public void setCdprDesc(String cdprDesc) {
		this.cdprDesc = cdprDesc;
	}
	
	@ExcelField(title="包装材料", align=2, sort=8)
	public String getCdprMaterial() {
		return cdprMaterial;
	}

	public void setCdprMaterial(String cdprMaterial) {
		this.cdprMaterial = cdprMaterial;
	}
	
	@ExcelField(title="装箱", align=2, sort=9)
	public String getCdprIsPackBox() {
		return cdprIsPackBox;
	}

	public void setCdprIsPackBox(String cdprIsPackBox) {
		this.cdprIsPackBox = cdprIsPackBox;
	}
	
	@ExcelField(title="入库标签", align=2, sort=10)
	public String getCdprIsLableIn() {
		return cdprIsLableIn;
	}

	public void setCdprIsLableIn(String cdprIsLableIn) {
		this.cdprIsLableIn = cdprIsLableIn;
	}
	
	@ExcelField(title="出库标签", align=2, sort=11)
	public String getCdprIsLableOut() {
		return cdprIsLableOut;
	}

	public void setCdprIsLableOut(String cdprIsLableOut) {
		this.cdprIsLableOut = cdprIsLableOut;
	}
	
	@ExcelField(title="长度", align=2, sort=12)
	public Double getCdprLength() {
		return cdprLength;
	}

	public void setCdprLength(Double cdprLength) {
		this.cdprLength = cdprLength;
	}
	
	@ExcelField(title="宽度", align=2, sort=13)
	public Double getCdprWidth() {
		return cdprWidth;
	}

	public void setCdprWidth(Double cdprWidth) {
		this.cdprWidth = cdprWidth;
	}
	
	@ExcelField(title="高度", align=2, sort=14)
	public Double getCdprHighth() {
		return cdprHighth;
	}

	public void setCdprHighth(Double cdprHighth) {
		this.cdprHighth = cdprHighth;
	}
	
	@ExcelField(title="体积", align=2, sort=15)
	public Double getCdprVolume() {
		return cdprVolume;
	}

	public void setCdprVolume(Double cdprVolume) {
		this.cdprVolume = cdprVolume;
	}
	
	@ExcelField(title="重量", align=2, sort=16)
	public Double getCdprWeight() {
		return cdprWeight;
	}

	public void setCdprWeight(Double cdprWeight) {
		this.cdprWeight = cdprWeight;
	}
	
	@ExcelField(title="仓库", align=2, sort=17)
	public String getCdprWhCode() {
		return cdprWhCode;
	}

	public void setCdprWhCode(String cdprWhCode) {
		this.cdprWhCode = cdprWhCode;
	}

	@ExcelField(title="分公司", align=2, sort=25)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@ExcelField(title="托盘每层可堆放箱数", align=2, sort=26)
	public Double getCdprTi() {
		return cdprTi;
	}

	public void setCdprTi(Double cdprTi) {
		this.cdprTi = cdprTi;
	}
	
	@ExcelField(title="托盘可堆放层数", align=2, sort=27)
	public Double getCdprHi() {
		return cdprHi;
	}

	public void setCdprHi(Double cdprHi) {
		this.cdprHi = cdprHi;
	}
	
	@ExcelField(title="代码", align=2, sort=28)
	public String getPmCode() {
		return pmCode;
	}

	public void setPmCode(String pmCode) {
		this.pmCode = pmCode;
	}
	
	@ExcelField(title="包装代码(平台级别)", align=2, sort=29)
	public String getCdprCdpaPmCode() {
		return cdprCdpaPmCode;
	}

	public void setCdprCdpaPmCode(String cdprCdpaPmCode) {
		this.cdprCdpaPmCode = cdprCdpaPmCode;
	}
	
	@ExcelField(title="包装外键", align=2, sort=30)
	public String getCdprPaId() {
		return cdprPaId;
	}

	public void setCdprPaId(String cdprPaId) {
		this.cdprPaId = cdprPaId;
	}

    public String getCdprIsDefault() {
        return cdprIsDefault;
    }

    public void setCdprIsDefault(String cdprIsDefault) {
        this.cdprIsDefault = cdprIsDefault;
    }

    public String getCdprIsReview() {
        return cdprIsReview;
    }

    public void setCdprIsReview(String cdprIsReview) {
        this.cdprIsReview = cdprIsReview;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

	public String getCodeAndName() {
		return codeAndName;
	}

	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}
}