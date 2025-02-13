package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.CdWhSkuClassification;
import com.yunyou.modules.wms.basicdata.service.CdWhSkuClassificationService;
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
@RequestMapping(value = "${adminPath}/wms/basic/skuClassification")
public class CdWhSkuClassificationController extends BaseController {
    @Autowired
    private CdWhSkuClassificationService cdWhSkuClassificationService;

    /**
     * 列表页面
     */
    @RequiresPermissions("wms:basic:skuClassification:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("cdWhSkuClassification", new CdWhSkuClassification());
        return "modules/wms/basicdata/cdWhSkuClassificationList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("wms:basic:skuClassification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CdWhSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(cdWhSkuClassificationService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"wms:basic:skuClassification:view", "wms:basic:skuClassification:add", "wms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(CdWhSkuClassification entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = cdWhSkuClassificationService.get(entity.getId());
        }
        model.addAttribute("cdWhSkuClassification", entity);
        return "modules/wms/basicdata/cdWhSkuClassificationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"wms:basic:skuClassification:add", "wms:basic:skuClassification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid CdWhSkuClassification cdWhSkuClassification, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, cdWhSkuClassification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            cdWhSkuClassificationService.save(cdWhSkuClassification);
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
    @RequiresPermissions("wms:basic:skuClassification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            cdWhSkuClassificationService.delete(new CdWhSkuClassification(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(CdWhSkuClassification entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(cdWhSkuClassificationService.findGrid(new Page<>(request, response), entity));
    }
}
