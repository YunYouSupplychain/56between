package com.yunyou.modules.wms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmRepOutDailyQueryEntity;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoHeaderService;
import com.yunyou.modules.wms.report.entity.extend.BanQinWmRepOutDailyQueryExportEntity;
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
 * 出库日报表Controller
 *
 * @author WMJ
 * @version 2019-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepOutDailyQuery")
public class BanQinWmRepOutDailyQueryController extends BaseController {
    @Autowired
    private BanQinWmSoHeaderService banQinWmSoHeaderService;

    @ModelAttribute
    public BanQinWmRepOutDailyQueryEntity get(@RequestParam(required = false) String id) {
        return new BanQinWmRepOutDailyQueryEntity();
    }

    /**
     * 入库日报表列表页面
     */
    @RequiresPermissions("report:banQinWmRepOutDailyQuery:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmRepOutDailyQueryList";
    }

    /**
     * 出库日报表列表数据
     * @param entity
     * @param request
     * @param response
     * @param model
     * @return
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutDailyQuery:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmRepOutDailyQueryEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmRepOutDailyQueryEntity> page = banQinWmSoHeaderService.wmRepOutDailyQuery(new Page<BanQinWmRepOutDailyQueryEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutDailyQuery:list")
    @RequestMapping(value = "count")
    public BanQinWmRepOutDailyQueryEntity count(BanQinWmRepOutDailyQueryEntity entity, Model model) {
        return banQinWmSoHeaderService.countTotalQuery(entity);
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutDailyQuery:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(BanQinWmRepOutDailyQueryEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "出库日报表数据记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmRepOutDailyQueryEntity> page = banQinWmSoHeaderService.wmRepOutDailyQuery(new Page<BanQinWmRepOutDailyQueryEntity>(request, response, -1), entity);
            new ExportExcel("", BanQinWmRepOutDailyQueryExportEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出出库日报表数据记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}