package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRulePaDetail;
import com.yunyou.modules.sys.common.service.SysWmsRulePaDetailService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 上架规则明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/rulePaDetail")
public class SysWmsRulePaDetailController extends BaseController {
    @Autowired
    private SysWmsRulePaDetailService sysWmsRulePaDetailService;

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRulePaDetail entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRulePaDetailService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:rulePa:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRulePaDetail sysWmsRulePaDetail, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysWmsRulePaDetail)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysWmsRulePaDetailService.save(sysWmsRulePaDetail);
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
    @RequiresPermissions("sys:common:wms:rulePa:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRulePaDetailService.delete(sysWmsRulePaDetailService.get(id));
        }
        j.setMsg("删除成功！");
        return j;
    }

}