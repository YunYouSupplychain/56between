package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfDetailEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTfHeader;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmTfDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 转移单明细Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmTfDetailService extends CrudService<BanQinWmTfDetailMapper, BanQinWmTfDetail> {
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    @Lazy
    private BanQinWmTfHeaderService wmTfHeaderService;
    @Autowired
    @Lazy
    private BanQinInvRemoveService invRemoveService;
    @Autowired
    @Lazy
    private BanQinInventoryService inventoryService;

	public BanQinWmTfDetail get(String id) {
		return super.get(id);
	}

    public BanQinWmTfDetailEntity getEntity(String id) {
        return mapper.getEntity(id);
    }
	
	public List<BanQinWmTfDetail> findList(BanQinWmTfDetail banQinWmTfDetail) {
		return super.findList(banQinWmTfDetail);
	}
	
	public Page<BanQinWmTfDetailEntity> findPage(Page page, BanQinWmTfDetailEntity banQinWmTfDetailEntity) {
        dataRuleFilter(banQinWmTfDetailEntity);
        banQinWmTfDetailEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmTfDetailEntity));
		return page;
	}
	
	public BanQinWmTfDetail findFirst(BanQinWmTfDetail banQinWmTfDetail) {
        List<BanQinWmTfDetail> list = this.findList(banQinWmTfDetail);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
	@Transactional
	public void save(BanQinWmTfDetail banQinWmTfDetail) {
		super.save(banQinWmTfDetail);
	}
	
	@Transactional
	public void delete(BanQinWmTfDetail banQinWmTfDetail) {
		super.delete(banQinWmTfDetail);
	}

    /**
     * 保存转移单号明细
     * @param wmTfDetailEntity
     * @return
     */
    @Transactional
    public ResultMessage saveTfDetailEntity(BanQinWmTfDetailEntity wmTfDetailEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmTfDetail model = new BanQinWmTfDetail();
        String lotnum = null;
        // 保存明细 校验单头状态
        // 获取转移单头信息
        BanQinWmTfHeader wmTfHeaderModel = wmTfHeaderService.get(wmTfDetailEntity.getHeaderId());
        if (wmTfHeaderModel == null) {
            msg.setSuccess(false);
            msg.addMessage(wmTfDetailEntity.getTfNo() + "数据过期");
            return msg;
        }
        // 当状态不为创建或已审核，说明状态已经发生变化，不能保存
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmTfHeaderModel.getAuditStatus()) || !WmsCodeMaster.TF_NEW.getCode().equals(wmTfHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmTfHeaderModel.getTfNo() + "状态不为创建或已审核");
            return msg;
        }
        BeanUtils.copyProperties(wmTfDetailEntity, model);
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(IdGen.uuid());
            model.setLineNo(wmsUtil.getMaxLineNo("wm_tf_detail", "header_id", model.getHeaderId()));
        }
        // 生成批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(model.getToOwner());
        wmInvLotAttModel.setSkuCode(model.getToSku());
        wmInvLotAttModel.setOrgId(model.getOrgId());
        wmInvLotAttModel.setLotAtt01(wmTfDetailEntity.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(wmTfDetailEntity.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(wmTfDetailEntity.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(wmTfDetailEntity.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(wmTfDetailEntity.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(wmTfDetailEntity.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(wmTfDetailEntity.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(wmTfDetailEntity.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(wmTfDetailEntity.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(wmTfDetailEntity.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(wmTfDetailEntity.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(wmTfDetailEntity.getLotAtt12());
        lotnum = inventoryService.createInvLotNum(wmInvLotAttModel);
        model.setToLot(lotnum);
        this.save(model);
        msg.setSuccess(true);
        msg.addMessage("操作成功");
        return msg;
    }
	
    /**
     * 删除转移单明细
     * @param headerId
     * @param detailIds
     * @return
     */
    @Transactional
    public ResultMessage removeTfDetailEntity(String headerId, String[] detailIds) {
        ResultMessage msg = new ResultMessage();
        ResultMessage headerMsg = wmTfHeaderService.checkNewAndNotAudit(headerId);
        if (!headerMsg.isSuccess()) {
            msg.setSuccess(false);
            msg.addMessage(headerMsg.getMessage());
            return msg;
        }
        for (String id : detailIds) {
            ResultMessage detailMsg = invRemoveService.removeTfDetailById(id);
            if (!detailMsg.isSuccess()) {
                msg.addMessage(detailMsg.getMessage());
            }
        }
        if (StringUtils.isEmpty(msg.getMessage())) {
            msg.setSuccess(true);
            msg.addMessage("操作成功");
        }
        return msg;
    }
	
	public List<BanQinWmTfDetail> findByHeaderId(String headerId, String orgId) {
        BanQinWmTfDetail banQinWmTfDetail = new BanQinWmTfDetail();
        banQinWmTfDetail.setOrgId(orgId);
        banQinWmTfDetail.setHeaderId(headerId);
        return findList(banQinWmTfDetail);
    }
    
    public List<BanQinWmTfDetailEntity> wmTfIsSerialQuery(String tfNo, String orgId) {
        BanQinWmTfDetailEntity wmTfDetail = new BanQinWmTfDetailEntity();
        wmTfDetail.setTfNo(tfNo);
        wmTfDetail.setOrgId(orgId);
	    return mapper.wmTfIsSerialQuery(wmTfDetail);
    }

    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmTfDetail newModel = new BanQinWmTfDetail();
        // 设置查询对象的值
        newModel.setFmOwner(ownerCode);
        newModel.setFmSku(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmTfDetail wmTfDetailModel = this.findFirst(newModel);

        BanQinWmTfDetail model = new BanQinWmTfDetail();
        // 设置查询对象的值
        model.setToOwner(ownerCode);
        model.setToSku(skuCode);
        model.setOrgId(orgId);
        // 查询出调用此商品的对象
        BanQinWmTfDetail wmTfDetailModel2 = this.findFirst(model);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != wmTfDetailModel || null != wmTfDetailModel2) {
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }
}