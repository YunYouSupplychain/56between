package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.common.service.WmsUtil;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetail;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdDetailEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdHeader;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmAdDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 调整单明细Service
 * @author WMJ
 * @version 2019-01-28
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAdDetailService extends CrudService<BanQinWmAdDetailMapper, BanQinWmAdDetail> {
    @Autowired
    private WmsUtil wmsUtil;
    @Autowired
    @Lazy
    private BanQinWmAdHeaderService wmAdHeaderService;
    @Autowired
    @Lazy
    private BanQinInventoryService inventoryService;
    @Autowired
    @Lazy
    private BanQinWmInvSerialService invSerialService;

	public BanQinWmAdDetail get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmAdDetail> findList(BanQinWmAdDetail banQinWmAdDetail) {
		return super.findList(banQinWmAdDetail);
	}
	
	public Page<BanQinWmAdDetailEntity> findPage(Page page, BanQinWmAdDetailEntity banQinWmAdDetailEntity) {
        dataRuleFilter(banQinWmAdDetailEntity);
        banQinWmAdDetailEntity.setPage(page);
        page.setList(mapper.findPage(banQinWmAdDetailEntity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmAdDetail banQinWmAdDetail) {
		super.save(banQinWmAdDetail);
	}

    @Transactional
    public ResultMessage saveEntity(BanQinWmAdDetailEntity wmAdDetailEntity) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdDetail model = new BanQinWmAdDetail();
        // 保存明细 校验单头状态
        // 获取调整单头信息
        BanQinWmAdHeader example = new BanQinWmAdHeader();
        example.setAdNo(wmAdDetailEntity.getAdNo());
        example.setOrgId(wmAdDetailEntity.getOrgId());
        BanQinWmAdHeader wmAdHeaderModel = wmAdHeaderService.findFirst(example);
        if (wmAdHeaderModel == null) {
            msg.setSuccess(false);
            msg.addMessage(wmAdDetailEntity.getAdNo() + "数据过期");
            return msg;
        }
        // 当状态不为创建或已审核，说明状态已经发生变化，不能保存
        if (WmsCodeMaster.AUDIT_CLOSE.getCode().equals(wmAdHeaderModel.getAuditStatus()) || !WmsCodeMaster.AD_NEW.getCode().equals(wmAdHeaderModel.getStatus())) {
            msg.setSuccess(false);
            msg.addMessage(wmAdHeaderModel.getAdNo() + "不是创建状态");
            return msg;
        }
        BeanUtils.copyProperties(wmAdDetailEntity, model);
        if (StringUtils.isEmpty(model.getId())) {
            model.setId(IdGen.uuid());
            model.setLineNo(wmsUtil.getMaxLineNo("wm_ad_detail", "header_id", model.getHeaderId()));
        }
        String lotNum = null;
        // 生成批次号
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(wmAdDetailEntity.getOwnerCode());
        wmInvLotAttModel.setSkuCode(wmAdDetailEntity.getSkuCode());
        wmInvLotAttModel.setLotAtt01(wmAdDetailEntity.getLotAtt01());
        wmInvLotAttModel.setLotAtt02(wmAdDetailEntity.getLotAtt02());
        wmInvLotAttModel.setLotAtt03(wmAdDetailEntity.getLotAtt03());
        wmInvLotAttModel.setLotAtt04(wmAdDetailEntity.getLotAtt04());
        wmInvLotAttModel.setLotAtt05(wmAdDetailEntity.getLotAtt05());
        wmInvLotAttModel.setLotAtt06(wmAdDetailEntity.getLotAtt06());
        wmInvLotAttModel.setLotAtt07(wmAdDetailEntity.getLotAtt07());
        wmInvLotAttModel.setLotAtt08(wmAdDetailEntity.getLotAtt08());
        wmInvLotAttModel.setLotAtt09(wmAdDetailEntity.getLotAtt09());
        wmInvLotAttModel.setLotAtt10(wmAdDetailEntity.getLotAtt10());
        wmInvLotAttModel.setLotAtt11(wmAdDetailEntity.getLotAtt11());
        wmInvLotAttModel.setLotAtt12(wmAdDetailEntity.getLotAtt12());
        wmInvLotAttModel.setOrgId(wmAdDetailEntity.getOrgId());
        lotNum = inventoryService.createInvLotNum(wmInvLotAttModel);
        model.setLotNum(lotNum);
        // 是序列号管理商品必须同时保存序列号调整表
        if (WmsConstants.YES.equals(wmAdDetailEntity.getIsSerial())) {
            invSerialService.saveAdDetailAndSerial(wmAdDetailEntity, model);
        } else {
            this.save(model);
        }
        msg.addMessage("操作成功");
        msg.setSuccess(true);
        return msg;
    }
	
	@Transactional
	public void delete(BanQinWmAdDetail banQinWmAdDetail) {
		super.delete(banQinWmAdDetail);
	}
	
	public List<BanQinWmAdDetail> findByHeaderId(String headerId, String orgId) {
        BanQinWmAdDetail wmAdDetail = new BanQinWmAdDetail();
        wmAdDetail.setOrgId(orgId);
        wmAdDetail.setHeaderId(headerId);
        return findList(wmAdDetail);
    }
    
    public List<BanQinWmAdDetailEntity> wmAdCheckSerialQuery(BanQinWmAdDetail banQinWmAdDetail) {
	    return mapper.wmAdCheckSerialQuery(banQinWmAdDetail);
    }

    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        BanQinWmAdDetail newModel = new BanQinWmAdDetail();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        List<BanQinWmAdDetail> list = this.findList(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (CollectionUtil.isNotEmpty(list)) {
            msg.setSuccess(false);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }


}