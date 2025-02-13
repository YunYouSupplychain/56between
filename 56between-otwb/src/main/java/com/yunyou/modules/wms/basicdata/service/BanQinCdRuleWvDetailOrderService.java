package com.yunyou.modules.wms.basicdata.service;

import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.number.NumberUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetailOrder;
import com.yunyou.modules.wms.basicdata.mapper.BanQinCdRuleWvDetailOrderMapper;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次规则Service
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinCdRuleWvDetailOrderService extends CrudService<BanQinCdRuleWvDetailOrderMapper, BanQinCdRuleWvDetailOrder> {
	@Autowired
	@Lazy
	private BanQinCdRuleWvDetailService cdRuleWvDetailService;

	public BanQinCdRuleWvDetailOrder get(String id) {
		return super.get(id);
	}
	
	public List<BanQinCdRuleWvDetailOrder> findList(BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder) {
		return super.findList(banQinCdRuleWvDetailOrder);
	}
	
	public Page<BanQinCdRuleWvDetailOrder> findPage(Page<BanQinCdRuleWvDetailOrder> page, BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder) {
		return super.findPage(page, banQinCdRuleWvDetailOrder);
	}
	
	@Transactional
	public void save(BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder) {
		super.save(banQinCdRuleWvDetailOrder);
	}
	
	@Transactional
	public void delete(BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder) {
		super.delete(banQinCdRuleWvDetailOrder);
	}
	
	@Transactional
    public void deleteByHeaderId(String headerId) {
	    mapper.deleteByHeaderId(headerId);
    }
	
	public BanQinCdRuleWvDetailOrder findFirst(BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder) {
        List<BanQinCdRuleWvDetailOrder> list = this.findList(banQinCdRuleWvDetailOrder);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 根据订单限制代码获取订单限制
     * @param ruleCode
     * @param lineNo
     * @return
     */
    public List<BanQinCdRuleWvDetailOrder> getByRuleCodeAndLineNo(String ruleCode, String lineNo, String orgId) {
        BanQinCdRuleWvDetailOrder example = new BanQinCdRuleWvDetailOrder();
        example.setRuleCode(ruleCode);
        example.setLineNo(lineNo);
        example.setOrgId(orgId);
        return this.findList(example);
    }

	/**
	 * 批量保存波次订单限制
	 * @param list
	 * @return
	 */
	@Transactional
    public String saveEntity(List<BanQinCdRuleWvDetailOrder> list) {
		StringBuilder sql = new StringBuilder();
		if (CollectionUtil.isNotEmpty(list)) {
			for (BanQinCdRuleWvDetailOrder order : list) {
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
			cdRuleWvDetailService.updateSQL(list.get(0).getHeaderId(), sql.toString());
		}
		return sql.toString();
    }

	/**
	 * 创建订单限制的SQL语句
	 * @param model
	 * @return
	 */
	public String createSql(BanQinCdRuleWvDetailOrder model) {
		String result = "";
		String valses = model.getValue().replaceAll("\'", "").replaceAll("\"", "");
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
					"SELECT 1 FROM WM_SO_DETAIL d WHERE d.so_no = wsh.so_no AND d.org_id = wsh.org_id AND d.sku_code IN(" + skuParams.toString() + "))";
		} else {
			result += model.getAndOr() + " " + model.getLeftBracket() + "wsh." + model.getOrderAttCode() + " " + model.getOperator() + " '" + valses + "' " + model.getRightBracket() + " \n";
		}
		return result;
    }

	@Transactional
	public void updateLineNo(String headerId, String lineNo) {
		mapper.execUpdateSql("update cd_rule_wv_detail_order set line_no = '" + lineNo + "' where header_id = '" + headerId + "'");
	}

	public List<BanQinCdRuleWvDetailOrder> findAllDetail(String orgId) {
		BanQinCdRuleWvDetailOrder condition = new BanQinCdRuleWvDetailOrder();
		condition.setOrgId(orgId);
		return this.findList(condition);
	}

	@Transactional
	public void deleteEntity(List<String> ids) {
		if (CollectionUtil.isNotEmpty(ids)) {
			StringBuilder sql = new StringBuilder();
			BanQinCdRuleWvDetailOrder wvDetailOrder = this.get(ids.get(0));
			for (String id : ids) {
				this.delete(new BanQinCdRuleWvDetailOrder(id));
			}
			if (null != wvDetailOrder) {
				BanQinCdRuleWvDetail banQinCdRuleWvDetail = cdRuleWvDetailService.get(wvDetailOrder.getHeaderId());
				List<BanQinCdRuleWvDetailOrder> orderList = this.getByRuleCodeAndLineNo(banQinCdRuleWvDetail.getRuleCode(), banQinCdRuleWvDetail.getLineNo(), banQinCdRuleWvDetail.getOrgId());
				for (BanQinCdRuleWvDetailOrder order : orderList) {
					if (!ids.contains(order.getId())) {
						sql.append(createSql(wvDetailOrder));
					}
				}
				cdRuleWvDetailService.updateSQL(wvDetailOrder.getHeaderId(), sql.toString());
			}
		}
	}

	@Transactional
    public void remove(String ruleCode, String orgId) {
		mapper.remove(ruleCode, orgId);
    }
}