package com.yunyou.modules.wms.inventory.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmAdSerialEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmAdSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 序列号调整Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmAdSerial")
public class BanQinWmAdSerialController extends BaseController {
    @Autowired
    private BanQinWmAdSerialService banQinWmAdSerialService;

    /**
     * 序列号调整列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmAdSerialEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmAdSerialEntity> page = banQinWmAdSerialService.findPage(new Page<BanQinWmAdSerialEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}