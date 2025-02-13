package com.yunyou.modules.wms.outbound.service;

import com.google.common.collect.Lists;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerial;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import com.yunyou.modules.wms.outbound.mapper.BanQinWmSoSerialMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出库序列号Service
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmSoSerialService extends CrudService<BanQinWmSoSerialMapper, BanQinWmSoSerial> {

    public BanQinWmSoSerial get(String id) {
        return super.get(id);
    }

    public List<BanQinWmSoSerial> findList(BanQinWmSoSerial banQinWmSoSerial) {
        return super.findList(banQinWmSoSerial);
    }

    public Page<BanQinWmSoSerialEntity> findPage(Page page, BanQinWmSoSerialEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    @Transactional
    public void save(BanQinWmSoSerial banQinWmSoSerial) {
        super.save(banQinWmSoSerial);
    }

    @Transactional
    public void delete(BanQinWmSoSerial banQinWmSoSerial) {
        super.delete(banQinWmSoSerial);
    }

    public BanQinWmSoSerial findFirst(BanQinWmSoSerial example) {
        List<BanQinWmSoSerial> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 描述： 查询出库序列号
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoSerial> findBySoNo(String soNo, String orgId) {
        BanQinWmSoSerial example = new BanQinWmSoSerial();
        example.setSoNo(soNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询出库序列号
     *
     * @param ownerCode 货主
     * @param skuCode   商品
     * @param serialNo  序列号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoSerial> findByOwnerCodeAndSkuCodeAndSerialNo(String ownerCode, String skuCode, String serialNo, String orgId) {
        BanQinWmSoSerial example = new BanQinWmSoSerial();
        example.setOwnerCode(ownerCode);
        example.setSkuCode(skuCode);
        example.setSerialNo(serialNo);
        example.setOrgId(orgId);
        return mapper.findList(example);
    }

    /**
     * 描述： 查询出库序列号
     *
     * @param soNo      出库单号
     * @param skuCode   商品
     * @param serialNo  序列号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public BanQinWmSoSerial findBySoNoAndSkuCodeAndSerialNo(String soNo, String skuCode, String serialNo, String orgId) {
        BanQinWmSoSerial example = new BanQinWmSoSerial();
        example.setSoNo(soNo);
        example.setSkuCode(skuCode);
        example.setSerialNo(serialNo);
        example.setOrgId(orgId);
        return this.findFirst(example);
    }

    /**
     * 描述： 查询出库序列号Entity
     *
     * @param soNo  出库单号
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoSerialEntity> findEntityBySoNo(String soNo, String orgId) {
        List<BanQinWmSoSerialEntity> entities = Lists.newArrayList();

        BanQinWmSoSerial example = new BanQinWmSoSerial();
        example.setSoNo(soNo);
        example.setOrgId(orgId);
        List<BanQinWmSoSerial> list = mapper.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            for (BanQinWmSoSerial wmSoSerial : list) {
                BanQinWmSoSerialEntity entity = (BanQinWmSoSerialEntity) wmSoSerial;
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * 描述： 查询出库序列号Entity
     *
     * @param allocId 分配ID
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    public List<BanQinWmSoSerialEntity> findEntityByAllocId(String allocId, String orgId) {
        List<BanQinWmSoSerialEntity> entities = Lists.newArrayList();

        BanQinWmSoSerial example = new BanQinWmSoSerial();
        example.setAllocId(allocId);
        example.setOrgId(orgId);
        List<BanQinWmSoSerial> list = this.findList(example);
        if (CollectionUtil.isNotEmpty(list)) {
            for (BanQinWmSoSerial wmSoSerial : list) {
                BanQinWmSoSerialEntity entity = new BanQinWmSoSerialEntity();
                BeanUtils.copyProperties(wmSoSerial, entity);
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * 描述： 删除出库序列号
     *
     * @param allocId 分配ID
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeByAllocId(String allocId, String orgId) {
        mapper.removeByAllocId(allocId, orgId);
    }

    /**
     * 描述： 删除未打包过的出库序列号
     *
     * @param allocId 分配ID
     * @param orgId
     * @author Jianhua on 2019/2/15
     */
    @Transactional
    public void removeByAllocIdAndNotPack(String allocId, String orgId) {
        mapper.removeByAllocIdAndNotPack(allocId, orgId);
    }
    
    public List<BanQinWmSoSerialEntity> findByAllocIds(List<String> allocIds, String orgId) {
        return mapper.findByAllocIds(allocIds, orgId);
    }
}