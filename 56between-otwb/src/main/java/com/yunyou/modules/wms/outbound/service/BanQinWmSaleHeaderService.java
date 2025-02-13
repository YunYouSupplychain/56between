package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSaleHeader;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSaleHeaderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 销售单Service
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSaleHeaderService extends CrudService<BanQinWmSaleHeaderMapper, BanQinWmSaleHeader> {
    @Autowired
    private BanQinWmSaleDetailService wmSaleDetailService;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmsCommonService wmsCommon;

	public BanQinWmSaleEntity getEntity(String id) {
	    return mapper.getEntity(id);
    }

	public Page<BanQinWmSaleEntity> findPage(Page page, BanQinWmSaleEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
		return page;
	}

	public BanQinWmSaleHeader findFirst(BanQinWmSaleHeader banQinWmSaleHeader) {
        List<BanQinWmSaleHeader> list = this.findList(banQinWmSaleHeader);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据单号，删除
     * @param saleNos
     */
    @Transactional
    public void removeBySaleNo(String[] saleNos, String orgId) {
        mapper.deleteWmSaleHeader(Arrays.asList(saleNos), orgId);
    }

    /**
     * 查询SALE
     * @param saleNo
     * @return
     */
    public BanQinWmSaleHeader getBySaleNo(String saleNo, String orgId) {
        BanQinWmSaleHeader example = new BanQinWmSaleHeader();
        example.setSaleNo(saleNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    /**
     * 批量查询SALE
     * @param saleNos
     * @return
     */
    public List<BanQinWmSaleHeader> getBySaleNoArray(String[] saleNos, String orgId) {
        return mapper.getBySaleNoArray(Arrays.asList(saleNos), orgId);
    }

    /**
     * 根据单号，查询订单entity
     * @param saleNo
     * @return
     */
    public BanQinWmSaleEntity getEntityBySaleNo(String saleNo, String orgId) {
        BanQinWmSaleEntity entity = new BanQinWmSaleEntity();
        // 查询头
        BanQinWmSaleHeader saleHeader = this.getBySaleNo(saleNo, orgId);
        BeanUtils.copyProperties(saleHeader, entity);
        // 查询明细
        entity.setWmSaleDetailList(wmSaleDetailService.getEntityBySaleNo(saleNo, orgId));
        
        return entity;
    }

    /**
     * 保存SALE单
     * @param model
     * @return
     */
    @Transactional
    public ResultMessage saveSaleHeader(BanQinWmSaleHeader model) {
        ResultMessage msg = new ResultMessage();
        // 是否需要审核
        String isAudit = "N";
        // 新增时，默认状态为创建，生成新的编号，审核状态（控制参数）
        if (StringUtils.isEmpty(model.getId())) {
            // 调用编号生成器生成SALE号
            model.setSaleNo(noService.getDocumentNo(GenNoType.WM_SALE_NO.name()));
            model.setStatus(WmsCodeMaster.SALE_NEW.getCode());
            if (WmsConstants.YES.equals(isAudit)) {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
            } else {
                model.setAuditStatus(WmsCodeMaster.AUDIT_NOT.getCode());
            }
            model.setAuditOp(null);
            model.setAuditTime(null);
        } else {
            // 如果修改了物流单号，更新子表的物流单号
//            UpdateWmSaleDetailLogisticNoUpdateCondition update = new UpdateWmSaleDetailLogisticNoUpdateCondition();
//            update.setLogisticNo(model.getLogisticNo());
//            update.setSaleNo(model.getSaleNo());
//            update.setProjectId(SessionContext.getUser().getProjectId());
//            update.setOrgId(SessionContext.getUser().getOrgId());
//            dao.update(update);
        }
        this.save(model);
        msg.setSuccess(true);
        msg.setData(model);
        return msg;
    }

    /**
     * 审核
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage audit(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
//        ResultMessage errorMsg = checkSaleStatus(saleNos, new String[]{WmsCodeMaster.AUDIT_NEW.getCode()}, new String[]{WmsCodeMaster.SALE_NEW.getCode()});
//        Object[] updateSaleNos = (Object[]) errorMsg.getData();
//        if (updateSaleNos.length > 0) {
//            List<Object> saleList = Arrays.asList(updateSaleNos);
//            UpdateWmSaleAuditStatusUpdateCondition update = new UpdateWmSaleAuditStatusUpdateCondition();
//            update.setAuditOp(user.getUsername());
//            update.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
//            update.setAuditTime(DateUtils.newLocalDate());
//            update.setOrgId(user.getOrgId());
//            update.setProjectId(user.getProjectId());
//            for (int j = 0, len = saleList.size(); j < len; j = j + 995) {
//                if (j + 995 > len) {
//                    update.setSaleNos(saleList.subList(j, len).toArray(new String[] {}));
//                } else {
//                    update.setSaleNos(saleList.subList(j, j + 995).toArray(new String[] {}));
//                }
//                dao.update(update);
//            }
//        }
//        if (StringUtils.isEmpty(errorMsg.getMessage())) {
//            msg.addMessage("操作成功");
//        } else {
//            msg.addMessage("非创建状态、已审核、已冻结的订单，不能操作");
//        }
        msg.setSuccess(true);
        return msg;

    }

    /**
     * 取消审核
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage cancelAudit(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage checkMsg = new ResultMessage();
//        ResultMessage errorMsg = checkSaleStatus(saleNos, new String[]{WmsCodeMaster.AUDIT_CLOSE.getCode()}, new String[]{WmsCodeMaster.SALE_NEW.getCode()});
//        String[] updateSaleNos = (String[]) errorMsg.getData();
//        if (updateSaleNos.length > 0) {
//            // 校验是否存在SO单
//            checkMsg = checkSaleIsExistSo(Arrays.asList(updateSaleNos).toArray(new String[]{}));
//            Object[] checkSaleNos = (Object[]) checkMsg.getData();
//            if (checkSaleNos.length > 0) {
//                List<Object> saleList = Arrays.asList(checkSaleNos);
//                UpdateWmSaleAuditStatusUpdateCondition update = new UpdateWmSaleAuditStatusUpdateCondition();
//                update.setAuditOp(null);
//                update.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
//                update.setAuditTime(null);
//                update.setOrgId(user.getOrgId());
//                update.setProjectId(user.getProjectId());
//                for (int j = 0, len = saleList.size(); j < len; j = j + 995) {
//                    if (j + 995 > len) {
//                        update.setSaleNos(saleList.subList(j, len).toArray(new String[] {}));
//                    } else {
//                        update.setSaleNos(saleList.subList(j, j + 995).toArray(new String[] {}));
//                    }
//                    dao.update(update);
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(errorMsg.getMessage())) {
//            msg.addMessage("非创建状态、已审核、已冻结的订单，不能操作");
//        }
//        if (StringUtils.isNotEmpty(checkMsg.getMessage())) {
//            msg.addMessage("已生成SO单，不能操作");
//        }
//        if (StringUtils.isEmpty(errorMsg.getMessage()) && StringUtils.isEmpty(checkMsg.getMessage())) {
//            msg.addMessage("操作成功");
//        }
//        msg.setSuccess(true);
        return msg;
    }

    /**
     * 取消
     * @param saleNos
     * @return
     */
    @Transactional
    public ResultMessage cancel(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        ResultMessage errorMsg = new ResultMessage();
        // 校验状态
//        ResultMessage checkMsg = checkSaleStatus(saleNos, new String[] { WmsCodeMaster.AUDIT_NEW.getCode(), WmsCodeMaster.AUDIT_NOT.getCode() }, new String[] { WmsCodeMaster.SALE_NEW.getCode() });
//        Object[] checkSaleNos = (Object[]) checkMsg.getData();
//        if (checkSaleNos.length > 0) {
//            // 校验是否存在SO单
//            errorMsg = checkSaleIsExistSo(Arrays.asList(checkSaleNos).toArray(new String[] {}));
//            Object[] updateSaleNos = (Object[]) errorMsg.getData();
//            if (updateSaleNos.length > 0) {
//                List<Object> lieNoList = Arrays.asList(updateSaleNos);
//                UpdateWmSaleStatusUpdateCondition con = new UpdateWmSaleStatusUpdateCondition();
//                con.setStatus(WmsCodeMaster.SALE_CANCEL.getCode());
//                con.setOrgId(SessionContext.getUser().getOrgId());
//                con.setProjectId(SessionContext.getUser().getProjectId());
//                for (int j = 0, len = lieNoList.size(); j < len; j = j + 995) {
//                    if (j + 995 > len) {
//                        con.setSaleNos(lieNoList.subList(j, len).toArray(new String[] {}));
//                    } else {
//                        con.setSaleNos(lieNoList.subList(j, j + 995).toArray(new String[] {}));
//                    }
//                    dao.update(con);
//                }
//            }
//
//        }
//        if (EmptyUtils.isNotEmpty(checkMsg.getMessagesAsString())) {
//            msg.addMessage(checkMsg.getMessagesAsString() + MsgUtils.getMessage("msg.wms.status.notNewAuditHold"));// 非创建状态、已审核、已冻结的订单，不能操作
//        }
//        if (EmptyUtils.isNotEmpty(errorMsg.getMessagesAsString())) {
//            msg.addMessage(errorMsg.getMessagesAsString() + MsgUtils.getMessage("msg.wms.status.createSo"));// "已生成SO单，不能操作"
//        }
//        if (EmptyUtils.isEmpty(errorMsg.getMessagesAsString()) && EmptyUtils.isEmpty(checkMsg.getMessagesAsString())) {
//            msg.addMessage(MsgUtils.getMessage("msg.wms.info.success"));
//        }
//        msg.setSuccess(true);
        return msg;
    }

    /**
     * 关闭销售单
     * @param wmSaleHeaderModel
     * @return
     * @throws WarehouseException
     */
    @Transactional
    public ResultMessage close(BanQinWmSaleHeader wmSaleHeaderModel) throws WarehouseException {
        ResultMessage msg = new ResultMessage();
        // 存在未关闭的SO
        List<String> items = mapper.checkWmSaleExistsUnCloseSo(wmSaleHeaderModel.getSaleNo(), wmSaleHeaderModel.getOrgId());
        if (null != items && items.size() > 0) {
            throw new WarehouseException("存在未关闭的SO，不能关闭");
        }
        wmSaleHeaderModel.setStatus(WmsCodeMaster.SALE_CLOSE.getCode());
        this.save(wmSaleHeaderModel);
        msg.setMessage("操作成功");
        return msg;
    }

    /**
     * 校验传入的SALE的状态，返回符合状态的SALE，提示不符的SALE
     * @param saleNos
     * @param auditStatus
     * @param status
     * @return
     */
    public ResultMessage checkSaleStatus(String[] saleNos, String[] auditStatus, String[] status, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 返回符合条件的单号
        List<String> returnSaleNos = getCheckSale(Arrays.asList(saleNos), status, auditStatus, orgId);
        // 不符合条件的单号，提示
        Object[] minusSaleNos = wmsCommon.minus(saleNos, returnSaleNos.toArray());
        String str = "";
        for (Object minusSaleNo : minusSaleNos) {
            str = str + minusSaleNo.toString() + "\n";
        }
        if (StringUtils.isNotEmpty(str)) {
            msg.addMessage(str);
        }
        msg.setData(returnSaleNos.toArray());
        return msg;
    }

    /**
     * 查询符合条件的SALE
     * @param saleNos
     * @param auditStatus
     * @param status
     * @return
     */
    protected List<String> getCheckSale(List<String> saleNos, String[] auditStatus, String[] status, String orgId) {
        return mapper.checkSaleStatus(saleNos, Arrays.asList(status), Arrays.asList(auditStatus), orgId);
    }

    /**
     * 查询存在SO单的SALE明细
     * @param saleNos
     * @return
     */
    public ResultMessage checkSaleIsExistSo(String[] saleNos, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 存在SO的单号
        List<String> returnSaleNos = getSaleExistsSo(Arrays.asList(saleNos), orgId);
        Object[] minusSaleNos = wmsCommon.minus(Arrays.asList(saleNos).toArray(), returnSaleNos.toArray());
        String str = "";
        for (Object returnSaleNo : returnSaleNos) {
            str = str + returnSaleNo.toString() + "\n";
        }
        msg.addMessage(str);
        msg.setData(minusSaleNos);
        return msg;
    }

    /**
     * 查询存在SO单的SALE明细
     *
     * @param saleNos
     * @return
     */
    protected List<String> getSaleExistsSo(List<String> saleNos, String orgId) {
        return mapper.checkWmSaleExistsSo(saleNos, orgId);
    }
    
    @Transactional
    public void updateWmSaleHeaderStatus(List<String> saleNos, String userId, String orgId) {
	    mapper.updateWmSaleHeaderStatus(saleNos, userId, orgId);
    }
	
}