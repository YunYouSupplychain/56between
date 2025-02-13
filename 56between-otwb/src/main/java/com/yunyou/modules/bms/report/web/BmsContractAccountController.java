package com.yunyou.modules.bms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.BmsContractAccount;
import com.yunyou.modules.bms.report.service.BmsContractAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 合同台账Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/contractAccount")
public class BmsContractAccountController extends BaseController {
    @Autowired
    private BmsContractAccountService bmsContractAccountService;

    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsContractAccount", new BmsContractAccount());
        return "modules/bms/report/bmsContractAccountList";
    }

    @ResponseBody
    @RequestMapping("data")
    public Map<String, Object> data(BmsContractAccount bmsContractAccount, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsContractAccountService.findPage(new Page<>(request, response), bmsContractAccount));
    }

    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsContractAccount bmsContractAccount, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsContractAccount> page = bmsContractAccountService.findPage(new Page<>(request, response, -1), bmsContractAccount);
            new ExportExcel(null, BmsContractAccount.class).setDataList(page.getList()).write(response, "合同台账.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}
