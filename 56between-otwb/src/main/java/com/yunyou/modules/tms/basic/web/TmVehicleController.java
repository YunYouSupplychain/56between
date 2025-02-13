package com.yunyou.modules.tms.basic.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmVehicle;
import com.yunyou.modules.tms.basic.entity.extend.TmVehicleEntity;
import com.yunyou.modules.tms.basic.service.TmVehicleService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;

/**
 * 车辆信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmVehicle")
public class TmVehicleController extends BaseController {

    @Autowired
    private TmVehicleService tmVehicleService;

    @ModelAttribute
    public TmVehicleEntity get(@RequestParam(required = false) String id) {
        TmVehicleEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmVehicleService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmVehicleEntity();
        }
        return entity;
    }

    /**
     * 车辆信息列表页面
     */
    @RequiresPermissions("basic:tmVehicle:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmVehicleList";
    }

    /**
     * 车辆信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicle:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmVehicle tmVehicle, HttpServletRequest request, HttpServletResponse response, Model model) {
        return getBootstrapData(tmVehicleService.findPage(new Page<TmVehicleEntity>(request, response), tmVehicle));
    }

    /**
     * 查看，增加，编辑车辆信息表单页面
     */
    @RequiresPermissions(value = {"basic:tmVehicle:view", "basic:tmVehicle:add", "basic:tmVehicle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmVehicleEntity tmVehicleEntity, Model model) {
        model.addAttribute("tmVehicleEntity", tmVehicleEntity);
        return "modules/tms/basic/tmVehicleForm";
    }

    /**
     * 保存车辆信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmVehicle:add", "basic:tmVehicle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmVehicle tmVehicle, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmVehicle)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmVehicleService.saveValidator(tmVehicle);
            tmVehicleService.save(tmVehicle);
            j.put("entity", tmVehicle);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除车辆信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicle:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmVehicle tmVehicle, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        try {
            tmVehicleService.delete(tmVehicle);
        } catch (Exception e) {
            logger.error("删除车辆信息id=[" + tmVehicle.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除车辆信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicle:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmVehicle tmVehicle = tmVehicleService.get(id);
            try {
                tmVehicleService.delete(tmVehicle);
            } catch (Exception e) {
                logger.error("删除车辆信息id=[" + id + "]", e);
                errMsg.append("<br>").append("车辆[").append(tmVehicle.getCarNo()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicle:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmVehicle tmVehicle, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "车辆信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmVehicleEntity> page = tmVehicleService.findPage(new Page<TmVehicleEntity>(request, response, -1), tmVehicle);
            new ExportExcel("车辆信息", TmVehicle.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出车辆信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmVehicle:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmVehicle> list = ei.getDataList(TmVehicle.class);
            for (TmVehicle tmVehicle : list) {
                try {
                    tmVehicleService.save(tmVehicle);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条车辆信息记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条车辆信息记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入车辆信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmVehicle/?repage";
    }

    /**
     * 下载导入车辆信息数据模板
     */
    @RequiresPermissions("basic:tmVehicle:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "车辆信息数据导入模板.xlsx";
            List<TmVehicle> list = Lists.newArrayList();
            new ExportExcel("车辆信息数据", TmVehicle.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmVehicle/?repage";
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions(value = "basic:tmVehicle:enable")
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmVehicle tmVehicle = tmVehicleService.get(id);
            try {
                tmVehicle.setDelFlag("0".equals(flag) ? BaseEntity.DEL_FLAG_NORMAL : BaseEntity.DEL_FLAG_DELETE);
                tmVehicle.setUpdateBy(UserUtils.getUser());
                tmVehicle.setUpdateDate(new Date());
                tmVehicleService.save(tmVehicle);
            } catch (Exception e) {
                logger.error("启用/停用车辆id=[" + id + "]", e);
                errMsg.append("<br>").append("车牌号[").append(tmVehicle.getCarNo()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 可用/不可用
     */
    @ResponseBody
    @RequiresPermissions(value = "basic:tmVehicle:updateStatus")
    @RequestMapping(value = "updateStatus")
    public AjaxJson updateStatus(@RequestParam String ids, @RequestParam String flag) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmVehicle tmVehicle = tmVehicleService.get(id);
            try {
                tmVehicle.setStatus("0".equals(flag) ? TmsConstants.VEHICLE_STATUS_00 : TmsConstants.VEHICLE_STATUS_01);
                tmVehicle.setUpdateBy(UserUtils.getUser());
                tmVehicle.setUpdateDate(new Date());
                tmVehicleService.save(tmVehicle);
            } catch (Exception e) {
                logger.error("可用/不可用车辆id=[" + id + "]", e);
                errMsg.append("<br>").append("车牌号[").append(tmVehicle.getCarNo()).append("]操作失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 车辆信息Grid列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(TmVehicleEntity qEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isNotBlank(qEntity.getOpOrgId())) {
            qEntity.setDispatchCenterId(UserUtils.getDispatchCenter(qEntity.getOpOrgId()).getId());
        }
        return getBootstrapData(tmVehicleService.findGrid(new Page<TmVehicleEntity>(request, response), qEntity));
    }

}