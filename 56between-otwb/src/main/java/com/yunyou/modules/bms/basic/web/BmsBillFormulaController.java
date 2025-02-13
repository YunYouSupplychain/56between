package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsBillFormula;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillFormulaEntity;
import com.yunyou.modules.bms.basic.service.BmsBillFormulaService;
import com.yunyou.modules.bms.common.BmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 计费公式Controller
 *
 * @author Jianhua Liu
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsBillFormula")
public class BmsBillFormulaController extends BaseController {
    @Autowired
    private BmsBillFormulaService bmsBillFormulaService;

    /**
     * 计费公式列表页面
     */
    @RequiresPermissions("bms:bmsBillFormula:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsBillFormulaEntity", new BmsBillFormulaEntity());
        return "modules/bms/basic/bmsBillFormulaList";
    }

    /**
     * 计费公式列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillFormula:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillFormulaEntity bmsBillFormulaEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillFormulaService.findPage(new Page<>(request, response), bmsBillFormulaEntity));
    }

    /**
     * 查看，增加，编辑计费公式表单页面
     */
    @RequiresPermissions(value = {"bms:bmsBillFormula:view", "bms:bmsBillFormula:add", "bms:bmsBillFormula:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsBillFormulaEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsBillFormulaService.getEntity(entity.getId());
        }
        model.addAttribute("bmsBillFormulaEntity", entity);
        return "modules/bms/basic/bmsBillFormulaForm";
    }

    /**
     * 保存计费公式
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsBillFormula:add", "bms:bmsBillFormula:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsBillFormulaEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsBillFormulaService.saveEntity(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("计费公式已存在");
        }
        return j;
    }

    /**
     * 批量删除计费公式
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillFormula:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsBillFormulaService.delete(bmsBillFormulaService.get(id));
        }
        j.setMsg("删除计费公式成功");
        return j;
    }

    /**
     * 弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsBillFormula bmsBillFormula, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillFormulaService.findPage(new Page<>(request, response), bmsBillFormula));
    }

}