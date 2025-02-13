package com.yunyou.modules.wms.kit.service;

import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitResultDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitStepEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理- 前后交互方法接口类 
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitRemoveService {
    @Autowired
    protected BanQinCdWhBomHeaderService banQinCdWhBomHeaderService;
    @Autowired
    protected BanQinCdWhBomDetailService banQinCdWhBomDetailService;
    @Autowired
    protected BanQinCdWhBomStepService banQinCdWhBomStepService;
    @Autowired
    @Lazy
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinWmKitResultDetailService banQinWmKitResultDetailService;
    @Autowired
    protected BanQinWmKitStepService banQinWmKitStepService;

    /**
     * 描述：根据货主、父件编码、加工类型删除BOM单信息
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeBomEntity(String ownerCode, String parentSkuCode, String kitType, String orgId) throws WarehouseException {
        BanQinCdWhBomHeader model = banQinCdWhBomHeaderService.getByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
        if (model != null) {
            // 校验是否被引用
            banQinCdWhBomHeaderService.checkIsReferenced(model);
            banQinCdWhBomStepService.deleteByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
            banQinCdWhBomDetailService.deleteByOwnerAndParentSkuAndKitType(ownerCode, parentSkuCode, kitType, orgId);
            banQinCdWhBomHeaderService.delete(model);
        }
    }

    /**
     * 描述：删除加工单
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeKitEntity(String kitNo, String orgId) throws WarehouseException {
        BanQinWmKitHeader model = banQinWmKitHeaderService.getByKitNo(kitNo, orgId);
        // 如果状态不为新增，那么不能删除
        if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
            throw new WarehouseException(model.getKitNo() + "不是创建状态，不能操作");
        }
        // 订单状态00，但审核，不能删除
        if (model.getAuditStatus().equals(WmsCodeMaster.AUDIT_CLOSE.getCode())) {
            throw new WarehouseException(model.getKitNo() + "已审核，不能操作");
        }
        // 删除父件加工结果明细和加工工序明细
        this.removeKitResultByKitNo(kitNo, orgId);
        // 删除父件明细
        banQinWmKitParentDetailService.removeByKitNo(kitNo, orgId);
        // 删除加工单
        banQinWmKitHeaderService.delete(model);
    }

    /**
     * 描述：删除父件明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeKitParent(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        // 删除父件加工结果明细和加工工序明细
        this.removeKitResultByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        // 删除父件明细
        banQinWmKitParentDetailService.removeByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
    }

    /**
     * 描述：按加工单删除父件加工结果明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeKitResultByKitNo(String kitNo, String orgId) throws WarehouseException {
        List<BanQinWmKitResultDetailEntity> entities = banQinWmKitResultDetailService.getEntityByKitNo(kitNo, orgId);
        // 删除结果集
        String[] kitLineNos = new String[entities.size()];
        int i = 0;
        // 循环商品明细
        for (BanQinWmKitResultDetailEntity entity : entities) {
            // 如果明细状态不是新增或者取消状态，那么不能删除
            if ((!entity.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) && (!entity.getStatus().equals(WmsCodeMaster.KIT_CANCEL.getCode()))) {
                throw new WarehouseException("[" + entity.getKitNo() + "][" + entity.getParentLineNo() + "]不是创建或者取消状态，不能操作");
            }
            kitLineNos[i] = entity.getKitLineNo();
            // 删除父件加工结果明细
            this.banQinWmKitResultDetailService.delete(entity);
            i++;
        }
        // 删除父件加工结果的加工工序
        List<BanQinWmKitStepEntity> stepEntities = banQinWmKitStepService.getEntityByKitNoAndKitLineNo(kitNo, kitLineNos, orgId);
        for (BanQinWmKitStepEntity entity : stepEntities) {
            banQinWmKitStepService.delete(entity);
        }
    }

    /**
     * 描述：根据加工单、父件加工结果行号删除父件加工结果明细
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void removeKitResultByKitNoAndParentLineNo(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        List<BanQinWmKitResultDetail> models = banQinWmKitResultDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        int size = models.size();
        String[] kitLineNos = new String[size];
        for (int i = 0; i < size; i++) {
            BanQinWmKitResultDetail model = models.get(i);
            if (!model.getStatus().equals(WmsCodeMaster.KIT_NEW.getCode())) {
                throw new WarehouseException("[" + kitNo + "][" + parentLineNo + "不是创建状态，不能操作");
            }
            kitLineNos[i] = model.getKitLineNo();
        }

        // 删除父件加工结果的加工工序
        List<BanQinWmKitStepEntity> stepEntities = banQinWmKitStepService.getEntityByKitNoAndKitLineNo(kitNo, kitLineNos, orgId);
        for (BanQinWmKitStepEntity entity : stepEntities) {
            banQinWmKitStepService.delete(entity);
        }
        // 删除父件加工结果明细
        for (BanQinWmKitResultDetail model : models) {
            this.banQinWmKitResultDetailService.delete(model);
        }
    }
}
