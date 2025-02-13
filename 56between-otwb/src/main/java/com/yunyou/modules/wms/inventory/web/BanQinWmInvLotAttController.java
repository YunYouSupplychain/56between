package com.yunyou.modules.wms.inventory.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvLotAttEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmInvLotAttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 批次号库存表Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmInvLotAtt")
public class BanQinWmInvLotAttController extends BaseController {

	@Autowired
	private BanQinWmInvLotAttService banQinWmInvLotAttService;
	
    /**
	 * 批次号库存表列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(BanQinWmInvLotAttEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BanQinWmInvLotAttEntity> page = banQinWmInvLotAttService.findPage(new Page<BanQinWmInvLotAttEntity>(request, response), entity);
		return getBootstrapData(page);
	}

}