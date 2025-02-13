package com.yunyou.modules.wms.qc.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.SysControlParamsUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.ControlParamCode;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcEntity;
import com.yunyou.modules.wms.qc.entity.BanQinWmQcHeader;
import com.yunyou.modules.wms.qc.mapper.BanQinWmQcHeaderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 质检单Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmQcHeaderService extends CrudService<BanQinWmQcHeaderMapper, BanQinWmQcHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmsCommonService wmsCommonService;

    public BanQinWmQcEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmQcEntity> findPage(Page page, BanQinWmQcEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public void updateStatus(String qcNo, String orgId) {
        mapper.updateStatus(qcNo, orgId);
    }

    public BanQinWmQcHeader findFirst(BanQinWmQcHeader example) {
        List<BanQinWmQcHeader> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询质检单
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmQcHeader findByQcNo(String qcNo, String orgId) {
        BanQinWmQcHeader example = new BanQinWmQcHeader();
        example.setQcNo(qcNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    /**
     * 描述：查询质检单Entity
     *
     * @param qcNo  质检单号
     * @param orgId
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmQcEntity findEntityByQcNo(String qcNo, String orgId) {
        BanQinWmQcHeader byQcNo = findByQcNo(qcNo, orgId);
        BanQinWmQcEntity entity = new BanQinWmQcEntity();
        BeanUtils.copyProperties(byQcNo, entity);
        return entity;
    }

    /**
     * 描述： 保存质检单
     *
     * @param model
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage saveModel(BanQinWmQcHeader model) {
        ResultMessage msg = new ResultMessage();
        // QC参数：QC单是否需要做审核（Y：需要审核；N：不用审核）
        String QC_AUDIT = SysControlParamsUtils.getValue(ControlParamCode.QC_AUDIT.getCode(), model.getOrgId());
        // 新增时，默认状态为创建，生成新的编号，审核状态（控制参数）
        if (StringUtils.isEmpty(model.getId())) {
            // 调用编号生成器生成QC号
            model.setQcNo(noService.getDocumentNo(GenNoType.WM_QC_NO.name()));
            model.setStatus(WmsCodeMaster.QC_NEW.getCode());
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            if (WmsConstants.YES.equals(QC_AUDIT)) {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
            } else {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NOT.getCode());
            }
            model.setAuditOp(null);
            model.setAuditTime(null);
        }
        this.save(model);
        msg.setSuccess(true);
        msg.setData(model);
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 描述： 删除质检单
     *
     * @param qcIds 质检单ID
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public void removeByQcId(String[] qcIds) {
        for (String id : qcIds) {
            this.delete(new BanQinWmQcHeader(id));
        }
    }

    /**
     * 审核：审核后不能修改订单
     *
     * @param qcIds
     * @return
     */
    @Transactional
    public ResultMessage audit(String[] qcIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = checkQcStatus(qcIds, new String[]{WmsCodeMaster.QC_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_NEW.getCode()}, null);
        String[] updateQcIds = (String[]) errorMsg.getData();
        if (updateQcIds.length > 0) {
            for (String id : updateQcIds) {
                BanQinWmQcHeader wmQcHeader = this.get(id);
                wmQcHeader.setAuditOp(UserUtils.getUser().getName());
                wmQcHeader.setAuditTime(new Date());
                wmQcHeader.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
                this.save(wmQcHeader);
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "非创建状态、已审核、已质检的订单，不能操作");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 取消审核
     *
     * @param qcNos
     * @return
     */
    @Transactional
    public ResultMessage cancelAudit(String[] qcNos) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = checkQcStatus(qcNos, new String[]{WmsCodeMaster.QC_NEW.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode()}, WmsConstants.NO);
        String[] updateQcIds = (String[]) errorMsg.getData();
        if (updateQcIds.length > 0) {
            for (String id : updateQcIds) {
                BanQinWmQcHeader wmQcHeader = this.get(id);
                wmQcHeader.setAuditOp(null);
                wmQcHeader.setAuditTime(null);
                wmQcHeader.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
                this.save(wmQcHeader);
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "非创建、未审核、已质检的订单，不能操作");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 关闭
     *
     * @param qcIds
     * @return
     */
    @Transactional
    public ResultMessage close(String[] qcIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = checkQcStatus(qcIds, new String[]{WmsCodeMaster.QC_FULL_QC.getCode()}, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()},
                WmsConstants.YES);
        String[] updateQcIds = (String[]) errorMsg.getData();
        if (updateQcIds.length > 0) {
            for (String id : updateQcIds) {
                BanQinWmQcHeader wmQcHeader = this.get(id);
                wmQcHeader.setStatus(WmsCodeMaster.QC_CLOSE.getCode());
                this.save(wmQcHeader);
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "不是完全质检，不能操作");// +"不是完全质检，不能操作"
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 校验传入的QC的状态，返回符合状态的QC，提示不符的QC
     *
     * @param qcIds
     * @param status
     * @param auditStatus
     * @param isQcSuggest
     * @return
     */
    public ResultMessage checkQcStatus(String[] qcIds, String[] status, String[] auditStatus, String isQcSuggest) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<String> returnQcIds = mapper.checkQcStatusQuery(qcIds, status, auditStatus, isQcSuggest);
        // 不符合条件的单号，提示
        Object[] minusQcIds = wmsCommonService.minus(qcIds, returnQcIds.toArray());
        msg.setMessage(getNotConvertMsg(minusQcIds));
        msg.setData(returnQcIds.toArray(new String[]{}));
        return msg;
    }

    private String getNotConvertMsg(Object[] minusAsnIds) {
        String msg = null;
        if (null != minusAsnIds && minusAsnIds.length > 0) {
            List<BanQinWmQcHeader> byIds = mapper.getByIds(Arrays.stream(minusAsnIds).map(String::valueOf).collect(Collectors.toList()).toArray(new String[]{}));
            msg = String.join("\n", byIds.stream().map(BanQinWmQcHeader::getQcNo).collect(Collectors.toList()));
        }
        return msg;
    }
}