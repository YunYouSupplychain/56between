package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.config.Global;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsBillSubject;
import com.yunyou.modules.bms.basic.entity.BmsContract;
import com.yunyou.modules.bms.basic.entity.BmsSettleObject;
import com.yunyou.modules.bms.basic.service.BmsBillSubjectService;
import com.yunyou.modules.bms.basic.service.BmsContractService;
import com.yunyou.modules.bms.basic.service.BmsSettleObjectService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailEntity;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillDetailImport;
import com.yunyou.modules.bms.finance.mapper.BmsBillDetailMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品费用明细Service
 *
 * @author Jianhua Liu
 * @version 2019-06-19
 */
@Service
@Transactional(readOnly = true)
public class BmsBillDetailService extends CrudService<BmsBillDetailMapper, BmsBillDetail> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private BmsSettleObjectService bmsSettleObjectService;
    @Autowired
    private BmsContractService bmsContractService;
    @Autowired
    private BmsBillSubjectService bmsBillSubjectService;

    public BmsBillDetailEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：查询费用明细Page
     */
    public Page<BmsBillDetail> findPage(Page<BmsBillDetail> page, BmsBillDetailEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    /**
     * 描述：统计费用明细发生量、计费量、费用
     */
    public Map<String, BigDecimal> getTotal(BmsBillDetailEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        return mapper.getTotal(entity);
    }

    /**
     * 描述：查询运输费用明细Page
     */
    public Page<BmsBillDetail> findTransportPage(Page<BmsBillDetail> page, BmsBillDetailEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findTransportPage(entity));
        return page;
    }

    @Override
    @Transactional
    public void save(BmsBillDetail entity) {
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(noService.getDocumentNo(GenNoType.BMS_BILL_NO.name()));
        }
        super.save(entity);
    }

    /**
     * 描述：批量插入费用明细
     */
    @Transactional
    public void batchInsert(List<BmsBillDetail> data) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        for (int i = 0; i < data.size(); i += Global.DB_SQL_MAX_OP_QTY) {
            if (data.size() - i < Global.DB_SQL_MAX_OP_QTY) {
                mapper.batchInsert(data.subList(i, data.size()));
            } else {
                mapper.batchInsert(data.subList(i, i + Global.DB_SQL_MAX_OP_QTY));
            }
        }
    }

    /**
     * 描述：批量删除
     */
    @Transactional
    public void deleteAll(List<BmsBillDetail> bmsBillDetails) {
        if (CollectionUtil.isEmpty(bmsBillDetails)) {
            return;
        }
        if (bmsBillDetails.stream().filter(Objects::nonNull).anyMatch(o -> !BmsConstants.BILL_STATUS_01.equals(o.getStatus()))) {
            throw new BmsException("不是新建状态不能删除！");
        }
        List<String> ids = bmsBillDetails.stream().map(BmsBillDetail::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i += Global.DB_SQL_MAX_OP_QTY) {
            if (ids.size() - i < Global.DB_SQL_MAX_OP_QTY) {
                mapper.batchDelete(ids.subList(i, ids.size()));
            } else {
                mapper.batchDelete(ids.subList(i, i + Global.DB_SQL_MAX_OP_QTY));
            }
        }
    }

    /**
     * 描述：根据费用单号删除费用明细
     */
    @Transactional
    public void deleteByBillNo(String billNo, String orgId, String receivablePayable) {
        BmsBillDetail condition = new BmsBillDetail();
        condition.setBillNo(billNo);
        condition.setReceivablePayable(receivablePayable);
        condition.setOrgId(orgId);
        this.deleteAll(this.findList(condition));
    }

    /**
     * 描述：删除历史费用明细
     */
    @Transactional
    public void deleteHistoryFeeByModel(String settleModelCode, Date fmDate, Date toDate, String orgId) {
        List<BmsBillDetail> billDetailList = mapper.findBySettleModelAndBusinessData(settleModelCode, fmDate, toDate, orgId);
        // 排除手工费
        this.deleteAll(billDetailList.stream().filter(o -> !BmsConstants.MANUAL_FEE_TERM_CODE.equals(o.getBillTermsCode())).collect(Collectors.toList()));
    }

    /**
     * 描述：删除历史费用明细
     */
    @Transactional
    public void deleteHistoryFeeByContact(String sysContractNo, Date fmDate, Date toDate, String orgId) {
        List<BmsBillDetail> billDetailList = mapper.findByContractAndBusinessData(sysContractNo, fmDate, toDate, orgId);
        // 排除手工费
        this.deleteAll(billDetailList.stream().filter(o -> !BmsConstants.MANUAL_FEE_TERM_CODE.equals(o.getBillTermsCode())).collect(Collectors.toList()));
    }

    /**
     * 描述：Excel导入
     */
    @Transactional
    public void importFile(List<BmsBillDetailImport> importList, String orgId) {
        if (CollectionUtil.isEmpty(importList)) {
            return;
        }
        User user = UserUtils.getUser();
        Office office = officeService.get(orgId);
        List<BmsBillDetail> rsList = Lists.newArrayList();
        int size = importList.size();
        for (int i = 0; i < size; i++) {
            BmsBillDetailImport data = importList.get(i);
            if (StringUtils.isBlank(data.getWarehouseCode())) {
                throw new BmsException("第" + (i + 1) + "行，仓别不能为空");
            }
            Office warehouse = officeService.getByCode(data.getWarehouseCode());
            if (warehouse == null) {
                throw new BmsException("第" + (i + 1) + "行，仓别不存在");
            }
            if (StringUtils.isBlank(data.getSettleObjectCode())) {
                throw new BmsException("第" + (i + 1) + "行，结算对象编码不能为空");
            }
            BmsSettleObject settlementObject = bmsSettleObjectService.getByCode(data.getSettleObjectCode(), office.getId());
            if (settlementObject == null) {
                throw new BmsException("第" + (i + 1) + "行，结算对象不存在");
            }
            BmsContract bmsContract = null;
            if (StringUtils.isNotBlank(data.getSysContractNo())) {
                bmsContract = bmsContractService.getByContract(data.getSysContractNo(), office.getId());
                if (bmsContract == null) {
                    throw new BmsException("第" + (i + 1) + "行，系统合同号不存在");
                }
                if (!settlementObject.getSettleObjectCode().equals(bmsContract.getSettleObjectCode())) {
                    throw new BmsException("第" + (i + 1) + "行，该结算对象下未找到此合同");
                }
            }
            if (StringUtils.isBlank(data.getBillSubjectCode())) {
                throw new BmsException("第" + (i + 1) + "行，费用科目代码不能为空");
            }
            BmsBillSubject billSubject = bmsBillSubjectService.getByCode(data.getBillSubjectCode(), office.getId());
            if (billSubject == null) {
                throw new BmsException("第" + (i + 1) + "行，费用科目不存在");
            }
            if (StringUtils.isBlank(data.getReceivablePayable())) {
                throw new BmsException("第" + (i + 1) + "行，应收应付不能为空");
            }
            String rp = DictUtils.getDictValue(data.getReceivablePayable(), "BMS_RECEIVABLE_PAYABLE", null);
            if (StringUtils.isBlank(rp)) {
                throw new BmsException("第" + (i + 1) + "行，应收应付不存在");
            }
            if (data.getBillStandard() == null) {
                throw new BmsException("第" + (i + 1) + "行，计费标准不能为空");
            }
            if (data.getBillQty() == null) {
                throw new BmsException("第" + (i + 1) + "行，计费量不能为空");
            }
            if (data.getCost() == null) {
                throw new BmsException("第" + (i + 1) + "行，费用不能为空");
            }

            BmsBillDetail billSkuDetail = new BmsBillDetail();
            billSkuDetail.setId(IdGen.uuid());
            billSkuDetail.setBillNo(noService.getDocumentNo(GenNoType.BMS_BILL_NO.name()));
            billSkuDetail.setCreateBy(user);
            billSkuDetail.setCreateDate(new Date());
            billSkuDetail.setUpdateBy(user);
            billSkuDetail.setUpdateDate(new Date());

            billSkuDetail.setStatus(BmsConstants.BILL_STATUS_01);
            billSkuDetail.setSettleObjectCode(settlementObject.getSettleObjectCode());
            billSkuDetail.setSettleObjectName(settlementObject.getSettleObjectName());
            billSkuDetail.setSettleCategory(settlementObject.getSettleCategory());
            billSkuDetail.setSettleMethod(settlementObject.getSettleType());
            billSkuDetail.setSysContractNo(bmsContract == null ? null : bmsContract.getSysContractNo());
            billSkuDetail.setContractNo(bmsContract == null ? null : bmsContract.getContractNo());
            billSkuDetail.setSubcontractNo(bmsContract == null ? null : bmsContract.getSubcontractNo());
            billSkuDetail.setBillModule(billSubject.getBillModule());
            billSkuDetail.setBillSubjectCode(billSubject.getBillSubjectCode());
            billSkuDetail.setBillSubjectName(billSubject.getBillSubjectName());
            billSkuDetail.setBillCategory(billSubject.getBillCategory());
            billSkuDetail.setBillTermsCode(BmsConstants.MANUAL_FEE_TERM_CODE);
            billSkuDetail.setBillTermsDesc("手工费");
            billSkuDetail.setReceivablePayable(rp);
            billSkuDetail.setBusinessDate(data.getBusinessDate());
            billSkuDetail.setWarehouseCode(warehouse.getCode());
            billSkuDetail.setWarehouseName(warehouse.getName());
            billSkuDetail.setOccurrenceQty(BigDecimal.valueOf(data.getOccurrenceQty()));
            billSkuDetail.setBillQty(BigDecimal.valueOf(data.getBillQty()));
            billSkuDetail.setBillStandard(BigDecimal.valueOf(data.getBillStandard()));
            billSkuDetail.setCost(BigDecimal.valueOf(data.getCost()));
            billSkuDetail.setOrgId(orgId);

            billSkuDetail.setSysOrderNo(data.getSysOrderNo());
            billSkuDetail.setCustomerOrderNo(data.getCustomerOrderNo());
            billSkuDetail.setRemarks(data.getRemarks());
            rsList.add(billSkuDetail);
        }
        this.batchInsert(rsList);
    }

    /**
     * 保存计算结果
     *
     * @param bmsBillDetails 费用结果
     */
    @Transactional
    public void saveCalculateResult(List<BmsBillDetail> bmsBillDetails) {
        if (CollectionUtil.isEmpty(bmsBillDetails)) {
            return;
        }
        User user = UserUtils.getUser();
        Date date = new Date();
        String billBatchNo = noService.getDocumentNo(GenNoType.BMS_BILL_NO.name());
        for (BmsBillDetail bmsBillDetail : bmsBillDetails) {
            bmsBillDetail.setId(IdGen.uuid());
            bmsBillDetail.setCreateBy(user);
            bmsBillDetail.setCreateDate(date);
            bmsBillDetail.setUpdateBy(user);
            bmsBillDetail.setUpdateDate(date);
            bmsBillDetail.setBillNo(billBatchNo);
        }
        this.batchInsert(bmsBillDetails);
    }
}