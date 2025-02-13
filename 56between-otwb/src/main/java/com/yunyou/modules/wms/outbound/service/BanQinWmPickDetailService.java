package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickDetail;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmPickDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 拣货单明细Service
 *
 * @author ZYF
 * @version 2020-05-13
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmPickDetailService extends CrudService<BanQinWmPickDetailMapper, BanQinWmPickDetail> {

    public BanQinWmPickDetail get(String id) {
        return super.get(id);
    }

    public List<BanQinWmPickDetail> findList(BanQinWmPickDetail banQinWmPickDetail) {
        return super.findList(banQinWmPickDetail);
    }

    public Page<BanQinWmPickDetail> findPage(Page<BanQinWmPickDetail> page, BanQinWmPickDetail banQinWmPickDetail) {
        return super.findPage(page, banQinWmPickDetail);
    }

    @Transactional
    public void save(BanQinWmPickDetail banQinWmPickDetail) {
        super.save(banQinWmPickDetail);
    }

    @Transactional
    public void delete(BanQinWmPickDetail banQinWmPickDetail) {
        super.delete(banQinWmPickDetail);
    }

    public BanQinWmPickDetail findFirst(BanQinWmPickDetail example) {
        List<BanQinWmPickDetail> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述：查询拣货明细
     */
    public List<BanQinWmPickDetail> findByPickNo(String pickNo, String orgId) {
        BanQinWmPickDetail model = new BanQinWmPickDetail();
        model.setPickNo(pickNo);
        model.setOrgId(orgId);
        return mapper.findList(model);
    }

    /**
     * 描述：查询拣货明细
     */
    public BanQinWmPickDetail findBySoNo(String soNo, String allocId, String orgId) {
        BanQinWmPickDetail model = new BanQinWmPickDetail();
        model.setSoNo(soNo);
        model.setAllocId(allocId);
        model.setOrgId(orgId);
        return this.findFirst(model);
    }

    /**
     * 描述： 删除拣货单明细
     */
    @Transactional
    public void removeByPickNo(String pickNo, String orgId) throws WarehouseException {
        List<BanQinWmPickDetail> list = this.findByPickNo(pickNo, orgId);
        for (BanQinWmPickDetail model : list) {
            this.delete(model);
        }
    }
}