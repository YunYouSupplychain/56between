package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.enums.CustomerType;
import com.yunyou.common.enums.SystemAliases;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.interfaces.interactive.service.PushTmsService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleRotationHeader;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeaderEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhPackageRelation;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleRotationHeaderService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhPackageRelationService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.*;
import com.yunyou.modules.wms.customer.entity.BanQinEbCustomer;
import com.yunyou.modules.wms.customer.service.BanQinEbCustomerService;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoImportEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoHeaderMapper;
import com.yunyou.modules.wms.report.entity.PickingOrderLabel;
import com.yunyou.modules.wms.report.entity.ShipHandoverOrder;
import com.yunyou.modules.wms.report.entity.ShipOrderLabel;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 出库单Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoHeaderService extends CrudService<BanQinWmSoHeaderMapper, BanQinWmSoHeader> {
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmSoDetailService banQinWmSoDetailService;
    @Autowired
    private BanQinWmWvDetailService banQinWmWvDetailService;
    @Autowired
    private BanQinEbCustomerService ebCustomerService;
    @Autowired
    private BanQinCdWhSkuService cdWhSkuService;
    @Autowired
    private BanQinWmSoAllocService banQinWmSoAllocService;
    @Autowired
    private BanQinWmUpdateConsigneeInfoLogService wmUpdateConsigneeInfoLogService;
    @Autowired
    private BanQinCdWhPackageRelationService cdWhPackageRelationService;
    @Autowired
    private BanQinCdRuleRotationHeaderService cdRuleRotationHeaderService;
    @Autowired
    @Lazy
    private PushTmsService pushTmsService;

    public BanQinWmSoEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public Page<BanQinWmSoEntity> findPage(Page page, BanQinWmSoEntity banQinWmSoEntity) {
        banQinWmSoEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmSoEntity.getOrgId()));
        dataRuleFilter(banQinWmSoEntity);
        banQinWmSoEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmSoEntity));
        return page;
    }

    public Page<BanQinWmSoEntity> findGrid(Page page, BanQinWmSoEntity banQinWmSoEntity) {
        banQinWmSoEntity.setDataScope("AND a.org_id ='" + banQinWmSoEntity.getOrgId() + "'");
        dataRuleFilter(banQinWmSoEntity);
        banQinWmSoEntity.setPage(page);
        page.setList(mapper.findGrid(banQinWmSoEntity));
        return page;
    }

    public Page<BanQinWmRepOutDailyQueryEntity> wmRepOutDailyQuery(Page page, BanQinWmRepOutDailyQueryEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("wsh.org_id", entity.getOrgId()));
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.wmRepOutDailyQuery(entity));
        return page;
    }

    public BanQinWmRepOutDailyQueryEntity countTotalQuery(BanQinWmRepOutDailyQueryEntity entity) {
        BanQinWmRepOutDailyQueryEntity result = new BanQinWmRepOutDailyQueryEntity();
        entity.setDataScope(GlobalDataRule.getDataRuleSql("wsh.org_id", entity.getOrgId()));
        List<BanQinWmRepOutDailyQueryEntity> list = mapper.countTotalQuery(entity);
        double sumQtySo = 0d;
        double sumQtyShip = 0d;
        for (BanQinWmRepOutDailyQueryEntity query : list) {
            if (null != query) {
                sumQtySo += query.getQtySoEa();
                sumQtyShip += query.getQtyShipEa();
            }
        }
        result.setTotalSoEaQty(sumQtySo);
        result.setTotalShipEaQty(sumQtyShip);
        return result;
    }

    public BanQinWmSoHeader findFirst(BanQinWmSoHeader example) {
        List<BanQinWmSoHeader> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询出库单
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoHeader findBySoNo(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = new BanQinWmSoHeader();
        model.setSoNo(soNo);
        model.setOrgId(orgId);
        model = this.findFirst(model);
        if (model == null) {
            // 查找不到出库单{0}
            throw new WarehouseException("查找不到出库单" + soNo);
        }
        return model;
    }

    /**
     * 描述： 查询出库单Entity
     *
     * @param waveNo 波次单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoEntity> findEntityByWaveNo(String waveNo, String orgId) {
        BanQinWmSoEntity entity = new BanQinWmSoEntity();
        entity.setWaveNo(waveNo);
        entity.setOrgId(orgId);
        return mapper.findEntity(entity);
    }

    /**
     * 描述： 查询出库单Entity
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoEntity findEntityBySoNo(String soNo, String orgId) {
        BanQinWmSoEntity entity = new BanQinWmSoEntity();
        entity.setSoNo(soNo);
        entity.setOrgId(orgId);
        List<BanQinWmSoEntity> entities = mapper.findEntity(entity);
        if (CollectionUtil.isNotEmpty(entities)) {
            return entities.get(0);
        }
        return null;
    }

    /**
     * 描述： 保存出库单
     *
     * @param entity
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public BanQinWmSoHeader saveSoHeader(BanQinWmSoEntity entity) {
        BanQinWmSoHeader model = new BanQinWmSoHeader();
        BeanUtils.copyProperties(entity, model);
        // 1、出库单号为空，则自动生成
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            // 生成编号
            model.setSoNo(noService.getDocumentNo(GenNoType.WM_SO_NO.name()));
            // 订单状态
            model.setStatus(WmsCodeMaster.SO_NEW.getCode());
            // 审核状态
            // 需要审核，状态为00
            model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
            model.setAuditOp(null);// 审核人
            model.setAuditTime(null);// 审核时间
            // 拦截状态
            model.setInterceptStatus(WmsCodeMaster.ITC_NO_INTERCEPT.getCode());
            // 冻结状态
            model.setHoldStatus(WmsCodeMaster.ODHL_NO_HOLD.getCode());
            // 打包状态
            model.setPackStatus(WmsCodeMaster.PACK_NEW.getCode());
            if (StringUtils.isBlank(model.getDef13())) {
                // 业务订单类型：SWMS-系统WMS业务订单
                model.setDef13("SWMS");
            }
        }
        // 2、保存
        this.save(model);
        // 如果物流单号 不为空，那么更新明细行的所有物流单号
        if (StringUtils.isNotEmpty(model.getLogisticNo())) {
            banQinWmSoDetailService.updateLogistic(model.getSoNo(), model.getLogisticNo(), model.getOrgId());
        }
        return model;
    }

    /**
     * 描述： 审核
     *
     * @param soNo 出库单号
     */
    @Transactional
    public void audit(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        if (WmsCodeMaster.AUDIT_NOT.getCode().equals(model.getAuditStatus())) {
            // 订单{0}不审核
            throw new WarehouseException("订单" + soNo + "不审核");
        }
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            // 订单{0}已审核
            throw new WarehouseException("订单" + soNo + "已审核");
        }
        if (!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
            // 订单{0}不是创建状态，不能审核
            throw new WarehouseException("订单" + soNo + "不是创建状态，不能审核");
        }
        // 审核状态
        model.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
        model.setAuditOp(UserUtils.getUser().getName());// 审核人
        model.setAuditTime(new Date());// 审核时间
        this.save(model);
    }

    /**
     * 描述： 取消审核
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void cancelAudit(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_NOT.getCode())) {
            // 不审核
            throw new WarehouseException("订单" + soNo + "不审核");
        }
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_NEW.getCode())) {
            // 未审核
            throw new WarehouseException(soNo + "未审核，不能操作");
        }
        if (!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
            // 不是创建状态，不能操作
            throw new WarehouseException(soNo + "不是创建状态，不能操作");
        }
        BanQinWmWvDetail wvDetailModel = banQinWmWvDetailService.findBySoNo(soNo, orgId);
        if (wvDetailModel != null) {
            // 已经生成波次计划，不能操作
            throw new WarehouseException(soNo + "已经生成波次计划，不能操作");
        }
        ResultMessage msg = banQinWmSoDetailService.checkSoExistCd(new String[]{soNo}, orgId);
        Object[] soNos = (Object[]) msg.getData();
        if (soNos.length == 0) {
            throw new WarehouseException("存在越库标记的明细，不能操作");
        }
        // 审核状态
        model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
        model.setAuditOp(null);// 审核人
        model.setAuditTime(null);// 审核时间
        this.save(model);
    }

    /**
     * 描述： 冻结出库单
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void hold(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        // 关闭状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CLOSE.getCode())) {
            // 订单{0}已关闭
            throw new WarehouseException("订单" + soNo + "已关闭");
        }
        // 取消状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 订单{0}已取消
            throw new WarehouseException("订单" + soNo + "已取消");
        }
        // 校验拦载状态
        if (WmsCodeMaster.ITC_INTERCEPT.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_WAITING.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_SUCCESS.getCode().equals(model.getInterceptStatus())) {
            // 已拦截
            throw new WarehouseException(soNo + "已拦截");
        }
        // 校验冻结状态
        if (model.getHoldStatus().equals(WmsCodeMaster.ODHL_HOLD.getCode())) {
            // 订单{0}已冻结
            throw new WarehouseException("订单" + soNo + "已冻结");
        }
        // 冻结状态
        model.setHoldStatus(WmsCodeMaster.ODHL_HOLD.getCode());
        model.setHoldOp(UserUtils.getUser().getName());// 冻结人
        model.setHoldTime(new Date());// 冻结时间
        this.save(model);
    }

    /**
     * 描述： 取消冻结出库单
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void cancelHold(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        // 关闭状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CLOSE.getCode())) {
            // 订单{0}已关闭
            throw new WarehouseException("订单" + soNo + "已关闭");
        }
        // 取消状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 订单{0}已取消
            throw new WarehouseException("订单" + soNo + "已取消");
        }
        // 校验拦载状态
        if (WmsCodeMaster.ITC_INTERCEPT.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_WAITING.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_SUCCESS.getCode().equals(model.getInterceptStatus())) {
            // 已拦截
            throw new WarehouseException(soNo + "已拦截");
        }
        // 校验冻结状态
        if (model.getHoldStatus().equals(WmsCodeMaster.ODHL_NO_HOLD.getCode())) {
            // 未冻结,不能操作
            throw new WarehouseException(soNo + "未冻结,不能操作");
        }
        // 冻结状态
        model.setHoldStatus(WmsCodeMaster.ODHL_NO_HOLD.getCode());
        model.setHoldOp(UserUtils.getUser().getName());// 冻结人
        model.setHoldTime(new Date());// 冻结时间
        this.save(model);
    }

    /**
     * 描述： 取消拦截
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void cancelByIntercept(String soNo, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        // 拦截状态为拦截(10)、待归储(20)
        if (model.getInterceptStatus().equals(WmsCodeMaster.ITC_INTERCEPT.getCode()) || model.getInterceptStatus().equals(WmsCodeMaster.ITC_WAITING.getCode())) {
            // 状态90
            model.setStatus(WmsCodeMaster.SO_CANCEL.getCode());
            // 拦截状态99
            model.setInterceptStatus(WmsCodeMaster.ITC_SUCCESS.getCode());
            this.save(model);
        }
    }

    /**
     * 描述： 校验订单拦截状态，抛出提示
     *
     * @param processByCode
     * @param noList
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage checkBatchInterceptStatus(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
        int size = noList.size();
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < size; i = i + 999) {
            if (size >= i + 999) {
                list.addAll(getInterceptStatusSoNo(processByCode, noList.subList(i, i + 999), orgId));
            } else {
                list.addAll(getInterceptStatusSoNo(processByCode, noList.subList(i, size), orgId));
            }
        }
        if (list.size() > 0) {
            String listStr = "";
            for (String soNo : list) {
                listStr += soNo + "\n";
            }
            // {0}已拦截，不能操作
            msg.addMessage(listStr + "已拦截，不能操作");
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 获取拦截状态的出库单号
     *
     * @param processByCode
     * @param noList
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    protected List<String> getInterceptStatusSoNo(String processByCode, List<String> noList, String orgId) {
        // 按出库单号
        if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(WmsConstants.YES, null, noList, null, null, null, null, orgId);
        }
        // 按出库单行号
        else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(WmsConstants.YES, null, null, noList, null, null, null, orgId);
        }
        // 按预配ID
        else if (processByCode.equals(ProcessByCode.BY_PREALLOC.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(WmsConstants.YES, null, null, null, noList, null, null, orgId);
        }
        // 按分配ID
        else if (processByCode.equals(ProcessByCode.BY_ALLOC.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(WmsConstants.YES, null, null, null, null, noList, null, orgId);
        }
        // 按波次单号
        else if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(WmsConstants.YES, null, null, null, null, null, noList, orgId);
        }
        return null;
    }

    /**
     * 描述： 校验订单冻结状态，抛出提示
     *
     * @param processByCode
     * @param noList
     * @author Jianhua on 2019/2/15
     */
    public ResultMessage checkBatchHoldStatus(String processByCode, List<String> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
        int size = noList.size();
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < size; i = i + 999) {
            if (size >= i + 999) {
                list.addAll(getHoldStatusSoNo(processByCode, noList.subList(i, i + 999), orgId));
            } else {
                list.addAll(getHoldStatusSoNo(processByCode, noList.subList(i, size), orgId));
            }
        }
        if (list.size() > 0) {
            String listStr = "";
            for (String soNo : list) {
                listStr += soNo + "\n";
            }
            // {0}已冻结，不能操作
            msg.addMessage(listStr + "已冻结，不能操作");
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 描述： 获取冻结状态的出库单号
     *
     * @param processByCode
     * @param noList
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    protected List<String> getHoldStatusSoNo(String processByCode, List<String> noList, String orgId) {
        // 按出库单号
        if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(null, WmsConstants.YES, noList, null, null, null, null, orgId);
        }
        // 按出库单行号
        else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(null, WmsConstants.YES, null, noList, null, null, null, orgId);
        }
        // 按预配ID
        else if (processByCode.equals(ProcessByCode.BY_PREALLOC.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(null, WmsConstants.YES, null, null, noList, null, null, orgId);
        }
        // 按分配ID
        else if (processByCode.equals(ProcessByCode.BY_ALLOC.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(null, WmsConstants.YES, null, null, null, noList, null, orgId);
        }
        // 按波次单号
        else if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            return mapper.findSoNoInterceptOrHoldStatus(null, WmsConstants.YES, null, null, null, null, noList, orgId);
        }
        return null;
    }

    /***
     * 描述： 校验出库单各状态 取消关闭状态、拦截状态、冻结状态
     *
     * @param soNo
     * @param actionCode
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoHeader checkStatus(String soNo, ActionCode actionCode, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        if (actionCode == null) {
            if (!model.getStatus().equals(WmsCodeMaster.SO_NEW.getCode())) {
                // 不是创建状态
                throw new WarehouseException(soNo + "不是创建状态");
            }
            if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
                // 已审核，不能操作
                throw new WarehouseException(soNo + "已审核，不能操作");
            }
        }

        // 取消、关闭状态
        // 关闭状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CLOSE.getCode())) {
            // 订单{0}已关闭
            throw new WarehouseException("订单" + soNo + "已关闭");
        }
        // 取消状态
        if (model.getStatus().equals(WmsCodeMaster.SO_CANCEL.getCode())) {
            // 订单{0}已取消
            throw new WarehouseException("订单" + soNo + "已取消");
        }
        // 拦截状态
        if (WmsCodeMaster.ITC_INTERCEPT.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_WAITING.getCode().equals(model.getInterceptStatus())
            || WmsCodeMaster.ITC_SUCCESS.getCode().equals(model.getInterceptStatus())) {
            // 已拦截
            throw new WarehouseException(soNo + "已拦截");
        }
        // 冻结状态
        if (WmsCodeMaster.ODHL_HOLD.getCode().equals(model.getHoldStatus())) {
            // 已冻结
            throw new WarehouseException(soNo + "已冻结");
        }
        return model;
    }

    /**
     * 描述： 校验拣货/发运状态，是否可生成装车单，抛出提示
     *
     * @param soNo
     * @param status
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public boolean checkSoStatusByIsLd(String soNo, String status, String orgId) throws WarehouseException {
        BanQinWmSoHeader wmSoHeader = this.findBySoNo(soNo, orgId);
        return !wmSoHeader.getStatus().equals(status);
    }

    /**
     * 描述： 未生成装车单的发运订单装车交接/取消交接 更新装车状态
     *
     * @param soNo
     * @param status
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateLdStatusBySoNo(String soNo, String status, String orgId) throws WarehouseException {
        BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
        // 不是完全发运状态，不能装车交接
        if (!model.getStatus().equals(WmsCodeMaster.SO_FULL_SHIPPING.getCode())) {
            throw new WarehouseException(soNo + "不是完全发运状态，不能操作");
        }
        model.setLdStatus(status);// 装车状态
        this.save(model);
    }

    /**
     * 描述： 更新装车状态
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateLdStatus(String soNo, String orgId) throws WarehouseException {
        String ldStatus = null;
        List<BanQinWmSoDetail> wmSoDetails = banQinWmSoDetailService.findBySoNo(soNo, orgId);
        long isNull = wmSoDetails.stream().filter(s -> StringUtils.isEmpty(s.getLdStatus())).count();
        long isNew = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_NEW.getCode().equals(s.getLdStatus())).count();
        long isFullLoad = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_FULL_LOAD.getCode().equals(s.getLdStatus())).count();
        long isPartLoad = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_PART_LOAD.getCode().equals(s.getLdStatus())).count();
        long isPartDelivery = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_PART_DELIVERY.getCode().equals(s.getLdStatus())).count();
        long isFullDelivery = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_FULL_DELIVERY.getCode().equals(s.getLdStatus())).count();
        long isClose = wmSoDetails.stream().filter(s -> WmsCodeMaster.LD_CLOSE.getCode().equals(s.getLdStatus())).count();
        if (isNew > 0 && isFullLoad == 0 && isPartLoad == 0 && isFullDelivery == 0 && isPartDelivery == 0) {
            ldStatus = WmsCodeMaster.LD_NEW.getCode();
        } else if (isPartLoad > 0 || (isFullLoad > 0 && isFullLoad != wmSoDetails.size() && isFullDelivery == 0 && isPartDelivery == 0)) {
            ldStatus = WmsCodeMaster.LD_PART_LOAD.getCode();
        } else if (wmSoDetails.size() == isFullLoad) {
            ldStatus = WmsCodeMaster.LD_FULL_LOAD.getCode();
        } else if (isPartDelivery > 0 || (isFullDelivery > 0 && wmSoDetails.size() != isFullDelivery + isClose)) {
            ldStatus = WmsCodeMaster.LD_PART_DELIVERY.getCode();
        } else if (wmSoDetails.size() == isFullDelivery || (isFullDelivery > 0 && wmSoDetails.size() == isFullDelivery + isClose)) {
            ldStatus = WmsCodeMaster.LD_FULL_DELIVERY.getCode();
        } else if (wmSoDetails.size() == isClose) {
            ldStatus = WmsCodeMaster.LD_CLOSE.getCode();
        }
        if (ldStatus != null) {
            BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
            model.setLdStatus(ldStatus);
            this.save(model);
        } else if (isNull == wmSoDetails.size()) {
            BanQinWmSoHeader model = this.findBySoNo(soNo, orgId);
            model.setLdStatus(ldStatus);
            this.save(model);
        }
    }

    @Transactional
    public void updateSoHeaderStatusByWave(List<String> waveNos, String userId, String orgId) {
        mapper.updateSoHeaderStatusByWave(waveNos, userId, orgId);
    }

    @Transactional
    public void updateSoHeaderStatusBySo(List<String> soNos, String userId, String orgId) {
        mapper.updateSoHeaderStatusBySo(soNos, userId, orgId);
    }

    public List<String> checkSoIsCdByCreateWave(List<String> soNos, String orgId) {
        return mapper.checkSoIsCdByCreateWave(soNos, orgId).stream().distinct().collect(Collectors.toList());
    }

    public List<BanQinWmSoHeader> checkSoIsClose(String ldNo, String orgId) {
        return mapper.checkSoIsClose(ldNo, orgId);
    }

    public List<PickingOrderLabel> getPickingOrder(List<String> soHeaderIds) {
        List<PickingOrderLabel> result = Lists.newArrayList();
        List<PickingOrderLabel> pickingOrder = mapper.getPickingOrder(soHeaderIds);
        Map<String, List<PickingOrderLabel>> collect = pickingOrder.stream().collect(Collectors.groupingBy(s -> s.getSoNo() + s.getOrgId()));
        collect.values().forEach(label -> {
            double sumQty = label.stream().mapToDouble(PickingOrderLabel::getQty).sum();
            double sumBoxQty = label.stream().mapToDouble(PickingOrderLabel::getBoxQty).sum();
            label.forEach(v -> {
                v.setTotalQty(sumQty);
                v.setTotalBoxQty(sumBoxQty);
                if (v.getRiceNum() > 0) {
                    v.setRicePack((double) Math.round(v.getQty() / v.getRiceNum() * 100) / 100);
                }
            });
            List<PickingOrderLabel> resultList = label.stream().sorted(Comparator.comparing(PickingOrderLabel::getSkuCode).thenComparing(PickingOrderLabel::getPkSeq)).collect(Collectors.toList());
            result.addAll(resultList);
        });

        return result;
    }

    public List<PickingOrderLabel> getPickingOrderLandscape(List<String> soHeaderIds) {
        List<PickingOrderLabel> result = Lists.newArrayList();
        List<PickingOrderLabel> pickingOrder = mapper.getPickingOrder(soHeaderIds);
        Map<String, List<PickingOrderLabel>> collect = pickingOrder.stream().collect(Collectors.groupingBy(s -> s.getSoNo() + s.getOrgId()));
        collect.values().forEach(label -> {
            double sumQty = label.stream().mapToDouble(PickingOrderLabel::getQty).sum();
            double sumBoxQty = label.stream().mapToDouble(PickingOrderLabel::getBoxQty).sum();
            label.forEach(v -> {
                v.setTotalQty(sumQty);
                v.setTotalBoxQty(sumBoxQty);
                if (v.getRiceNum() > 0) {
                    v.setRicePack((double) Math.round(v.getQty() / v.getRiceNum() * 100) / 100);
                }
            });
            List<PickingOrderLabel> resultList = label.stream().sorted(Comparator.comparing(PickingOrderLabel::getPkSeq)).collect(Collectors.toList());
            result.addAll(resultList);
        });

        return result;
    }

    public List<ShipOrderLabel> getShipOrder(List<String> soHeaderIds) {
        List<ShipOrderLabel> shipOrder = mapper.getShipOrder(soHeaderIds);
        if (CollectionUtil.isNotEmpty(shipOrder)) {
            for (ShipOrderLabel label : shipOrder) {
                if (label.getRiceNum() > 0) {
                    label.setRicePack((double) Math.round(label.getQtySo() / label.getRiceNum() * 100) / 100);
                }
            }
        }
        return shipOrder;
    }

    public List<BanQinWmSoEntity> rfInvGetPickBoxHeaderQuery(String toId, String orgId) {
        return mapper.rfInvGetPickBoxHeaderQuery(toId, orgId);
    }

    @Transactional
    public ResultMessage updateConsigneeInfo(BanQinWmSoEntity entity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoHeader soHeader = this.get(entity.getId());
        if (null == soHeader) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期");
            return msg;
        }
        if (Integer.parseInt(soHeader.getStatus()) >= 70) {
            msg.setSuccess(false);
            msg.setMessage("该单已经发货或取消，无法更改收货信息!");
            return msg;
        }
        List<BanQinWmSoAlloc> allocList = banQinWmSoAllocService.getBySoNo(soHeader.getSoNo(), soHeader.getOrgId());
        long hasPack = allocList.stream().filter(s -> null != s.getPackTime()).count();
        if (hasPack > 0) {
            msg.setSuccess(false);
            msg.setMessage("该单已经打包过，无法更改收货信息!");
            return msg;
        }
        mapper.updateConsigneeInfo(entity);
        // 插入更新日志
        BanQinWmUpdateConsigneeInfoLog log = new BanQinWmUpdateConsigneeInfoLog();
        log.setSoNo(soHeader.getSoNo());
        log.setCustomerNo(soHeader.getDef5());
        log.setExtendNo(soHeader.getDef16());
        log.setConsigneeName(soHeader.getContactName());
        log.setConsigneeTel(soHeader.getContactTel());
        log.setConsigneeAddr(soHeader.getContactAddr());
        log.setConsigneeArea(soHeader.getDef17());
        log.setOrgId(soHeader.getOrgId());
        wmUpdateConsigneeInfoLogService.save(log);

        msg.setMessage("操作成功");
        return msg;
    }

    @Transactional
    public ResultMessage updateCarrierInfo(BanQinWmSoEntity entity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmSoHeader soHeader = this.get(entity.getId());
        if (null == soHeader) {
            msg.setSuccess(false);
            msg.setMessage("数据已过期");
            return msg;
        }
        List<BanQinWmSoAlloc> allocList = banQinWmSoAllocService.getBySoNo(soHeader.getSoNo(), soHeader.getOrgId());
        long hasPack = allocList.stream().filter(s -> null != s.getPackTime()).count();
        if (hasPack > 0) {
            msg.setSuccess(false);
            msg.setMessage("该单已经打包过，无法更改收货信息!");
            return msg;
        }
        mapper.updateCarrierInfo(entity);

        msg.setMessage("操作成功");
        return msg;
    }

    public List<Map<String, Object>> countBySoQuery(BanQinWmSoEntity entity) {
        entity.setDataScope(GlobalDataRule.getDataRuleSql("wsh.org_id", entity.getOrgId()));
        return mapper.countBySoQuery(entity);
    }

    public List<BanQinWmSoEntity> findEntityList(BanQinWmSoEntity banQinWmSoEntity) {
        banQinWmSoEntity.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmSoEntity.getOrgId()));
        return mapper.findPage(banQinWmSoEntity);
    }

    @Transactional
    public void updateInterceptStatus(String soNo, String interceptStatus, Date interceptTime, String orgId, Integer recVer) {
        mapper.updateInterceptStatus(soNo, interceptStatus, interceptTime, orgId, recVer);
    }

    public List<String> findSoNosByWaveGroup(BanQinCdRuleWvGroupHeaderEntity entity) {
        return mapper.findSoNosByWaveGroup(entity);
    }

    public List<BanQinWmSoHeader> findSoNosForIntercept() {
        return mapper.findSoNosForIntercept();
    }

    @Transactional
    public ResultMessage importOrder(List<BanQinWmSoImportEntity> list) {
        ResultMessage msg = new ResultMessage();
        StringBuilder errorMsg = new StringBuilder();
        List<BanQinWmSoEntity> result = Lists.newArrayList();
        Map<String, List<BanQinWmSoImportEntity>> collect = list.stream().collect(Collectors.groupingBy(BanQinWmSoImportEntity::getOrderNo, LinkedHashMap::new, Collectors.toList()));
        int index = 2;
        for (Map.Entry<String, List<BanQinWmSoImportEntity>> entry : collect.entrySet()) {
            BanQinWmSoImportEntity entity = entry.getValue().get(0);
            StringBuilder checkNull = checkNullForImport(entity);
            if (StringUtils.isNotEmpty(checkNull.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkNull.toString()).append("<br>");
                index += entry.getValue().size();
                continue;
            }
            StringBuilder checkExist = checkIsExistForImport(entity);
            if (StringUtils.isNotEmpty(checkExist.toString())) {
                errorMsg.append("第[").append(index).append("]行,").append(checkExist.toString()).append("<br>");
                index += entry.getValue().size();
                continue;
            }

            BanQinWmSoEntity wmSoEntity = new BanQinWmSoEntity();
            BeanUtils.copyProperties(entity, wmSoEntity);
            wmSoEntity.setDef5(entity.getOrderNo());
            int lineIndex = 1;
            List<BanQinWmSoDetailEntity> detailList = Lists.newArrayList();
            for (BanQinWmSoImportEntity importEntity : entry.getValue()) {
                StringBuilder detailNull = checkNullForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailNull.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailNull.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }
                StringBuilder detailExist = checkIsExistForDetail(importEntity);
                if (StringUtils.isNotEmpty(detailExist.toString())) {
                    errorMsg.append("第[").append(index + lineIndex - 1).append("]行,").append(detailExist.toString()).append("<br>");
                    lineIndex++;
                    continue;
                }

                BanQinWmSoDetailEntity detailEntity = setSoDetailForImport(importEntity);
                detailEntity.setLineNo(String.format("%04d", lineIndex));
                detailList.add(detailEntity);
                lineIndex++;
            }
            wmSoEntity.setDetailList(detailList);
            result.add(wmSoEntity);
            index += entry.getValue().size();
        }

        if (errorMsg.length() > 0) {
            msg.setSuccess(false);
            msg.setMessage(errorMsg.toString());
            return msg;
        }

        for (BanQinWmSoEntity soEntity : result) {
            BanQinWmSoHeader soHeader = this.saveSoHeader(soEntity);
            for (BanQinWmSoDetailEntity detailEntity : soEntity.getDetailList()) {
                detailEntity.setHeadId(soHeader.getId());
                detailEntity.setSoNo(soHeader.getSoNo());
                banQinWmSoDetailService.saveSoDetail(detailEntity);
            }
        }

        msg.setMessage("导入成功");
        return msg;
    }

    private StringBuilder checkNullForImport(BanQinWmSoImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (null == entity.getOrderTime()) {
            builder.append("订单日期为空或订单日期格式不正确!");
        }
        if (StringUtils.isEmpty(entity.getSoType())) {
            builder.append("出库单类型为空!");
        }
        if (StringUtils.isEmpty(entity.getOwnerCode())) {
            builder.append("货主编码为空!");
        }
        if (StringUtils.isEmpty(entity.getOrderNo())) {
            builder.append("订单号为空!");
        }
        //        if (null == entity.getOutboundTime()) {
        //            builder.append("出库时间为空或出库时间格式不正确!");
        //        }
        //        builder.append(checkNullForDetail(entity));
        return builder;
    }

    private StringBuilder checkNullForDetail(BanQinWmSoImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(entity.getSkuCode())) {
            builder.append("商品编码为空!");
        }
        if (null == entity.getQty()) {
            builder.append("数量为空或格式错误!");
        }
        if (StringUtils.isEmpty(entity.getUom())) {
            builder.append("包装单位为空!");
        }
        return builder;
    }

    private StringBuilder checkIsExistForImport(BanQinWmSoImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<String> orderType = Arrays.asList(WmsCodeMaster.SO_PO.getCode(), WmsCodeMaster.SO_AO.getCode(), WmsCodeMaster.SO_RO.getCode(), WmsCodeMaster.SO_EO.getCode(),
            WmsCodeMaster.SO_LO.getCode(), WmsCodeMaster.SO_ADO.getCode(), WmsCodeMaster.SO_CO.getCode());
        if (!orderType.contains(entity.getSoType())) {
            builder.append("出库单类型值填写错误!");
        }
        BanQinEbCustomer owner = ebCustomerService.find(entity.getOwnerCode(), CustomerType.OWNER.getCode(), entity.getOrgId());
        if (null == owner) {
            builder.append("货主编码不存在!");
        }
        if (StringUtils.isNotEmpty(entity.getCarrierCode())) {
            BanQinEbCustomer carrier = ebCustomerService.find(entity.getCarrierCode(), CustomerType.CARRIER.getCode(), entity.getOrgId());
            if (null == carrier) {
                builder.append("承运商编码不存在!");
            }
        }
        if (StringUtils.isNotEmpty(entity.getContactName()) && entity.getContactName().length() > 64) {
            builder.append("联系人名称长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getContactTel()) && entity.getContactTel().length() > 32) {
            builder.append("联系人电话长度不能超过32个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getContactAddr()) && entity.getContactAddr().length() > 256) {
            builder.append("联系人地址长度不能超过256个字符!");
        }
        //        checkIsExistForDetail(entity);

        return builder;
    }

    private StringBuilder checkIsExistForDetail(BanQinWmSoImportEntity entity) {
        StringBuilder builder = new StringBuilder();
        BanQinCdWhSku item = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        if (null == item) {
            builder.append("商品编码不存在!");
        }
        if (StringUtils.isNotBlank(entity.getRotationRule())) {
            BanQinCdRuleRotationHeader rotationRule = cdRuleRotationHeaderService.findByRuleCode(entity.getRotationRule(), entity.getOrgId());
            if (null == rotationRule) {
                builder.append("库存周转规则不存在!");
            }
        }
        Pattern chinaP = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        if (StringUtils.isNotBlank(entity.getLotAtt01())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt01()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt01());
            } else {
                builder.append("生产日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt02())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt02()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt02());
            } else {
                builder.append("失效日期格式错误!");
            }
        }
        if (StringUtils.isNotBlank(entity.getLotAtt03())) {
            boolean chinaKey = chinaP.matcher(entity.getLotAtt03()).matches();
            if (chinaKey) {
                DateUtils.parseDate(entity.getLotAtt03());
            } else {
                builder.append("入库日期格式错误!");
            }
        }

        List<String> lotAtt04 = Arrays.asList(WmsCodeMaster.QC_ATT_QUALIFIED.getCode(), WmsCodeMaster.QC_ATT_NON_QUALIFIED.getCode());
        if (StringUtils.isNotEmpty(entity.getLotAtt04()) && !lotAtt04.contains(entity.getLotAtt04())) {
            builder.append("品质值填写错误!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt05()) && entity.getLotAtt05().length() > 64) {
            builder.append("批次属性5长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt06()) && entity.getLotAtt06().length() > 64) {
            builder.append("批次属性6长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt07()) && entity.getLotAtt07().length() > 64) {
            builder.append("批次属性7长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt08()) && entity.getLotAtt08().length() > 64) {
            builder.append("批次属性8长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt09()) && entity.getLotAtt09().length() > 64) {
            builder.append("批次属性9长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt10()) && entity.getLotAtt10().length() > 64) {
            builder.append("批次属性10长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt11()) && entity.getLotAtt11().length() > 64) {
            builder.append("批次属性11长度不能超过64个字符!");
        }
        if (StringUtils.isNotEmpty(entity.getLotAtt12()) && entity.getLotAtt12().length() > 64) {
            builder.append("批次属性12长度不能超过64个字符!");
        }

        return builder;
    }

    private BanQinWmSoDetailEntity setSoDetailForImport(BanQinWmSoImportEntity entity) {
        BanQinWmSoDetailEntity detailEntity = null;
        BanQinCdWhSku cdWhSku = cdWhSkuService.getByOwnerAndSkuCode(entity.getOwnerCode(), entity.getSkuCode(), entity.getOrgId());
        if (null != cdWhSku) {
            detailEntity = new BanQinWmSoDetailEntity();
            detailEntity.setOrgId(entity.getOrgId());
            detailEntity.setStatus(WmsCodeMaster.SO_NEW.getCode());
            detailEntity.setOwnerCode(cdWhSku.getOwnerCode());
            detailEntity.setSkuCode(cdWhSku.getSkuCode());
            detailEntity.setPackCode(cdWhSku.getPackCode());
            detailEntity.setUom(StringUtils.isNotBlank(entity.getUom()) ? entity.getUom() : "EA");
            BanQinCdWhPackageRelation packageUom = cdWhPackageRelationService.findByPackageUom(cdWhSku.getPackCode(), detailEntity.getUom(), cdWhSku.getOrgId());
            detailEntity.setQtySoEa(entity.getQty().doubleValue() * packageUom.getCdprQuantity());
            detailEntity.setPrice(0d);
            detailEntity.setRotationRule(StringUtils.isNotBlank(entity.getRotationRule()) ? entity.getRotationRule() : cdWhSku.getRotationRule());
            detailEntity.setAllocRule(cdWhSku.getAllocRule());
            detailEntity.setOrgId(entity.getOrgId());
            detailEntity.setLotAtt01(StringUtils.isNotBlank(entity.getLotAtt01()) ? DateUtils.parseDate(entity.getLotAtt01()) : null);
            detailEntity.setLotAtt02(StringUtils.isNotBlank(entity.getLotAtt02()) ? DateUtils.parseDate(entity.getLotAtt02()) : null);
            detailEntity.setLotAtt03(StringUtils.isNotBlank(entity.getLotAtt03()) ? DateUtils.parseDate(entity.getLotAtt03()) : null);
            detailEntity.setLotAtt04(entity.getLotAtt04());
            detailEntity.setLotAtt05(entity.getLotAtt05());
            detailEntity.setLotAtt06(entity.getLotAtt06());
            detailEntity.setLotAtt07(entity.getLotAtt07());
            detailEntity.setLotAtt08(entity.getLotAtt08());
            detailEntity.setLotAtt09(entity.getLotAtt09());
            detailEntity.setLotAtt10(entity.getLotAtt10());
            detailEntity.setLotAtt11(entity.getLotAtt11());
            detailEntity.setLotAtt12(entity.getLotAtt12());
            detailEntity.setOutboundTime(entity.getOutboundTime());
        }
        return detailEntity;
    }

    public List<ShipHandoverOrder> getShipHandoverOrder(List<String> soHeaderIds) {
        return mapper.getShipHandoverOrder(soHeaderIds);
    }

    @Transactional
    public void pushToTms(String id) {
        BanQinWmSoEntity entity = this.getEntity(id);
        if (!(WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(entity.getStatus()) || WmsCodeMaster.SO_PART_SHIPPING.getCode().equals(entity.getStatus()))) {
            throw new WarehouseException("不是部分发运或完全发运状态");
        }
        List<BanQinWmSoDetailEntity> soDetailEntities = banQinWmSoDetailService.findEntityBySoNo(entity.getSoNo(), entity.getOrgId());
        if (CollectionUtil.isEmpty(soDetailEntities)) {
            return;
        }
        entity.setDetailList(soDetailEntities.stream().filter(o -> WmsCodeMaster.SO_FULL_SHIPPING.getCode().equals(o.getStatus()) || WmsCodeMaster.SO_PART_SHIPPING.getCode().equals(o.getStatus())).collect(Collectors.toList()));

        BanQinWmSoAllocEntity con = new BanQinWmSoAllocEntity();
        con.setSoNo(entity.getSoNo());
        con.setOrgId(entity.getOrgId());
        con.setStatus(WmsCodeMaster.SO_FULL_SHIPPING.getCode());
        List<BanQinWmSoAllocEntity> soAllocEntities = banQinWmSoAllocService.findByEntity(con);
        if (CollectionUtil.isEmpty(soAllocEntities)) {
            return;
        }
        entity.setAllocList(soAllocEntities);
        pushTmsService.pushWmsToTransport(entity);
        mapper.pushedToTms(id, WmsConstants.YES);
    }

    @Transactional
    public void cancelPushToTms(String id) {
        BanQinWmSoHeader wmSoHeader = this.get(id);
        if (wmSoHeader == null || !WmsConstants.YES.equals(wmSoHeader.getIsPushed())) return;
        pushTmsService.deleteTransportTask(wmSoHeader.getSoNo(), SystemAliases.WMS.getCode(), wmSoHeader.getOrgId());
        mapper.pushedToTms(id, WmsConstants.NO);
    }
}