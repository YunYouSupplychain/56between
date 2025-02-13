package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcClass;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcClassService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcHeaderService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.inbound.entity.*;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.qc.entity.*;
import com.yunyou.modules.wms.qc.service.BanQinWmQcDetailService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcHeaderService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcSkuService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 描述： 入库QC操作Service
 *
 * @auther: Jianhua on 2019/1/29
 */
@Service
public class BanQinInboundQcOperationService {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinCdRuleQcHeaderService banQinCdRuleQcHeaderService;
    @Autowired
    private BanQinCdRuleQcClassService banQinCdRuleQcClassService;
    @Autowired
    private BanQinWmQcHeaderService banQinWmQcHeaderService;
    @Autowired
    private BanQinWmQcSkuService banQinWmQcSkuService;
    @Autowired
    private BanQinWmQcDetailService banQinWmQcDetailService;
    @Autowired
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private BanQinInventoryService banQinInventoryService;
    @Autowired
    private BanQinInboundPaOperationService banQinInboundPaOperationService;

    /**
     * 描述： 勾选ASN单生成QC
     *
     * @param asnIds
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage createQcByAsn(String[] asnIds) {
        ResultMessage msg = new ResultMessage();
        for (String asnId : asnIds) {
            List<String> lineNos = Lists.newArrayList();
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
            List<BanQinWmAsnDetail> list = banQinWmAsnDetailService.getByAsnNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getOrgId());
            for (BanQinWmAsnDetail wmAsnDetailModel : list) {
                lineNos.add(wmAsnDetailModel.getLineNo());
            }
            ResultMessage message = banQinInboundOperationService.checkAsnIsOperateStatus(asnId);
            if (!message.isSuccess()) {
                msg.addMessage(message.getMessage());
                continue;
            }
            ResultMessage errorMsg = createQcByAsnLineNo(wmAsnHeader.getAsnNo(), lineNos.toArray(new String[]{}), wmAsnHeader.getOrgId());
            msg.addMessage(errorMsg.getMessage());
        }
        return msg;
    }

    /**
     * 描述： 勾选ASN商品明细生成QC
     *
     * @param asnNo
     * @param lineNos
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage createQcByAsnLineNo(String asnNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 查询出收货明细质检状态为空并且需要质检管理的商品明细
        String str = "";
        BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(asnNo, orgId);
        // 按质检阶段分组
        Map<String, List<BanQinWmAsnDetailEntity>> map = getAvailQcDetail(asnNo, lineNos, orgId);
        if (map.isEmpty()) {
            msg.setSuccess(false);
            msg.addMessage(asnNo + "没有满足需求的质检单生成");
            return msg;
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            List<BanQinWmAsnDetailEntity> list = map.get(key);
            String qcNo = "";
            String headId = "";
            if (list.size() > 0) {
                BanQinWmQcHeader wmQcHeaderModel = new BanQinWmQcHeader();
                wmQcHeaderModel.setOrderNo(asnNo);
                wmQcHeaderModel.setQcPhase(key);
                wmQcHeaderModel.setOrderType(WmsCodeMaster.ORDER_ASN.getCode());
                wmQcHeaderModel.setOrderTime(new Date());
                wmQcHeaderModel.setOwnerCode(wmAsnHeaderModel.getOwnerCode());
                wmQcHeaderModel.setPriority(wmAsnHeaderModel.getPriority());
                wmQcHeaderModel.setOrgId(orgId);
                ResultMessage message = banQinWmQcHeaderService.saveModel(wmQcHeaderModel);
                BanQinWmQcHeader qcHeader = (BanQinWmQcHeader) message.getData();
                qcNo = qcHeader.getQcNo();
                headId = qcHeader.getId();
                str = str + qcNo + "\n";
                // 保存商品明细及其质检明细
                for (BanQinWmAsnDetailEntity wmAsnDetailEntity : list) {
                    createQcSkuAndDetail(qcNo, key, wmAsnDetailEntity, headId);
                }
            }
        }
        // 更新状态
        if (StringUtils.isEmpty(wmAsnHeaderModel.getQcStatus())) {
            wmAsnHeaderModel.setQcStatus(WmsCodeMaster.QC_NEW.getCode());
            banQinWmAsnHeaderService.save(wmAsnHeaderModel);
        }
        if (StringUtils.isNotEmpty(str)) {
            msg.setSuccess(true);
            msg.addMessage(asnNo + "操作成功，生成质检单号：" + str);
        }
        return msg;
    }

    /**
     * 构造质检商品明细及质检明细
     * @param newQcNo
     * @param qcPhase
     * @param wmAsnDetailEntity
     * @return
     */
    @Transactional
    public ResultMessage createQcSkuAndDetail(String newQcNo, String qcPhase, BanQinWmAsnDetailEntity wmAsnDetailEntity, String headId) {
        ResultMessage msg = new ResultMessage();
        List<BanQinWmAsnDetailReceiveEntity> asnRcvList = wmAsnDetailEntity.getWmAsnDetailReceiveEntities();

        // 生成质检单商品明细
        BanQinWmQcSku wmQcSkuModel = new BanQinWmQcSku();
        wmQcSkuModel.setQcNo(newQcNo);
        wmQcSkuModel.setOrderNo(wmAsnDetailEntity.getAsnNo());
        wmQcSkuModel.setOrderLineNo(wmAsnDetailEntity.getLineNo());
        wmQcSkuModel.setOwnerCode(wmAsnDetailEntity.getOwnerCode());
        wmQcSkuModel.setSkuCode(wmAsnDetailEntity.getSkuCode());
        wmQcSkuModel.setPackCode(wmAsnDetailEntity.getPackCode());
        wmQcSkuModel.setUom(wmAsnDetailEntity.getUom());
        wmQcSkuModel.setQcRule(wmAsnDetailEntity.getQcRule());
        wmQcSkuModel.setItemGroupCode(wmAsnDetailEntity.getItemGroupCode());
        wmQcSkuModel.setQtyAvailQcEa(wmAsnDetailEntity.getCurrentQtyQc());
        wmQcSkuModel.setPaRule(wmAsnDetailEntity.getPaRule());
        wmQcSkuModel.setTraceId(wmAsnDetailEntity.getTraceId());
        wmQcSkuModel.setLocCode(wmAsnDetailEntity.getPlanToLoc());
        wmQcSkuModel.setQtyPlanQcEa(getQtyPlanQc(wmQcSkuModel.getQcRule(), wmQcSkuModel.getQtyAvailQcEa(), wmAsnDetailEntity.getOrgId()));
        wmQcSkuModel.setLotAtt01(wmAsnDetailEntity.getLotAtt01());
        wmQcSkuModel.setLotAtt02(wmAsnDetailEntity.getLotAtt02());
        wmQcSkuModel.setLotAtt03(wmAsnDetailEntity.getLotAtt03());
        wmQcSkuModel.setLotAtt04(wmAsnDetailEntity.getLotAtt04());
        wmQcSkuModel.setLotAtt05(wmAsnDetailEntity.getLotAtt05());
        wmQcSkuModel.setLotAtt06(wmAsnDetailEntity.getLotAtt06());
        wmQcSkuModel.setLotAtt07(wmAsnDetailEntity.getLotAtt07());
        wmQcSkuModel.setLotAtt08(wmAsnDetailEntity.getLotAtt08());
        wmQcSkuModel.setLotAtt09(wmAsnDetailEntity.getLotAtt09());
        wmQcSkuModel.setLotAtt10(wmAsnDetailEntity.getLotAtt10());
        wmQcSkuModel.setLotAtt11(wmAsnDetailEntity.getLotAtt11());
        wmQcSkuModel.setLotAtt12(wmAsnDetailEntity.getLotAtt12());
        wmQcSkuModel.setOrgId(wmAsnDetailEntity.getOrgId());
        wmQcSkuModel.setHeadId(headId);
        wmQcSkuModel = banQinWmQcSkuService.saveQcSkuModel(wmQcSkuModel);

        // 生成质检单的质检明细
        for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : asnRcvList) {
            String qcRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
            BanQinWmQcDetail wmQcDetailModel = new BanQinWmQcDetail();
            wmQcDetailModel.setQcNo(newQcNo);
            wmQcDetailModel.setStatus(WmsCodeMaster.QC_NEW.getCode());
            wmQcDetailModel.setQcLineNo(wmQcSkuModel.getLineNo());
            wmQcDetailModel.setQcRcvId(qcRcvId);
            wmQcDetailModel.setOrderNo(wmAsnDetailReceiveEntity.getAsnNo());
            wmQcDetailModel.setOrderLineNo(wmAsnDetailReceiveEntity.getAsnLineNo());
            wmQcDetailModel.setOwnerCode(wmAsnDetailReceiveEntity.getOwnerCode());
            wmQcDetailModel.setSkuCode(wmAsnDetailReceiveEntity.getSkuCode());
            wmQcDetailModel.setPackCode(wmAsnDetailReceiveEntity.getPackCode());
            wmQcDetailModel.setUom(wmAsnDetailReceiveEntity.getUom());
            wmQcDetailModel.setQcRule(wmAsnDetailReceiveEntity.getQcRule());
            wmQcDetailModel.setItemGroupCode(wmAsnDetailReceiveEntity.getItemGroupCode());
            wmQcDetailModel.setLotAtt01(wmAsnDetailReceiveEntity.getLotAtt01());
            wmQcDetailModel.setLotAtt02(wmAsnDetailReceiveEntity.getLotAtt02());
            wmQcDetailModel.setLotAtt03(wmAsnDetailReceiveEntity.getLotAtt03());
            wmQcDetailModel.setLotAtt04(wmAsnDetailReceiveEntity.getLotAtt04());
            wmQcDetailModel.setLotAtt05(wmAsnDetailReceiveEntity.getLotAtt05());
            wmQcDetailModel.setLotAtt06(wmAsnDetailReceiveEntity.getLotAtt06());
            wmQcDetailModel.setLotAtt07(wmAsnDetailReceiveEntity.getLotAtt07());
            wmQcDetailModel.setLotAtt08(wmAsnDetailReceiveEntity.getLotAtt08());
            wmQcDetailModel.setLotAtt09(wmAsnDetailReceiveEntity.getLotAtt09());
            wmQcDetailModel.setLotAtt10(wmAsnDetailReceiveEntity.getLotAtt10());
            wmQcDetailModel.setLotAtt11(wmAsnDetailReceiveEntity.getLotAtt11());
            wmQcDetailModel.setLotAtt12(wmAsnDetailReceiveEntity.getLotAtt12());
            wmQcDetailModel.setLotNum(wmAsnDetailReceiveEntity.getLotNum());
            wmQcDetailModel.setHeadId(headId);
            wmQcDetailModel.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
            // 如果是收货前，跟踪号 = 码盘跟踪号 ；收货后，跟踪号 = 收货跟踪号
            if (qcPhase.equals(WmsCodeMaster.QC_PHASE_B_RCV.getCode())) {
                wmQcDetailModel.setLocCode(wmAsnDetailEntity.getPlanToLoc());
                wmQcDetailModel.setTraceId(wmAsnDetailReceiveEntity.getPlanId());
                wmQcDetailModel.setQtyAvailQcEa(wmAsnDetailReceiveEntity.getQtyPlanEa());
                // 良品，不良品库位和跟踪号默认为计划的库位和跟踪号。
                wmQcDetailModel.setQuaLoc(wmAsnDetailEntity.getPlanToLoc());
                wmQcDetailModel.setQuaTraceId(wmAsnDetailReceiveEntity.getPlanId());
                wmQcDetailModel.setUnquaLoc(wmAsnDetailEntity.getPlanToLoc());
                wmQcDetailModel.setUnquaTraceId(wmAsnDetailReceiveEntity.getPlanId());
            } else {
                wmQcDetailModel.setLocCode(wmAsnDetailReceiveEntity.getToLoc());
                wmQcDetailModel.setTraceId(wmAsnDetailReceiveEntity.getToId());
                wmQcDetailModel.setQtyAvailQcEa(wmAsnDetailReceiveEntity.getQtyRcvEa());
                // 良品，不良品库位和跟踪号默认为收货的库位和跟踪号。
                wmQcDetailModel.setQuaLoc(wmAsnDetailReceiveEntity.getToLoc());
                wmQcDetailModel.setQuaTraceId(wmAsnDetailReceiveEntity.getToId());
                wmQcDetailModel.setUnquaLoc(wmAsnDetailReceiveEntity.getToLoc());
                wmQcDetailModel.setUnquaTraceId(wmAsnDetailReceiveEntity.getToId());
            }
            banQinWmQcDetailService.saveQcDetail(wmQcDetailModel);
            if (StringUtils.isEmpty(wmAsnDetailReceiveEntity.getQcStatus())) {
                wmAsnDetailReceiveEntity.setQcRcvId(qcRcvId);// 质检收货明细号
                wmAsnDetailReceiveEntity.setQcStatus(WmsCodeMaster.QC_NEW.getCode());

                BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
                BeanUtils.copyProperties(wmAsnDetailReceiveEntity, wmAsnDetailReceiveModel);
                banQinWmAsnDetailReceiveService.save(wmAsnDetailReceiveModel);
            }
        }
        // 更新ASN质检状态
        if (StringUtils.isEmpty(wmAsnDetailEntity.getQcStatus())) {
            wmAsnDetailEntity.setQcStatus(WmsCodeMaster.QC_NEW.getCode());

            BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
            BeanUtils.copyProperties(wmAsnDetailEntity, wmAsnDetailModel);
            banQinWmAsnDetailService.save(wmAsnDetailModel);
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 查询出收货明细质检状态为空并且需要质检管理，商品明细不为完全质检，未安排库位的
     * @param asnNo
     * @param lineNos
     * @param orgId
     * @return
     */
    private Map<String, List<BanQinWmAsnDetailEntity>> getAvailQcDetail(String asnNo, String[] lineNos, String orgId) {
        List<String> lineNoList = Arrays.asList(lineNos);
        List<BanQinWmAsnDetailEntity> detailList = Lists.newArrayList();

        int size = lineNoList.size();
        for (int i = 0; i < size; i++) {
            detailList.add(banQinWmAsnDetailService.getEntityByAsnNoAndLineNo(asnNo, lineNos[i], orgId));
        }
        // 按质检阶段分组
        Map<String, List<BanQinWmAsnDetailEntity>> map = Maps.newHashMap();
        for (BanQinWmAsnDetailEntity entity : detailList) {
            double currentQtyQc = 0D;// 当前可质检数
            List<BanQinWmAsnDetailEntity> entityList = Lists.newArrayList();
            List<BanQinWmAsnDetailReceiveEntity> rcvEntityList = Lists.newArrayList();
            // 查询质检状态为空的收货明细
            List<BanQinWmAsnDetailReceive> list = banQinWmAsnDetailReceiveService.findUnQcDetail(asnNo, entity.getLineNo(), orgId);
            for (BanQinWmAsnDetailReceive wmAsnDetailReceiveModel : list) {
                BanQinWmAsnDetailReceiveEntity rcv = new BanQinWmAsnDetailReceiveEntity();
                BeanUtils.copyProperties(wmAsnDetailReceiveModel, rcv);
                // 如果是收货后质检，不能是创建状态的收货明细
                if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(entity.getQcPhase()) && WmsCodeMaster.ASN_NEW.getCode().equals(rcv.getStatus())) {
                    continue;
                }
                currentQtyQc = currentQtyQc + rcv.getQtyPlanEa();
                rcvEntityList.add(rcv);
            }
            entity.setCurrentQtyQc(currentQtyQc);
            entity.setWmAsnDetailReceiveEntities(rcvEntityList);
            // 分组
            String key = entity.getQcPhase();
            if (map.containsKey(key)) {
                entityList = map.get(key);
            }
            if (entity.getWmAsnDetailReceiveEntities().size() > 0) {
                entityList.add(entity);
                map.put(key, entityList);
            }
        }
        return map;
    }

    /**
     * 计算计划质检数
     * @param qcRule
     * @param qtyAvailQc
     * @param orgId
     * @return
     * @throws WarehouseException
     */
    private Double getQtyPlanQc(String qcRule, Double qtyAvailQc, String orgId) throws WarehouseException {
        Double qtyPlanQcEa = 0D;
        if (StringUtils.isEmpty(qcRule)) {
            throw new WarehouseException("质检规则不能为空");// "质检规则不能为空");
        }
        // 如果是全检，计划质检数 = 可质检数
        BanQinCdRuleQcHeader qcModel = banQinCdRuleQcHeaderService.getByRuleCode(qcRule, orgId);
        if (null == qcModel) {
            // "质检规则"+qcRule+"不存在");
            throw new WarehouseException("质检规则" + qcRule + "不存在");
        }
        if (WmsCodeMaster.QC_TYPE_FULL.getCode().equals(qcModel.getQcType())) {
            qtyPlanQcEa = qtyAvailQc;
            return qtyPlanQcEa;
        }
        // 抽检，循环级差。
        Collection<BanQinCdRuleQcClass> list = banQinCdRuleQcClassService.getCdRuleQcClassByRuleCode(qcRule, orgId);
        for (BanQinCdRuleQcClass cdRuleQcClassModel : list) {
            if (qtyAvailQc <= cdRuleQcClassModel.getToClass() && qtyAvailQc > cdRuleQcClassModel.getFmClass()) {
                // 如果抽检是按数量，则 计划质检数 = 级差数
                if (WmsCodeMaster.QTY_TYPE_QTY.getCode().equals(cdRuleQcClassModel.getQtyType())) {
                    qtyPlanQcEa = cdRuleQcClassModel.getQty();
                } else {
                    qtyPlanQcEa = qtyAvailQc * (cdRuleQcClassModel.getQty() / 100);
                }
                break;
            }
        }
        return qtyPlanQcEa;
    }

//***************************************************QC************************************************************************//

    /**
     * 描述： 批量质检确认
     *
     * @param wmQcSkuEntities
     * @author Jianhua on 2019/1/30
     */
    public ResultMessage inboundBatchQcConfirm(List<BanQinWmQcSkuEntity> wmQcSkuEntities) {
        ResultMessage msg = new ResultMessage();
        BanQinWmQcSkuEntity wmQcSkuEntity = wmQcSkuEntities.get(0);
        String qcNo = wmQcSkuEntity.getQcNo();
        List<String> lineNos = Lists.newArrayList();
        Map<String, BanQinWmQcSkuEntity> map = Maps.newHashMap();
        for (BanQinWmQcSkuEntity entity : wmQcSkuEntities) {
            // 分组
            String key = entity.getLineNo();
            map.put(key, entity);
            if (!lineNos.contains(key)) {
                lineNos.add(key);
            }
        }
        ResultMessage errorMsg = banQinWmQcSkuService.checkQcSkuStatus(qcNo, new String[]{WmsCodeMaster.QC_NEW.getCode(), WmsCodeMaster.QC_PART_QC.getCode()}, lineNos.toArray(new String[]{}),
                new String[]{WmsCodeMaster.QC_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()}, WmsConstants.YES, wmQcSkuEntity.getOrgId());
        String[] updateLineNos = (String[]) errorMsg.getData();
        for (String lineNo : updateLineNos) {
            try {
                this.qcConfirm(map.get(lineNo));
            } catch (WarehouseException e) {
                msg.addMessage("行号" + lineNo + e.getMessage());
            }
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage("行号" + "\n" + errorMsg.getMessage() + "未审核、未质检、已完全质检、已关闭的订单，不能操作");
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 描述： 质检确认
     *
     * @param wmQcSkuEntity
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage qcConfirm(BanQinWmQcSkuEntity wmQcSkuEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String qcNo = wmQcSkuEntity.getQcNo();
        String lineNo = wmQcSkuEntity.getLineNo();
        String orgId = wmQcSkuEntity.getOrgId();
        // 实际质检建议
        String qcActSuggest = wmQcSkuEntity.getQcActSuggest();
        // 校验非空
        if (StringUtils.isEmpty(qcActSuggest)) {
            throw new WarehouseException("实际质检建议不能为空");
        }
        if (null == wmQcSkuEntity.getPctQua()) {
            throw new WarehouseException("合格率不能为空");
        }

        BanQinWmQcEntity qcEntity = banQinWmQcHeaderService.findEntityByQcNo(qcNo, orgId);
        String aduitStatus = qcEntity.getAuditStatus();
        if (aduitStatus.equals(WmsCodeMaster.AUDIT_NEW.getCode())) {
            throw new WarehouseException("订单未审核，不能操作");
        }
        if (WmsCodeMaster.ASN_CLOSE.getCode().equals(qcEntity.getStatus())) {
            throw new WarehouseException("订单已关闭，不能操作");
        }

        // 升级为全检处理：更新质检明细及商品明细为关闭，复制一条新建状态的质检行
        if (WmsCodeMaster.QC_SUGGEST_C01.getCode().equals(qcActSuggest)) {
            qcSuggestForFullQc(wmQcSkuEntity);
            msg.addMessage("操作成功");
            msg.setSuccess(true);
            return msg;
        }
        // 现场决定：收货后质检；收货前，不做处理。
        if (WmsCodeMaster.QC_SUGGEST_D01.getCode().equals(qcActSuggest) && WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(qcEntity.getQcPhase())) {
            if (wmQcSkuEntity.getQtyAvailQcEa().equals(wmQcSkuEntity.getQtyQcQuaEa() + wmQcSkuEntity.getQtyQcUnquaEa())) {
                throw new WarehouseException("合格数与不合格数之和需等于可质检数");
            }
        }
        // 其他质检建议
        // 质检明细
        Double qtyQuaEa = 0D;
        Double qtyUnquaEa = 0D;
        List<BanQinWmQcDetail> detailList = banQinWmQcDetailService.findByQcNoAndQcLineNo(qcNo, lineNo, orgId);
        for (BanQinWmQcDetail wmQcDetailModel : detailList) {
            ResultMessage detailMsg = sliptAsnRcvByQcSuggest(qcActSuggest, wmQcDetailModel);
            BanQinWmQcDetail detailModel = (BanQinWmQcDetail) detailMsg.getData();
            qtyQuaEa = qtyQuaEa + detailModel.getQtyQuaEa();
            qtyUnquaEa = qtyUnquaEa + detailModel.getQtyUnquaEa();
        }
        // 更新质检单状态及ASN的质检状态
        wmQcSkuEntity.setQtyQuaEa(qtyQuaEa);
        wmQcSkuEntity.setQtyUnquaEa(qtyUnquaEa);
        wmQcSkuEntity.setStatus(WmsCodeMaster.QC_FULL_QC.getCode());
        BanQinWmQcSku wmQcSkuModel = new BanQinWmQcSku();
        BeanUtils.copyProperties(wmQcSkuEntity, wmQcSkuModel);
        banQinInboundOperationService.updateQcConfirmStatus(wmQcSkuModel);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 升级为全检处理
     *
     * @param wmQcSkuEntity
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public void qcSuggestForFullQc(BanQinWmQcSkuEntity wmQcSkuEntity) {
        BanQinWmQcSku wmQcSkuModel = new BanQinWmQcSku();
        BeanUtils.copyProperties(wmQcSkuEntity, wmQcSkuModel);
        // 更新状态
        banQinInboundOperationService.updateQcDetailStatus(wmQcSkuEntity.getQcNo(), wmQcSkuEntity.getLineNo(), WmsCodeMaster.QC_CLOSE.getCode(), wmQcSkuEntity.getOrgId());
        wmQcSkuModel.setStatus(WmsCodeMaster.QC_CLOSE.getCode());
        banQinWmQcSkuService.save(wmQcSkuModel);
        // 新建一条全检行
        BanQinWmQcSku newQcSku = new BanQinWmQcSku();
        BeanUtils.copyProperties(wmQcSkuEntity, newQcSku);
        newQcSku.setId(null);
        newQcSku.setLineNo(null);
        newQcSku.setQtyPlanQcEa(wmQcSkuEntity.getQtyAvailQcEa());
        newQcSku.setQtyQcQuaEa(0d);
        newQcSku.setQtyQuaEa(0d);
        newQcSku.setQtyUnquaEa(0d);
        newQcSku.setQtyQuaEa(0d);
        newQcSku.setPctQua(0d);
        newQcSku.setQcActSuggest(null);
        newQcSku.setQcSuggest(null);
        newQcSku = banQinWmQcSkuService.saveQcSkuModel(newQcSku);
        // 质检明细
        List<BanQinWmQcDetail> detailList = banQinWmQcDetailService.findByQcNoAndQcLineNo(wmQcSkuEntity.getQcNo(), wmQcSkuEntity.getLineNo(), wmQcSkuEntity.getOrgId());
        for (BanQinWmQcDetail wmQcDetailModel : detailList) {
            BanQinWmQcDetail newQcDetail = new BanQinWmQcDetail();
            BeanUtils.copyProperties(wmQcDetailModel, newQcDetail);
            newQcDetail.setId(null);
            newQcDetail.setQcLineNo(newQcSku.getLineNo());
            // 良品，不良品库位和跟踪号默认为收货的库位和跟踪号。
            newQcDetail.setQuaLoc(wmQcDetailModel.getLocCode());
            newQcDetail.setQuaLotNum(null);
            newQcDetail.setQuaPaId(null);
            newQcDetail.setQuaTraceId(wmQcDetailModel.getTraceId());
            newQcDetail.setUnquaLoc(wmQcDetailModel.getLocCode());
            newQcDetail.setUnquaLotNum(null);
            newQcDetail.setUnquaPaId(null);
            newQcDetail.setUnquaTraceId(wmQcDetailModel.getTraceId());
            newQcDetail.setQtyQuaEa(0d);
            newQcDetail.setQtyUnquaEa(0d);
            newQcDetail.setQtyQcQuaEa(0d);
            newQcDetail.setQtyQcUnquaEa(0d);
            banQinWmQcDetailService.saveQcDetail(newQcDetail);
        }
    }

    /**
     * 根据质检处理建议，拆分ASN收货明细 收货前 如果区分良品则拆分收货明细，并回填lot04 收货后不拆分收货明细，更新库存，记录交易
     * @param qcSuggest
     * @param wmQcDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage sliptAsnRcvByQcSuggest(String qcSuggest, BanQinWmQcDetail wmQcDetailModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String qcRcvId = wmQcDetailModel.getQcRcvId();// 质检收货明细号
        String qcQuaRcvId = wmQcDetailModel.getQcQuaRcvId();// 质检良品收货明细号
        String qcUnquaRcvId = wmQcDetailModel.getQcUnquaRcvId();// 质检不良品收货明细号
        Double qtyQua = wmQcDetailModel.getQtyQcQuaEa() == null ? 0D : wmQcDetailModel.getQtyQcQuaEa();// 质检合格数
        Double qtyUnQua = wmQcDetailModel.getQtyQcUnquaEa() == null ? 0D : wmQcDetailModel.getQtyQcUnquaEa();// 质检不合格数
        Double qtyAvailQcEa = wmQcDetailModel.getQtyAvailQcEa();

        // 更新ASN收货明细
        String quaLot = "";// 合格的批次号
        String unQuaLot = "";// 不合格的批次号
        String quaLoc = wmQcDetailModel.getQuaLoc();// 良品库位
        String unquaLoc = wmQcDetailModel.getUnquaLoc();// 不良品库位
        BanQinInventoryEntity quaInv;// 执行质检转移后，库存更新，可能忽略跟踪号
        BanQinInventoryEntity unquaInv;
        String quaTraceId = wmQcDetailModel.getQuaTraceId();// 质检确认前的跟踪号
        String unquaTraceId = wmQcDetailModel.getUnquaTraceId();
        List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveModels = banQinWmAsnDetailReceiveService.findByQcRcvId(qcRcvId, wmQcDetailModel.getOrgId());
        if (wmAsnDetailReceiveModels.size() == 0) {
            throw new WarehouseException("查询不到质检明细号" + qcRcvId + "对应的收货明细行");// 查询不到质检明细号{0}对应的收货明细行
        }
        if (wmAsnDetailReceiveModels.size() > 1) {
            throw new WarehouseException("收货明细可能已做了码盘，生成越库任务操作，不能取消质检确认");// "收货明细可能已做了码盘，生成越库任务操作，不能取消质检确认"
        }
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = wmAsnDetailReceiveModels.get(0);
        String fmLot = wmQcDetailModel.getLotNum();// 生成质检单后的批次号(待检)
        String lotAtt04 = wmAsnDetailReceiveModel.getLotAtt04();// 原批次4
        wmAsnDetailReceiveModel.setQcStatus(WmsCodeMaster.QC_FULL_QC.getCode());
        if (WmsCodeMaster.QC_SUGGEST_A01.getCode().equals(qcSuggest)) {
            qtyQua = qtyAvailQcEa;
            qtyUnQua = 0D;
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveModel.getStatus())) {
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                quaLot = getLotNum(wmAsnDetailReceiveModel);
                wmAsnDetailReceiveModel.setLotAtt04(lotAtt04);
                // 质检转移库存，转良品
                quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToQuaInvModel(wmQcDetailModel, quaLot, qtyAvailQcEa));
                quaTraceId = quaInv.getTraceId();
            } else {
                qcQuaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                wmAsnDetailReceiveModel.setQcRcvId(qcQuaRcvId);
                wmAsnDetailReceiveModel.setToId(quaTraceId);
                wmAsnDetailReceiveModel.setToLoc(quaLoc);
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
            }
        }
        if (WmsCodeMaster.QC_SUGGEST_A02.getCode().equals(qcSuggest)) {
            qtyQua = qtyAvailQcEa - qtyUnQua;
            // 质检转移库存
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveModel.getStatus())) {
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                quaLot = getLotNum(wmAsnDetailReceiveModel);
                if (qtyUnQua == 0D) {
                    // 转良品
                    quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToQuaInvModel(wmQcDetailModel, quaLot, qtyAvailQcEa));
                    quaTraceId = quaInv.getTraceId();
                } else {
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
                    unQuaLot = getLotNum(wmAsnDetailReceiveModel);
                    // 转良品
                    quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyQua), getToQuaInvModel(wmQcDetailModel, quaLot, qtyQua));
                    quaTraceId = quaInv.getTraceId();
                    // 转不良品
                    unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyUnQua), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyUnQua));
                    unquaTraceId = unquaInv.getTraceId();
                }
                wmAsnDetailReceiveModel.setLotAtt04(lotAtt04);
            } else {
                qcQuaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                wmAsnDetailReceiveModel.setQcRcvId(qcQuaRcvId);
                wmAsnDetailReceiveModel.setToId(quaTraceId);
                wmAsnDetailReceiveModel.setToLoc(quaLoc);
                if (qtyUnQua == 0D) {
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                } else {
                    qcUnquaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                    wmAsnDetailReceiveModel.setQtyPlanEa(qtyQua);
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                    splitAsnDetailReceive(qtyUnQua, qcUnquaRcvId, wmAsnDetailReceiveModel, wmQcDetailModel);
                }
            }
        }

        if (WmsCodeMaster.QC_SUGGEST_B01.getCode().equals(qcSuggest)) {
            qtyQua = 0D;
            qtyUnQua = qtyAvailQcEa;
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveModel.getStatus())) {
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
                unQuaLot = getLotNum(wmAsnDetailReceiveModel);
                wmAsnDetailReceiveModel.setLotAtt04(lotAtt04);
                // 质检转移库存，转不良品
                unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyAvailQcEa));
                unquaTraceId = unquaInv.getTraceId();
            } else {
                qcUnquaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                wmAsnDetailReceiveModel.setQcRcvId(qcUnquaRcvId);
                wmAsnDetailReceiveModel.setToId(unquaTraceId);
                wmAsnDetailReceiveModel.setToLoc(unquaLoc);
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
            }
        }
        if (WmsCodeMaster.QC_SUGGEST_B02.getCode().equals(qcSuggest)) {
            qtyUnQua = qtyAvailQcEa - qtyQua;

            // 质检转移库存
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveModel.getStatus())) {
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
                unQuaLot = getLotNum(wmAsnDetailReceiveModel);
                if (qtyQua == 0D) {
                    // 转不良品
                    unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyUnQua));
                    unquaTraceId = unquaInv.getTraceId();
                } else {
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                    quaLot = getLotNum(wmAsnDetailReceiveModel);
                    // 转良品
                    quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyQua), getToQuaInvModel(wmQcDetailModel, quaLot, qtyQua));
                    quaTraceId = quaInv.getTraceId();
                    // 转不良品
                    unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyUnQua), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyUnQua));
                    unquaTraceId = unquaInv.getTraceId();
                }
                wmAsnDetailReceiveModel.setLotAtt04(lotAtt04);
            } else {
                qcUnquaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                if (qtyQua == 0D) {
                    wmAsnDetailReceiveModel.setQcRcvId(qcUnquaRcvId);
                    wmAsnDetailReceiveModel.setToId(unquaTraceId);
                    wmAsnDetailReceiveModel.setToLoc(unquaLoc);
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
                } else {
                    qcQuaRcvId = noService.getDocumentNo(GenNoType.WM_QC_RCV_ID.name());
                    wmAsnDetailReceiveModel.setQcRcvId(qcQuaRcvId);
                    wmAsnDetailReceiveModel.setToId(quaTraceId);
                    wmAsnDetailReceiveModel.setToLoc(quaLoc);
                    wmAsnDetailReceiveModel.setQtyPlanEa(qtyQua);
                    wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                    splitAsnDetailReceive(qtyUnQua, qcUnquaRcvId, wmAsnDetailReceiveModel, wmQcDetailModel);
                }
            }
        }

        if (WmsCodeMaster.QC_SUGGEST_D01.getCode().equals(qcSuggest)) {
            // 质检转移库存
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnDetailReceiveModel.getStatus())) {
                if (qtyQua == 0D && qtyUnQua == 0D) {
                    throw new WarehouseException("质检明细行号" + wmQcDetailModel.getLineNo() + "合格数与不合格数不能为同时为0");// "质检明细行号"++"合格数与不合格数不能为同时为0"
                }
                if (!Double.valueOf(qtyQua + qtyUnQua).equals(qtyAvailQcEa)) {
                    throw new WarehouseException("质检明细行号" + wmQcDetailModel.getLineNo() + "合格数与不合格数之和需等于可质检数");// "质检明细行号"合格数与不合格数之和需等于可质检数");
                }
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
                unQuaLot = getLotNum(wmAsnDetailReceiveModel);
                wmAsnDetailReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_QUALIFIED.getCode());
                quaLot = getLotNum(wmAsnDetailReceiveModel);
                if (qtyQua == 0D) {
                    // 全部转不良
                    unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyAvailQcEa));
                    unquaTraceId = unquaInv.getTraceId();
                } else if (qtyUnQua == 0D) {
                    // 全部转良
                    quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyAvailQcEa), getToQuaInvModel(wmQcDetailModel, quaLot, qtyAvailQcEa));
                    quaTraceId = quaInv.getTraceId();
                } else {
                    // 转良品
                    quaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyQua), getToQuaInvModel(wmQcDetailModel, quaLot, qtyQua));
                    quaTraceId = quaInv.getTraceId();
                    // 转不良品
                    unquaInv = banQinInventoryService.updateInventory(getFmInvModel(wmQcDetailModel, fmLot, qtyUnQua), getToUnquaInvModel(wmQcDetailModel, unQuaLot, qtyUnQua));
                    unquaTraceId = unquaInv.getTraceId();
                }
                wmAsnDetailReceiveModel.setLotAtt04(lotAtt04);
            } else {
                wmAsnDetailReceiveModel.setLotAtt04(null);
            }
        }

        banQinWmAsnDetailReceiveService.save(wmAsnDetailReceiveModel);
        // 更新质检明细状态
        wmQcDetailModel.setQcTime(new Date());
        wmQcDetailModel.setStatus(WmsCodeMaster.QC_FULL_QC.getCode());
        wmQcDetailModel.setQtyQuaEa(qtyQua);
        wmQcDetailModel.setQtyUnquaEa(qtyUnQua);
        wmQcDetailModel.setQuaLotNum(quaLot);
        wmQcDetailModel.setUnquaLotNum(unQuaLot);
        wmQcDetailModel.setUnquaTraceId(unquaTraceId);
        wmQcDetailModel.setQuaTraceId(quaTraceId);
        wmQcDetailModel.setQcQuaRcvId(qcQuaRcvId);
        wmQcDetailModel.setQcUnquaRcvId(qcUnquaRcvId);
        banQinWmQcDetailService.save(wmQcDetailModel);
        msg.setData(banQinWmQcDetailService.get(wmQcDetailModel.getId()));
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 收货前，区分良品不良品，拆分明细行
     * @param qtyUnQua
     * @param qcUnquaRcvId
     * @param wmAsnDetailReceiveModel
     * @param wmQcDetailModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public BanQinWmAsnDetailReceive splitAsnDetailReceive(Double qtyUnQua, String qcUnquaRcvId, BanQinWmAsnDetailReceive wmAsnDetailReceiveModel, BanQinWmQcDetail wmQcDetailModel) {
        // 赋值
        BanQinWmAsnDetailReceive newReceiveModel = new BanQinWmAsnDetailReceive();
        BeanUtils.copyProperties(wmAsnDetailReceiveModel, newReceiveModel);
        newReceiveModel.setId(null);
        newReceiveModel.setQcRcvId(qcUnquaRcvId);
        newReceiveModel.setToId(wmQcDetailModel.getUnquaTraceId());
        newReceiveModel.setToLoc(wmQcDetailModel.getUnquaLoc());
        newReceiveModel.setLotAtt04(WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
        newReceiveModel.setQtyPlanEa(qtyUnQua);
        newReceiveModel.setQtyRcvEa(0D);
        // 新增保存
        banQinInboundOperationService.saveAsnDetailReceive(newReceiveModel);
        return newReceiveModel;

    }

    /**
     * SET源转移信息
     * @param wmQcDetailModel
     * @param lotOp
     * @param qtyOp
     * @return
     */
    private BanQinInventoryEntity getFmInvModel(BanQinWmQcDetail wmQcDetailModel, String lotOp, Double qtyOp) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setAction(ActionCode.QC);
        fminvm.setLotNum(lotOp);
        fminvm.setLocCode(wmQcDetailModel.getLocCode());
        fminvm.setTraceId(wmQcDetailModel.getTraceId());
        fminvm.setSkuCode(wmQcDetailModel.getSkuCode());
        fminvm.setOwnerCode(wmQcDetailModel.getOwnerCode());
        fminvm.setOrderNo(wmQcDetailModel.getQcNo());
        fminvm.setLineNo(wmQcDetailModel.getLineNo());
        fminvm.setPackCode(wmQcDetailModel.getPackCode());
        fminvm.setQtyUom(qtyOp);
        fminvm.setUom(WmsConstants.UOM_EA);
        fminvm.setQtyEaOp(qtyOp);
        fminvm.setOrderNo(wmQcDetailModel.getOrgId());
        fminvm.setOrgId(wmQcDetailModel.getOrgId());
        return fminvm;
    }

    /**
     * SET目标转移信息(良品)
     * @param wmQcDetailModel
     * @param lotOp
     * @param qtyOp
     * @return
     */
    private BanQinInventoryEntity getToQuaInvModel(BanQinWmQcDetail wmQcDetailModel, String lotOp, Double qtyOp) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setAction(ActionCode.QC);
        toinvm.setLotNum(lotOp);
        toinvm.setLocCode(wmQcDetailModel.getQuaLoc());
        toinvm.setTraceId(wmQcDetailModel.getQuaTraceId());
        toinvm.setSkuCode(wmQcDetailModel.getSkuCode());
        toinvm.setOwnerCode(wmQcDetailModel.getOwnerCode());
        toinvm.setOrderNo(wmQcDetailModel.getQcNo());
        toinvm.setLineNo(wmQcDetailModel.getLineNo());
        toinvm.setPackCode(wmQcDetailModel.getPackCode());
        toinvm.setQtyUom(qtyOp);
        toinvm.setUom(WmsConstants.UOM_EA);
        toinvm.setQtyEaOp(qtyOp);
        toinvm.setOrgId(wmQcDetailModel.getOrgId());
        return toinvm;
    }

    /**
     * SET目标转移信息（不良品）
     * @param wmQcDetailModel
     * @param lotOp
     * @param qtyOp
     * @return
     */
    private BanQinInventoryEntity getToUnquaInvModel(BanQinWmQcDetail wmQcDetailModel, String lotOp, Double qtyOp) {
        BanQinInventoryEntity toinvm = new BanQinInventoryEntity();
        toinvm.setAction(ActionCode.QC);
        toinvm.setLotNum(lotOp);
        toinvm.setLocCode(wmQcDetailModel.getUnquaLoc());
        toinvm.setTraceId(wmQcDetailModel.getUnquaTraceId());
        toinvm.setSkuCode(wmQcDetailModel.getSkuCode());
        toinvm.setOwnerCode(wmQcDetailModel.getOwnerCode());
        toinvm.setOrderNo(wmQcDetailModel.getQcNo());
        toinvm.setLineNo(wmQcDetailModel.getLineNo());
        toinvm.setPackCode(wmQcDetailModel.getPackCode());
        toinvm.setQtyUom(qtyOp);
        toinvm.setUom(WmsConstants.UOM_EA);
        toinvm.setQtyEaOp(qtyOp);
        toinvm.setOrgId(wmQcDetailModel.getOrgId());
        return toinvm;
    }

    /**
     * 描述： 生成批次号
     *
     * @param wmAsnDetailReceiveModel
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public String getLotNum(BanQinWmAsnDetailReceive wmAsnDetailReceiveModel) {
        // 生成批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(wmAsnDetailReceiveModel.getOwnerCode());
        wmInvLotAttModel.setSkuCode(wmAsnDetailReceiveModel.getSkuCode());
        wmInvLotAttModel.setLotAtt01(wmAsnDetailReceiveModel.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(wmAsnDetailReceiveModel.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(wmAsnDetailReceiveModel.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(wmAsnDetailReceiveModel.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(wmAsnDetailReceiveModel.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(wmAsnDetailReceiveModel.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(wmAsnDetailReceiveModel.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(wmAsnDetailReceiveModel.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(wmAsnDetailReceiveModel.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(wmAsnDetailReceiveModel.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(wmAsnDetailReceiveModel.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(wmAsnDetailReceiveModel.getLotAtt12());
        wmInvLotAttModel.setOrgId(wmAsnDetailReceiveModel.getOrgId());
        return this.banQinInventoryService.createInvLotNum(wmInvLotAttModel);
    }

//****************************************取消QC***************************************************************************************//

    /**
     * 批量取消质检确认
     * @param qcNo
     * @param lineNos
     * @param orgId
     * @return
     */
    public ResultMessage batchCancelQcConfirm(String qcNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 校验：订单关闭，不能操作
        BanQinWmQcEntity qcEntity = banQinWmQcHeaderService.findEntityByQcNo(qcNo, orgId);
        if (WmsCodeMaster.ASN_CLOSE.getCode().equals(qcEntity.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage("订单已关闭，不能操作");
            return msg;
        }
        String qcPhase = qcEntity.getQcPhase();
        // 收货前质检，未完成质检，存在质检明细对应的收货明细已收货，不能取消。
        // 收货后质检，未完成质检，已生成上架任务，不能操作
        ResultMessage errorMsg = banQinWmQcSkuService.checkQcSkuForCancelQc(qcNo, lineNos, new String[]{WmsCodeMaster.QC_FULL_QC.getCode()}, qcPhase, orgId);
        Object[] updateLineNos = (Object[]) errorMsg.getData();
        if (updateLineNos.length > 0) {
            for (Object lineNo : updateLineNos) {
                try {
                    this.cancelQcConfirm(qcPhase, qcNo, lineNo.toString(), orgId);
                } catch (WarehouseException e) {
                    msg.addMessage("行号" + lineNo.toString() + e.getMessage());
                }
            }
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(qcPhase)) {
                msg.addMessage("行号" + "\n" + errorMsg.getMessage() + "未质检确认，已关闭，相关质检明细已生成上架任务，不能操作");
            }
            if (WmsCodeMaster.QC_PHASE_B_RCV.getCode().equals(qcPhase)) {
                msg.addMessage("行号" + "\n" + errorMsg.getMessage() + "未质检确认，已关闭，或相关收货明细已收货，不能操作");
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 取消质检确认
     * @param qcPhase
     * @param qcNo
     * @param lineNo
     * @param orgId
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage cancelQcConfirm(String qcPhase, String qcNo, String lineNo, String orgId) {
        ResultMessage msg = new ResultMessage();

        BanQinWmQcSku querySku = banQinWmQcSkuService.findByQcNoAndLineNo(qcNo, lineNo, orgId);
        if (WmsCodeMaster.QC_CLOSE.getCode().equals(querySku.getStatus())) {
            throw new WarehouseException(qcNo + "已关闭，不能操作");
        }

        // 升级为全检，未更新收货明细，实质未质检，故关闭状态，不能取消质检
        // 升级为全检处理：质检明细及商品明细为关闭的还原为创建，删除新建的质检行
        List<BanQinWmQcDetail> detailList = banQinWmQcDetailService.findByQcNoAndQcLineNo(qcNo, lineNo, orgId);
        for (BanQinWmQcDetail wmQcDetailModel : detailList) {
            try {
                cancelDetailQcConfirm(qcPhase, wmQcDetailModel);
            } catch (WarehouseException e) {
                throw new WarehouseException("质检明细行号" + wmQcDetailModel.getLineNo() + e.getMessage());
            }
        }

        // 更新质检单状态及ASN的质检状态
        querySku.setQcActSuggest(null);
        querySku.setQcSuggest(null);
        querySku.setQtyQcQuaEa(null);
        querySku.setQtyQcUnquaEa(null);
        querySku.setQtyQuaEa(null);
        querySku.setQtyUnquaEa(null);
        querySku.setPctQua(null);
        querySku.setStatus(WmsCodeMaster.QC_NEW.getCode());
        banQinInboundOperationService.updateQcConfirmStatus(querySku);
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 质检明细的取消质检
     *
     * @param qcPhase
     * @param wmQcDetailModel
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage cancelDetailQcConfirm(String qcPhase, BanQinWmQcDetail wmQcDetailModel) {
        ResultMessage msg = new ResultMessage();
        String qcRcvId = wmQcDetailModel.getQcRcvId();// 质检收货明细号
        String qcQuaRcvId = wmQcDetailModel.getQcQuaRcvId();// 质检良品收货明细号
        String qcUnquaRcvId = wmQcDetailModel.getQcUnquaRcvId();// 质检不良品收货明细号
        Double qtyQua = wmQcDetailModel.getQtyQuaEa() == null ? 0D : wmQcDetailModel.getQtyQuaEa();// 合格数
        Double qtyUnQua = wmQcDetailModel.getQtyUnquaEa() == null ? 0D : wmQcDetailModel.getQtyUnquaEa();// 不合格数
        Double qtyAvailQcEa = wmQcDetailModel.getQtyAvailQcEa();
        // 更新ASN收货明细
        String quaLot = wmQcDetailModel.getQuaLotNum();// 合格的批次号
        String unQuaLot = wmQcDetailModel.getUnquaLotNum();// 不合格的批次号
        String fmLot = wmQcDetailModel.getLotNum();// 源批次号
        String quaLoc = wmQcDetailModel.getQuaLoc();// 良品库位
        String unquaLoc = wmQcDetailModel.getUnquaLoc();// 不良品库位
        String fmLoc = wmQcDetailModel.getLocCode();// 源库位
        String fmTraceId = wmQcDetailModel.getTraceId();// 源跟踪号
        String quaTraceId = wmQcDetailModel.getQuaTraceId();// 良品跟踪号
        String unquaTraceId = wmQcDetailModel.getUnquaTraceId();// 不良品跟踪号
        String orgId = wmQcDetailModel.getOrgId();

        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
        // 收货后质检，更新ASN收货明细的质检状态，执行质检转移
        if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(qcPhase)) {
            wmAsnDetailReceiveModel = getAndCheckRcvByQcRcvId(qcRcvId, orgId);
            if (qtyQua > 0D) {
                // 良品转待检
                banQinInventoryService.updateInventory(getInvModel(wmQcDetailModel, quaLoc, quaTraceId, quaLot, qtyQua), getInvModel(wmQcDetailModel, fmLoc, fmTraceId, fmLot, qtyQua));
            }
            if (qtyUnQua > 0D) {
                // 不良品转待检
                banQinInventoryService.updateInventory(getInvModel(wmQcDetailModel, unquaLoc, unquaTraceId, unQuaLot, qtyUnQua), getInvModel(wmQcDetailModel, fmLoc, fmTraceId, fmLot, qtyUnQua));
            }
        }

        // 收货前质检
        if (WmsCodeMaster.QC_PHASE_B_RCV.getCode().equals(qcPhase)) {
            // 如果质检号，都不为空，则说明质检确认进行了拆分。
            if (StringUtils.isNotEmpty(qcQuaRcvId) && StringUtils.isNotEmpty(qcUnquaRcvId)) {
                // 良品行 是原来ASN未拆分的收货明细
                wmAsnDetailReceiveModel = getAndCheckRcvByQcRcvId(qcQuaRcvId, orgId);
                // 删除拆分的不良品行
                banQinWmAsnDetailReceiveService.delete(getAndCheckRcvByQcRcvId(qcUnquaRcvId, orgId));
            } else if (StringUtils.isNotEmpty(qcQuaRcvId)) {
                wmAsnDetailReceiveModel = getAndCheckRcvByQcRcvId(qcQuaRcvId, orgId);
            } else if (StringUtils.isNotEmpty(qcUnquaRcvId)) {
                wmAsnDetailReceiveModel = getAndCheckRcvByQcRcvId(qcUnquaRcvId, orgId);
            } else {
                wmAsnDetailReceiveModel = getAndCheckRcvByQcRcvId(qcRcvId, orgId);
            }
            // 还原到质检前的收货明细信息
            wmAsnDetailReceiveModel.setQtyPlanEa(qtyAvailQcEa);
            wmAsnDetailReceiveModel.setToId(fmTraceId);
            wmAsnDetailReceiveModel.setToLoc(fmLoc);
            wmAsnDetailReceiveModel.setLotNum(null);
            wmAsnDetailReceiveModel.setLotAtt04(wmQcDetailModel.getLotAtt04());
        }
        wmAsnDetailReceiveModel.setQcStatus(WmsCodeMaster.QC_NEW.getCode());
        wmAsnDetailReceiveModel.setQcRcvId(qcRcvId);
        banQinWmAsnDetailReceiveService.save(wmAsnDetailReceiveModel);

        // 更新质检明细状态
        wmQcDetailModel.setQcTime(null);
        wmQcDetailModel.setStatus(WmsCodeMaster.QC_NEW.getCode());
        wmQcDetailModel.setQtyQuaEa(0d);
        wmQcDetailModel.setQtyUnquaEa(0d);
        wmQcDetailModel.setQuaLotNum(null);
        wmQcDetailModel.setUnquaLotNum(null);
        wmQcDetailModel.setUnquaTraceId(fmTraceId);
        wmQcDetailModel.setQuaTraceId(fmTraceId);
        wmQcDetailModel.setQuaLoc(fmLoc);
        wmQcDetailModel.setUnquaLoc(fmLoc);
        wmQcDetailModel.setQtyQcUnquaEa(0d);
        wmQcDetailModel.setQtyQcQuaEa(0d);
        wmQcDetailModel.setQcQuaRcvId(null);
        wmQcDetailModel.setQcUnquaRcvId(null);
        banQinWmQcDetailService.save(wmQcDetailModel);

        msg.setData(banQinWmQcDetailService.get(wmQcDetailModel.getId()));
        msg.setSuccess(true);
        return msg;

    }

    /**
     * 根据质检收货明细号，查询并校验收货明细
     * @param qcRcvId
     * @param orgId
     * @return
     */
    private BanQinWmAsnDetailReceive getAndCheckRcvByQcRcvId(String qcRcvId, String orgId) {
        List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveModels = banQinWmAsnDetailReceiveService.findByQcRcvId(qcRcvId, orgId);
        if (wmAsnDetailReceiveModels.size() == 0) {
            throw new WarehouseException("查询不到质检明细号" + qcRcvId + "对应的收货明细行");// 查询不到质检明细号{0}对应的收货明细行
        }
        if (wmAsnDetailReceiveModels.size() > 1) {
            throw new WarehouseException("收货明细可能已做了码盘，生成越库任务操作，不能取消质检确认");// "收货明细可能已做了码盘，生成越库任务操作，不能取消质检确认"
        }
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = wmAsnDetailReceiveModels.get(0);
        if (StringUtils.isNotEmpty(wmAsnDetailReceiveModel.getPlanPaLoc())) {
            throw new WarehouseException("已安排库位，不能操作");
        }
        return wmAsnDetailReceiveModel;
    }

    /**
     * 收货前，取消质检时，可能改变收货行号。查询不到原来收货明细，故删除收货明细，重新生成
     * @param wmAsnDetailModel
     * @param wmQcDetailModel
     * @return
     */
    private BanQinWmAsnDetailReceive qcDetailConvertToAsnReceive(BanQinWmAsnDetail wmAsnDetailModel, BanQinWmQcDetail wmQcDetailModel) {
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
        BeanUtils.copyProperties(wmAsnDetailModel, wmAsnDetailReceiveModel);
        wmAsnDetailReceiveModel.setId(null);
        wmAsnDetailReceiveModel.setRecVer(0);
        wmAsnDetailReceiveModel.setAsnLineNo(wmQcDetailModel.getOrderLineNo());
        // wmAsnDetailReceiveModel.setLineNo(wmQcDetailModel.getOpLineNo());
        wmAsnDetailReceiveModel.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        wmAsnDetailReceiveModel.setUom(wmQcDetailModel.getUom());
        wmAsnDetailReceiveModel.setToLoc(wmQcDetailModel.getLocCode());
        wmAsnDetailReceiveModel.setPlanId(wmQcDetailModel.getTraceId());
        wmAsnDetailReceiveModel.setToId(wmQcDetailModel.getTraceId());
        wmAsnDetailReceiveModel.setQtyPlanEa(wmQcDetailModel.getQtyAvailQcEa());
        wmAsnDetailReceiveModel.setQcStatus(WmsCodeMaster.QC_NEW.getCode());
        wmAsnDetailReceiveModel.setIsEdiSend(WmsConstants.NO);
        wmAsnDetailReceiveModel.setEdiSendTime(null);
        wmAsnDetailReceiveModel.setQtyRcvEa(0D);
        return wmAsnDetailReceiveModel;
    }

    /**
     * 设置转移参数
     * @param wmQcDetailModel
     * @param locOp
     * @param traceOp
     * @param lotOp
     * @param qtyOp
     * @return
     */
    private BanQinInventoryEntity getInvModel(BanQinWmQcDetail wmQcDetailModel, String locOp, String traceOp, String lotOp, Double qtyOp) {
        BanQinInventoryEntity fminvm = new BanQinInventoryEntity();
        fminvm.setAction(ActionCode.QC);
        fminvm.setLotNum(lotOp);
        fminvm.setLocCode(locOp);
        fminvm.setTraceId(traceOp);
        fminvm.setSkuCode(wmQcDetailModel.getSkuCode());
        fminvm.setOwnerCode(wmQcDetailModel.getOwnerCode());
        fminvm.setOrderNo(wmQcDetailModel.getQcNo());
        fminvm.setLineNo(wmQcDetailModel.getLineNo());
        fminvm.setPackCode(wmQcDetailModel.getPackCode());
        fminvm.setQtyUom(qtyOp);
        fminvm.setUom(WmsConstants.UOM_EA);
        fminvm.setQtyEaOp(qtyOp);
        return fminvm;
    }

    /**
     *  升级全检后的取消质检
     * @param qcPhase
     * @param wmQcSkuModel
     * @param wmAsnDetailModel
     */
    @Transactional
    public void revertFullQc(String qcPhase, BanQinWmQcSku wmQcSkuModel, BanQinWmAsnDetail wmAsnDetailModel) {
        // 删除全检的质检明细行及商品行
        BanQinWmQcDetail example = new BanQinWmQcDetail();
        example.setOrderNo(wmQcSkuModel.getOrderNo());
        example.setOrderLineNo(wmQcSkuModel.getOrderLineNo());
        example.setStatus(WmsCodeMaster.QC_NEW.getCode());
        example.setOrgId(wmQcSkuModel.getOrgId());
        List<BanQinWmQcDetail> wmQcDetailList = banQinWmQcDetailService.findList(example);
        if (CollectionUtil.isNotEmpty(wmQcDetailList)) {
            for (BanQinWmQcDetail wmQcDetail : wmQcDetailList) {
                banQinWmQcDetailService.delete(wmQcDetail);
            }
        }

        BanQinWmQcSku qcSku = new BanQinWmQcSku();
        qcSku.setOrderNo(wmQcSkuModel.getOrderNo());
        qcSku.setOrderLineNo(wmQcSkuModel.getOrderLineNo());
        qcSku.setStatus(WmsCodeMaster.QC_NEW.getCode());
        qcSku.setOrgId(wmQcSkuModel.getOrgId());
        List<BanQinWmQcSku> wmQcSkuList = banQinWmQcSkuService.findList(qcSku);
        if (CollectionUtil.isNotEmpty(wmQcSkuList)) {
            for (BanQinWmQcSku wmQcSku : wmQcSkuList) {
                banQinWmQcSkuService.delete(wmQcSku);
            }
        }

        // 收货前，先删除原来商品明细，再生成收货明细
        if (WmsCodeMaster.QC_PHASE_B_RCV.getCode().equals(qcPhase)) {
            banQinWmAsnDetailReceiveService.removeByAsnNoAndLineNo(wmQcSkuModel.getOrderNo(), new String[]{wmQcSkuModel.getOrderLineNo()}, wmQcSkuModel.getOrgId());
        }

        // 质检明细及商品明细为关闭的还原为创建
        List<BanQinWmQcDetail> detailList = banQinWmQcDetailService.findByQcNoAndQcLineNo(wmQcSkuModel.getQcNo(), wmQcSkuModel.getLineNo(), wmQcSkuModel.getOrgId());
        for (BanQinWmQcDetail wmQcDetailModel : detailList) {
            if (WmsCodeMaster.QC_PHASE_B_RCV.getCode().equals(qcPhase)) {
                // 生成新的收货明细
                BanQinWmAsnDetailReceive newRcv = qcDetailConvertToAsnReceive(wmAsnDetailModel, wmQcDetailModel);
                banQinInboundOperationService.saveAsnDetailReceive(newRcv);
            }
            // 执行质检明细还原
            wmQcDetailModel.setQcTime(null);
            wmQcDetailModel.setStatus(WmsCodeMaster.QC_NEW.getCode());
            wmQcDetailModel.setQtyQuaEa(null);
            wmQcDetailModel.setQtyUnquaEa(null);
            wmQcDetailModel.setQuaLotNum(null);
            wmQcDetailModel.setUnquaLotNum(null);
            wmQcDetailModel.setUnquaTraceId(wmQcDetailModel.getTraceId());
            wmQcDetailModel.setQuaTraceId(wmQcDetailModel.getTraceId());
            wmQcDetailModel.setQuaLoc(wmQcDetailModel.getLocCode());
            wmQcDetailModel.setUnquaLoc(wmQcDetailModel.getLocCode());
            wmQcDetailModel.setQtyQcUnquaEa(null);
            wmQcDetailModel.setQtyQcQuaEa(null);
            banQinWmQcDetailService.save(wmQcDetailModel);
        }
        // 还原商品明细还原
        wmQcSkuModel.setQcActSuggest(null);
        wmQcSkuModel.setQcSuggest(null);
        wmQcSkuModel.setQtyQcQuaEa(null);
        wmQcSkuModel.setQtyQcUnquaEa(null);
        wmQcSkuModel.setQtyQuaEa(null);
        wmQcSkuModel.setQtyUnquaEa(null);
        wmQcSkuModel.setPctQua(null);
        wmQcSkuModel.setStatus(WmsCodeMaster.QC_NEW.getCode());
        banQinWmQcSkuService.save(wmQcSkuModel);
    }

    /**
     * 删除质检单
     * @param qcIds
     * @return
     */
    @Transactional
    public ResultMessage removeQc(String[] qcIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = banQinWmQcHeaderService.checkQcStatus(qcIds, new String[] {WmsCodeMaster.QC_NEW.getCode()},
                new String[] { WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode() }, WmsConstants.NO);
        String[] updateQcIds = (String[]) errorMsg.getData();
        if (updateQcIds.length > 0) {
            for (String qcId : updateQcIds) {
                BanQinWmQcHeader wmQcHeader = banQinWmQcHeaderService.get(qcId);
                List<BanQinWmQcSku> wmQcSkuList = banQinWmQcSkuService.findByQcNo(wmQcHeader.getQcNo(), wmQcHeader.getOrgId());
                for (BanQinWmQcSku banQinWmQcSku : wmQcSkuList) {
                    this.removeQcSku(banQinWmQcSku.getQcNo(), banQinWmQcSku.getLineNo(), banQinWmQcSku.getOrgId());
                }
                banQinWmQcHeaderService.removeByQcId(new String[] {qcId});
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        } else {
            msg.setSuccess(false);
            msg.addMessage("非创建状态、已审核、已质检的订单，不能操作");
        }
        return msg;
    }

    /**
     * 删除QC明细及质检明细
     * @param qcNo
     * @param lineNos
     * @return
     */
    @Transactional
    public ResultMessage removeQcSkuEntity(String qcNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = banQinWmQcSkuService.checkQcSkuStatus(qcNo, new String[] { WmsCodeMaster.QC_NEW.getCode() }, lineNos, new String[] { WmsCodeMaster.QC_NEW.getCode() }, new String[] {
                WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode() }, WmsConstants.NO, orgId);
        String[] updateLineNos = (String[]) errorMsg.getData();
        if (updateLineNos.length > 0) {
            for (String lineNo : updateLineNos) {
                this.removeQcSku(qcNo, lineNo, orgId);
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        } else {
            msg.addMessage("非创建状态、已审核、已质检的订单，不能操作");
            msg.setSuccess(false);
        }
        return msg;
    }

    /**
     * 删除一条质检商品明细
     * @param qcNo
     * @param qcLineNo
     * @param orgId
     */
    @Transactional
    public void removeQcSku(String qcNo, String qcLineNo, String orgId) {
        // 执行质检转移
        List<BanQinWmQcDetailEntity> wmQcDetailEntityList = banQinWmQcDetailService.findEntityByQcNoAndQcLineNo(qcNo, qcLineNo, orgId);
        for (BanQinWmQcDetailEntity wmQcDetailEntity : wmQcDetailEntityList) {
            List<BanQinWmAsnDetailReceive> list = banQinWmAsnDetailReceiveService.findByQcRcvId(wmQcDetailEntity.getQcRcvId(), orgId);
            for (BanQinWmAsnDetailReceive model : list) {
                model.setQcRcvId(null);
                model.setQcStatus(null);
                banQinWmAsnDetailReceiveService.save(model);
            }
            // 更新ASN的商品明细及单头状态
            banQinInboundOperationService.updateAsnDetailQcStatus(wmQcDetailEntity.getOrderNo(), wmQcDetailEntity.getOrderLineNo(), orgId);
            banQinInboundOperationService.updateAsnQcStatus(wmQcDetailEntity.getOrderNo(), orgId);
        }
        // 执行删除
        banQinWmQcDetailService.removeByQcNoAndQcLineNo(qcNo, new String[]{qcLineNo}, orgId);
        banQinWmQcSkuService.removeByQcNoAndLineNo(qcNo, new String[]{qcLineNo}, orgId);
    }

    /**
     * 批量生成上架任务
     * @param wmQcSkuEntitys
     * @return
     */
    public ResultMessage batchCreateTaskPa(List<BanQinWmQcSkuEntity> wmQcSkuEntitys) {
        return banQinInboundPaOperationService.inboundBatchCreateTaskPaFmQc(wmQcSkuEntitys);
    }

    /**
     * 删除上架任务
     * @param list
     * @return
     */
    public ResultMessage batchRemoveTaskPa(List<BanQinWmTaskPaEntity> list) {
        return banQinInboundPaOperationService.inboundBatchRemoveTaskPaFmQc(list);
    }

    /**
     * 批量上架
     * @param list
     * @return
     */
    public ResultMessage batchPutaway(List<BanQinWmTaskPaEntity> list) {
        return banQinInboundPaOperationService.inboundBatchPutaway(list);
    }
}
