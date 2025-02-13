package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRulePreallocDetail;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRulePreallocDetailMapper;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预配规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRulePreallocDetailService extends CrudService<BanQinCdRulePreallocDetailMapper, BanQinCdRulePreallocDetail> {

	public BanQinCdRulePreallocDetail get(String id) {
		return super.get(id);
	}
	
	public List<BanQinCdRulePreallocDetail> findList(BanQinCdRulePreallocDetail banQinCdRulePreallocDetail) {
		return super.findList(banQinCdRulePreallocDetail);
	}
	
	public Page<BanQinCdRulePreallocDetail> findPage(Page<BanQinCdRulePreallocDetail> page, BanQinCdRulePreallocDetail banQinCdRulePreallocDetail) {
		return super.findPage(page, banQinCdRulePreallocDetail);
	}
	
	@Transactional
	public void save(BanQinCdRulePreallocDetail banQinCdRulePreallocDetail) {
		super.save(banQinCdRulePreallocDetail);
	}
	
	@Transactional
	public void delete(BanQinCdRulePreallocDetail banQinCdRulePreallocDetail) {
		super.delete(banQinCdRulePreallocDetail);
	}

    /**
     * 根据预配规则代码获取预配规则明细
     * @param ruleCode
     * @return
     * @throws WarehouseException
     */
    public List<BanQinCdRulePreallocDetail> getByRuleCode(String ruleCode, String orgId) throws WarehouseException {
        BanQinCdRulePreallocDetail model = new BanQinCdRulePreallocDetail();
        model.setRuleCode(ruleCode);
        model.setIsEnable(WmsConstants.YES);// 是否启用
        model.setOrgId(orgId);
        List<BanQinCdRulePreallocDetail> cdRulePreallocDetailList = this.findList(model);

        if (cdRulePreallocDetailList.size() == 0) {
            throw new WarehouseException("缺少参数", ruleCode);
        }
        // 校验预配规则是否配置EA单位
        boolean flag = false;
        for (BanQinCdRulePreallocDetail preallocDetailModel : cdRulePreallocDetailList) {
            if (preallocDetailModel.getUom().equals(WmsConstants.UOM_EA)) {
                flag = true;
            }
        }
        if (!flag) {
            throw new WarehouseException("缺少参数", ruleCode);
        }

        return cdRulePreallocDetailList;
    }

    /**
     * 获取CdRulePreallocDetailModel并对其进行排序
     * @param ruleCode
     * @return
     */
    public List<BanQinCdRulePreallocDetail> findByExample(String ruleCode, String orgId) {
        // 查询出从表内容
        BanQinCdRulePreallocDetail example = new BanQinCdRulePreallocDetail();
        example.setRuleCode(ruleCode);
        example.setOrgId(orgId);
        return this.findList(example);
    }
    
}