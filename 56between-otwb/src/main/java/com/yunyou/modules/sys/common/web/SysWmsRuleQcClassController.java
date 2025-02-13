package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcClass;
import com.yunyou.modules.sys.common.service.SysWmsRuleQcClassService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 质检规则级差明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleQcClass")
public class SysWmsRuleQcClassController extends BaseController {
    @Autowired
    private SysWmsRuleQcClassService sysWmsRuleQcClassService;

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleQcClass entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleQcClassService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleQc:class:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleQcClass sysWmsRuleQcClass) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            sysWmsRuleQcClassService.save(sysWmsRuleQcClass);
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
    @RequiresPermissions("sys:common:wms:ruleQc:class:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleQcClassService.delete(sysWmsRuleQcClassService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}