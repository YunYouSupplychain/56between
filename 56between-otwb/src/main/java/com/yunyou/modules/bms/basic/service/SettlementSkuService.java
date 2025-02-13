package com.yunyou.modules.bms.basic.service;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.SettlementSku;
import com.yunyou.modules.bms.basic.entity.SettlementSkuSupplier;
import com.yunyou.modules.bms.basic.entity.extend.SettlementSkuEntity;
import com.yunyou.modules.bms.basic.mapper.SettlementSkuMapper;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.common.utils.number.BigDecimalUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 结算商品Service
 *
 * @author Jianhua Liu
 * @version 2019-06-20
 */
@Service
@Transactional(readOnly = true)
public class SettlementSkuService extends CrudService<SettlementSkuMapper, SettlementSku> {
    @Autowired
    private SettlementSkuSupplierService settlementSkuSupplierService;

    public Page<SettlementSku> findPage(Page<SettlementSku> page, SettlementSkuEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }

    public Page<SettlementSku> findGrid(Page<SettlementSku> page, SettlementSkuEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGrid(entity));
        return page;
    }

    public Page<SettlementSku> findGridDataAndSupplier(Page<SettlementSku> page, SettlementSkuEntity entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findGridDataAndSupplier(entity));
        return page;
    }

    public SettlementSkuEntity getEntity(String id) {
        SettlementSkuEntity entity = mapper.getEntity(id);
        if (entity != null) {
            entity.setSkuSuppliers(settlementSkuSupplierService.findBySkuId(entity.getId(), entity.getOrgId()));
        }
        return entity;
    }

    public SettlementSku getByOwnerAndSku(String ownerCode, String skuCode, String orgId) {
        return mapper.getByOwnerAndSku(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void save(SettlementSkuEntity entity) {
        super.save(entity);
        for (SettlementSkuSupplier skuSupplier : entity.getSkuSuppliers()) {
            if (skuSupplier.getId() == null) {
                continue;
            }
            if (SettlementSkuSupplier.DEL_FLAG_NORMAL.equals(skuSupplier.getDelFlag())) {
                skuSupplier.setSkuId(entity.getId());
                skuSupplier.setOrgId(entity.getOrgId());
                settlementSkuSupplierService.save(skuSupplier);
            } else {
                settlementSkuSupplierService.delete(skuSupplier);
            }
        }
    }

    @Transactional
    public void update(SettlementSku entity) {
        mapper.update(entity);
    }

    @Transactional
    public void delete(SettlementSkuEntity entity) {
        for (SettlementSkuSupplier skuSupplier : entity.getSkuSuppliers()) {
            settlementSkuSupplierService.delete(skuSupplier);
        }
        super.delete(entity);
    }

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        settlementSkuSupplierService.remove(ownerCode, skuCode, orgId);
        mapper.remove(ownerCode, skuCode, orgId);
    }

    @Transactional
    public void batchInsert(List<SettlementSku> list) {
        for (int i = 0; i < list.size(); i += 999) {
            if (list.size() - i < 999) {
                mapper.batchInsert(list.subList(i, list.size()));
            } else {
                mapper.batchInsert(list.subList(i, i + 999));
            }
        }
    }

    public Map<String, Double> getConversionQty(Double csUomQty, Double plUomQty, Double qty, Double boxQty, Double palletQty) {
        Map<String, Double> qtyMap = Maps.newHashMap();
        if (qty == null) {
            // 数量 为空
            if (boxQty == null) {
                // 箱数 为空
                if (palletQty == null) {
                    // 托盘数 为空
                    qtyMap.put("eaQty", qty);
                    qtyMap.put("csQty", boxQty);
                    qtyMap.put("plQty", palletQty);
                } else {
                    // 托盘数 不为空
                    if (csUomQty == null || plUomQty == null) {
                        throw new BmsException("数量、箱数为空时，结算商品包装比例不能为空！");
                    } else {
                        qtyMap.put("eaQty", palletQty * plUomQty);
                        qtyMap.put("csQty", Math.ceil((palletQty * plUomQty) / csUomQty));
                        qtyMap.put("plQty", palletQty);
                    }
                }
            } else {
                // 箱数 不为空
                if (palletQty == null) {
                    // 托盘数 为空
                    if (csUomQty == null || plUomQty == null) {
                        throw new BmsException("数量、托盘数为空时，结算商品包装比例不能为空！");
                    } else {
                        qtyMap.put("eaQty", boxQty * csUomQty);
                        qtyMap.put("csQty", boxQty);
                        qtyMap.put("plQty", Math.ceil((boxQty * csUomQty) / plUomQty));
                    }
                } else {
                    // 托盘数 不为空
                    if (plUomQty != null) {
                        qtyMap.put("eaQty", palletQty * plUomQty);
                        qtyMap.put("csQty", boxQty);
                        qtyMap.put("plQty", palletQty);
                    } else if (csUomQty != null) {
                        qtyMap.put("eaQty", boxQty * csUomQty);
                        qtyMap.put("csQty", boxQty);
                        qtyMap.put("plQty", palletQty);
                    } else {
                        throw new BmsException("数量为空时，结算商品包装比例不能全为空！");
                    }
                }
            }
        } else {
            // 数量 不为空
            if (boxQty == null) {
                // 箱数 为空
                if (palletQty == null) {
                    // 托盘数 为空
                    if (csUomQty == null || plUomQty == null) {
                        throw new BmsException("箱数、托盘数为空时，结算商品包装比例不能为空！");
                    } else {
                        qtyMap.put("eaQty", qty);
                        qtyMap.put("csQty", Math.ceil(qty / csUomQty));
                        qtyMap.put("plQty", Math.ceil(qty / plUomQty));
                    }
                } else {
                    // 托盘数 不为空
                    if (csUomQty == null) {
                        throw new BmsException("箱数为空时，箱包装比例不能为空！");
                    } else {
                        qtyMap.put("eaQty", qty);
                        qtyMap.put("csQty", Math.ceil(qty / csUomQty));
                        qtyMap.put("plQty", palletQty);
                    }
                }
            } else {
                // 箱数 不为空
                if (palletQty == null) {
                    // 托盘数 为空
                    if (plUomQty == null) {
                        throw new BmsException("托盘数为空时，托盘包装比例不能为空！");
                    } else {
                        qtyMap.put("eaQty", qty);
                        qtyMap.put("csQty", boxQty);
                        qtyMap.put("plQty", Math.ceil(qty / plUomQty));
                    }
                } else {
                    // 托盘数 不为空
                    qtyMap.put("eaQty", qty);
                    qtyMap.put("csQty", boxQty);
                    qtyMap.put("plQty", palletQty);
                }
            }
        }
        return qtyMap;
    }

    /**
     * 换算单位数量
     *
     * @param ownerCode  货主编码
     * @param skuCode    商品编码
     * @param orgId      机构ID
     * @param sourceUnit 原单位
     * @param sourceQty  原单位数量
     * @param targetUnit 目标单位
     * @return 目标单位数量
     */
    public double convertQty(String ownerCode, String skuCode, String orgId, String sourceUnit, Double sourceQty, String targetUnit) {
        if (sourceQty == null) {
            return 0;
        }
        SettlementSku settlementSku = this.getByOwnerAndSku(ownerCode, skuCode, orgId);
        if (settlementSku == null) {
            return 0;
        }
        double ea;
        if ("EA".equals(sourceUnit)) {
            ea = sourceQty;
        } else if ("IP".equals(sourceUnit) && settlementSku.getIpQuantity() != null) {
            ea = BigDecimalUtil.mul(sourceQty, settlementSku.getIpQuantity());
        } else if ("CS".equals(sourceUnit) && settlementSku.getCsQuantity() != null) {
            ea = BigDecimalUtil.mul(sourceQty, settlementSku.getCsQuantity());
        } else if ("PL".equals(sourceUnit) && settlementSku.getPlQuantity() != null) {
            ea = BigDecimalUtil.mul(sourceQty, settlementSku.getPlQuantity());
        } else if ("OT".equals(sourceUnit) && settlementSku.getOtQuantity() != null) {
            ea = BigDecimalUtil.mul(sourceQty, settlementSku.getOtQuantity());
        } else {
            return 0;
        }
        if ("EA".equals(targetUnit)) {
            return ea;
        } else if ("IP".equals(targetUnit)) {
            return BigDecimalUtil.div(ea, settlementSku.getIpQuantity(), 0, BigDecimal.ROUND_UP);
        } else if ("CS".equals(targetUnit)) {
            return BigDecimalUtil.div(ea, settlementSku.getCsQuantity(), 0, BigDecimal.ROUND_UP);
        } else if ("PL".equals(targetUnit)) {
            return BigDecimalUtil.div(ea, settlementSku.getPlQuantity(), 0, BigDecimal.ROUND_UP);
        } else if ("OT".equals(targetUnit)) {
            return BigDecimalUtil.div(ea, settlementSku.getOtQuantity(), 0, BigDecimal.ROUND_UP);
        } else {
            return 0;
        }
    }

    public double calcWeight(String ownerCode, String skuCode, String orgId, Double qty) {
        if (qty == null) {
            return 0;
        }
        SettlementSku settlementSku = this.getByOwnerAndSku(ownerCode, skuCode, orgId);
        if (settlementSku == null || settlementSku.getGrossWeight() == null) {
            return 0;
        }
        return BigDecimalUtil.mul(qty, settlementSku.getGrossWeight());
    }

    public double calcVolume(String ownerCode, String skuCode, String orgId, Double qty) {
        if (qty == null) {
            return 0;
        }
        SettlementSku settlementSku = this.getByOwnerAndSku(ownerCode, skuCode, orgId);
        if (settlementSku == null || settlementSku.getVolume() == null) {
            return 0;
        }
        return BigDecimalUtil.mul(qty, settlementSku.getVolume());
    }
}