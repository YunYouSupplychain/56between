package com.yunyou.modules.bms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.annotation.NotNull;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsBillTermsParameter;
import com.yunyou.modules.bms.basic.entity.BmsCustomer;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillTermsEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsContractDetailTermsParamsEntity;
import com.yunyou.modules.bms.basic.service.BmsBillTermsParameterService;
import com.yunyou.modules.bms.basic.service.BmsBillTermsService;
import com.yunyou.modules.bms.basic.service.BmsCustomerService;
import com.yunyou.modules.bms.common.BmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述：计费条款Controller
 *
 * @author liujianhua
 * @version 2019-05-27
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsBillTerms")
public class BmsBillTermsController extends BaseController {
    @Autowired
    private BmsBillTermsService bmsBillTermsService;
    @Autowired
    private BmsBillTermsParameterService bmsBillTermsParameterService;
    @Autowired
    private BmsCustomerService bmsCustomerService;

    /**
     * 描述：列表页面
     */
    @RequiresPermissions("bms:bmsBillTerms:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("bmsBillTermsEntity", new BmsBillTermsEntity());
        return "modules/bms/basic/bmsBillTermsList";
    }

    /**
     * 描述：列表数据
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillTerms:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsBillTermsEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillTermsService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 描述：查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"bms:bmsBillTerms:view", "bms:bmsBillTerms:add", "bms:bmsBillTerms:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsBillTermsEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsBillTermsService.getEntity(entity.getId());
        }
        model.addAttribute("bmsBillTermsEntity", entity);
        return "modules/bms/basic/bmsBillTermsForm";
    }

    /**
     * 描述：保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsBillTerms:add", "bms:bmsBillTerms:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsBillTermsEntity entity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            j.put("entity", bmsBillTermsService.saveEntity(entity));
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("已存在");
        }
        return j;
    }

    /**
     * 描述：批量删除
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillTerms:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsBillTermsService.delete(bmsBillTermsService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 描述：批量删除
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsBillTerms:del")
    @RequestMapping(value = "deleteParameter")
    public AjaxJson deleteParameter(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            bmsBillTermsParameterService.delete(bmsBillTermsParameterService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    /**
     * 描述：弹出窗数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BmsBillTermsEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsBillTermsService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 描述：复制
     */
    @RequiresPermissions("bms:bmsBillTerms:duplicate")
    @RequestMapping(value = "duplicate")
    public String duplicate(String id, Model model) {
        BmsBillTermsEntity entity = bmsBillTermsService.getEntity(id);
        entity.setId(null);
        entity.setBillTermsCode(null);
        for (BmsBillTermsParameter parameter : entity.getParameters()) {
            parameter.setId(null);
            parameter.setBillTermsCode(null);
        }
        model.addAttribute("bmsBillTermsEntity", entity);
        return "modules/bms/basic/bmsBillTermsForm";
    }

    @ResponseBody
    @RequestMapping(value = "getTermsParams")
    public List<BmsContractDetailTermsParamsEntity> getTermsParams(@RequestParam @NotNull String billTermsCode, @RequestParam @NotNull String settleObjectCode, @RequestParam @NotNull String orgId) {
        BmsCustomer bmsCustomer = bmsCustomerService.getByCode(settleObjectCode, orgId);
        return bmsBillTermsParameterService.getTermsParams(billTermsCode, settleObjectCode, bmsCustomer.getEbcuType());
    }
}