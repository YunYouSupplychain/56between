package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 波次规则Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvDetail")
public class BanQinCdRuleWvDetailController extends BaseController {
    @Autowired
    private BanQinCdRuleWvDetailService banQinCdRuleWvDetailService;

    /**
     * 波次规则列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdRuleWvDetail banQinCdRuleWvDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdRuleWvDetail> page = banQinCdRuleWvDetailService.findPage(new Page<BanQinCdRuleWvDetail>(request, response), banQinCdRuleWvDetail);
        return getBootstrapData(page);
    }

    /**
     * 保存波次规则
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvDetail:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdRuleWvDetail banQinCdRuleWvDetail, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdRuleWvDetail)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            banQinCdRuleWvDetailService.saveEntity(banQinCdRuleWvDetail);
            j.setMsg("保存成功");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("行号不能重复!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除波次规则
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvDetail:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdRuleWvDetailService.delete(new BanQinCdRuleWvDetail(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}