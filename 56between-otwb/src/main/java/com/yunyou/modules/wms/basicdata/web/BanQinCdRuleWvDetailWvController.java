package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvDetailWv;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvDetailWvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 波次规则波次限制Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvDetailWv")
public class BanQinCdRuleWvDetailWvController extends BaseController {

    @Autowired
    private BanQinCdRuleWvDetailWvService banQinCdRuleWvDetailWvService;

    /**
     * 波次规则波次列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<BanQinCdRuleWvDetailWv> data(BanQinCdRuleWvDetailWv banQinCdRuleWvDetailWv, HttpServletRequest request, HttpServletResponse response, Model model) {
        return banQinCdRuleWvDetailWvService.findList(banQinCdRuleWvDetailWv);
    }

    /**
     * 保存波次规则波次限制
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdRuleWvDetail banQinCdRuleWvDetail, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            banQinCdRuleWvDetailWvService.saveEntity(banQinCdRuleWvDetail.getRuleWvDetailWvList());
            j.setMsg("保存成功！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除波次规则波次限制
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdRuleWvDetailWvService.delete(new BanQinCdRuleWvDetailWv(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}