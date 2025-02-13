package com.yunyou.modules.wms.inbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetail;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoDetailEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoHeader;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmPoDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购单明细Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPoDetailService extends CrudService<BanQinWmPoDetailMapper, BanQinWmPoDetail> {
    @Autowired
    @Lazy
    private BanQinWmPoHeaderService banQinWmPoHeaderService;
    @Autowired
    private BanQinWmsCommonService banQinWmsCommonService;
    @Autowired
    @Lazy
    private BanQinInboundOperationService banQinInboundOperationService;
    @Autowired
    private WmsUtil wmsUtil;

    public BanQinWmPoDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmPoDetail> findList(BanQinWmPoDetail banQinWmPoDetail) {
        return super.findList(banQinWmPoDetail);
    }
    
    public List<BanQinWmPoDetailEntity> findEntity(BanQinWmPoDetailEntity banQinWmPoDetailEntity) {
        List<BanQinWmPoDetailEntity> list = mapper.findEntity(banQinWmPoDetailEntity);
        setQtyUom(list);
        return list;
    }

    public Page<BanQinWmPoDetailEntity> findPage(Page page, BanQinWmPoDetailEntity banQinWmPoDetailEntity) {
        dataRuleFilter(banQinWmPoDetailEntity);
        banQinWmPoDetailEntity.setPage(page);
        List<BanQinWmPoDetailEntity> list = mapper.findPage(banQinWmPoDetailEntity);
        page.setList(list);
        setQtyUom(list);
        return page;
    }
    
    public void setQtyUom(List<BanQinWmPoDetailEntity> list) {
        for (BanQinWmPoDetailEntity entity : list) {
            entity.setQtyPoUom(entity.getQtyPoEa() / entity.getUomQty());
            entity.setQtyAsnUom(entity.getQtyAsnEa() / entity.getUomQty());
            entity.setQtyRcvUom(entity.getQtyRcvEa() / entity.getUomQty());
            entity.setCurrentQtyAsnEa(entity.getQtyPoEa() - entity.getQtyAsnEa());
            entity.setCurrentQtyAsnUom(entity.getCurrentQtyAsnEa() / entity.getUomQty());
        }
    }

    @Transactional
    public void save(BanQinWmPoDetail banQinWmPoDetail) {
        super.save(banQinWmPoDetail);
    }

    @Transactional
    public void delete(BanQinWmPoDetail banQinWmPoDetail) {
        super.delete(banQinWmPoDetail);
    }

    public BanQinWmPoDetail findFirst(BanQinWmPoDetail example) {
        List<BanQinWmPoDetail> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询PO单明细
     *
     * @param poNo     PO单号
     * @param poLineNo PO单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public BanQinWmPoDetail findByPoNoAndLineNo(String poNo, String poLineNo, String orgId) {
        BanQinWmPoDetail example = new BanQinWmPoDetail();
        example.setPoNo(poNo);
        example.setLineNo(poLineNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    /**
     * 描述： 查询PO单明细
     *
     * @param poNo    PO单号
     * @param lineNos PO单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/26
     */
    public List<BanQinWmPoDetail> findByPoNoAndLineNo(String poNo, String[] lineNos, String orgId) {
        return mapper.findByPoNoAndLineNo(poNo, lineNos, orgId);
    }

    /**
     * 描述： 查询PO单明细
     *
     * @param poNo  PO单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetail> findByPoNo(String poNo, String orgId) {
        BanQinWmPoDetail example = new BanQinWmPoDetail();
        example.setPoNo(poNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询PO单明细
     *
     * @param poId PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetail> findByPoId(String poId, String orgId) {
        BanQinWmPoDetail example = new BanQinWmPoDetail();
        example.setHeadId(poId);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询PO单明细
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetail> findByPoId(String[] poIds, String orgId) {
        List<BanQinWmPoDetail> list = Lists.newArrayList();
        for (String poId : poIds) {
            list.addAll(findByPoId(poId, orgId));
        }
        return list;
    }

    /**
     * 描述： 查询PO单明细Entity
     *
     * @param poId PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetailEntity> findEntityByPoId(String poId, String orgId) {
        BanQinWmPoDetailEntity entity = new BanQinWmPoDetailEntity();
        entity.setHeadId(poId);
        entity.setOrgId(orgId);
        return this.findEntity(entity);
    }

    /**
     * 描述： 查询PO单明细Entity
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetailEntity> findEntityByPoId(String[] poIds, String orgId) {
        List<BanQinWmPoDetailEntity> entityList = Lists.newArrayList();
        for (String poId : poIds) {
            entityList.addAll(findEntityByPoId(poId, orgId));
        }
        return entityList;
    }

    /**
     * 描述： 查询PO单明细Entity
     *
     * @param poNo  PO单号
     * @param poNo  PO单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmPoDetailEntity findEntityByPoNoAndLineNo(String poNo, String poLineNo, String orgId) {
        BanQinWmPoDetail wmPoDetail = findByPoNoAndLineNo(poNo, poLineNo, orgId);
        BanQinWmPoDetailEntity entity = (BanQinWmPoDetailEntity) wmPoDetail;
        return entity;
    }

    /**
     * 描述： 查询PO单明细Entity
     *
     * @param poNo  PO单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetailEntity> findEntityByPoNo(String poNo, String orgId) {
        BanQinWmPoDetailEntity entity = new BanQinWmPoDetailEntity();
        entity.setPoNo(poNo);
        entity.setOrgId(orgId);
        return this.findEntity(entity);
    }

    /**
     * 描述： 查询PO单明细Entity，过滤已取消的明细
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoDetailEntity> findEntityFilterCancelByPoId(String[] poIds, String orgId) {
        List<BanQinWmPoDetailEntity> entityList = Lists.newArrayList();

        List<BanQinWmPoDetailEntity> entities = findEntityByPoId(poIds, orgId);
        if (CollectionUtil.isNotEmpty(entities)) {
            for (BanQinWmPoDetailEntity entity : entities) {
                if (WmsCodeMaster.PO_CANCEL.getCode().equals(entity.getStatus())) {
                    continue;
                }
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * 描述： 查询已生成ASN的PO单明细
     *
     * @param poNo    PO单号
     * @param lineNos PO单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    private List<BanQinWmPoDetail> findExistsAsn(String poNo, String[] lineNos, String orgId) {
        return mapper.findExistsAsn(poNo, lineNos, orgId);
    }

    /**
     * 描述： 查询存在ASN单的PO明细
     *
     * @param poNo
     * @param lineNos
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public ResultMessage checkPoIsExistAsn(String poNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();

        List<String> lineNoList = Arrays.asList(lineNos);
        // 存在ASN的单号
        List<String> returnLineNos = Lists.newArrayList();
        List<BanQinWmPoDetail> existsAsn = findExistsAsn(poNo, lineNos, orgId);
        if (CollectionUtil.isNotEmpty(existsAsn)) {
            returnLineNos.addAll(existsAsn.stream().map(BanQinWmPoDetail::getLineNo).distinct().collect(Collectors.toList()));
        }
        List<String> minusLineNos = lineNoList.stream().filter(o -> !returnLineNos.contains(o)).collect(Collectors.toList());
        String str = "";
        for (String returnLineNo : returnLineNos) {
            str = str + returnLineNo + "\n";
        }
        msg.addMessage(str);
        msg.setData(minusLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 校验传入的PO的状态，返回符合状态的PO，提示不符的PO
     *
     * @param poNo
     * @param poStatus
     * @param auditStatus
     * @param lineNos
     * @param lineStatus
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public ResultMessage checkPoDetailStatus(String poNo, String[] poStatus, String[] auditStatus, String[] lineNos, String[] lineStatus, String orgId) {
        ResultMessage msg = new ResultMessage();

        List<String> poStatusList = null != poStatus ? Arrays.asList(poStatus) : Lists.newArrayList();
        List<String> auditStatusList = null != poStatus ? Arrays.asList(auditStatus) : Lists.newArrayList();
        List<String> lineStatusList = null != lineStatus ? Arrays.asList(lineStatus) : Lists.newArrayList();
        List<String> lineNoList = null != lineNos ? Arrays.asList(lineNos) : Lists.newArrayList();

        // 返回符合条件的PO单明细行号
        List<String> returnLineNos = Lists.newArrayList();
        BanQinWmPoHeader wmPoHeader = banQinWmPoHeaderService.findByPoNo(poNo, orgId);
        
        if ((CollectionUtil.isEmpty(poStatusList) && CollectionUtil.isEmpty(auditStatusList) || (poStatusList.contains(wmPoHeader.getStatus()) && auditStatusList.contains(wmPoHeader.getAuditStatus())))) {
            List<BanQinWmPoDetail> wmPoDetails = findByPoNoAndLineNo(poNo, lineNos, orgId);
            if (CollectionUtil.isNotEmpty(wmPoDetails)) {
                for (BanQinWmPoDetail detail : wmPoDetails) {
                    if (!lineStatusList.contains(detail.getStatus())) {
                        continue;
                    }
                    returnLineNos.add(detail.getLineNo());
                }
            }
        }
        
        // 不符合条件的PO单明细行号
        List<String> minusLineNos = lineNoList.stream().filter(o -> !returnLineNos.contains(o)).collect(Collectors.toList());
        String str = "";
        for (String minusLineNo : minusLineNos) {
            str = str + minusLineNo + "\n";
        }
        if (StringUtils.isNotEmpty(str)) {
            msg.addMessage(str);
        }
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 删除PO单明细
     *
     * @param poNo    PO单号
     * @param lineNos PO单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage removeByPoNoAndLineNo(String poNo, String[] lineNos, String orgId) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = new ResultMessage();
        BanQinWmPoHeader wmPoHeaderModel = banQinWmPoHeaderService.findByPoNo(poNo, orgId);
        if (!WmsCodeMaster.PO_NEW.getCode().equals(wmPoHeaderModel.getStatus())) {
            msg.addMessage(poNo + "不是创建状态，不能操作");// 不是创建状态，不能操作
            msg.setSuccess(false);
            return msg;
        }
        // 校验状态
        ResultMessage checkMsg = checkPoDetailStatus(poNo, new String[]{WmsCodeMaster.PO_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()}, lineNos,
                new String[]{WmsCodeMaster.PO_NEW.getCode()}, orgId);
        String[] checkLineNos = (String[]) checkMsg.getData();
        if (checkLineNos.length > 0) {
            // 校验是否存在ASN单
            errorMsg = checkPoIsExistAsn(poNo, checkLineNos, orgId);
            String[] updateLineNos = (String[]) errorMsg.getData();
            if (updateLineNos.length > 0) {
                mapper.removeByPoNoAndLineNo(poNo, updateLineNos, orgId);
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            // 非创建状态、已审核、已冻结的订单，不能操作
            msg.addMessage("行号" + checkMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            // "已生成ASN单，不能操作"
            msg.addMessage("行号" + errorMsg.getMessage() + "已生成ASN单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;

    }

    /**
     * 描述： 删除PO单明细
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void removeByPoId(String[] poIds) {
        for (String id : poIds) {
            BanQinWmPoHeader wmPoHeader = banQinWmPoHeaderService.get(id);
            mapper.removeByPoNoAndLineNo(wmPoHeader.getPoNo(), null, wmPoHeader.getOrgId());
        }
    }

    /**
     * 描述： 更新物流单号
     *
     * @param poNo       PO单号
     * @param logisticNo 新物流单号
     * @param orgId
     * @author Jianhua on 2019/1/25
     */
    @Transactional
    public void updateLogisticNo(String poNo, String logisticNo, String orgId) {
        mapper.updateLogisticNo(poNo, logisticNo, orgId);
    }

    /**
     * 描述： 统计PO单状态
     *
     * @param poNo  PO单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public String countPoStatus(String poNo, String orgId) {
        return mapper.countPoStatus(poNo, orgId);
    }

    /**
     * 描述： 保存采购明细
     *
     * @param model
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage savePoDetail(BanQinWmPoDetail model) {
        ResultMessage msg = new ResultMessage();
        // 新增时，取数据库最大行号
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(IdGen.uuid());
            String lineNo = wmsUtil.getMaxLineNo("wm_po_detail", "head_id", model.getHeadId());
            model.setLineNo(lineNo);
            model.setStatus(WmsCodeMaster.PO_NEW.getCode());
            model.setQtyAsnEa(0D);
            model.setQtyRcvEa(0D);
            model.setIsEdiSend(WmsConstants.NO);
            model.setEdiSendTime(null);
        }
        model.setPrice(model.getPrice() == null ? 0D : model.getPrice());
        /*
         * 生产日期、失效日期的逻辑校验
         */
        // 生产日期
        Date lotAtt01 = model.getLotAtt01();
        // 失效日期
        Date lotAtt02 = model.getLotAtt02();
        // 入库日期
        Date lotAtt03 = model.getLotAtt03();
        // 校验日期正确性
        try {
            this.banQinWmsCommonService.checkProductAndExpiryDate(lotAtt01, lotAtt02, lotAtt03);
        } catch (WarehouseException e) {
            msg.addMessage(e.getMessage());
            msg.setSuccess(false);
            return msg;
        }
        this.save(model);

        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;

    }

    /**
     * 描述： 取消PO单明细
     *
     * @param wmPoDetailModel
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void cancel(BanQinWmPoDetail wmPoDetailModel) {
        wmPoDetailModel.setStatus(WmsCodeMaster.PO_CANCEL.getCode());
        this.save(wmPoDetailModel);
        // 更新头状态
        banQinInboundOperationService.updatePoStatus(wmPoDetailModel.getPoNo(), wmPoDetailModel.getOrgId());
    }

    /**
     * 描述： 回填PO单的已生成ASN数
     *
     * @param wmAsnDetailModel
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void backfillQtyAsn(BanQinWmAsnDetail wmAsnDetailModel) {
        String poNo = wmAsnDetailModel.getPoNo();
        String poLineNo = wmAsnDetailModel.getPoLineNo();
        Double qtyAsnEa = wmAsnDetailModel.getQtyAsnEa();// 预收货数
        Double qytRcvEa = wmAsnDetailModel.getQtyRcvEa();// 收货数
        if (StringUtils.isNotEmpty(poNo) && StringUtils.isNotEmpty(poLineNo)
                && (WmsCodeMaster.ASN_NEW.getCode().equals(wmAsnDetailModel.getStatus()) || WmsCodeMaster.ASN_PART_RECEIVING.getCode().equals(wmAsnDetailModel.getStatus()))) {
            BanQinWmPoDetail wmPoDetailModel = findByPoNoAndLineNo(poNo, poLineNo, wmAsnDetailModel.getOrgId());
            if (null != wmPoDetailModel) {
                Double qtyAsnPo = wmPoDetailModel.getQtyAsnEa();// 已生成ASN数
                wmPoDetailModel.setQtyAsnEa(qtyAsnPo - (qtyAsnEa - qytRcvEa));
                this.save(wmPoDetailModel);
            }
        }
    }

    /**
     * 描述： 获取PO单明细
     *
     * @param ownerCode 货主
     * @param skuCode   商品
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmPoDetail newModel = new BanQinWmPoDetail();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmPoDetail wmPoDetail = findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != wmPoDetail) {
            msg.setSuccess(false);
            msg.setData(wmPoDetail);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }
}