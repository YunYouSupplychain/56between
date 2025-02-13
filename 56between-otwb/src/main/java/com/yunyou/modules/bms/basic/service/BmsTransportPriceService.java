package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsTransportGroup;
import com.yunyou.modules.bms.basic.entity.BmsTransportPrice;
import com.yunyou.modules.bms.basic.entity.BmsTransportSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportPriceEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsTransportPriceTemplate;
import com.yunyou.modules.bms.basic.mapper.BmsTransportPriceMapper;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.common.entity.SysCommonRegion;
import com.yunyou.modules.sys.common.service.SysCommonRegionService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：运输价格Service
 *
 * @author liujianhua created on 2019-11-14
 */
@Service
@Transactional(readOnly = true)
public class BmsTransportPriceService extends CrudService<BmsTransportPriceMapper, BmsTransportPrice> {
    @Autowired
    private BmsTransportSteppedPriceService bmsTransportSteppedPriceService;
    @Autowired
    private SysCommonRegionService sysCommonRegionService;
    @Autowired
    private AreaService areaService;

    /**
     * 根据ID查询运输价格（扩展实体）
     */
    public BmsTransportPriceEntity getEntity(String id) {
        BmsTransportPriceEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setSteppedPrices(bmsTransportSteppedPriceService.findByFkId(id));
        }
        return entity;
    }

    /**
     * 保存前校验（扩展实体）
     */
    public void checkSaveBefore(BmsTransportPriceEntity entity) {
        // 启用阶梯价格
        if (BmsConstants.YES.equals(entity.getIsUseStep())) {
            if (CollectionUtil.isNotEmpty(entity.getSteppedPrices())) {
                List<BmsTransportSteppedPrice> steppedPrices = entity.getSteppedPrices().stream()
                        .filter(e -> e.getId() != null)
                        .sorted(Comparator.comparing(BmsTransportSteppedPrice::getFm))
                        .collect(Collectors.toList());
                BmsTransportSteppedPrice preSteppedPrice = steppedPrices.get(0);
                if (preSteppedPrice.getFm() == null) {
                    throw new BmsException("从值不能为空");
                }
                // 到值为空表示无穷大，如果还存在下一条抛出“阶梯价格区间出现交叉范围”
                if (preSteppedPrice.getTo() != null) {
                    // 从值大于等于到值
                    if (preSteppedPrice.getTo().compareTo(preSteppedPrice.getFm()) <= 0) {
                        throw new BmsException("阶梯价格区间从值必须小于到值");
                    }
                } else if (steppedPrices.size() > 1) {
                    throw new BmsException("阶梯价格区间出现交叉范围");
                }
                for (int i = 1; i < steppedPrices.size(); i++) {
                    BmsTransportSteppedPrice steppedPrice = steppedPrices.get(i);
                    if (steppedPrice.getFm() == null) {
                        throw new BmsException("从值不能为空");
                    }
                    // 从值小于上一条到值
                    if (steppedPrice.getFm().compareTo(preSteppedPrice.getTo()) < 0) {
                        throw new BmsException("阶梯价格区间出现交叉范围");
                    }
                    // 到值为空表示无穷大，如果还存在下一条抛出“阶梯价格区间出现交叉范围”
                    if (steppedPrice.getTo() != null && steppedPrice.getFm().compareTo(steppedPrice.getTo()) >= 0) {
                        throw new BmsException("阶梯价格区间从值必须小于到值");
                    } else if (steppedPrice.getTo() == null && (i + 1) < steppedPrices.size()) {
                        throw new BmsException("阶梯价格区间出现交叉范围");
                    }
                    preSteppedPrice = steppedPrice;
                }
            }
        }
    }

    /**
     * 保存（扩展实体）
     */
    @Transactional
    public void saveEntity(BmsTransportPriceEntity entity) {
        this.checkSaveBefore(entity);
        if (StringUtils.isBlank(entity.getIsUseStep())) {
            entity.setIsUseStep(BmsConstants.NO);
        }
        if (entity.getPrice() == null) {
            entity.setPrice(BigDecimal.ONE);
        }
        this.save(entity);

        for (BmsTransportSteppedPrice bmsTransportSteppedPrice : entity.getSteppedPrices()) {
            if (bmsTransportSteppedPrice.getId() == null) {
                continue;
            }
            // 启用阶梯价格才保存
            if (BmsConstants.YES.equals(entity.getIsUseStep())) {
                bmsTransportSteppedPrice.setFkId(entity.getId());
                bmsTransportSteppedPrice.setOrgId(entity.getOrgId());
                if (bmsTransportSteppedPrice.getPrice() == null) {
                    bmsTransportSteppedPrice.setPrice(BigDecimal.ONE);
                }
                bmsTransportSteppedPriceService.save(bmsTransportSteppedPrice);
            } else {
                // 不启用阶梯价格且为新增记录，跳过；否则，删除
                if (StringUtils.isBlank(bmsTransportSteppedPrice.getId())) {
                    continue;
                }
                bmsTransportSteppedPrice.setDelFlag(BmsTransportSteppedPrice.DEL_FLAG_DELETE);
                bmsTransportSteppedPriceService.save(bmsTransportSteppedPrice);
            }
        }
    }

    /**
     * 根据ID删除运输价格，同时删除关联的阶梯价格
     */
    @Transactional
    public void deleteById(String id) {
        if (StringUtils.isBlank(id)) {
            return;
        }

        this.delete(new BmsTransportPrice(id));
        bmsTransportSteppedPriceService.deleteByFkId(id);
    }

    /**
     * 根据外键ID删除运输价格
     */
    @Transactional
    public void deleteByFkId(String fkId) {
        if (StringUtils.isBlank(fkId)) {
            return;
        }
        List<BmsTransportPriceEntity> entities = this.findByFkId(fkId);
        if (CollectionUtil.isEmpty(entities)) {
            return;
        }
        for (BmsTransportPriceEntity entity : entities) {
            this.deleteById(entity.getId());
            bmsTransportSteppedPriceService.deleteByFkId(entity.getId());
        }
    }

    /**
     * 根据外键ID查询运输价格（扩展实体）
     */
    public List<BmsTransportPriceEntity> findByFkId(String fkId) {
        if (StringUtils.isBlank(fkId)) {
            return Lists.newArrayList();
        }

        List<BmsTransportPriceEntity> entities = mapper.findByFkId(fkId);
        if (CollectionUtil.isNotEmpty(entities)) {
            for (BmsTransportPriceEntity entity : entities) {
                entity.setSteppedPrices(bmsTransportSteppedPriceService.findByFkId(entity.getId()));
            }
        }
        return entities;
    }

    /**
     * 根据运输体系编码查询运输价格（扩展实体）
     */
    @SuppressWarnings("unchecked")
    public Page<BmsTransportPriceEntity> findByTransportCode(Page page, BmsTransportPriceEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        if (StringUtils.isNotBlank(qEntity.getTransportGroupCode()) && StringUtils.isNotBlank(qEntity.getOrgId())) {
            page.setList(mapper.findByTransportCode(qEntity));
        }
        return page;
    }

    /**
     * 获取运输价格
     *
     * @param contractDetailId 合同明细ID
     * @param startPlaceCode   起点编码
     * @param endPlaceCode     终点编码
     * @param regionCode       区域编码
     * @param carTypeCode      车型编码
     * @param orgId            机构ID
     * @return 运输价格
     */
    public BmsContractPrice getContractPrice(String contractDetailId, String startPlaceCode, String endPlaceCode, String regionCode, String carTypeCode, String orgId) {
        BmsContractPrice bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, endPlaceCode, regionCode, carTypeCode, orgId);
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, endPlaceCode, regionCode, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(endPlaceCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, null, regionCode, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(regionCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, endPlaceCode, null, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, endPlaceCode, regionCode, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(endPlaceCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, null, regionCode, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(regionCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, endPlaceCode, null, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, endPlaceCode, regionCode, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(endPlaceCode) && StringUtils.isNotBlank(regionCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, null, null, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(endPlaceCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, null, regionCode, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(regionCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, endPlaceCode, null, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(endPlaceCode) && StringUtils.isNotBlank(regionCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, null, null, carTypeCode, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(endPlaceCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, null, regionCode, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(startPlaceCode) && StringUtils.isNotBlank(regionCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, endPlaceCode, null, null, orgId);
        }
        if (bmsContractPrice == null && StringUtils.isNotBlank(endPlaceCode) && StringUtils.isNotBlank(regionCode) && StringUtils.isNotBlank(carTypeCode)) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, startPlaceCode, null, null, null, orgId);
        }
        if (bmsContractPrice == null) {
            bmsContractPrice = mapper.getContractPrice(contractDetailId, null, null, null, null, orgId);
        }
        if (bmsContractPrice != null) {
            bmsContractPrice.setSteppedPrices(bmsTransportSteppedPriceService.findSteppedPriceByFkId(bmsContractPrice.getId()));
        }
        if (bmsContractPrice == null) {
            bmsContractPrice = new BmsContractPrice();
        }
        return bmsContractPrice;
    }

    @Transactional
    public void importFile(BmsTransportGroup group, List<BmsTransportPriceTemplate> dataList) {
        if (group == null || StringUtils.isBlank(group.getId())) {
            throw new BmsException("导入失败");
        }
        StringBuilder errMsg = new StringBuilder();
        dataList.stream().collect(Collectors.groupingBy(o -> o.getCarTypeCode() + o.getStartPlaceCode() + o.getEndPlaceCode() + o.getRegionCode())).values()
                .stream().filter(list -> list.size() > 1)
                .forEach(list -> errMsg
                        .append("起点编码：").append(list.get(0).getStartPlaceCode())
                        .append(" 终点编码：").append(list.get(0).getEndPlaceCode())
                        .append(" 车型编码：").append(list.get(0).getCarTypeCode())
                        .append(" 区域编码：").append(list.get(0).getRegionCode())
                        .append("存在重复值<br>"));
        if (StringUtils.isNotBlank(errMsg)) {
            throw new BmsException(errMsg.toString());
        }
        // 删除原有记录
        this.deleteByFkId(group.getId());
        // 插入新记录
        List<BmsTransportPriceEntity> list = Lists.newArrayList();
        for (int i = 0; i < dataList.size(); i++) {
            BmsTransportPriceTemplate data = dataList.get(i);
            if (StringUtils.isNotBlank(data.getRegionCode())) {
                SysCommonRegion sysCommonRegion = sysCommonRegionService.getByCode(data.getRegionCode(), group.getOrgId());
                if (sysCommonRegion == null) {
                    errMsg.append("第").append(i + 2).append("行").append("区域不存在!<br>");
                }
            }
            if (StringUtils.isNotBlank(data.getStartPlaceCode())) {
                Area area = areaService.getByCode(data.getStartPlaceCode());
                if (area == null) {
                    errMsg.append("第").append(i + 2).append("行").append("起点不存在!<br>");
                }
            }
            if (StringUtils.isNotBlank(data.getEndPlaceCode())) {
                Area area = areaService.getByCode(data.getEndPlaceCode());
                if (area == null) {
                    errMsg.append("第").append(i + 2).append("行").append("终点不存在!<br>");
                }
            }

            BmsTransportPriceEntity entity = new BmsTransportPriceEntity();
            List<BmsTransportSteppedPrice> steppedPrices = Lists.newArrayList();
            if ((data.getFm1() == null && data.getTo1() != null)
                    || (data.getFm1() != null && data.getTo1() != null && data.getFm1().compareTo(data.getTo1()) >= 0)) {
                errMsg.append("第").append(i + 2).append("行").append("区间价格维护错误!<br>");
            }
            if ((data.getFm2() == null && data.getTo2() != null)
                    || (data.getFm2() != null && data.getTo2() != null && data.getFm2().compareTo(data.getTo2()) >= 0)
                    || (data.getFm2() != null && data.getTo1() != null && data.getFm2().compareTo(data.getTo1()) < 0)) {
                errMsg.append("第").append(i + 2).append("行").append("区间价格维护错误!<br>");
            }
            if ((data.getFm3() == null && data.getTo3() != null)
                    || (data.getFm3() != null && data.getTo3() != null && data.getFm3().compareTo(data.getTo3()) >= 0)
                    || (data.getFm3() != null && data.getTo2() != null && data.getFm3().compareTo(data.getTo2()) < 0)) {
                errMsg.append("第").append(i + 2).append("行").append("区间价格维护错误!<br>");
            }
            if ((data.getFm4() == null && data.getTo4() != null)
                    || (data.getFm4() != null && data.getTo4() != null && data.getFm4().compareTo(data.getTo4()) >= 0)
                    || (data.getFm4() != null && data.getTo3() != null && data.getFm4().compareTo(data.getTo3()) < 0)) {
                errMsg.append("第").append(i + 2).append("行").append("区间价格维护错误!<br>");
            }
            if ((data.getFm5() == null && data.getTo5() != null)
                    || (data.getFm5() != null && data.getTo5() != null && data.getFm5().compareTo(data.getTo5()) >= 0)
                    || (data.getFm5() != null && data.getTo4() != null && data.getFm5().compareTo(data.getTo4()) < 0)) {
                errMsg.append("第").append(i + 2).append("行").append("区间价格维护错误!<br>");
            }
            if (data.getFm1() != null) {
                BmsTransportSteppedPrice steppedPrice = new BmsTransportSteppedPrice();
                steppedPrice.setId("");
                steppedPrice.setFm(data.getFm1());
                steppedPrice.setTo(data.getTo1());
                steppedPrice.setPrice(data.getPrice1());
                steppedPrices.add(steppedPrice);
            }
            if (data.getFm2() != null) {
                BmsTransportSteppedPrice steppedPrice = new BmsTransportSteppedPrice();
                steppedPrice.setId("");
                steppedPrice.setFm(data.getFm2());
                steppedPrice.setTo(data.getTo2());
                steppedPrice.setPrice(data.getPrice2());
                steppedPrices.add(steppedPrice);
            }
            if (data.getFm3() != null) {
                BmsTransportSteppedPrice steppedPrice = new BmsTransportSteppedPrice();
                steppedPrice.setId("");
                steppedPrice.setFm(data.getFm3());
                steppedPrice.setTo(data.getTo3());
                steppedPrice.setPrice(data.getPrice3());
                steppedPrices.add(steppedPrice);
            }
            if (data.getFm4() != null) {
                BmsTransportSteppedPrice steppedPrice = new BmsTransportSteppedPrice();
                steppedPrice.setId("");
                steppedPrice.setFm(data.getFm4());
                steppedPrice.setTo(data.getTo4());
                steppedPrice.setPrice(data.getPrice4());
                steppedPrices.add(steppedPrice);
            }
            if (data.getFm5() != null) {
                BmsTransportSteppedPrice steppedPrice = new BmsTransportSteppedPrice();
                steppedPrice.setId("");
                steppedPrice.setFm(data.getFm5());
                steppedPrice.setTo(data.getTo5());
                steppedPrice.setPrice(data.getPrice5());
                steppedPrices.add(steppedPrice);
            }

            BeanUtils.copyProperties(data, entity);
            entity.setFkId(group.getId());
            entity.setOrgId(group.getOrgId());
            entity.setIsUseStep(CollectionUtil.isNotEmpty(steppedPrices) ? BmsConstants.YES : BmsConstants.NO);
            entity.setSteppedPrices(steppedPrices);
            list.add(entity);
        }
        if (StringUtils.isNotBlank(errMsg)) {
            throw new BmsException(errMsg.toString());
        }
        for (BmsTransportPriceEntity entity : list) {
            this.saveEntity(entity);
        }
    }

}