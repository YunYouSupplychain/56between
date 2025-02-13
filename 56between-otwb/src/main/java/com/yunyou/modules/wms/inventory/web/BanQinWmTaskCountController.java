package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmCountHeaderService;
import com.yunyou.modules.wms.inventory.service.BanQinWmTaskCountService;
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
import java.util.List;
import java.util.Map;

/**
 * 盘点任务Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmTaskCount")
public class BanQinWmTaskCountController extends BaseController {
    @Autowired
    private BanQinWmTaskCountService banQinWmTaskCountService;
    @Autowired
    private BanQinWmCountHeaderService banQinWmCountHeaderService;

    /**
     * 盘点任务列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTaskCountEntity banQinWmTaskCountEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmTaskCountEntity> page = banQinWmTaskCountService.findPage(new Page<BanQinWmTaskCountEntity>(request, response), banQinWmTaskCountEntity);
        return getBootstrapData(page);
    }

    /**
     * 批量删除盘点任务
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTaskCount:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, String headerId, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountService.removeTaskById(ids.split(","), headerId);
            j.setSuccess(msg.isSuccess());
            j.setMsg("操作成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 盘点确认
     *
     * @param list
     * @return
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTaskCount:confirmDetail")
    @RequestMapping(value = "confirmDetail")
    public AjaxJson confirmDetail(@RequestBody List<BanQinWmTaskCountEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.confirmSelectCount(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("盘点确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 盘点确认
     *
     * @param list
     * @return
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTaskCount:addConfirmCount")
    @RequestMapping(value = "addConfirmCount")
    public AjaxJson addConfirmCount(@RequestBody List<BanQinWmTaskCountEntity> list) {
        AjaxJson j = new AjaxJson();
        if (CollectionUtil.isEmpty(list)) return j;
        try {
            ResultMessage msg = banQinWmCountHeaderService.addConfirmCount(list.get(0));
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("盘点确认异常:" + e.getMessage());
        }
        return j;
    }

}