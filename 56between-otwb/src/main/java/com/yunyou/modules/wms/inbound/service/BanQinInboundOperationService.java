package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.crossDock.service.BanQinCrossDockCreateTaskService;
import com.yunyou.modules.wms.inbound.entity.*;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.service.BanQinInventoryService;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotService;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetailEntity;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSku;
import com.yunyou.modules.wms.qc.service.BanQinWmQcDetailService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcHeaderService;
import com.yunyou.modules.wms.qc.service.BanQinWmQcSkuService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：入库业务操作统一Service
 *
 * @auther: Jianhua on 2019/1/26
 */
@Service
public class BanQinInboundOperationService {
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;
    @Autowired
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private BanQinWmAsnDetailReceivesService banQinWmAsnDetailReceivesService;
    @Autowired
    private BanQinWmAsnSerialService banQinWmAsnSerialService;
    @Autowired
    private BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    private BanQinWmQcDetailService banQinWmQcDetailService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmsCommonService banQinWmsCommonService;
    @Autowired
    private BanQinCrossDockCreateTaskService banQinCrossDockCreateTaskService;
    @Autowired
    private BanQinInventoryService banQinInventoryService;
    @Autowired
    @Lazy
    private BanQinInboundPaOperationService banQinInboundPaOperationService;
    @Autowired
    private BanQinWmInvLotService banQinWmInvLotService;
    @Autowired
    private BanQinWmPoHeaderService banQinWmPoHeaderService;
    @Autowired
    private BanQinWmQcSkuService banQinWmQcSkuService;
    @Autowired
    private BanQinWmQcHeaderService banQinWmQcHeaderService;
    @Autowired
    private BanQinWmTaskPaService banQinWmTaskPaService;

    /**
     * PO单，生成一个ASN单
     *
     * @param ownerCode
     * @param supplierCode
     * @param wmPoDetailEntitys
     * @param orgId
     * @return
     */
    @Transactional
    public ResultMessage inboundCreateAsn(String ownerCode, String supplierCode, List<BanQinWmPoDetailEntity> wmPoDetailEntitys, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 校验是否可以生成ASN单
        List<String> nos = wmPoDetailEntitys.stream().map(BanQinWmPoDetailEntity::getPoNo).distinct().collect(Collectors.toList());
        List<BanQinWmPoDetailEntity> poDetailList = wmPoDetailEntitys.stream().filter(s -> s.getCurrentQtyAsnEa() > 0d).collect(Collectors.toList());
        if (poDetailList.size() == 0) {
            msg.setSuccess(false);
            msg.addMessage("无ASN单生成");
            return msg;
        }
        // ASN单头
        BanQinWmAsnHeader wmAsnHeaderModel = new BanQinWmAsnHeader();
        // 如果是来自一个采购单，则带采购单的值到ASN单。
        if (nos.size() == 1) {
            BanQinWmPoHeader poHeader = banQinWmPoHeaderService.findByPoNo(nos.get(0), orgId);
            BeanUtils.copyProperties(poHeader, wmAsnHeaderModel);
            wmAsnHeaderModel.setId(null);
            wmAsnHeaderModel.setIsNewRecord(true);
        } else {
            wmAsnHeaderModel.setOwnerCode(ownerCode);
            wmAsnHeaderModel.setSupplierCode(supplierCode);
        }
        wmAsnHeaderModel.setAuditOp(null);
        wmAsnHeaderModel.setAuditTime(null);
        wmAsnHeaderModel.setEdiSendTime(null);
        wmAsnHeaderModel.setIsEdiSend(WmsConstants.EDI_FLAG_00);
        wmAsnHeaderModel.setAsnType(WmsCodeMaster.ASN_PI.getCode());
        wmAsnHeaderModel.setOrderTime(new Date());
        msg = this.saveAsnHeader(wmAsnHeaderModel);
        String asnNo = null;
        if (msg.isSuccess()) {
            wmAsnHeaderModel = (BanQinWmAsnHeader) msg.getData();
            asnNo = wmAsnHeaderModel.getAsnNo();
            String asnId = wmAsnHeaderModel.getId();
            for (BanQinWmPoDetailEntity wmPoDetailEntity : poDetailList) {
                // ASN SKU明细
                BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
                BeanUtils.copyProperties(wmPoDetailEntity, wmAsnDetailModel);
                wmAsnDetailModel.setId(null);
                wmAsnDetailModel.setIsNewRecord(true);
                wmAsnDetailModel.setAsnNo(asnNo);
                wmAsnDetailModel.setPoLineNo(wmPoDetailEntity.getLineNo());
                wmAsnDetailModel.setQtyAsnEa(wmPoDetailEntity.getCurrentQtyAsnEa());
                wmAsnDetailModel.setQtyRcvEa(0D);
                wmAsnDetailModel.setLogisticLineNo(null);
                wmAsnDetailModel.setLogisticNo(null);
                wmAsnDetailModel.setHeadId(asnId);
                try {
                    wmAsnDetailModel = this.saveAsnDetailFromPo(wmAsnDetailModel);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                    return msg;
                }

                // ASN 收货明细
                BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
                BeanUtils.copyProperties(wmAsnDetailModel, wmAsnDetailReceiveModel);
                wmAsnDetailReceiveModel.setId(IdGen.uuid());
                wmAsnDetailReceiveModel.setLogisticLineNo(null);
                wmAsnDetailReceiveModel.setLogisticNo(null);
                wmAsnDetailReceiveModel.setAsnLineNo(wmAsnDetailModel.getLineNo());
                wmAsnDetailReceiveModel.setAsnNo(asnNo);
                wmAsnDetailReceiveModel.setPoLineNo(wmPoDetailEntity.getLineNo());
                wmAsnDetailReceiveModel.setQtyPlanEa(wmPoDetailEntity.getCurrentQtyAsnEa());
                wmAsnDetailReceiveModel.setQtyRcvEa(0D);
                wmAsnDetailReceiveModel.setToLoc(wmAsnDetailModel.getPlanToLoc());
                this.saveAsnDetailReceive(wmAsnDetailReceiveModel);

                // 更新PO明细ASN数
                wmPoDetailEntity.setQtyAsnEa(wmPoDetailEntity.getQtyAsnEa() + wmPoDetailEntity.getCurrentQtyAsnEa());
                BanQinWmPoDetail wmPoDetailModel = new BanQinWmPoDetail();
                BeanUtils.copyProperties(wmPoDetailEntity, wmPoDetailModel);
                this.banQinWmPoDetailService.save(wmPoDetailModel);
            }

        }
        msg.setSuccess(true);
        msg.addMessage("操作成功，生成的ASN单:" + asnNo);
        return msg;
    }

    /**
     * 描述： 保存ASN单
     *
     * @param wmAsnHeader 入库单头信息
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public ResultMessage saveAsnHeader(BanQinWmAsnHeader wmAsnHeader) {
        ResultMessage msg = new ResultMessage();
        // ASN参数：ASN单是否需要做审核（Y：需要审核；N：不用审核）
        String isAudit = SysControlParamsUtils.getValue(ControlParamCode.ASN_AUDIT.getCode(), wmAsnHeader.getOrgId());
        // 新增时，默认状态为创建，生成新的编号
        if (StringUtils.isEmpty(wmAsnHeader.getId())) {
            wmAsnHeader.setId(IdGen.uuid());
            wmAsnHeader.setIsNewRecord(true);
            wmAsnHeader.setAsnNo(noService.getDocumentNo(GenNoType.WM_ASN_NO.name()));
            wmAsnHeader.setStatus(WmsCodeMaster.ASN_NEW.getCode());
            wmAsnHeader.setHoldStatus(WmsCodeMaster.ODHL_NO_HOLD.getCode());
            wmAsnHeader.setAuditStatus(WmsConstants.YES.equals(isAudit) ? WmsCodeMaster.AUDIT_NEW.getCode() : WmsCodeMaster.AUDIT_NOT.getCode());
            wmAsnHeader.setIsAppointDock(WmsConstants.NO);
            wmAsnHeader.setAuditOp(null);
            wmAsnHeader.setAuditTime(null);
            wmAsnHeader.setIsEdiSend(WmsConstants.EDI_FLAG_00);
            wmAsnHeader.setEdiSendTime(null);
            if (StringUtils.isBlank(wmAsnHeader.getDef10())) {
                wmAsnHeader.setDef10("SWMS");// 业务订单类型：SWMS-系统WMS业务订单
            }
        } else {
            // 如果修改了物流单号，更新子表的物流单号
            banQinWmAsnDetailService.updateLogisticNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getLogisticNo(), wmAsnHeader.getOrgId());
            banQinWmAsnDetailReceiveService.updateLogisticNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getLogisticNo(), wmAsnHeader.getOrgId());
        }
        banQinWmAsnHeaderService.save(wmAsnHeader);

        msg.setSuccess(true);
        msg.setData(wmAsnHeader);
        return msg;
    }

    /**
     * 描述： 保存ASN明细：新增ASN明细时，同时新增收货明细
     *
     * @param wmAsnDetailEntity
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage saveAsnDetailEntity(BanQinWmAsnDetailEntity wmAsnDetailEntity) {
        ResultMessage msg = new ResultMessage();
        // ASN单头
        BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.getByAsnNo(wmAsnDetailEntity.getAsnNo(), wmAsnDetailEntity.getOrgId());
        if (StringUtils.isNotEmpty(wmAsnHeader.getStatus()) && !WmsCodeMaster.ASN_NEW.getCode().equals(wmAsnHeader.getStatus())) {
            throw new WarehouseException(wmAsnDetailEntity.getAsnNo() + "不是创建状态，不能操作");
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAsnHeader.getAuditStatus())) {
            throw new WarehouseException(wmAsnDetailEntity.getAsnNo() + "已审核，不能操作");
        }
        if (WmsCodeMaster.ODHL_HOLD.getCode().equals(wmAsnHeader.getHoldStatus())) {
            throw new WarehouseException(wmAsnDetailEntity.getAsnNo() + "订单冻结，不能操作");
        }
        BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
        BeanUtils.copyProperties(wmAsnDetailEntity, wmAsnDetailModel);
        try {
            wmAsnDetailModel = this.saveAsnDetail(wmAsnDetailModel);
        } catch (WarehouseException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 赋值给收货明细
        if (StringUtils.isEmpty(wmAsnDetailEntity.getId())) {
            BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
            BeanUtils.copyProperties(wmAsnDetailModel, wmAsnDetailReceiveModel);
            wmAsnDetailReceiveModel.setId(IdGen.uuid());
            wmAsnDetailReceiveModel.setRecVer(0);
            wmAsnDetailReceiveModel.setAsnLineNo(wmAsnDetailModel.getLineNo());
            wmAsnDetailReceiveModel.setQtyPlanEa(wmAsnDetailModel.getQtyAsnEa());
            wmAsnDetailReceiveModel.setQtyRcvEa(0D);
            wmAsnDetailReceiveModel.setPlanId(wmAsnDetailModel.getTraceId());
            wmAsnDetailReceiveModel.setToId(wmAsnDetailModel.getTraceId());
            wmAsnDetailReceiveModel.setToLoc(wmAsnDetailModel.getPlanToLoc());
            this.saveAsnDetailReceive(wmAsnDetailReceiveModel);
        } else {
            List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities = banQinWmAsnDetailReceiveService.getEntityByAsnNoAndAsnLineNo(wmAsnDetailModel.getAsnNo(), wmAsnDetailModel.getLineNo(), wmAsnDetailModel.getOrgId());
            for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : wmAsnDetailReceiveEntities) {
                String receiveId = wmAsnDetailReceiveEntity.getId();
                int recVer = wmAsnDetailReceiveEntity.getRecVer();
                BeanUtils.copyProperties(wmAsnDetailModel, wmAsnDetailReceiveEntity);
                wmAsnDetailReceiveEntity.setId(receiveId);
                wmAsnDetailReceiveEntity.setRecVer(recVer);
                wmAsnDetailReceiveEntity.setQtyPlanEa(wmAsnDetailModel.getQtyAsnEa());
                wmAsnDetailReceiveEntity.setQtyRcvEa(0D);
                wmAsnDetailReceiveEntity.setPlanId(wmAsnDetailModel.getTraceId());
                wmAsnDetailReceiveEntity.setToId(wmAsnDetailModel.getTraceId());
                wmAsnDetailReceiveEntity.setToLoc(wmAsnDetailModel.getPlanToLoc());
                BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
                BeanUtils.copyProperties(wmAsnDetailReceiveEntity, wmAsnDetailReceiveModel);
                this.saveAsnDetailReceive(wmAsnDetailReceiveModel);
            }
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }

    /**
     * 描述： 保存ASN明细
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public BanQinWmAsnDetail saveAsnDetail(BanQinWmAsnDetail wmAsnDetailModel) throws WarehouseException {
        // 如果是从采购单生成的ASN，更新预收数，是要同步到PO单。
        if (StringUtils.isNotBlank(wmAsnDetailModel.getId()) && StringUtils.isNotEmpty(wmAsnDetailModel.getPoNo()) && StringUtils.isNotEmpty(wmAsnDetailModel.getPoLineNo())) {
            BanQinWmAsnDetail query = banQinWmAsnDetailService.getByAsnNoAndLineNo(wmAsnDetailModel.getAsnNo(), wmAsnDetailModel.getLineNo(), wmAsnDetailModel.getOrgId());
            double qtyAsnEa = query.getQtyAsnEa() == null ? 0D : query.getQtyAsnEa();
            double currentQtyAsnEa = wmAsnDetailModel.getQtyAsnEa() == null ? 0D : wmAsnDetailModel.getQtyAsnEa();
            if (qtyAsnEa != currentQtyAsnEa) {
                BanQinWmPoDetail wmPoDetailModel = banQinWmPoDetailService.findByPoNoAndLineNo(wmAsnDetailModel.getPoNo(), wmAsnDetailModel.getPoLineNo(), wmAsnDetailModel.getOrgId());
                // 订货数
                double expectedQtyEaPo = wmPoDetailModel.getQtyPoEa();
                double expectedQtyEaAsn = wmPoDetailModel.getQtyAsnEa();
                if (expectedQtyEaPo < (expectedQtyEaAsn - qtyAsnEa + currentQtyAsnEa)) {
                    throw new WarehouseException("预收数不能大于采购单" + wmAsnDetailModel.getPoNo() + "的采购数");
                } else {
                    wmPoDetailModel.setQtyAsnEa(expectedQtyEaAsn - qtyAsnEa + currentQtyAsnEa);
                    banQinWmPoDetailService.save(wmPoDetailModel);
                }
            }
        }
        return saveAsnDetailFromPo(wmAsnDetailModel);
    }

    /**
     * 描述： 保存从采购单生成的ASN明细
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public BanQinWmAsnDetail saveAsnDetailFromPo(BanQinWmAsnDetail wmAsnDetailModel) throws WarehouseException {
        BanQinCdWhSku skuModel = banQinCdWhSkuService.getByOwnerAndSkuCode(wmAsnDetailModel.getOwnerCode(), wmAsnDetailModel.getSkuCode(), wmAsnDetailModel.getOrgId());
        if (null == skuModel) {
            throw new WarehouseException("货主" + wmAsnDetailModel.getOwnerCode() + "的商品" + wmAsnDetailModel.getSkuCode() + "信息不存在或错误");// 货主{0}的商品{1}信息不存在或错误
        }
        if (StringUtils.isEmpty(wmAsnDetailModel.getId())) {
            // 默认库位
            if (StringUtils.isEmpty(wmAsnDetailModel.getPlanToLoc())) {
                String ASN_DEF_LOC = "STAGE";
                wmAsnDetailModel.setPlanToLoc(ASN_DEF_LOC);
            }
            // 默认上架规则
            if (StringUtils.isEmpty(wmAsnDetailModel.getPaRule())) {
                wmAsnDetailModel.setPaRule(skuModel.getPaRule());
            }
            // 默认库位指定规则
            if (StringUtils.isEmpty(wmAsnDetailModel.getReserveCode())) {
                wmAsnDetailModel.setReserveCode(skuModel.getReserveCode());
            }
            // 默认质检相关
            if (StringUtils.isEmpty(wmAsnDetailModel.getIsQc())) {
                wmAsnDetailModel.setIsQc(skuModel.getIsQc());
            }
            if (StringUtils.isEmpty(wmAsnDetailModel.getQcRule())) {
                wmAsnDetailModel.setQcRule(skuModel.getQcRule());
            }
            if (StringUtils.isEmpty(wmAsnDetailModel.getQcPhase())) {
                wmAsnDetailModel.setQcPhase(skuModel.getQcPhase());
            }
            if (StringUtils.isEmpty(wmAsnDetailModel.getItemGroupCode())) {
                wmAsnDetailModel.setItemGroupCode(skuModel.getItemGroupCode());
            }
        }
        // 初始化
        BanQinWmAsnDetail banQinWmAsnDetail = formAsnDetailModel(wmAsnDetailModel, skuModel);
        banQinWmAsnDetailService.save(banQinWmAsnDetail);
        return banQinWmAsnDetail;
    }

    /**
     * 初始化ASN明细信息
     *
     * @param wmAsnDetailModel
     * @throws WarehouseException
     */
    private BanQinWmAsnDetail formAsnDetailModel(BanQinWmAsnDetail wmAsnDetailModel, BanQinCdWhSku skuModel) throws WarehouseException {
        if (StringUtils.isEmpty(wmAsnDetailModel.getId())) {
            String lineNo = banQinWmAsnDetailService.getNewLineNo(wmAsnDetailModel.getAsnNo(), wmAsnDetailModel.getOrgId());
            wmAsnDetailModel.setId(IdGen.uuid());
            wmAsnDetailModel.setIsNewRecord(true);
            wmAsnDetailModel.setLineNo(lineNo);
            wmAsnDetailModel.setStatus(WmsCodeMaster.ASN_NEW.getCode());
            wmAsnDetailModel.setQtyRcvEa(0D);
            wmAsnDetailModel.setIsPalletize(WmsConstants.NO);
            wmAsnDetailModel.setIsEdiSend(WmsConstants.NO);
            wmAsnDetailModel.setEdiSendTime(null);
        }
        // 生产日期、失效日期的逻辑校验
        Date lotAtt01 = wmAsnDetailModel.getLotAtt01();// 生产日期
        Date lotAtt02 = wmAsnDetailModel.getLotAtt02();// 失效日期
        Date lotAtt03 = wmAsnDetailModel.getLotAtt03();// 入库日期
        Double shelfLife = skuModel.getShelfLife();// 保质期
        // 校验日期正确性
        this.banQinWmsCommonService.checkProductAndExpiryDate(lotAtt01, lotAtt02, lotAtt03);
        // 收货时是否根据生产日期、保质期自动计算失效日期
        if (null != lotAtt01 && null != shelfLife && shelfLife != 0D && lotAtt02 == null) {
            lotAtt02 = DateUtils.addDays(lotAtt01, shelfLife.intValue());
            wmAsnDetailModel.setLotAtt02(lotAtt02);
        }
        String isQc = StringUtils.isEmpty(wmAsnDetailModel.getIsQc()) ? WmsConstants.NO : wmAsnDetailModel.getIsQc();
        // 判断商品是否存在待检库存
        boolean isToQc = banQinWmInvLotService.checkIsToQc(wmAsnDetailModel.getOwnerCode(), wmAsnDetailModel.getSkuCode(), isQc, wmAsnDetailModel.getOrgId());
        if (isToQc) {
            if (WmsConstants.NO.equals(isQc)) {
                throw new WarehouseException(wmAsnDetailModel.getSkuCode() + "商品存在待检库存，需要质检");
            } else {
                throw new WarehouseException(wmAsnDetailModel.getSkuCode() + "商品不存在质检库存，不需要质检");
            }
        }
        // 校验质检阶段为收货前质检时，上架库位指定规则为：人工指定、收货时计算、上架时计算；
        // 质检阶段为收货后上架前质检时，上架库位指定规则为：人工指定、上架时计算
        if (WmsConstants.YES.equals(isQc)) {
            if (WmsCodeMaster.QC_PHASE_B_PA.getCode().equals(wmAsnDetailModel.getQcPhase())
                    && (!(WmsCodeMaster.RESERVE_MAN.getCode().equals(wmAsnDetailModel.getReserveCode()) || WmsCodeMaster.RESERVE_PA.getCode().equals(wmAsnDetailModel.getReserveCode())))) {
                throw new WarehouseException("收货后上架前质检，上架库位指定规则需为人工指定库位或上架时计算库位");
            } else if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(wmAsnDetailModel.getReserveCode()) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(wmAsnDetailModel.getReserveCode())) {
                throw new WarehouseException("收货前质检，上架库位指定规则不能为收货前计算库位");
            }
        }
        wmAsnDetailModel.setPrice(wmAsnDetailModel.getPrice() == null ? 0D : wmAsnDetailModel.getPrice());
        wmAsnDetailModel.setTraceId(StringUtils.isEmpty(wmAsnDetailModel.getTraceId()) ? WmsConstants.TRACE_ID : wmAsnDetailModel.getTraceId());
        return wmAsnDetailModel;
    }

    /**
     * 保存收货明细
     *
     * @param wmAsnDetailReceiveEntity
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity saveAsnDetailReceiveEntity(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) {
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
        BeanUtils.copyProperties(wmAsnDetailReceiveEntity, wmAsnDetailReceiveModel);
        wmAsnDetailReceiveModel = this.saveAsnDetailReceive(wmAsnDetailReceiveModel);
        return banQinWmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(wmAsnDetailReceiveModel.getAsnNo(), wmAsnDetailReceiveModel.getLineNo(), wmAsnDetailReceiveModel.getOrgId());
    }

    /**
     * 描述： 保存收货明细
     *
     * @param wmAsnDetailReceiveModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public BanQinWmAsnDetailReceive saveAsnDetailReceive(BanQinWmAsnDetailReceive wmAsnDetailReceiveModel) {
        if (StringUtils.isEmpty(wmAsnDetailReceiveModel.getId())) {
            String lineNo = banQinWmAsnDetailReceiveService.getNewLineNo(wmAsnDetailReceiveModel.getAsnNo(), wmAsnDetailReceiveModel.getOrgId());
            wmAsnDetailReceiveModel.setLineNo(lineNo);
            wmAsnDetailReceiveModel.setStatus(WmsCodeMaster.ASN_NEW.getCode());
            wmAsnDetailReceiveModel.setIsEdiSend(WmsConstants.NO);
            wmAsnDetailReceiveModel.setEdiSendTime(null);
            wmAsnDetailReceiveModel.setRcvTime(null);
            wmAsnDetailReceiveModel.setId(IdGen.uuid());
            wmAsnDetailReceiveModel.setIsNewRecord(true);
        }
        wmAsnDetailReceiveModel.setPrice(wmAsnDetailReceiveModel.getPrice() == null ? 0D : wmAsnDetailReceiveModel.getPrice());
        String traceId = StringUtils.isEmpty(wmAsnDetailReceiveModel.getToId()) ? WmsConstants.TRACE_ID : wmAsnDetailReceiveModel.getToId();
        wmAsnDetailReceiveModel.setToId(traceId);
        banQinWmAsnDetailReceiveService.save(wmAsnDetailReceiveModel);
        return wmAsnDetailReceiveModel;
    }

    /**
     * 校验传入的ASN的状态，返回符合状态的ASN，提示不符的ASN
     *
     * @param asnIds       ASN单ID
     * @param orderStatus
     * @param auditStatus
     * @param holdStatus
     * @param isPalletize  是否存在码盘
     * @param isVoucher    是否生成凭证号
     * @param isEdiSend    是否发送EDI
     * @param isCrossDock  是否越库
     * @param isArrangeLoc 是否已安排库位
     * @return
     */
    public ResultMessage checkAsnStatus(String[] asnIds, String[] orderStatus, String[] auditStatus, String[] holdStatus, String isPalletize, String isVoucher, String isEdiSend, String isCrossDock, String isArrangeLoc) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的ASN
        List<BanQinWmAsnHeader> banQinWmAsnHeaders = banQinWmAsnHeaderService.checkAsnStatusQuery(asnIds, orderStatus, auditStatus, holdStatus, isPalletize, isVoucher, isCrossDock, isArrangeLoc);
        Map<String, String> idNoMap = banQinWmAsnHeaders.stream().collect(Collectors.toMap(BanQinWmAsnHeader::getId, BanQinWmAsnHeader::getAsnNo, (k1, k2) -> k1));
        List<String> returnAsnIds = Lists.newArrayList(idNoMap.keySet());
        Object[] minusAsnIds = banQinWmsCommonService.minus(asnIds, returnAsnIds.toArray());
        msg.setMessage(getNotConvertMsg(minusAsnIds));
        msg.setData(returnAsnIds.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 校验传入的ASN明细的状态，返回符合状态的ASN明细，提示不符的ASN明细
     *
     * @param asnId
     * @param asnStatus
     * @param lineNos
     * @param lineStatus
     * @param auditStatus
     * @param holdStatus
     * @param isPalletize
     * @param isCrossDock
     * @param isArrangeLoc
     * @author Jianhua on 2019/1/26
     */
    public ResultMessage checkAsnDetailStatus(String asnId, String[] asnStatus, String[] lineNos, String[] lineStatus, String[] auditStatus, String[] holdStatus, String isPalletize, String isCrossDock, String isArrangeLoc) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的行号
        List<String> returnLineNos = banQinWmAsnDetailService.checkAsnDetailStatusQuery(asnId, asnStatus, lineNos, lineStatus, auditStatus, holdStatus, isPalletize, isCrossDock, isArrangeLoc);
        // 不符合条件的单号，提示
        Object[] minusLineNos = banQinWmsCommonService.minus(lineNos, returnLineNos.toArray());
        msg.addMessage(Arrays.stream(minusLineNos).map(String::valueOf).collect(Collectors.joining("\n")));
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 校验ASN是否存在质检单
     *
     * @param asnIds
     * @author Jianhua on 2019/1/26
     */
    public ResultMessage checkAsnExistQc(String[] asnIds) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<String> returnAsnIds = banQinWmAsnHeaderService.findNotQCById(Arrays.asList(asnIds));
        // 不符合条件的单号，提示
        Object[] minusAsnIds = banQinWmsCommonService.minus(asnIds, returnAsnIds.toArray());
        msg.setMessage(getNotConvertMsg(minusAsnIds));
        msg.setData(returnAsnIds.stream().toArray(String[]::new));
        return msg;
    }

    private String getNotConvertMsg(Object[] minusAsnIds) {
        String msg = null;
        if (null != minusAsnIds && minusAsnIds.length > 0) {
            List<BanQinWmAsnHeader> byIds = banQinWmAsnHeaderService.getByIds(Arrays.stream(minusAsnIds).map(String::valueOf).collect(Collectors.toList()).toArray(new String[]{}));
            msg = String.join("\n", byIds.stream().map(BanQinWmAsnHeader::getAsnNo).collect(Collectors.toList()));
        }
        return msg;
    }

    /**
     * 描述： 校验ASN明细是否已生成质检单
     *
     * @param asnId
     * @param lineNos
     * @param qcStatus
     * @author Jianhua on 2019/1/26
     */
    public ResultMessage checkAsnDetailExistQC(String asnId, String[] lineNos, String qcStatus) {
        ResultMessage msg = new ResultMessage();
        if (null != lineNos && lineNos.length > 0) {
            // 返回符合条件的单号
            List<String> returnLineNos = banQinWmAsnDetailService.getCheckQcLine(asnId, lineNos, qcStatus);
            // 不符合条件的单号，提示
            Object[] minusLineNos = banQinWmsCommonService.minus(lineNos, returnLineNos.toArray());
            msg.addMessage(Arrays.stream(minusLineNos).map(String::valueOf).collect(Collectors.joining("\n")));
            msg.setData(returnLineNos.toArray(new String[]{}));
        } else {
            msg.setData(new String[]{});
        }
        return msg;
    }

    /**
     * 描述： 校验传入的收货明细的状态，返回符合状态的收货明细，提示不符的收货明细
     *
     * @param asnId
     * @param lineNos      收货明细行
     * @param lineStatus   状态
     * @param isArrangeLoc 是否已安排库位
     * @param isVoucher    是否已生成凭证号
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage checkAsnDetailReceive(String asnId, String[] lineNos, String[] lineStatus, String isArrangeLoc, String isVoucher) {
        ResultMessage msg = new ResultMessage();

        BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
        // 返回符合条件的单号
        List<String> returnLineNos = banQinWmAsnDetailReceiveService.getLineNoByCheckStatus(wmAsnHeader.getAsnNo(), lineNos, lineStatus, isArrangeLoc, isVoucher, wmAsnHeader.getOrgId());
        // 不符合条件的单号，提示
        Object[] minusLineNos = banQinWmsCommonService.minus(lineNos, returnLineNos.toArray());
        msg.addMessage(Arrays.stream(minusLineNos).map(String::valueOf).collect(Collectors.joining("\n")));
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 校验ASN单是否可以进行码盘，收货，取消收货
     *
     * @param asnId
     * @author Jianhua on 2019/1/26
     */
    public ResultMessage checkAsnIsOperateStatus(String asnId) {
        ResultMessage msg = new ResultMessage();
        // ASN单头
        BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.get(asnId);
        String holdStatus = wmAsnHeaderModel.getHoldStatus();// 冻结状态
        if (WmsCodeMaster.ODHL_HOLD.getCode().equals(holdStatus)) {
            msg.setSuccess(false);
            msg.addMessage("订单冻结，不能操作");// "订单冻结，不能操作"
            return msg;
        }
        String aduitStatus = wmAsnHeaderModel.getAuditStatus();// 审核状态
        if (WmsCodeMaster.AUDIT_NEW.getCode().equals(aduitStatus)) {
            msg.setSuccess(false);
            msg.addMessage("订单未审核，不能操作");// 订单未审核，不能操作
            return msg;
        }
        if (WmsCodeMaster.ASN_CANCEL.getCode().equals(wmAsnHeaderModel.getStatus())) {// 入库单状态为90或99时不能操作
            msg.setSuccess(false);
            msg.addMessage("订单已取消，不能操作");// 订单已取消，不能操作
            return msg;
        }

        if (WmsCodeMaster.ASN_CLOSE.getCode().equals(wmAsnHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage("订单已关闭，不能操作");// 订单已关闭，不能操作
            return msg;
        }
        msg.setSuccess(true);
        msg.setData(wmAsnHeaderModel);
        return msg;
    }

    /**
     * 描述： 校验ASN收货明细是否存在码盘跟踪号相同的记录(取消码盘)
     *
     * @param asnId
     * @param asnLineNos
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage checkRepeatPlanId(String asnId, String[] asnLineNos) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
        if (null != asnLineNos && asnLineNos.length > 0) {
            // 返回符合条件的ASN明细行号
            List<BanQinWmAsnDetailReceive> wmAsnDetailReceives = banQinWmAsnDetailReceiveService.getRepeatPlanId(wmAsnHeader.getAsnNo(), asnLineNos, wmAsnHeader.getOrgId());
            List<String> errorLineNos = wmAsnDetailReceives.stream().map(BanQinWmAsnDetailReceive::getAsnLineNo).distinct().collect(Collectors.toList());
            // 不符合条件的单号，提示
            Object[] minusLineNos = banQinWmsCommonService.minus(asnLineNos, errorLineNos.toArray());
            msg.addMessage(String.join("\n", errorLineNos));
            msg.setData(Arrays.stream(minusLineNos).map(String::valueOf).collect(Collectors.toList()).toArray(new String[]{}));
        } else {
            msg.setData(new String[]{});
        }
        return msg;
    }

    /**
     * 描述： 安排库位数据校验
     *
     * @param entity
     * @param isArrange Y:安排库位 N：取消安排
     * @author Jianhua on 2019/1/28
     */
    protected BanQinWmAsnDetailReceiveEntity checkArrangeLocBefore(BanQinWmAsnDetailReceiveEntity entity, String isArrange) throws WarehouseException {
        // 校验数据是否过期
        BanQinWmAsnDetailReceive querySku = banQinWmAsnDetailReceiveService.getByAsnNoAndLineNo(entity.getAsnNo(), entity.getLineNo(), entity.getOrgId());
        if (null == querySku) {
            throw new WarehouseException("不存在");// 不存在
        }
        // 收货明细状态的校验
        if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(querySku.getStatus())) {
            throw new WarehouseException("已收货，不能操作");
        }
        if (WmsCodeMaster.ASN_CANCEL.getCode().equals(querySku.getStatus())) {
            throw new WarehouseException("已取消，不能操作");
        }
        if (isArrange.equals(WmsConstants.YES)) {
            if (StringUtils.isNotEmpty(querySku.getPlanPaLoc())) {
                throw new WarehouseException("已安排库位，不能操作");
            }
            if (!(WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(entity.getReserveCode()) || WmsCodeMaster.RESERVE_B_RCV_TWO.getCode().equals(entity.getReserveCode()))) {
                throw new WarehouseException("上架库位指定规则需为收货前计算库位");
            }
            // 质检商品，批次04，不能为空
            if (WmsConstants.YES.equals(querySku.getIsQc())) {
                if (StringUtils.isEmpty(querySku.getLotAtt04())) {
                    throw new WarehouseException("商品需要质检，批次属性04不能为空");
                } else if (WmsCodeMaster.QC_ATT_TO_QC.getCode().equals(querySku.getLotAtt04())) {
                    throw new WarehouseException("批次属性04不能为待检");
                }
            }
        } else {
            if (StringUtils.isEmpty(querySku.getPlanPaLoc())) {
                throw new WarehouseException("未安排库位，不能操作");
            }
        }

        // 订单状态的校验
        ResultMessage errorMsg = this.checkAsnIsOperateStatus(entity.getHeadId());
        if (!errorMsg.isSuccess()) {
            throw new WarehouseException(errorMsg.getMessage());
        }
        BanQinWmAsnHeader wmAsnHeaderModel = (BanQinWmAsnHeader) errorMsg.getData();
        // 订单类型
        entity.setOrderType(wmAsnHeaderModel.getAsnType());
        return entity;
    }

    /**
     * 完全收货状态，并且不存在未完成的上架任务，是否自动关闭ASN   (待确认完全收货后是否自动关闭ASN)
     */
    public void checkRcvAutoCloseAsn(String asnNo, String orgId) {
        final String RCV_AUTO_CLOSE_ASN = SysControlParamsUtils.getValue(ControlParamCode.RCV_AUTO_CLOSE_ASN.getCode());
        if (WmsConstants.YES.equals(RCV_AUTO_CLOSE_ASN)) {
            BanQinWmAsnHeader wmAsnHeaderModel = banQinWmAsnHeaderService.getByAsnNo(asnNo, orgId);
            if (WmsCodeMaster.ASN_FULL_RECEIVING.getCode().equals(wmAsnHeaderModel.getStatus())) {
                try {
                    this.closeAsn(wmAsnHeaderModel);
                    /*generalCharge.generalChargeBoundImmOrder(wmAsnHeaderModel.getOwnerCode(), wmAsnHeaderModel.getAsnNo(), WmsCodeMaster.ORDER_ASN.getCode());*/
                } catch (WarehouseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 描述： 删除ASN
     *
     * @param asnId
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeAsnEntity(String asnId) {
        BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
        // 回填PO单的ASN数
        List<BanQinWmAsnDetail> wmAsnDetailList = banQinWmAsnDetailService.getByAsnNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getOrgId());
        for (BanQinWmAsnDetail wmAsnDetailModel : wmAsnDetailList) {
            banQinWmPoDetailService.backfillQtyAsn(wmAsnDetailModel);
        }
        // 删除收货明细
        banQinWmAsnDetailReceiveService.removeByAsnNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getOrgId());
        // 删除ASN明细
        for (BanQinWmAsnDetail wmAsnDetailModel : wmAsnDetailList) {
            banQinWmAsnDetailService.delete(wmAsnDetailModel);
        }
        // 删除序列号
        banQinWmAsnSerialService.removeByAsnNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getOrgId());
        // 删除单头
        banQinWmAsnHeaderService.removeByAsnNo(wmAsnHeader.getAsnNo(), wmAsnHeader.getOrgId());
    }

    /**
     * 描述： 删除ASN明细及收货明细
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeAsnDetailEntity(BanQinWmAsnDetail wmAsnDetailModel) {
        // 回填PO单的ASN数
        banQinWmPoDetailService.backfillQtyAsn(wmAsnDetailModel);
        // 删除收货明细
        banQinWmAsnDetailReceiveService.removeByAsnNoAndLineNo(wmAsnDetailModel.getAsnNo(), new String[]{wmAsnDetailModel.getLineNo()}, wmAsnDetailModel.getOrgId());
        // 删除ASN明细
        banQinWmAsnDetailService.delete(wmAsnDetailModel);
        // 删除收货箱明细
        banQinWmAsnDetailReceivesService.removeByReceiveId(Lists.newArrayList(wmAsnDetailModel.getId()));
    }

    /**
     * 描述： ASN审核：审核后不能修改订单
     *
     * @param asnIds ASN单ID
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public ResultMessage audit(String[] asnIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode()}, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()},
                null, null, null, null, null);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        if (updateAsnIds.length > 0) {
            banQinWmAsnHeaderService.updateAuditStatusById(Arrays.asList(updateAsnIds), WmsCodeMaster.AUDIT_CLOSE.getCode(), UserUtils.getUser().getName());
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage());
            msg.addMessage("非创建状态、已审核、已冻结的订单，不能操作");
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 采购成本分摊
     *
     * @param asnIds
     * @param strategy
     * @return
     */
    @Transactional
    public ResultMessage costAlloc(String[] asnIds, String strategy) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode()}, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()},
                null, null, null, null, null);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        if (updateAsnIds.length > 0) {
            List<BanQinWmAsnHeader> asnHeaderList = banQinWmAsnHeaderService.getByIds(updateAsnIds);
            if (CollectionUtil.isNotEmpty(asnHeaderList)) {
                // 采购成本分摊
                if ("2".equals(strategy)) {
                    for (BanQinWmAsnHeader asnHeader : asnHeaderList) {
                        if (null != asnHeader.getFreightAmount() && asnHeader.getFreightAmount() > 0) {
                            // 商品明细
                            List<BanQinWmAsnDetail> asnDetailList = banQinWmAsnDetailService.getByAsnNo(asnHeader.getAsnNo(), asnHeader.getOrgId());
                            double sumPrice = asnDetailList.stream().filter(s -> null != s.getPrice()).mapToDouble(s -> s.getPrice() * s.getQtyAsnEa()).sum();
                            for (BanQinWmAsnDetail asnDetail : asnDetailList) {
                                double cost = (null != asnDetail.getPrice() ? asnDetail.getPrice() : 0d) * asnDetail.getQtyAsnEa();
                                sumPrice = 0d == sumPrice ? 1 : sumPrice;
                                double detailCost = cost / sumPrice * asnHeader.getFreightAmount() / asnDetail.getQtyAsnEa() + (null != asnDetail.getPrice() ? asnDetail.getPrice() : 0d);
                                asnDetail.setLotAtt05(new DecimalFormat("0.00").format(detailCost));
                                banQinWmAsnDetailService.save(asnDetail);
                                // 收货明细
                                List<BanQinWmAsnDetailReceiveEntity> receiveEntityList = banQinWmAsnDetailReceiveService.getEntityByAsnNoAndAsnLineNo(asnDetail.getAsnNo(), asnDetail.getLineNo(), asnDetail.getOrgId());
                                for (BanQinWmAsnDetailReceiveEntity receiveEntity : receiveEntityList) {
                                    double costPrice = Double.valueOf(asnDetail.getLotAtt05()) * receiveEntity.getQtyPlanEa() / asnDetail.getQtyAsnEa();
                                    receiveEntity.setLotAtt05(new DecimalFormat("0.00").format(costPrice));
                                    banQinWmAsnDetailReceiveService.save(receiveEntity);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage());
            msg.addMessage("非创建状态、已审核、已冻结的订单，不能操作");
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 对ASN明细，进行按托拆分，生成码盘明细
     *
     * @param wmAsnDetailEntity
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage createPalletize(BanQinWmAsnDetailEntity wmAsnDetailEntity) throws WarehouseException {
        String asnNo = wmAsnDetailEntity.getAsnNo();
        String lineNo = wmAsnDetailEntity.getLineNo();
        BanQinWmAsnDetail wmAsnDetailModel = new BanQinWmAsnDetail();
        BeanUtils.copyProperties(wmAsnDetailEntity, wmAsnDetailModel);
        // 未码盘标识
        // 每个收货明细行码盘数
        Double detailQty;
        String traceId = "";
        String uom = "";
        Double plQty = wmAsnDetailEntity.getPlQty();
        // 越库码盘
        ResultMessage msg = banQinCrossDockCreateTaskService.createPalletizeByCd(wmAsnDetailEntity);
        if (!msg.isSuccess()) {
            return msg;
        }
        // 获取可以进行正常码盘的收货明细
        // 修改码盘，添加质检确认后码盘
        List<BanQinWmAsnDetailReceive> receiveList = banQinWmAsnDetailReceiveService.findCanPalletizeDetail(asnNo, lineNo, WmsCodeMaster.ASN_NEW.getCode(), WmsConstants.NO, wmAsnDetailEntity.getOrgId());
        // 1.包装未维护托盘数时，不拆盘，按原来收货明细码盘，更新跟踪号
        if (null == plQty || plQty == 0D) {
            // 更新原创建状态明细行
            for (BanQinWmAsnDetailReceive receive : receiveList) {
                traceId = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name());
                receive.setPlanId(traceId);
                receive.setToId(traceId);
                banQinWmAsnDetailReceiveService.save(receive);
            }
            msg.setSuccess(true);
            return msg;
        }
        // 删除码盘明细行
        for (BanQinWmAsnDetailReceive model : receiveList) {
            banQinWmAsnDetailReceiveService.delete(model);
        }
        // 循环码盘
        for (BanQinWmAsnDetailReceive receive : receiveList) {
            Double palletQtyEa = receive.getQtyPlanEa();
            // 托盘跟踪号数
            int num = (int) Math.ceil(palletQtyEa / plQty);// 向上取整
            // 批量生成跟踪号
            List<String> traceIds = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name(), num);
            for (int i = 0; i < num; i++) {
                if (palletQtyEa >= plQty) {
                    detailQty = plQty;// 收货明细数量 = 托盘数量
                    uom = WmsConstants.UOM_PL;
                } else {
                    detailQty = palletQtyEa;// 收货明细数量
                    uom = WmsConstants.UOM_EA;
                }
                palletQtyEa -= detailQty;
                traceId = traceIds.get(i);
                // 保存
                BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
                BeanUtils.copyProperties(receive, wmAsnDetailReceiveModel);
                wmAsnDetailReceiveModel.setId(null);
                wmAsnDetailReceiveModel.setRecVer(0);
                wmAsnDetailReceiveModel.setQtyPlanEa(detailQty);
                wmAsnDetailReceiveModel.setQtyRcvEa(0D);
                wmAsnDetailReceiveModel.setUom(uom);
                wmAsnDetailReceiveModel.setPlanId(traceId);
                wmAsnDetailReceiveModel.setToId(traceId);
                this.saveAsnDetailReceive(wmAsnDetailReceiveModel);
            }
        }

        // 标识为已码盘
        wmAsnDetailModel.setIsPalletize(WmsConstants.YES);
        banQinWmAsnDetailService.save(wmAsnDetailModel);

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 批量安排库位
     *
     * @param wmAsnDetailReceiveEntitys
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage inboundBatchArrangeLoc(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntitys) {
        ResultMessage msg = new ResultMessage();
        String asnId = wmAsnDetailReceiveEntitys.get(0).getHeadId();
        String qcStr = "";
        String cdStr = "";
        Map<String, List<BanQinWmAsnDetailReceiveEntity>> map = Maps.newHashMap();
        for (BanQinWmAsnDetailReceiveEntity entity : wmAsnDetailReceiveEntitys) {
            List<BanQinWmAsnDetailReceiveEntity> list = Lists.newArrayList();
            // 添加已越库匹配收货明细校验，不允许安排库位
            if (StringUtils.isNotEmpty(entity.getCdType())) {
                cdStr = cdStr + entity.getLineNo() + "\n";
            } else if (WmsConstants.YES.equals(entity.getIsQc()) && StringUtils.isEmpty(entity.getQcStatus())) {
                qcStr = qcStr + entity.getLineNo() + "\n";
            } else {
                // 分组
                String key = entity.getAsnLineNo();
                if (map.containsKey(key)) {
                    list = map.get(key);
                }
                list.add(entity);
                map.put(key, list);
            }
        }
        ResultMessage isQcMsg = this.checkAsnDetailExistQC(asnId, map.keySet().toArray(new String[]{}), WmsCodeMaster.QC_NEW.getCode());
        String[] updateLineNos = (String[]) isQcMsg.getData();
        for (String asnLineNo : updateLineNos) {
            List<BanQinWmAsnDetailReceiveEntity> updateEntities = map.get(asnLineNo);
            for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : updateEntities) {
                try {
                    this.arrangeLoc(wmAsnDetailReceiveEntity);
                } catch (WarehouseException e) {
                    msg.addMessage("行号" + wmAsnDetailReceiveEntity.getLineNo() + e.getMessage());
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage()) && StringUtils.isEmpty(qcStr) && StringUtils.isEmpty(cdStr)) {
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.addMessage("行号" + isQcMsg.getMessage() + "不是完全质检状态，不能操作");
            }
            if (StringUtils.isNotEmpty(qcStr)) {
                msg.addMessage("行号" + qcStr + "商品需要质检");
            }
            if (StringUtils.isNotEmpty(cdStr)) {
                msg.addMessage("行号" + cdStr + "已经越库匹配，不能操作");
            }
        }
        return msg;
    }

    /**
     * 描述： 安排库位
     *
     * @param wmAsnDetailReceiveEntity
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage arrangeLoc(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 未收货，码盘跟踪号不为“*”,计划上架库位为空, 并且收货明细行的库位指定规则为[收货前计算上架库位]，可以安排库位
        wmAsnDetailReceiveEntity = this.checkArrangeLocBefore(wmAsnDetailReceiveEntity, WmsConstants.YES);
        // 单据类型
        String orderType = wmAsnDetailReceiveEntity.getOrderType();
        // 货主
        String ownerCode = wmAsnDetailReceiveEntity.getOwnerCode();
        // 商品
        String skuCode = wmAsnDetailReceiveEntity.getSkuCode();
        // 收货库位
        String fromLoc = wmAsnDetailReceiveEntity.getToLoc();
        // 码盘跟踪号
        String fromId = wmAsnDetailReceiveEntity.getPlanId();
        // 单位
        String uom = wmAsnDetailReceiveEntity.getUom();
        // 包装
        String packCode = wmAsnDetailReceiveEntity.getPackCode();
        // 指定上架规则
        String newPaRule = wmAsnDetailReceiveEntity.getPaRule();
        // 上架库位指定规则
        String reserveCode = wmAsnDetailReceiveEntity.getReserveCode();
        // 实际生成上架任务数
        Double qtyPa = wmAsnDetailReceiveEntity.getQtyPlanEa();
        // 实际生成上架单位数
        Double qtyPaUom = wmAsnDetailReceiveEntity.getQtyPlanUom();
        // 毛重
        Double grossWeight = wmAsnDetailReceiveEntity.getGrossWeight() == null ? 0D : wmAsnDetailReceiveEntity.getGrossWeight();
        // 体积
        Double cubic = wmAsnDetailReceiveEntity.getCubic() == null ? 0D : wmAsnDetailReceiveEntity.getCubic();

        // 产生一个预收货的批次号，收货时可修改批次属性，更新批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(ownerCode);
        wmInvLotAttModel.setSkuCode(skuCode);
        wmInvLotAttModel.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
        wmInvLotAttModel.setLotAtt01(wmAsnDetailReceiveEntity.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(wmAsnDetailReceiveEntity.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(wmAsnDetailReceiveEntity.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(wmAsnDetailReceiveEntity.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(wmAsnDetailReceiveEntity.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(wmAsnDetailReceiveEntity.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(wmAsnDetailReceiveEntity.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(wmAsnDetailReceiveEntity.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(wmAsnDetailReceiveEntity.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(wmAsnDetailReceiveEntity.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(wmAsnDetailReceiveEntity.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(wmAsnDetailReceiveEntity.getLotAtt12());
        String lotnum = this.banQinInventoryService.createInvLotNum(wmInvLotAttModel);
        wmAsnDetailReceiveEntity.setLotNum(lotnum);

        // 根据上架规则，计算计划上架库位，更新库存
        BanQinWmTaskPaEntity entity = new BanQinWmTaskPaEntity();
        entity.setOwnerCode(ownerCode);
        entity.setSkuCode(skuCode);
        entity.setFmLoc(fromLoc);
        entity.setFmId(fromId);
        entity.setLotNum(lotnum);
        entity.setOrderType(orderType);
        entity.setPackCode(packCode);
        entity.setPaRule(newPaRule);
        entity.setUom(uom);
        entity.setQtyPa(qtyPa);
        entity.setQtyPaUom(qtyPaUom);
        entity.setGrossWeight(grossWeight * qtyPa);
        entity.setCubic(cubic * qtyPa);
        entity.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
        String planToLoc = banQinInboundPaOperationService.putawayCalculation(entity);
        if (StringUtils.isEmpty(planToLoc)) {
            throw new WarehouseException("未找到合适库位");// 未找到合适库位
        }
        // 更新收货明细的计划上架库位
        wmAsnDetailReceiveEntity.setPlanPaLoc(planToLoc);
        // 如果是一步收货，收货库位=计划上架库位
        if (WmsCodeMaster.RESERVE_B_RCV_ONE.getCode().equals(reserveCode)) {
            wmAsnDetailReceiveEntity.setToLoc(planToLoc);
        }
        wmAsnDetailReceiveEntity.setRcvTime(null);
        wmAsnDetailReceiveEntity = this.saveAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);

        // 实例收货参数
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.PLAN_LOCATION);
        invEntity.setOwnerCode(ownerCode);
        invEntity.setSkuCode(skuCode);
        invEntity.setLocCode(planToLoc);
        invEntity.setLotNum(lotnum);
        invEntity.setTraceId(fromId);
        invEntity.setPackCode(packCode);
        invEntity.setQtyEaOp(qtyPa);
        invEntity.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
        BanQinInventoryEntity updatedInv = banQinInventoryService.updateInventory(invEntity);
        if (!wmAsnDetailReceiveEntity.getLotNum().equals(updatedInv.getLotNum())) {
            throw new WarehouseException("并发时，批次号异常：收货明细" + wmAsnDetailReceiveEntity.getLotNum() + "库存" + updatedInv.getLotNum());
        }
        return msg;
    }

//*********************************************************************************************************************************************************//


    /**
     * 描述： 取消审核：不能进行收货操作
     *
     * @param asnIds
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public ResultMessage cancelAudit(String[] asnIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = checkAsnStatus(asnIds, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode()},
                new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.YES, null, null, WmsConstants.NO, WmsConstants.NO);
        String[] updateAsnIds = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = checkAsnExistQc(updateAsnIds);
        updateAsnIds = (String[]) isQcMsg.getData();
        if (updateAsnIds.length > 0) {
            banQinWmAsnHeaderService.updateAuditStatusById(Arrays.asList(updateAsnIds), WmsCodeMaster.AUDIT_NEW.getCode(), UserUtils.getUser().getName());
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
                msg.setSuccess(false);
                msg.addMessage(errorMsg.getMessage() + "非创建状态、未审核、已冻结、已码盘、已安排库位、已生成越库任务的订单，不能操作");
            }
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.setSuccess(false);
                msg.addMessage(isQcMsg.getMessage() + "已生成质检单，不能操作");
            }
        }

        return msg;
    }

    /**
     * 描述： 对ASN明细行未收货数，进行取消码盘
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public ResultMessage cancelPalletize(BanQinWmAsnDetail wmAsnDetailModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();

        String asnNo = wmAsnDetailModel.getAsnNo();
        String lineNo = wmAsnDetailModel.getLineNo();
        /*if (EqualsUtils.isEquals(wmAsnDetailModel.getStatus(), BanQinWmsCodeMaster.ASN_CANCEL.getCode())) {
            throw new WarehouseException(MsgUtils.getMessage("msg.wms.status.cancelPrm", MsgUtils.getMessage("msg.wms.phrase.lineNo") + lineNo));//={0}已取消，不能操作
        }
        //如果存在计划上架库位，不能取消码盘
        CommonQuery<WmAsnDetailReceiveModel> checkQuery =
                dao.createCommonQuery(WmAsnDetailReceiveModel.class)
                        .addCondition(Condition.eq("asnLineNo", lineNo))
                        .addCondition(Condition.eq("asnNo", asnNo))
                        .addCondition(Condition.isNotNull("planPaLoc"));//计划上架库位不为空
        List<WmAsnDetailReceiveModel> checkList = checkQuery.query();
        if (checkList.size() > 0) {
            msg.setSuccess(false);
            throw new WarehouseException(MsgUtils.getMessage("msg.wms.phrase.lineNo") + lineNo + MsgUtils.getMessage("msg.wms.status.reserveLoc"));
            //"已安排库位，不能操作");
        }
        String isPalletize = wmAsnDetailModel.getIsPalletize();
        //可取消码盘数 = 预收数-已收货数
        Double palletQtyEa = wmAsnDetailModel.getQtyAsnEa() - wmAsnDetailModel.getQtyRcvEa();
        if (palletQtyEa <= 0D) {
            msg.setSuccess(false);
            throw new WarehouseException(MsgUtils.getMessage("msg.wms.compare.cancelPlQty", MsgUtils.getMessage("msg.wms.phrase.lineNo") + lineNo));//没有可以取消码盘的数量
        }
        //码盘标识
        if (isPalletize == null || WmsConstants.NO.equals(isPalletize)) {
            msg.setSuccess(false);
            throw new WarehouseException(MsgUtils.getMessage("msg.wms.status.notPalletize", MsgUtils.getMessage("msg.wms.phrase.lineNo") + lineNo));//未码盘，不能操作
        }*/

        // 按码盘跟踪号、越库收货明细ID,质检收货明细ID分组
        // 获取可以取消码盘的收货明细记录
        List<BanQinWmAsnDetailReceive> list = banQinWmAsnDetailReceiveService.findCancelPalletizeDetail(asnNo, lineNo, WmsCodeMaster.ASN_NEW.getCode(), WmsConstants.NO, wmAsnDetailModel.getOrgId());
        if (list.size() > 0) {
            // 分组
            Map<String, List<BanQinWmAsnDetailReceive>> rcvMaps = Maps.newHashMap();
            // 越库收货明细ID,质检收货明细ID分组
            for (BanQinWmAsnDetailReceive model : list) {
                String key = model.getCdRcvId() + model.getQcRcvId();// 越库收货明细行号+质检收货明细ID
                if (rcvMaps.containsKey(key)) {
                    rcvMaps.get(key).add(model);
                } else {
                    List<BanQinWmAsnDetailReceive> models = Lists.newArrayList();
                    models.add(model);
                    rcvMaps.put(key, models);
                }
            }
            // 删除原越库匹配后码盘的收货明细
            for (BanQinWmAsnDetailReceive model : list) {
                banQinWmAsnDetailReceiveService.delete(model);
            }
            // 合并相同越库收货明细ID+质检收货明细ID的收货明细记录
            for (String key : rcvMaps.keySet()) {
                List<BanQinWmAsnDetailReceive> models = rcvMaps.get(key);
                BanQinWmAsnDetailReceive model = models.get(0);
                Double qtyPlanEa = 0D;
                for (BanQinWmAsnDetailReceive tempModel : models) {
                    qtyPlanEa += tempModel.getQtyPlanEa();
                }
                // 新增一条未收货数的收货明细
                BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = model;
                wmAsnDetailReceiveModel.setId(null);
                wmAsnDetailReceiveModel.setQtyPlanEa(qtyPlanEa);
                wmAsnDetailReceiveModel.setQtyRcvEa(0D);
                wmAsnDetailReceiveModel.setUom(wmAsnDetailModel.getUom());// 包装单位
                wmAsnDetailReceiveModel.setToId(wmAsnDetailModel.getTraceId());// 跟踪号
                wmAsnDetailReceiveModel.setPlanId(wmAsnDetailModel.getTraceId());
                // 如果是质检后，取消码盘，还原到质检确认时的信息。20150325
                if (StringUtils.isNotEmpty(wmAsnDetailReceiveModel.getQcRcvId())) {
                    List<BanQinWmQcDetailEntity> entities = banQinWmQcDetailService.getByQcRcvId(wmAsnDetailReceiveModel.getQcRcvId(), wmAsnDetailReceiveModel.getOrgId());
                    BanQinWmQcDetailEntity item = entities.get(0);
                    wmAsnDetailReceiveModel.setToId(item.getTraceId());
                    wmAsnDetailReceiveModel.setToLoc(item.getLocCode());
                }
                this.saveAsnDetailReceive(wmAsnDetailReceiveModel);
            }
        }

        // 取消后，标识为未码盘
        wmAsnDetailModel.setIsPalletize(WmsConstants.NO);
        banQinWmAsnDetailService.save(wmAsnDetailModel);

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 批量取消安排库位
     *
     * @param wmAsnDetailReceiveEntities
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage inboundBatchCancelArrangeLoc(List<BanQinWmAsnDetailReceiveEntity> wmAsnDetailReceiveEntities) {
        ResultMessage msg = new ResultMessage();
        String asnId = wmAsnDetailReceiveEntities.get(0).getHeadId();
        Map<String, List<BanQinWmAsnDetailReceiveEntity>> map = Maps.newHashMap();
        for (BanQinWmAsnDetailReceiveEntity entity : wmAsnDetailReceiveEntities) {
            List<BanQinWmAsnDetailReceiveEntity> list = Lists.newArrayList();
            // 分组
            String key = entity.getAsnLineNo();
            if (map.containsKey(key)) {
                list = map.get(key);
            }
            list.add(entity);
            map.put(key, list);
        }
        ResultMessage isQcMsg = this.checkAsnDetailExistQC(asnId, map.keySet().toArray(new String[]{}), WmsCodeMaster.QC_NEW.getCode());
        String[] updateLineNos = (String[]) isQcMsg.getData();
        for (String asnLineNo : updateLineNos) {
            List<BanQinWmAsnDetailReceiveEntity> updateEntitys = map.get(asnLineNo);
            for (BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity : updateEntitys) {
                try {
                    this.cancelArrangeLoc(wmAsnDetailReceiveEntity);
                } catch (WarehouseException e) {
                    msg.addMessage("行号" + wmAsnDetailReceiveEntity.getLineNo() + e.getMessage());
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
            msg.addMessage("行号" + isQcMsg.getMessage() + "不是完全质检状态，不能操作");
        }
        return msg;
    }

    /**
     * 取消安排库位
     */
    @Transactional
    public ResultMessage cancelArrangeLoc(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 未收货，计划上架库位不为空, 并且收货明细行的库位指定规则为[收货前计算上架库位]，可以取消安排库位
        wmAsnDetailReceiveEntity = this.checkArrangeLocBefore(wmAsnDetailReceiveEntity, WmsConstants.NO);
        // 更新收货明细的计划上架库位
        banQinWmAsnDetailReceiveService.updatePlanPaLoc(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getLineNo(), wmAsnDetailReceiveEntity.getOrgId());
        // 更新库存
        BanQinInventoryEntity invEntity = new BanQinInventoryEntity();
        invEntity.setAction(ActionCode.CANCEL_PLAN_LOCATION);
        invEntity.setOwnerCode(wmAsnDetailReceiveEntity.getOwnerCode());
        invEntity.setSkuCode(wmAsnDetailReceiveEntity.getSkuCode());
        invEntity.setLocCode(wmAsnDetailReceiveEntity.getPlanPaLoc());
        invEntity.setLotNum(wmAsnDetailReceiveEntity.getLotNum());
        invEntity.setTraceId(wmAsnDetailReceiveEntity.getPlanId());
        invEntity.setPackCode(wmAsnDetailReceiveEntity.getPackCode());
        invEntity.setQtyEaOp(wmAsnDetailReceiveEntity.getQtyPlanEa());
        invEntity.setOrgId(wmAsnDetailReceiveEntity.getOrgId());
        banQinInventoryService.updateInventory(invEntity);
        return msg;
    }

    /**
     * 描述： 取消ASN订单
     *
     * @param wmAsnHeaderModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void cancelAsn(BanQinWmAsnHeader wmAsnHeaderModel) {
        // 回填PO单的ASN数
        List<BanQinWmAsnDetail> wmAsnDetailList = banQinWmAsnDetailService.getByAsnNo(wmAsnHeaderModel.getAsnNo(), wmAsnHeaderModel.getOrgId());
        for (BanQinWmAsnDetail wmAsnDetailModel : wmAsnDetailList) {
            banQinWmPoDetailService.backfillQtyAsn(wmAsnDetailModel);
        }
        // 更新头状态
        wmAsnHeaderModel.setStatus(WmsCodeMaster.ASN_CANCEL.getCode());
        banQinWmAsnHeaderService.save(wmAsnHeaderModel);
    }

    /**
     * 描述： 取消ASN明细
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void cancelAsnDetail(BanQinWmAsnDetail wmAsnDetailModel) {
        // 回填PO单的ASN数
        banQinWmPoDetailService.backfillQtyAsn(wmAsnDetailModel);
        wmAsnDetailModel.setStatus(WmsCodeMaster.ASN_CANCEL.getCode());
        banQinWmAsnDetailService.save(wmAsnDetailModel);
        // 更新收货明细
        banQinWmAsnDetailReceiveService.updateStatus(wmAsnDetailModel.getAsnNo(), wmAsnDetailModel.getLineNo(), WmsCodeMaster.ASN_CANCEL.getCode(), wmAsnDetailModel.getOrgId());
        // 更新头状态
        banQinWmAsnHeaderService.updateStatus(wmAsnDetailModel.getAsnNo(), wmAsnDetailModel.getOrgId());
    }

    /**
     * 描述： 关闭ASN，未收货的ASN数，回填到PO单
     *
     * @param wmAsnHeaderModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void closeAsn(BanQinWmAsnHeader wmAsnHeaderModel) throws WarehouseException {
        BanQinWmTaskPa example = new BanQinWmTaskPa();
        example.setOrderNo(wmAsnHeaderModel.getAsnNo());
        example.setStatus(WmsCodeMaster.TSK_NEW.getCode());
        example.setOrgId(wmAsnHeaderModel.getOrgId());
        List<BanQinWmTaskPa> taskPaList = banQinWmTaskPaService.findList(example);
        // 不存在未完成的上架任务
        if (null != taskPaList && taskPaList.size() > 0) {
            throw new WarehouseException("存在未完成的上架任务，不能操作");// 存在未完成的上架任务，不能操作
        }
        // 存在未质检完成的明细
        List<BanQinWmQcSku> list = banQinWmQcSkuService.findUnCancelAndClose(wmAsnHeaderModel.getAsnNo(), wmAsnHeaderModel.getOrgId());
        if (null != list && list.size() > 0) {
            throw new WarehouseException("存在未关闭的质检单，不能操作");// "存在未关闭的质检单，不能操作"
        }

        // 回填PO单的ASN数
        List<BanQinWmAsnDetail> wmAsnDetailList = banQinWmAsnDetailService.getByAsnNo(wmAsnHeaderModel.getAsnNo(), wmAsnHeaderModel.getOrgId());
        for (BanQinWmAsnDetail wmAsnDetailModel : wmAsnDetailList) {
            banQinWmPoDetailService.backfillQtyAsn(wmAsnDetailModel);
        }
        // 更新头状态
        wmAsnHeaderModel.setStatus(WmsCodeMaster.ASN_CLOSE.getCode());
        banQinWmAsnHeaderService.save(wmAsnHeaderModel);
    }

    /**
     * 收货时，更新收货明细，ASN明细，PO明细状态
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity updateReceivingStatus(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) {
        Double currentRcvQty = wmAsnDetailReceiveEntity.getCurrentQtyRcvEa();// 当前实收数
        Double planQty = wmAsnDetailReceiveEntity.getQtyPlanEa();
        // 更新收货明细：对于收货明细行，只有完全收货，部分收货时，会拆行
        wmAsnDetailReceiveEntity.setStatus(WmsCodeMaster.ASN_FULL_RECEIVING.getCode());
        // 如果单行超收，预收数不变
        if (planQty > currentRcvQty) {
            wmAsnDetailReceiveEntity.setQtyPlanEa(currentRcvQty);
        }
        wmAsnDetailReceiveEntity.setQtyRcvEa(currentRcvQty);
        wmAsnDetailReceiveEntity = this.saveAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);

        // 更新ASN明细
        String status = "";
        String asnNo = wmAsnDetailReceiveEntity.getAsnNo();
        String asnLineNo = wmAsnDetailReceiveEntity.getAsnLineNo();
        BanQinWmAsnDetail wmAsnDetailModel = banQinWmAsnDetailService.getByAsnNoAndLineNo(asnNo, asnLineNo, wmAsnDetailReceiveEntity.getOrgId());
        Double receivedEaAsn = wmAsnDetailModel.getQtyRcvEa();// 已收数
        Double expectedEaAsn = wmAsnDetailModel.getQtyAsnEa();// 明细行预收数
        // 预收数>已收数+当前收货
        if (expectedEaAsn > currentRcvQty + receivedEaAsn) {
            status = WmsCodeMaster.ASN_PART_RECEIVING.getCode();
        } else {
            status = WmsCodeMaster.ASN_FULL_RECEIVING.getCode();
        }
        // 更新ASN明细的收货数
        wmAsnDetailModel.setQtyRcvEa(currentRcvQty + receivedEaAsn);
        wmAsnDetailModel.setStatus(status);
        banQinWmAsnDetailService.save(wmAsnDetailModel);

        // 如果存在PO号，则更新PO状态
        String poNo = wmAsnDetailReceiveEntity.getPoNo();
        String poLineNo = wmAsnDetailReceiveEntity.getPoLineNo();
        if (StringUtils.isNotEmpty(poNo) && StringUtils.isNotEmpty(poLineNo)) {
            BanQinWmPoDetail wmPoDetailModel = banQinWmPoDetailService.findByPoNoAndLineNo(poNo, poLineNo, wmAsnDetailReceiveEntity.getOrgId());
            if (null != wmPoDetailModel) {
                Double poEa = wmPoDetailModel.getQtyPoEa();// 采购数
                Double rcvEaPo = wmPoDetailModel.getQtyRcvEa();// 已收数
                // 预收数>已收数+当前收货
                if (poEa > rcvEaPo + currentRcvQty) {
                    status = WmsCodeMaster.PO_PART_RECEIVING.getCode();
                } else {
                    status = WmsCodeMaster.PO_FULL_RECEIVING.getCode();
                }
                wmPoDetailModel.setStatus(status);
                // 更新PO收货数
                wmPoDetailModel.setQtyRcvEa(rcvEaPo + currentRcvQty);
                banQinWmPoDetailService.save(wmPoDetailModel);
            }
        }
        return wmAsnDetailReceiveEntity;
    }

    /**
     * 取消收货时，更新状态，ASN和PO的收货数
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity updateCancelReceivingStatus(BanQinWmAsnDetailReceiveEntity wmAsnDetailReceiveEntity) {
        // 需取消的收货数
        Double cancelQytRcvEa = wmAsnDetailReceiveEntity.getCurrentQtyRcvEa();

        // 更新收货明细
        wmAsnDetailReceiveEntity.setQtyRcvEa(0D);
        wmAsnDetailReceiveEntity.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        wmAsnDetailReceiveEntity = this.saveAsnDetailReceiveEntity(wmAsnDetailReceiveEntity);

        // 更新ASN明细
        String status = "";
        BanQinWmAsnDetail wmAsnDetailModel = banQinWmAsnDetailService.getByAsnNoAndLineNo(wmAsnDetailReceiveEntity.getAsnNo(), wmAsnDetailReceiveEntity.getAsnLineNo(), wmAsnDetailReceiveEntity.getOrgId());
        Double receivedEaAsn = wmAsnDetailModel.getQtyRcvEa();// 已收数
        Double expectedEaAsn = wmAsnDetailModel.getQtyAsnEa();// 明细行预收数
        // 已收货=取消收货数
        if (receivedEaAsn - cancelQytRcvEa == 0D) {
            status = WmsCodeMaster.ASN_NEW.getCode();
        } else if (expectedEaAsn < receivedEaAsn - cancelQytRcvEa) {// 预收数<已收货-取消收货数
            status = WmsCodeMaster.ASN_FULL_RECEIVING.getCode();
        } else if (receivedEaAsn > cancelQytRcvEa) {// 已收数>取消收货数
            status = WmsCodeMaster.ASN_PART_RECEIVING.getCode();
        }
        // 更新ASN明细的收货数
        wmAsnDetailModel.setQtyRcvEa(receivedEaAsn - cancelQytRcvEa);
        wmAsnDetailModel.setStatus(status);
        banQinWmAsnDetailService.save(wmAsnDetailModel);

        // 如果存在PO号，则更新PO状态
        String poNo = wmAsnDetailReceiveEntity.getPoNo();
        String poLineNo = wmAsnDetailReceiveEntity.getPoLineNo();
        if (StringUtils.isNotEmpty(poNo) && StringUtils.isNotEmpty(poLineNo)) {
            BanQinWmPoDetail wmPoDetailModel = banQinWmPoDetailService.findByPoNoAndLineNo(poNo, poLineNo, wmAsnDetailReceiveEntity.getOrgId());
            if (null != wmPoDetailModel) {
                Double poEa = wmPoDetailModel.getQtyPoEa();// 采购数
                Double rcvEaPo = wmPoDetailModel.getQtyRcvEa();// 已收数
                // 已收货=取消收货数
                if (rcvEaPo - cancelQytRcvEa == 0D) {
                    status = WmsCodeMaster.PO_NEW.getCode();
                } else if (poEa < rcvEaPo - cancelQytRcvEa) {
                    status = WmsCodeMaster.PO_FULL_RECEIVING.getCode();// 预收数<已收货-取消收货数
                } else if (rcvEaPo > cancelQytRcvEa) {
                    status = WmsCodeMaster.PO_PART_RECEIVING.getCode();// 已收数>取消收货数
                }
                wmPoDetailModel.setStatus(status);
                // 更新PO收货数
                wmPoDetailModel.setQtyRcvEa(rcvEaPo - cancelQytRcvEa);
                banQinWmPoDetailService.save(wmPoDetailModel);
            }
        }
        return wmAsnDetailReceiveEntity;
    }


    /**
     * 描述：ASN单不为取消或关闭状态时，统计明细状态，更新单头状态
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    @Transactional
    public void updateAsnStatus(String asnNo, String orgId) {
        banQinWmAsnHeaderService.updateStatus(asnNo, orgId);
    }


    /**
     * PO单不为取消或关闭状态时，统计明细状态，更新单头状态
     *
     * @param poNo
     * @param orgId
     */
    @Transactional
    public void updatePoStatus(String poNo, String orgId) {
        // 根据PO单号，查询PO明细状态，统计出PO单的状态
        String poStatus = banQinWmPoDetailService.countPoStatus(poNo, orgId);
        // 更新状态
        BanQinWmPoHeader header = banQinWmPoHeaderService.findByPoNo(poNo, orgId);
        header.setStatus(poStatus);
        banQinWmPoHeaderService.save(header);
    }

    /**
     * 检确认时更新商品质检状态，ASN状态
     *
     * @param wmQcSkuModel
     */
    @Transactional
    public void updateQcConfirmStatus(BanQinWmQcSku wmQcSkuModel) {
        // 更新质检商品状态
        banQinWmQcSkuService.save(wmQcSkuModel);
        // 更新质检单头状态
        banQinWmQcHeaderService.updateStatus(wmQcSkuModel.getQcNo(), wmQcSkuModel.getOrgId());
        // 更新ASN商品质检状态
        updateAsnDetailQcStatus(wmQcSkuModel.getOrderNo(), wmQcSkuModel.getOrderLineNo(), wmQcSkuModel.getOrgId());
        // 更新ASN单头状态
        updateAsnQcStatus(wmQcSkuModel.getOrderNo(), wmQcSkuModel.getOrgId());
    }

    /**
     * 更新ASN商品明细的质检状态
     *
     * @param asnNo
     * @param lineNo
     * @param orgId
     */
    @Transactional
    public void updateAsnDetailQcStatus(String asnNo, String lineNo, String orgId) {
        String status = "";
        BanQinWmAsnDetailReceive wmAsnDetailReceive = new BanQinWmAsnDetailReceive();
        wmAsnDetailReceive.setAsnNo(asnNo);
        wmAsnDetailReceive.setAsnLineNo(lineNo);
        wmAsnDetailReceive.setOrgId(orgId);
        List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveList = banQinWmAsnDetailReceiveService.findList(wmAsnDetailReceive);
        if (CollectionUtil.isNotEmpty(wmAsnDetailReceiveList) && wmAsnDetailReceiveList.size() > 0) {
            List<String> qcStatusList = wmAsnDetailReceiveList.stream().map(BanQinWmAsnDetailReceive::getQcStatus).distinct().collect(Collectors.toList());
            if (qcStatusList.size() == 1) {
                status = qcStatusList.get(0);
            }
            if (qcStatusList.size() == 2) {
                status = WmsCodeMaster.QC_NEW.getCode();
                for (String qcStatus : qcStatusList) {
                    if (WmsCodeMaster.QC_FULL_QC.getCode().equals(qcStatus)) {
                        status = WmsCodeMaster.QC_PART_QC.getCode();
                        break;
                    }
                }
            }
            if (qcStatusList.size() == 3) {
                status = WmsCodeMaster.QC_PART_QC.getCode();
            }
            BanQinWmAsnDetail wmAsnDetail = banQinWmAsnDetailService.getByAsnNoAndLineNo(asnNo, lineNo, orgId);
            wmAsnDetail.setQcStatus(status);
            banQinWmAsnDetailService.save(wmAsnDetail);
        }
    }

    /**
     * 更新ASN单的质检状态
     */
    @Transactional
    public void updateAsnQcStatus(String asnNo, String orgId) {
        String status = "";
        BanQinWmAsnDetail wmAsnDetail = new BanQinWmAsnDetail();
        wmAsnDetail.setAsnNo(asnNo);
        wmAsnDetail.setIsQc(WmsConstants.YES);
        wmAsnDetail.setOrgId(orgId);
        List<BanQinWmAsnDetail> qcAsnDetails = banQinWmAsnDetailService.findList(wmAsnDetail);
        if (CollectionUtil.isNotEmpty(qcAsnDetails) && qcAsnDetails.size() > 0) {
            List<String> qcStatusList = qcAsnDetails.stream().map(BanQinWmAsnDetail::getQcStatus).distinct().collect(Collectors.toList());
            if (qcStatusList.size() == 1) {
                status = qcStatusList.get(0);
            }
            if (qcStatusList.size() == 2) {
                status = WmsCodeMaster.QC_NEW.getCode();
                for (String qcStatus : qcStatusList) {
                    if (WmsCodeMaster.QC_FULL_QC.getCode().equals(qcStatus)) {
                        status = WmsCodeMaster.QC_PART_QC.getCode();
                        break;
                    }
                }
            }
            if (qcStatusList.size() == 3) {
                status = WmsCodeMaster.QC_PART_QC.getCode();
            }
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.getByAsnNo(asnNo, orgId);
            wmAsnHeader.setQcStatus(status);
            banQinWmAsnHeaderService.save(wmAsnHeader);
        }
    }

    /**
     * 更新商品行的所有质检明细
     */
    @Transactional
    public void updateQcDetailStatus(String qcNo, String qcLineNo, String status, String orgId) {
        banQinWmQcDetailService.updateStatus(qcNo, qcLineNo, status, orgId);
    }

    /**
     * 分摊重量
     */
    @Transactional
    public void apportionWeight(BanQinWmAsnDetail model) {
        if (null != model.getTotalWeight()) {
            double avgWeight = BigDecimal.valueOf(model.getTotalWeight() / model.getQtyAsnEa()).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
            model.setLotAtt05(String.valueOf(avgWeight));
            banQinWmAsnDetailService.save(model);
            // 收货明细
            BanQinWmAsnDetailReceive detailReceiveModel = banQinWmAsnDetailReceiveService.getByAsnNoAndLineNo(model.getAsnNo(), model.getLineNo(), model.getOrgId());
            if (null != detailReceiveModel) {
                detailReceiveModel.setLotAtt05(String.valueOf(avgWeight));
                banQinWmAsnDetailReceiveService.save(detailReceiveModel);
            }
        }
    }

    /**
     * 描述： 生成码盘收货明细
     * 对未收货数，根据商品包装，按托盘拆ASN明细，生成多条收货明细，自动生成traceID（10位流水号），不足一托生成一条，带traceID
     *
     * @param asnId
     * @param lineNos
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage createPalletize(String asnId, String[] lineNos) {
        ResultMessage msg = this.checkAsnIsOperateStatus(asnId);
        if (!msg.isSuccess()) {
            return msg;
        }
        ResultMessage errorMsg = this.checkAsnDetailStatus(asnId, new String[]{WmsCodeMaster.ASN_NEW.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode()}, lineNos,
                new String[]{WmsCodeMaster.ASN_NEW.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()},
                new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.NO, WmsConstants.NO, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = this.checkAsnDetailExistQC(asnId, updateLineNos, WmsCodeMaster.QC_NEW.getCode());
        updateLineNos = (String[]) isQcMsg.getData();
        if (updateLineNos.length > 0) {
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
            for (String lineNo : updateLineNos) {
                BanQinWmAsnDetailEntity entity = banQinWmAsnDetailService.getEntityByAsnNoAndLineNo(wmAsnHeader.getAsnNo(), lineNo, wmAsnHeader.getOrgId());
                try {
                    this.createPalletize(entity);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage()) && StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
                msg.setMessage("行号" + errorMsg.getMessage() + "未审核，已码盘，已安排库位，已完全收货，已取消，不能操作");
            }
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.setMessage("行号" + isQcMsg.getMessage() + "不是收货状态、已冻结，不能操作");
            }
        }
        return msg;
    }

    /**
     * 描述： 取消码盘收货明细 对来自同一个ASN明细行的未收货明细取消码盘，合并为一条收货明细，traceId 取ASN明细的ID
     *
     * @param asnId
     * @param lineNos
     * @author Jianhua on 2019/1/28
     */
    public ResultMessage cancelPalletize(String asnId, String[] lineNos) {
        ResultMessage msg = this.checkAsnIsOperateStatus(asnId);
        if (!msg.isSuccess()) {
            return msg;
        }
        ResultMessage errorMsg = this.checkAsnDetailStatus(asnId, new String[]{WmsCodeMaster.ASN_NEW.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode()}, lineNos,
                new String[]{WmsCodeMaster.ASN_NEW.getCode(), WmsCodeMaster.ASN_PART_RECEIVING.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()},
                new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.YES, null, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        // 校验是否存在质检状态为创建的收货明细，存在不允许取消码盘，并且过滤
        ResultMessage isQcMsg = this.checkAsnDetailExistQC(asnId, updateLineNos, WmsCodeMaster.QC_NEW.getCode());
        updateLineNos = (String[]) isQcMsg.getData();
        // 校验并且过滤存在重复码盘跟踪号的商品行
        ResultMessage rePlMsg = this.checkRepeatPlanId(asnId, updateLineNos);
        updateLineNos = (String[]) rePlMsg.getData();
        if (updateLineNos.length > 0) {
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
            for (String lineNo : updateLineNos) {
                BanQinWmAsnDetail wmAsnDetail = banQinWmAsnDetailService.getByAsnNoAndLineNo(wmAsnHeader.getAsnNo(), lineNo, wmAsnHeader.getOrgId());
                try {
                    this.cancelPalletize(wmAsnDetail);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(isQcMsg.getMessage()) && StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(rePlMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
                msg.setMessage("行号" + errorMsg.getMessage() + "未码盘，或已完全收货，已安排库位，或已取消，不能操作");
            }
            if (StringUtils.isNotEmpty(isQcMsg.getMessage())) {
                msg.setMessage("行号" + isQcMsg.getMessage() + "不是完全质检状态，不能操作");
            }
            if (StringUtils.isNotEmpty(rePlMsg.getMessage())) {
                msg.setMessage("行号" + rePlMsg.getMessage() + "收货明细可能做了生成越库任务，质检确认，收货等操作，不能操作");
            }
        }
        return msg;
    }
}
