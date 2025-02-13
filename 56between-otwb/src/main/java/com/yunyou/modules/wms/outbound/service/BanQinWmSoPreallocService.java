package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPreallocEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoPreallocMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预配明细Service
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoPreallocService extends CrudService<BanQinWmSoPreallocMapper, BanQinWmSoPrealloc> {

	public BanQinWmSoPrealloc get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmSoPrealloc> findList(BanQinWmSoPrealloc banQinWmSoPrealloc) {
		return super.findList(banQinWmSoPrealloc);
	}
	
	public Page<BanQinWmSoPrealloc> findPage(Page<BanQinWmSoPrealloc> page, BanQinWmSoPrealloc banQinWmSoPrealloc) {
		return super.findPage(page, banQinWmSoPrealloc);
	}
	
	@Transactional
	public void save(BanQinWmSoPrealloc banQinWmSoPrealloc) {
		super.save(banQinWmSoPrealloc);
	}
	
	@Transactional
	public void delete(BanQinWmSoPrealloc banQinWmSoPrealloc) {
		super.delete(banQinWmSoPrealloc);
	}
    
    public BanQinWmSoPrealloc findFirst(BanQinWmSoPrealloc banQinWmSoPrealloc) {
        List<BanQinWmSoPrealloc> list = this.findList(banQinWmSoPrealloc);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
	
    /**
     * 根据预配编号，获取预配明细
     * @param preallocId
     * @return
     * @throws WarehouseException
     */
    public BanQinWmSoPrealloc getByPreallocId(String preallocId, String orgId) throws WarehouseException {
        BanQinWmSoPrealloc model = new BanQinWmSoPrealloc();
        model.setPreallocId(preallocId);
        model.setOrgId(orgId);
        model = this.findFirst(model);
        if (model == null) {
            // 查询不到预配明细{0}
            throw new WarehouseException("查询不到预配明细", preallocId);
        }
        return model;
    }

    /**
     * 根据波次单号获取预配记录
     * @param waveNo
     * @return
     */
    public List<BanQinWmSoPreallocEntity> getEntityByWaveNo(String waveNo, String orgId) {
        BanQinWmSoPreallocEntity condition = new BanQinWmSoPreallocEntity();
        condition.setWaveNo(waveNo);
        condition.setOrgId(orgId);
        
        return mapper.findEntity(condition);
    }

    /**
     * 根据发运单号获取预配记录
     * @param soNo
     * @return
     */
    public List<BanQinWmSoPreallocEntity> getEntityBySoNo(String soNo, String orgId) {
        BanQinWmSoPreallocEntity condition = new BanQinWmSoPreallocEntity();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        
        return mapper.findEntity(condition);
    }

    /**
     * 根据预配ID获取预配记录
     * @param preallocId
     * @return
     */
    public BanQinWmSoPreallocEntity getEntityByPreallocId(String preallocId, String orgId) {
        BanQinWmSoPreallocEntity condition = new BanQinWmSoPreallocEntity();
        condition.setPreallocId(preallocId);
        condition.setOrgId(orgId);

        List<BanQinWmSoPreallocEntity> entity = mapper.findEntity(condition);
        return CollectionUtil.isNotEmpty(entity) ? entity.get(0) : null;
    }

    /**
     * 获取预配明细 --按波次号、出库单号、出库单号+行号、预配ID 过滤掉取消、关闭状态，订单拦截状态、冻结状态
     * @param processByCode
     * @param noList
     * @return
     */
    public List<BanQinWmSoPreallocEntity> getEntityByProcessByCode(String processByCode, List<String> noList, String orgId) {
        BanQinWmSoPreallocEntity cond = new BanQinWmSoPreallocEntity();
        cond.setOrgId(orgId);
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            cond.setWaveNos(noList);
        } else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            cond.setSoNos(noList);
        } else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            // 按订单行
            cond.setLineNos(StringUtils.join(noList.toArray(), ","));
        } else if (processByCode.equals(ProcessByCode.BY_PREALLOC.getCode())) {
            cond.setPreallocIds(noList);
        }
        return mapper.getWmSoPreallocByNo(cond);
    }

    /**
     * 拦截订单 获取预配明细 --按波次号、出库单号、出库单号+行号、预配ID 过滤掉取消、关闭状态
     * @param processByCode
     * @param noList
     * @return
     */
    public List<BanQinWmSoPreallocEntity> getEntityByIntercept(String processByCode, List<String> noList, String orgId) {
        BanQinWmSoPreallocEntity condition = new BanQinWmSoPreallocEntity();
        condition.setOrgId(orgId);
        if (processByCode.equals(ProcessByCode.BY_WAVE.getCode())) {
            condition.setWaveNos(noList);
        } else if (processByCode.equals(ProcessByCode.BY_SO.getCode())) {
            condition.setSoNos(noList);
        } else if (processByCode.equals(ProcessByCode.BY_SO_LINE.getCode())) {
            // 按订单行
            condition.setLineNos(StringUtils.join(noList.toArray(), ","));
        } else if (processByCode.equals(ProcessByCode.BY_PREALLOC.getCode())) {
            condition.setPreallocIds(noList);
        }
        return mapper.getWmInterceptSoPrealloc(condition);
    }
	
}