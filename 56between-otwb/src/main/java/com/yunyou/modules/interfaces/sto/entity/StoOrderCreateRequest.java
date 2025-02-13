package com.yunyou.modules.interfaces.sto.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 订单创建接口Request
 */
public class StoOrderCreateRequest implements Serializable {
    private static final long serialVersionUID = -4148970143536303102L;
    // 订单号（客户系统自己生成，唯一）
    private String orderNo;
    // 订单来源（订阅服务时填写的来源编码）
    private String orderSource;
    // 获取面单的类型（00-普通、03-国际、01-代收、02-到付、04-生鲜）
    private String billType;
    // 订单类型（01-普通订单、02-调度订单）无特殊业务，都传01
    private String orderType;
    // 寄件人信息
    private SenderDO sender;
    // 收件人信息
    private ReceiverDO receiver;
    // 包裹信息
    private CargoDO cargo;
    // 客户信息
    private CustomerDO customer;
    // 国际订单附属信息（国际订单必填）
    private InternationalAnnexDO internationalAnnex;
    // 运单号（下单前已获取运单号时必传，否则不传或传NULL）
    private String waybillNo;
    // 指定网点揽收（非调度业务不传）
    private AssignAnnex assignAnnex;
    // 代收货款金额，单位：元（代收货款业务时必填）
    private String codValue;
    // 到付运费金额，单位：元（到付业务时必填）
    private String freightCollectValue;
    // 时效类型（01-普通）
    private String timelessType;
    // 产品类型 （01-普通、02-冷链、03-生鲜）
    private String productType;
    // 增值服务（DELIVER_CONTACT-派前电联,TRACE_PUSH-轨迹回传）
    private List<String> serviceTypeList;
    // 拓展字段（注意事项:属性值有逗号等于号需过滤掉）
    private Map<String, String> extendFieldMap;
    // 备注
    private String remark;
    // 快递流向（01-正向订单)默认01
    private String expressDirection;
    // 创建原因（01-客户创建）默认01
    private String createChannel;
    // 区域类型（01-国内）默认01
    private String regionType;
    // 保价模型（保价服务必填）
    private InsuredAnnexDo insuredAnnex;
    // 预估费用
    private String expectValue;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public SenderDO getSender() {
        return sender;
    }

    public void setSender(SenderDO sender) {
        this.sender = sender;
    }

    public ReceiverDO getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDO receiver) {
        this.receiver = receiver;
    }

    public CargoDO getCargo() {
        return cargo;
    }

    public void setCargo(CargoDO cargo) {
        this.cargo = cargo;
    }

    public CustomerDO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDO customer) {
        this.customer = customer;
    }

    public InternationalAnnexDO getInternationalAnnex() {
        return internationalAnnex;
    }

    public void setInternationalAnnex(InternationalAnnexDO internationalAnnex) {
        this.internationalAnnex = internationalAnnex;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public AssignAnnex getAssignAnnex() {
        return assignAnnex;
    }

    public void setAssignAnnex(AssignAnnex assignAnnex) {
        this.assignAnnex = assignAnnex;
    }

    public String getCodValue() {
        return codValue;
    }

    public void setCodValue(String codValue) {
        this.codValue = codValue;
    }

    public String getFreightCollectValue() {
        return freightCollectValue;
    }

    public void setFreightCollectValue(String freightCollectValue) {
        this.freightCollectValue = freightCollectValue;
    }

    public String getTimelessType() {
        return timelessType;
    }

    public void setTimelessType(String timelessType) {
        this.timelessType = timelessType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public List<String> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<String> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public Map<String, String> getExtendFieldMap() {
        return extendFieldMap;
    }

    public void setExtendFieldMap(Map<String, String> extendFieldMap) {
        this.extendFieldMap = extendFieldMap;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExpressDirection() {
        return expressDirection;
    }

    public void setExpressDirection(String expressDirection) {
        this.expressDirection = expressDirection;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public InsuredAnnexDo getInsuredAnnex() {
        return insuredAnnex;
    }

    public void setInsuredAnnex(InsuredAnnexDo insuredAnnex) {
        this.insuredAnnex = insuredAnnex;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }
}