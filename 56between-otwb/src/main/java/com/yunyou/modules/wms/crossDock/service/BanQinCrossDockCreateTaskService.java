package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.service.BanQinInboundOperationService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成越库任务
 *
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockCreateTaskService {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinWmAsnDetailService wmAsnDetailService;
    @Autowired
    protected SynchronizedNoService noService;
    @Autowired
    protected BanQinWmsCommonService wmsCommonService;
    @Autowired
    @Lazy
    private BanQinInboundOperationService inboundOperationService;

    /**
     * 直接越库， 以出库单为基准将入库单收货明细行与出库单商品明细行匹配， ASN记录SO单号、行号，SO不记录ASN信息
     * 匹配规则： 预计出货时间到升序、优先级升序、订单创建时间升序 预计出货时间到、优先级、订单创建时间都相同的情况下，按数量从大到小顺序匹配
     * 出库订单明细未生成装车单，出库订单未生成波次计划，已审核/不审核，不冻结、不拦截 商品不进行序列号控制
     */
    @Transactional
    public ResultMessage crossDockCreateTask(BanQinWmSoDetailEntity soDetailEntity, List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        Double qtySoEa = soDetailEntity.getQtySoEa();
        Double qtySoUom = soDetailEntity.getQtySoUom();
        List<BanQinWmAsnDetailReceiveEntity> removeRcvEntitys = new ArrayList<>();
        List<BanQinWmAsnDetailReceiveEntity> newRcvEntitys = new ArrayList<>();
        // 循环收货明细
        for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
            if (qtySoEa == 0) {
                break;
            }
            // 不考虑单位是否相同
            Double qtyOpEa = 0D;// 操作数EA
            Double qtyOpUom = 0D;// 操作数UOM
            // 1、比较ASN收货明细的数量和SO商品数量
            if (asnReceiveEntity.getQtyPlanEa().compareTo(qtySoEa) > 0) {
                // 如果预收货数>订货数，部分收货，操作数=订货数,
                // 拆分收货明细行
                qtyOpEa = qtySoEa;
                qtyOpUom = qtySoUom;
                // 收货明细拆分，单位 = SO商品明细单位
            } else {
                qtyOpEa = asnReceiveEntity.getQtyPlanEa();
                qtyOpUom = asnReceiveEntity.getQtyPlanUom();
            }
            // 拆分收货明细
            BanQinWmAsnDetailReceiveEntity newAsnRcvEntity = splitWmAsnDetailReceive(asnReceiveEntity, asnReceiveEntity.getQtyPlanEa() - qtyOpEa, WmsCodeMaster.CD_TYPE_DIRECT.getCode(), soDetailEntity.getSoNo(), soDetailEntity.getLineNo());
            if (newAsnRcvEntity != null) {
                // 新增记录写入
                newRcvEntitys.add(newAsnRcvEntity);
            }
            // 已操作记录对象
            removeRcvEntitys.add(asnReceiveEntity);

            // 剩余未匹配ASN的订货数
            qtySoEa -= qtyOpEa;
            qtySoUom -= qtyOpUom;
        }
        // 如果无法全匹配
        // 拆分出库单明细行
        splitWmSoDetail(soDetailEntity, qtySoEa, WmsCodeMaster.CD_TYPE_DIRECT.getCode());

        asnReceiveEntitys.removeAll(removeRcvEntitys);
        newRcvEntitys.addAll(asnReceiveEntitys);// 拆分出来的记录，按顺序前置
        msg.setData(newRcvEntitys);
        return msg;
    }

    /**
     * 拆分入库单收货明细行
     */
    @Transactional
    public BanQinWmAsnDetailReceiveEntity splitWmAsnDetailReceive(BanQinWmAsnDetailReceiveEntity asnReceiveEntity, Double qtyOpEa, String cdType, String soNo, String soLineNo) throws WarehouseException {
        BanQinWmAsnDetailReceiveEntity newAsnReceiveEntity = null;
        if (qtyOpEa > 0) {
            // 新收货明细
            BanQinWmAsnDetailReceive newModel = new BanQinWmAsnDetailReceive();
            BeanUtils.copyProperties(asnReceiveEntity, newModel);
            newModel.setId(IdGen.uuid());
            newModel.setRecVer(0);
            newModel.setIsNewRecord(true);
            // ASN明细
            BanQinWmAsnDetail wmAsnDetailModel = wmAsnDetailService.getByAsnNoAndLineNo(asnReceiveEntity.getAsnNo(), asnReceiveEntity.getAsnLineNo(), asnReceiveEntity.getOrgId());
            // 码盘标识
            String isPalletize = wmAsnDetailModel.getIsPalletize();
            // 新的收货明细，如果ASN明细已码盘，traceID为系统生成的新traceID，反之，traceID为ASN明细的traceID；
            if (WmsConstants.YES.equals(isPalletize)) {
                newModel.setToId(noService.getDocumentNo(GenNoType.WM_TRACE_ID.name()));
            } else {
                newModel.setToId(asnReceiveEntity.getPlanId());
            }
            newModel.setQtyPlanEa(qtyOpEa);// 预收货数
            if (asnReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_FULL_RECEIVING.getCode())) {
                newModel.setQtyRcvEa(qtyOpEa);// 收货完成后拆分，写收货数
            }

            String lineNo = wmAsnDetailReceiveService.getNewLineNo(asnReceiveEntity.getAsnNo(), asnReceiveEntity.getOrgId());
            newModel.setLineNo(lineNo);
            newModel.setCdType(null);// 越库标记为空
            newModel.setSoNo(null);
            newModel.setSoLineNo(null);
            this.wmAsnDetailReceiveService.save(newModel);

            newAsnReceiveEntity = wmAsnDetailReceiveService.getEntityByAsnNoAndLineNo(newModel.getAsnNo(), newModel.getLineNo(), newModel.getOrgId());
        }

        // 源收货明细行，标记越库，soNo,soLineNo
        asnReceiveEntity.setQtyPlanEa(asnReceiveEntity.getQtyPlanEa() - qtyOpEa);
        asnReceiveEntity.setQtyPlanUom(asnReceiveEntity.getQtyPlanUom() / asnReceiveEntity.getUomQty());
        if (asnReceiveEntity.getStatus().equals(WmsCodeMaster.ASN_FULL_RECEIVING.getCode())) {
            asnReceiveEntity.setQtyRcvEa(asnReceiveEntity.getQtyPlanEa());// 收货完成后拆分，写收货数
            asnReceiveEntity.setQtyRcvUom(asnReceiveEntity.getQtyPlanUom());
        }
        asnReceiveEntity.setSoNo(soNo);
        asnReceiveEntity.setSoLineNo(soLineNo);
        asnReceiveEntity.setCdType(cdType);// 越库类型标记
        if (StringUtils.isEmpty(asnReceiveEntity.getCdRcvId())) {
            asnReceiveEntity.setCdRcvId(noService.getDocumentNo(GenNoType.WM_CD_RCV_ID.name()));// 越库收货明细号
        }
        inboundOperationService.saveAsnDetailReceiveEntity(asnReceiveEntity);

        return newAsnReceiveEntity;
    }

    /**
     * 拆分出库单明细
     */
    @Transactional
    public BanQinWmSoDetailEntity splitWmSoDetail(BanQinWmSoDetailEntity soDetailEntity, Double qtyOpEa, String cdType) throws WarehouseException {
        // 新商品明细行
        BanQinWmSoDetailEntity newSoDetailEntity = null;
        if (qtyOpEa > 0) {
            // 新商品明细行
            newSoDetailEntity = new BanQinWmSoDetailEntity();
            // soDetailEntity.setUom(WmsConstants.UOM_EA);//拆分时单位默认EA
            // 复制
            BeanUtils.copyProperties(soDetailEntity, newSoDetailEntity);
            newSoDetailEntity.setQtySoEa(qtyOpEa);// 订货数
            newSoDetailEntity.setCdType(null);// 越库标记
            newSoDetailEntity.setId(null);
            newSoDetailEntity.setRecVer(0);
            newSoDetailEntity.setLdStatus(null);// 装车状态不复制
            newSoDetailEntity.setIsNewRecord(true);
            // 数量重置为0
            newSoDetailEntity.setQtyPreallocEa(0D);// 预配数量
            newSoDetailEntity.setQtyAllocEa(0D);// 分配数量
            newSoDetailEntity.setQtyPkEa(0D);// 拣货数量
            newSoDetailEntity.setQtyShipEa(0D);// 发货数量
            // 保存
            // 计算出库单行状态
            newSoDetailEntity.setStatus(wmSoDetailService.getStatus(newSoDetailEntity));
            newSoDetailEntity = wmSoDetailService.saveSoDetail(newSoDetailEntity);
        }
        // 源商品明细行
        soDetailEntity.setQtySoEa(soDetailEntity.getQtySoEa() - qtyOpEa);
        soDetailEntity.setCdType(cdType);
        // 计算出库单行状态
        soDetailEntity.setStatus(wmSoDetailService.getStatus(soDetailEntity));
        wmSoDetailService.saveSoDetail(soDetailEntity);

        return newSoDetailEntity;
    }

    /**
     * 已有越库类型标记的ASN码盘，按单位，生成码盘明细 1、按包装单位拆分
     * 2、按控制参数控制4个，是否要拆分到最明细(1PL\1PL\1CS\1CS\nEA) 已安排库位、生成质检单并且未质检确认的收货明细
     * 必须先取消安排库位，删除质检单/质检确认
     */
    @Transactional
    public ResultMessage createPalletizeByCd(BanQinWmAsnDetailEntity wmAsnDetailEntity) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String asnNo = wmAsnDetailEntity.getAsnNo();
        String lineNo = wmAsnDetailEntity.getLineNo();
        String isPalletize = wmAsnDetailEntity.getIsPalletize();
        String orgId = wmAsnDetailEntity.getOrgId();
        // 已码盘校验
        if (StringUtils.isNotEmpty(isPalletize) && WmsConstants.YES.equals(isPalletize)) {
            msg.setSuccess(false);
            throw new WarehouseException("[" + asnNo + "][" + lineNo + "]已码盘，不能操作");
        }
        // 未码盘标识
        double detailQty = 0D;// 每个收货明细行码盘数
        String traceId = "";
        String uom = "";
        // 获取创建状态，未安排库位的收货明细
        List<BanQinWmAsnDetailReceive> list = wmAsnDetailReceiveService.findCanPalletizeDetail(asnNo, lineNo, WmsCodeMaster.ASN_NEW.getCode(), WmsConstants.YES, wmAsnDetailEntity.getOrgId());
        if (list.size() > 0) {
            // 获取包装维护
            BanQinCdWhPackageEntity packageEntity = wmsCommonService.getCdWhPackageRelation(wmAsnDetailEntity.getPackCode(), orgId);
            List<BanQinCdWhPackageRelation> packageRelationItems = packageEntity.getCdWhPackageRelations();
            double qtyOt = 0;
            double qtyPl = 0;
            double qtyCs = 0;
            double qtyIp = 0;
            // 越库后码盘控制参数
            String cdSplitOt = null;
            String cdSplitPl = null;
            String cdSplitCs = null;
            String cdSplitIp = null;
            // 获取包装换算数量
            for (BanQinCdWhPackageRelation item : packageRelationItems) {
                if (item.getCdprUnitLevel().equals(WmsConstants.UOM_OT)) {
                    qtyOt = item.getCdprQuantity();
                    cdSplitOt = SysControlParamsUtils.getValue(ControlParamCode.ASN_CD_SPLIT_OT.getCode(), orgId);// OT单位是否拆分收货明细
                } else if (item.getCdprUnitLevel().equals(WmsConstants.UOM_PL)) {
                    qtyPl = item.getCdprQuantity();
                    cdSplitPl = SysControlParamsUtils.getValue(ControlParamCode.ASN_CD_SPLIT_PL.getCode(), orgId);// PL单位是否拆分收货明细
                } else if (item.getCdprUnitLevel().equals(WmsConstants.UOM_CS)) {
                    qtyCs = item.getCdprQuantity();
                    cdSplitCs = SysControlParamsUtils.getValue(ControlParamCode.ASN_CD_SPLIT_CS.getCode(), orgId);// CS单位是否拆分收货明细
                } else if (item.getCdprUnitLevel().equals(WmsConstants.UOM_IP)) {
                    qtyIp = item.getCdprQuantity();
                    cdSplitIp = SysControlParamsUtils.getValue(ControlParamCode.ASN_CD_SPLIT_IP.getCode(), orgId);// IP单位是否拆分收货明细
                }
            }
            // 删除原创建状态明细行
            wmAsnDetailReceiveService.deleteAll(list);
            // 循环码盘
            // 生成托盘号的次数=可码盘数/包装托盘数
            int num = 0;
            for (BanQinWmAsnDetailReceive model : list) {
                Double qtyPlanEa = model.getQtyPlanEa();// 用于计算生成托盘数,可操作数
                if (qtyOt > 0 && qtyPlanEa >= qtyOt) {
                    // OT单位
                    int otNum = (int) Math.floor(qtyPlanEa / qtyOt);// 向下取整
                    if (cdSplitOt != null && cdSplitOt.equals(WmsConstants.YES)) {
                        num += otNum;
                    } else {
                        num++;
                    }
                    qtyPlanEa -= otNum * qtyOt;
                }
                if (qtyPl > 0 && qtyPlanEa >= qtyPl) {
                    // PL单位
                    int plNum = (int) Math.floor(qtyPlanEa / qtyPl);// 向下取整
                    if (cdSplitPl != null && cdSplitPl.equals(WmsConstants.YES)) {
                        num += plNum;
                    } else {
                        num++;
                    }
                    qtyPlanEa -= plNum * qtyPl;
                }
                if (qtyCs > 0 && qtyPlanEa >= qtyCs) {
                    // CS单位
                    int csNum = (int) Math.floor(qtyPlanEa / qtyCs);// 向下取整
                    if (cdSplitCs != null && cdSplitCs.equals(WmsConstants.YES)) {
                        num += csNum;
                    } else {
                        num++;
                    }
                    qtyPlanEa -= csNum * qtyCs;
                }
                if (qtyIp > 0 && qtyPlanEa >= qtyIp) {
                    // IP单位
                    int ipNum = (int) Math.floor(qtyPlanEa / qtyIp);// 向下取整
                    if (cdSplitIp != null && cdSplitIp.equals(WmsConstants.YES)) {
                        num += ipNum;
                    } else {
                        num++;
                    }
                    qtyPlanEa -= ipNum * qtyIp;
                }
                if (qtyPlanEa > 0) {
                    num++;
                }
            }

            // 批量生成跟踪号
            List<String> traceIds = noService.getDocumentNo(GenNoType.WM_TRACE_ID.name(), num);
            int i = 0;
            // 循环收货明细
            for (BanQinWmAsnDetailReceive model : list) {
                Double qtyPlanEa = model.getQtyPlanEa();// 可操作数
                // 开始拆分
                // OT单位
                if (qtyOt > 0 && qtyPlanEa >= qtyOt) {
                    int otNum = (int) Math.floor(qtyPlanEa / qtyOt);// 向下取整
                    // 控制参数，拆分到1单位数据行
                    if (cdSplitOt != null && cdSplitOt.equals(WmsConstants.YES)) {
                        for (int j = 0; j < otNum; j++, i++) {
                            detailQty = qtyOt;// 收货明细数量
                            uom = WmsConstants.UOM_OT;
                            qtyPlanEa -= detailQty;
                            // 保存
                            saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        }
                    } else {
                        detailQty = otNum * qtyOt;
                        uom = WmsConstants.UOM_OT;
                        qtyPlanEa -= detailQty;
                        // 保存
                        saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        i++;
                    }
                }
                // PL单位
                if (qtyPl > 0 && qtyPlanEa >= qtyPl) {
                    int plNum = (int) Math.floor(qtyPlanEa / qtyPl);// 向下取整
                    if (cdSplitPl != null && cdSplitPl.equals(WmsConstants.YES)) {
                        for (int j = 0; j < plNum; j++, i++) {
                            detailQty = qtyPl;// 收货明细数量
                            uom = WmsConstants.UOM_PL;
                            qtyPlanEa -= detailQty;
                            // 保存
                            saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        }
                    } else {
                        detailQty = plNum * qtyPl;
                        uom = WmsConstants.UOM_PL;
                        qtyPlanEa -= detailQty;
                        // 保存
                        saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        i++;
                    }
                }

                // CS单位
                if (qtyCs > 0 && qtyPlanEa >= qtyCs) {
                    int csNum = (int) Math.floor(qtyPlanEa / qtyCs);// 向下取整
                    if (cdSplitCs != null && cdSplitCs.equals(WmsConstants.YES)) {
                        for (int j = 0; j < csNum; j++, i++) {
                            detailQty = qtyCs;// 收货明细数量
                            uom = WmsConstants.UOM_CS;
                            qtyPlanEa -= detailQty;
                            // 保存
                            saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        }
                    } else {
                        detailQty = csNum * qtyCs;
                        uom = WmsConstants.UOM_CS;
                        qtyPlanEa -= detailQty;
                        // 保存
                        saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        i++;
                    }
                }

                // IP单位
                if (qtyIp > 0 && qtyPlanEa >= qtyIp) {
                    int ipNum = (int) Math.floor(qtyPlanEa / qtyIp);// 向下取整
                    if (cdSplitIp != null && cdSplitIp.equals(WmsConstants.YES)) {
                        for (int j = 0; j < ipNum; j++, i++) {
                            detailQty = qtyIp;// 收货明细数量
                            uom = WmsConstants.UOM_IP;
                            qtyPlanEa -= detailQty;
                            // 保存
                            saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        }
                    } else {
                        detailQty = ipNum * qtyIp;
                        uom = WmsConstants.UOM_IP;
                        qtyPlanEa -= detailQty;
                        // 保存
                        saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                        i++;
                    }
                }
                if (qtyPlanEa > 0) {
                    detailQty = qtyPlanEa;
                    uom = WmsConstants.UOM_EA;
                    // 保存
                    saveAsnDetailReceives(model, uom, detailQty, traceIds.get(i));
                    i++;
                }
            }
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 对ASN明细行未收货数，进行取消码盘 已安排库位、需要质检并且未质检确认的收货明细 必须先取消安排库位、取消质检
     */
    @Transactional
    public ResultMessage cancelPalletizeByCd(BanQinWmAsnDetail wmAsnDetailModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        String asnNo = wmAsnDetailModel.getAsnNo();
        String lineNo = wmAsnDetailModel.getLineNo();
        if (wmAsnDetailModel.getStatus().equals(WmsCodeMaster.ASN_CANCEL.getCode())) {
            throw new WarehouseException("[" + asnNo + "][" + lineNo + "]已取消，不能操作");
        }
        // 获取可以取消码盘的收货明细记录
        BanQinWmAsnDetailReceive condition = new BanQinWmAsnDetailReceive();
        condition.setAsnNo(asnNo);
        condition.setLineNo(lineNo);
        condition.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        condition.setOrgId(wmAsnDetailModel.getOrgId());
        List<BanQinWmAsnDetailReceive> list = wmAsnDetailReceiveService.findList(condition);
        list = list.stream().filter(s -> StringUtils.isEmpty(s.getPlanPaLoc()) && StringUtils.isNotEmpty(s.getCdType())).collect(Collectors.toList());
        list.sort(Comparator.comparing(BanQinWmAsnDetailReceive::getLineNo));
        if (list.size() > 0) {
            // 越库码盘原越库记录 分组 key:记录原行号，
            Map<String, List<BanQinWmAsnDetailReceive>> rcvMaps = new HashMap<>();
            // 越库码盘原越库记录 分组
            for (BanQinWmAsnDetailReceive model : list) {
                String key = model.getCdRcvId();// 越库收货明细行号
                if (rcvMaps.containsKey(key)) {
                    rcvMaps.get(key).add(model);
                } else {
                    List<BanQinWmAsnDetailReceive> models = new ArrayList<>();
                    models.add(model);
                    rcvMaps.put(key, models);
                }
            }
            for (String key : rcvMaps.keySet()) {
                List<BanQinWmAsnDetailReceive> models = rcvMaps.get(key);
                BanQinWmAsnDetailReceive model = models.get(0);
                Double qtyPlanEa = 0D;
                for (BanQinWmAsnDetailReceive tempModel : models) {
                    qtyPlanEa += tempModel.getQtyPlanEa();
                }
                // 删除原越库匹配后码盘的收货明细
                wmAsnDetailReceiveService.deleteAll(models);
                // 新增一条未收货数的收货明细
                BanQinWmAsnDetailReceive receiveModel = model;
                receiveModel.setId(null);
                receiveModel.setQtyPlanEa(qtyPlanEa);
                receiveModel.setQtyRcvEa(0D);
                receiveModel.setUom(wmAsnDetailModel.getUom());// 包装单位
                receiveModel.setToId(wmAsnDetailModel.getTraceId());// 跟踪号
                receiveModel.setPlanId(wmAsnDetailModel.getTraceId());
                inboundOperationService.saveAsnDetailReceive(receiveModel);
            }
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 保存收货明细
     */
    @Transactional
    public void saveAsnDetailReceives(BanQinWmAsnDetailReceive model, String uom, Double qtyPlanEa, String traceId) throws WarehouseException {
        BanQinWmAsnDetailReceive wmAsnDetailReceiveModel = new BanQinWmAsnDetailReceive();
        BeanUtils.copyProperties(model, wmAsnDetailReceiveModel);
        wmAsnDetailReceiveModel.setId(null);
        wmAsnDetailReceiveModel.setQtyPlanEa(qtyPlanEa);
        wmAsnDetailReceiveModel.setQtyRcvEa(0D);
        wmAsnDetailReceiveModel.setUom(uom);
        wmAsnDetailReceiveModel.setPlanId(traceId);
        wmAsnDetailReceiveModel.setToId(traceId);
        inboundOperationService.saveAsnDetailReceive(wmAsnDetailReceiveModel);
    }

}
