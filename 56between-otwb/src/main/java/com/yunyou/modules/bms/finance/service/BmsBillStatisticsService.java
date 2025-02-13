package com.yunyou.modules.bms.finance.service;

import com.yunyou.common.config.Global;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.common.BmsSummaryStatistics;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.bms.finance.entity.extend.BmsBillStatisticsEntity;
import com.yunyou.modules.bms.finance.mapper.BmsBillStatisticsMapper;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 费用统计Service
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class BmsBillStatisticsService extends CrudService<BmsBillStatisticsMapper, BmsBillStatistics> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BmsBillDetailService bmsBillDetailService;

    public Page<BmsBillStatistics> findPage(Page<BmsBillStatistics> page, BmsBillStatisticsEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Map<String, BigDecimal> getTotal(BmsBillStatisticsEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        return mapper.getTotal(entity);
    }

    @Override
    @Transactional
    public void save(BmsBillStatistics entity) {
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(noService.getDocumentNo(GenNoType.BMS_BILL_NO.name()));
        }
        super.save(entity);
    }

    @Transactional
    public void batchInsert(List<BmsBillStatistics> bmsBillStatistics) {
        if (CollectionUtil.isEmpty(bmsBillStatistics)) {
            return;
        }
        for (int i = 0; i < bmsBillStatistics.size(); i += Global.DB_SQL_MAX_OP_QTY) {
            if (bmsBillStatistics.size() - i < Global.DB_SQL_MAX_OP_QTY) {
                mapper.batchInsert(bmsBillStatistics.subList(i, bmsBillStatistics.size()));
            } else {
                mapper.batchInsert(bmsBillStatistics.subList(i, i + Global.DB_SQL_MAX_OP_QTY));
            }
        }
    }

    /**
     * 描述：批量删除
     */
    @Transactional
    public void deleteAll(List<BmsBillStatistics> bmsBillStatistics) {
        if (CollectionUtil.isEmpty(bmsBillStatistics)) {
            return;
        }
        if (bmsBillStatistics.stream().filter(Objects::nonNull).anyMatch(o -> !BmsConstants.BILL_STATUS_01.equals(o.getStatus()))) {
            throw new BmsException("已确认数据不能删除！");
        }
        List<String> ids = bmsBillStatistics.stream().map(BmsBillStatistics::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i += Global.DB_SQL_MAX_OP_QTY) {
            if (ids.size() - i < Global.DB_SQL_MAX_OP_QTY) {
                mapper.batchDelete(ids.subList(i, ids.size()));
            } else {
                mapper.batchDelete(ids.subList(i, i + Global.DB_SQL_MAX_OP_QTY));
            }
        }
    }

    /**
     * 重新统计
     *
     * @param confirmNo 账单号
     * @param orgId     机构ID
     */
    @Transactional
    public void reStatistics(String confirmNo, String orgId) {
        mapper.deleteByConfirmNo(confirmNo, orgId);

        BmsBillDetail bmsBillDetail = new BmsBillDetail();
        bmsBillDetail.setConfirmNo(confirmNo);
        bmsBillDetail.setOrgId(orgId);
        List<BmsBillDetail> bmsBillDetails = bmsBillDetailService.findList(bmsBillDetail);
        if (CollectionUtil.isEmpty(bmsBillDetails)) {
            return;
        }
        this.batchInsert(BmsSummaryStatistics.convert(bmsBillDetails).stream().peek(o -> o.setConfirmNo(confirmNo)).collect(Collectors.toList()));
    }

}