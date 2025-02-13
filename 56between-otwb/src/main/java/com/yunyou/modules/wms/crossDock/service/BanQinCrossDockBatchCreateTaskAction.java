package com.yunyou.modules.wms.crossDock.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.crossDock.entity.BanQinGetCrossDockQueryEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceiveEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnReceiveByCdQuery;
import com.yunyou.modules.wms.inbound.service.BanQinWmAsnDetailReceiveService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailByCdQuery;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量生成越库(绑定ASN与SO的越库关系)
 * @author WMJ
 * @version 2020-02-17
 */
@Service
public class BanQinCrossDockBatchCreateTaskAction {
    @Autowired
    protected BanQinWmAsnDetailReceiveService wmAsnDetailReceiveService;
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    @Autowired
    protected BanQinCrossDockCreateTaskService crossDockCreateTaskService;

    /**
     * 直接越库，按商品生成越库任务
     */
    public ResultMessage crossDockBatchCreateTaskBySku(String ownerCode, String skuCode, BanQinGetCrossDockQueryEntity skuCondition) {
        ResultMessage msg = new ResultMessage();
        // 按货主、商品获取【创建状态】的ASN收货明细和SO商品明细Entity，
        // 无越库标记、按预计到/出货时间到升序、优先级升序、订单创建时间升序,并且未冻结，未拦截
        BanQinWmAsnReceiveByCdQuery asnCondition = new BanQinWmAsnReceiveByCdQuery();
        BeanUtils.copyProperties(skuCondition, asnCondition);
        asnCondition.setOwnerCode(ownerCode);
        asnCondition.setSkuCode(skuCode);
        asnCondition.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 创建状态
        asnCondition.setIsCd(WmsConstants.YES);// 未越库
        List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys = wmAsnDetailReceiveService.getEntityByCdAndSku(asnCondition);
        if (asnReceiveEntitys.size() == 0) {
            msg.addMessage("货主[" + ownerCode + "]商品[" + skuCode + "没有可生成越库任务的收货明细");
            msg.setSuccess(false);
            return msg;
        }
        // 当出货时间、优先级、订单时间相同时，按订货数从大到小排序,未生成波次计划,未生成装车单
        BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
        BeanUtils.copyProperties(skuCondition, soCondition);
        soCondition.setOwnerCode(ownerCode);
        soCondition.setSkuCode(skuCode);
        soCondition.setStatus(WmsCodeMaster.SO_NEW.getCode());// 创建状态
        soCondition.setIsCd(WmsConstants.YES);// 未越库
        List<BanQinWmSoDetailEntity> soDetailEntitys = wmSoDetailService.getEntityByCdAndSku(soCondition);
        if (soDetailEntitys.size() == 0) {
            msg.addMessage("货主[" + ownerCode + "]商品[" + skuCode + "]没有可生成越库任务的出库单商品明细");
            msg.setSuccess(false);
            return msg;
        }
        // 一层循环SO商品明细
        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            try {
                // 出库相当于进行手工分配，不考虑出库单的分配规则和库存周转规则
                ResultMessage asnMsg = crossDockCreateTaskService.crossDockCreateTask(soDetailEntity, asnReceiveEntitys);
                asnReceiveEntitys = (List<BanQinWmAsnDetailReceiveEntity>) asnMsg.getData();
                if (asnReceiveEntitys.size() == 0) {
                    break;// 无可匹配的收货明细
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 直接越库，按明细行生成越库任务
     */
    public ResultMessage crossDockBatchCreateTaskByDirect(List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys, List<BanQinWmSoDetailEntity> soDetailEntitys, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 按货主、商品获取【创建状态】的ASN收货明细和SO商品明细Entity，
        // 无越库标记、按预计到/出货时间到升序、优先级升序、订单创建时间升序,并且未冻结，未拦截
        // 按单号+行号组合条件查询
        List<String> noList = new ArrayList<>();
        for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
            noList.add("('" + asnReceiveEntity.getAsnNo() + "','" + asnReceiveEntity.getLineNo() + "')");
        }
        asnReceiveEntitys = wmAsnDetailReceiveService.getEntityByCdAndLineNo(noList, WmsCodeMaster.ASN_NEW.getCode(), WmsConstants.YES, null, orgId);

        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            noList.add("('" + soDetailEntity.getSoNo() + "','" + soDetailEntity.getLineNo() + "')");
        }
        soDetailEntitys = wmSoDetailService.getEntityByCdAndLineNo(noList, WmsCodeMaster.SO_NEW.getCode(), WmsConstants.YES, null, orgId);
        // 一层循环SO商品明细
        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            try {
                // 出库相当于进行手工分配，不考虑出库单的分配规则和库存周转规则
                ResultMessage asnMsg = crossDockCreateTaskService.crossDockCreateTask(soDetailEntity, asnReceiveEntitys);
                asnReceiveEntitys = (List<BanQinWmAsnDetailReceiveEntity>) asnMsg.getData();
                if (asnReceiveEntitys.size() == 0) {
                    break;// 无可匹配的收货明细
                }
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 分拨越库，按商品生成越库任务，按商品越库标记录
     */
    public ResultMessage crossDockBatchCreateTaskBySkuInDirect(String ownerCode, String skuCode, BanQinGetCrossDockQueryEntity skuCondition) {
        ResultMessage msg = new ResultMessage();
        // 按货主、商品获取【创建状态】的ASN收货明细和SO商品明细Entity，
        // 无越库标记、按预计到/出货时间到升序、优先级升序、订单创建时间升序,并且未冻结，未拦截
        BanQinWmAsnReceiveByCdQuery asnCondition = new BanQinWmAsnReceiveByCdQuery();
        BeanUtils.copyProperties(skuCondition, asnCondition);
        asnCondition.setOwnerCode(ownerCode);
        asnCondition.setSkuCode(skuCode);
        asnCondition.setStatus(WmsCodeMaster.ASN_NEW.getCode());// 创建状态
        asnCondition.setIsCd(WmsConstants.YES);// 未越库
        List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys = wmAsnDetailReceiveService.getEntityByCdAndSku(asnCondition);
        // 当出货时间、优先级、订单时间相同时，按订单按订单号、行号升序,未生成波次计划,未生成装车单
        BanQinWmSoDetailByCdQuery soCondition = new BanQinWmSoDetailByCdQuery();
        BeanUtils.copyProperties(skuCondition, soCondition);
        soCondition.setOwnerCode(ownerCode);
        soCondition.setSkuCode(skuCode);
        soCondition.setStatus(WmsCodeMaster.SO_NEW.getCode());// 创建状态
        soCondition.setIsCd(WmsConstants.YES);// 未越库
        List<BanQinWmSoDetailEntity> soDetailEntitys = wmSoDetailService.getEntityByCdAndSku(soCondition);
        // 循环ASN收货明细
        for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
            try {
                // 更新收货明细
                crossDockCreateTaskService.splitWmAsnDetailReceive(asnReceiveEntity, 0D, WmsCodeMaster.CD_TYPE_INDIRECT.getCode(), null, null);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        // 循环出库单商品行
        for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
            try {
                // 更新出库商品行
                crossDockCreateTaskService.splitWmSoDetail(soDetailEntity, 0D, WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }

        return msg;
    }

    /**
     * 分拨越库，选择收货明细和出库商品明细 匹配规则： 以当前收货订单的数量按出库单时间再按订单优先级依次匹配出库单，
     * 没有预计发货时间到升序、优先级升序、下单时间升序匹配，如果都一样则订单号升序顺序匹配
     */
    public ResultMessage crossDockBatchCreateTaskByInDirect(List<BanQinWmAsnDetailReceiveEntity> asnReceiveEntitys, List<BanQinWmSoDetailEntity> soDetailEntitys, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 按货主、商品获取【创建状态】的ASN收货明细和SO商品明细Entity，
        // 无越库标记、按出货时间到升序、优先级升序、订单创建时间升序,未冻结、未拦截
        // 按单号+行号组合条件查询
        if (CollectionUtil.isNotEmpty(asnReceiveEntitys)) {
            List<String> noList = new ArrayList<>();
            for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
                noList.add("('" + asnReceiveEntity.getAsnNo() + "','" + asnReceiveEntity.getLineNo() + "')");
            }
            asnReceiveEntitys = wmAsnDetailReceiveService.getEntityByCdAndLineNo(noList, WmsCodeMaster.ASN_NEW.getCode(), WmsConstants.YES, null, orgId);
            // 循环ASN收货明细
            for (BanQinWmAsnDetailReceiveEntity asnReceiveEntity : asnReceiveEntitys) {
                try {
                    // 更新收货明细
                    crossDockCreateTaskService.splitWmAsnDetailReceive(asnReceiveEntity, 0D, WmsCodeMaster.CD_TYPE_INDIRECT.getCode(), null, null);
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(soDetailEntitys)) {
            List<String> noList = new ArrayList<>();
            for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
                noList.add("('" + soDetailEntity.getSoNo() + "','" + soDetailEntity.getLineNo() + "')");
            }
            soDetailEntitys = wmSoDetailService.getEntityByCdAndLineNo(noList, WmsCodeMaster.SO_NEW.getCode(), WmsConstants.YES, null, orgId);
            // 循环出库单商品行
            for (BanQinWmSoDetailEntity soDetailEntity : soDetailEntitys) {
                try {
                    // 更新出库商品行
                    crossDockCreateTaskService.splitWmSoDetail(soDetailEntity, 0D, WmsCodeMaster.CD_TYPE_INDIRECT.getCode());
                } catch (WarehouseException e) {
                    msg.addMessage(e.getMessage());
                    msg.setSuccess(false);
                }
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

}
