package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSku;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.BanQinWmsCommonService;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitHeader;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：加工管理-生成子件
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitGenerateSubService {
    @Autowired
    protected BanQinWmsCommonService wmCommon;
    @Autowired
    protected BanQinWmKitHeaderService banQinWmKitHeaderService;
    @Autowired
    protected BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    protected BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    protected BanQinCdWhBomDetailService banQinCdWhBomDetailService;
    @Autowired
    protected BanQinCdWhSkuService banQinCdWhSkuService;

    /**
     * 描述：生成子件
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void generateSub(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        // 加工单，获取货主、加工类型
        BanQinWmKitHeader headerModel = banQinWmKitHeaderService.getByKitNo(kitNo, orgId);
        // 父件明细，获取父件、加工数
        BanQinWmKitParentDetail parentModel = banQinWmKitParentDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        if (parentModel.getIsCreateSub().equals(WmsConstants.YES)) {
            throw new WarehouseException("[" + kitNo + "][" + parentLineNo + "]已经生成子件，不能操作");
        }
        // 获取基础子件信息，按行号升序排序(关系到后续的加工确认，加工单子件与基础子件的对应取值和计算)
        List<BanQinCdWhBomDetail> bomModels = banQinCdWhBomDetailService.getByOwnerAndParentSkuAndKitType(headerModel.getOwnerCode(), parentModel.getParentSkuCode(), headerModel.getKitType(), headerModel.getOrgId());
        if (CollectionUtil.isEmpty(bomModels)) {
            throw new WarehouseException("[" + kitNo + "][" + parentLineNo + "]没有子件明细，不能操作");
        }
        // 子件对象
        // 循环基础子件，写子件明细
        for (BanQinCdWhBomDetail bomModel : bomModels) {
            BanQinWmKitSubDetail model = new BanQinWmKitSubDetail();
            model.setId(IdGen.uuid());
            model.setIsNewRecord(true);
            model.setStatus(WmsCodeMaster.KIT_NEW.getCode());// 状态00
            model.setKitNo(kitNo);// 行号
            model.setParentLineNo(parentModel.getParentLineNo());// 父件行号
            model.setOwnerCode(headerModel.getOwnerCode());// 货主
            model.setSubSkuCode(bomModel.getSubSkuCode());// 子件编码
            model.setPackCode(bomModel.getPackCode());// 包装规格
            model.setUom(bomModel.getUom());// 包装单位
            model.setQtyPlanEa(parentModel.getQtyPlanEa() * bomModel.getQtyEa());// 待加工子件数EA
            model.setSubSkuType(bomModel.getSubSkuType());// 子件类型
            model.setQtyBomEa(bomModel.getQtyEa());// BOM子件数量
            // 查询商品的分配规则和库存周转规则
            BanQinCdWhSku skuModel = this.banQinCdWhSkuService.getByOwnerAndSkuCode(headerModel.getOwnerCode(), bomModel.getSubSkuCode(), orgId);
            model.setAllocRule(skuModel.getAllocRule());// 分配规则
            model.setRotationRule(skuModel.getRotationRule());// 库存周转规则
            model.setSubLineNo(banQinWmKitSubDetailService.getNewLineNo(kitNo, orgId));// 子件行号
            model.setOrgId(orgId);
            model.setHeaderId(headerModel.getId());
            // 保存
            this.banQinWmKitSubDetailService.save(model);
        }
        // 保存父件明细，标记是否生成子件
        parentModel.setIsCreateSub(WmsConstants.YES);
        this.banQinWmKitParentDetailService.save(parentModel);

    }

    /**
     * 描述：取消生成子件
     * <p>
     * create by Jianhua on 2019/8/25
     */
    @Transactional
    public void cancelGenerateSub(String kitNo, String parentLineNo, String orgId) throws WarehouseException {
        // 父件明细，获取父件、加工数
        BanQinWmKitParentDetail parentModel = banQinWmKitParentDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        if (parentModel.getIsCreateSub().equals(WmsConstants.NO)) {
            throw new WarehouseException("[" + kitNo + "][" + parentLineNo + "]未生成子件，不能操作");
        }
        // 删除子件明细
        List<BanQinWmKitSubDetail> subModels = this.banQinWmKitSubDetailService.getByKitNoAndParentLineNo(kitNo, parentLineNo, orgId);
        for (BanQinWmKitSubDetail subModel : subModels) {
            if (!subModel.getStatus().equals(WmsCodeMaster.SUB_KIT_NEW.getCode())) {
                throw new WarehouseException("[" + parentLineNo + "][" + subModel.getSubLineNo() + "]不是创建状态，不能操作");
            }
            banQinWmKitSubDetailService.delete(subModel);
        }
        // 保存父件明细，标记是否生成子件
        parentModel.setIsCreateSub(WmsConstants.NO);
        this.banQinWmKitParentDetailService.save(parentModel);
    }
}
