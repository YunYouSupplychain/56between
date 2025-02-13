package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.kit.entity.BanQinCdWhBomDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinCdWhBomDetailEntity;
import com.yunyou.modules.wms.kit.service.BanQinCdWhBomDetailService;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import org.apache.shiro.authz.annotation.Logical;
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
 * 组合件明细Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinCdWhBomDetail")
public class BanQinCdWhBomDetailController extends BaseController {
    @Autowired
    private BanQinCdWhBomDetailService banQinCdWhBomDetailService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * 组合件明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhBomDetail banQinCdWhBomDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhBomDetailEntity> page = banQinCdWhBomDetailService.findPage(new Page<BanQinCdWhBomDetailEntity>(request, response), banQinCdWhBomDetail);
        return getBootstrapData(page);
    }

    /**
     * 保存组合件明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinCdWhBomHeader:add", "kit:banQinCdWhBomHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhBomDetailEntity banQinCdWhBomDetailEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhBomDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            banQinKitService.saveBomDetailEntity(banQinCdWhBomDetailEntity);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除组合件明细
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinCdWhBomHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            banQinKitService.removeBomDetailEntity(ids.split(","));
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：子件弹出窗数据
     * <p>
     * create by Jianhua on 2019/8/19
     */
    @ResponseBody
    @RequestMapping(value = "subSkuGrid")
    public Map<String, Object> subSkuGrid(BanQinCdWhBomDetailEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhBomDetailEntity> page = banQinCdWhBomDetailService.findSubSkuList(new Page<BanQinCdWhBomDetailEntity>(request, response), entity);
        return getBootstrapData(page);
    }

}