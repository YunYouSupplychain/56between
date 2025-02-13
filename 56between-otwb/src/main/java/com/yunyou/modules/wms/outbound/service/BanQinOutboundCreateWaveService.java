package com.yunyou.modules.wms.outbound.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetailWv;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvHeader;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvDetailService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvDetailWvService;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvHeaderService;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmWvHeader;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成波次计划
 *
 * @author WMJ
 * @version 2019/02/20
 */
@Service
@Transactional(readOnly = true)
public class BanQinOutboundCreateWaveService {
    // 波次规则主表
    @Autowired
    protected BanQinCdRuleWvHeaderService cdRuleWvHeaderService;
    // 波次规则子表
    @Autowired
    protected BanQinCdRuleWvDetailService cdRuleWvDetailService;
    // 波次规则子表波次限制
    @Autowired
    protected BanQinCdRuleWvDetailWvService cdRuleWvDetailWvService;
    // 波次单
    @Autowired
    protected BanQinWmWvHeaderService wmWvHeaderService;
    // 波次单明细
    @Autowired
    protected BanQinWmWvDetailService wmWvDetailService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SynchronizedNoService noService;
    @Autowired
    private BanQinWmSoHeaderService wmSoHeaderService;

    /**
     * 生成波次计划
     *
     * @param soNos
     * @param waveRuleCode
     * @throws WarehouseException
     */
    @Transactional
    public void createWave(List<String> soNos, String waveRuleCode, String orgId) throws WarehouseException {
        soNos = getNotWaveNoOfSoNos(soNos, orgId);
        if (StringUtils.isNotEmpty(waveRuleCode)) {
            BanQinCdRuleWvHeader wvRuleHeader = cdRuleWvHeaderService.getByCode(waveRuleCode, orgId);
            if (wvRuleHeader != null) {
                List<BanQinCdRuleWvDetail> wvRuleDetails = this.cdRuleWvDetailService.getByRuleCodeAndIsEnable(wvRuleHeader.getRuleCode(), orgId);
                for (BanQinCdRuleWvDetail rule : wvRuleDetails) {
                    String mainCode = rule.getMainCode();
                    List<Object[]> orderItems = Lists.newArrayList();
                    String sql;
                    if (WmsCodeMaster.WAVE_RULE_2.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_6.getCode().equals(mainCode)) {
                        sql = this.queryDoubleOrderSql(rule);
                    } else if (WmsCodeMaster.WAVE_RULE_7.getCode().equals(mainCode)) {
                        sql = this.queryOrderQtySql(rule);
                    } else {
                        sql = this.querySql(rule);
                    }
                    if (soNos.size() > 0) {
                        List<Map<String, Object>> listObjects = jdbcTemplate.queryForList(createString(sql, soNos));
                        orderItems = mapToObjectArray(listObjects);
                    }
                    if (orderItems.size() > 0) {
                        if (WmsCodeMaster.WAVE_RULE_1.getCode().equals(mainCode)) {
                            int orderQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERQTY.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() == 1) {
                                BanQinCdRuleWvDetailWv wvRuleDetailWv = wvRuleDetailWvs.get(0);
                                String value = wvRuleDetailWv.getValue();
                                String operator = wvRuleDetailWv.getOperator();
                                if (!WmsCodeMaster.OPERATOR_1.getCode().equals(operator)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单数配置错误");
                                }
                                if (StringUtils.isEmpty(value)) {
                                    throw new WarehouseException(waveRuleCode + "参数错误");
                                }
                                try {
                                    orderQty = new Integer(value);
                                } catch (NumberFormatException e) {
                                    throw new WarehouseException(waveRuleCode + "参数错误");
                                }
                                if (orderQty < 1) {
                                    throw new WarehouseException(waveRuleCode + "参数错误");
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems, orderQty);
                                this.saveWaves(waves, rule, orgId);
                                for (List<String> j : waves.values()) {
                                    soNos.removeAll(j);
                                }
                            } else if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() > 1) {
                                throw new WarehouseException(waveRuleCode + "参数错误");
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单数不存在");
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_2.getCode().equals(mainCode)) {
                            int orderDoubleQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERDOUBLE.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() == 1) {
                                BanQinCdRuleWvDetailWv wvRuleDetailWv = wvRuleDetailWvs.get(0);
                                String value = wvRuleDetailWv.getValue();
                                String operator = wvRuleDetailWv.getOperator();
                                if (!(WmsCodeMaster.OPERATOR_1.getCode().equals(operator) || WmsCodeMaster.OPERATOR_5.getCode().equals(operator) || WmsCodeMaster.OPERATOR_6.getCode().equals(operator))) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                }
                                if (StringUtils.isEmpty(value)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                }
                                try {
                                    orderDoubleQty = new Integer(value);
                                } catch (NumberFormatException e) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                }
                                if (orderDoubleQty < 1) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems);
                                Map<String, List<String>> swaves = new HashMap<>();
                                for (String key : waves.keySet()) {
                                    List<String> orders = waves.get(key);
                                    if (WmsCodeMaster.OPERATOR_5.getCode().equals(operator) && orders.size() > orderDoubleQty) {
                                        swaves.put(key, orders);
                                    } else if (WmsCodeMaster.OPERATOR_1.getCode().equals(operator) && orders.size() == orderDoubleQty) {
                                        swaves.put(key, orders);
                                    } else if (WmsCodeMaster.OPERATOR_6.getCode().equals(operator) && orders.size() >= orderDoubleQty) {
                                        swaves.put(key, orders);
                                    }
                                }
                                this.saveWaves(swaves, rule, orgId);
                                for (List<String> j : swaves.values()) {
                                    soNos.removeAll(j);
                                }
                            } else if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() > 1) {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率不存在");
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_3.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_4.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_5.getCode().equals(mainCode)) {
                            Map<String, List<String>> waves = this.groupOrders(orderItems);
                            this.saveWaves(waves, rule, orgId);
                            for (List<String> j : waves.values()) {
                                soNos.removeAll(j);
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_6.getCode().equals(mainCode)) {
                            int orderDoubleQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERDOUBLE_GROUP.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() == 1) {
                                BanQinCdRuleWvDetailWv wvRuleDetailWv = wvRuleDetailWvs.get(0);
                                String value = wvRuleDetailWv.getValue();
                                String operator = wvRuleDetailWv.getOperator();
                                if (!WmsCodeMaster.OPERATOR_1.getCode().equals(operator)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组配置错误");
                                }
                                if (StringUtils.isEmpty(value)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组配置错误");
                                }
                                try {
                                    orderDoubleQty = new Integer(value);
                                } catch (NumberFormatException e) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组配置错误");
                                }
                                if (orderDoubleQty < 1) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组配置错误");
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems);
                                Map<String, List<String>> swaves = new HashMap<>();
                                for (String key : waves.keySet()) {
                                    List<Object[]> collect = orderItems.stream().filter(array -> key.equals(array[0])).collect(Collectors.toList());
                                    swaves.putAll(this.groupOrders(collect, orderDoubleQty));
                                }
                                this.saveWaves(swaves, rule, orgId);
                                for (List<String> j : swaves.values()) {
                                    soNos.removeAll(j);
                                }
                            } else if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() > 1) {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组配置错误");
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率分组不存在");
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_7.getCode().equals(mainCode)) {
                            int orderDoubleQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERQTY.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() == 1) {
                                BanQinCdRuleWvDetailWv wvRuleDetailWv = wvRuleDetailWvs.get(0);
                                String value = wvRuleDetailWv.getValue();
                                String operator = wvRuleDetailWv.getOperator();
                                if (!WmsCodeMaster.OPERATOR_1.getCode().equals(operator)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                }
                                if (StringUtils.isEmpty(value)) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                }
                                try {
                                    orderDoubleQty = new Integer(value);
                                } catch (NumberFormatException e) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                }
                                if (orderDoubleQty < 1) {
                                    throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems);
                                Map<String, List<String>> swaves = new HashMap<>();
                                for (String key : waves.keySet()) {
                                    List<Object[]> collect = orderItems.stream().filter(array -> key.equals(array[0])).collect(Collectors.toList());
                                    swaves.putAll(this.groupOrders(collect, orderDoubleQty));
                                }
                                this.saveWaves(swaves, rule, orgId);
                                for (List<String> j : swaves.values()) {
                                    soNos.removeAll(j);
                                }
                            } else if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() > 1) {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组不存在");
                            }
                        }
                    }
                }
            } else {
                throw new WarehouseException("波次规则" + waveRuleCode + "不存在");
            }
        }
    }

    /**
     * 生成波次计划
     *
     * @param soNos
     * @param waveRuleCode
     * @throws WarehouseException
     */
    @Transactional
    public void createWaveNew(List<String> soNos, String waveRuleCode, String orgId) throws WarehouseException {
        if (soNos.size() == 0) return;
        soNos = getNotWaveNoOfSoNos(soNos, orgId);
        if (StringUtils.isNotEmpty(waveRuleCode)) {
            BanQinCdRuleWvHeader wvRuleHeader = cdRuleWvHeaderService.getByCode(waveRuleCode, orgId);
            if (wvRuleHeader != null) {
                List<BanQinCdRuleWvDetail> wvRuleDetails = this.cdRuleWvDetailService.getByRuleCodeAndIsEnable(wvRuleHeader.getRuleCode(), orgId);
                for (BanQinCdRuleWvDetail rule : wvRuleDetails) {
                    String mainCode = rule.getMainCode();
                    List<Object[]> orderItems = Lists.newArrayList();
                    String sql;
                    if (WmsCodeMaster.WAVE_RULE_2.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_6.getCode().equals(mainCode)) {
                        sql = this.queryDoubleOrderSql(rule);
                    } else if (WmsCodeMaster.WAVE_RULE_7.getCode().equals(mainCode)) {
                        sql = this.queryOrderQtySql(rule);
                    } else {
                        sql = this.querySql(rule);
                    }
                    if (soNos.size() > 0) {
                        List<Map<String, Object>> listObjects = jdbcTemplate.queryForList(createString(sql, soNos));
                        orderItems = mapToObjectArray(listObjects);
                    }
                    if (orderItems.size() > 0) {
                        if (WmsCodeMaster.WAVE_RULE_1.getCode().equals(mainCode)) {
                            int orderQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERQTY.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() >= 1) {
                                for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                    String value = wvRuleDetailWv.getValue();
                                    String operator = wvRuleDetailWv.getOperator();
                                    if (!WmsCodeMaster.OPERATOR_1.getCode().equals(operator) && !WmsCodeMaster.OPERATOR_3.getCode().equals(operator) && !WmsCodeMaster.OPERATOR_5.getCode().equals(operator)) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单数配置错误");
                                    }
                                    if (StringUtils.isEmpty(value)) {
                                        throw new WarehouseException(waveRuleCode + "参数错误");
                                    }
                                    try {
                                        orderQty = new Integer(value);
                                    } catch (NumberFormatException e) {
                                        throw new WarehouseException(waveRuleCode + "参数错误");
                                    }
                                    if (orderQty < 1) {
                                        throw new WarehouseException(waveRuleCode + "参数错误");
                                    }
                                }
                                for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                    String operator = wvRuleDetailWv.getOperator();
                                    orderQty = new Integer(wvRuleDetailWv.getValue());
                                    if (WmsCodeMaster.OPERATOR_1.getCode().equals(operator)) {
                                        Map<String, List<String>> waves = this.groupOrders(orderItems, orderQty);
                                        this.saveWaves(waves, rule, orgId);
                                        for (List<String> j : waves.values()) {
                                            soNos.removeAll(j);
                                        }
                                        Iterator<Object[]> iterator = orderItems.iterator();
                                        while (iterator.hasNext()) {
                                            Object[] next = iterator.next();
                                            for (List<String> soNoList : waves.values()) {
                                                if (soNoList.contains(next[1].toString())) {
                                                    iterator.remove();
                                                }
                                            }
                                        }
                                    } else if (WmsCodeMaster.OPERATOR_3.getCode().equals(operator)) {
                                        if (orderItems.size() < orderQty) {
                                            Map<String, List<String>> waves = this.groupOrders(orderItems, orderItems.size());
                                            this.saveWaves(waves, rule, orgId);
                                            for (List<String> j : waves.values()) {
                                                soNos.removeAll(j);
                                            }
                                            break;
                                        }
                                    } else if (WmsCodeMaster.OPERATOR_5.getCode().equals(operator)) {
                                        if (orderItems.size() > orderQty) {
                                            Map<String, List<String>> waves = this.groupOrders(orderItems, orderItems.size());
                                            this.saveWaves(waves, rule, orgId);
                                            for (List<String> j : waves.values()) {
                                                soNos.removeAll(j);
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单数不存在");
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_2.getCode().equals(mainCode)) {
                            int orderDoubleQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERQTY.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() >= 1) {
                                for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                    String value = wvRuleDetailWv.getValue();
                                    String operator = wvRuleDetailWv.getOperator();
                                    if (!(WmsCodeMaster.OPERATOR_1.getCode().equals(operator) || WmsCodeMaster.OPERATOR_3.getCode().equals(operator) || WmsCodeMaster.OPERATOR_5.getCode().equals(operator))) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                    }
                                    if (StringUtils.isEmpty(value)) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                    }
                                    try {
                                        orderDoubleQty = new Integer(value);
                                    } catch (NumberFormatException e) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                    }
                                    if (orderDoubleQty < 1) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率配置错误");
                                    }
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems);
                                for (String key : waves.keySet()) {
                                    Map<String, List<String>> swaves = new HashMap<>();
                                    for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                        List<String> orders = waves.get(key);
                                        String operator = wvRuleDetailWv.getOperator();
                                        orderDoubleQty = new Integer(wvRuleDetailWv.getValue());
                                        if (WmsCodeMaster.OPERATOR_1.getCode().equals(operator) && orders.size() == orderDoubleQty) {
                                            List<Object[]> collect = orderItems.stream().filter(array -> key.equals(array[0])).collect(Collectors.toList());
                                            swaves.putAll(this.groupOrders(collect, orderDoubleQty));
                                        } else if (WmsCodeMaster.OPERATOR_3.getCode().equals(operator) && orders.size() < orderDoubleQty) {
                                            swaves.put(key, orders);
                                        } else if (WmsCodeMaster.OPERATOR_5.getCode().equals(operator) && orders.size() > orderDoubleQty) {
                                            swaves.put(key, orders);
                                        }
                                        this.saveWaves(swaves, rule, orgId);
                                        for (List<String> j : swaves.values()) {
                                            soNos.removeAll(j);
                                            orders.removeAll(j);
                                        }
                                        if ((WmsCodeMaster.OPERATOR_3.getCode().equals(operator) && orders.size() < orderDoubleQty)
                                            || ((WmsCodeMaster.OPERATOR_5.getCode().equals(operator) && orders.size() > orderDoubleQty))) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单重合率不存在");
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_3.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_4.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_5.getCode().equals(mainCode)) {
                            Map<String, List<String>> waves = this.groupOrders(orderItems);
                            this.saveWaves(waves, rule, orgId);
                            for (List<String> j : waves.values()) {
                                soNos.removeAll(j);
                            }
                        } else if (WmsCodeMaster.WAVE_RULE_7.getCode().equals(mainCode) || WmsCodeMaster.WAVE_RULE_6.getCode().equals(mainCode)) {
                            int orderDoubleQty = 0;
                            List<BanQinCdRuleWvDetailWv> wvRuleDetailWvs = this.cdRuleWvDetailWvService.getByRuleCodeAndLineNoAndCondition(rule.getRuleCode(), rule.getLineNo(),
                                WmsCodeMaster.WAVE_CONDITION_ORDERQTY.getCode(), orgId);
                            if (wvRuleDetailWvs != null && wvRuleDetailWvs.size() >= 1) {
                                for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                    String value = wvRuleDetailWv.getValue();
                                    String operator = wvRuleDetailWv.getOperator();
                                    if (!(WmsCodeMaster.OPERATOR_1.getCode().equals(operator) || WmsCodeMaster.OPERATOR_3.getCode().equals(operator) || WmsCodeMaster.OPERATOR_5.getCode().equals(operator))) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                    }
                                    if (StringUtils.isEmpty(value)) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                    }
                                    try {
                                        orderDoubleQty = new Integer(value);
                                    } catch (NumberFormatException e) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                    }
                                    if (orderDoubleQty < 1) {
                                        throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组配置错误");
                                    }
                                }
                                Map<String, List<String>> waves = this.groupOrders(orderItems);
                                for (String key : waves.keySet()) {
                                    List<String> orders = waves.get(key);
                                    for (BanQinCdRuleWvDetailWv wvRuleDetailWv : wvRuleDetailWvs) {
                                        Map<String, List<String>> swaves = new HashMap<>();
                                        String operator = wvRuleDetailWv.getOperator();
                                        orderDoubleQty = new Integer(wvRuleDetailWv.getValue());
                                        if (WmsCodeMaster.OPERATOR_1.getCode().equals(operator)) {
                                            List<Object[]> collect = orderItems.stream().filter(array -> key.equals(array[0])).collect(Collectors.toList());
                                            swaves.putAll(this.groupOrders(collect, orderDoubleQty));
                                        } else if (WmsCodeMaster.OPERATOR_3.getCode().equals(operator) && orders.size() < orderDoubleQty) {
                                            swaves.put(key, orders);
                                        } else if (WmsCodeMaster.OPERATOR_5.getCode().equals(operator) && orders.size() > orderDoubleQty) {
                                            swaves.put(key, orders);
                                        }
                                        this.saveWaves(swaves, rule, orgId);
                                        for (List<String> j : swaves.values()) {
                                            soNos.removeAll(j);
                                            orders.removeAll(j);
                                        }
                                        if ((WmsCodeMaster.OPERATOR_3.getCode().equals(operator) && orders.size() < orderDoubleQty)
                                            || ((WmsCodeMaster.OPERATOR_5.getCode().equals(operator) && orders.size() > orderDoubleQty))) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                throw new WarehouseException("波次规则" + waveRuleCode + "按订单明细完全重合分组不存在");
                            }
                        }
                    }
                }
            } else {
                throw new WarehouseException("波次规则" + waveRuleCode + "不存在");
            }
        }
    }

    private List<Object[]> mapToObjectArray(List<Map<String, Object>> list) {
        List<Object[]> result = Lists.newArrayList();
        for (Map<String, Object> map : list) {
            result.add(map.values().toArray());
        }

        return result;
    }

    /**
     * 生成波次计划-不用规则，全部订单生成一个波次
     *
     * @param soNos
     * @throws WarehouseException
     */
    @Transactional
    public void createWave(List<String> soNos, String orgId) throws WarehouseException {
        // 校验
        if (soNos.size() == 0) {
            throw new WarehouseException("没有满足生成波次计划条件的订单");
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("**ALL**", soNos);
        this.saveWaves(map, null, orgId);
    }

    /**
     * 生成波次计划
     *
     * @param map
     * @param ruleDetail
     */
    @Transactional
    public void saveWaves(Map<String, List<String>> map, BanQinCdRuleWvDetail ruleDetail, String orgId) {
        for (String key : map.keySet()) {
            List<String> orders = map.get(key);
            if (CollectionUtil.isEmpty(orders) || orders.size() == 0) continue;
            BanQinWmWvHeader wmWvHeaderModel = new BanQinWmWvHeader();
            String waveNo = noService.getDocumentNo(GenNoType.WM_WAVE_NO.name());
            wmWvHeaderModel.setWaveNo(waveNo);
            wmWvHeaderModel.setStatus(WmsCodeMaster.WAVE_NEW.getCode());
            if (ruleDetail != null) {
                wmWvHeaderModel.setRemarks(ruleDetail.getDescr());
            } else {
                wmWvHeaderModel.setRemarks(null);
            }
            wmWvHeaderModel.setOrgId(orgId);
            wmWvHeaderService.save(wmWvHeaderModel);
            int i = 1;
            for (String order : orders) {
                BanQinWmWvDetail wmWvDetailModel = new BanQinWmWvDetail();
                wmWvDetailModel.setWaveNo(waveNo);
                wmWvDetailModel.setSoNo(order);
                wmWvDetailModel.setStatus(WmsCodeMaster.WAVE_NEW.getCode());// 状态
                wmWvDetailModel.setOrgId(orgId);
                wmWvDetailModel.setDef1(String.format("%04d", i));
                wmWvDetailService.save(wmWvDetailModel);
                i++;
            }
        }
    }

    /**
     * 按承运人，收货人，路线分组
     *
     * @param orderItems
     * @return
     */
    protected Map<String, List<String>> groupOrders(List<Object[]> orderItems) {
        Map<String, List<String>> carrier = new HashMap<>();
        List<String> nullorders = new ArrayList<>();
        for (Object[] obj : orderItems) {
            if (obj[0] == null) {
                nullorders.add(obj[1].toString());
            } else {
                if (!carrier.containsKey(obj[0].toString())) {
                    List<String> orders = new ArrayList<>();
                    orders.add(obj[1].toString());
                    carrier.put(obj[0].toString(), orders);
                } else {
                    List<String> orders = carrier.get(obj[0].toString());
                    orders.add(obj[1].toString());
                }
            }
        }
        if (nullorders.size() > 0) {
            carrier.put("**NULL**", nullorders);
        }
        return carrier;
    }

    /**
     * 按订单数分组
     *
     * @param orderItems
     * @param orderQty
     * @return
     */
    protected Map<String, List<String>> groupOrders(List<Object[]> orderItems, int orderQty) {
        Map<String, List<String>> carrier = new HashMap<>();
        Double waveQty = Math.floor(orderItems.size() / (orderQty + 0.0));
        for (int i = 0; i < waveQty; i++) {
            String waveNo = noService.getDocumentNo(GenNoType.WM_WAVE_NO.name());
            List<String> orders = new ArrayList<>();
            carrier.put(waveNo, orders);
            for (int j = 0; j < orderQty; j++) {
                if (orderItems.size() > i * orderQty + j) {
                    Object[] oj = orderItems.get(i * orderQty + j);
                    orders = carrier.get(waveNo);
                    orders.add(oj[1].toString());
                } else {
                    break;
                }
            }
        }
        return carrier;
    }

    /**
     * 按承运人，收货人，路线
     *
     * @param detail
     * @return
     */
    protected String querySql(BanQinCdRuleWvDetail detail) {
        String orgId = detail.getOrgId();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        // 按承运人
        if (WmsCodeMaster.WAVE_RULE_3.getCode().equals(detail.getMainCode())) {
            sql.append(" wsh.carrier_code ");
        }
        // 按收货人
        else if (WmsCodeMaster.WAVE_RULE_4.getCode().equals(detail.getMainCode())) {
            // 收货人同名同址(收货人名称、电话、地址+联系人名称、电话、地址)
            sql.append(" CONCAT_WS(wsh.consignee_name, wsh.consignee_tel, wsh.consignee_addr, wsh.contact_name, wsh.contact_tel, wsh.contact_addr) ");
        }
        // 按路线
        else if (WmsCodeMaster.WAVE_RULE_5.getCode().equals(detail.getMainCode())) {
            sql.append(" wsh.line ");
        } else {
            sql.append(" 1 ");
        }
        sql.append(" , wsh.so_no ");
        sql.append(" from wm_so_header wsh");
        sql.append(" where 1=1 ");
        sql.append(" and wsh.so_no in (:soNos) ");
        sql.append(" and wsh.org_id='" + orgId + "' ");
        // 订单限制条件
        if (StringUtils.isNotEmpty(detail.getSql())) {
            sql.append(detail.getSql());
        }
        return sql.toString();
    }

    /**
     * 按订单重合率
     *
     * @param detail
     * @return
     */
    protected String queryDoubleOrderSql(BanQinCdRuleWvDetail detail) {
        String orgId = detail.getOrgId();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(" GROUP_CONCAT(wsd.sku_code order by wsd.sku_code) as sku_code  ");
        sql.append(" , wsd.so_no ");
        sql.append(" from wm_so_header wsh");
        sql.append(" left join ");
        sql.append(" wm_so_detail wsd ");
        sql.append(" on wsh.so_no=wsd.so_no ");
        sql.append(" and wsh.org_id=wsd.org_id ");
        sql.append(" where 1=1 ");
        sql.append(" and wsh.so_no in (:soNos) ");
        sql.append(" and wsh.org_id='" + orgId + "' ");
        // 订单限制条件
        if (StringUtils.isNotEmpty(detail.getSql())) {
            sql.append(detail.getSql());
        }
        sql.append(" group by wsd.so_no ");
        sql.append(" order by sku_code,wsd.so_no ");
        return sql.toString();
    }

    /**
     * 按订单明细完全重合分组
     *
     * @param detail
     * @return
     */
    protected String queryOrderQtySql(BanQinCdRuleWvDetail detail) {
        String orgId = detail.getOrgId();
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(" GROUP_CONCAT(CONCAT(sku_code, ',', qty_so_ea) order by sku_code) as sku_qty  ");
        sql.append(" , wsd.so_no ");
        sql.append(" from wm_so_header wsh");
        sql.append(" left join ");
        sql.append(" wm_so_detail wsd ");
        sql.append(" on wsh.so_no=wsd.so_no ");
        sql.append(" and wsh.org_id=wsd.org_id ");
        sql.append(" where 1=1 ");
        sql.append(" and wsh.so_no in (:soNos) ");
        sql.append(" and wsh.org_id='" + orgId + "' ");
        // 订单限制条件
        if (StringUtils.isNotEmpty(detail.getSql())) {
            sql.append(detail.getSql());
        }
        sql.append(" group by wsd.so_no ");
        sql.append(" order by sku_code,wsd.so_no ");
        return sql.toString();
    }

    private String createString(String sql, List<String> soNos) {
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < soNos.size(); j++) {
            s.append("'").append(soNos.get(j)).append("',");
        }
        if (s.length() > 1) {
            s.deleteCharAt(s.length() - 1);
        }
        return sql.replace(":soNos", s);
    }

    /**
     * 过滤出还没有生成波次的订单
     *
     * @param soNos
     * @return
     */
    public List<String> getNotWaveNoOfSoNos(List<String> soNos, String orgId) {
        // 订单已经生成波次，不能再生成
        List<String> isWaveSoNos = wmWvDetailService.checkSoCreateWaveOrder(soNos, orgId);
        soNos.removeAll(isWaveSoNos);
        return soNos;
    }

    /**
     * 过滤未进行越库匹配的出库订单
     *
     * @param soNos
     * @return
     */
    public List<String> getNotCdOfSoNos(List<String> soNos, String orgId) {
        // 存在商品明细进行越库匹配，不能生成波次计划
        List<String> cdSoNos = wmSoHeaderService.checkSoIsCdByCreateWave(soNos, orgId);
        soNos.removeAll(cdSoNos);
        return soNos;
    }
}