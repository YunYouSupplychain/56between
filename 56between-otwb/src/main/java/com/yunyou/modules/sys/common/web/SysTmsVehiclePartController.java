package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysTmsVehiclePart;
import com.yunyou.modules.sys.common.service.SysTmsVehiclePartService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 车辆配件Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/vehiclePart")
public class SysTmsVehiclePartController extends BaseController {
    @Autowired
    private SysTmsVehiclePartService sysTmsVehiclePartService;

    @ModelAttribute
    public SysTmsVehiclePart get(@RequestParam(required = false) String id) {
        SysTmsVehiclePart entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsVehiclePartService.get(id);
        }
        if (entity == null) {
            entity = new SysTmsVehiclePart();
        }
        return entity;
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehiclePart:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsVehiclePart entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsVehiclePartService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:vehiclePart:add", "sys:common:tms:vehiclePart:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsVehiclePart sysTmsVehiclePart, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsVehiclePart)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsVehiclePartService.saveValidator(sysTmsVehiclePart);
            sysTmsVehiclePartService.save(sysTmsVehiclePart);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehiclePart:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysTmsVehiclePartService.delete(sysTmsVehiclePartService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}