package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLot;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvLotMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批次库存表Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvLotService extends CrudService<BanQinWmInvLotMapper, BanQinWmInvLot> {

	public BanQinWmInvLot get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmInvLot> findList(BanQinWmInvLot banQinWmInvLot) {
		return mapper.findList(banQinWmInvLot);
	}
	
	public Page<BanQinWmInvLot> findPage(Page<BanQinWmInvLot> page, BanQinWmInvLot banQinWmInvLot) {
		return super.findPage(page, banQinWmInvLot);
	}
	
	@Transactional
	public void save(BanQinWmInvLot banQinWmInvLot) {
		super.save(banQinWmInvLot);
	}
	
	@Transactional
	public void delete(BanQinWmInvLot banQinWmInvLot) {
		super.delete(banQinWmInvLot);
	}
    
	public BanQinWmInvLot findFirst(BanQinWmInvLot banQinWmInvLot) {
        List<BanQinWmInvLot> list = this.findList(banQinWmInvLot);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
    /**
     * 通过批次号查找批次库存记录
     * @param lotNum
     * @return
     */
    public BanQinWmInvLot getByLotNum(String lotNum, String orgId) {
        BanQinWmInvLot wmInvLotModel = new BanQinWmInvLot();
        wmInvLotModel.setLotNum(lotNum);
        wmInvLotModel.setOrgId(orgId);
        return this.findFirst(wmInvLotModel);
    }

    /**
     * 可分配数量
     * @param invLotModel
     * @return
     */
    public Double getQtyPreallocAvailable(BanQinWmInvLot invLotModel) {
        return invLotModel.getQty() - invLotModel.getQtyHold() - invLotModel.getQtyPrealloc() - invLotModel.getQtyAlloc() - invLotModel.getQtyPk();
    }

    /**
     * 根据货主和商品获取wmInvLotModel
     * @param ownerCode
     * @param skuCode
     * @return
     */
    public ResultMessage getBySkuCodeAndOwnerCode(String ownerCode, String skuCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmInvLot newModel = new BanQinWmInvLot();
        // 设置查询对象的值
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmInvLot wmInvLotModel = this.findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != wmInvLotModel) {
            msg.setSuccess(false);
            msg.setData(wmInvLotModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    /**
     * 根据货主和商品获取wmInvLotModel
     * @param ownerCode
     * @param skuCode
     * @return
     */
    public List<BanQinWmInvLot> getBySkuCodeAndOwnerCode2(String ownerCode, String skuCode, String orgId) {
        BanQinWmInvLot newModel = new BanQinWmInvLot();
        newModel.setOwnerCode(ownerCode);
        newModel.setSkuCode(skuCode);
        newModel.setOrgId(orgId);
        return this.findList(newModel);
    }

    /**
     * 判断商品是否存在待检库存
     * 校验商品是可以修改是否质检管理属性，如果从是到否，lot04不能Q并且上商品批次属性04需为必填，从否到是，lot04不能为空
     * @param ownerCode
     * @param skuCode
     * @param isQc
     * @return
     */
    public boolean checkIsToQc(String ownerCode, String skuCode, String isQc, String orgId) {
        BanQinWmInvLot condition = new BanQinWmInvLot();
        condition.setOwnerCode(ownerCode);
        condition.setSkuCode(skuCode);
        condition.setIsQc(isQc);
        condition.setOrgId(orgId);
        List<String> result = mapper.checkSkuIsToQcQuery(condition);
        
        return CollectionUtil.isNotEmpty(result);
    }
	
}