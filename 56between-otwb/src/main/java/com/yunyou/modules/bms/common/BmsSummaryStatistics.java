package com.yunyou.modules.bms.common;

import com.yunyou.common.utils.IdGen;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：将费用明细转换汇总统计
 * <p>
 * create by Jianhua on 2019/6/20
 */
public class BmsSummaryStatistics {

    public static List<BmsBillStatistics> convert(List<BmsBillDetail> bmsBillDetails) {
        List<BmsBillStatistics> rsList = Lists.newArrayList();
        User user = UserUtils.getUser();

        // 账单号 + 结算模型 + 仓库 + 状态 + 日期范围 + 结算对象 + 合同号 + 费用模块 + 费用科目 + 计费条款 + 应收应付 + 计费标准值 + 机构
        Map<String, List<BmsBillDetail>> map = bmsBillDetails.stream().collect(
                Collectors.groupingBy(o -> o.getConfirmNo() + o.getSettleModelCode() + o.getWarehouseCode() + o.getStatus() + o.getDateRange()
                        + o.getSysContractNo() + o.getSettleObjectCode() + o.getBillModule() + o.getBillSubjectCode() + o.getBillTermsCode()
                        + o.getReceivablePayable() + o.getBillStandard() + o.getOrgId()));
        for (List<BmsBillDetail> list : map.values()) {
            BmsBillDetail billSkuDetail = list.get(0);
            BigDecimal occurrenceQty = BigDecimal.ZERO, billQty = BigDecimal.ZERO, billCost = BigDecimal.ZERO;
            for (BmsBillDetail o : list) {
                occurrenceQty = occurrenceQty.add(o.getOccurrenceQty() == null ? BigDecimal.ZERO : o.getOccurrenceQty());
                billQty = billQty.add(o.getBillQty() == null ? BigDecimal.ZERO : o.getBillQty());
                billCost = billCost.add(o.getCost() == null ? BigDecimal.ZERO : o.getCost());
            }
            BmsBillStatistics billDetail = new BmsBillStatistics();
            billDetail.setId(IdGen.uuid());
            billDetail.setCreateBy(user);
            billDetail.setCreateDate(new Date());
            billDetail.setUpdateBy(user);
            billDetail.setUpdateDate(new Date());

            billDetail.setConfirmNo(billSkuDetail.getConfirmNo());
            billDetail.setStatus(billSkuDetail.getStatus());
            billDetail.setDateFm(billSkuDetail.getDateFm());
            billDetail.setDateTo(billSkuDetail.getDateTo());
            billDetail.setSettleModelCode(billSkuDetail.getSettleModelCode());
            billDetail.setSettleObjectCode(billSkuDetail.getSettleObjectCode());
            billDetail.setSettleObjectName(billSkuDetail.getSettleObjectName());
            billDetail.setSettleMethod(billSkuDetail.getSettleMethod());
            billDetail.setSettleCategory(billSkuDetail.getSettleCategory());
            billDetail.setSysContractNo(billSkuDetail.getSysContractNo());
            billDetail.setContractNo(billSkuDetail.getContractNo());
            billDetail.setSubcontractNo(billSkuDetail.getSubcontractNo());
            billDetail.setBillModule(billSkuDetail.getBillModule());
            billDetail.setBillSubjectCode(billSkuDetail.getBillSubjectCode());
            billDetail.setBillSubjectName(billSkuDetail.getBillSubjectName());
            billDetail.setBillCategory(billSkuDetail.getBillCategory());
            billDetail.setBillTermsCode(billSkuDetail.getBillTermsCode());
            billDetail.setBillTermsDesc(billSkuDetail.getBillTermsDesc());
            billDetail.setReceivablePayable(billSkuDetail.getReceivablePayable());
            billDetail.setOrgId(billSkuDetail.getOrgId());
            billDetail.setWarehouseCode(billSkuDetail.getWarehouseCode());
            billDetail.setWarehouseName(billSkuDetail.getWarehouseName());

            billDetail.setBillStandard(billSkuDetail.getBillStandard());
            billDetail.setOccurrenceQty(occurrenceQty);
            billDetail.setBillQty(billQty);
            billDetail.setCost(billCost.setScale(BmsConstants.BILL_RETAIN_DECIMAL, RoundingMode.HALF_UP));
            rsList.add(billDetail);
        }
        return rsList;
    }

}
