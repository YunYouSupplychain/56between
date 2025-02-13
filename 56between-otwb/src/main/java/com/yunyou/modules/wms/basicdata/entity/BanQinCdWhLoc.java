package com.yunyou.modules.wms.basicdata.entity;

import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 库位Entity
 * @author WMJ
 * @version 2019-01-25
 */
public class BanQinCdWhLoc extends DataEntity<BanQinCdWhLoc> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title = "库位编码**必填")
	private String locCode;
	@ExcelField(title = "库区编码**必填")
	private String zoneCode;
	@ExcelField(title = "库区名称", type = 1)
	private String zoneName;
	@ExcelField(title = "状态**填写\n正常\n冻结", dictType = "SYS_WM_LOC_STATUS")
	private String status;
	@ExcelField(title = "是否启用", type = 1, dictType = "SYS_YES_NO")
	private String isEnable;
	@ExcelField(title = "库位种类**必填 填写\n平面仓库位\n货架仓库位\n高架仓库位\n重力式货架库位\n堆垛机货架库位", dictType = "SYS_WM_CATEGORY")
	private String category;
	@ExcelField(title = "库位使用类型**必填 填写\n存储库位\n箱拣货库位\n件拣货库位\n箱件合并拣货库位\n过渡库位\n理货站\n品质库位\n加工台\n越库库位", dictType = "SYS_WM_LOC_USE_TYPE")
	private String locUseType;
	@ExcelField(title = "上架顺序**必填")
	private String paSeq;
	@ExcelField(title = "拣货顺序**必填")
	private String pkSeq;
	@ExcelField(title = "库位ABC")
	private String abc;
	@ExcelField(title = "长**必须是数字")
	private Double length;
	@ExcelField(title = "宽**必须是数字")
	private Double width;
	@ExcelField(title = "高**必须是数字")
	private Double height;
	@ExcelField(title = "通道")
	private String lane;
	@ExcelField(title = "序号**必须是整数")
	private Long seq;
	@ExcelField(title = "层**必须是整数")
	private Long floor;
	@ExcelField(title = "库位组")
	private String locGroup;
	@ExcelField(title = "是否允许混商品**填写\n是\n否", dictType = "SYS_YES_NO")
	private String isMixSku;
	@ExcelField(title = "最大混商品数量**必须是整数")
	private Long maxMixSku;
	@ExcelField(title = "是否允许混批次**填写\n是\n否", dictType = "SYS_YES_NO")
	private String isMixLot;
	@ExcelField(title = "最大混批次数量**必须是整数")
	private Long maxMixLot;
	@ExcelField(title = "是否忽略跟踪号**填写\n是\n否", dictType = "SYS_YES_NO")
	private String isLoseId;
	@ExcelField(title = "最大重量**必须是数字")
	private Double maxWeight;
	@ExcelField(title = "最大体积**必须是数字")
	private Double maxCubic;
	@ExcelField(title = "最大托盘数**必须是数字")
	private Double maxPl;
	@ExcelField(title = "X坐标**必须是数字")
	private Double x;
	@ExcelField(title = "Y坐标**必须是数字")
	private Double y;
	@ExcelField(title = "Z坐标**必须是数字")
	private Double z;
	@ExcelField(title = "自定义1")
	private String def1;
	@ExcelField(title = "自定义2")
	private String def2;
	@ExcelField(title = "自定义3")
	private String def3;
	@ExcelField(title = "自定义4")
	private String def4;
	@ExcelField(title = "自定义5")
	private String def5;
	@ExcelField(title = "自定义6")
	private String def6;
	@ExcelField(title = "自定义7")
	private String def7;
	@ExcelField(title = "自定义8")
	private String def8;
	@ExcelField(title = "自定义9")
	private String def9;
	@ExcelField(title = "自定义10")
	private String def10;
	private String orgId;		// 分公司
	private String createStatus; // 生成状态

	public BanQinCdWhLoc() {
		super();
		this.recVer = 0;
	}

	public BanQinCdWhLoc(String id){
		super(id);
	}

	@NotNull(message="库位编码不能为空")
	public String getLocCode() {
		return locCode;
	}

	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}

	@NotNull(message="库区编码不能为空")
	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@NotNull(message="库位使用类型不能为空")
	public String getLocUseType() {
		return locUseType;
	}

	public void setLocUseType(String locUseType) {
		this.locUseType = locUseType;
	}

	@NotNull(message="上架顺序不能为空")
	public String getPaSeq() {
		return paSeq;
	}

	public void setPaSeq(String paSeq) {
		this.paSeq = paSeq;
	}

	@NotNull(message="拣货顺序不能为空")
	public String getPkSeq() {
		return pkSeq;
	}

	public void setPkSeq(String pkSeq) {
		this.pkSeq = pkSeq;
	}

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getLane() {
		return lane;
	}

	public void setLane(String lane) {
		this.lane = lane;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getFloor() {
		return floor;
	}

	public void setFloor(Long floor) {
		this.floor = floor;
	}

	public String getLocGroup() {
		return locGroup;
	}

	public void setLocGroup(String locGroup) {
		this.locGroup = locGroup;
	}

	public String getIsMixSku() {
		return isMixSku;
	}

	public void setIsMixSku(String isMixSku) {
		this.isMixSku = isMixSku;
	}

	public Long getMaxMixSku() {
		return maxMixSku;
	}

	public void setMaxMixSku(Long maxMixSku) {
		this.maxMixSku = maxMixSku;
	}

	public String getIsMixLot() {
		return isMixLot;
	}

	public void setIsMixLot(String isMixLot) {
		this.isMixLot = isMixLot;
	}

	public Long getMaxMixLot() {
		return maxMixLot;
	}

	public void setMaxMixLot(Long maxMixLot) {
		this.maxMixLot = maxMixLot;
	}

	public String getIsLoseId() {
		return isLoseId;
	}

	public void setIsLoseId(String isLoseId) {
		this.isLoseId = isLoseId;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMaxCubic() {
		return maxCubic;
	}

	public void setMaxCubic(Double maxCubic) {
		this.maxCubic = maxCubic;
	}

	public Double getMaxPl() {
		return maxPl;
	}

	public void setMaxPl(Double maxPl) {
		this.maxPl = maxPl;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
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

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCreateStatus() {
		return createStatus;
	}

	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
}