package com.yunyou.modules.wms.inventory.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountEntity;
import com.yunyou.modules.wms.inventory.entity.BanQinWmTaskCountSerialEntity;
import com.yunyou.modules.wms.inventory.service.BanQinWmCountHeaderService;
import com.yunyou.modules.wms.inventory.service.BanQinWmTaskCountSerialService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 序列号盘点任务Controller
 *
 * @author WMJ
 * @version 2019-01-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/inventory/banQinWmTaskCountSerial")
public class BanQinWmTaskCountSerialController extends BaseController {
    @Autowired
    private BanQinWmTaskCountSerialService banQinWmTaskCountSerialService;
    @Autowired
    private BanQinWmCountHeaderService banQinWmCountHeaderService;

    /**
     * 序列号盘点任务列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmTaskCountSerialEntity entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmTaskCountSerialEntity> page = banQinWmTaskCountSerialService.findPage(new Page<BanQinWmTaskCountSerialEntity>(request, response), entity);
        return getBootstrapData(page);
    }

    /**
     * 序列号盘点确认
     * @param list
     * @return
     */
    @ResponseBody
    @RequiresPermissions("inventory:banQinWmTaskCountSerial:confirmDetail")
    @RequestMapping(value = "confirmDetail")
    public AjaxJson confirmSerialCount(@RequestBody List<BanQinWmTaskCountEntity> list) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmCountHeaderService.confirmSerialCount(list);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘点确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 序列号盘点确认
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scanSerialNo")
    public AjaxJson scanSerialNo(BanQinWmTaskCountSerialEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountSerialService.scanSerialNo(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘点确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 序列号盘点确认
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "batchComfirmCount")
    public AjaxJson batchComfirmCount(@RequestBody List<BanQinWmTaskCountSerialEntity> entitys) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountSerialService.batchComfirmCount(entitys);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘点确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 序列号盘亏确认
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "comfirmLoss")
    public AjaxJson comfirmLoss(BanQinWmTaskCountSerialEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountSerialService.comfirmLoss(entity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘亏确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 序列号盘亏确认
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "batchComfirmLoss")
    public AjaxJson batchComfirmLoss(@RequestBody List<BanQinWmTaskCountSerialEntity> entitys) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountSerialService.batchComfirmLoss(entitys);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘亏确认异常:" + e.getMessage());
        }
        return j;
    }

    /**
     * 序列号取消盘亏确认
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deleteSerialTask")
    public AjaxJson deleteSerialTask(@RequestBody List<BanQinWmTaskCountSerialEntity> entitys) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinWmTaskCountSerialService.deleteSerialTask(entitys);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("序列号盘亏确认异常:" + e.getMessage());
        }
        return j;
    }

}