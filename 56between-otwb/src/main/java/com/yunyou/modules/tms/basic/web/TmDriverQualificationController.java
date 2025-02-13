package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmDriverQualification;
import com.yunyou.modules.tms.basic.service.TmDriverQualificationService;
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
 * 运输人员资质信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmDriverQualification")
public class TmDriverQualificationController extends BaseController {

    @Autowired
    private TmDriverQualificationService tmDriverQualificationService;

    @ModelAttribute
    public TmDriverQualification get(@RequestParam(required = false) String id) {
        TmDriverQualification entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmDriverQualificationService.get(id);
        }
        if (entity == null) {
            entity = new TmDriverQualification();
        }
        return entity;
    }

    /**
     * 运输人员资质信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriverQualification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmDriverQualification tmDriverQualification, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmDriverQualification> page = tmDriverQualificationService.findPage(new Page<TmDriverQualification>(request, response), tmDriverQualification);
        return getBootstrapData(page);
    }

    /**
     * 保存运输人员资质信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmDriverQualification:add", "basic:tmDriverQualification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmDriverQualification tmDriverQualification, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmDriverQualification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmDriverQualificationService.saveValidator(tmDriverQualification);
            tmDriverQualificationService.save(tmDriverQualification);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除运输人员资质信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriverQualification:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmDriverQualification tmDriverQualification, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        tmDriverQualificationService.delete(tmDriverQualification);
        j.setMsg("删除运输人员资质信息成功");
        return j;
    }

    /**
     * 批量删除运输人员资质信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmDriverQualification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            tmDriverQualificationService.delete(tmDriverQualificationService.get(id));
        }
        j.setMsg("删除运输人员资质信息成功");
        return j;
    }

}