package com.yunyou.modules.bms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.BmsTransportPriceFileList;
import com.yunyou.modules.bms.report.service.BmsTransportPriceFileListService;
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
 * 运输价格档案清单Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/transportPriceFileList")
public class BmsTransportPriceFileListController extends BaseController {
    @Autowired
    private BmsTransportPriceFileListService bmsTransportPriceFileListService;

    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsTransportPriceFileList", new BmsTransportPriceFileList());
        return "modules/bms/report/bmsTransportPriceFileList";
    }

    @ResponseBody
    @RequestMapping("data")
    public Map<String, Object> data(BmsTransportPriceFileList bmsTransportPriceFileList, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsTransportPriceFileListService.findPage(new Page<>(request, response), bmsTransportPriceFileList));
    }

    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsTransportPriceFileList bmsTransportPriceFileList, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsTransportPriceFileList> page = bmsTransportPriceFileListService.findPage(new Page<>(request, response, -1), bmsTransportPriceFileList);
            new ExportExcel(null, BmsTransportPriceFileList.class).setDataList(page.getList()).write(response, "运输价格档案清单.xlsx").dispose();
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
