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
import com.yunyou.modules.bms.business.entity.BmsDispatchData;
import com.yunyou.modules.bms.business.entity.extend.BmsDispatchDataEntity;
import com.yunyou.modules.bms.business.service.BmsDispatchDataService;
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
 * 派车配载数据Controller
 *
 * @author Jianhua Liu
 * @version 2019-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/business/bmsDispatchData")
public class BmsDispatchDataController extends BaseController {
    @Autowired
    private BmsDispatchDataService bmsDispatchDataService;
    @Autowired
    private BmsPullDataHandle bmsPullDataHandle;

    /**
     * 派车配载数据列表页面
     */
    @RequiresPermissions("business:bmsDispatchData:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsDispatchDataEntity", new BmsDispatchDataEntity());
        return "modules/bms/business/bmsDispatchDataList";
    }

    /**
     * 派车配载数据列表数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsDispatchData:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsDispatchDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (entity.getOrderDateFm() != null) {
            entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
        }
        if (entity.getOrderDateTo() != null) {
            entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
        }
        if (entity.getDispatchTimeFm() != null) {
            entity.setDispatchTimeFm(DateUtil.beginOfDate(entity.getDispatchTimeFm()));
        }
        if (entity.getDispatchTimeTo() != null) {
            entity.setDispatchTimeTo(DateUtil.endOfDate(entity.getDispatchTimeTo()));
        }
        return getBootstrapData(bmsDispatchDataService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 批量删除派车配载数据
     */
    @ResponseBody
    @RequiresPermissions("business:bmsDispatchData:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsDispatchDataService.delete(bmsDispatchDataService.get(id));
        }
        j.setMsg("删除派车配载数据成功");
        return j;
    }

    /**
     * 描述：同步数据
     */
    @ResponseBody
    @RequestMapping("pullData")
    public AjaxJson pullData(@RequestBody BmsPullDataCondition condition) {
        AjaxJson j = new AjaxJson();
        condition.setDateType(BmsPullDataCondition.DATA_TYPE_DISPATCH);
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
    @RequiresPermissions(value = {"business:bmsDispatchData:cancelFee", "business:bmsDispatchData:addFee"}, logical = Logical.OR)
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
                BmsDispatchData bmsDispatchData = bmsDispatchDataService.get(id);
                bmsDispatchData.setIsFee(isFee);
                bmsDispatchDataService.save(bmsDispatchData);
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
    @RequiresPermissions("business:bmsDispatchData:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsDispatchDataEntity entity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            if (entity.getOrderDateFm() != null) {
                entity.setOrderDateFm(DateUtil.beginOfDate(entity.getOrderDateFm()));
            }
            if (entity.getOrderDateTo() != null) {
                entity.setOrderDateTo(DateUtil.endOfDate(entity.getOrderDateTo()));
            }
            if (entity.getDispatchTimeFm() != null) {
                entity.setDispatchTimeFm(DateUtil.beginOfDate(entity.getDispatchTimeFm()));
            }
            if (entity.getDispatchTimeTo() != null) {
                entity.setDispatchTimeTo(DateUtil.endOfDate(entity.getDispatchTimeTo()));
            }
            List<BmsDispatchData> list = bmsDispatchDataService.findPage(new Page<>(request, response, -1), entity).getList();
            new ExportExcel(null, BmsDispatchData.class).setDataList(list).write(response, "派车配载数据.xlsx").dispose();
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
    @RequiresPermissions("business:bmsDispatchData:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, String orgId, RedirectAttributes redirectAttributes) {
        try {
            bmsDispatchDataService.importFile(new ImportExcel(file, 1, 0).getDataList(BmsDispatchData.class), orgId);
            addMessage(redirectAttributes, "导入成功");
        } catch (Exception e) {
            logger.error("导入失败", e);
            addMessage(redirectAttributes, "导入失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsDispatchData/?repage";
    }

    /**
     * 下载导入数据模板
     */
    @RequiresPermissions("business:bmsDispatchData:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            new ExportExcel(null, BmsDispatchData.class, 1).setDataList(Lists.newArrayList()).write(response, "派车配载数据模板.xlsx").dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bms/business/bmsDispatchData/?repage";
    }
}