package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhQcItemDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhQcItemDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 质检项明细Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhQcItemDetail")
public class BanQinCdWhQcItemDetailController extends BaseController {
    @Autowired
    private BanQinCdWhQcItemDetailService banQinCdWhQcItemDetailService;
    
    /**
     * 质检项列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhQcItemDetail banQinCdWhQcItemDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhQcItemDetail> page = banQinCdWhQcItemDetailService.findPage(new Page<BanQinCdWhQcItemDetail>(request, response), banQinCdWhQcItemDetail);
        return getBootstrapData(page);
    }
    
    /**
     * 保存质检项
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhQcItemDetail:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhQcItemDetail banQinCdWhQcItemDetail, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhQcItemDetail)) {
            j.setSuccess(false);
            j.setMsg("非法数据!");
        }
        try {
            banQinCdWhQcItemDetailService.save(banQinCdWhQcItemDetail);
            j.setMsg("保存成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除质检项
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhQcItemDetail:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdWhQcItemDetailService.delete(new BanQinCdWhQcItemDetail(id));
        }
        j.setMsg("删除质检项成功");
        return j;
    }

}