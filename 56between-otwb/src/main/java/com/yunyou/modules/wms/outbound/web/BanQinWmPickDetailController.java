package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundPickOrderService;
import com.yunyou.modules.wms.outbound.service.BanQinWmPickDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 波次单明细Controller
 * @author zyf
 * @version 2020-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmPickDetail")
public class BanQinWmPickDetailController extends BaseController {
	@Autowired
	private BanQinOutboundPickOrderService banQinOutboundPickOrderService;
	@Autowired
	private BanQinWmPickDetailService banQinWmPickDetailService;

}