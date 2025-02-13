package com.yunyou.modules.wms.inventory.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotLocEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotLocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 批次库位库存表Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvLotLoc")
public class BanQinWmInvLotLocController extends BaseController {
	@Autowired
	private BanQinWmInvLotLocService banQinWmInvLotLocService;

    /**
	 * 批次库位库存表列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvLotLocEntity banQinWmInvLotLocEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvLotLocEntity> page = banQinWmInvLotLocService.findPage(new Page<BanQinWmInvLotLocEntity>(request, response), banQinWmInvLotLocEntity); 
		return getBootstrapData(page);
	}

}