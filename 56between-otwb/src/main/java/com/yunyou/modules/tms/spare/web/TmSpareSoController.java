package com.yunyou.modules.tms.spare.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.spare.action.TmSpareSoAction;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoDetailEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoEntity;
import com.yunyou.modules.tms.spare.entity.extend.TmSpareSoScanInfoEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/spare/so")
public class TmSpareSoController extends BaseController {
    @Autowired
    private TmSpareSoAction tmSpareSoAction;

    @ModelAttribute
    public TmSpareSoEntity get(String id) {
        TmSpareSoEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmSpareSoAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmSpareSoEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:spare:so:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/spare/tmSpareSoList";
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:so:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmSpareSoEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmSpareSoAction.findPage(new Page<>(request, response), qEntity));
    }

    @RequiresPermissions(value = {"tms:spare:so:add", "tms:spare:so:edit", "tms:spare:so:view"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmSpareSoEntity entity, Model model) {
        model.addAttribute("tmSpareSoEntity", entity);
        return "modules/tms/spare/tmSpareSoForm";
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:spare:so:add", "tms:spare:so:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmSpareSoEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSpareSoAction.saveHeader(entity);
        if (msg.isSuccess()) {
            j.put("entity", msg.getData());
        } else {
            j.setSuccess(false);
            j.setMsg(msg.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:so:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSpareSoAction.remove(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:so:list")
    @RequestMapping(value = "/detail/data")
    public List<TmSpareSoDetailEntity> detail(TmSpareSoDetailEntity qEntity) {
        return tmSpareSoAction.findList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:spare:so:detail:add", "tms:spare:so:detail:edit"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson saveDetail(TmSpareSoEntity entity) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSpareSoAction.saveDetail(entity.getTmSpareSoDetailList());
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:so:detail:del")
    @RequestMapping(value = "/detail/deleteAll")
    public AjaxJson deleteDetail(String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage msg = tmSpareSoAction.removeDetail(ids.split(","));
        j.setSuccess(msg.isSuccess());
        j.setMsg(msg.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:so:list")
    @RequestMapping(value = "/scan/data")
    public Map<String, Object> scanInfo(TmSpareSoScanInfoEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmSpareSoAction.findPage(new Page<>(request, response), qEntity));
    }
}
