package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitEntity;
import com.yunyou.modules.wms.kit.mapper.BanQinWmKitHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 加工单Service
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmKitHeaderService extends CrudService<BanQinWmKitHeaderMapper, BanQinWmKitHeader> {
    // 公共
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    // 自动生成
    @Autowired
    protected SynchronizedNoService noService;
    // 子件明细
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    private BanQinKitRemoveService banQinKitRemoveService;

    /**
     * 描述：根据加工单号获取Model
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitHeader getByKitNo(String kitNo, String orgId) {
        BanQinWmKitHeader model = new BanQinWmKitHeader();
        model.setKitNo(kitNo);
        model.setOrgId(orgId);
        List<BanQinWmKitHeader> list = this.findList(model);
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：根据ID查询Entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    /**
     * 描述：根据加工单号查询Entity
     * <p>
     * create by Jianhua on 2019/8/23
     */
    public BanQinWmKitEntity getEntityByKitNo(String kitNo, String orgId) {
        return mapper.getEntityByKitNo(kitNo, orgId);
    }

    /**
     * 描述：审核
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void audit(String kitNo, String orgId) throws WarehouseException {
        BanQinWmKitHeader model = getByKitNo(kitNo, orgId);
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_NOT.getCode())) {
            // 订单{0}不审核
            throw new WarehouseException("订单" + kitNo + "不审核");
        }
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            // 订单{0}已审核
            throw new WarehouseException("订单" + kitNo + "已审核");
        }
        if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            // 订单{0}不是创建状态，不能审核
            throw new WarehouseException("订单" + kitNo + "不是创建状态，不能审核");
        }
        // 审核状态
        model.setAuditStatus(WmsCodeMaster.AUDIT_CLOSE.getCode());
        model.setAuditOp(UserUtils.getUser().getName());// 审核人
        model.setAuditTime(new Date());// 审核时间
        this.save(model);
    }

    /**
     * 描述：保存加工单
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmKitEntity saveEntity(BanQinWmKitEntity entity) throws WarehouseException {
        if (StringUtils.isBlank(entity.getId())) {
            // 生成编号
            entity.setKitNo(noService.getDocumentNo(GenNoType.WM_KIT_NO.name()));
            // 订单状态
            entity.setStatus(WmsCodeMaster.KIT_NEW.getCode());
            // 审核状态
            String isAudit = WmsConstants.YES;/*wmCommon.getSysControlParam(ControlParamCode.KIT_AUDIT.getCode());*/
            if (WmsConstants.YES.equals(isAudit)) {
                entity.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());// 需要审核，状态为00
            } else {
                entity.setAuditStatus(WmsCodeMaster.AUDIT_NOT.getCode());// 不需要审核，状态为90
            }
            entity.setAuditOp(null);
            entity.setAuditTime(null);
        }
        this.save(entity);
        return getEntity(entity.getId());
    }

    /**
     * 描述：取消审核
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void cancelAudit(String kitNo, String orgId) throws WarehouseException {
        BanQinWmKitHeader model = getByKitNo(kitNo, orgId);
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_NOT.getCode())) {
            throw new WarehouseException(kitNo + "不审核，不能操作");
        }
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_NEW.getCode())) {
            throw new WarehouseException(kitNo + "未审核，不能操作");
        }
        if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            throw new WarehouseException(kitNo + "不是创建状态，不能操作");
        }
        List<BanQinWmKitSubDetail> subList = banQinWmKitSubDetailService.getByKitNo(kitNo, orgId);
        if (subList.size() > 0) {
            throw new WarehouseException(kitNo + "已经生成子件,不能操作");
        }
        // 审核状态
        model.setAuditStatus(WmsCodeMaster.AUDIT_NEW.getCode());
        model.setAuditOp(null);// 审核人
        model.setAuditTime(null);// 审核时间
        this.save(model);
    }

    /**
     * 描述：根据加工单号更新加工单状态
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void updateStatus(String kitNo, String orgId) {
        BanQinWmKitHeader model = new BanQinWmKitHeader();
        model.setKitNo(kitNo);
        model.setOrgId(orgId);
        model.setUpdateBy(UserUtils.getUser());
        model.setUpdateDate(new Date());
        mapper.updateStatus(model);
    }

    @Transactional
    public ResultMessage removeEntity(String[] ids) {
        ResultMessage msg = new ResultMessage();
        for (String id : ids) {
            BanQinWmKitHeader banQinWmKitHeader = get(id);
            banQinKitRemoveService.removeKitEntity(banQinWmKitHeader.getKitNo(), banQinWmKitHeader.getOrgId());
        }
        msg.setMessage("操作成功");
        return msg;
    }

}