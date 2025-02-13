package com.yunyou.modules.bms.finance.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.common.BmsConstants;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.bms.finance.entity.BmsBill;
import com.yunyou.modules.bms.finance.entity.BmsBillDetail;
import com.yunyou.modules.bms.finance.entity.BmsBillStatistics;
import com.yunyou.modules.bms.finance.service.BmsBillDetailService;
import com.yunyou.modules.bms.finance.service.BmsBillService;
import com.yunyou.modules.bms.finance.service.BmsBillStatisticsService;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 费用账单Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/finance/bmsBill")
public class BmsBillController extends BaseController {
    @Autowired
    private BmsBillService bmsBillService;
    @Autowired
    private BmsBillStatisticsService bmsBillStatisticsService;
    @Autowired
    private BmsBillDetailService bmsBillDetailService;

    @ModelAttribute
    public BmsBill get(@RequestParam(required = false) String id) {
        BmsBill entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsBillService.get(id);
        }
        if (entity == null) {
            entity = new BmsBill();
        }
        return entity;
    }

    /**
     * 费用账单列表页面
     */
    @RequiresPermissions("bms:bmsBill:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/finance/bmsBillList";
    }

    /**
     * 费用账单列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBill bmsBill, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillService.findPage(new Page<>(request, response), bmsBill));
    }

    /**
     * 查看，增加，编辑费用账单表单页面
     */
    @RequiresPermissions(value = {"bms:bmsBill:view", "bms:bmsBill:add", "bms:bmsBill:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsBill bmsBill, Model model) {
        model.addAttribute("bmsBill", bmsBill);
        return "modules/bms/finance/bmsBillForm";
    }

    /**
     * 保存费用账单
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsBill:add", "bms:bmsBill:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsBill bmsBill, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, bmsBill)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsBillService.save(bmsBill);
            j.put("entity", bmsBill);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
            return j;
        }
        j.setSuccess(true);
        j.setMsg("保存费用账单成功");
        return j;
    }

    /**
     * 批量删除费用账单
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        if (StringUtils.isBlank(ids)) {
            return j;
        }
        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            BmsBill bmsBill = bmsBillService.get(id);
            try {
                bmsBillService.delete(bmsBill);
            } catch (BmsException e) {
                errMsg.append("账单[").append(bmsBill.getConfirmNo()).append("]删除失败，").append(e.getMessage()).append("<br>");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    /**
     * 添加费用明细
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:addBillDetail")
    @RequestMapping(value = "addBillDetail")
    public AjaxJson addBillDetail(@RequestParam String confirmNo, @RequestParam String orgId, @RequestParam String ids) {
        AjaxJson j = new AjaxJson();

        if (StringUtils.isBlank(ids)) {
            return j;
        }

        List<BmsBillDetail> addBillDetails = Lists.newArrayList();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            BmsBillDetail bmsBillDetail = bmsBillDetailService.get(id);
            if (!BmsConstants.BILL_STATUS_01.equals(bmsBillDetail.getStatus())) {
                continue;
            }
            addBillDetails.add(bmsBillDetail);
        }
        try {
            BmsBill bmsBill = bmsBillService.addBillDetail(confirmNo, orgId, addBillDetails);
            j.put("entity", bmsBill);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 移除费用明细
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:removeBillDetail")
    @RequestMapping(value = "removeBillDetail")
    public AjaxJson removeBillDetail(@RequestParam String confirmNo, @RequestParam String orgId, @RequestParam String ids) {
        AjaxJson j = new AjaxJson();

        if (StringUtils.isBlank(ids)) {
            return j;
        }

        List<BmsBillDetail> removeBillDetails = Lists.newArrayList();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            BmsBillDetail bmsBillDetail = bmsBillDetailService.get(id);
            if (!BmsConstants.BILL_STATUS_02.equals(bmsBillDetail.getStatus())) {
                continue;
            }
            removeBillDetails.add(bmsBillDetail);
        }
        try {
            BmsBill bmsBill = bmsBillService.removeBillDetail(confirmNo, orgId, removeBillDetails);
            j.put("entity", bmsBill);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsBill bmsBill, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            BmsBillDetail qDetail = new BmsBillDetail();
            qDetail.setConfirmNo(bmsBill.getConfirmNo());
            qDetail.setOrgId(bmsBill.getOrgId());
            List<BmsBillDetail> list = bmsBillDetailService.findList(qDetail);
            new ExportExcel(null, BmsBillDetail.class).setDataList(list).write(response, "账单费用明细.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出费用账单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBill:export")
    @RequestMapping(value = "export1", method = RequestMethod.POST)
    public AjaxJson exportFile1(@RequestBody BmsBill bmsBill, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            BmsBillStatistics qDetail = new BmsBillStatistics();
            qDetail.setConfirmNo(bmsBill.getConfirmNo());
            qDetail.setOrgId(bmsBill.getOrgId());
            List<BmsBillStatistics> list = bmsBillStatisticsService.findList(qDetail);
            new ExportExcel(null, BmsBillStatistics.class).setDataList(list).write(response, "账单费用统计.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出费用账单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}