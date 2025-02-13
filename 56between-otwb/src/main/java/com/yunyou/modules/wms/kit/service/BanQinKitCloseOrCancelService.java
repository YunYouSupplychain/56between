package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPa;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理-关闭、取消
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
@Transactional(readOnly = true)
public class BanQinKitCloseOrCancelService {
    @Autowired
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;

    /**
     * 描述：关闭加工单
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void close(String kitNo, String orgId) throws WarehouseException {
        BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(kitNo, orgId);
        // 是否完全加工后才可关闭
        String kitOnlyFullKitClose = WmsConstants.YES;/*wmCommon.getSysControlParam(ControlParamCode.KIT_ONLY_FULL_KIT_CLOSE.getCode());*/
        if (kitOnlyFullKitClose.equals(WmsConstants.YES) && !model.getStatus().equals(WmsCodeMaster.KIT_FULL_KIT.getCode())) {
            throw new WarehouseException(kitNo + "非完全加工状态，不能操作");
        } else {
            // 部分加工，可关闭
            // 如果订单有分配数、拣货数，那么提示不可关闭
            List<BanQinWmKitSubDetail> subDetailList = banQinWmKitSubDetailService.getByKitNo(kitNo, orgId);
            for (BanQinWmKitSubDetail subDetail : subDetailList) {
                if (subDetail.getQtyAllocEa() + subDetail.getQtyPkEa() > 0) {
                    throw new WarehouseException(kitNo + "存在分配数或拣货数，不能操作");
                }
            }
        }
        // 如果订单存在未完成上架任务，那么不可关闭
        BanQinWmTaskPa taskPaModel = new BanQinWmTaskPa();
        taskPaModel.setOrderNo(kitNo);
        taskPaModel.setStatus(WmsCodeMaster.TSK_NEW.getCode());
        taskPaModel.setOrgId(orgId);
        List<BanQinWmTaskPa> wmTaskPaList = banQinWmTaskPaService.findList(taskPaModel);
        if (CollectionUtil.isNotEmpty(wmTaskPaList)) {
            throw new WarehouseException(kitNo + "存在未完成上架任务，不能操作");
        }
        // 状态更新close
        model.setStatus(WmsCodeMaster.KIT_CLOSE.getCode());
        // 保存
        this.banQinWmKitHeaderService.save(model);
    }

    /**
     * 描述：取消加工单
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void cancelKit(String kitNo, String orgId) throws WarehouseException {
        // 获取出库单
        BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(kitNo, orgId);
        // 订单状态00，但审核，不能取消
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            throw new WarehouseException("订单" + kitNo + "已审核");
        }
        // 如果订单不是新增状态，不能取消
        if (model.getStatus().compareTo(WmsCodeMaster.KIT_NEW.getCode()) > 0) {
            throw new WarehouseException(kitNo + "不是创建状态，不能操作");
        }
        // 如果不审核并且已经生成子件，不能取消
        List<BanQinWmKitSubDetail> subModels = this.banQinWmKitSubDetailService.getByKitNo(kitNo, orgId);
        if (CollectionUtil.isNotEmpty(subModels)) {
            throw new WarehouseException(kitNo + "已经生成子件,不能操作");
        }
        // 状态更新取消
        model.setStatus(WmsCodeMaster.KIT_CANCEL.getCode());
        this.banQinWmKitHeaderService.save(model);
    }

    /**
     * 描述：取消加工单父件
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void cancelKitParent(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        // 获取加工单父件明细行
        BanQinWmKitParentDetail parentModel = banQinWmKitParentDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        // 如果不是新增状态，不能取消
        if (parentModel.getStatus().compareTo(WmsCodeMaster.KIT_NEW.getCode()) > 0) {
            throw new WarehouseException("订单[" + kitNo + "]行号[" + parentLineNo + "]不是新增状态");
        }
        // 如果已经生成子件，不能取消
        List<BanQinWmKitSubDetail> subModels = this.banQinWmKitSubDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        if (subModels.size() > 0) {
            throw new WarehouseException("订单[" + kitNo + "]行号[" + parentLineNo + "]已经生成子件,不能操作");
        }
        // 订单明细行状态更新取消
        parentModel.setStatus(WmsCodeMaster.KIT_CANCEL.getCode());
        // 保存
        this.banQinWmKitParentDetailService.save(parentModel);
        // 更新加工单状态
        this.banQinWmKitHeaderService.updateStatus(kitNo, orgId);
    }

}
