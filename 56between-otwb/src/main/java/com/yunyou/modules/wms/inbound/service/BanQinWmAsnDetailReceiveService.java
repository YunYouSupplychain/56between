package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockAsnDetailReceiveQueryEntity;
import com.yunyou.modules.wms.crossDock.entity.BanQinWmAsnDetailReceiveQueryEntity;
import com.yunyou.modules.wms.crossDock.service.BanQinCrossDockService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnReceiveByCdQuery;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmAsnDetailReceiveMapper;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetailEntity;
import com.yunyou.modules.wms.qc.service.BanQinWmQcDetailService;
import com.yunyou.modules.wms.report.entity.TraceLabel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货明细Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAsnDetailReceiveService extends CrudService<BanQinWmAsnDetailReceiveMapper, BanQinWmAsnDetailReceive> {
    @Autowired
    @Lazy
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    @Lazy
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmQcDetailService banQinWmQcDetailService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    @Lazy
    private BanQinCrossDockService banQinCrossDockService;

    public Page<BanQinWmAsnDetailReceiveEntity> findPage(Page page, BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceive) {
        dataRuleFilter(banQinWmAsnDetailReceive);
        banQinWmAsnDetailReceive.setPage(page);
        List<BanQinWmAsnDetailReceiveEntity> list = mapper.findPage(banQinWmAsnDetailReceive);
        page.setList(list);
        for (BanQinWmAsnDetailReceiveEntity entity : list) {
            entity.setQtyPlanUom(entity.getQtyPlanEa() / entity.getUomQty());
            entity.setQtyRcvUom(entity.getQtyRcvEa() / entity.getUomQty());
            entity.setCurrentQtyRcvUom(entity.getQtyPlanUom() - entity.getQtyRcvUom());
            entity.setCurrentQtyRcvEa(entity.getQtyPlanEa() - entity.getQtyRcvEa());
        }
        return page;
    }

    public Page<BanQinWmAsnDetailReceiveEntity> findGrid(Page page, BanQinWmAsnDetailReceiveEntity banQinWmAsnDetailReceive) {
        dataRuleFilter(banQinWmAsnDetailReceive);
        banQinWmAsnDetailReceive.setPage(page);
        List<BanQinWmAsnDetailReceiveEntity> list = mapper.findGrid(banQinWmAsnDetailReceive);
        page.setList(list);
        return page;
    }

    public List<BanQinWmAsnDetailReceiveEntity> findEntityList(BanQinWmAsnDetailReceiveEntity entity) {
        return mapper.findEntityList(entity);
    }

    public List<BanQinWmAsnDetailReceiveEntity> findEntityByIds(List<String> ids) {
        return mapper.findEntityByIds(ids);
    }

    /**
     * 描述： 根据ASN单号获取最新行号
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public String getNewLineNo(String asnNo, String orgId) {
        Integer lineNo = mapper.getMaxLineNo(asnNo, orgId);
        if (lineNo == null) {
            lineNo = 0;
        }
        return String.format("%04d", lineNo + 1);
    }

    /**
     * 描述：根据ASN单号+行号获取收货明细
     *
     * @param asnNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public BanQinWmAsnDetailReceive getByAsnNoAndLineNo(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnDetailReceive wmAsnDetailReceive = new BanQinWmAsnDetailReceive();
        wmAsnDetailReceive.setAsnNo(asnNo);
        wmAsnDetailReceive.setLineNo(lineNo);
        wmAsnDetailReceive.setOrgId(orgId);
        List<BanQinWmAsnDetailReceive> list = this.findList(wmAsnDetailReceive);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 通过订单号和收货跟踪号查询收货明细 traceId为*时，只通过订单号查询
     *
     * @param asnNo
     * @param traceId
     * @param status
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetailReceive> getByAsnNoAndTraceId(String asnNo, String traceId, String status, String orgId) {
        BanQinWmAsnDetailReceive example = new BanQinWmAsnDetailReceive();
        example.setAsnNo(asnNo);
        if (!WmsConstants.TRACE_ID.equals(traceId)) {
            example.setToId(traceId);
        }
        example.setStatus(status);
        example.setOrgId(orgId);
        return this.findList(example);
    }

    /**
     * 描述： 获取ASN收货明细存在码盘跟踪号相同的记录
     *
     * @param asnNo
     * @param asnLineNos
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetailReceive> getRepeatPlanId(String asnNo, String[] asnLineNos, String orgId) {
        return mapper.getRepeatPlanId(asnNo, asnLineNos, orgId);
    }

    /**
     * 描述： 根据质检收货明细号查询
     *
     * @param qcRcvId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetailReceive> findByQcRcvId(String qcRcvId, String orgId) {
        BanQinWmAsnDetailReceive example = new BanQinWmAsnDetailReceive();
        example.setQcRcvId(qcRcvId);
        example.setOrgId(orgId);
        return this.findList(example);
    }

    /**
     * 描述： 根据ASN单号获取收货明细Entity
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmAsnDetailReceiveEntity> getEntityByAsnNo(String asnNo, String orgId) {
        BanQinWmAsnDetailReceiveEntity entity = new BanQinWmAsnDetailReceiveEntity();
        entity.setAsnNo(asnNo);
        entity.setOrgId(orgId);
        return mapper.findEntityList(entity);
    }

    /**
     * 描述： 根据ASN单号 + ASN明细行号行号获取收货明细Entity
     *
     * @param asnNo
     * @param asnLineNo ASN明细行号
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmAsnDetailReceiveEntity> getEntityByAsnNoAndAsnLineNo(String asnNo, String asnLineNo, String orgId) {
        BanQinWmAsnDetailReceiveEntity entity = new BanQinWmAsnDetailReceiveEntity();
        entity.setAsnNo(asnNo);
        entity.setAsnLineNo(asnLineNo);
        entity.setOrgId(orgId);
        return mapper.findEntityList(entity);
    }

    /**
     * 描述： 根据ASN单号 + 行号获取收货明细Entity
     *
     * @param asnNo
     * @param lineNo 行号
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public BanQinWmAsnDetailReceiveEntity getEntityByAsnNoAndLineNo(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnDetailReceiveEntity entity = new BanQinWmAsnDetailReceiveEntity();
        entity.setAsnNo(asnNo);
        entity.setLineNo(lineNo);
        entity.setOrgId(orgId);
        List<BanQinWmAsnDetailReceiveEntity> result = this.findEntityList(entity);
        return CollectionUtil.isNotEmpty(result) ? result.get(0) : new BanQinWmAsnDetailReceiveEntity();
    }

    /**
     * 描述： 获取可以进行正常码盘的收货明细
     *
     * @param asnNo
     * @param asnLineNo
     * @param status
     * @param isCrossDock
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetailReceive> findCanPalletizeDetail(String asnNo, String asnLineNo, String status, String isCrossDock, String orgId) {
        return mapper.findCanPalletizeDetail(asnNo, asnLineNo, status, isCrossDock, orgId);
    }

    /**
     * 描述： 获取可以进行正常取消码盘的收货明细
     *
     * @param asnNo
     * @param asnLineNo
     * @param status
     * @param isCrossDock
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetailReceive> findCancelPalletizeDetail(String asnNo, String asnLineNo, String status, String isCrossDock, String orgId) {
        return mapper.findCancelPalletizeDetail(asnNo, asnLineNo, status, isCrossDock, orgId);
    }

    /**
     * 描述： 获取可以进行取消收货的收货明细
     *
     * @param asnNo
     * @param asnLineNo
     * @param planId
     * @param status
     * @param cdRcvId
     * @param qcRcvId
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    private List<BanQinWmAsnDetailReceive> findCancelReceiveDetail(String asnNo, String asnLineNo, String planId, String status, String cdRcvId, String qcRcvId, String orgId) {
        return mapper.findCancelReceiveDetail(asnNo, asnLineNo, planId, status, cdRcvId, qcRcvId, orgId);
    }

    /**
     * 描述： 查询需要质检且状态为空的收货明细
     *
     * @param asnNo
     * @param asnLineNo
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmAsnDetailReceive> findUnQcDetail(String asnNo, String asnLineNo, String orgId) {
        return mapper.findUnQcDetail(asnNo, asnLineNo, orgId);
    }

    /**
     * 描述： 更新状态
     *
     * @param asnNo
     * @param asnLineNo
     * @param status
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void updateStatus(String asnNo, String asnLineNo, String status, String orgId) {
        mapper.updateStatus(asnNo, asnLineNo, status, orgId);
    }

    /**
     * 描述： 更新物流单号
     *
     * @param asnNo
     * @param logisticNo
     * @param orgId
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public void updateLogisticNo(String asnNo, String logisticNo, String orgId) {
        mapper.updateLogisticNo(asnNo, logisticNo, orgId);
    }

    /**
     * 描述： 更新计划上架库位
     *
     * @param
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void updatePlanPaLoc(String asnNo, String lineNo, String orgId) {
        mapper.updatePlanPaLoc(asnNo, lineNo, orgId);
    }

    /**
     * 描述： 更新收货明细上架ID为空
     * 删除上架任务时，如果paId已不存在在任务表，但在收货明细里有存在，则更新
     *
     * @param paId  上架ID()
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public void updatePaIdNull(String paId, String orgId) {
        mapper.updatePaIdNull(paId, orgId);
    }

    /**
     * 描述： 根据ASN单号删除收货明细
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeByAsnNo(String asnNo, String orgId) {
        mapper.removeByAsnNo(asnNo, orgId);
    }

    /**
     * 描述： 根据ASN单号+行号删除
     *
     * @param asnNo
     * @param lineNos ASN行号
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeByAsnNoAndLineNo(String asnNo, String[] lineNos, String orgId) {
        mapper.removeByAsnNoAndLineNo(asnNo, lineNos, orgId);
    }


    /**
     * 描述： 部分收货时，拆分收货明细
     *
     * @param wmAsnDetailReceiveEntity
     * @param currentQtyRcvEa
     * @param qtyPlanEa
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void splitAsnDetailReceive(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity, Double currentQtyRcvEa, Double qtyPlanEa) throws WarehouseException {
        // 如果收货数<预收数，部分收货时，拆明细
        if (currentQtyRcvEa != 0D && currentQtyRcvEa < qtyPlanEa) {
            // ASN明细
            BanQinWmAsnDetail wmAsnDetailModel = banQinWmAsnDetailService.getByAsnNoAndLineNo(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getOrgId());
            // 码盘标识
            String isPalletize = wmAsnDetailModel.getIsPalletize();
            // 赋值
            BanQinWmAsnDetailReceive newReceiveModel = new BanQinWmAsnDetailReceive();
            BeanUtils.copyProperties(wmAsnDetailReceiveEntity, newReceiveModel);
            newReceiveModel.setId(null);
            newReceiveModel.setLotNum("");// 未收货明细，批次号为空
            // 新的收货明细，如果ASN明细已码盘，traceID为系统生成的新traceID，反之，traceID为ASN明细的traceID；
            if (WmsConstants.YES.equals(isPalletize)) {
                newReceiveModel.setToId(noService.getDocumentNo(GenNoType.WM_TRACE_ID.name()));
            } else {
                newReceiveModel.setToId(wmAsnDetailReceiveEntity.getPlanId());
            }
            newReceiveModel.setQtyPlanEa(qtyPlanEa - currentQtyRcvEa);
            newReceiveModel.setQtyRcvEa(0D);
            // 如果是收货前计算库位（两步收货），新的收货明细，上架库位指定规则取【收货时计算库位】，计划上架库位置空
            if (WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(wmAsnDetailReceiveEntity.getReserveCode())) {
                newReceiveModel.setReserveCode(WmsCodeMaster.RESERVE_RCV.getCode());
                newReceiveModel.setPlanPaLoc(null);
            }
            // 新增保存
            banQinInboundOperationService.saveAsnDetailReceive(newReceiveModel);
        }
    }

    /**
     * Description : 取消收货明细时，查询来自同一个ASN明细行，且planID相同的创建收货明细，删除并将为收货数合并到取消行。
     * 如果存在计划上架库位的明细，不合并。 收货前质检，质检确认后，拆分收货明细生成质检收货明细号。取消收货时，相同质检收货明细号，合并。
     * 如果是质检后的取消收货，跟踪号，收货库位，批次4，取质检结果。 生成越库任务，标记CR_RCV_ID
     *
     * @param wmAsnDetailReceiveEntity
     * @Author: Ramona.Wang
     * @Create Date: 2014-6-27
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity cancelRcvUnionRcvDetail(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) {
        // ASN SKU明细
        BanQinWmAsnDetail wmAsnDetailModel = banQinWmAsnDetailService.getByAsnNoAndLineNo(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getOrgId());
        // 取消时的收货数=计划数。
        Double qtyRcvEa = wmAsnDetailReceiveEntity.getQtyPlanEa();

        // 查询出同一个planId的创建状态的收货明细
        // 2015-2-15.2 Morice 添加条件 查询出同一个planId和同一越库匹配行号的创建状态的收货明细
        // 2015-3-24.3 Ramona 添加条件 查询出同一个planId和同一越库匹配行号和同一质检收货明细号的创建状态的收货明细
        List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveList;
        BanQinWmAsnDetailReceive example = new BanQinWmAsnDetailReceive();
        if (StringUtils.isEmpty(wmAsnDetailReceiveEntity.getCdRcvId()) && StringUtils.isEmpty(wmAsnDetailReceiveEntity.getQcRcvId())) {
            // 正常取消收货合并记录
            wmAsnDetailReceiveList = this.findCancelReceiveDetail(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getPlanId(), WmsCodeMaster.ASN_NEW.getCode(),
                    null, null, wmAsnDetailReceiveEntity.getOrgId());
        } else if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getCdRcvId()) && StringUtils.isEmpty(wmAsnDetailReceiveEntity.getQcRcvId())) {
            // 越库匹配取消收货合并记录
            wmAsnDetailReceiveList = this.findCancelReceiveDetail(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getPlanId(), WmsCodeMaster.ASN_NEW.getCode(),
                    wmAsnDetailReceiveEntity.getCdRcvId(), null, wmAsnDetailReceiveEntity.getOrgId());
        } else if (StringUtils.isEmpty(wmAsnDetailReceiveEntity.getCdRcvId()) && StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getQcRcvId())) {
            // 质检确认 取消收货合并记录
            wmAsnDetailReceiveList = this.findCancelReceiveDetail(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getPlanId(), WmsCodeMaster.ASN_NEW.getCode(),
                    null, wmAsnDetailReceiveEntity.getQcRcvId(), wmAsnDetailReceiveEntity.getOrgId());
        } else {
            wmAsnDetailReceiveList = this.findCancelReceiveDetail(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getPlanId(), WmsCodeMaster.ASN_NEW.getCode(),
                    wmAsnDetailReceiveEntity.getCdRcvId(), wmAsnDetailReceiveEntity.getQcRcvId(), wmAsnDetailReceiveEntity.getOrgId());
        }
        for (BanQinWmAsnDetailReceive wmAsnDetailReceiveModel : wmAsnDetailReceiveList) {
            qtyRcvEa = qtyRcvEa + wmAsnDetailReceiveModel.getQtyPlanEa();
        }
        // 取消收货的明细，恢复到原来的值
        // 如果是质检后，取消收货的，还原到质检确认时的信息。20150325
        wmAsnDetailReceiveEntity.setToId(wmAsnDetailReceiveEntity.getPlanId());
        wmAsnDetailReceiveEntity.setToLoc(wmAsnDetailModel.getPlanToLoc());
        wmAsnDetailReceiveEntity.setLotAtt04(wmAsnDetailModel.getLotAtt04());
        if (StringUtils.isNotEmpty(wmAsnDetailReceiveEntity.getQcRcvId())) {
            List<BanQinWmQcDetailEntity> list = banQinWmQcDetailService.getByQcRcvId(wmAsnDetailReceiveEntity.getQcRcvId(), wmAsnDetailReceiveEntity.getOrgId());
            BanQinWmQcDetailEntity entity = list.get(0);
            // 如果是码盘，质检确认后的取消则不更新该字段
            if (WmsConstants.NO.equals(wmAsnDetailModel.getIsPalletize())) {
                wmAsnDetailReceiveEntity.setToId(entity.getTraceId());
            }
            wmAsnDetailReceiveEntity.setToLoc(entity.getLocCode());
            wmAsnDetailReceiveEntity.setLotAtt04(entity.getQcType());
            // 查询出的质检信息，如果是Q，批次4取原来ASN的。
            if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(entity.getQcType())) {
                wmAsnDetailReceiveEntity.setLotAtt04(wmAsnDetailModel.getLotAtt04());
            }
        }
        wmAsnDetailReceiveEntity.setPaRule(wmAsnDetailModel.getPaRule());
        wmAsnDetailReceiveEntity.setReserveCode(wmAsnDetailModel.getReserveCode());
        wmAsnDetailReceiveEntity.setPrice(wmAsnDetailModel.getPrice());
        wmAsnDetailReceiveEntity.setQtyPlanEa(qtyRcvEa);
        wmAsnDetailReceiveEntity.setQtyRcvEa(0D);
        wmAsnDetailReceiveEntity.setLotNum(null);
        // 清空计划上架库位
        wmAsnDetailReceiveEntity.setPlanPaLoc(null);
        wmAsnDetailReceiveEntity.setLotAtt01(wmAsnDetailModel.getLotAtt01());
        wmAsnDetailReceiveEntity.setLotAtt02(wmAsnDetailModel.getLotAtt02());
        wmAsnDetailReceiveEntity.setLotAtt03(wmAsnDetailModel.getLotAtt03());
        wmAsnDetailReceiveEntity.setLotAtt05(wmAsnDetailModel.getLotAtt05());
        wmAsnDetailReceiveEntity.setLotAtt06(wmAsnDetailModel.getLotAtt06());
        wmAsnDetailReceiveEntity.setLotAtt07(wmAsnDetailModel.getLotAtt07());
        wmAsnDetailReceiveEntity.setLotAtt08(wmAsnDetailModel.getLotAtt08());
        wmAsnDetailReceiveEntity.setLotAtt09(wmAsnDetailModel.getLotAtt09());
        wmAsnDetailReceiveEntity.setLotAtt10(wmAsnDetailModel.getLotAtt10());
        wmAsnDetailReceiveEntity.setLotAtt11(wmAsnDetailModel.getLotAtt11());
        wmAsnDetailReceiveEntity.setLotAtt12(wmAsnDetailModel.getLotAtt12());
        // 清空PAID
        wmAsnDetailReceiveEntity.setPaId(null);
        wmAsnDetailReceiveEntity.setRcvTime(null);
        wmAsnDetailReceiveEntity.setEdiSendTime(null);
        wmAsnDetailReceiveEntity.setIsEdiSend(WmsConstants.NO);
        // 更新取消行，删除创建行
        for (BanQinWmAsnDetailReceive wmAsnDetailReceive : wmAsnDetailReceiveList) {
            this.delete(wmAsnDetailReceive);
        }
        return banQinInboundOperationService.saveAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);
    }

    /**
     * 获取越库操作记录，按商品
     */
    public List<BanQinWmAsnDetailReceiveEntity> getEntityByCdAndSku(BanQinWmAsnReceiveByCdQuery condition) {
        List<BanQinWmAsnDetailReceiveEntity> entitys = new ArrayList<>();
        List<BanQinWmAsnReceiveByCdQuery> items = mapper.getEntityByCdAndSku(condition);
        if (items.size() > 0) {
            for (BanQinWmAsnReceiveByCdQuery item : items) {
                BanQinWmAsnDetailReceiveEntity entity = new BanQinWmAsnDetailReceiveEntity();
                BeanUtils.copyProperties(item, entity);
                entitys.add(entity);
            }
        }
        return entitys;
    }

    /**
     * 获取越库操作记录，按明细
     */
    public List<BanQinWmAsnDetailReceiveEntity> getEntityByCdAndLineNo(List<String> asnAndLineNos, String status, String isCd, String[] cdType, String orgId) {
        List<BanQinWmAsnDetailReceiveEntity> entitys = new ArrayList<>();
        BanQinWmAsnReceiveByCdQuery condition = new BanQinWmAsnReceiveByCdQuery();
        condition.setAsnAndLineNos(String.join(",", asnAndLineNos));
        condition.setIsCd(isCd);// 未越库标记
        condition.setStatus(status);// 状态
        condition.setCdTypes(cdType);
        condition.setOrgId(orgId);
        List<BanQinWmAsnReceiveByCdQuery> items = mapper.getEntityByCdAndSku(condition);
        if (items.size() > 0) {
            for (BanQinWmAsnReceiveByCdQuery item : items) {
                BanQinWmAsnDetailReceiveEntity entity = new BanQinWmAsnDetailReceiveEntity();
                BeanUtils.copyProperties(item, entity);
                entitys.add(entity);
            }
        }
        return entitys;
    }

    /**
     * 获取越库操作记录
     */
    public List<BanQinWmAsnDetailReceiveEntity> getEntityByCrossDock(BanQinWmAsnDetailReceiveQueryEntity entity) {
        List<BanQinWmAsnDetailReceiveEntity> entitys = new ArrayList<>();
        BanQinGetCrossDockAsnDetailReceiveQueryEntity condition = new BanQinGetCrossDockAsnDetailReceiveQueryEntity();
        // 如果是分拨越库,创建状态，按货主、商品查询
        if (StringUtils.isEmpty(entity.getAsnNo()) && StringUtils.isEmpty(entity.getRcvLineNo())) {
            condition.setSkuCode(entity.getSkuCode());
            condition.setOwnerCode(entity.getOwnerCode());
            condition.setCdType(WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
            condition.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        } else {
            // 如果是直接越库(创建、完全收货)或者分拨越库(完成收货)，按入库单号、收货明细行号查询
            condition.setAsnNo(entity.getAsnNo());
            condition.setLineNo(entity.getRcvLineNo());
        }
        condition.setIsCd("Y");
        condition.setOrgId(entity.getOrgId());
        List<BanQinGetCrossDockAsnDetailReceiveQueryEntity> items = banQinCrossDockService.getCrossDockAsnDetailReceiveQueryList(condition);
        if (items.size() > 0) {
            for (BanQinGetCrossDockAsnDetailReceiveQueryEntity item : items) {
                BanQinWmAsnDetailReceiveEntity receiveEntity = new BanQinWmAsnDetailReceiveEntity();
                BeanUtils.copyProperties(item, receiveEntity);
                entitys.add(receiveEntity);
            }
        }
        return entitys;
    }

    /**
     * 描述： 订单未冻结，行状态“完全收货”并且未生成凭证号的收货明细，生成凭证号
     *
     * @param asnId
     * @param lineNos ASN明细行号
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void createVoucherNoByLineNo(String asnId, String[] lineNos) {
        mapper.createVoucherNoByAsn(asnId, lineNos, noService.getDocumentNo(GenNoType.WM_VOUCHER_NO.name()));
    }

    /**
     * 描述： 订单未冻结，行状态“完全收货”并且已生成凭证号的收货明细，取消凭证号
     *
     * @param asnId
     * @param lineNos ASN明细行号
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void cancelVoucherNoByLineNo(String asnId, String[] lineNos) {
        mapper.cancelVoucherNoByAsn(asnId, lineNos);
    }

    /**
     * 描述： 批量勾选订单，一个ASN单生成一个凭证号
     *
     * @param asnIds
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void createVoucherNoByAsn(String[] asnIds) {
        int length = asnIds.length;
        // 批量生成凭证号
        List<String> voucherNos = noService.getDocumentNo(GenNoType.WM_VOUCHER_NO.name(), length);
        for (int i = 0; i < length; i++) {
            mapper.createVoucherNoByAsn(asnIds[i], null, voucherNos.get(i));
        }
    }

    /**
     * 描述： 批量勾选订单，取消凭证号
     *
     * @param asnIds
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void cancelVoucherNoByAsn(String[] asnIds) {
        for (String asnId : asnIds) {
            mapper.cancelVoucherNoByAsn(asnId, null);
        }
    }

    public List<String> getLineNoByCheckStatus(String asnNo, String[] lineNos, String[] lineStatus, String isArrangeLoc, String isVoucher, String orgId) {
        return mapper.getLineNoByCheckStatus(asnNo, lineNos, lineStatus, isArrangeLoc, isVoucher, orgId);
    }

    public List<BanQinWmAsnDetailReceive> rfPaCheckCreatePATaskByToIDQuery(String toId, String orgId) {
        return mapper.rfPaCheckCreatePATaskByToIDQuery(toId, orgId);
    }

    public List<TraceLabel> getTraceLabel(List<String> asnDetailReceiveIds) {
        return mapper.getTraceLabel(asnDetailReceiveIds);
    }
}