package com.yunyou.modules.bms.report.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.report.entity.BmsNationwideWarehouseList;
import com.yunyou.modules.bms.report.service.BmsNationwideWarehouseListService;
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
 * 全国仓网清单Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/report/nationwideWarehouseList")
public class BmsNationwideWarehouseListController extends BaseController {
    @Autowired
    private BmsNationwideWarehouseListService bmsNationwideWarehouseListService;

    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("bmsNationwideWarehouseList", new BmsNationwideWarehouseList());
        return "modules/bms/report/bmsNationwideWarehouseList";
    }

    @ResponseBody
    @RequestMapping("data")
    public Map<String, Object> data(BmsNationwideWarehouseList bmsNationwideWarehouseList, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsNationwideWarehouseListService.findPage(new Page<>(request, response), bmsNationwideWarehouseList));
    }

    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsNationwideWarehouseList bmsNationwideWarehouseList, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            Page<BmsNationwideWarehouseList> page = bmsNationwideWarehouseListService.findPage(new Page<>(request, response, -1), bmsNationwideWarehouseList);
            new ExportExcel(null, BmsNationwideWarehouseList.class).setDataList(page.getList()).write(response, "全国仓网清单.xlsx").dispose();
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
