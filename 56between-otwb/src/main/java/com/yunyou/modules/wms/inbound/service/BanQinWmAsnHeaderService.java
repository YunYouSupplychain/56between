package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.collection.ListUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLocService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageRelationService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.ActionCode;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.inbound.entity.*;
import com.yunyou.modules.wms.inbound.entity.extend.BanQinWmAsnImportEntity;
import com.yunyou.modules.wms.inbound.entity.extend.BanQinWmAsnSerialReceiveImportEntity;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmAsnHeaderMapper;
import com.yunyou.modules.wms.report.entity.ReceivingOrderLabel;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import com.yunyou.modules.wms.report.entity.WmCheckReceiveOrderLabel;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 入库单Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAsnHeaderService extends CrudService<BanQinWmAsnHeaderMapper, BanQinWmAsnHeader> {
    @Autowired
    @Lazy
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    @Lazy
    private BanQinInboundQcOperationService banQinInboundQcOperationService;
    @Autowired
    @Lazy
    private BanQinInboundRcOperationService banQinInboundRcOperationService;
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinCdWhLocService cdWhLocService;
    @Autowired
    private BanQinCdWhPackageService cdWhPackageService;
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;

    public BanQinWmAsnEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmAsnEntity> findPage(Page page, BanQinWmAsnEntity banQinWmAsnEntity) {
        banQinWmAsnEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmAsnEntity.getOrgId()));
        dataRuleFilter(banQinWmAsnEntity);
        banQinWmAsnEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmAsnEntity));
        return page;
    }

    public Page<BanQinWmRepInDailyQueryEntity> getRepInDailyQuery(Page page, BanQinWmRepInDailyQueryEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("wah.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.wmRepInDailyQuery(entity));
        return page;
    }

    public BanQinWmRepInDailyQueryEntity countQtyQuery(BanQinWmRepInDailyQueryEntity entity) {
        BanQinWmRepInDailyQueryEntity result = new BanQinWmRepInDailyQueryEntity();
        entity.setDataScope(GlobalDataRule.getDataRuleSql("wah.org_id", entity.getOrgId()));
        List<BanQinWmRepInDailyQueryEntity> list = mapper.countQtyQuery(entity);
        double sumAsnEa = 0d;
        double sumRcvEA = 0d;
        for (BanQinWmRepInDailyQueryEntity query : list) {
            if (null != query) {
                sumAsnEa += query.getQtyAsnEa();
                sumRcvEA += query.getQtyRcvEa();
            }
        }
        result.setTotalQtyAsnEa(sumAsnEa);
        result.setTotalQtyRcvEa(sumRcvEA);
        return result;
    }

    /**
     * 描述： 根据入库单号获取
     *
     * @param asnNo 入库单号
     * @param orgId 机构ID
     * @author Jianhua on 2019/1/25
     */
    public BanQinWmAsnHeader getByAsnNo(String asnNo, String orgId) {
        return mapper.getByAsnNo(asnNo, orgId);
    }

    /**
     * 描述： 获取非QC的ASN单
     *
     * @param orderIds 入库单ID
     * @author Jianhua on 2019/1/25
     */
    public List<String> findNotQCById(List<String> orderIds) {
        return mapper.findNotQCById(orderIds);
    }

    /**
     * 描述： 更新审核状态
     *
     * @param orderIds    入库单ID
     * @param auditStatus 审核状态
     * @param auditOp     审核人
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public void updateAuditStatusById(List<String> orderIds, String auditStatus, String auditOp) {
        mapper.updateAuditStatusById(orderIds, auditStatus, auditOp);
    }

    /**
     * 描述： 更新冻结状态
     *
     * @param orderIds   入库单ID
     * @param holdStatus 冻结状态
     * @param holdOp     冻结人
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public void updateHoldStatusById(List<String> orderIds, String holdStatus, String holdOp) {
        mapper.updateHoldStatusById(orderIds, holdStatus, holdOp);
    }

    /**
     * 描述：ASN单不为取消或关闭状态时，统计明细状态，更新单头状态
     *
     * @param asnNo 入库单号
     * @param orgId 机构ID
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void updateStatus(String asnNo, String orgId) {
        mapper.updateStatus(asnNo, orgId);
    }

    /**
     * 描述： 根据ASN单号
     *
     * @param asnNo 入库单号
     * @param orgId 机构ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeByAsnNo(String asnNo, String orgId) {
        mapper.removeByAsnNo(asnNo, orgId);
    }

    //*************************************************************************************************************************************************//

    /**
     * 描述： 保存ASN单
     *
     * @param wmAsnEntity 入库单实体
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public ResultMessage saveAsnEntity(BanQinWmAsnEntity wmAsnEntity) {
        ResultMessage msg = banQinInboundOperationService.saveAsnHeader(wmAsnEntity);
        if (msg.isSuccess()) {
            msg.addMessage("操作成功");
            msg.setData(msg.getData());
        }
        return msg;
    }

    /**
     * 描述： 删除ASN单：未审核的创建状态单
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage removeAsnEntity(String[] asnIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(),
                WmsCodeMaster.AUDIT_NOT.getCode()}, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.YES, null, null, WmsConstants.NO, WmsConstants.NO);
        String[] updateAsnIds = (String[]) errorMsg.getData();

        ResultMessage isQcMsg = banQinInboundOperationService.checkAsnExistQc(updateAsnIds);
        updateAsnIds = (String[]) isQcMsg.getData();

        for (String asnId : updateAsnIds) {
            banQinInboundOperationService.removeAsnEntity(asnId);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
                msg.addMessage(errorMsg.getMessage() + "非创建状态、已审核、已冻结、已码盘、已安排库位、已生成越库任务的订单，不能操作");
            }
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.addMessage(isQcMsg.getMessage() + "已生成质检单，不能操作");
            }
        }
        return msg;
    }

    /**
     * 描述： 审核：审核后不能修改订单
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage auditAsn(String[] asnIds) {
        return banQinInboundOperationService.audit(asnIds);
    }

    /**
     * 描述： 取消审核：不能进行收货操作
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage cancelAuditAsn(String[] asnIds) {
        return banQinInboundOperationService.cancelAudit(asnIds);
    }

    /**
     * 描述： 冻结：不能进行其他操作
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void holdAsn(String[] asnIds) {
        this.updateHoldStatusById(Lists.newArrayList(asnIds), WmsCodeMaster.ODHL_HOLD.getCode(), UserUtils.getUser().getName());
    }

    /**
     * 描述： 取消冻结：恢复操作
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void cancelHoldAsn(String[] asnIds) {
        this.updateHoldStatusById(Lists.newArrayList(asnIds), WmsCodeMaster.ODHL_NO_HOLD.getCode(), UserUtils.getUser().getName());
    }

    /**
     * 描述： 订单未冻结，行状态“完全收货”并且未生成凭证号的收货明细，生成凭证号
     *
     * @param asnId   入库单ID
     * @param lineNos 入库单明细行号
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage createVoucherNoByLineNo(String asnId, String[] lineNos) {
        ResultMessage msg = new ResultMessage();

        BanQinWmAsnHeader wmAsnHeader = this.get(asnId);
        if (WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeader.getHoldStatus())) {
            msg.addMessage("订单冻结，不能操作");
            return msg;
        }
        ResultMessage errorMsg = banQinInboundOperationService.checkAsnDetailReceive(asnId, lineNos, new String[]{WmsCodeMaster.ASN_FULL_RECEIVING.getCode()}, null, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        if (updateLineNos.length > 0) {
            banQinWmAsnDetailReceiveService.createVoucherNoByLineNo(asnId, updateLineNos);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage("行号\n" + errorMsg.getMessage() + "不是完全收货，或已生成凭证号，不能操作");
        }
        return msg;
    }

    /**
     * 描述： 订单未冻结，行状态“完全收货”并且已生成凭证号的收货明细，取消凭证号
     *
     * @param asnId   入库单ID
     * @param lineNos 入库单明细行号
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage cancelVoucherNoByLineNo(String asnId, String[] lineNos) {
        ResultMessage msg = new ResultMessage();

        BanQinWmAsnHeader wmAsnHeader = this.get(asnId);
        if (WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeader.getHoldStatus())) {
            msg.addMessage("订单冻结，不能操作");
            return msg;
        }
        ResultMessage errorMsg = banQinInboundOperationService.checkAsnDetailReceive(asnId, lineNos, new String[]{WmsCodeMaster.ASN_FULL_RECEIVING.getCode()}, null, WmsConstants.YES);
        String[] updateLineNos = (String[]) errorMsg.getData();
        if (updateLineNos.length > 0) {
            banQinWmAsnDetailReceiveService.cancelVoucherNoByLineNo(asnId, updateLineNos);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage("行号\n" + errorMsg.getMessage() + "不是完全收货，或未生成凭证号，不能操作");
        }
        return msg;
    }

    /**
     * 描述： 批量勾选订单，一个ASN单生成一个凭证号
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage createVoucherNoByAsn(String[] asnIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_FULL_RECEIVING.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode(),
                WmsCodeMaster.ASN_CLOSE.getCode()}, null, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, null, WmsConstants.YES, null, null, null);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        if (updateAsnIds.length > 0) {
            banQinWmAsnDetailReceiveService.createVoucherNoByAsn(updateAsnIds);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "不是收货状态、或已生成凭证号、已冻结，不能操作");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 批量勾选订单，取消
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage cancelVoucherNoByAsn(String[] asnIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_FULL_RECEIVING.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode(),
                WmsCodeMaster.ASN_CLOSE.getCode()}, null, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, null, WmsConstants.NO, null, null, null);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        if (updateAsnIds.length > 0) {
            banQinWmAsnDetailReceiveService.cancelVoucherNoByAsn(updateAsnIds);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "不是收货状态、或未生成、已冻结，不能操作");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 入库扫描序列号收货
     *
     * @param wmAsnDetailReceiveEntity 入库单收货明细实体
     */
    @Transactional
    public ResultMessage serialReceiving(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) {
        wmAsnDetailReceiveEntity.setActionCode(ActionCode.SCAN_RECEIVING);
        ResultMessage msg = banQinInboundRcOperationService.inboundReceiving(wmAsnDetailReceiveEntity);
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 按ASN订单生成质检单，根据商品的质检阶段拆分质检单
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage createQcByAsn(String[] asnIds) {
        return banQinInboundQcOperationService.createQcByAsn(asnIds);
    }

    /**
     * 描述： 勾选ASN商品明细，生成质检单
     *
     * @param asnNo   入库单号
     * @param lineNos 入库单明细
     * @param orgId   机构ID
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage createQcByAsnLineNo(String asnNo, String[] lineNos, String orgId) {
        return banQinInboundQcOperationService.createQcByAsnLineNo(asnNo, lineNos, orgId);
    }

    /**
     * 描述： 取消ASN单：新增订单可以取消，取消后，不能操作
     *
     * @param asnIds 入库单ID
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage cancelAsn(String[] asnIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(),
                WmsCodeMaster.AUDIT_NOT.getCode()}, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.YES, null, null, WmsConstants.NO, WmsConstants.NO);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = banQinInboundOperationService.checkAsnExistQc(updateAsnIds);
        updateAsnIds = (String[]) isQcMsg.getData();
        if (updateAsnIds.length > 0) {
            for (String asnId : updateAsnIds) {
                banQinInboundOperationService.cancelAsn(this.get(asnId));
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
                msg.addMessage(errorMsg.getMessage() + "非创建状态、已审核、已冻结、已码盘、已安排库位、已生成越库任务的订单，不能操作");
            }
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.addMessage(isQcMsg.getMessage() + "已生成质检单，不能操作");
            }
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 关闭ASN单：部分收货、完全收货可以关闭，关闭时校验是否来自PO单，需回填PO单ASN数
     *
     * @param asnIds 入库单ID
     * @author WMJ
     */
    @Transactional
    public ResultMessage closeAsn(String[] asnIds) {
        ResultMessage msg = new ResultMessage();

        List<String> status = Lists.newArrayList();
        status.add(WmsCodeMaster.ASN_FULL_RECEIVING.getCode());
        status.add(WmsCodeMaster.ASN_PART_RECEIVING.getCode());
        // 是否只有ASN完全收货才可以关闭ASN（Y：必须是完全收货；N：部分收货或完全收货）
        String ASN_ONLY_FULL_RCV_CLOSE = SysControlParamsUtils.getValue(ControlParamCode.ASN_ONLY_FULL_RCV_CLOSE.getCode(), null);
        if (WmsConstants.YES.equals(ASN_ONLY_FULL_RCV_CLOSE)) {
            status.remove(WmsCodeMaster.ASN_PART_RECEIVING.getCode());
        }
        ResultMessage errorMsg = banQinInboundOperationService.checkAsnStatus(asnIds, status.toArray(new String[]{}), null, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, null, null, null, null, null);
        String[] updateAsnIds = (String[]) errorMsg.getData();

        List<BanQinWmAsnHeader> list = Lists.newArrayList();
        for (String asnId : updateAsnIds) {
            list.add(this.get(asnId));
        }
        for (BanQinWmAsnHeader model : list) {
            banQinInboundOperationService.closeAsn(model);
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        } else if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            if (WmsConstants.YES.equals(ASN_ONLY_FULL_RCV_CLOSE)) {
                msg.addMessage(errorMsg.getMessage() + "不是完全收货状态、已冻结，不能操作");
            } else {
                msg.addMessage(errorMsg.getMessage() + "不是收货状态、已冻结，不能操作");
            }
        }
        msg.setSuccess(true);
        return msg;
    }

    public List<BanQinWmAsnHeader> checkAsnStatusQuery(String[] asnIds, String[] orderStatus, String[] auditStatus, String[] holdStatus,
                                                       String isPalletize, String isVoucher, String isCrossDock, String isArrangeLoc) {
        return mapper.checkAsnStatusQuery(asnIds, orderStatus, auditStatus, holdStatus, isPalletize, isVoucher, isCrossDock, isArrangeLoc);
    }

    public List<BanQinWmAsnHeader> getByIds(String[] ids) {
        return mapper.getByIds(ids);
    }

    public List<ReceivingOrderLabel> getReceivingOrder(List<String> asnHeaderIds) {
        return mapper.getReceivingOrder(asnHeaderIds);
    }

    public List<TraceLabel> getTraceLabel(List<String> asnHeaderIds) {
        return mapper.getTraceLabel(asnHeaderIds);
    }

    public List<Map<String, Long>> rfRcCheckAsnIsPalletizeQuery(String asnNo, String orgId) {
        return mapper.rfRcCheckAsnIsPalletizeQuery(asnNo, orgId);
    }

    public List<BanQinWmAsnDetailReceiveEntity> rfRcGetAsnDetailByTraceIDOrSkuQuery(String asnNo, String planId, String skuCode, String funType, String orgId) {
        return mapper.rfRcGetAsnDetailByTraceIDOrSkuQuery(asnNo, planId, skuCode, funType, orgId);
    }

    @Transactional
    public ResultMessage costAlloc(String[] ids, String strategy) {
        return banQinInboundOperationService.costAlloc(ids, strategy);
    }

    public List<BanQinWmAsnEntity> findEntityList(BanQinWmAsnEntity banQinWmAsnEntity) {
        banQinWmAsnEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmAsnEntity.getOrgId()));
        return mapper.findPage(banQinWmAsnEntity);
    }

    @Transactional
    public ResultMessage importOrder(List<BanQinWmAsnImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        List<BanQinWmAsnEntity> result = Lists.newArrayList();
        Map<String, List<BanQinWmAsnImportEntity>> collect = list.stream().collect(Collectors.groupingBy(BanQinWmAsnImportEntity::getOrderNo, LinkedHashMap::new, Collectors.toList()));
        int index = 2;
        for (Map.Entry<String, List<BanQinWmAsnImportEntity>> entry : collect.entrySet()) {
            BanQinWmAsnImportEntity entity = entry.getValue().get(0);
            StringBuilder checkNull = checkNullForImport(entity);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                break;
            }
            StringBuilder checkExist = checkIsExistForImport(entity);
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                break;
            }

            BanQinWmAsnEntity wmAsnEntity = new BanQinWmAsnEntity();
            BeanUtils.copyProperties(entity, wmAsnEntity);
            wmAsnEntity.setDef4(entity.getOrderNo());
            int lineIndex = 1;
            List<BanQinWmAsnDetailEntity> detailList = Lists.newArrayList();
            for (BanQinWmAsnImportEntity importEntity : entry.getValue()) {
                StringBuilder detailNull = checkNullForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailNull.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailNull.toString()).append("<br>");
                    break;
                }
                StringBuilder detailExist = checkIsExistForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailExist.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailExist.toString()).append("<br>");
                    break;
                }
                BanQinWmAsnDetailEntity detailEntity = setAsnDetailForImport(importEntity);
                detailEntity.setLineNo(String.format("%04d", lineIndex));
                detailList.add(detailEntity);
                lineIndex++;
            }
            wmAsnEntity.setWmAsnDetailEntities(detailList);
            result.add(wmAsnEntity);
            index += entry.getValue().size();
        }

        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }

        for (BanQinWmAsnEntity asnEntity : result) {
            ResultMessage message = this.saveAsnEntity(asnEntity);
            BanQinWmAsnHeader asnHeader = (BanQinWmAsnHeader) message.getData();
            for (BanQinWmAsnDetailEntity detailEntity : asnEntity.getWmAsnDetailEntities()) {
                detailEntity.setHeadId(asnHeader.getId());
                detailEntity.setAsnNo(asnHeader.getAsnNo());
                banQinWmAsnDetailService.saveAsnDetailEntity(detailEntity);
            }
        }

        msg.setMessage("导入成功");
        return msg;
    }

    private StringBuilder checkNullForImport(BanQinWmAsnImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (null == entity.getOrderTime()) {
            builder.append("订单日期为空或订单日期格式不正确!");
        }
        if (StringUtils.isEmpty(entity.getAsnType())) {
            builder.append("入库单类型为空!");
        }
        if (StringUtils.isEmpty(entity.getOwnerCode())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getOrderNo())) {
            builder.append("订单号为空!");
        }
//        if (null == entity.getInboundTime()) {
//            builder.append("入库时间为空或入库时间格式不正确!!");
//        }
//        builder.append(checkNullForDetail(entity));
        return builder;
    }

    private StringBuilder checkNullForDetail(BanQinWmAsnImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
//        if (StringUtils.isEmpty(entity.getSkuName())) {
//            builder.append("商品名称为空!");
//        }
        if (null == entity.getQty()) {
            builder.append("数量为空或格式错误!");
        }
        if (StringUtils.isEmpty(entity.getPlanRcvLoc())) {
            builder.append("计划收货库位为空!");
        }
        if (StringUtils.isEmpty(entity.getUom())) {
            builder.append("包装单位为空!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForImport(BanQinWmAsnImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<String> orderType = Arrays.asList(WmsCodeMaster.ASN_PI.getCode(), WmsCodeMaster.ASN_AI.getCode(), WmsCodeMaster.ASN_RI.getCode(), WmsCodeMaster.ASN_EI.getCode(),
                WmsCodeMaster.ASN_ADI.getCode(), WmsCodeMaster.ASN_CI.getCode());
        if (!orderType.contains(entity.getAsnType())) {
            builder.append("入库单类型值填写错误!");
        }
        BanQinEbCustomer owner = ebCustomerService.find(entity.getOwnerCode(), CustomerType.OWNER.getCode(), entity.getOrgId());
        if (null == owner) {
            builder.append("货主编码不存在!");
        }
        if (StringUtils.isNotEmpty(entity.getSupplierCode())) {
            BanQinEbCustomer supplier = ebCustomerService.find(entity.getSupplierCode(), CustomerType.SUPPLIER.getCode(), entity.getOrgId());
            if (null == supplier) {
                builder.append("供应商编码不存在!");
            }
        }
        if (StringUtils.isNotEmpty(entity.getCarrierCode())) {
            BanQinEbCustomer carrier = ebCustomerService.find(entity.getCarrierCode(), CustomerType.CARRIER.getCode(), entity.getOrgId());
            if (null == carrier) {
                builder.append("承运商编码不存在!");
            }
        }
//        builder.append(checkIsExistForDetail(entity));

        return builder;
    }

    private StringBuilder checkIsExistForDetail(BanQinWmAsnImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        BanQinCdWhSku item = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        if (null == item) {
            builder.append("商品编码不存在!");
        }
        BanQinCdWhLoc loc = cdWhLocService.findByLocCode(entity.getPlanRcvLoc(), entity.getOrgId());
        if (null == loc) {
            builder.append("计划收货库位不存在!");
        }
        Pattern chinaP = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        if (StringUtils.isNotBlank(entity.getLotAtt01())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt01()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt01());
            } else {
                builder.append("生产日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt02())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt02()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt02());
            } else {
                builder.append("失效日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt03())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt03()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt03());
            } else {
                builder.append("入库日期格式错误!");
            }
        }

        List<String> lotAtt04 = Arrays.asList(WmsCodeMaster.QC_ATT_QUALIFIED.getCode(), WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
        if (StringUtils.isNotEmpty(entity.getLotAtt04()) && !lotAtt04.contains(entity.getLotAtt04())) {
            builder.append("品质值填写错误!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt05()) && entity.getLotAtt05().length() > 64) {
            builder.append("批次属性5长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt06()) && entity.getLotAtt06().length() > 64) {
            builder.append("批次属性6长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt07()) && entity.getLotAtt07().length() > 64) {
            builder.append("批次属性7长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt08()) && entity.getLotAtt08().length() > 64) {
            builder.append("批次属性8长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt09()) && entity.getLotAtt09().length() > 64) {
            builder.append("批次属性9长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt10()) && entity.getLotAtt10().length() > 64) {
            builder.append("批次属性10长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt11()) && entity.getLotAtt11().length() > 64) {
            builder.append("批次属性11长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt12()) && entity.getLotAtt12().length() > 64) {
            builder.append("批次属性12长度不能超过64个字符!");
        }

        return builder;
    }

    @Transactional
    public BanQinWmAsnDetailEntity setAsnDetailForImport(BanQinWmAsnImportEntity entity) {
        BanQinCdWhSku cdWhSku = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        /*if (null == cdWhSku) {
            cdWhSku = new BanQinCdWhSku();
            cdWhSku.setLotCode("01");
            cdWhSku.setReserveCode(StringUtils.isNotBlank(entity.getReserveCode()) ? entity.getReserveCode() : "MAN");
            cdWhSku.setPaRule(StringUtils.isNotBlank(entity.getPaRule()) ? entity.getPaRule() : "01");
            cdWhSku.setRotationRule(StringUtils.isNotBlank(entity.getRotationRule()) ? entity.getRotationRule() : "01");
            cdWhSku.setAllocRule(StringUtils.isNotBlank(entity.getAllocRule()) ? entity.getAllocRule() : "01");
            cdWhSku.setRcvUom(StringUtils.isNotBlank(entity.getUom()) ? entity.getUom() : "EA");
            cdWhSku.setShipUom(StringUtils.isNotBlank(entity.getUom()) ? entity.getUom() : "EA");
            cdWhSku.setPrintUom(StringUtils.isNotBlank(entity.getUom()) ? entity.getUom() : "EA");
            cdWhSku.setTempLevel(entity.getTempLevel());
            cdWhSku.setIsEnable("Y");
            cdWhSku.setIsCold("N");
            cdWhSku.setIsDg("N");
            cdWhSku.setIsValidity("N");
            cdWhSku.setIsOverRcv("N");
            cdWhSku.setIsQc("N");
            cdWhSku.setIsSerial("N");
            cdWhSku.setIsParent("N");
            cdWhSku.setDef1(entity.getSkuDef1());
            cdWhSku.setDef2(entity.getSkuDef2());
            cdWhSku.setDef3(entity.getSkuDef3());
            cdWhSku.setDef4(entity.getSkuDef4());
            cdWhSku.setDef5(entity.getSkuDef5());
            cdWhSku.setDef6(entity.getSkuDef6());
            cdWhSku.setDef7(entity.getSkuDef7());
            cdWhSku.setDef8(entity.getSkuDef8());
            cdWhSku.setDef9(entity.getSkuDef9());
            cdWhSku.setDef10(entity.getSkuDef10());
            cdWhSku.setDef11(entity.getSkuDef11());
            cdWhSku.setDef12(entity.getSkuDef12());
            cdWhSku.setDef13(entity.getSkuDef13());
            cdWhSku.setDef14(entity.getSkuDef14());
            cdWhSku.setDef15(entity.getSkuDef15());
            cdWhSku.setCreateBy(UserUtils.getUser());
            cdWhSku.setCreateDate(new Date());
            cdWhSku.setOrgId(entity.getOrgId());
            cdWhSku.setOwnerCode(entity.getOwnerCode());
            cdWhSku.setSkuCode(entity.getSkuCode());
            cdWhSku.setSkuName(entity.getSkuName());
            cdWhSku.setUpdateBy(UserUtils.getUser());
            cdWhSku.setUpdateDate(new Date());
            if (null == entity.getCsQty()) {
                cdWhSku.setPackCode("01");
            } else {
                cdWhSku.setPackCode(cdWhSku.getSkuCode() + cdWhSku.getOwnerCode());
                BanQinCdWhPackageRelation cs = cdWhPackageRelationService.findByPackageUom(cdWhSku.getPackCode(), "CS", cdWhSku.getOrgId());
                if (cs != null) {
                    cs.setCdprQuantity(entity.getCsQty().doubleValue());
                    cdWhPackageRelationService.save(cs);
                } else {
                    BanQinCdWhPackage cdWhPackage = new BanQinCdWhPackage();
                    cdWhPackage.setCdpaCode(cdWhSku.getPackCode());
                    cdWhPackage.setCdpaType("M_P_G");
                    cdWhPackage.setCdpaFormat(cdWhSku.getPackCode());
                    cdWhPackage.setCdpaIsUse("0");
                    cdWhPackage.setCdpaFormatEn(cdWhSku.getPackCode());
                    cdWhPackage.setOrgId(cdWhSku.getOrgId());
                    cdWhPackage.setRecVer(0);
                    cdWhPackage.setCreateBy(UserUtils.getUser());
                    cdWhPackage.setCreateDate(new Date());
                    cdWhPackage.setUpdateBy(UserUtils.getUser());
                    cdWhPackage.setUpdateDate(new Date());
                    List<BanQinCdWhPackageRelation> cdWhPackageRelations = cdWhPackageRelationService.initialList();
                    for (BanQinCdWhPackageRelation cdWhPackageRelation : cdWhPackageRelations) {
                        cdWhPackageRelation.setOrgId(cdWhSku.getOrgId());
                        cdWhPackageRelation.setRecVer(0);
                        cdWhPackageRelation.setCreateBy(UserUtils.getUser());
                        cdWhPackageRelation.setCreateDate(new Date());
                        cdWhPackageRelation.setUpdateBy(UserUtils.getUser());
                        cdWhPackageRelation.setUpdateDate(new Date());
                        if ("CS".equals(cdWhPackageRelation.getCdprUnitLevel())) {
                            cdWhPackageRelation.setCdprQuantity(entity.getCsQty().doubleValue());
                        }
                        if ("PL".equals(cdWhPackageRelation.getCdprUnitLevel())) {
                            cdWhPackageRelation.setCdprQuantity(entity.getPlQty() != null ? entity.getPlQty().doubleValue() : 1d);
                            cdWhPackageRelation.setCdprTi(entity.getCdprTi() != null ? entity.getCdprTi().doubleValue() : 0d);
                            cdWhPackageRelation.setCdprHi(entity.getCdprHi() != null ? entity.getCdprHi().doubleValue() : 0d);
                        }
                    }
                    cdWhPackage.setPackageDetailList(cdWhPackageRelations);
                    cdWhPackageService.save(cdWhPackage);
                }
            }
            cdWhSkuService.save(cdWhSku);
        }*/
        BanQinWmAsnDetailEntity detailEntity = new BanQinWmAsnDetailEntity();
        detailEntity.setOrgId(entity.getOrgId());
        detailEntity.setSkuCode(entity.getSkuCode());
        detailEntity.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        detailEntity.setOwnerCode(cdWhSku.getOwnerCode());
        detailEntity.setSkuCode(cdWhSku.getSkuCode());
        detailEntity.setPackCode(cdWhSku.getPackCode());
        detailEntity.setUom(StringUtils.isNotBlank(entity.getUom()) ? entity.getUom() : "EA");
        BanQinCdWhPackageRelation packageUom = cdWhPackageRelationService.findByPackageUom(cdWhSku.getPackCode(), detailEntity.getUom(), cdWhSku.getOrgId());
        detailEntity.setQtyAsnEa(entity.getQty().doubleValue() * packageUom.getCdprQuantity());
        detailEntity.setQtyRcvEa(0D);
        detailEntity.setPrice(0d);
        detailEntity.setReserveCode(cdWhSku.getReserveCode());
        detailEntity.setPaRule(cdWhSku.getPaRule());
        detailEntity.setIsQc(cdWhSku.getIsQc());
        detailEntity.setQcPhase(cdWhSku.getQcPhase());
        detailEntity.setQcRule(cdWhSku.getQcRule());
        detailEntity.setItemGroupCode(cdWhSku.getItemGroupCode());
        detailEntity.setOrgId(entity.getOrgId());
        detailEntity.setTraceId(StringUtils.isNotBlank(entity.getTraceId()) ? entity.getTraceId() : WmsConstants.TRACE_ID);
        detailEntity.setPlanToLoc(entity.getPlanRcvLoc());
        detailEntity.setLotAtt01(StringUtils.isNotBlank(entity.getLotAtt01()) ? DateUtils.parseDate(entity.getLotAtt01()) : null);
        detailEntity.setLotAtt02(StringUtils.isNotBlank(entity.getLotAtt02()) ? DateUtils.parseDate(entity.getLotAtt02()) : null);
        detailEntity.setLotAtt03(StringUtils.isNotBlank(entity.getLotAtt03()) ? DateUtils.parseDate(entity.getLotAtt03()) : null);
        detailEntity.setLotAtt04(entity.getLotAtt04());
        detailEntity.setLotAtt05(entity.getLotAtt05());
        detailEntity.setLotAtt06(entity.getLotAtt06());
        detailEntity.setLotAtt07(entity.getLotAtt07());
        detailEntity.setLotAtt08(entity.getLotAtt08());
        detailEntity.setLotAtt09(entity.getLotAtt09());
        detailEntity.setLotAtt10(entity.getLotAtt10());
        detailEntity.setLotAtt11(entity.getLotAtt11());
        detailEntity.setLotAtt12(entity.getLotAtt12());
        detailEntity.setInboundTime(entity.getInboundTime());
        return detailEntity;
    }

    @Transactional
    public ResultMessage serialReceive(List<BanQinWmAsnSerialReceiveImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        Map<String, List<BanQinWmAsnSerialReceiveImportEntity>> collect = list.stream().collect(Collectors.groupingBy(BanQinWmAsnSerialReceiveImportEntity::getOrderNo, LinkedHashMap::new, Collectors.toList()));
        int index = 2;
        List<BanQinWmAsnDetailReceiveEntity> result = ListUtil.newArrayList();
        for (Map.Entry<String, List<BanQinWmAsnSerialReceiveImportEntity>> entry : collect.entrySet()) {
            BanQinWmAsnSerialReceiveImportEntity importEntity = entry.getValue().get(0);
            StringBuilder checkNull = checkOrderForSerialReceive(importEntity);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                break;
            }
            int lineIndex = 1;
            for (BanQinWmAsnSerialReceiveImportEntity detailEntity : entry.getValue()) {
                StringBuilder detailNull = checkOrderForSerialReceiveDetail(detailEntity);
                if (StringUtils.isNotEmpty(detailNull.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailNull.toString()).append("<br>");
                    break;
                }
                lineIndex++;
            }
            if (errorMsg.length() > 0) break;
            Map<String, List<BanQinWmAsnSerialReceiveImportEntity>> skuMap = entry.getValue().stream().collect(Collectors.groupingBy(BanQinWmAsnSerialReceiveImportEntity::getSkuCode));
            Map<String, List<BanQinWmAsnDetailReceiveEntity>> receiveMap = banQinWmAsnDetailReceiveService.getEntityByAsnNo(importEntity.getOrderNo(), importEntity.getOrgId()).stream().collect(Collectors.groupingBy(BanQinWmAsnDetailReceiveEntity::getSkuCode));
            for (Map.Entry<String, List<BanQinWmAsnSerialReceiveImportEntity>> skuEntry : skuMap.entrySet()) {
                List<BanQinWmAsnDetailReceiveEntity> receiveEntities = receiveMap.get(skuEntry.getKey());
                if (CollectionUtil.isEmpty(receiveEntities)) {
                    errorMsg.append("商品编码[").append(skuEntry.getKey()).append("]不在订单中").append("<br>");
                    break;
                }
                BanQinCdWhSku cdWhSku = cdWhSkuService.getByOwnerAndSkuCode(receiveEntities.get(0).getOwnerCode(), skuEntry.getKey(), receiveEntities.get(0).getOrgId());
                if (null == cdWhSku || !cdWhSku.getIsSerial().equals("Y")) {
                    errorMsg.append("商品编码[").append(skuEntry.getKey()).append("]不是序列号管理").append("<br>");
                    break;
                }
                double sum = receiveEntities.stream().mapToDouble(BanQinWmAsnDetailReceiveEntity::getQtyPlanEa).sum();
                if (sum < skuEntry.getValue().size()) {
                    errorMsg.append("商品编码[").append(skuEntry.getKey()).append("]的序列号数量大于订单[").append(importEntity.getOrderNo()).append("]的待收货总数").append("<br>");
                    break;
                }
                // 序列号绑定收货明细
                addSerialToReceiveDetail(receiveEntities, skuEntry.getValue());
                result.addAll(receiveEntities);
            }
            index += entry.getValue().size();
        }

        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }
        // 序列号收货
        for (BanQinWmAsnDetailReceiveEntity entity : result) {
            ResultMessage message = this.serialReceiving(entity);
            if (!message.isSuccess()) {
                msg.setSuccess(false);
                msg.addMessage(message.getMessage() + "<br>");
            }
        }

        if (StringUtils.isBlank(msg.getMessage())) {
            msg.setSuccess(true);
            msg.setMessage("操作成功");
        }

        return msg;
    }

    private StringBuilder checkOrderForSerialReceive(BanQinWmAsnSerialReceiveImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(entity.getOrderNo())) {
            builder.append("订单号为空!");
            return builder;
        }
        BanQinWmAsnHeader wmAsnHeader = getByAsnNo(entity.getOrderNo(), entity.getOrgId());
        if (null == wmAsnHeader) {
            builder.append("订单不存在!");
            return builder;
        }
        if (!wmAsnHeader.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            builder.append("订单未审核, 不能收货!");
            return builder;
        }
        if (!wmAsnHeader.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode()) && !wmAsnHeader.getStatus().equals(WmsCodeMaster.ASN_PART_RECEIVING.getCode())) {
            builder.append("订单不存在可收货明细行!");
            return builder;
        }
        List<BanQinWmAsnDetailReceiveEntity> list = banQinWmAsnDetailReceiveService.getEntityByAsnNo(entity.getOrderNo(), entity.getOrgId());
        if (CollectionUtil.isEmpty(list)) {
            builder.append("订单不存在可收货明细行!");
            return builder;
        }

        return builder;
    }

    private StringBuilder checkOrderForSerialReceiveDetail(BanQinWmAsnSerialReceiveImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (StringUtils.isBlank(entity.getSerialNo())) {
            builder.append("序列号为空!");
        }
        if (StringUtils.isBlank(entity.getToLoc())) {
            builder.append("收货库位为空!");
        } else {
            BanQinCdWhLoc loc = cdWhLocService.findByLocCode(entity.getToLoc(), entity.getOrgId());
            if (null == loc) {
                builder.append("收货库位不存在!");
            }
        }
        Pattern chinaP = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        if (StringUtils.isNotBlank(entity.getLotAtt01())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt01()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt01());
            } else {
                builder.append("生产日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt02())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt02()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt02());
            } else {
                builder.append("失效日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt03())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt03()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt03());
            } else {
                builder.append("入库日期格式错误!");
            }
        }

        List<String> lotAtt04 = Arrays.asList(WmsCodeMaster.QC_ATT_QUALIFIED.getCode(), WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
        if (StringUtils.isNotEmpty(entity.getLotAtt04()) && !lotAtt04.contains(entity.getLotAtt04())) {
            builder.append("品质值填写错误!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt05()) && entity.getLotAtt05().length() > 64) {
            builder.append("批次属性5长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt06()) && entity.getLotAtt06().length() > 64) {
            builder.append("批次属性6长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt07()) && entity.getLotAtt07().length() > 64) {
            builder.append("批次属性7长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt08()) && entity.getLotAtt08().length() > 64) {
            builder.append("批次属性8长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt09()) && entity.getLotAtt09().length() > 64) {
            builder.append("批次属性9长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt10()) && entity.getLotAtt10().length() > 64) {
            builder.append("批次属性10长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt11()) && entity.getLotAtt11().length() > 64) {
            builder.append("批次属性11长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt12()) && entity.getLotAtt12().length() > 64) {
            builder.append("批次属性12长度不能超过64个字符!");
        }

        return builder;
    }

    private void addSerialToReceiveDetail(List<BanQinWmAsnDetailReceiveEntity> receiveEntities, List<BanQinWmAsnSerialReceiveImportEntity> importList) {
        for (BanQinWmAsnDetailReceiveEntity receiveEntity : receiveEntities) {
            List<BanQinWmAsnSerialEntity> result = ListUtil.newArrayList();
            List<BanQinWmAsnSerialReceiveImportEntity> importEntities;
            if (importList.size() == 0) break;
            if (receiveEntity.getQtyPlanEa() > importList.size()) {
                importEntities = importList;
            } else {
                importEntities = importList.subList(0, receiveEntity.getQtyRcvEa().intValue());
            }
            for (BanQinWmAsnSerialReceiveImportEntity importEntity : importEntities) {
                BanQinWmAsnSerialEntity serialEntity = new BanQinWmAsnSerialEntity();
                serialEntity.setAsnNo(importEntity.getOrderNo());
                serialEntity.setOwnerCode(receiveEntity.getOwnerCode());
                serialEntity.setStatus("10");
                serialEntity.setRcvLineNo(receiveEntity.getLineNo());
                serialEntity.setSerialNo(importEntity.getSerialNo());
                serialEntity.setSkuCode(importEntity.getSkuCode());
                serialEntity.setHeadId(receiveEntity.getHeadId());
                serialEntity.setOrgId(receiveEntity.getOrgId());
                result.add(serialEntity);
            }
            BanQinWmAsnSerialReceiveImportEntity firstImport = importEntities.get(0);
            receiveEntity.setLotAtt01(StringUtils.isNotBlank(firstImport.getLotAtt01()) ? DateUtils.parseDate(firstImport.getLotAtt01()) : null);
            receiveEntity.setLotAtt02(StringUtils.isNotBlank(firstImport.getLotAtt02()) ? DateUtils.parseDate(firstImport.getLotAtt02()) : null);
            receiveEntity.setLotAtt03(StringUtils.isNotBlank(firstImport.getLotAtt03()) ? DateUtils.parseDate(firstImport.getLotAtt03()) : null);
            receiveEntity.setLotAtt04(firstImport.getLotAtt04());
            receiveEntity.setLotAtt05(firstImport.getLotAtt05());
            receiveEntity.setLotAtt06(firstImport.getLotAtt06());
            receiveEntity.setLotAtt07(firstImport.getLotAtt07());
            receiveEntity.setLotAtt08(firstImport.getLotAtt08());
            receiveEntity.setLotAtt09(firstImport.getLotAtt09());
            receiveEntity.setLotAtt10(firstImport.getLotAtt10());
            receiveEntity.setLotAtt11(firstImport.getLotAtt11());
            receiveEntity.setLotAtt12(firstImport.getLotAtt12());
            receiveEntity.setSerialList(result);
            receiveEntity.setToLoc(firstImport.getToLoc());
            receiveEntity.setCurrentQtyRcvEa((double) importEntities.size());
            importList.removeAll(importEntities);
        }
    }

    /**
     * 验收单报表数据
     */
    public List<WmCheckReceiveOrderLabel> getCheckReceiveOrder(List<String> asnHeaderIds) {
        return mapper.getCheckReceiveOrder(asnHeaderIds);
    }
}