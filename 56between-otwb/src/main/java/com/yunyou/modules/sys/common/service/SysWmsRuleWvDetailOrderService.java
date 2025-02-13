package com.yunyou.modules.sys.common.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.number.NumberUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailOrder;
import com.yunyou.modules.sys.common.mapper.SysWmsRuleWvDetailOrderMapper;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 */
@Service
@Transactional(readOnly = true)
public class SysWmsRuleWvDetailOrderService extends CrudService<SysWmsRuleWvDetailOrderMapper, SysWmsRuleWvDetailOrder> {
    @Autowired
    @Lazy
    private SysWmsRuleWvDetailService sysWmsRuleWvDetailService;

    @Transactional
    public void deleteByHeaderId(String headerId) {
        mapper.deleteByHeaderId(headerId);
    }

    @Transactional
    public void updateLineNo(String headerId, String lineNo) {
        mapper.execUpdateSql("update sys_wms_rule_wv_detail_order set line_no = '" + lineNo + "' where header_id = '" + headerId + "'");
    }

    @Transactional
    public String saveEntity(List<SysWmsRuleWvDetailOrder> list) {
        StringBuilder sql = new StringBuilder();
        if (CollectionUtil.isNotEmpty(list)) {
            for (SysWmsRuleWvDetailOrder order : list) {
                if (WmsCodeMaster.ORDER_ATT_VOLUME.getCode().equals(order.getOrderAttCode()) || WmsCodeMaster.ORDER_ATT_WEIGHT.getCode().equals(order.getOrderAttCode())) {
                    if (!NumberUtil.isNumber(order.getValue())) {
                        throw new RuntimeException("出库单属性为体积或重量时，值必须是数字!");
                    }
                    if (WmsCodeMaster.OPERATOR_LIKE.getCode().equals(order.getOperator()) || WmsCodeMaster.OPERATOR_NOT_LIKE.getCode().equals(order.getOperator())) {
                        throw new RuntimeException("运算符配置错误!");
                    }
                }
                if (WmsCodeMaster.ORDER_ATT_SKU.getCode().equals(order.getOrderAttCode())) {
                    if (!WmsCodeMaster.OPERATOR_1.getCode().equals(order.getOperator())) {
                        throw new RuntimeException("运算符配置错误!");
                    }
                }
                order.setOperator(StringEscapeUtils.unescapeHtml3(order.getOperator()));
                this.save(order);
                sql.append(createSql(order));
            }
            sysWmsRuleWvDetailService.updateSQL(list.get(0).getHeaderId(), sql.toString());
        }
        return sql.toString();
    }

    public String createSql(SysWmsRuleWvDetailOrder model) {
        String result = "";
        String valses = model.getValue().replaceAll("'", "").replaceAll("\"", "");
        if (WmsCodeMaster.ORDER_ATT_VOLUME.getCode().equals(model.getOrderAttCode()) || WmsCodeMaster.ORDER_ATT_WEIGHT.getCode().equals(model.getOrderAttCode())) {
            result += model.getAndOr() + " " + model.getLeftBracket() +
                "\t\tselect sum(ifnull(c." + (WmsCodeMaster.ORDER_ATT_VOLUME.getCode().equals(model.getOrderAttCode()) ? "cubic" : "gross_weight") + ", 0) * d.qty_so_ea)" +
                " from wm_so_detail d\n" +
                "\t\tinner join cd_wh_sku c on d.owner_code = c.owner_code and d.sku_code = c.sku_code\n" +
                "\t\twhere d.so_no = wsh.so_no and d.org_id = wsh.org_id)" +
                model.getOperator() + " " + valses + " ";
        } else if (WmsCodeMaster.ORDER_ATT_SKU.getCode().equals(model.getOrderAttCode())) {
            String[] split = valses.split(",", -1);
            StringBuilder skuParams = new StringBuilder();
            for (String s : split) {
                skuParams.append("'").append(s).append("',");
            }
            skuParams.deleteCharAt(skuParams.length() - 1);
            result += model.getAndOr() + " EXISTS" + model.getLeftBracket() +
                "SELECT 1 FROM wm_so_detail d WHERE d.so_no = wsh.so_no AND d.org_id = wsh.org_id AND d.sku_code IN(" + skuParams.toString() + "))";
        } else {
            result += model.getAndOr() + " " + model.getLeftBracket() + "wsh." + model.getOrderAttCode() + " " + model.getOperator() + " '" + valses + "' " + model.getRightBracket() + " \n";
        }
        return result;
    }

    @Transactional
    public void deleteEntity(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            StringBuilder sql = new StringBuilder();
            SysWmsRuleWvDetailOrder wvDetailOrder = this.get(ids.get(0));
            for (String id : ids) {
                this.delete(new SysWmsRuleWvDetailOrder(id));
            }
            if (null != wvDetailOrder) {
                SysWmsRuleWvDetail sysWmsRuleWvDetail = sysWmsRuleWvDetailService.get(wvDetailOrder.getHeaderId());
                List<SysWmsRuleWvDetailOrder> orderList = this.getByRuleCodeAndLineNo(sysWmsRuleWvDetail.getRuleCode(), sysWmsRuleWvDetail.getLineNo(), sysWmsRuleWvDetail.getDataSet());
                for (SysWmsRuleWvDetailOrder order : orderList) {
                    if (!ids.contains(order.getId())) {
                        sql.append(createSql(wvDetailOrder));
                    }
                }
                sysWmsRuleWvDetailService.updateSQL(wvDetailOrder.getHeaderId(), sql.toString());
            }
        }
    }

    public List<SysWmsRuleWvDetailOrder> getByRuleCodeAndLineNo(String ruleCode, String lineNo, String dataSet) {
        SysWmsRuleWvDetailOrder example = new SysWmsRuleWvDetailOrder();
        example.setRuleCode(ruleCode);
        example.setLineNo(lineNo);
        example.setDataSet(dataSet);
        return this.findList(example);
    }

    public List<SysWmsRuleWvDetailOrder> findByHeaderId(String headerId, String dataSet) {
        return findList(new SysWmsRuleWvDetailOrder(null, headerId, dataSet));
    }
}