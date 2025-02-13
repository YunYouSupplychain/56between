package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.BanQinWmTaskKit;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmDelAllocEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAlloc;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmDelAllocMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * 取消分配拣货记录Service
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmDelAllocService extends CrudService<BanQinWmDelAllocMapper, BanQinWmDelAlloc> {
    @Autowired
    @Lazy
    private BanQinOutboundBatchCreateTaskPaAction outboundBatchCreateTaskPaAction;

	public BanQinWmDelAlloc get(String id) {
		return super.get(id);
	}
	
	public List<BanQinWmDelAlloc> findList(BanQinWmDelAlloc banQinWmDelAlloc) {
		return mapper.findList(banQinWmDelAlloc);
	}
	
	public Page<BanQinWmDelAlloc> findPage(Page<BanQinWmDelAlloc> page, BanQinWmDelAlloc banQinWmDelAlloc) {
		return super.findPage(page, banQinWmDelAlloc);
	}
	
	@Transactional
	public void save(BanQinWmDelAlloc banQinWmDelAlloc) {
		super.save(banQinWmDelAlloc);
	}
	
	@Transactional
	public void delete(BanQinWmDelAlloc banQinWmDelAlloc) {
		super.delete(banQinWmDelAlloc);
	}

    /**
     * 保存删除分配明细
     * @param wmSoAllocModel
     */
    @Transactional
    public void saveDelAlloc(BanQinWmSoAlloc wmSoAllocModel) {
        BanQinWmDelAlloc model = new BanQinWmDelAlloc();
        BeanUtils.copyProperties(wmSoAllocModel, model);
        model.setId(IdGen.uuid());
        model.setIsNewRecord(true);
        // 添加流水号
        String allocSeq = this.getMaxLineNo(wmSoAllocModel);
        model.setAllocSeq(allocSeq);// 行号
        model.setAllocId(wmSoAllocModel.getAllocId());// 分配ID
        model.setOrderNo(wmSoAllocModel.getSoNo());// 写订单号
        model.setLineNo(wmSoAllocModel.getLineNo());// 写明细行号
        model.setOrderType(WmsCodeMaster.ORDER_SO.getCode());// 订单类型
        // 操作人、操作时间
        model.setOp(UserUtils.getUser().getName());// 登录用户
        model.setOpTime(new Date());
        this.save(model);
    }

    /**
     * 根据订单号获取取消分配记录
     * @param orderNo
     * @return
     */
    public List<BanQinWmDelAllocEntity> getEntityByOrderNo(String orderNo, String orgId) {
        List<BanQinWmDelAllocEntity> result = Lists.newArrayList();
        BanQinWmDelAlloc condition = new BanQinWmDelAlloc();
        condition.setOrderNo(orderNo);
        condition.setOrgId(orgId);
        List<BanQinWmDelAlloc> list = this.findList(condition);
        for (BanQinWmDelAlloc alloc : list) {
            BanQinWmDelAllocEntity entity = new BanQinWmDelAllocEntity();
            BeanUtils.copyProperties(alloc, entity);
            result.add(entity);
        }
        return result;
    }

    /**
     * 根据波次号获取取消分配记录
     * @param waveNo
     * @return
     */
    public List<BanQinWmDelAllocEntity> getEntityByWaveNo(String waveNo, String orgId) {
        List<BanQinWmDelAllocEntity> result = Lists.newArrayList();
        BanQinWmDelAlloc condition = new BanQinWmDelAlloc();
        condition.setWaveNo(waveNo);
        condition.setOrgId(orgId);
        List<BanQinWmDelAlloc> list = this.findList(condition);
        for (BanQinWmDelAlloc alloc : list) {
            BanQinWmDelAllocEntity entity = new BanQinWmDelAllocEntity();
            BeanUtils.copyProperties(alloc, entity);
            result.add(entity);
        }
        return result;
    }

    /**
     * 加工写保存删除分配明细
     * @param wmTaskKitModel
     */
    @Transactional
    public void saveDelAllocByTaskKit(BanQinWmTaskKit wmTaskKitModel) {
        BanQinWmDelAlloc model = new BanQinWmDelAlloc();
        BeanUtils.copyProperties(wmTaskKitModel, model);
        model.setId(null);
        // 添加流水号
        String allocSeq = "";/*wmCommon.getLineNo(model, new String[] { "allocId", "projectId" }, "allocSeq");*/
        model.setAllocSeq(allocSeq);// 行号
        model.setAllocId(wmTaskKitModel.getKitTaskId());// 加工任务ID
        model.setOrderNo(wmTaskKitModel.getKitNo());// 写订单号
        model.setLineNo(wmTaskKitModel.getSubLineNo());// 写子件行号
        model.setOrderType(WmsCodeMaster.ORDER_KIT.getCode());// 订单类型
        model.setSkuCode(wmTaskKitModel.getSubSkuCode());// 商品编码
        // 操作人、操作时间
        model.setOp(UserUtils.getUser().getName());// 登录用户
        model.setOpTime(new Date());
        this.save(model);
    }

    @Transactional
    public ResultMessage cancelPickingByAlloc(List<BanQinWmDelAllocEntity> list) {
        List<BanQinWmSoAllocEntity> soAllocList = Lists.newArrayList();
        for (BanQinWmDelAllocEntity wmDelAllocEntity : list) {
            BanQinWmSoAllocEntity wmSoAllocEntity = new BanQinWmSoAllocEntity();
            BeanUtils.copyProperties(wmDelAllocEntity, wmSoAllocEntity);
            soAllocList.add(wmSoAllocEntity);
        }
        return outboundBatchCreateTaskPaAction.outboundBatchCreateTaskPa(soAllocList);
    }

    private String getMaxLineNo(BanQinWmSoAlloc wmSoAlloc) {
        Long id;
        BanQinWmDelAlloc condition = new BanQinWmDelAlloc();
        condition.setAllocId(wmSoAlloc.getAllocId());
        condition.setOrgId(wmSoAlloc.getOrgId());
        List<BanQinWmDelAlloc> list = this.findList(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            list.sort(comparing(BanQinWmDelAlloc::getAllocSeq).reversed());
            id = Long.parseLong(list.get(0).getAllocSeq()) + 1;
        } else {
            id = 1L;
        }

        return String.format("%04d", id);
    }
    
}