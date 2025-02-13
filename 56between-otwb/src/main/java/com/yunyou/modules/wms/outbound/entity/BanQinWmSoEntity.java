package com.yunyou.modules.wms.outbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 描述：出库单扩展Entity
 *
 * @auther: Jianhua on 2019/2/14
 */
public class BanQinWmSoEntity extends BanQinWmSoHeader {

    // 货主名称
    private String ownerName;
    // 承运商名称
    private String carrierName;
    // 结算人名称
    private String settleName;
    // 结算人电话
    private String settleTel;
    // 结算人传真
    private String settleFax;
    // 结算人行业类型
    private String settleIndustryType;
    // 结算人地址
    private String settleAddress;
    // 路线名称
    private String lineName;
    // 波次单号
    private String waveNo;
    // 波次生成规格
    private String waveRuleCode;
    //
    private String orgName;
    // 商品明细列表
    private List<BanQinWmSoDetailEntity> detailList;
    // 预配明细列表
    private List<BanQinWmSoPreallocEntity> preallocList;
    // 分配明细列表
    private List<BanQinWmSoAllocEntity> allocList;
    // 商品明细
    private BanQinWmSoDetailEntity detailEntity;
    // 预配明细
    private BanQinWmSoPreallocEntity preallocEntity;
    // 分配明细
    private BanQinWmSoAllocEntity allocEntity;
    // RF查询用
    private String checkStatus;
    private Double qtySku;
    private Double qtyTotal;
    // 面单用
    private String ownerShortName;
    private String ownerTel;
    private String ownerAddress;
    private String ownerPostCode;
    private String ownerArea;
    private String ownerRemarks;
    private String orderSeq;

    // 查询条件
    private List<String> soTypeList;
    private List<String> statusList;
    private List<String> auditStatusList;
    private List<String> ownerCodeList;
    private List<String> skuCodeList;
    private List<String> priorityList;
    private List<String> interceptStatusList;
    private List<String> holdStatusList;
    private List<String> carrierCodeList;
    private List<String> consigneeCodeList;
    private List<String> settleCodeList;
    private List<String> lineList;
    private List<String> ldStatusList;
    private String isLd;
    private String saleNo;
    private String saleLineNo;
    private String skuCode;
    private List<String> customerNoList;
    private List<String> extendNoList;
    private List<String> soNoList;

    private MultipartFile customerNoFile;
    private MultipartFile extendNoFile;

    private Date beginOrderTime;	// 开始 订单时间
    private Date endOrderTime;		// 结束 订单时间
    private Date createDateFm;      // 创建时间从
    private Date endDateTo;         // 创建时间到

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

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public String getSettleTel() {
        return settleTel;
    }

    public void setSettleTel(String settleTel) {
        this.settleTel = settleTel;
    }

    public String getSettleFax() {
        return settleFax;
    }

    public void setSettleFax(String settleFax) {
        this.settleFax = settleFax;
    }

    public String getSettleIndustryType() {
        return settleIndustryType;
    }

    public void setSettleIndustryType(String settleIndustryType) {
        this.settleIndustryType = settleIndustryType;
    }

    public String getSettleAddress() {
        return settleAddress;
    }

    public void setSettleAddress(String settleAddress) {
        this.settleAddress = settleAddress;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getWaveNo() {
        return waveNo;
    }

    public void setWaveNo(String waveNo) {
        this.waveNo = waveNo;
    }

    public List<BanQinWmSoDetailEntity> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<BanQinWmSoDetailEntity> detailList) {
        this.detailList = detailList;
    }

    public List<BanQinWmSoPreallocEntity> getPreallocList() {
        return preallocList;
    }

    public void setPreallocList(List<BanQinWmSoPreallocEntity> preallocList) {
        this.preallocList = preallocList;
    }

    public List<BanQinWmSoAllocEntity> getAllocList() {
        return allocList;
    }

    public void setAllocList(List<BanQinWmSoAllocEntity> allocList) {
        this.allocList = allocList;
    }

    public BanQinWmSoDetailEntity getDetailEntity() {
        return detailEntity;
    }

    public void setDetailEntity(BanQinWmSoDetailEntity detailEntity) {
        this.detailEntity = detailEntity;
    }

    public BanQinWmSoPreallocEntity getPreallocEntity() {
        return preallocEntity;
    }

    public void setPreallocEntity(BanQinWmSoPreallocEntity preallocEntity) {
        this.preallocEntity = preallocEntity;
    }

    public BanQinWmSoAllocEntity getAllocEntity() {
        return allocEntity;
    }

    public void setAllocEntity(BanQinWmSoAllocEntity allocEntity) {
        this.allocEntity = allocEntity;
    }

    public List<String> getSoTypeList() {
        return soTypeList;
    }

    public void setSoTypeList(List<String> soTypeList) {
        this.soTypeList = soTypeList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getAuditStatusList() {
        return auditStatusList;
    }

    public void setAuditStatusList(List<String> auditStatusList) {
        this.auditStatusList = auditStatusList;
    }

    public List<String> getOwnerCodeList() {
        return ownerCodeList;
    }

    public void setOwnerCodeList(List<String> ownerCodeList) {
        this.ownerCodeList = ownerCodeList;
    }

    public List<String> getSkuCodeList() {
        return skuCodeList;
    }

    public void setSkuCodeList(List<String> skuCodeList) {
        this.skuCodeList = skuCodeList;
    }

    public List<String> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<String> priorityList) {
        this.priorityList = priorityList;
    }

    public List<String> getInterceptStatusList() {
        return interceptStatusList;
    }

    public void setInterceptStatusList(List<String> interceptStatusList) {
        this.interceptStatusList = interceptStatusList;
    }

    public List<String> getHoldStatusList() {
        return holdStatusList;
    }

    public void setHoldStatusList(List<String> holdStatusList) {
        this.holdStatusList = holdStatusList;
    }

    public List<String> getCarrierCodeList() {
        return carrierCodeList;
    }

    public void setCarrierCodeList(List<String> carrierCodeList) {
        this.carrierCodeList = carrierCodeList;
    }

    public List<String> getConsigneeCodeList() {
        return consigneeCodeList;
    }

    public void setConsigneeCodeList(List<String> consigneeCodeList) {
        this.consigneeCodeList = consigneeCodeList;
    }

    public List<String> getSettleCodeList() {
        return settleCodeList;
    }

    public void setSettleCodeList(List<String> settleCodeList) {
        this.settleCodeList = settleCodeList;
    }

    public List<String> getLineList() {
        return lineList;
    }

    public void setLineList(List<String> lineList) {
        this.lineList = lineList;
    }

    public List<String> getLdStatusList() {
        return ldStatusList;
    }

    public void setLdStatusList(List<String> ldStatusList) {
        this.ldStatusList = ldStatusList;
    }

    public String getIsLd() {
        return isLd;
    }

    public void setIsLd(String isLd) {
        this.isLd = isLd;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getSaleLineNo() {
        return saleLineNo;
    }

    public void setSaleLineNo(String saleLineNo) {
        this.saleLineNo = saleLineNo;
    }

    public String getWaveRuleCode() {
        return waveRuleCode;
    }

    public void setWaveRuleCode(String waveRuleCode) {
        this.waveRuleCode = waveRuleCode;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Double getQtySku() {
        return qtySku;
    }

    public void setQtySku(Double qtySku) {
        this.qtySku = qtySku;
    }

    public Double getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(Double qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOwnerShortName() {
        return ownerShortName;
    }

    public void setOwnerShortName(String ownerShortName) {
        this.ownerShortName = ownerShortName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerPostCode() {
        return ownerPostCode;
    }

    public void setOwnerPostCode(String ownerPostCode) {
        this.ownerPostCode = ownerPostCode;
    }

    public String getOwnerArea() {
        return ownerArea;
    }

    public void setOwnerArea(String ownerArea) {
        this.ownerArea = ownerArea;
    }

    public String getOwnerRemarks() {
        return ownerRemarks;
    }

    public void setOwnerRemarks(String ownerRemarks) {
        this.ownerRemarks = ownerRemarks;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public List<String> getCustomerNoList() {
        return customerNoList;
    }

    public void setCustomerNoList(List<String> customerNoList) {
        this.customerNoList = customerNoList;
    }

    public List<String> getExtendNoList() {
        return extendNoList;
    }

    public void setExtendNoList(List<String> extendNoList) {
        this.extendNoList = extendNoList;
    }

    public MultipartFile getCustomerNoFile() {
        return customerNoFile;
    }

    public void setCustomerNoFile(MultipartFile customerNoFile) {
        this.customerNoFile = customerNoFile;
    }

    public MultipartFile getExtendNoFile() {
        return extendNoFile;
    }

    public void setExtendNoFile(MultipartFile extendNoFile) {
        this.extendNoFile = extendNoFile;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBeginOrderTime() {
        return beginOrderTime;
    }

    public void setBeginOrderTime(Date beginOrderTime) {
        this.beginOrderTime = beginOrderTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public Date getCreateDateFm() {
        return createDateFm;
    }

    public void setCreateDateFm(Date createDateFm) {
        this.createDateFm = createDateFm;
    }

    public Date getEndDateTo() {
        return endDateTo;
    }

    public void setEndDateTo(Date endDateTo) {
        this.endDateTo = endDateTo;
    }

    public List<String> getSoNoList() {
        return soNoList;
    }

    public void setSoNoList(List<String> soNoList) {
        this.soNoList = soNoList;
    }
}
