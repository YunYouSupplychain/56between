package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRuleQcDetail;
import com.yunyou.modules.sys.common.service.SysWmsRuleQcDetailService;
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
 * 质检处理意见Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleQcDetail")
public class SysWmsRuleQcDetailController extends BaseController {
    @Autowired
    private SysWmsRuleQcDetailService sysWmsRuleQcDetailService;

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysWmsRuleQcDetail entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysWmsRuleQcDetailService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions("sys:common:wms:ruleQc:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleQcDetail sysWmsRuleQcDetail) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            sysWmsRuleQcDetailService.save(sysWmsRuleQcDetail);
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
    @RequiresPermissions("sys:common:wms:ruleQc:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleQcDetailService.delete(sysWmsRuleQcDetailService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}