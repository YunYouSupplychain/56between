package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmRepInvValidityEntity;
import com.yunyou.modules.wms.report.service.BanQinWmRepInvValidityService;
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
 * 库存效期报表Controller
 *
 * @author ZYF
 * @version 2021-04-02
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepInvValidity")
public class BanQinWmRepInvValidityController extends BaseController {
    @Autowired
    private BanQinWmRepInvValidityService wmRepInvValidityService;

    @ModelAttribute
    public WmRepInvValidityEntity get(@RequestParam(required = false) String id) {
        return new WmRepInvValidityEntity();
    }

    @RequiresPermissions("report:banQinWmRepInvValidity:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmRepInvValidityList";
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmRepInvValidity:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmRepInvValidityEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WmRepInvValidityEntity> page = wmRepInvValidityService.findPage(new Page<WmRepInvValidityEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepInvValidity:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WmRepInvValidityEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "库存效期报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WmRepInvValidityEntity> page = wmRepInvValidityService.findPage(new Page<WmRepInvValidityEntity>(request, response, -1), entity);
            new ExportExcel("", WmRepInvValidityEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出库存效期报表失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}