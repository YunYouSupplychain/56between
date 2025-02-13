package com.yunyou.modules.wms.inbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoEntity;
import com.yunyou.modules.wms.inbound.entity.BanQinWmPoHeader;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmPoHeaderMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购单Service
 *
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPoHeaderService extends CrudService<BanQinWmPoHeaderMapper, BanQinWmPoHeader> {
    @Autowired
    private BanQinWmAsnDetailService banQinWmAsnDetailService;
    @Autowired
    private BanQinWmPoDetailService banQinWmPoDetailService;
    @Autowired
    private BanQinWmPoDetailNewService banQinWmPoDetailNewService;
    @Autowired
    private SynchronizedNoService noService;

    public BanQinWmPoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmPoEntity> findPage(Page page, BanQinWmPoEntity banQinWmPoEntity) {
        banQinWmPoEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmPoEntity.getOrgId()));
        dataRuleFilter(banQinWmPoEntity);
        banQinWmPoEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmPoEntity));
        return page;
    }

    /**
     * 描述： 获取查询结果的第一条记录
     *
     * @param wmPoHeader
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmPoHeader findFirst(BanQinWmPoHeader wmPoHeader) {
        List<BanQinWmPoHeader> list = this.findList(wmPoHeader);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：查询PO单
     *
     * @param poNo  PO单号
     * @param orgId 机构ID
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmPoHeader findByPoNo(String poNo, String orgId) {
        BanQinWmPoHeader example = new BanQinWmPoHeader();
        example.setPoNo(poNo);
        example.setOrgId(orgId);
        return findFirst(example);
    }

    /**
     * 描述： 查询PO单
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoHeader> findByPoId(String[] poIds) {
        List<BanQinWmPoHeader> list = Lists.newArrayList();
        for (String id : poIds) {
            BanQinWmPoHeader wmPoHeader = this.get(id);
            if (wmPoHeader != null) {
                list.add(wmPoHeader);
            }
        }
        return list;
    }

    /**
     * 描述： 查询PO单Entity
     *
     * @param poNo  PO单号
     * @param orgId 机构ID
     * @author Jianhua on 2019/1/29
     */
    public BanQinWmPoEntity findEntityByPoNo(String poNo, String orgId) {
        BanQinWmPoHeader wmPoHeader = findByPoNo(poNo, orgId);
        BanQinWmPoEntity entity = (BanQinWmPoEntity) wmPoHeader;
        return entity;
    }

    /**
     * 描述： 查询已生成ASN的PO单
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public List<BanQinWmPoHeader> findExistsAsn(String[] poIds) {
        return mapper.findExistsAsn(poIds);
    }


    /**
     * 描述： 校验存在ASN与不存在ASN的PO单
     * 存在的提示，不存在的返回值
     *
     * @param poIds PO单ID
     * @author Jianhua on 2019/1/29
     */
    public ResultMessage checkPoIsExistAsn(String[] poIds) {
        ResultMessage msg = new ResultMessage();

        StringBuilder sb = new StringBuilder();
        List<String> ids = Lists.newArrayList();
        // 存在ASN的PO单
        List<BanQinWmPoHeader> existsAsn = this.findExistsAsn(poIds);
        if (CollectionUtil.isNotEmpty(existsAsn) && existsAsn.size() > 0) {
            for (BanQinWmPoHeader wmPoHeader : existsAsn) {
                sb.append(wmPoHeader.getPoNo() + "\n");
                ids.add(wmPoHeader.getId());
            }
        }
        // 不存在ASN的PO单ID
        List<String> minusPoIds = Arrays.asList(poIds).stream().filter(o -> !ids.contains(o)).collect(Collectors.toList());

        msg.addMessage(sb.toString());
        msg.setData(minusPoIds.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 查询符合条件的PO
     *
     * @param poIds       PO单ID
     * @param auditStatus 审核状态
     * @param status      状态
     * @param isEdiSend   EDI发送标识
     * @author Jianhua on 2019/1/29
     */
    private List<BanQinWmPoHeader> findCheckPo(List<String> poIds, List<String> auditStatus, List<String> status, String isEdiSend) {
        List<BanQinWmPoHeader> rsList = Lists.newArrayList();

        List<BanQinWmPoHeader> wmPoHeaders = findByPoId(poIds.toArray(new String[]{}));
        for (BanQinWmPoHeader wmPoHeader : wmPoHeaders) {
            if (StringUtils.isNotEmpty(isEdiSend) && !isEdiSend.equals(wmPoHeader.getIsEdiSend())) {
                continue;
            }
            if (CollectionUtil.isNotEmpty(auditStatus) && !auditStatus.contains(wmPoHeader.getAuditStatus())) {
                continue;
            }
            if (CollectionUtil.isNotEmpty(status) && !status.contains(wmPoHeader.getStatus())) {
                continue;
            }
            rsList.add(wmPoHeader);
        }
        return rsList;
    }

    /**
     * 描述： 校验PO状态
     * 返回符合状态的PO，提示不符的PO
     *
     * @param poIds
     * @param auditStatus
     * @param status
     * @param isEdiSend
     * @author Jianhua on 2019/1/29
     */
    public ResultMessage checkPoStatus(String[] poIds, String[] auditStatus, String[] status, String isEdiSend) {
        ResultMessage msg = new ResultMessage();

        List<String> poIdList = null != poIds ? Arrays.asList(poIds) : null;
        List<String> auditStatusList = null != auditStatus ? Arrays.asList(auditStatus) : null;
        List<String> statusList = status != status ? Arrays.asList(status) : null;
        List<String> ids = Lists.newArrayList();
        // 返回符合条件的PO
        List<BanQinWmPoHeader> wmPoHeaderList = this.findCheckPo(poIdList, auditStatusList, statusList, isEdiSend);
        if (CollectionUtil.isNotEmpty(wmPoHeaderList) && wmPoHeaderList.size() > 0) {
            for (BanQinWmPoHeader wmPoHeader : wmPoHeaderList) {
                ids.add(wmPoHeader.getId());
            }
        }
        // 不符合条件的单号，提示
        List<String> minusPoIds = poIdList.stream().filter(o -> !ids.contains(o)).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (String id : minusPoIds) {
            BanQinWmPoHeader wmPoHeader = get(id);
            sb.append(wmPoHeader.getPoNo() + "\n");
        }
        msg.addMessage(sb.toString());
        msg.setData(ids.toArray(new String[]{}));
        return msg;
    }

    /**
     * 描述： 保存PO单
     *
     * @param model
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage savePoHeader(BanQinWmPoHeader model) {
        ResultMessage msg = new ResultMessage();
        // 是否需要审核
        String isAudit = WmsConstants.YES;
        // 新增时，默认状态为创建，生成新的编号，审核状态（控制参数）
        if (StringUtils.isEmpty(model.getId())) {
            // 调用编号生成器生成PO号
            model.setId(IdGen.uuid());
            model.setPoNo(noService.getDocumentNo(GenNoType.WM_PO_NO.name()));
            model.setStatus(WmsCodeMaster.PO_NEW.getCode());
            if (WmsConstants.YES.equals(isAudit)) {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
            } else {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NOT.getCode());
            }
            model.setAuditOp(null);
            model.setAuditTime(null);
            model.setIsEdiSend(WmsConstants.NO);
            model.setEdiSendTime(null);
        } else {
            // 如果修改了物流单号，更新子表的物流单号
            banQinWmPoDetailService.updateLogisticNo(model.getPoNo(), model.getLogisticNo(), model.getOrgId());
        }
        this.save(model);
        msg.setSuccess(true);
        msg.setMessage("保存成功");
        msg.setData(model);
        return msg;
    }

    /**
     * 描述： 审核
     *
     * @param poIds
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage audit(String[] poIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage errorMsg = checkPoStatus(poIds, new String[]{WmsCodeMaster.AUDIT_NEW.getCode()}, new String[]{WmsCodeMaster.PO_NEW.getCode()}, null);
        String[] updatePoIds = (String[]) errorMsg.getData();
        if (updatePoIds.length > 0) {
            for (String id : updatePoIds) {
                BanQinWmPoHeader wmPoHeader = this.get(id);
                if (!WmsCodeMaster.AUDIT_NEW.getCode().equals(wmPoHeader.getAuditStatus())) {
                    continue;
                }
                wmPoHeader.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
                this.save(wmPoHeader);
            }
        }
        if (StringUtils.isEmpty(errorMsg.getMessage())) {
            msg.addMessage("操作成功");
        } else {
            msg.addMessage(errorMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
        }
        msg.setSuccess(true);
        return msg;

    }

    /**
     * 描述： 取消审核
     *
     * @param poIds
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage cancelAudit(String[] poIds) {
        ResultMessage msg = new ResultMessage();

        ResultMessage checkMsg = new ResultMessage();
        ResultMessage errorMsg = this.checkPoStatus(poIds, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode()}, new String[]{WmsCodeMaster.PO_NEW.getCode()}, null);
        String[] updatePoIds = (String[]) errorMsg.getData();
        if (updatePoIds.length > 0) {
            // 校验是否存在ASN单
            checkMsg = checkPoIsExistAsn(updatePoIds);
            String[] checkPoIds = (String[]) checkMsg.getData();
            if (checkPoIds.length > 0) {
                for (String id : updatePoIds) {
                    BanQinWmPoHeader wmPoHeader = this.get(id);
                    wmPoHeader.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
                    wmPoHeader.setAuditOp(null);
                    wmPoHeader.setAuditTime(null);
                    this.save(wmPoHeader);
                }
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage(checkMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage(errorMsg.getMessage() + "已生成ASN单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 取消采购单
     *
     * @param poIds
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage cancel(String[] poIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
        // 校验状态
        ResultMessage checkMsg = this.checkPoStatus(poIds, new String[]{WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode()}, new String[]{WmsCodeMaster.PO_NEW.getCode()}, null);
        String[] checkPoIds = (String[]) checkMsg.getData();
        if (checkPoIds.length > 0) {
            // 校验是否存在ASN单
            errorMsg = this.checkPoIsExistAsn(checkPoIds);
            String[] updatePoIds = (String[]) errorMsg.getData();
            if (updatePoIds.length > 0) {
                for (String id : updatePoIds) {
                    BanQinWmPoHeader wmPoHeader = this.get(id);
                    wmPoHeader.setStatus(WmsCodeMaster.PO_CANCEL.getCode());
                    this.save(wmPoHeader);
                }
            }
        }
        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
            msg.addMessage(checkMsg.getMessage() + "非创建状态、已审核、已冻结的订单，不能操作");
        }
        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
            msg.addMessage(errorMsg.getMessage() + "已生成ASN单，不能操作");
        }
        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
            msg.addMessage("操作成功");
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 关闭采购单
     *
     * @param wmPoHeaderModel
     * @author Jianhua on 2019/1/29
     */
    @Transactional
    public ResultMessage close(BanQinWmPoHeader wmPoHeaderModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();

        boolean allCancelOrClose = banQinWmAsnDetailService.checkAllCancelOrCloseByPoNo(wmPoHeaderModel.getPoNo(), wmPoHeaderModel.getOrgId());
        if (!allCancelOrClose) {
            throw new WarehouseException("存在未关闭的ASN，不能关闭");
        }
        wmPoHeaderModel.setStatus(WmsCodeMaster.PO_CLOSE.getCode());
        this.save(wmPoHeaderModel);

        return msg;
    }

    /**
     * 描述： 删除PO
     *
     * @param wmPoHeader
     * @author Jianhua on 2019/2/14
     */
    @Transactional
    public void removePoEntity(BanQinWmPoHeader wmPoHeader) {
        // 删除明细(业务新增)
        banQinWmPoDetailNewService.removeByPoId(new String[]{wmPoHeader.getId()});
        // 删除明细
        banQinWmPoDetailService.removeByPoId(new String[]{wmPoHeader.getId()});
        // 删除单头
        this.delete(wmPoHeader);
    }

}