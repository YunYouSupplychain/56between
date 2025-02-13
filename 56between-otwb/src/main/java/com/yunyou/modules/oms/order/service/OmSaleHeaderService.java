package com.yunyou.modules.oms.order.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.*;
import com.yunyou.modules.oms.order.mapper.OmSaleDetailMapper;
import com.yunyou.modules.oms.order.mapper.OmSaleHeaderMapper;
import com.yunyou.modules.oms.report.entity.OmShipOrderLabel;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售订单Service
 *
 * @author WMJ
 * @version 2019-04-17
 */
@Service
@Transactional(readOnly = true)
public class OmSaleHeaderService extends CrudService<OmSaleHeaderMapper, OmSaleHeader> {
    @Autowired
    private OmSaleDetailMapper omSaleDetailMapper;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    /*@Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;*/

    @Override
    public OmSaleHeader get(String id) {
        OmSaleHeader omSaleHeader = super.get(id);
        if (null != omSaleHeader) {
            omSaleHeader.setOmSaleDetailList(omSaleDetailMapper.findList(new OmSaleDetail(null, id)));
        }
        return omSaleHeader;
    }

    public OmSaleHeaderEntity getEntity(String id) {
        OmSaleHeaderEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setOmSaleDetailList(omSaleDetailMapper.findDetailList(new OmSaleDetail(null, id)));
        }
        return entity;
    }

    public Page<OmSaleHeaderEntity> findPage(Page page, OmSaleHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void checkAndSave(OmSaleHeader omSaleHeader) {
        if (OmsConstants.OMS_SO_STATUS_30.equals(omSaleHeader.getStatus()) || OmsConstants.OMS_SO_STATUS_40.equals(omSaleHeader.getStatus())) {
            throw new OmsException("订单状态须为新建或确认");
        }
        if (omSaleHeader.getOrderDate() == null) {
            throw new OmsException("订单日期不能为空");
        }
        if (StringUtils.isBlank(omSaleHeader.getOwner())) {
            throw new OmsException("货主不能为空");
        }
        if (StringUtils.isBlank(omSaleHeader.getSaleType())) {
            throw new OmsException("订单类型不能为空");
        }
        if (StringUtils.isBlank(omSaleHeader.getOutWarhouse())) {
            throw new OmsException("下发机构不能为空");
        }
        save(omSaleHeader);
    }

    @Override
    @Transactional
    public void save(OmSaleHeader omSaleHeader) {
        if (StringUtils.isBlank(omSaleHeader.getSaleNo())) {
            omSaleHeader.setSaleNo(noService.getDocumentNo(GenNoType.OM_SALE_NO.name()));
        }
        User user = UserUtils.getUser();
        if (StringUtils.isBlank(omSaleHeader.getId())) {
            if (StringUtils.isBlank(omSaleHeader.getOrganization())) {
                omSaleHeader.setOrganization(user.getCompany() == null ? "" : user.getCompany().getId());
            }
            if (StringUtils.isBlank(omSaleHeader.getDepartment())) {
                omSaleHeader.setDepartment(user.getOffice() == null ? "" : user.getOffice().getId());
            }
            if (StringUtils.isBlank(omSaleHeader.getPreparedBy())) {
                omSaleHeader.setPreparedBy(user.getName());
            }
            omSaleHeader.setStatus(OmsConstants.OMS_SO_STATUS_20);
        }
        omSaleHeader.setUpdateOrgId(user.getCompany() == null ? "" : user.getCompany().getId());
        omSaleHeader.setUpdateDepartment(user.getOffice() == null ? "" : user.getOffice().getId());

        super.save(omSaleHeader);
        for (OmSaleDetail omSaleDetail : omSaleHeader.getOmSaleDetailList()) {
            if (omSaleDetail.getId() == null) {
                continue;
            }
            if (OmSaleDetail.DEL_FLAG_NORMAL.equals(omSaleDetail.getDelFlag())) {
                if (StringUtils.isEmpty(omSaleDetail.getId())) {
                    omSaleDetail.setHeaderId(omSaleHeader.getId());
                    omSaleDetail.setOrgId(omSaleHeader.getOrgId());
                    omSaleDetail.preInsert();
                    omSaleDetailMapper.insert(omSaleDetail);
                } else {
                    omSaleDetail.preUpdate();
                    omSaleDetailMapper.update(omSaleDetail);
                }
            } else {
                omSaleDetailMapper.delete(omSaleDetail);
            }
        }
//        updateAmount(omSaleHeader.getId());
    }

    @Override
    @Transactional
    public void delete(OmSaleHeader omSaleHeader) {
        if (!OmsConstants.OMS_SO_STATUS_20.equals(omSaleHeader.getStatus())) {
            throw new OmsException("非确认状态，不能删除");
        }
        super.delete(omSaleHeader);
        omSaleDetailMapper.delete(new OmSaleDetail(null, omSaleHeader.getId()));
    }

    /**
     * 描述：更新合计金额、税额、含税金额(仅接口保存使用，页面保存通过前台计算)
     *
     * @author Jianhua on 2019/5/15
     */
    @Transactional
    public void updateAmount(String id) {
        OmSaleHeader omSaleHeader = get(id);
        double amount = omSaleHeader.getOmSaleDetailList().stream().mapToDouble(o -> o.getAmount().doubleValue()).sum();
        double taxMoney = omSaleHeader.getOmSaleDetailList().stream().mapToDouble(o -> o.getTaxMoney().doubleValue()).sum();
        double taxAmount = omSaleHeader.getOmSaleDetailList().stream().mapToDouble(o -> o.getTaxAmount().doubleValue()).sum();
        BigDecimal freightCharge = omSaleHeader.getFreightCharge() == null ? BigDecimal.ZERO : omSaleHeader.getFreightCharge(); // 运费
        BigDecimal couponAmount = omSaleHeader.getCouponAmount() == null ? BigDecimal.ZERO : omSaleHeader.getCouponAmount();   // 优惠券金额
        omSaleHeader.setTotalAmount(BigDecimal.valueOf(amount));
        omSaleHeader.setTotalTax(BigDecimal.valueOf(taxMoney));
        omSaleHeader.setTotalTaxInAmount(BigDecimal.valueOf(taxAmount));
        omSaleHeader.setOrderSettleAmount(BigDecimal.valueOf(taxAmount).subtract(couponAmount).add(freightCharge));
        omSaleHeader.setSkuSettleAmount(BigDecimal.valueOf(taxAmount).subtract(couponAmount));
        mapper.updateAmount(omSaleHeader);
    }

    @Transactional
    public void updateStatus(String saleNo, String newStatus, User operator, String orgId) {
        OmSaleHeader omSaleHeader = new OmSaleHeader();
        omSaleHeader.setSaleNo(saleNo);
        omSaleHeader.setStatus(newStatus);
        omSaleHeader.setUpdateBy(operator);
        omSaleHeader.setUpdateDate(new Date());
        omSaleHeader.setUpdateOrgId(operator.getCompany() == null ? null : operator.getCompany().getId());
        omSaleHeader.setUpdateDepartment(operator.getOffice() == null ? null : operator.getOffice().getId());
        omSaleHeader.setOrgId(orgId);
        mapper.updateStatus(omSaleHeader);
    }

    @Transactional
    public void updateStatusById(String id, String newStatus, User operator) {
        OmSaleHeader omSaleHeader = new OmSaleHeader();
        omSaleHeader.setId(id);
        omSaleHeader.setStatus(newStatus);
        omSaleHeader.setUpdateBy(operator);
        omSaleHeader.setUpdateDate(new Date());
        omSaleHeader.setUpdateOrgId(operator.getCompany() == null ? null : operator.getCompany().getId());
        omSaleHeader.setUpdateDepartment(operator.getOffice() == null ? null : operator.getOffice().getId());
        mapper.updateStatusById(omSaleHeader);
    }

    @Transactional
    public void updateAuditStatus(String saleNo, String newStatus, User operator, String orgId) {
        OmSaleHeader omSaleHeader = new OmSaleHeader();
        omSaleHeader.setSaleNo(saleNo);
        omSaleHeader.setStatus(newStatus);
        if (OmsConstants.OMS_SO_STATUS_30.equals(newStatus)) {
            omSaleHeader.setAuditBy(operator.getName());
            omSaleHeader.setAuditDate(new Date());
            omSaleHeader.setAuditOrgId(operator.getCompany() == null ? null : operator.getCompany().getId());
            omSaleHeader.setAuditDepartment(operator.getOffice() == null ? "" : operator.getOffice().getId());
        }
        omSaleHeader.setUpdateBy(operator);
        omSaleHeader.setUpdateDate(new Date());
        omSaleHeader.setUpdateOrgId(operator.getCompany() == null ? null : operator.getCompany().getId());
        omSaleHeader.setUpdateDepartment(operator.getOffice() == null ? null : operator.getOffice().getId());
        omSaleHeader.setOrgId(orgId);
        mapper.updateAuditStatus(omSaleHeader);
    }

    /**
     * 描述：审核
     *
     * @author Jianhua on 2019/5/10
     */
    @Transactional
    public void audit(String id) {
        audit(get(id), UserUtils.getUser());
    }

    /**
     * 描述：审核
     *
     * @author Jianhua on 2019/5/10
     */
    @Transactional
    public void audit(OmSaleHeader omSaleHeader, User operator) {
        if (omSaleHeader == null) {
            throw new OmsException("记录不存在，无法审核");
        }
        if (!OmsConstants.OMS_SO_STATUS_20.equals(omSaleHeader.getStatus())) {
            throw new OmsException("非确认状态，无法审核");
        }
        if (CollectionUtil.isEmpty(omSaleHeader.getOmSaleDetailList())) {
            throw new OmsException("订单明细不能为空");
        }
        updateAuditStatus(omSaleHeader.getSaleNo(), OmsConstants.OMS_SO_STATUS_30, operator, omSaleHeader.getOrgId());
    }

    /**
     * 描述：取消审核
     *
     * @author Jianhua on 2019/5/10
     */
    @Transactional
    public void cancelAudit(String id) {
        cancelAudit(super.get(id), UserUtils.getUser());
    }

    /**
     * 描述：取消审核
     *
     * @author Jianhua on 2019/5/10
     */
    @Transactional
    public void cancelAudit(OmSaleHeader omSaleHeader, User operator) {
        if (omSaleHeader == null) {
            throw new OmsException("记录不存在，无法取消审核");
        }
        if (!OmsConstants.OMS_SO_STATUS_30.equals(omSaleHeader.getStatus())) {
            throw new OmsException("非审核状态，无法取消审核");
        }
        updateAuditStatus(omSaleHeader.getSaleNo(), OmsConstants.OMS_SO_STATUS_20, operator, omSaleHeader.getOrgId());
    }

    /**
     * 描述：生成供应链订单
     *
     * @author Jianhua on 2019/5/10
     */
    @Transactional
    public void createChainOrder(String id) {
        OmSaleHeader omSaleHeader = get(id);
        if (omSaleHeader == null) {
            throw new OmsException("记录不存在，无法生成供应链订单");
        }
        if (OmsConstants.OMS_SO_STATUS_40.equals(omSaleHeader.getStatus())) {
            throw new OmsException("不能重复生成供应链订单");
        }
        if (!OmsConstants.OMS_SO_STATUS_30.equals(omSaleHeader.getStatus())) {
            throw new OmsException("未审核，无法生成供应链订单");
        }
        User operator = UserUtils.getUser();
        // 销售任务下发机构ID（供应链订单所属机构ID）
        String orgId = omSaleHeader.getOutWarhouse();

        String businessOrderType = "";
        if (OmsConstants.OMS_SO_TYPE_01.equals(omSaleHeader.getSaleType())) {
            businessOrderType = OmsConstants.OMS_BUSINESS_ORDER_TYPE_02;
        } else if (OmsConstants.OMS_SO_TYPE_02.equals(omSaleHeader.getSaleType())) {
            businessOrderType = OmsConstants.OMS_BUSINESS_ORDER_TYPE_03;
        } else if (OmsConstants.OMS_SO_TYPE_03.equals(omSaleHeader.getSaleType())) {
            businessOrderType = OmsConstants.OMS_BUSINESS_ORDER_TYPE_08;
        } else {
            businessOrderType = "";
        }
        /*OmBusinessOrderTypeRelation businessOrderTypeRelation = omBusinessOrderTypeRelationService.getByBusinessOrderType(businessOrderType, orgId);
        if (businessOrderTypeRelation == null) {
            throw new OmsException("未维护业务订单类型的关联关系");
        }
        String chainType = businessOrderTypeRelation.getOrderType();*/

        BigDecimal totalAmount = BigDecimal.valueOf(0);
        BigDecimal totalTax = BigDecimal.valueOf(0);
        BigDecimal totalTaxInAmount = BigDecimal.valueOf(0);
        List<OmChainDetail> omChainDetails = Lists.newArrayList();
        for (OmSaleDetail omSaleDetail : omSaleHeader.getOmSaleDetailList()) {
            totalAmount = totalAmount.add(omSaleDetail.getAmount());
            totalTax = totalTax.add(omSaleDetail.getTaxMoney());
            totalTaxInAmount = totalTaxInAmount.add(omSaleDetail.getTaxAmount());

            OmChainDetail omChainDetail = new OmChainDetail();
            BeanUtils.copyProperties(omSaleDetail, omChainDetail);
            omChainDetail.setId("");
            omChainDetail.setHeaderId(null);
            omChainDetail.setCreateBy(operator);
            omChainDetail.setCreateDate(new Date());
            omChainDetail.setUpdateBy(operator);
            omChainDetail.setUpdateDate(new Date());
            omChainDetail.setOrgId(orgId);
            omChainDetails.add(omChainDetail);
        }

        OmChainHeader omChainHeader = new OmChainHeader();
        BeanUtils.copyProperties(omSaleHeader, omChainHeader);
        omChainHeader.setId("");
        omChainHeader.setChainNo(noService.getDocumentNo(GenNoType.OM_CHAIN_NO.name()));
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
        omChainHeader.setBusinessOrderType(businessOrderType);
        omChainHeader.setWarehouse(omSaleHeader.getOutWarhouse());
        omChainHeader.setAuditBy(null);
        omChainHeader.setCreateBy(operator);
        omChainHeader.setCreateDate(new Date());
        omChainHeader.setUpdateBy(operator);
        omChainHeader.setUpdateDate(new Date());
        omChainHeader.setTotalAmount(totalAmount);
        omChainHeader.setTotalTax(totalTax);
        omChainHeader.setTotalTaxInAmount(totalTaxInAmount);
        omChainHeader.setOmChainDetailList(omChainDetails);
        omChainHeader.setSourceOrderNo(omSaleHeader.getSaleNo());
        omChainHeader.setSourceOrderType(OmsConstants.OMS_SOURCE_TYPE_SO);
        omChainHeader.setIsAvailableStock(omSaleHeader.getIsAvailableStock());
        omChainHeader.setOrgId(orgId);
        omChainHeader.setSourceOrderId(omSaleHeader.getId());
        omChainHeader.setBusinessNo(omSaleHeader.getSaleNo());
        // 若为出库运输且委托方为空则客户即委托方
        if (StringUtils.isBlank(omChainHeader.getPrincipal())) {
            omChainHeader.setPrincipal(omChainHeader.getCustomer());
        }
        omChainHeaderService.save(omChainHeader);

        updateStatus(omSaleHeader.getSaleNo(), OmsConstants.OMS_SO_STATUS_40, operator, omSaleHeader.getOrgId());
    }

    public List<OmSaleDetail> findDetailsByOwnerAndSku(String owner, String skuCode, String warehouse, String status) {
        return omSaleDetailMapper.findDetailsByOwnerAndSku(owner, skuCode, warehouse, status);
    }

    @Transactional
    public void saveAll(List<OmSaleHeader> saveList) {
        for (OmSaleHeader header : saveList) {
            mapper.insert(header);
            List<OmSaleDetail> detailList = header.getOmSaleDetailList();
            omSaleDetailMapper.saveAll(detailList);
            updateAmount(header.getId());
        }
    }

    public Page<OmSaleHeaderEntity> getUnAssociatedPoData(Page page, OmSaleHeaderEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.getUnAssociatedPoData(entity));
        return page;
    }

    /**
     * 描述：附件上传，更新表中附件路径
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @Transactional
    public void updateAppAnnex(String id, String file) {
        mapper.updateAppAnnex(id, file);
    }

    @Transactional
    public void updateAnnex(String id, String file) {
        mapper.updateAnnex(id, file);
    }

    /**
     * 描述：整单折扣
     * <p>
     * create by Jianhua on 2019/7/31
     */
    public OmSaleHeaderEntity discount(String id, Double discountRate) {
        OmSaleHeaderEntity entity = getEntity(id);
        if (!(OmsConstants.OMS_SO_STATUS_10.equals(entity.getStatus()) || OmsConstants.OMS_SO_STATUS_20.equals(entity.getStatus()))) {
            throw new OmsException("非新建/确认状态不能进行整单折扣调整");
        }
        BigDecimal totalTaxInAmount = entity.getTotalTaxInAmount() == null ? BigDecimal.valueOf(0D) : entity.getTotalTaxInAmount();
        BigDecimal couponAmount = entity.getCouponAmount() == null ? BigDecimal.valueOf(0D) : entity.getCouponAmount();
        BigDecimal freightCharge = entity.getFreightCharge() == null ? BigDecimal.valueOf(0D) : entity.getFreightCharge();
        BigDecimal orderSettleAmount = (totalTaxInAmount.subtract(couponAmount)).multiply(BigDecimal.valueOf(discountRate)).add(freightCharge);

        entity.setUpdateOrgId(UserUtils.getUser().getCompany().getId());
        entity.setUpdateDepartment(UserUtils.getUser().getOffice().getId());
        entity.setDiscountRate(BigDecimal.valueOf(discountRate));
        entity.setOrderSettleAmount(orderSettleAmount);
        if (CollectionUtil.isEmpty(entity.getOmSaleDetailList())) {
            return entity;
        }
        for (OmSaleDetail omSaleDetail : entity.getOmSaleDetailList()) {
            omSaleDetail.setDiscount(BigDecimal.valueOf(discountRate));
            omSaleDetail.setSumTransactionPriceTax(omSaleDetail.getTaxPrice().multiply(omSaleDetail.getQty()).multiply(BigDecimal.valueOf(discountRate)));
        }
        return entity;
    }

    /**
     * 描述：关联PO
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @Transactional
    public void associatedPo(String[] saleNos, String poNo, String orgId) {
        mapper.associatedPo(saleNos, poNo, orgId);
    }

    /**
     * 描述：取消PO关联
     * <p>
     * create by Jianhua on 2019/7/31
     */
    @Transactional
    public void unAssociatedPo(String poNo, String orgId) {
        mapper.unAssociatedPo(poNo, orgId);
    }

    public List<OmShipOrderLabel> getShipOrder(List<String> soHeaderIds) {
        return mapper.getShipOrder(soHeaderIds);
    }

    @Transactional
    public void editRemarks(String id, String remarks) {
        OmSaleHeader omSaleHeader = mapper.get(id);
        omSaleHeader.setRemarks(remarks);
        mapper.update(omSaleHeader);
    }

    /**
     * 根据wms订单的状态，回刷更新销售单状态为完成
     */
    @Transactional
    public void updateFinishStatusByWmsStatus() {
        // 销售订单
        // 1 获取所有【已生成供应链订单】状态的销售订单
        OmSaleHeader saleCondition = new OmSaleHeader();
        saleCondition.setStatus(OmsConstants.OMS_SO_STATUS_40);
        List<OmSaleHeader> list = mapper.findList(saleCondition);
        for (OmSaleHeader saleHeader : list) {
            saleHeader = this.get(saleHeader.getId());
            List<OmSaleDetail> detailList = saleHeader.getOmSaleDetailList();
            Map<String, List<OmSaleDetail>> skuMap = detailList.stream().collect(Collectors.groupingBy(OmSaleDetail::getSkuCode));
            // 2 根据销售单号，分别去wms获取所有出库单状态
            String saleNo = saleHeader.getSaleNo();
            String getSql = "select CONCAT(wsd.sku_code,',',sum(wsd.qty_ship_ea)) from wm_so_detail wsd inner join wm_so_header wsh on wsd.head_id = wsh.id where wsh.def1 = '" + saleNo + "' group by wsd.sku_code";
            List<Object> objects = mapper.execSelectSql(getSql);
            if (CollectionUtil.isNotEmpty(objects)) {
                boolean isUpdate = true;
                if (skuMap.size() != objects.size()) {
                    isUpdate = false;
                }
                for (int i = 0; i < objects.size(); i++) {
                    String item = (String) objects.get(i);
                    String[] itemList = item.split(",");
                    String skuCode = itemList[0];
                    String shipQty = itemList[1];
                    List<OmSaleDetail> omSaleDetails = skuMap.get(skuCode);
                    if (CollectionUtil.isNotEmpty(omSaleDetails)) {
                        double sumQty = omSaleDetails.stream().mapToDouble(t -> t.getQty().doubleValue()).sum();
                        if (sumQty == Double.parseDouble(shipQty)) {
                            continue;
                        } else {
                            isUpdate = false;
                        }
                    } else {
                        isUpdate = false;
                    }
                }
                if (isUpdate) {
                    this.updateStatusById(saleHeader.getId(), OmsConstants.OMS_SO_STATUS_99, UserUtils.getSystemUser());
                }
            }
        }
    }

    /**
     * 描述：关闭订单
     *
     * @author zyf on 2019/8/13
     */
    @Transactional
    public void closeOrder(String id) {
        OmSaleHeader saleHeader = get(id);
        this.updateStatusById(saleHeader.getId(), OmsConstants.OMS_SO_STATUS_99, UserUtils.getUser());
    }

    /**
     * 描述：取wms所有未关闭的so单的面单号
     * @return
     */
    public Map<String, List<String>> findMailNo() {
        Map<String, List<String>> mailCustomerMap = Maps.newHashMap();
        String getSql = "select DISTINCT concat( wsa.tracking_no, ',', wsd.def6 )  from wm_so_alloc wsa LEFT JOIN wm_so_header wsh ON wsh.so_no = wsa.so_no AND wsh.org_id = wsa.org_id LEFT JOIN wm_so_detail wsd on wsa.so_no = wsd.so_no AND wsa.line_no = wsd.line_no and wsa.org_id = wsd.org_id where wsh.`status` != '99' and wsa.tracking_no is not null and wsa.tracking_no != ''";
        List<Object> objects = mapper.execSelectSql(getSql);
        if (CollectionUtil.isNotEmpty(objects)) {
            for (int i = 0; i < objects.size(); i++) {
                String item = (String) objects.get(i);
                String[] itemList = item.split(",");
                String mailNo = itemList[0];
                String customerNo = itemList[1];
                if (mailCustomerMap.containsKey(mailNo)) {
                    List<String> customerNoList = mailCustomerMap.get(mailNo);
                    customerNoList.add(customerNo);
                    mailCustomerMap.remove(mailNo);
                    mailCustomerMap.put(mailNo, customerNoList);
                } else {
                    List<String> customerNoList = Lists.newArrayList();
                    customerNoList.add(customerNo);
                    mailCustomerMap.put(mailNo, customerNoList);
                }

            }
        }
        return mailCustomerMap;
    }

    public List<Map<String, Object>> saleOrderCountAndAmountByDay(Date fromDate, Date toDate, String orgId) {
        return mapper.saleOrderCountAndAmountByDay(fromDate, toDate, orgId);
    }

    public Map<String, Object> findSaleOrderCountAndAmount(Date fromDate, Date toDate, String orgId) {
        return mapper.findSaleOrderCountAndAmount(fromDate, toDate, orgId);
    }
}