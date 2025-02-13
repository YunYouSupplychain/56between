package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvDetail;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmWvDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次单明细Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmWvDetailService extends CrudService<BanQinWmWvDetailMapper, BanQinWmWvDetail> {

    public BanQinWmWvDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmWvDetail> findList(BanQinWmWvDetail banQinWmWvDetail) {
        return super.findList(banQinWmWvDetail);
    }

    public Page<BanQinWmWvDetail> findPage(Page<BanQinWmWvDetail> page, BanQinWmWvDetail banQinWmWvDetail) {
        return super.findPage(page, banQinWmWvDetail);
    }

    @Transactional
    public void save(BanQinWmWvDetail banQinWmWvDetail) {
        super.save(banQinWmWvDetail);
    }

    @Transactional
    public void delete(BanQinWmWvDetail banQinWmWvDetail) {
        super.delete(banQinWmWvDetail);
    }

    public BanQinWmWvDetail findFirst(BanQinWmWvDetail example) {
        List<BanQinWmWvDetail> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：查询波次明细
     *
     * @param waveNo 波次单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmWvDetail> findByWaveNo(String waveNo, String orgId) {
        BanQinWmWvDetail model = new BanQinWmWvDetail();
        model.setWaveNo(waveNo);
        model.setOrgId(orgId);
        return mapper.findList(model);
    }

    /**
     * 描述：查询波次明细
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmWvDetail findBySoNo(String soNo, String orgId) {
        BanQinWmWvDetail model = new BanQinWmWvDetail();
        model.setSoNo(soNo);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    /**
     * 描述： 删除波次单明细
     *
     * @param waveNo 波次单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeByWaveNo(String waveNo, String orgId) throws WarehouseException {
        List<BanQinWmWvDetail> list = this.findByWaveNo(waveNo, orgId);
        for (BanQinWmWvDetail model : list) {
            if ((!WmsCodeMaster.WAVE_NEW.getCode().equals(model.getStatus())) && (!WmsCodeMaster.WAVE_CANCEL.getCode().equals(model.getStatus()))) {
                // {0}{1}不是创建或者取消状态，不能操作
                throw new WarehouseException("[" + model.getWaveNo() + "][" + model.getSoNo() + "]非创建、非取消状态，不能操作");
            }
            this.delete(model);
        }
    }

    /**
     * 描述： 删除波次单明细
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeBySoNo(String soNo, String orgId) throws WarehouseException {
        BanQinWmWvDetail model = this.findBySoNo(soNo, orgId);
        if (!WmsCodeMaster.WAVE_NEW.getCode().equals(model.getStatus())) {
            // 订单{0}不是新增状态，不能删除
            throw new WarehouseException("[" + model.getWaveNo() + "][" + model.getSoNo() + "]非创建、非取消状态，不能操作");
        }
        this.delete(model);
    }

    /**
     * 描述： 波次单明细状态更新
     *
     * @param soNo   出库单号
     * @param status 状态
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void updateStatus(String soNo, String status, String orgId) {
        BanQinWmWvDetail model = this.findBySoNo(soNo, orgId);
        if (model != null) {
            model.setStatus(status);
            this.save(model);
        }
    }
    
    @Transactional
    public void updateWvDetailStatusByWave(List<String> waveNos, String userId, String orgId) {
        mapper.updateWvDetailStatusByWave(waveNos, userId, orgId);
    }

    @Transactional
    public void updateWvDetailStatusBySo(List<String> soNos, String userId, String orgId) {
        mapper.updateWvDetailStatusBySo(soNos, userId, orgId);
    }
    
    public List<String> checkSoCreateWaveOrder(List<String> soNos, String orgId) {
        return mapper.checkSoCreateWaveOrder(soNos, orgId);
    }
}