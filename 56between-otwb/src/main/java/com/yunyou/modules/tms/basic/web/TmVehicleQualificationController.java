package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmVehicleQualification;
import com.yunyou.modules.tms.basic.service.TmVehicleQualificationService;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
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
 * 车辆资质信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmVehicleQualification")
public class TmVehicleQualificationController extends BaseController {

    @Autowired
    private TmVehicleQualificationService tmVehicleQualificationService;

    @ModelAttribute
    public TmVehicleQualification get(@RequestParam(required = false) String id) {
        TmVehicleQualification entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmVehicleQualificationService.get(id);
        }
        if (entity == null) {
            entity = new TmVehicleQualification();
        }
        return entity;
    }

    /**
     * 车辆资质信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleQualification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmVehicleQualification tmVehicleQualification, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmVehicleQualification> page = tmVehicleQualificationService.findPage(new Page<>(request, response), tmVehicleQualification);
        return getBootstrapData(page);
    }

    /**
     * 保存车辆资质信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmVehicleQualification:add", "basic:tmVehicleQualification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmVehicleQualification tmVehicleQualification, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmVehicleQualification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmVehicleQualificationService.saveValidator(tmVehicleQualification);
            tmVehicleQualificationService.save(tmVehicleQualification);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除车辆资质信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleQualification:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmVehicleQualification tmVehicleQualification, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        tmVehicleQualificationService.delete(tmVehicleQualification);
        j.setMsg("删除车辆资质信息成功");
        return j;
    }

    /**
     * 批量删除车辆资质信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehicleQualification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmVehicleQualificationService.delete(tmVehicleQualificationService.get(id));
        }
        j.setMsg("删除车辆资质信息成功");
        return j;
    }

}