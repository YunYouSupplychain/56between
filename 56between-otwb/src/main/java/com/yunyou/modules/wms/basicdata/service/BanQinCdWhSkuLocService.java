package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLoc;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLocEntity;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdWhSkuLocMapper;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品拣货位Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdWhSkuLocService extends CrudService<BanQinCdWhSkuLocMapper, BanQinCdWhSkuLoc> {

	public BanQinCdWhSkuLoc get(String id) {
		return super.get(id);
	}
	
	public List<BanQinCdWhSkuLoc> findList(BanQinCdWhSkuLoc banQinCdWhSkuLoc) {
		return super.findList(banQinCdWhSkuLoc);
	}
	
	public Page<BanQinCdWhSkuLocEntity> findPage(Page page, BanQinCdWhSkuLocEntity banQinCdWhSkuLocEntity) {
        dataRuleFilter(banQinCdWhSkuLocEntity);
        banQinCdWhSkuLocEntity.setPage(page);
        page.setList(mapper.findPage(banQinCdWhSkuLocEntity));
		return page;
	}
	
	@Transactional
	public void save(BanQinCdWhSkuLoc banQinCdWhSkuLoc) {
		super.save(banQinCdWhSkuLoc);
	}

    @Transactional
    public void saveEntity(BanQinCdWhSkuLocEntity banQinCdWhSkuLocEntity) {
        if (null == banQinCdWhSkuLocEntity.getMaxLimit()) {
            banQinCdWhSkuLocEntity.setMaxLimit(0d);
        }
        if (null == banQinCdWhSkuLocEntity.getMinLimit()) {
            banQinCdWhSkuLocEntity.setMinLimit(0d);
        }
        if (null == banQinCdWhSkuLocEntity.getMinRp()) {
            banQinCdWhSkuLocEntity.setMinRp(0d);
        }
        if (StringUtils.isEmpty(banQinCdWhSkuLocEntity.getRpUom())) {
            banQinCdWhSkuLocEntity.setRpUom("EA");
        }
        if (WmsConstants.NO.equals(banQinCdWhSkuLocEntity.getIsOverAlloc()) && WmsConstants.NO.equals(banQinCdWhSkuLocEntity.getIsRpAlloc())
            && WmsConstants.NO.equals(banQinCdWhSkuLocEntity.getIsOverRp()) && WmsConstants.NO.equals(banQinCdWhSkuLocEntity.getIsFmRs())
            && WmsConstants.NO.equals(banQinCdWhSkuLocEntity.getIsFmCs())) {
            banQinCdWhSkuLocEntity.setIsFmRs(WmsConstants.YES);
        }
        this.save(banQinCdWhSkuLocEntity);
    }
	
	@Transactional
	public void delete(BanQinCdWhSkuLoc banQinCdWhSkuLoc) {
		super.delete(banQinCdWhSkuLoc);
	}
	
	public BanQinCdWhSkuLoc findFirst(BanQinCdWhSkuLoc banQinCdWhSkuLoc) {
        List<BanQinCdWhSkuLoc> list = this.findList(banQinCdWhSkuLoc);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }
    
    /**
     * 根据货主、商品获取商品拣货位(cd_wh_sku (货主,商品)组确认唯一记录)
     * @param ownerCode
     * @param skuCode
     * @return
     */
    public List<BanQinCdWhSkuLoc> getCdWhSkuLoc(String ownerCode, String skuCode, String orgId) {
        BanQinCdWhSkuLoc model = new BanQinCdWhSkuLoc();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setOrgId(orgId);

        return this.findList(model);
    }

    /**
     * 根据货主、商品、商品拣货位类型 获取商品拣货位(商品拣货位配置)
     * @param ownerCode
     * @param skuCode
     * @param skuLocType
     * @return
     */
    public BanQinCdWhSkuLoc getCdWhSkuLocType(String ownerCode, String skuCode, String skuLocType, String orgId) {
        BanQinCdWhSkuLoc model = new BanQinCdWhSkuLoc();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setSkuLocType(skuLocType);
        model.setOrgId(orgId);

        return this.findFirst(model);
    }

    /**
     * 根据货主、商品、商品拣货位 获取商品拣货位
     * @param ownerCode
     * @param skuCode
     * @param locCode
     * @return
     */
    public BanQinCdWhSkuLoc getByLocCode(String ownerCode, String skuCode, String locCode, String orgId) {
        BanQinCdWhSkuLoc model = new BanQinCdWhSkuLoc();
        model.setOwnerCode(ownerCode);
        model.setSkuCode(skuCode);
        model.setLocCode(locCode);
        model.setOrgId(orgId);

        return this.findFirst(model);
    }

    /**
     * 供删除方法查找CdWhSkuLocModel对象，根据库位编码作为LocCode字段查找条件
     * @param locCode
     * @return
     */
    public ResultMessage getByLocCode(String locCode, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 通过locCode判断库位是否被调用
        BanQinCdWhSkuLoc newModel = new BanQinCdWhSkuLoc();
        // 设置查询对象的值
        newModel.setLocCode(locCode);
        newModel.setOrgId(orgId);
        // 查询出调用此库位的对象
        BanQinCdWhSkuLoc skuLocModel = this.findFirst(newModel);
        // 若此调用对象数量不为空则说明已经被调用
        if (null != skuLocModel) {
            msg.setSuccess(false);
            msg.setData(skuLocModel);
            return msg;
        }
        msg.setSuccess(true);
        return msg;
    }

    public List<BanQinCdWhSkuLoc> findAllDetail(String orgId) {
        BanQinCdWhSkuLoc condition = new BanQinCdWhSkuLoc();
        condition.setOrgId(orgId);
        return this.findList(condition);
    }

    public List<BanQinCdWhSkuLocEntity> getRpSkuQuery(BanQinCdWhSkuLocEntity entity) {
        return mapper.getRpSkuQuery(entity);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<BanQinCdWhSkuLoc> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }
}