package com.yunyou.modules.wms.outbound.service;

import com.yunyou.modules.interfaces.interactive.service.SynchronizeNoService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoEntity;
import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinOutboundCloseEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出库管理 关闭、取消方法类 
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCloseOrCancelService {
    // 公共方法
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 出库管理公共方法
    @Autowired
    protected BanQinOutboundCommonService outboundCommon;
    // 出库单
    @Autowired
    protected BanQinWmSoHeaderService wmSoHeaderService;
    // 出库单明细
    @Autowired
    protected BanQinWmSoDetailService wmSoDetailService;
    // 装车单明细
    @Autowired
    protected BanQinWmLdDetailService wmLdDetailService;
    // 同步面单号
    @Autowired
    private SynchronizeNoService synchronizeNoService;

    /**
     * 出库单关闭
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    @Transactional
    public ResultMessage close(String soNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 获取出库单
        BanQinWmSoHeader model = wmSoHeaderService.findBySoNo(soNo, orgId);
        // 拦截状态、冻结状态，不能关闭
        check(model);
        // 如果订单有预配数、分配数、拣货数，那么提示不可关闭
        List<BanQinWmSoDetail> soDetailList = wmSoDetailService.findBySoNo(soNo, orgId);
        double qtyOpEa = soDetailList.stream().map(d -> d.getQtyPreallocEa() + d.getQtyAllocEa() + d.getQtyPkEa()).mapToDouble(Double::doubleValue).sum();
        if (qtyOpEa > 0) {
            throw new WarehouseException(soNo + "存在预配数或分配数或拣货数，不能操作");
        }
        // SO参数：是否只有SO完全发运才可以关闭SO（Y：必须是完全发运；N：部分发运或完全发运）
        String soOnlyFullShipClose = SysControlParamsUtils.getValue(ControlParamCode.SO_ONLY_FULL_SHIP_CLOSE.getCode(), orgId);
        // 如果soOnlyFullShipClose = N,状态<80，那么不能关闭
        if (WmsConstants.YES.equals(soOnlyFullShipClose) && model.getStatus().compareTo(WmsCodeMaster.SO_FULL_SHIPPING.getCode()) < 0) {
            throw new WarehouseException("订单" + soNo + "未完全发运");
        }
        // 校验生成装车单的分配是否都完全交接
        if (wmLdDetailService.CheckIsFullDeliveryBySoNo(soNo, orgId)) {
            throw new WarehouseException(soNo + "生成装车单的分配拣货明细未完全交接");
        }
        // 订单状态 70/80时关闭
        if (model.getStatus().equals(WmsCodeMaster.SO_PART_SHIPPING.getCode()) || model.getStatus().equals(WmsCodeMaster.SO_FULL_SHIPPING.getCode())) {
            // 状态更新close
            model.setStatus(WmsCodeMaster.SO_CLOSE.getCode());
            this.wmSoHeaderService.save(model);
        } else {
            throw new WarehouseException(soNo + "非部分发运、非完全发运状态，不能操作");
        }
        // 回填SALE单的已生成SO数
        List<BanQinWmSoDetail> list = wmSoDetailService.findBySoNo(soNo, orgId);
        // 循环商品明细
        for (BanQinWmSoDetail wmSoDetailModel : list) {
            outboundCommon.backfillQtySo(wmSoDetailModel);
        }
        // SO参数：订单关闭后，是否同步更新面单号到OMS、TMS（Y：同步；N：不同步）
        final String soSyncMailNo = SysControlParamsUtils.getValue(ControlParamCode.SO_SYNC_MAIL_NO.getCode(), orgId);
        if (WmsConstants.YES.equals(soSyncMailNo)) {
            BanQinWmSoEntity entity = wmSoHeaderService.findEntityBySoNo(soNo, orgId);
            if (entity != null) {
                new Thread(() -> synchronizeNoService.synchroWayBillNo(entity.getDef3(), soNo, orgId, entity.getCarrierCode(), entity.getCarrierName())).start();
            }
        }
        // 构造对象
        BanQinOutboundCloseEntity entity = new BanQinOutboundCloseEntity();
        entity.setWmSoHeaderModel(model);
        // 返回信息，数据
        msg.setSuccess(true);
        msg.setData(entity);
        return msg;
    }

    /**
     * 出库单取消
     *
     * @param soNo  出库单号
     * @param orgId 机构ID
     */
    @Transactional
    public void cancelSo(String soNo, String orgId) {
        // 获取出库单
        BanQinWmSoHeader model = wmSoHeaderService.findBySoNo(soNo, orgId);
        // 拦截状态、冻结状态,不能取消
        check(model);
        // 订单状态00，但审核，不能取消
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            throw new WarehouseException("订单" + soNo + "已审核");
        }
        // 如果订单不是新增状态，不能取消
        if (model.getStatus().compareTo(WmsCodeMaster.SO_NEW.getCode()) > 0) {
            throw new WarehouseException(soNo + "不是创建状态，不能操作");
        }
        ResultMessage msg = wmSoDetailService.checkSoExistCd(new String[]{soNo}, orgId);
        Object[] soNos = (Object[]) msg.getData();
        if (soNos.length <= 0) {
            throw new WarehouseException("存在越库标记的明细，不能操作");
        }
        // 状态更新取消
        model.setStatus(WmsCodeMaster.SO_CANCEL.getCode());
        // 保存
        this.wmSoHeaderService.save(model);
        // 更新波次单明细行状态 = 出库单状态
        outboundCommon.updateWvDetailStatus(soNo, orgId);
        // 更新波次单状态
        outboundCommon.updateWvHeaderStatus(ProcessByCode.BY_SO.getCode(), Lists.newArrayList(soNo), orgId);
        // 回填SALE单的已生成SO数
        List<BanQinWmSoDetail> list = wmSoDetailService.findBySoNo(soNo, orgId);
        // 循环商品明细
        for (BanQinWmSoDetail wmSoDetailModel : list) {
            outboundCommon.backfillQtySo(wmSoDetailModel);
        }
    }

    /**
     * 出库单明细取消
     *
     * @param soNo   出库单号
     * @param lineNo 出库单明细行号
     * @param orgId  机构ID
     */
    @Transactional
    public void cancelSoDetail(String soNo, String lineNo, String orgId) {
        // 获取出库单
        BanQinWmSoHeader model = wmSoHeaderService.findBySoNo(soNo, orgId);
        // 拦截状态、冻结状态,不能取消
        check(model);
        // 获取出库单明细行
        BanQinWmSoDetail detailModel = wmSoDetailService.findBySoNoAndLineNo(soNo, lineNo, orgId);
        // 如果订单不是新增状态，不能取消
        if (detailModel.getStatus().compareTo(WmsCodeMaster.SO_NEW.getCode()) > 0) {
            throw new WarehouseException("订单" + soNo + "行号" + lineNo + "不是新增状态");
        }
        if (StringUtils.isNotEmpty(detailModel.getCdType())) {
            throw new WarehouseException(soNo + lineNo + "已被越库标记，不能操作");
        }
        // 回填SALE单的已生成SO数
        outboundCommon.backfillQtySo(detailModel);
        // 订单明细行状态更新取消
        detailModel.setStatus(WmsCodeMaster.SO_CANCEL.getCode());
        // 保存
        this.wmSoDetailService.save(detailModel);
        // 构造入参
        List<String> noList = Lists.newArrayList(soNo);
        // 2、更新出库单状态
        outboundCommon.updateSoHeaderStatus(ProcessByCode.BY_SO.getCode(), noList, orgId);
        // 3、更新波次单状态,波次单明细行状态 = 出库单状态
        outboundCommon.updateWvDetailStatus(soNo, orgId);
        // 4、更新波次单状态
        outboundCommon.updateWvHeaderStatus(ProcessByCode.BY_SO.getCode(), noList, orgId);
    }

    /**
     * 校验
     *
     * @param model 出库单
     */
    protected void check(BanQinWmSoHeader model) {
        // 如果是拦截状态，那么不能关闭
        if (model.getInterceptStatus().equals(WmsCodeMaster.ITC_INTERCEPT.getCode()) || model.getInterceptStatus().equals(WmsCodeMaster.ITC_WAITING.getCode())
                || model.getInterceptStatus().equals(WmsCodeMaster.ITC_SUCCESS.getCode())) {
            throw new WarehouseException("订单" + model.getSoNo() + "已被拦截");
        }
        // 冻结状态
        if (model.getHoldStatus().equals(WmsCodeMaster.ODHL_HOLD.getCode())) {
            throw new WarehouseException("订单" + model.getSoNo() + "已被冻结");
        }
    }
}