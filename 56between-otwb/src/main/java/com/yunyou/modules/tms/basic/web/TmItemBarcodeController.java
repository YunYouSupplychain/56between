package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmItemBarcode;
import com.yunyou.modules.tms.basic.service.TmItemBarcodeService;
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
 * 商品条码信息Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmItemBarcode")
public class TmItemBarcodeController extends BaseController {

    @Autowired
    private TmItemBarcodeService tmItemBarcodeService;

    @ModelAttribute
    public TmItemBarcode get(@RequestParam(required = false) String id) {
        TmItemBarcode entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmItemBarcodeService.get(id);
        }
        if (entity == null) {
            entity = new TmItemBarcode();
        }
        return entity;
    }

    /**
     * 商品条码信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItemBarcode:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmItemBarcode tmItemBarcode, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmItemBarcode> page = tmItemBarcodeService.findPage(new Page<>(request, response), tmItemBarcode);
        return getBootstrapData(page);
    }

    /**
     * 保存商品条码信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmItemBarcode:add", "basic:tmItemBarcode:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmItemBarcode tmItemBarcode, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmItemBarcode)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmItemBarcodeService.saveValidator(tmItemBarcode);
            tmItemBarcodeService.save(tmItemBarcode);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除商品条码信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItemBarcode:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmItemBarcode tmItemBarcode, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        tmItemBarcodeService.delete(tmItemBarcode);
        j.setMsg("删除商品条码信息成功");
        return j;
    }

    /**
     * 批量删除商品条码信息
     */
    @ResponseBody
    @RequiresPermissions("basic:tmItemBarcode:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmItemBarcodeService.delete(tmItemBarcodeService.get(id));
        }
        j.setMsg("删除商品条码信息成功");
        return j;
    }

}