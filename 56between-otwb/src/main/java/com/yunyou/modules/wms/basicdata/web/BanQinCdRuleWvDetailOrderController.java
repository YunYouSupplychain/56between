package com.yunyou.modules.wms.basicdata.web;

import com.google.common.collect.Maps;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetailOrder;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvDetailOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 波次规则订单限制Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvDetailOrder")
public class BanQinCdRuleWvDetailOrderController extends BaseController {

	@Autowired
	private BanQinCdRuleWvDetailOrderService banQinCdRuleWvDetailOrderService;
	
    /**
	 * 订单限制列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public List<BanQinCdRuleWvDetailOrder> data(BanQinCdRuleWvDetailOrder banQinCdRuleWvDetailOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
	    return banQinCdRuleWvDetailOrderService.findList(banQinCdRuleWvDetailOrder);
	}

	/**
	 * 保存波次规则
	 */
	@RequestMapping(value = "save")
    @ResponseBody
	public AjaxJson save(BanQinCdRuleWvDetail banQinCdRuleWvDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
        AjaxJson j = new AjaxJson();
        try {
			String result = banQinCdRuleWvDetailOrderService.saveEntity(banQinCdRuleWvDetail.getRuleWvDetailOrderList());
			j.setMsg("保存成功");
			LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
			map.put("sql", result);
			j.setBody(map);
        } catch (Exception e) {
        	j.setSuccess(false);
        	j.setMsg(e.getMessage());
		}

        return j;
	}
	
	/**
	 * 批量删除订单限制
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		banQinCdRuleWvDetailOrderService.deleteEntity(Arrays.asList(ids.split(",")));
		j.setMsg("删除成功");
		return j;
	}

}