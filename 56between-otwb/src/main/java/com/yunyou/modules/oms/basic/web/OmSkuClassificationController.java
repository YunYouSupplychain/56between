package com.yunyou.modules.oms.basic.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.oms.basic.entity.OmSkuClassification;
import com.yunyou.modules.oms.basic.service.OmSkuClassificationService;
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
import javax.validation.Valid;
import java.util.Map;

/**
 * 商品分类Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/basic/skuClassification")
public class OmSkuClassificationController extends BaseController {
    @Autowired
    private OmSkuClassificationService omSkuClassificationService;

    /**
     * 列表页面
     */
    @RequiresPermissions("oms:basic:skuClassification:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("omSkuClassification", new OmSkuClassification());
        return "modules/oms/basic/omSkuClassificationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("oms:basic:skuClassification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omSkuClassificationService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"oms:basic:skuClassification:view", "oms:basic:skuClassification:add", "oms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmSkuClassification entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = omSkuClassificationService.get(entity.getId());
        }
        model.addAttribute("omSkuClassification", entity);
        return "modules/oms/basic/omSkuClassificationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"oms:basic:skuClassification:add", "oms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid OmSkuClassification omSkuClassification, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omSkuClassification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            omSkuClassificationService.save(omSkuClassification);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            logger.error(null, e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("oms:basic:skuClassification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            omSkuClassificationService.delete(new OmSkuClassification(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(OmSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(omSkuClassificationService.findGrid(new Page<>(request, response), entity));
    }
}
