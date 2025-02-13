package com.yunyou.modules.wms.inbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnHeader;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmAsnDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入库单明细Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAsnDetailService extends CrudService<BanQinWmAsnDetailMapper, BanQinWmAsnDetail> {
    @Autowired
    @Lazy
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    @Lazy
    private BanQinWmAsnHeaderService banQinWmAsnHeaderService;
    @Autowired
    private BanQinWmAsnDetailReceiveService banQinWmAsnDetailReceiveService;
    @Autowired
    private WmsUtil wmsUtil;

    public BanQinWmAsnDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmAsnDetail> findList(BanQinWmAsnDetail banQinWmAsnDetail) {
        return super.findList(banQinWmAsnDetail);
    }

    public Page<BanQinWmAsnDetailEntity> findPage(Page page, BanQinWmAsnDetailEntity banQinWmAsnDetail) {
        dataRuleFilter(banQinWmAsnDetail);
        banQinWmAsnDetail.setPage(page);
        List<BanQinWmAsnDetailEntity> list = mapper.findPage(banQinWmAsnDetail);
        page.setList(list);
        for (BanQinWmAsnDetailEntity entity : list) {
            entity.setQtyAsnUom(entity.getQtyAsnEa() / entity.getUomQty());
            entity.setQtyRcvUom(entity.getQtyRcvEa() / entity.getUomQty());
        }
        return page;
    }

    @Transactional
    public void save(BanQinWmAsnDetail banQinWmAsnDetail) {
        super.save(banQinWmAsnDetail);
    }

    @Transactional
    public void delete(BanQinWmAsnDetail banQinWmAsnDetail) {
        super.delete(banQinWmAsnDetail);
    }
    
    /**
     * 描述： 根据ASN单号更新物流单号
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
     * 描述： 根据ASN单号+行号获取明细
     *
     * @param asnNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public BanQinWmAsnDetailEntity getByAsnNoAndLineNo(String asnNo, String lineNo, String orgId) {
        return mapper.getByAsnNoAndLineNo(asnNo, lineNo, orgId);
    }

    /**
     * 描述： 根据ASN单号+行号获取明细
     *
     * @param asnNo
     * @param lineNos
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmAsnDetail> getByAsnNoAndLineNos(String asnNo, String[] lineNos, String orgId) {
        return mapper.getByAsnNoAndLineNos(asnNo, lineNos, orgId);
    }

    /**
     * 描述： 根据ASN单号获取明细Entity
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmAsnDetailEntity> getEntityByAsnNo(String asnNo, String orgId) {
        List<BanQinWmAsnDetailEntity> entities = Lists.newArrayList();
        List<BanQinWmAsnDetail> wmAsnDetails = this.getByAsnNo(asnNo, orgId);
        for (BanQinWmAsnDetail wmAsnDetail : wmAsnDetails) {
            BanQinWmAsnDetailEntity entity = new BanQinWmAsnDetailEntity();
            BeanUtils.copyProperties(wmAsnDetail, entity);
            // 未完成
            entity.setWmAsnDetailReceiveEntities(banQinWmAsnDetailReceiveService.getEntityByAsnNoAndAsnLineNo(asnNo, wmAsnDetail.getLineNo(), orgId));
            entities.add(entity);
        }
        return entities;
    }

    /**
     * 描述： 根据ASN单号+行号获取明细Entity
     *
     * @param asnNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public BanQinWmAsnDetailEntity getEntityByAsnNoAndLineNo(String asnNo, String lineNo, String orgId) {
        BanQinWmAsnDetailEntity entity = this.getByAsnNoAndLineNo(asnNo, lineNo, orgId);
        entity.setWmAsnDetailReceiveEntities(banQinWmAsnDetailReceiveService.getEntityByAsnNoAndAsnLineNo(asnNo, lineNo, orgId));
        return entity;
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
     * 描述： 根据ASN单号获取明细
     *
     * @param asnNo
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmAsnDetail> getByAsnNo(String asnNo, String orgId) {
        return this.getByAsnNoAndLineNos(asnNo, null, orgId);
    }

    /**
     * 描述： 通过采购单号和行号查询ASN明细
     *
     * @param poNo
     * @param poLineNo
     * @param orgId
     * @author Jianhua on 2019/1/28
     */
    public List<BanQinWmAsnDetail> getByPoNoAndPoLineNo(String poNo, String poLineNo, String orgId) {
        BanQinWmAsnDetail example = new BanQinWmAsnDetail();
        example.setPoNo(poNo);
        example.setPoLineNo(poLineNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述：获取未生成质检单/质检完成的商品行号
     *
     * @param asnId
     * @param lineNos
     * @param qcStatus
     * @author Jianhua on 2019/1/26
     */
    public List<String> getCheckQcLine(String asnId, String[] lineNos, String qcStatus) {
        return mapper.getCheckQcLine(asnId, lineNos, qcStatus);
    }

    /**
     * Description:根据货主和商品获取WmAsnDetailModel
     *
     * @param ownerCode
     * @param skuCode
     * @return
     * @Author:Christian
     * @Creater Date:2014-7-25
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmAsnDetail newModel = new BanQinWmAsnDetail();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        List<BanQinWmAsnDetail> list = mapper.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            msg.setSuccess(false);
            msg.setData(list.get(0));
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 查询ASN明细
     *
     * @param poNo  PO单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmAsnDetail> findByPoNo(String poNo, String orgId) {
        BanQinWmAsnDetail condition = new BanQinWmAsnDetail();
        condition.setPoNo(poNo);
        condition.setOrgId(orgId);
        return mapper.findList(condition);
    }

    /**
     * 描述：校验PO生成的ASN单是否全部关闭或取消
     *
     * @param poNo
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public boolean checkAllCancelOrCloseByPoNo(String poNo, String orgId) {
        List<BanQinWmAsnDetail> wmAsnDetails = this.findByPoNo(poNo, orgId);
        for (BanQinWmAsnDetail detail : wmAsnDetails) {
            if (!WmsCodeMaster.ASN_CANCEL.getCode().equals(detail.getStatus())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 描述： 保存ASN明细：新增ASN明细时，同时新增收货明细
     *
     * @param wmAsnDetailEntity
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage saveAsnDetailEntity(BanQinWmAsnDetailEntity wmAsnDetailEntity) {
        return banQinInboundOperationService.saveAsnDetailEntity(wmAsnDetailEntity);
    }

    /**
     * 描述： 删除ASN明细及收货明细
     *
     * @param asnId
     * @param lineNos
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage removeAsnDetailEntity(String asnId, String[] lineNos) {
        ResultMessage msg = new ResultMessage();

        BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
        ResultMessage errorMsg = banQinInboundOperationService.checkAsnDetailStatus(asnId, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, lineNos, new String[]{WmsCodeMaster.ASN_NEW.getCode()},
                new String[]{WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()}, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()}, WmsConstants.NO, WmsConstants.NO, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = banQinInboundOperationService.checkAsnDetailExistQC(asnId, updateLineNos, null);
        updateLineNos = (String[]) isQcMsg.getData();
        if (updateLineNos.length > 0) {
            List<BanQinWmAsnDetail> detailList = this.getByAsnNoAndLineNos(wmAsnHeader.getAsnNo(), updateLineNos, wmAsnHeader.getOrgId());
            for (BanQinWmAsnDetail detail : detailList) {
                banQinInboundOperationService.removeAsnDetailEntity(detail);
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
     * 描述： 取消ASN单明细
     *
     * @param asnId
     * @param lineNos
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public ResultMessage cancelAsnDetail(String asnId, String[] lineNos) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnDetailStatus(asnId, null, lineNos, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, null, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()},
                WmsConstants.NO, WmsConstants.NO, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = banQinInboundOperationService.checkAsnDetailExistQC(asnId, updateLineNos, null);
        updateLineNos = (String[]) isQcMsg.getData();
        if (updateLineNos.length > 0) {
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
            for (String lineNo : updateLineNos) {
                banQinInboundOperationService.cancelAsnDetail(this.getByAsnNoAndLineNo(wmAsnHeader.getAsnNo(), lineNo, wmAsnHeader.getOrgId()));
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

    public List<String> checkAsnDetailStatusQuery(String asnId, String[] asnStatus, String[] lineNos, String[] lineStatus, String[] auditStatus,
                                                  String[] holdStatus, String isPalletize, String isCrossDock, String isArrangeLoc) {
        return mapper.checkAsnDetailStatusQuery(asnId, asnStatus, lineNos, lineStatus, auditStatus, holdStatus, isPalletize, isCrossDock, isArrangeLoc);
    }

    /**
     * 分摊重量
     */
    @Transactional
    public ResultMessage apportionWeight(String asnId, String[] lineNos) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = banQinInboundOperationService.checkAsnDetailStatus(asnId, null, lineNos, new String[]{WmsCodeMaster.ASN_NEW.getCode()}, null, new String[]{WmsCodeMaster.ODHL_NO_HOLD.getCode()},
                WmsConstants.NO, WmsConstants.NO, WmsConstants.NO);
        String[] updateLineNos = (String[]) errorMsg.getData();
        ResultMessage isQcMsg = banQinInboundOperationService.checkAsnDetailExistQC(asnId, updateLineNos, null);
        updateLineNos = (String[]) isQcMsg.getData();
        if (updateLineNos.length > 0) {
            BanQinWmAsnHeader wmAsnHeader = banQinWmAsnHeaderService.get(asnId);
            for (String lineNo : updateLineNos) {
                banQinInboundOperationService.apportionWeight(this.getByAsnNoAndLineNo(wmAsnHeader.getAsnNo(), lineNo, wmAsnHeader.getOrgId()));
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
}