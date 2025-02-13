package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelPrealloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelPreallocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPrealloc;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmDelPreallocMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 取消预配记录Service
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmDelPreallocService extends CrudService<BanQinWmDelPreallocMapper, BanQinWmDelPrealloc> {

	public BanQinWmDelPrealloc get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmDelPrealloc> findList(BanQinWmDelPrealloc banQinWmDelPrealloc) {
		return super.findList(banQinWmDelPrealloc);
	}
	
	public Page<BanQinWmDelPrealloc> findPage(Page<BanQinWmDelPrealloc> page, BanQinWmDelPrealloc banQinWmDelPrealloc) {
		return super.findPage(page, banQinWmDelPrealloc);
	}
	
	@Transactional
	public void save(BanQinWmDelPrealloc banQinWmDelPrealloc) {
		super.save(banQinWmDelPrealloc);
	}
	
	@Transactional
	public void delete(BanQinWmDelPrealloc banQinWmDelPrealloc) {
		super.delete(banQinWmDelPrealloc);
	}

    /**
     * 写保存删除预配明细
     * @param wmSoPreallocModel
     */
    @Transactional
    public void saveDelPrealloc(BanQinWmSoPrealloc wmSoPreallocModel) {
        BanQinWmDelPrealloc model = new BanQinWmDelPrealloc();
        BeanUtils.copyProperties(wmSoPreallocModel, model);
        model.setId(null);
        // 添加流水号
        String preallocSeq = "";
        model.setPreallocSeq(preallocSeq);// 行号
        // 操作人、操作时间
        model.setOp(UserUtils.getUser().getName());// 登录用户
        model.setOpTime(new Date());
        this.save(model);
    }

    /**
     * 根据发运单号获取取消预配记录
     * @param soNo
     * @return
     */
    public List<BanQinWmDelPreallocEntity> getEntityBySoNo(String soNo, String orgId) {
        List<BanQinWmDelPreallocEntity> result = Lists.newArrayList();
        BanQinWmDelPrealloc condition = new BanQinWmDelPrealloc();
        condition.setSoNo(soNo);
        condition.setOrgId(orgId);
        List<BanQinWmDelPrealloc> list = this.findList(condition);
        for (BanQinWmDelPrealloc model : list) {
            BanQinWmDelPreallocEntity entity = new BanQinWmDelPreallocEntity();
            BeanUtils.copyProperties(model, entity);
            result.add(entity);
        }
        return result;
    }

    /**
     * 根据波次号获取取消预配记录
     * @param waveNo
     * @return
     */
    public List<BanQinWmDelPreallocEntity> getEntityByWaveNo(String waveNo, String orgId) {
        List<BanQinWmDelPreallocEntity> result = Lists.newArrayList();
        BanQinWmDelPrealloc condition = new BanQinWmDelPrealloc();
        condition.setWaveNo(waveNo);
        condition.setOrgId(orgId);
        List<BanQinWmDelPrealloc> list = this.findList(condition);
        for (BanQinWmDelPrealloc model : list) {
            BanQinWmDelPreallocEntity entity = new BanQinWmDelPreallocEntity();
            BeanUtils.copyProperties(model, entity);
            result.add(entity);
        }
        return result;
    }
	
}