package com.yunyou.modules.wms.outbound.web;

import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoSerialEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 出库序列号Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSoSerial")
public class BanQinWmSoSerialController extends BaseController {
    @Autowired
    private BanQinWmSoSerialService banQinWmSoSerialService;

    /**
     * 出库序列号列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoSerialEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmSoSerialEntity> page = banQinWmSoSerialService.findPage(new Page<BanQinWmSoSerialEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}