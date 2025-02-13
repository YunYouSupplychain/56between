package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysTmsVehicleQualification;
import com.yunyou.modules.sys.common.service.SysTmsVehicleQualificationService;
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
 * 车辆资质信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/vehicleQualification")
public class SysTmsVehicleQualificationController extends BaseController {
    @Autowired
    private SysTmsVehicleQualificationService sysTmsVehicleQualificationService;

    @ModelAttribute
    public SysTmsVehicleQualification get(@RequestParam(required = false) String id) {
        SysTmsVehicleQualification entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsVehicleQualificationService.get(id);
        }
        if (entity == null) {
            entity = new SysTmsVehicleQualification();
        }
        return entity;
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:vehicleQualification:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsVehicleQualification entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsVehicleQualificationService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:vehicleQualification:add", "sys:common:tms:vehicleQualification:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsVehicleQualification sysTmsVehicleQualification, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsVehicleQualification)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsVehicleQualificationService.saveValidator(sysTmsVehicleQualification);
            sysTmsVehicleQualificationService.save(sysTmsVehicleQualification);
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
    @RequiresPermissions("sys:common:tms:vehicleQualification:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysTmsVehicleQualificationService.delete(sysTmsVehicleQualificationService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}