package com.yunyou.modules.bms.report.web;

import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectEntity;
import com.yunyou.modules.bms.report.entity.BmsReportContractSubjectQuery;
import com.yunyou.modules.bms.report.service.BmsReportContractSubjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：报表-合同科目
 *
 * @author Jianhua
 * @version 2019/10/15
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/contractSubject")
public class BmsReportContractSubjectController extends BaseController {
    @Autowired
    private BmsReportContractSubjectService bmsReportContractSubjectService;

    @RequiresPermissions(value = "bms:report:contractSubject:list")
    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsReportContractSubjectQuery", new BmsReportContractSubjectQuery());
        return "modules/bms/report/bmsReportContractSubjectList";
    }

    @ResponseBody
    @RequiresPermissions(value = "bms:report:contractSubject:list")
    @RequestMapping("data")
    public Map<String, Object> data(BmsReportContractSubjectQuery query, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsReportContractSubjectService.findPage(new Page<>(request, response), query));
    }

    @RequiresPermissions(value = "bms:report:contractSubject:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void exportExcel(@RequestBody BmsReportContractSubjectQuery query, HttpServletResponse response) {
        try {
            List<BmsReportContractSubjectEntity> rsList = bmsReportContractSubjectService.findList(query);
            new ExportExcel(null, BmsReportContractSubjectEntity.class).setDataList(rsList).write(response, "合同费用科目.xlsx").dispose();
        } catch (IOException e) {
            logger.error("合同费用科目导出Excel失败", e);
        }
    }
}
