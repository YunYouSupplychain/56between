package com.yunyou.modules.bms.report.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.BmsCarrierRoutePrice;
import com.yunyou.modules.bms.report.service.BmsCarrierRoutePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 承运商路由价格Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/carrierRoutePrice")
public class BmsCarrierRoutePriceController extends BaseController {
    @Autowired
    private BmsCarrierRoutePriceService bmsCarrierRoutePriceService;

    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsCarrierRoutePrice", new BmsCarrierRoutePrice());
        return "modules/bms/report/bmsCarrierRoutePriceList";
    }

    @ResponseBody
    @RequestMapping("data")
    public Map<String, Object> data(BmsCarrierRoutePrice bmsCarrierRoutePrice, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsCarrierRoutePriceService.findPage(new Page<>(request, response), bmsCarrierRoutePrice));
    }
}
