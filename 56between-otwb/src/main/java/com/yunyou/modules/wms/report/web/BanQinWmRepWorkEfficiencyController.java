package com.yunyou.modules.wms.report.web;

import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyQuery;
import com.yunyou.modules.wms.report.entity.WmRepWorkEfficiencyReport;
import com.yunyou.modules.wms.report.service.BanQinWmRepWorkEfficiencyService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：作业效率统计报表
 *
 * @author Jianhua on 2020-1-7
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/wmRepWorkEfficiency")
public class BanQinWmRepWorkEfficiencyController extends BaseController {
    @Autowired
    private BanQinWmRepWorkEfficiencyService banQinWmRepWorkEfficiencyService;

    @ModelAttribute
    public WmRepWorkEfficiencyQuery get() {
        return new WmRepWorkEfficiencyQuery();
    }

    /**
     * 描述：作业效率 - 收货页面
     *
     * @author Jianhua on 2020-1-7
     */
    @RequiresPermissions("report:banQinWmRepWorkEfficiency:receive:list")
    @RequestMapping("receive/list")
    public String receiveList() {
        return "modules/wms/report/banQinWmRepReceiveEfficiencyList";
    }

    /**
     * 描述：作业效率 - 拣货页面
     *
     * @author Jianhua on 2020-1-7
     */
    @RequiresPermissions("report:banQinWmRepWorkEfficiency:pick:list")
    @RequestMapping("pick/list")
    public String pickList() {
        return "modules/wms/report/banQinWmRepPickEfficiencyList";
    }

    /**
     * 描述：作业效率 - 复核页面
     *
     * @author Jianhua on 2020-1-7
     */
    @RequiresPermissions("report:banQinWmRepWorkEfficiency:review:list")
    @RequestMapping("review/list")
    public String reviewList() {
        return "modules/wms/report/banQinWmRepReviewEfficiencyList";
    }

    /**
     * 描述：作业效率 - 数据
     *
     * @author Jianhua on 2020-1-7
     */
    @ResponseBody
    @RequiresPermissions(value = {"report:banQinWmRepWorkEfficiency:receive:list", "report:banQinWmRepWorkEfficiency:pick:list", "report:banQinWmRepWorkEfficiency:review:list"}, logical = Logical.OR)
    @RequestMapping("workEfficiency/data")
    public Map<String, Object> workEfficiencyData(WmRepWorkEfficiencyQuery query) {
        if (query.getDate() != null) {
            query.setFmDate(DateUtil.beginOfDate(query.getDate()));
            query.setToDate(DateUtil.endOfDate(query.getDate()));
        }
        List<WmRepWorkEfficiencyReport> list = banQinWmRepWorkEfficiencyService.findList(query);
        Page<WmRepWorkEfficiencyReport> page = new Page<>();
        page.setList(list);
        page.setCount(list.size());
        return getBootstrapData(page);
    }

    /**
     * 描述：作业效率 - 导出
     *
     * @author Jianhua on 2020-1-7
     */
    @ResponseBody
    @RequiresPermissions(value = {"report:banQinWmRepWorkEfficiency:receive:export", "report:banQinWmRepWorkEfficiency:pick:export", "report:banQinWmRepWorkEfficiency:review:export"}, logical = Logical.OR)
    @RequestMapping("workEfficiency/export")
    public void workEfficiencyExport(WmRepWorkEfficiencyQuery query, HttpServletResponse response) throws IOException {
        if (query.getDate() != null) {
            query.setFmDate(DateUtil.beginOfDate(query.getDate()));
            query.setToDate(DateUtil.endOfDate(query.getDate()));
        }
        List<WmRepWorkEfficiencyReport> list = banQinWmRepWorkEfficiencyService.findList(query);
        new ExportExcel(null, WmRepWorkEfficiencyReport.class).setDataList(list).write(response, "").dispose();
    }


}
