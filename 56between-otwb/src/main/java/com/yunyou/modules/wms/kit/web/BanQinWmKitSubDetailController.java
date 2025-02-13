package com.yunyou.modules.wms.kit.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.kit.entity.BanQinWmKitSubDetail;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmKitSubDetailEntity;
import com.yunyou.modules.wms.kit.service.BanQinKitService;
import com.yunyou.modules.wms.kit.service.BanQinWmKitSubDetailService;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加工单子件明细Controller
 *
 * @author Jianhua Liu
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/kit/banQinWmKitSubDetail")
public class BanQinWmKitSubDetailController extends BaseController {
    @Autowired
    private BanQinWmKitSubDetailService banQinWmKitSubDetailService;
    @Autowired
    private BanQinKitService banQinKitService;

    /**
     * 加工单子件明细列表数据
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmKitSubDetailEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmKitSubDetailEntity> page = banQinWmKitSubDetailService.findPage(new Page<BanQinWmKitSubDetailEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 保存加工单子件明细
     */
    @ResponseBody
    @RequiresPermissions(value = {"kit:banQinWmKitHeader:add", "kit:banQinWmKitHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinWmKitSubDetailEntity entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        LinkedHashMap<String, Object> rsMap = new LinkedHashMap<>();
        try {
            BanQinWmKitSubDetailEntity wmKitSubDetailEntity = banQinKitService.saveKitSubEntity(entity);
            rsMap.put("entity", wmKitSubDetailEntity);

            j.setBody(rsMap);
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 分配
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitSubDetail:alloc")
    @RequestMapping(value = "alloc")
    public AjaxJson alloc(BanQinWmKitSubDetail subDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.alloc(subDetail.getKitNo(), Arrays.asList(subDetail.getSubLineNo().split(",")), subDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitSubDetail:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(BanQinWmKitSubDetail subDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.pickingBySub(ProcessByCode.BY_KIT_SUB.getCode(), subDetail.getKitNo(), Arrays.asList(subDetail.getSubLineNo().split(",")), subDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitSubDetail:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(BanQinWmKitSubDetail subDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.cancelAllocBySub(ProcessByCode.BY_KIT_SUB.getCode(), subDetail.getKitNo(), Arrays.asList(subDetail.getSubLineNo().split(",")), subDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("kit:banQinWmKitSubDetail:cancelPicking")
    @RequestMapping(value = "cancelPicking")
    public AjaxJson cancelPicking(BanQinWmKitSubDetail subDetail) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinKitService.cancelPickingBySub(ProcessByCode.BY_KIT_SUB.getCode(), subDetail.getKitNo(), Arrays.asList(subDetail.getSubLineNo().split(",")), subDetail.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}