package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAtt;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAttEntity;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmInvLotAttMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批次号库存表Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmInvLotAttService extends CrudService<BanQinWmInvLotAttMapper, BanQinWmInvLotAtt> {

	public BanQinWmInvLotAtt get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmInvLotAtt> findList(BanQinWmInvLotAtt banQinWmInvLotAtt) {
		return mapper.findList(banQinWmInvLotAtt);
	}
	
	public Page<BanQinWmInvLotAttEntity> findPage(Page page, BanQinWmInvLotAttEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmInvLotAtt banQinWmInvLotAtt) {
		super.save(banQinWmInvLotAtt);
	}
	
	@Transactional
	public void delete(BanQinWmInvLotAtt banQinWmInvLotAtt) {
		super.delete(banQinWmInvLotAtt);
	}
	
	public BanQinWmInvLotAtt findFirst(BanQinWmInvLotAtt banQinWmInvLotAtt) {
        List<BanQinWmInvLotAtt> list = this.findList(banQinWmInvLotAtt);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 通过LotNum获取批次库存记录
     * @param lotNum
     * @return
     */
    public BanQinWmInvLotAtt getByLotNum(String lotNum, String orgId) {
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setLotNum(lotNum);
        wmInvLotAttModel.setOrgId(orgId);
        return this.findFirst(wmInvLotAttModel);
    }

    /**
     * 根据货主和商品获取批次属性
     * @param ownerCode
     * @param skuCode
     * @return
     */
    public BanQinWmInvLotAtt getByOwnerAndSku(String ownerCode, String skuCode, String orgId) {
        BanQinWmInvLotAtt wmInvLotAttModel = new BanQinWmInvLotAtt();
        wmInvLotAttModel.setOwnerCode(ownerCode);
        wmInvLotAttModel.setSkuCode(skuCode);
        wmInvLotAttModel.setOrgId(orgId);
        return this.findFirst(wmInvLotAttModel);
    }

    /**
     * 根据批次属性查询批号
     * @param model
     * @return
     * @Modify 由于批次属性为空时，sql不拼接造成查询到多条 2019-05-09
     */
    public List<BanQinWmInvLotAtt> getInvLotNum(BanQinWmInvLotAtt model) {
//        return this.findList(model);
        return mapper.findByAllLot(model);
    }
    
    /**
     * 字符串空转换
     *
     * @param value
     * @param nvlvalue
     * @return
     */
    protected String nvl(String value, String nvlvalue) {
        return StringUtils.isNotEmpty(value) ? value : nvlvalue;
    }

    /**
     * 验证批次号是否存在
     * @param InvLotNum
     * @return
     */
    public ResultMessage checkByInvLotNum(String InvLotNum, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过InvLotNum判断批次属性是否存在
        BanQinWmInvLotAtt newModel = new BanQinWmInvLotAtt();
        // 设置查询对象的值
        newModel.setLotNum(InvLotNum);
        newModel.setOrgId(orgId);
        BanQinWmInvLotAtt wmInvLotAttModel = this.findFirst(newModel);
        if (null != wmInvLotAttModel) {
            msg.setSuccess(true);
            return msg;
        }
        msg.setSuccess(false);
        return msg;
    }
	
}