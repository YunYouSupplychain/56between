package com.yunyou.modules.bms.business.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.business.entity.BmsExceptionData;
import com.yunyou.modules.bms.business.entity.extend.BmsExceptionDataEntity;
import com.yunyou.modules.bms.business.service.BmsExceptionDataService;
import com.yunyou.modules.bms.interactive.BmsPullDataCondition;
import com.yunyou.modules.bms.interactive.service.BmsPullDataHandle;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 异常数据Controller
 *
 * @author Jianhua Liu
 * @version 2019-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/business/bmsExceptionData")
public class BmsExceptionDataController extends BaseController {
    @Autowired
    private BmsExceptionDataService bmsExceptionDataService;
    @Autowired
    private BmsPullDataHandle bmsPullDataHandle;

    /**
     * 异常数据列表页面
     */
    @RequiresPermissions("business:bmsExceptionData:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsExceptionDataEntity", new BmsExceptionDataEntity());
        return "modules/bms/business/bmsExceptionDataList";
    }

    /**
     * 异常数据列表数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsExceptionData:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsExceptionDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (entity.getOrderDateFm() != null) {
            entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
        }
        if (entity.getOrderDateTo() != null) {
            entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
        }
        if (entity.getDispatchDateFm() != null) {
            entity.setDispatchDateFm(DateUtil.beginOfDate(entity.getDispatchDateFm()));
        }
        if (entity.getDispatchDateTo() != null) {
            entity.setDispatchDateTo(DateUtil.endOfDate(entity.getDispatchDateTo()));
        }
        return getBootstrapData(bmsExceptionDataService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 批量删除异常数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsExceptionData:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsExceptionDataService.delete(bmsExceptionDataService.get(id));
        }
        j.setMsg("删除异常数据成功");
        return j;
    }

    /**
     * 描述：同步数据
     */
    @ResponseBody
    @RequestMapping("pullData")
    public AjaxJson pullData(@RequestBody BmsPullDataCondition condition) {
        AjaxJson j = new AjaxJson();
        condition.setDateType(BmsPullDataCondition.DATA_TYPE_EXCEPTION);
        condition.setFmDate(DateUtil.beginOfDate(condition.getFmDate()));
        condition.setToDate(DateUtil.endOfDate(condition.getToDate()));
        ResultMessage message = bmsPullDataHandle.dataHandle(condition);
        if (!message.isSuccess()) {
            j.setSuccess(false);
            j.setMsg(message.getMessage());
        }
        return j;
    }

    /**
     * 描述：不计费/加入计费
     */
    @ResponseBody
    @RequiresPermissions(value = {"business:bmsExceptionData:cancelFee", "business:bmsExceptionData:addFee"}, logical = Logical.OR)
    @RequestMapping("isFee")
    public AjaxJson isFee(@RequestParam String ids, @RequestParam String isFee) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                BmsExceptionData bmsExceptionData = bmsExceptionDataService.get(id);
                bmsExceptionData.setIsFee(isFee);
                bmsExceptionDataService.save(bmsExceptionData);
            }
        } catch (Exception e) {
            logger.error("加入/取消计费异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("business:bmsExceptionData:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsExceptionDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            if (entity.getOrderDateFm() != null) {
                entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
            }
            if (entity.getOrderDateTo() != null) {
                entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
            }
            if (entity.getDispatchDateFm() != null) {
                entity.setDispatchDateFm(DateUtil.beginOfDate(entity.getDispatchDateFm()));
            }
            if (entity.getDispatchDateTo() != null) {
                entity.setDispatchDateTo(DateUtil.endOfDate(entity.getDispatchDateTo()));
            }
            List<BmsExceptionData> list = bmsExceptionDataService.findPage(new Page<>(request, response, -1), entity).getList();
            new ExportExcel(null, BmsExceptionData.class).setDataList(list).write(response, "异常数据.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("business:bmsExceptionData:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            bmsExceptionDataService.importFile(new ImportExcel(file, 1, 0).getDataList(BmsExceptionData.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            logger.error("导入失败", e);
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsExceptionData/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("business:bmsExceptionData:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsExceptionData.class, 1).setDataList(Lists.newArrayList()).write(response, "异常数据模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsExceptionData/?repage";
    }
}