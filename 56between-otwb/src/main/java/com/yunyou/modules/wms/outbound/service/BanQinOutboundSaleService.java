package com.yunyou.modules.wms.outbound.service;

import java.util.Arrays;
import java.util.List;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleDetailEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleHeader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 销售订单操作类
 *
 * @author WMJ
 * @version 2019/02/26
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundSaleService {
    @Autowired
    protected BanQinWmSaleHeaderService wmSaleHeaderService;
    @Autowired
    protected BanQinWmSaleDetailService wmSaleDetailService;
    @Autowired
    protected BanQinOutboundCreateSoService outboundCreateSoService;
    @Autowired
    protected BanQinOutboundDuplicateService outboundDuplicateService;

    /**
     * 保存Sale单
     *
     * @param wmSaleEntity
     * @return
     */
    @Transactional
    public ResultMessage saveSaleEntity(BanQinWmSaleEntity wmSaleEntity) {
        BanQinWmSaleHeader model = new BanQinWmSaleHeader();
        BeanUtils.copyProperties(wmSaleEntity, model);
        return this.wmSaleHeaderService.saveSaleHeader(model);
    }

    /**
     * 保存Sale明细
     *
     * @param wmSaleDetailEntity
     * @return
     */
    @Transactional
    public ResultMessage saveSaleDetailEntity(BanQinWmSaleDetailEntity wmSaleDetailEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSaleHeader wmSaleHeaderModel = wmSaleHeaderService.getBySaleNo(wmSaleDetailEntity.getSaleNo(), wmSaleDetailEntity.getOrgId());
        if (!WmsCodeMaster.SALE_NEW.getCode().equals(wmSaleHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmSaleDetailEntity.getSaleNo() + "不是创建状态，不能操作");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmSaleHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmSaleDetailEntity.getSaleNo() + "已审核，不能操作");
            return msg;
        }
        BanQinWmSaleDetail model = new BanQinWmSaleDetail();
        BeanUtils.copyProperties(wmSaleDetailEntity, model);
        return this.wmSaleDetailService.saveSaleDetail(model);
    }

    /**
     * 删除Sale单 审核后不能删除
     *
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage removeSaleEntity(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
        // 校验状态
        ResultMessage checkMsg = wmSaleHeaderService.checkSaleStatus(saleNos, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()},
                new String[]{WmsCodeMaster.SALE_NEW.getCode()}, orgId);
        Object[] checkSaleNos = (Object[]) checkMsg.getData();
        if (checkSaleNos.length > 0) {
            // 校验是否存在SO单
            errorMsg = wmSaleHeaderService.checkSaleIsExistSo(Arrays.asList(checkSaleNos).toArray(new String[]{}), orgId);
            Object[] updateSaleNos = (Object[]) errorMsg.getData();
            if (updateSaleNos.length > 0) {
                wmSaleDetailService.removeBySaleNo(Arrays.asList(updateSaleNos).toArray(new String[]{}), orgId);
                wmSaleHeaderService.removeBySaleNo(Arrays.asList(updateSaleNos).toArray(new String[]{}), orgId);
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage(checkMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage(errorMsg.getMessage() + "已生成SO单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        // 删除单头
        return msg;
    }

    /**
     * 删除Sale明细 审核后不能删除
     * @param saleNo
     * @param lineNos
     * @return
     */
    @Transactional
    public ResultMessage removeSaleDetailEntity(String saleNo, String[] lineNos, String orgId) {
        // 删除明细
        return wmSaleDetailService.removeBySaleNoAndLineNo(saleNo, lineNos, orgId);
    }

    /**
     * 通过Sale号获得单个Sale单
     * @param saleNo
     * @return
     */
    public BanQinWmSaleEntity getSaleEntityBySaleNo(String saleNo, String orgId) {
        BanQinWmSaleEntity entity = wmSaleHeaderService.getEntityBySaleNo(saleNo, orgId);
        entity.setWmSaleDetailList(wmSaleDetailService.getEntityBySaleNo(saleNo, orgId));
        return entity;
    }

    /**
     * 根据单号和行号，查询订单明细entity
     * @param saleNo
     * @param lineNo
     * @return
     */
    public BanQinWmSaleDetailEntity getSaleDetailEntityBySaleNoAndLineNo(String saleNo, String lineNo, String orgId) {
        return wmSaleDetailService.findEntityBySaleNoAndLineNo(saleNo, lineNo, orgId);
    }

    /**
     * 根据单号，查询订单未取消的明细entity
     * @param saleNos
     * @return
     */
    public List<BanQinWmSaleDetailEntity> getSaleDetailEntityBySaleNo(String[] saleNos, String orgId) {
        return wmSaleDetailService.getDetailBySaleNos(saleNos, orgId);
    }

    /**
     * 审核 审核后不能修改
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage auditSale(String[] saleNos, String orgId) {
        return wmSaleHeaderService.audit(saleNos, orgId);
    }

    /**
     * 取消审核 不能生成生成SO
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage cancelAuditSale(String[] saleNos, String orgId) {
        return wmSaleHeaderService.cancelAudit(saleNos, orgId);
    }

    /**
     * 取消Sale单 创建状态可以取消，取消后不能进行其他操作
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage cancelSale(String[] saleNos, String orgId) {
        return wmSaleHeaderService.cancel(saleNos, orgId);
    }

    /**
     * 取消Sale明细行
     * @param saleNo
     * @param lineNos
     * @return
     */
    @Transactional
    public ResultMessage cancelSaleDetail(String saleNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
        // 校验状态
        ResultMessage checkMsg = wmSaleDetailService.checkSaleDetailStatus(saleNo, null, null, lineNos, new String[]{WmsCodeMaster.SALE_NEW.getCode()}, orgId);
        Object[] checkLineNos = (Object[]) checkMsg.getData();
        if (checkLineNos.length > 0) {
            // 校验是否存在SO单
            errorMsg = wmSaleDetailService.checkSaleIsExistSo(saleNo, Arrays.asList(checkLineNos).toArray(new String[]{}), orgId);
            Object[] updateLineNos = (Object[]) errorMsg.getData();
            if (updateLineNos.length > 0) {
                List<BanQinWmSaleDetail> list = wmSaleDetailService.getBySaleNoAndLineNoArray(saleNo, Arrays.asList(updateLineNos).toArray(new String[]{}), orgId);
                for (BanQinWmSaleDetail wmSaleDetailModel : list) {
                    wmSaleDetailService.cancel(wmSaleDetailModel);
                }
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage("不是创建状态，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage("已生成SO单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 关闭Sale单 SO单关闭后，才能关闭Sale单
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage closeSale(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 校验状态
        ResultMessage checkMsg = wmSaleHeaderService.checkSaleStatus(saleNos, null, new String[]{WmsCodeMaster.SALE_FULL_SHIPPING.getCode(), WmsCodeMaster.SALE_PART_SHIPPING.getCode()}, orgId);
        Object[] checkSaleNos = (Object[]) checkMsg.getData();
        if (checkSaleNos.length > 0) {
            List<BanQinWmSaleHeader> list = wmSaleHeaderService.getBySaleNoArray(Arrays.asList(checkSaleNos).toArray(new String[]{}), orgId);
            for (BanQinWmSaleHeader model : list) {
                try {
                    wmSaleHeaderService.close(model);
                } catch (WarehouseException e) {
                    msg.addMessage(model.getSaleNo() + e.getMessage());
                    continue;
                }
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage("不是发运状态，不能操作");
        }
        if (StringUtils.isEmpty(msg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        return msg;
    }

    /**
     * 复制Sale
     * @param saleNo
     * @return
     */
    @Transactional
    public ResultMessage duplicateSale(String saleNo, String orgId) {
        return outboundDuplicateService.duplicateSale(saleNo, orgId);
    }

    /**
     * 复制Sale明细
     * @param saleNo
     * @param lineNo
     * @return
     */
    @Transactional
    public ResultMessage duplicateSaleDetail(String saleNo, String lineNo, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSaleHeader wmSaleHeaderModel = wmSaleHeaderService.getBySaleNo(saleNo, orgId);
        if (!WmsCodeMaster.SALE_NEW.getCode().equals(wmSaleHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(saleNo + "不是创建状态，不能操作");
            return msg;
        }
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmSaleHeaderModel.getAuditStatus())) {
            msg.setSuccess(false);
            msg.addMessage(saleNo + "已审核，不能操作");
            return msg;
        }
        BanQinWmSaleDetail wmSaleDetailModel = wmSaleDetailService.findBySaleNoAndLineNo(saleNo, lineNo, orgId);
        BanQinWmSaleDetail newModel = new BanQinWmSaleDetail();
        if (null != wmSaleDetailModel) {
            wmSaleDetailModel.setId(IdGen.uuid());
            ResultMessage message = wmSaleDetailService.saveSaleDetail(wmSaleDetailModel);
            if (message.isSuccess()) {
                newModel = (BanQinWmSaleDetail) message.getData();
            }
        } else {
            msg.setSuccess(false);
            msg.addMessage(saleNo + lineNo + "不存在");
            return msg;
        }
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        msg.setData(this.getSaleDetailEntityBySaleNoAndLineNo(saleNo, newModel.getLineNo(), orgId));
        return msg;
    }

    /**
     * 可选多个同货主，同收货人的Sale单，生成一个SO单
     * @param ownerCode
     * @param consigneeCode
     * @param wmSaleDetailEntitys
     * @return
     */
    @Transactional
    public ResultMessage createSo(String ownerCode, String consigneeCode, List<BanQinWmSaleDetailEntity> wmSaleDetailEntitys) {
        ResultMessage msg = new ResultMessage();
        try {
            msg = outboundCreateSoService.outboundCreateSo(ownerCode, consigneeCode, wmSaleDetailEntitys);
        } catch (WarehouseException e) {
            msg.setSuccess(false);
            msg.addMessage(e.getMessage());
        }
        return msg;
    }
}
