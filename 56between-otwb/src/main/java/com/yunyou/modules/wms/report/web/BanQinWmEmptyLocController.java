package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLoc;
import com.yunyou.modules.wms.report.service.BanQinWmEmptyLocService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 空库位报表Controller
 *
 * @author ZYF
 * @version 2020-09-21
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmEmptyLoc")
public class BanQinWmEmptyLocController extends BaseController {
    @Autowired
    private BanQinWmEmptyLocService wmEmptyLocService;

    @ModelAttribute
    public BanQinCdWhLoc get(@RequestParam(required = false) String id) {
        return new BanQinCdWhLoc();
    }

    @RequiresPermissions("report:banQinWmEmptyLoc:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmEmptyLocList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmEmptyLoc:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhLoc entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhLoc> page = wmEmptyLocService.findPage(new Page<BanQinCdWhLoc>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmEmptyLoc:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(BanQinCdWhLoc entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "空库位记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinCdWhLoc> page = wmEmptyLocService.findPage(new Page<BanQinCdWhLoc>(request, response, -1), entity);
            new ExportExcel("", BanQinCdWhLoc.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出空库位记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}