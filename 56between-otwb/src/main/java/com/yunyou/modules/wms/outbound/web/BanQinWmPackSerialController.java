package com.yunyou.modules.wms.outbound.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackSerialEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmPackSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 打包序列号Controller
 *
 * @author WMJ
 * @version 2020-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmPackSerial")
public class BanQinWmPackSerialController extends BaseController {
    @Autowired
    private BanQinWmPackSerialService banQinWmPackSerialService;

    @ModelAttribute
    public BanQinWmPackSerialEntity init() {
        return new BanQinWmPackSerialEntity();
    }

    /**
     * 打包序列号列表页面
     */
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmPackSerialList";
    }

    /**
     * 打包序列号列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmPackSerialEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinWmPackSerialService.findPage(new Page<BanQinWmPackSerialEntity>(request, response), entity));
    }

}