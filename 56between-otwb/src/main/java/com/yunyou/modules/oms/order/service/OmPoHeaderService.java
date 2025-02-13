package com.yunyou.modules.oms.order.service;

import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.oms.common.OmsConstants;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.*;
import com.yunyou.modules.oms.order.entity.extend.OmPoPrintData;
import com.yunyou.modules.oms.order.mapper.OmPoDetailMapper;
import com.yunyou.modules.oms.order.mapper.OmPoHeaderMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
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
 * 采购订单Service
 *
 * @author WMJ
 * @version 2019-04-16
 */
@Service
@Transactional(readOnly = true)
public class OmPoHeaderService extends CrudService<OmPoHeaderMapper, OmPoHeader> {
    @Autowired
    private OmPoDetailMapper omPoDetailMapper;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OmChainHeaderService omChainHeaderService;
    /*@Autowired
    private OmBusinessOrderTypeRelationService omBusinessOrderTypeRelationService;*/

    @Override
    public OmPoHeader get(String id) {
        OmPoHeader omPoHeader = super.get(id);
        if (null != omPoHeader) {
            omPoHeader.setOmPoDetailList(omPoDetailMapper.findList(new OmPoDetail(null, id)));
        }
        return omPoHeader;
    }

    public OmPoHeaderEntity getEntity(String id) {
        OmPoHeaderEntity omPoHeaderEntity = mapper.getEntity(id);
        if (null != omPoHeaderEntity) {
            omPoHeaderEntity.setOmPoDetailList(omPoDetailMapper.findDetailList(new OmPoDetail(null, id)));
        }
        return omPoHeaderEntity;
    }

    public Page<OmPoHeaderEntity> findPage(Page page, OmPoHeaderEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void checkAndSave(OmPoHeader omPoHeader) {
        if (OmsConstants.OMS_PO_STATUS_30.equals(omPoHeader.getStatus()) || OmsConstants.OMS_PO_STATUS_40.equals(omPoHeader.getStatus())) {
            throw new OmsException("订单状态须为新建或确认");
        }
        if (omPoHeader.getOrderDate() == null) {
            throw new OmsException("订单日期不能为空");
        }
        if (StringUtils.isBlank(omPoHeader.getOwner())) {
            throw new OmsException("货主不能为空");
        }
        if (StringUtils.isBlank(omPoHeader.getSupplierCode())) {
            throw new OmsException("供应商不能为空");
        }
        if (StringUtils.isBlank(omPoHeader.getPoType())) {
            throw new OmsException("订单类型不能为空");
        }
        save(omPoHeader);
    }

    @Override
    @Transactional
    public void save(OmPoHeader omPoHeader) {
        if (StringUtils.isBlank(omPoHeader.getPoNo())) {
            omPoHeader.setPoNo(noService.getDocumentNo(GenNoType.OM_PO_NO.name()));
        }
        if (StringUtils.isBlank(omPoHeader.getId())) {
            User user = UserUtils.getUser();
            if (StringUtils.isBlank(omPoHeader.getOrganization())) {
                omPoHeader.setOrganization(user.getCompany() == null ? "" : user.getCompany().getId());
            }
            if (StringUtils.isBlank(omPoHeader.getDepartment())) {
                omPoHeader.setDepartment(user.getOffice() == null ? "" : user.getOffice().getId());
            }
            if (StringUtils.isBlank(omPoHeader.getPreparedBy())) {
                omPoHeader.setPreparedBy(user.getName());
            }
            omPoHeader.setStatus(OmsConstants.OMS_PO_STATUS_20);
        }
        super.save(omPoHeader);
        for (OmPoDetail omPoDetail : omPoHeader.getOmPoDetailList()) {
            if (omPoDetail.getId() == null) {
                continue;
            }
            if (OmPoDetail.DEL_FLAG_NORMAL.equals(omPoDetail.getDelFlag())) {
                if (StringUtils.isEmpty(omPoDetail.getId())) {
                    omPoDetail.setHeaderId(omPoHeader.getId());
                    omPoDetail.setOrgId(omPoHeader.getOrgId());
                    omPoDetail.preInsert();
                    omPoDetailMapper.insert(omPoDetail);
                } else {
                    omPoDetail.preUpdate();
                    omPoDetailMapper.update(omPoDetail);
                }
            } else {
                omPoDetailMapper.delete(omPoDetail);
            }
        }
    }

    @Override
    @Transactional
    public void delete(OmPoHeader omPoHeader) {
        if (!OmsConstants.OMS_PO_STATUS_20.equals(omPoHeader.getStatus())) {
            throw new OmsException("非确认状态，不能删除");
        }
        super.delete(omPoHeader);
        omPoDetailMapper.delete(new OmPoDetail(null, omPoHeader.getId()));
    }

    @Transactional
    public void updateStatus(String poNo, String newStatus, User operator, String orgId) {
        OmPoHeader omPoHeader = new OmPoHeader();
        omPoHeader.setPoNo(poNo);
        omPoHeader.setStatus(newStatus);
        omPoHeader.setUpdateBy(operator);
        omPoHeader.setUpdateDate(new Date());
        omPoHeader.setOrgId(orgId);
        mapper.updateStatus(omPoHeader);
    }

    @Transactional
    public void updateStatusById(String id, String newStatus, User operator) {
        OmPoHeader omPoHeader = new OmPoHeader();
        omPoHeader.setId(id);
        omPoHeader.setStatus(newStatus);
        omPoHeader.setUpdateBy(operator);
        omPoHeader.setUpdateDate(new Date());
        mapper.updateStatusById(omPoHeader);
    }

    @Transactional
    public void audit(String id) {
        audit(get(id), UserUtils.getUser());
    }

    @Transactional
    public void audit(OmPoHeader omPoHeader, User user) {
        if (omPoHeader == null) {
            throw new OmsException("记录不存在，无法审核");
        }
        if (!OmsConstants.OMS_PO_STATUS_20.equals(omPoHeader.getStatus())) {
            throw new OmsException("非确认状态，无法审核");
        }
        if (CollectionUtil.isEmpty(omPoHeader.getOmPoDetailList())) {
            throw new OmsException("订单明细不能为空");
        }
        OmPoHeader update = new OmPoHeader();
        update.setPoNo(omPoHeader.getPoNo());
        update.setStatus(OmsConstants.OMS_PO_STATUS_30);
        update.setAuditBy(user.getName());
        update.setAuditDate(new Date());
        update.setUpdateBy(user);
        update.setUpdateDate(new Date());
        update.setOrgId(omPoHeader.getOrgId());
        mapper.updateAuditStatus(update);
    }

    @Transactional
    public void cancelAudit(String id) {
        cancelAudit(super.get(id), UserUtils.getUser());
    }

    @Transactional
    public void cancelAudit(OmPoHeader omPoHeader, User operator) {
        if (omPoHeader == null) {
            throw new OmsException("记录不存在，无法取消审核");
        }
        if (!OmsConstants.OMS_PO_STATUS_30.equals(omPoHeader.getStatus())) {
            throw new OmsException("非审核状态，无法取消审核");
        }
        OmPoHeader update = new OmPoHeader();
        update.setPoNo(omPoHeader.getPoNo());
        update.setStatus(OmsConstants.OMS_PO_STATUS_20);
        update.setAuditBy(null);
        update.setAuditDate(null);
        update.setUpdateBy(operator);
        update.setUpdateDate(new Date());
        update.setOrgId(omPoHeader.getOrgId());
        mapper.updateAuditStatus(update);
    }

    @Transactional
    public void createChainOrder(String id) {
        OmPoHeader omPoHeader = get(id);
        if (omPoHeader == null) {
            throw new OmsException("记录不存在，无法生成供应链订单");
        }
        if (OmsConstants.OMS_PO_STATUS_40.equals(omPoHeader.getStatus())) {
            throw new OmsException("不能重复生成供应链订单");
        }
        if (!OmsConstants.OMS_PO_STATUS_30.equals(omPoHeader.getStatus())) {
            throw new OmsException("未审核，无法生成供应链订单");
        }
        User operator = UserUtils.getUser();
        // 采购任务下发机构ID（供应链订单所属机构ID）
        String orgId = omPoHeader.getSubOrgId();
        String businessOrderType = "";
        if (OmsConstants.OMS_PO_TYPE_01.equals(omPoHeader.getPoType())) {
            businessOrderType = OmsConstants.OMS_BUSINESS_ORDER_TYPE_01;
        } else if (OmsConstants.OMS_PO_TYPE_02.equals(omPoHeader.getPoType())) {
            businessOrderType = OmsConstants.OMS_BUSINESS_ORDER_TYPE_04;
        } else {
            throw new OmsException("不存在的采购单类型");
        }
        /*OmBusinessOrderTypeRelation businessOrderTypeRelation = omBusinessOrderTypeRelationService.getByBusinessOrderType(businessOrderType, orgId);
        if (businessOrderTypeRelation == null) {
            throw new OmsException("未维护业务订单类型的关联关系");
        }
        String chainType = businessOrderTypeRelation.getOrderType();
        String chainType = StringUtils.isNotBlank(omPoHeader.getCarrier()) ? OmsConstants.OMS_CHAIN_TYPE_04 : OmsConstants.OMS_CHAIN_TYPE_02;*/
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        BigDecimal totalTax = BigDecimal.valueOf(0);
        BigDecimal totalTaxInAmount = BigDecimal.valueOf(0);
        List<OmChainDetail> omChainDetails = Lists.newArrayList();
        for (OmPoDetail omPoDetail : omPoHeader.getOmPoDetailList()) {
            totalAmount = totalAmount.add(omPoDetail.getAmount());
            totalTax = totalTax.add(omPoDetail.getTaxMoney());
            totalTaxInAmount = totalTaxInAmount.add(omPoDetail.getTaxAmount());

            OmChainDetail omChainDetail = new OmChainDetail();
            BeanUtils.copyProperties(omPoDetail, omChainDetail);
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
        BeanUtils.copyProperties(omPoHeader, omChainHeader);
        omChainHeader.setId("");
        omChainHeader.setChainNo(noService.getDocumentNo(GenNoType.OM_CHAIN_NO.name()));
        omChainHeader.setStatus(OmsConstants.OMS_CO_STATUS_00);
        omChainHeader.setBusinessOrderType(businessOrderType);
        omChainHeader.setAuditBy(null);
        omChainHeader.setCreateBy(operator);
        omChainHeader.setCreateDate(new Date());
        omChainHeader.setUpdateBy(operator);
        omChainHeader.setUpdateDate(new Date());
        omChainHeader.setTotalAmount(totalAmount);
        omChainHeader.setTotalTax(totalTax);
        omChainHeader.setTotalTaxInAmount(totalTaxInAmount);
        omChainHeader.setOmChainDetailList(omChainDetails);
        omChainHeader.setSourceOrderNo(omPoHeader.getPoNo());
        omChainHeader.setSourceOrderType(OmsConstants.OMS_SOURCE_TYPE_PO);
        omChainHeader.setOrgId(orgId);
        omChainHeader.setWarehouse(orgId);
        omChainHeader.setSourceOrderId(omPoHeader.getId());
        omChainHeader.setBusinessNo(omPoHeader.getPoNo());
//        omChainHeader.setPrincipal(omPoHeader.getOwner());
        // 若委托方为空则供应商即委托方
        if (StringUtils.isBlank(omChainHeader.getPrincipal())) {
            omChainHeader.setPrincipal(omChainHeader.getSupplierCode());
        }
        omChainHeaderService.save(omChainHeader);

        updateStatus(omPoHeader.getPoNo(), OmsConstants.OMS_PO_STATUS_40, operator, omPoHeader.getOrgId());
    }

    public List<OmPoPrintData> getPoPrintData(List<String> ids) {
        return mapper.getPoPrintData(ids);
    }

    /**
     * 根据wms订单的状态，回刷更新采购单状态为完成
     */
    @Transactional
    public void updateFinishStatusByWmsStatus(){
        // 采购订单
        // 1 获取所有【已生成供应链订单】状态的采购订单
        OmPoHeader poCondition = new OmPoHeader();
        poCondition.setStatus(OmsConstants.OMS_PO_STATUS_40);
        List<OmPoHeader> list = mapper.findList(poCondition);
        for (OmPoHeader poHeader : list) {
            poHeader = this.get(poHeader.getId());
            List<OmPoDetail> detailList = poHeader.getOmPoDetailList();
            Map<String, List<OmPoDetail>> skuMap = detailList.stream().collect(Collectors.groupingBy(OmPoDetail::getSkuCode));
            // 2 根据销售单号，分别去wms获取所有出库单状态
            String poNo = poHeader.getPoNo();
            String getSql = "select CONCAT( wad.sku_code, ',', sum( wad.qty_rcv_ea ) ) from wm_asn_detail wad inner join wm_asn_header wah ON wad.head_id = wah.id where wah.def1 = '" + poNo + "' group by wad.sku_code";
            List<Object> objects = mapper.execSelectSql(getSql);
            if (CollectionUtil.isNotEmpty(objects)) {
                boolean isUpdate = skuMap.size() == objects.size();
                for (Object object : objects) {
                    String item = (String) object;
                    String[] itemList = item.split(",");
                    String skuCode = itemList[0];
                    String receiveQty = itemList[1];
                    List<OmPoDetail> omPoDetails = skuMap.get(skuCode);
                    if (CollectionUtil.isNotEmpty(omPoDetails)) {
                        double sumQty = omPoDetails.stream().mapToDouble(t -> t.getQty().doubleValue()).sum();
                        if (sumQty != Double.parseDouble(receiveQty)) {
                            isUpdate = false;
                        }
                    } else {
                        isUpdate = false;
                    }
                }
                if (isUpdate) {
                    this.updateStatusById(poHeader.getId(), OmsConstants.OMS_PO_STATUS_99, UserUtils.getSystemUser());
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
        OmPoHeader poHeader = get(id);
        this.updateStatusById(poHeader.getId(), OmsConstants.OMS_SO_STATUS_99, UserUtils.getUser());
    }
}