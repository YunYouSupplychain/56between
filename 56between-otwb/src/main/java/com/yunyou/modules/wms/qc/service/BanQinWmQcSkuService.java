package com.yunyou.modules.wms.qc.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageRelationService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcHeader;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSku;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSkuEntity;
import com.yunyou.modules.wms.qc.mapper.BanQinWmQcSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 质检单商品Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmQcSkuService extends CrudService<BanQinWmQcSkuMapper, BanQinWmQcSku> {
    @Autowired
    private BanQinWmQcHeaderService banQinWmQcHeaderService;
    @Autowired
    private BanQinCdWhSkuService banQinCdWhSkuService;
    @Autowired
    private BanQinCdWhPackageService banQinCdWhPackageService;
    @Autowired
    private BanQinCdWhPackageRelationService banQinCdWhPackageRelationService;
    @Autowired
    private WmsUtil wmsUtil;

    public BanQinWmQcSku get(String id) {
        return super.get(id);
    }

    public List<BanQinWmQcSku> findList(BanQinWmQcSku banQinWmQcSku) {
        return super.findList(banQinWmQcSku);
    }

    public Page<BanQinWmQcSkuEntity> findPage(Page page, BanQinWmQcSkuEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmQcSku banQinWmQcSku) {
        super.save(banQinWmQcSku);
    }

    @Transactional
    public void delete(BanQinWmQcSku banQinWmQcSku) {
        super.delete(banQinWmQcSku);
    }

    public BanQinWmQcSku findFirst(BanQinWmQcSku example) {
        List<BanQinWmQcSku> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询质检单商品明细
     *
     * @param qcNo
     * @param lineNo
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmQcSku findByQcNoAndLineNo(String qcNo, String lineNo, String orgId) {
        BanQinWmQcSku example = new BanQinWmQcSku();
        example.setQcNo(qcNo);
        example.setLineNo(lineNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    /**
     * 描述： 查询质检商品
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmQcSku> findByQcNo(String qcNo, String orgId) {
        BanQinWmQcSku wmQcSku = new BanQinWmQcSku();
        wmQcSku.setQcNo(qcNo);
        wmQcSku.setOrgId(orgId);
        return findList(wmQcSku);
    }

    /**
     * 描述： 查询质检商品明细
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmQcSkuEntity> findEntityByQcNo(String qcNo, String orgId) {
        List<BanQinWmQcSkuEntity> list = Lists.newArrayList();

        List<BanQinWmQcSku> wmQcSkus = findByQcNo(qcNo, orgId);
        for (BanQinWmQcSku wmQcSku : wmQcSkus) {
            list.add(findEntityByQcNoAndLineNo(wmQcSku.getQcNo(), wmQcSku.getLineNo(), wmQcSku.getOrgId()));
        }
        return list;
    }

    /**
     * 描述：查询质检商品明细
     *
     * @param qcNo   质检单号
     * @param lineNo 质检单商品明细行号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmQcSkuEntity findEntityByQcNoAndLineNo(String qcNo, String lineNo, String orgId) {
        BanQinWmQcSku wmQcSku = findByQcNoAndLineNo(qcNo, lineNo, orgId);
        BanQinWmQcSkuEntity entity = (BanQinWmQcSkuEntity) wmQcSku;

        BanQinCdWhSku cdWhSku = banQinCdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        entity.setSkuName(cdWhSku.getSkuName());
        entity.setQuickCode(cdWhSku.getQuickCode());

        BanQinCdWhPackageEntity cdWhPackageEntity = banQinCdWhPackageService.findByPackageCode(entity.getPackCode(), entity.getOrgId());
        entity.setPackDesc(cdWhPackageEntity.getCdpaFormat());

        BanQinCdWhPackageRelation cdWhPackageRelation = banQinCdWhPackageRelationService.findByPackageUom(entity.getPackCode(), entity.getUom(), entity.getOrgId());
        entity.setUomDesc(cdWhPackageRelation.getCdprDesc());
        entity.setUomQty(cdWhPackageRelation.getCdprQuantity());

        Double cdprQuantity = cdWhPackageRelation.getCdprQuantity() == null ? 1 : cdWhPackageRelation.getCdprQuantity();
        entity.setQtyAvailQcUom(entity.getQtyAvailQcEa() / cdprQuantity);
        entity.setQtyPlanQcUom(entity.getQtyPlanQcEa() / cdprQuantity);
        return entity;
    }

    public String getNewLineNo(String headerId) {
        return wmsUtil.getMaxLineNo("wm_qc_sku", "head_id", headerId);
    }

    /**
     * 描述：查询合格率=NULL的质检商品
     *
     * @param qcNo
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmQcSku> findPctQuaIsNull(String qcNo, String orgId) {
        return mapper.findPctQuaIsNull(qcNo, orgId);
    }

    /**
     * 描述：查询可取消质检的商品明细
     *
     * @param qcNo
     * @param lineNo
     * @param status
     * @param qcPhase
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmQcSku> findCanCancelDetail(String qcNo, String[] lineNo, String[] status, String qcPhase, String orgId) {
        return mapper.findCanCancelDetail(qcNo, lineNo, status, qcPhase, orgId);
    }

    /**
     * 描述： 查询未关闭/取消的商品明细
     *
     * @param orderNo 单据号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcSku> findUnCancelAndClose(String orderNo, String orgId) {
        return mapper.findUnCancelAndClose(orderNo, orgId);
    }

    /**
     * 描述： 删除质检商品明细
     *
     * @param qcIds 质检单ID
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void removeByQcId(String[] qcIds) {
        for (String qcId : qcIds) {
            BanQinWmQcHeader wmQcHeader = banQinWmQcHeaderService.get(qcId);
            removeByQcNoAndLineNo(wmQcHeader.getQcNo(), null, wmQcHeader.getOrgId());
        }
    }

    /**
     * 描述：删除质检商品明细
     *
     * @param qcNo    质检单号
     * @param lineNos 质检单商品明细行号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void removeByQcNoAndLineNo(String qcNo, String[] lineNos, String orgId) {
        mapper.removeByQcNoAndLineNo(qcNo, lineNos, orgId);
    }

    /**
     * 描述： 保存质检商品明细
     *
     * @param model
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public BanQinWmQcSku saveQcSkuModel(BanQinWmQcSku model) {
        if (StringUtils.isEmpty(model.getId())) {
            String lineNo = getNewLineNo(model.getHeadId());
            model.setLineNo(lineNo);
            model.setStatus(WmsCodeMaster.QC_NEW.getCode());
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
        }
        this.save(model);
        return model;
    }

    /**
     * 校验传入的QC明细的状态，返回符合状态的QC明细，提示不符的QC明细
     * @param qcNo
     * @param qcStatus
     * @param lineNos
     * @param lineStatus
     * @param auditStatus
     * @param isQcSuggest
     * @param orgId
     * @return
     */
    public ResultMessage checkQcSkuStatus(String qcNo, String[] qcStatus, String[] lineNos, String[] lineStatus, String[] auditStatus, String isQcSuggest, String orgId) {
        ResultMessage msg = new ResultMessage();

        List<String> auditStatusList = Arrays.asList(auditStatus);
        List<String> lineStatusList = Arrays.asList(lineStatus);
        List<String> qcStatusList = Arrays.asList(qcStatus);

        BanQinWmQcHeader wmQcHeader = banQinWmQcHeaderService.findByQcNo(qcNo, orgId);
        if (!auditStatusList.contains(wmQcHeader.getAuditStatus())) {

        }
        // 返回符合条件的行号
        List<String> returnLineNos = Lists.newArrayList();
        // 不符合条件的行号，提示
        StringBuilder sb = new StringBuilder();

        for (String lineNo : lineNos) {
            BanQinWmQcSku wmQcSku = findByQcNoAndLineNo(qcNo, lineNo, orgId);
            if (!lineStatusList.contains(wmQcSku.getStatus()) && !qcStatusList.contains(wmQcSku.getStatus())) {
                sb.append(wmQcSku.getLineNo() + "\n");
                continue;
            }
            if (StringUtils.isNotEmpty(isQcSuggest)) {
                if (WmsConstants.YES.equals(isQcSuggest) && wmQcSku.getPctQua() == null) {
                    sb.append(wmQcSku.getLineNo() + "\n");
                    continue;
                } else if (WmsConstants.NO.equals(isQcSuggest) && wmQcSku.getPctQua() != null) {
                    sb.append(wmQcSku.getLineNo() + "\n");
                    continue;
                }
            }
            returnLineNos.add(wmQcSku.getLineNo());
        }
        msg.addMessage(sb.toString());
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    /**
     * 校验可取消质检的商品明细
     * @param qcNo
     * @param lineNos
     * @param status
     * @param qcPhase
     * @param orgId
     * @return
     */
    public ResultMessage checkQcSkuForCancelQc(String qcNo, String[] lineNos, String[] status, String qcPhase, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 符合条件的行号
        List<String> returnLineNos = Lists.newArrayList();

        List<BanQinWmQcSku> wmQcSkus = this.findCanCancelDetail(qcNo, lineNos, status, qcPhase, orgId);
        if (CollectionUtil.isNotEmpty(wmQcSkus)) {
            for (BanQinWmQcSku qcSku : wmQcSkus) {
                returnLineNos.add(qcSku.getLineNo());
            }
        }
        // 不符合条件的行号，提示
        StringBuilder sb = new StringBuilder();
        List<String> errorLineNos = Arrays.asList(lineNos).stream().filter(o -> !returnLineNos.contains(o)).collect(Collectors.toList());
        for (String lineNo : errorLineNos) {
            sb.append(lineNo + "\n");
        }
        msg.addMessage(sb.toString());
        msg.setData(returnLineNos.toArray(new String[]{}));
        return msg;
    }

    public BanQinWmQcSku getWmQcPctQuaQuery(String qcNo, String qcLineNo, String orgId) {
        return mapper.getWmQcPctQuaQuery(qcNo, qcLineNo, orgId);
    }

}