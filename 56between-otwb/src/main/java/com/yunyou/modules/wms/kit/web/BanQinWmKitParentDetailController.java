package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitParentDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitParentDetailEntity;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import com.yunyou.modules.wms.kit.service.BanQinWmKitParentDetailService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加工单父件明细Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmKitParentDetail")
public class BanQinWmKitParentDetailController extends BaseController {
    @Autowired
    private BanQinWmKitParentDetailService banQinWmKitParentDetailService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * 加工单父件明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmKitParentDetailEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmKitParentDetailEntity> page = banQinWmKitParentDetailService.findPage(new Page<BanQinWmKitParentDetailEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 保存加工单父件明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinWmKitHeader:add", "kit:banQinWmKitHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody BanQinWmKitParentDetailEntity entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }

        LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
        try {
            BanQinWmKitParentDetailEntity wmKitParentDetailEntity = banQinKitService.saveKitParentEntity(entity);
            rsMap.put("entity", wmKitParentDetailEntity);

            j.setBody(rsMap);
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除加工单父件明细
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitParentDetail:del")
    @RequestMapping(value = "remove")
    public AjaxJson remove(BanQinWmKitParentDetail parentDetail) {
        AjaxJson j = new AjaxJson();
        try {
            banQinKitService.removeKitParentEntity(parentDetail.getKitNo(), parentDetail.getParentLineNo().split(","), parentDetail.getOrgId());
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：复制
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitParentDetail:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(String id) {
        AjaxJson j = new AjaxJson();
        try {
            banQinKitService.duplicateKitParentEntity(banQinWmKitParentDetailService.getEntity(id));
            j.setSuccess(true);
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：生成子件
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitParentDetail:genSub")
    @RequestMapping(value = "genSub")
    public AjaxJson genSub(BanQinWmKitParentDetail parentDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.generateSubs(parentDetail.getKitNo(), parentDetail.getParentLineNo().split(","), parentDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：取消生成子件
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitParentDetail:cancelGenSub")
    @RequestMapping(value = "cancelGenSub")
    public AjaxJson cancelGenSub(BanQinWmKitParentDetail parentDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.cancelGenerateSubs(parentDetail.getKitNo(), parentDetail.getParentLineNo().split(","), parentDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：取消订单行
     * <p>
     * create by Jianhua on 2019/8/26
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitParentDetail:cancelLine")
    @RequestMapping(value = "cancelLine")
    public AjaxJson cancelLine(BanQinWmKitParentDetail parentDetail) {
        AjaxJson j = new AjaxJson();
        try {
            banQinKitService.cancelKitParent(parentDetail.getKitNo(), parentDetail.getParentLineNo().split(","), parentDetail.getOrgId());
            j.setSuccess(true);
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}