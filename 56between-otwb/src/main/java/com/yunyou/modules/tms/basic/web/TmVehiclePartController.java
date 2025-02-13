package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmVehiclePart;
import com.yunyou.modules.tms.basic.service.TmVehiclePartService;
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
 * 车辆配件Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmVehiclePart")
public class TmVehiclePartController extends BaseController {

    @Autowired
    private TmVehiclePartService tmVehiclePartService;

    @ModelAttribute
    public TmVehiclePart get(@RequestParam(required = false) String id) {
        TmVehiclePart entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmVehiclePartService.get(id);
        }
        if (entity == null) {
            entity = new TmVehiclePart();
        }
        return entity;
    }

    /**
     * 车辆配件列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehiclePart:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmVehiclePart tmVehiclePart, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmVehiclePart> page = tmVehiclePartService.findPage(new Page<TmVehiclePart>(request, response), tmVehiclePart);
        return getBootstrapData(page);
    }

    /**
     * 保存车辆配件
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmVehiclePart:add", "basic:tmVehiclePart:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmVehiclePart tmVehiclePart, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmVehiclePart)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmVehiclePartService.saveValidator(tmVehiclePart);
            tmVehiclePartService.save(tmVehiclePart);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除车辆配件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehiclePart:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmVehiclePart tmVehiclePart, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        tmVehiclePartService.delete(tmVehiclePart);
        j.setMsg("删除车辆配件成功");
        return j;
    }

    /**
     * 批量删除车辆配件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmVehiclePart:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmVehiclePartService.delete(tmVehiclePartService.get(id));
        }
        j.setMsg("删除车辆配件成功");
        return j;
    }

}