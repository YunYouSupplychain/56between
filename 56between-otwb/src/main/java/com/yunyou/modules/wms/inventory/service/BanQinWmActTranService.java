package com.yunyou.modules.wms.inventory.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.GlobalDataRule;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTran;
import com.yunyou.modules.wms.inventory.mapper.BanQinWmActTranMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存交易Service
 * @author WMJ
 * @version 2019-01-26
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmActTranService extends CrudService<BanQinWmActTranMapper, BanQinWmActTran> {

	public BanQinWmActTran get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmActTran> findList(BanQinWmActTran banQinWmActTran) {
		return mapper.findList(banQinWmActTran);
	}
	
	public Page<BanQinWmActTran> findPage(Page<BanQinWmActTran> page, BanQinWmActTran banQinWmActTran) {
        banQinWmActTran.setDataScope(GlobalDataRule.getDataRuleSql("a.org_id", banQinWmActTran.getOrgId()));
        dataRuleFilter(banQinWmActTran);
        banQinWmActTran.setPage(page);
        page.setList(mapper.findPage(banQinWmActTran));
		return page;
	}
	
	@Transactional
	public void save(BanQinWmActTran banQinWmActTran) {
		super.save(banQinWmActTran);
	}
	
	@Transactional
	public void delete(BanQinWmActTran banQinWmActTran) {
		super.delete(banQinWmActTran);
	}
	
	public BanQinWmActTran findFirst(BanQinWmActTran banQinWmActTran) {
        List<BanQinWmActTran> list = this.findList(banQinWmActTran);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据tranId查询库存交易记录
     * @param tranId
     * @return
     */
    public BanQinWmActTran getByTranId(String tranId, String orgId) {
        BanQinWmActTran WmActTranModel = new BanQinWmActTran();
        WmActTranModel.setTranId(tranId);
        WmActTranModel.setOrgId(orgId);
        return this.findFirst(WmActTranModel);
    }

    /**
     * 根据单号和单据类型获取交易日志
     * @param orderNo
     * @param docType
     * @return
     */
    public List<BanQinWmActTran> getTransByOrderNo(String orderNo, String docType, String orgId) {
        BanQinWmActTran actTransModel = new BanQinWmActTran();
        actTransModel.setOrderNo(orderNo);
        actTransModel.setOrderType(docType);
        actTransModel.setOrgId(orgId);
        return this.findList(actTransModel);
    }

    /**
     * 查询库存交易记录
     * @param docNo
     * @param orderType
     * @param tranType
     * @return
     */
    public List<BanQinWmActTran> getWmActTranList(String docNo, String orderType, String tranType, String orgId) {
        BanQinWmActTran actTransModel = new BanQinWmActTran();
        actTransModel.setOrderNo(docNo);
        actTransModel.setTranType(tranType);
        actTransModel.setOrderType(orderType);
        actTransModel.setOrgId(orgId);
        return this.findList(actTransModel);
    }

    /**
     * 删除方法查找WmActTranModel对象 根据库位编码作为ToLoc字段查找条件
     * @param locCode
     * @return
     */
    public ResultMessage getByLocCode(String locCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinWmActTran newModel = new BanQinWmActTran();
        // 设置查询对象的值
        newModel.setToLoc(locCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinWmActTran actTranModel = this.findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != actTranModel) {
            msg.setSuccess(false);
            msg.setData(actTranModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    public List<BanQinWmActTran> checkInitInvQuery(String initInventory, String orgId) {
        return mapper.checkInitInvQuery(initInventory, orgId);
    }

    public List<BanQinWmActTran> getBeginInvByLotNum(String lotNum, String orgId) {
        BanQinWmActTran tran = new BanQinWmActTran();
        tran.setTranType("RCV");
        tran.setOrderType("ASN");
        tran.setOrderNo("BEGIN");
        tran.setToLot(lotNum);
        tran.setOrgId(orgId);
        return this.findList(tran);
    }

}