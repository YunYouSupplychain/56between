package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.BmsBill;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.bms.finance.mapper.BmsBillMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

/**
 * 费用账单Service
 */
@Service
@Transactional(readOnly = true)
public class BmsBillService extends CrudService<BmsBillMapper, BmsBill> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BmsBillDetailService bmsBillDetailService;
    @Autowired
    private BmsBillStatisticsService bmsBillStatisticsService;

    BmsBill getByNo(String confirmNo, String orgId) {
        return mapper.getByNo(confirmNo, orgId);
    }

    @Override
    public Page<BmsBill> findPage(Page<BmsBill> page, BmsBill qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    @Override
    @Transactional
    public void save(BmsBill entity) {
        if (StringUtils.isBlank(entity.getSettleObjCode())) {
            throw new BmsException("结算对象不能为空");
        }
        if (StringUtils.isBlank(entity.getConfirmNo())) {
            entity.setConfirmNo(noService.getDocumentNo(GenNoType.BMS_CONFIRM_NO.name()));
        }
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus(BmsConstants.BILL_STATUS_01);
        }
        super.save(entity);
    }

    @Override
    @Transactional
    public void delete(BmsBill entity) {
        super.delete(entity);

        BmsBillStatistics qStatistics = new BmsBillStatistics();
        qStatistics.setConfirmNo(entity.getConfirmNo());
        qStatistics.setOrgId(entity.getOrgId());
        List<BmsBillStatistics> bmsBillStatisticss = bmsBillStatisticsService.findList(qStatistics);
        for (BmsBillStatistics bmsBillStatistics : bmsBillStatisticss) {
            bmsBillStatisticsService.delete(bmsBillStatistics);
        }

        BmsBillDetail qDetail = new BmsBillDetail();
        qDetail.setConfirmNo(entity.getConfirmNo());
        qDetail.setOrgId(entity.getOrgId());
        List<BmsBillDetail> bmsBillDetails = bmsBillDetailService.findList(qDetail);
        for (BmsBillDetail bmsBillDetail : bmsBillDetails) {
            bmsBillDetail.setConfirmNo(null);
            bmsBillDetail.setStatus(BmsConstants.BILL_STATUS_01);
            bmsBillDetailService.save(bmsBillDetail);
        }
    }

    @Transactional
    public BmsBill addBillDetail(String confirmNo, String orgId, List<BmsBillDetail> addBillDetails) {
        BmsBill bmsBill = this.getByNo(confirmNo, orgId);
        if (!BmsConstants.BILL_STATUS_01.equals(bmsBill.getStatus())) {
            throw new BmsException(MessageFormat.format("账单{0}不是新建状态，无法添加费用", confirmNo));
        }

        // 查出该账单下已有的费用明细，与新增加的费用明细并集
        BmsBillDetail qDetail = new BmsBillDetail();
        qDetail.setConfirmNo(confirmNo);
        qDetail.setOrgId(orgId);
        List<BmsBillDetail> bmsBillDetails = bmsBillDetailService.findList(qDetail);
        if (CollectionUtil.isNotEmpty(bmsBillDetails)) {
            addBillDetails.addAll(bmsBillDetails);
        }

        // 重新计算总费用金额、费用统计，并更新费用明细费用单号、状态
        BigDecimal amount = BigDecimal.ZERO;
        for (BmsBillDetail o : addBillDetails) {
            if (BmsConstants.RECEIVABLE.equals(o.getReceivablePayable())) {
                amount = amount.add(o.getCost());
            } else if (BmsConstants.PAYABLE.equals(o.getReceivablePayable())) {
                amount = amount.subtract(o.getCost());
            }
            o.setConfirmNo(confirmNo);
            o.setStatus(BmsConstants.BILL_STATUS_02);
            bmsBillDetailService.save(o);
        }
        bmsBillStatisticsService.reStatistics(confirmNo, orgId);

        bmsBill.setAmount(amount.doubleValue());
        this.save(bmsBill);
        return bmsBill;
    }

    @Transactional
    public BmsBill removeBillDetail(String confirmNo, String orgId, List<BmsBillDetail> removeBillDetails) {
        BmsBill bmsBill = this.getByNo(confirmNo, orgId);
        if (!BmsConstants.BILL_STATUS_01.equals(bmsBill.getStatus())) {
            throw new BmsException(MessageFormat.format("账单{0}不是新建状态，无法添加费用", confirmNo));
        }

        // 移除费用明细上费用账单号、重置状态
        for (BmsBillDetail bmsBillDetail : removeBillDetails) {
            bmsBillDetail.setConfirmNo(null);
            bmsBillDetail.setStatus(BmsConstants.BILL_STATUS_01);
            bmsBillDetailService.save(bmsBillDetail);
        }

        // 查出该账单剩余费用明细
        BmsBillDetail qDetail = new BmsBillDetail();
        qDetail.setConfirmNo(confirmNo);
        qDetail.setOrgId(orgId);
        List<BmsBillDetail> bmsBillDetails = bmsBillDetailService.findList(qDetail);
        // 重新计算总费用金额、费用统计
        BigDecimal amount = BigDecimal.ZERO;
        for (BmsBillDetail o : bmsBillDetails) {
            if (BmsConstants.RECEIVABLE.equals(o.getReceivablePayable())) {
                amount = amount.add(o.getCost());
            } else if (BmsConstants.PAYABLE.equals(o.getReceivablePayable())) {
                amount = amount.subtract(o.getCost());
            }
        }
        bmsBillStatisticsService.reStatistics(confirmNo, orgId);

        bmsBill.setAmount(amount.doubleValue());
        this.save(bmsBill);
        return bmsBill;
    }
}