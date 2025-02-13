package com.yunyou.modules.wms.qc.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleQcDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleQcDetailService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetail;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcDetailEntity;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcSku;
import com.yunyou.modules.wms.qc.mapper.BanQinWmQcDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 质检单明细Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmQcDetailService extends CrudService<BanQinWmQcDetailMapper, BanQinWmQcDetail> {
    @Autowired
    private BanQinCdRuleQcDetailService banQinCdRuleQcDetailService;
    @Autowired
    private BanQinWmQcSkuService banQinWmQcSkuService;
    @Autowired
    private WmsUtil wmsUtil;

    public BanQinWmQcDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmQcDetail> findList(BanQinWmQcDetail banQinWmQcDetail) {
        return super.findList(banQinWmQcDetail);
    }

    public Page<BanQinWmQcDetailEntity> findPage(Page page, BanQinWmQcDetailEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmQcDetail banQinWmQcDetail) {
        super.save(banQinWmQcDetail);
    }

    @Transactional
    public void delete(BanQinWmQcDetail banQinWmQcDetail) {
        super.delete(banQinWmQcDetail);
    }

    public BanQinWmQcDetail findFirst(BanQinWmQcDetail example) {
        List<BanQinWmQcDetail> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：查询质检单明细
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcDetail> findByQcNo(String qcNo, String orgId) {
        BanQinWmQcDetail example = new BanQinWmQcDetail();
        example.setQcNo(qcNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述：查询质检单明细
     *
     * @param qcNo     质检单号
     * @param qcLineNo 质检单商品明细行号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcDetail> findByQcNoAndQcLineNo(String qcNo, String qcLineNo, String orgId) {
        BanQinWmQcDetail example = new BanQinWmQcDetail();
        example.setQcNo(qcNo);
        example.setQcLineNo(qcLineNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述：查询质检单明细
     *
     * @param qcNo   质检单号
     * @param lineNo 质检单明细行号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public BanQinWmQcDetail findByQcNoAndLineNo(String qcNo, String lineNo, String orgId) {
        BanQinWmQcDetail example = new BanQinWmQcDetail();
        example.setQcNo(qcNo);
        example.setLineNo(lineNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    /**
     * 描述： 查询质检单明细Entity
     *
     * @param qcRcvId 质检收货明细号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcDetailEntity> getByQcRcvId(String qcRcvId, String orgId) {
//        List<BanQinWmQcDetailEntity> entities = Lists.newArrayList();
//
//        BanQinWmQcDetail qcDetail = new BanQinWmQcDetail();
//        qcDetail.setQcRcvId(qcRcvId);
//        qcDetail.setOrgId(orgId);
//        List<BanQinWmQcDetail> qcDetails = mapper.findList(qcDetail);
//        if (CollectionUtil.isNotEmpty(qcDetails)) {
//            for (BanQinWmQcDetail wmQcDetail : qcDetails) {
//                BanQinWmQcDetailEntity yEntity = new BanQinWmQcDetailEntity();
//                BeanUtils.copyProperties(wmQcDetail, yEntity);
//                yEntity.setQcType("Y");
//                entities.add(yEntity);
//
//                BanQinWmQcDetailEntity nEntity = new BanQinWmQcDetailEntity();
//                BeanUtils.copyProperties(wmQcDetail, nEntity);
//                nEntity.setQcType("N");
//                entities.add(nEntity);
//
//                BanQinWmQcDetailEntity qEntity = new BanQinWmQcDetailEntity();
//                BeanUtils.copyProperties(wmQcDetail, qEntity);
//                qEntity.setQcType("Q");
//                entities.add(qEntity);
//            }
//        }
//        return entities;
        BanQinWmQcDetailEntity entity = new BanQinWmQcDetailEntity();
        entity.setQcRcvId(qcRcvId);
        entity.setOrgId(orgId);
        return mapper.getWmQcDetailByQcRcvId(entity);
    }

    /**
     * 描述： 查询质检单明细Entity
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcDetailEntity> findEntityByQcNo(String qcNo, String orgId) {
        List<BanQinWmQcDetailEntity> list = Lists.newArrayList();

        List<BanQinWmQcDetail> wmQcDetailList = findByQcNo(qcNo, orgId);
        for (BanQinWmQcDetail detail : wmQcDetailList) {
            BanQinWmQcDetailEntity entity = new BanQinWmQcDetailEntity();
            BeanUtils.copyProperties(detail, entity);
            list.add(entity);
        }
        return list;
    }

    /**
     * 描述： 查询质检单明细Entity
     *
     * @param qcNo     质检单号
     * @param qcLineNo 质检单商品明细行号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public List<BanQinWmQcDetailEntity> findEntityByQcNoAndQcLineNo(String qcNo, String qcLineNo, String orgId) {
        List<BanQinWmQcDetailEntity> list = Lists.newArrayList();

        List<BanQinWmQcDetail> wmQcDetailList = findByQcNoAndQcLineNo(qcNo, qcLineNo, orgId);
        for (BanQinWmQcDetail detail : wmQcDetailList) {
            BanQinWmQcDetailEntity entity = new BanQinWmQcDetailEntity();
            BeanUtils.copyProperties(detail, entity);
            list.add(entity);
        }
        return list;
    }

    /**
     * 描述：查询质检单明细Entity
     *
     * @param qcNo   质检单号
     * @param lineNo 质检明细行号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    public BanQinWmQcDetailEntity findEntityByQcNoAndLineNo(String qcNo, String lineNo, String orgId) {
        BanQinWmQcDetail wmQcDetail = findByQcNoAndLineNo(qcNo, lineNo, orgId);
        if (wmQcDetail != null) {
            BanQinWmQcDetailEntity entity = new BanQinWmQcDetailEntity();
            BeanUtils.copyProperties(wmQcDetail, entity);
            return entity;
        }
        return null;
    }

    /**
     * 获取最新行号
     * @param headerId
     * @return
     */
    public String getNewLineNo(String headerId) {
        return wmsUtil.getMaxLineNo("wm_qc_detail", "head_id", headerId);
    }

    /**
     * 描述： 更新质检商品明细状态
     *
     * @param qcNo     质检单号
     * @param qcLineNo 质检单商品明细行号
     * @param status   更新状态
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void updateStatus(String qcNo, String qcLineNo, String status, String orgId) {
        mapper.updateStatus(qcNo, qcLineNo, status, orgId);
    }

    /**
     * 描述： 更新质检商品明细上架任务ID为空
     *
     * @param paId 上架任务ID
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public void updateQcQuaPaIdNull(String paId, String orgId) {
        mapper.updateQcQuaPaIdNull(paId, orgId);
    }

    /**
     * 描述： 更新质检商品明细上架任务ID为空
     *
     * @param paId 上架任务ID
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public void updateQcUnquaPaIdNull(String paId, String orgId) {
        mapper.updateQcUnquaPaIdNull(paId, orgId);
    }

    /**
     * 描述： 删除质检明细
     *
     * @param qcNo      质检单号
     * @param qcLineNos 质检单商品明细行号
     * @param orgId
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public void removeByQcNoAndQcLineNo(String qcNo, String[] qcLineNos, String orgId) {
        mapper.removeByQcNoAndQcLineNo(qcNo, qcLineNos, orgId);
    }

    /**
     * 描述： 保存质检明细
     *
     * @param model
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public BanQinWmQcDetail saveQcDetail(BanQinWmQcDetail model) {
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
     * 描述： 保存质检明细Entity
     *
     * @param entity
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public BanQinWmQcDetailEntity saveQcDetailEntity(BanQinWmQcDetailEntity entity) {
        BanQinWmQcDetail model = entity;
        this.save(model);
        return this.findEntityByQcNoAndLineNo(entity.getQcNo(), entity.getLineNo(), entity.getOrgId());
    }

    /**
     * 描述： 保存质检明细，汇总明细的合格数和不合格数，计算出合格率，并根据质检规则，算出质检处理建议，回填商品明细
     *
     * @param entity
     * @author Jianhua on 2019/1/30
     */
    @Transactional
    public ResultMessage saveAndSetQcSuggest(BanQinWmQcDetailEntity entity) {
        ResultMessage msg = new ResultMessage();

        Double qtyQcQuaEa = 0D;
        Double qtyQcUnquaEa = 0D;

        // 校验不同质检明细行间的良品traceId和不良品traceId不能一致。
        List<BanQinWmQcDetail> qcDetailList = findByQcNo(entity.getQcNo(), entity.getOrgId());
        for (BanQinWmQcDetail wmQcDetailModel : qcDetailList) {
            if (!wmQcDetailModel.getLineNo().equals(entity.getLineNo())) {
                if (!WmsConstants.TRACE_ID.equals(wmQcDetailModel.getQuaTraceId()) && !WmsConstants.TRACE_ID.equals(entity.getUnquaTraceId())
                        && wmQcDetailModel.getQuaTraceId().equals(entity.getUnquaTraceId())) {
                    msg.setSuccess(false);
                    // 质检明细行号{0}良品跟踪号和质检明细行号{1}不良品跟踪号不能一致
                    msg.addMessage("质检明细行号" + wmQcDetailModel.getLineNo() + "良品跟踪号和质检明细行号" + entity.getLineNo() + "不良品跟踪号不能一致");
                    return msg;
                }
                if (!WmsConstants.TRACE_ID.equals(wmQcDetailModel.getUnquaTraceId()) && !WmsConstants.TRACE_ID.equals(entity.getQuaTraceId())
                        && wmQcDetailModel.getUnquaTraceId().equals(entity.getQuaTraceId())) {
                    msg.setSuccess(false);
                    msg.addMessage("质检明细行号" + wmQcDetailModel.getLineNo() + "良品跟踪号和质检明细行号" + entity.getLineNo() + "不良品跟踪号不能一致");
                    return msg;
                }
            }
            qtyQcQuaEa = qtyQcQuaEa + (wmQcDetailModel.getQtyQcQuaEa() == null ? 0D : wmQcDetailModel.getQtyQcQuaEa());
            qtyQcUnquaEa = qtyQcUnquaEa + (wmQcDetailModel.getQtyQcUnquaEa() == null ? 0D : wmQcDetailModel.getQtyQcUnquaEa());
        }

        BanQinWmQcDetail model = entity;
        save(model);

        // 计算合格率
        BanQinWmQcSku item = banQinWmQcSkuService.getWmQcPctQuaQuery(entity.getQcNo(), entity.getQcLineNo(), entity.getOrgId());
        Double pctQua = item.getPctQua() * 100;
        Double qtyQua = item.getQtyQuaEa();
        Double qtyUnqua = item.getQtyUnquaEa();
        // 计算质检处理建议
        String qcSuggest = "";
        Collection<BanQinCdRuleQcDetail> detailList = banQinCdRuleQcDetailService.getCdRuleQcDetailByRuleCode(entity.getQcRule(), entity.getOrgId());
        for (BanQinCdRuleQcDetail cdRuleQcDetailModel : detailList) {
            if (cdRuleQcDetailModel.getFmRate() < pctQua && cdRuleQcDetailModel.getToRate() >= pctQua) {
                qcSuggest = cdRuleQcDetailModel.getQcSuggest();
                break;
            }
        }
        // 回填商品明细
        BanQinWmQcSku skuModel = banQinWmQcSkuService.findByQcNoAndLineNo(entity.getQcNo(), entity.getQcLineNo(), entity.getOrgId());
        skuModel.setQcSuggest(qcSuggest);
        skuModel.setQcActSuggest(qcSuggest);
        skuModel.setQtyQcQuaEa(qtyQua);
        skuModel.setQtyQcUnquaEa(qtyUnqua);
        skuModel.setPctQua(pctQua);
        banQinWmQcSkuService.save(skuModel);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }
}