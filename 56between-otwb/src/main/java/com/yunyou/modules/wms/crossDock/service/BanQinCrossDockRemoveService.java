package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceive;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnReceiveByCdQuery;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成越库任务
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockRemoveService {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinCrossDockCreateTaskService crossDockCreateTaskService;

    /**
     * 删除asn收货明细和SO商品明细的越库绑定关系（外围+batch批量方法？） 针对直接越库(创建、SO部分状态)
     */
    @Transactional
    public ResultMessage crossDockRemoveByDirect(String asnNo, String rcvLineNo, String soNo, String soLineNo, String orgId) throws WarehouseException {
        ResultMessage msg =  crossDockRemoveAsn(asnNo, rcvLineNo, orgId);
        if (msg.isSuccess()) {
            Double qtyOpEa = (Double) msg.getData();
            crossDockRemoveSo(soNo, soLineNo, qtyOpEa, orgId);
        }
        return msg;
    }

    /**
     * 取消越库标记：删除asn收货明细和SO商品明细的越库标记 分拨越库(创建、SO部分状态)
     */
    @Transactional
    public ResultMessage crossDockRemoveByIndirect(String ownerCode, String skuCode, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 收货明细
        BanQinWmAsnReceiveByCdQuery asnCondition = new BanQinWmAsnReceiveByCdQuery();
        asnCondition.setOwnerCode(ownerCode);
        asnCondition.setSkuCode(skuCode);
        asnCondition.setStatus(WmsCodeMaster.ASN_NEW.getCode());
        asnCondition.setCdTypes(new String[] { WmsCodeMaster.CD_TYPE_INDIRECT.getCode() });
        asnCondition.setOrgId(orgId);
        List<BanQinWmAsnDetailReceiveEntity> asnRcvEntitys = wmAsnDetailReceiveService.getEntityByCdAndSku(asnCondition);
        for (BanQinWmAsnDetailReceiveEntity asnRcvEntity : asnRcvEntitys) {
            crossDockRemoveAsn(asnRcvEntity.getAsnNo(), asnRcvEntity.getLineNo(), asnRcvEntity.getOrgId());
        }
        // SO商品明细
        BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
        soCondition.setOwnerCode(ownerCode);
        soCondition.setSkuCode(skuCode);
        soCondition.setCdTypes(new String[] { WmsCodeMaster.CD_TYPE_INDIRECT.getCode() });
        soCondition.setOrgId(orgId);
        List<BanQinWmSoDetailEntity> soDetailEntitys = wmSoDetailService.getEntityByCdAndSku(soCondition);
        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            // 已操作数
            Double qtyOpEa = soDetailEntity.getQtyAllocEa() + soDetailEntity.getQtyPkEa() + soDetailEntity.getQtyShipEa();
            if (soDetailEntity.getQtySoEa() > qtyOpEa) {
                // 如果订货数>已操作数，那么表示商品明细创建/部分状态，可取消标记/拆分取消
                crossDockRemoveSo(soDetailEntity.getSoNo(), soDetailEntity.getLineNo(), soDetailEntity.getQtySoEa() - qtyOpEa, orgId);
            }
        }
        return msg;
    }

    /**
     * 直接越库 ： 删除asn收货明细和SO商品明细的越库标记
     */
    @Transactional
    public ResultMessage crossDockRemoveAsn(String asnNo, String rcvLineNo, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // asnNo,LineNo获取收货明细
        BanQinWmAsnDetailReceive asnRcvModel = wmAsnDetailReceiveService.getByAsnNoAndLineNo(asnNo, rcvLineNo, orgId);
        if (asnRcvModel != null) {
            // 清空ASN收货明细 越库类型标记
            if (!asnRcvModel.getStatus().equals(WmsCodeMaster.ASN_NEW.getCode())) {
                throw new WarehouseException("[" + asnNo + "][" + rcvLineNo + "]不是创建状态，不能操作");
            }
            asnRcvModel.setSoNo(null);
            asnRcvModel.setSoLineNo(null);
            asnRcvModel.setCdType(null);

            Double qtyPlanEa = asnRcvModel.getQtyPlanEa();
            msg.setData(qtyPlanEa);// 返回收货操作数，用于出库

            // 合并收货明细
            // 查询出同一个planId的创建状态的收货明细
            BanQinWmAsnDetailReceive condition = new BanQinWmAsnDetailReceive();
            condition.setAsnLineNo(asnRcvModel.getAsnLineNo());
            condition.setAsnNo(asnRcvModel.getAsnNo());
            condition.setPlanId(asnRcvModel.getPlanId());
            condition.setStatus(WmsCodeMaster.ASN_NEW.getCode());
            condition.setQcRcvId(asnRcvModel.getQcRcvId());
            condition.setOrgId(orgId);
            List<BanQinWmAsnDetailReceive> wmAsnDetailReceiveList = wmAsnDetailReceiveService.findList(condition);
            wmAsnDetailReceiveList = wmAsnDetailReceiveList.stream().filter(r -> StringUtils.isEmpty(r.getQcRcvId()) && StringUtils.isEmpty(r.getPlanPaLoc()) && StringUtils.isEmpty(r.getCdType())).collect(Collectors.toList());
            wmAsnDetailReceiveList.sort(Comparator.comparing(BanQinWmAsnDetailReceive::getLineNo));
            if (wmAsnDetailReceiveList.size() > 0) {
                wmAsnDetailReceiveList.add(asnRcvModel);// 将当前行加入计算
                qtyPlanEa = 0D;
                asnRcvModel = wmAsnDetailReceiveList.get(0);// 第一行合并保存
                int removeIndex = 0;// 当前保存行不参与删除操作(行号最小行)
                for (int i = 0; i < wmAsnDetailReceiveList.size(); i++) {
                    BanQinWmAsnDetailReceive model = wmAsnDetailReceiveList.get(i);
                    // 获取行号最小行
                    if (asnRcvModel.getLineNo().compareTo(model.getLineNo()) > 0) {
                        asnRcvModel = model;// 获取行号最小记录
                        removeIndex = i;
                    }
                    qtyPlanEa = qtyPlanEa + model.getQtyPlanEa();
                }
                asnRcvModel.setQtyPlanEa(qtyPlanEa);// 行号最小记录
                wmAsnDetailReceiveList.remove(removeIndex);// 去除第一行
                // 更新取消行，删除创建行
                wmAsnDetailReceiveService.deleteAll(wmAsnDetailReceiveList);
            }
            asnRcvModel.setCdRcvId(null);// 越库收货明细行号
            // 保存
            this.wmAsnDetailReceiveService.save(asnRcvModel);
        }

        msg.setSuccess(true);
        return msg;
    }

    /**
     * 直接越库 ： 删除asn收货明细和SO商品明细的越库标记
     */
    @Transactional
    public ResultMessage crossDockRemoveSo(String soNo, String soLineNo, Double qtyOpEa, String orgId) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 获取soNo lineNo
        BanQinWmSoDetailEntity soDetailEntity = wmSoDetailService.findEntityBySoNoAndLineNo(soNo, soLineNo, orgId);
        if (null != soDetailEntity) {
            // 清空SO商品明细的越库类型标记
            // 先拆分，再合并
            if (qtyOpEa > 0D && soDetailEntity.getQtySoEa() > qtyOpEa) {
                // 保存，拆分行，
                // 源行：标记 ，新行：无越库标记，记录源行
                crossDockCreateTaskService.splitWmSoDetail(soDetailEntity, qtyOpEa, soDetailEntity.getCdType());
            } else {
                soDetailEntity.setCdType(null);
                this.wmSoDetailService.saveSoDetail(soDetailEntity);
            }

            // 合并商品明细
            BanQinWmSoDetail condition = new BanQinWmSoDetail();
            condition.setSoNo(soDetailEntity.getSoNo());
            condition.setOldLineNo(soDetailEntity.getOldLineNo());
            condition.setStatus(WmsCodeMaster.SO_NEW.getCode());
            condition.setOrgId(orgId);
            List<BanQinWmSoDetail> soDetailList = wmSoDetailService.findList(condition);
            soDetailList = soDetailList.stream().filter(d -> StringUtils.isEmpty(d.getCdType())).collect(Collectors.toList());
            soDetailList.sort(Comparator.comparing(BanQinWmSoDetail::getLineNo));
            if (soDetailList.size() > 1) {
                BanQinWmSoDetail oldModel = soDetailList.get(0);// 第一行合并保存
                Double qtySoEa = 0D;
                int removeIndex = 0;// 当前保存不参与删除操作(行号最小记录)
                for (int i = 0; i < soDetailList.size(); i++) {
                    BanQinWmSoDetail model = soDetailList.get(i);
                    if (oldModel.getLineNo().compareTo(model.getLineNo()) > 0) {
                        oldModel = model;// 获取行号最小记录
                        removeIndex = i;
                    }
                    qtySoEa += model.getQtySoEa();
                }
                oldModel.setQtySoEa(qtySoEa);// 取最小行号保存
                soDetailList.remove(removeIndex);
                // 删除创建行
                wmSoDetailService.deleteAll(soDetailList);
                wmSoDetailService.save(oldModel);// 保存最原始行
            }
        }
        return msg;
    }

}
