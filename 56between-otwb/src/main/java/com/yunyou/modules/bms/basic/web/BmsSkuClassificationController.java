package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsSkuClassification;
import com.yunyou.modules.bms.basic.service.BmsSkuClassificationService;
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
@RequestMapping(value = "${adminPath}/bms/basic/skuClassification")
public class BmsSkuClassificationController extends BaseController {
    @Autowired
    private BmsSkuClassificationService bmsSkuClassificationService;

    /**
     * 列表页面
     */
    @RequiresPermissions("bms:basic:skuClassification:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsSkuClassification", new BmsSkuClassification());
        return "modules/bms/basic/bmsSkuClassificationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:basic:skuClassification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSkuClassificationService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"bms:basic:skuClassification:view", "bms:basic:skuClassification:add", "bms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsSkuClassification entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsSkuClassificationService.get(entity.getId());
        }
        model.addAttribute("bmsSkuClassification", entity);
        return "modules/bms/basic/bmsSkuClassificationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:basic:skuClassification:add", "bms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid BmsSkuClassification entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsSkuClassificationService.save(entity);
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
    @RequiresPermissions("bms:basic:skuClassification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsSkuClassificationService.delete(new BmsSkuClassification(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsSkuClassificationService.findGrid(new Page<>(request, response), entity));
    }
}
