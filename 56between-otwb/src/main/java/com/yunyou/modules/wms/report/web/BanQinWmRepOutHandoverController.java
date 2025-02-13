package com.yunyou.modules.wms.report.web;

import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPack;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoPackEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoPackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 描述：出库交接清单
 *
 * @author Jianhua on 2020-1-12
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepOutHandover")
public class BanQinWmRepOutHandoverController extends BaseController {
    @Autowired
    private BanQinWmSoPackService banQinWmSoPackService;

    @RequiresPermissions("report:banQinWmRepOutHandover:list")
    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("banQinWmSoPackEntity", new BanQinWmSoPackEntity());
        return "modules/wms/report/banQinWmRepOutHandoverList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutHandover:list")
    @RequestMapping("data")
    public Map<String, Object> data(BanQinWmSoPackEntity query, HttpServletRequest request, HttpServletResponse response) {
        Page<BanQinWmSoPackEntity> page = banQinWmSoPackService.findOutHandoverPage(new Page<>(request, response), query);
        return getBootstrapData(page);
    }

    @RequiresPermissions("report:banQinWmRepOutHandover:export")
    @RequestMapping("export")
    public void export(BanQinWmSoPackEntity query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Page<BanQinWmSoPackEntity> page = banQinWmSoPackService.findOutHandoverPage(new Page<>(request, response, -1), query);
        new ExportExcel(null, BanQinWmSoPack.class).setDataList(page.getList()).write(response, "出库交接清单.xlsx").dispose();
    }

}
