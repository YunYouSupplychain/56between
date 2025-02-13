package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitParentDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitResultDetailEntity;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitStepEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理 复制方法类
 * <p>
 * create by Jianhua on 2019/8/23
 */
@Service
@Transactional(readOnly = true)
public class BanQinKitDuplicateService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    protected BanQinWmKitResultDetailService banQinWmKitResultDetailService;
    @Autowired
    protected BanQinWmKitStepService banQinWmKitStepService;

    /**
     * 描述：复制加工单
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void duplicateKitEntity(BanQinWmKitEntity entity) throws WarehouseException {
        // 复制加工单
        String kitNo = entity.getKitNo();
        entity.setId(null);// 主键清空
        entity.setLogisticNo(null);// 物流单号不复制
        entity.setRecVer(0);// 版本号清空
        // 保存加工单
        entity = banQinWmKitHeaderService.saveEntity(entity);
        // 复制父件明细
        List<BanQinWmKitParentDetailEntity> parentEntities = banQinWmKitParentDetailService.getEntityByKitNo(kitNo, entity.getOrgId());
        if (CollectionUtil.isEmpty(parentEntities)) {
            return;
        }
        for (BanQinWmKitParentDetailEntity parentEntity : parentEntities) {
            // 复制父件明细，包括加工结果、加工工序
            parentEntity.setKitNo(entity.getKitNo());
            parentEntity.setHeaderId(entity.getId());
            this.duplicateKitParentEntity(parentEntity);
        }
    }

    /**
     * 描述：复制加工单父件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public void duplicateKitParentEntity(BanQinWmKitParentDetailEntity parentEntity) throws WarehouseException {
        // 复制父件明细
        parentEntity.setId(null);// 主键清空
        parentEntity.setLogisticNo(null);// 物流单号不复制
        parentEntity.setLogisticLineNo(null);
        parentEntity.setQtyKitEa(0D);// 实际加工数置0
        parentEntity.setRecVer(0);// 版本号清空
        this.saveKitParentEntity(parentEntity);
    }

    /**
     * 描述：保存加工父件明细
     * <p>
     * create by Jianhua on 2019/8/23
     */
    @Transactional
    public BanQinWmKitParentDetailEntity saveKitParentEntity(BanQinWmKitParentDetailEntity parentEntity) throws WarehouseException {
        parentEntity = banQinWmKitParentDetailService.saveKitParent(parentEntity);
        if (parentEntity != null) {
            // 保存父件加工明细结果
            BanQinWmKitResultDetailEntity resultEntity = banQinWmKitResultDetailService.saveEntity(parentEntity);
            // 保存加工工序
            List<BanQinWmKitStepEntity> stepEntities = banQinWmKitStepService.saveEntityByResult(resultEntity);
            // 设置
            parentEntity.setResultEntity(resultEntity);
            resultEntity.setStepEntitys(stepEntities);
        }
        return parentEntity;
    }

}
